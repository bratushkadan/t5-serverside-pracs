package server

import (
	"bytes"
	"context"
	"fmt"
	"io"
	"net/http"
	"pr9/internal/logger"
	"strconv"

	"github.com/gin-gonic/gin"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
)

func ValidateFileIdMiddleware(c *gin.Context) {
	id := c.Param("id")
	fmt.Printf(`id="%s"`, id)
	if id == "" {
		c.JSON(http.StatusBadRequest, gin.H{"error": "path parameter \"id\" can't be empty"})
		return
	}
}

func GetFiles(c *gin.Context) {
	cursor, err := Bucket.GetFilesCollection().Find(context.Background(), bson.D{{}})
	if err != nil {
		logger.Logger.Error(err)
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to query files"})
		return
	}

	filesInfo := []FileInfo{}
	err = cursor.All(Ctx, &filesInfo)
	if err != nil {
		logger.Logger.Error(err)
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to read files"})
		return
	}

	c.JSON(http.StatusOK, filesInfo)
}

func GetFile(c *gin.Context) {
	id := c.Param("id")
	mongoId, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		logger.Logger.Error(err)
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Broken id."})
		return
	}

	result := Bucket.GetFilesCollection().FindOne(context.Background(), mongoId)
	if result == nil {
		c.JSON(http.StatusNotFound, gin.H{"error": fmt.Sprintf(`file id="%s" not found`, id)})
		return
	}

	fileBuffer := bytes.NewBuffer(nil)

	stream, err := Bucket.OpenDownloadStream(mongoId)
	if err != nil {
		logger.Logger.Error(err)
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to download file"})
		return
	}
	defer stream.Close()

	file := stream.GetFile()

	io.Copy(fileBuffer, stream)

	c.Header("Content-Disposition", fmt.Sprintf(`attachment; filename="%s"`, file.Name))
	c.Header("Content-Length", strconv.Itoa(int(file.Length)))

	c.Writer.Write(fileBuffer.Bytes())
}

func GetFileInfo(c *gin.Context) {
	id := c.Param("id")
	objectID, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		logger.Logger.Error(err)
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid file ID format"})
		return
	}

	fileResult := Bucket.GetFilesCollection().FindOne(context.Background(), bson.M{"_id": objectID})
	if fileResult == nil {
		logger.Logger.Error(err)
		c.JSON(http.StatusNotFound, gin.H{"error": "File not found"})
		return
	}

	var fileInfo FileInfo

	if err := fileResult.Decode(&fileInfo); err != nil {
		logger.Logger.Error(err)
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	err = fileResult.Decode(&fileInfo)
	if err != nil {
		logger.Logger.Error(err)
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to decode file info"})
		return
	}

	c.JSON(http.StatusOK, fileInfo)
}

func AddFile(c *gin.Context) {
	file, err := c.FormFile("file")
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "No file attached"})
		return
	}

	fileStream, err := file.Open()
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to open file"})
		return
	}

	uploadStream, err := Bucket.OpenUploadStream(file.Filename)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to open upload stream"})
		return
	}

	defer uploadStream.Close()

	_, err = io.Copy(uploadStream, fileStream)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to upload file"})
		return
	}

	c.JSON(http.StatusOK, gin.H{"message": "File uploaded", "file_id": uploadStream.FileID})
}

/* func UpdateFile(c *gin.Context) {
	id := c.Param("id")

	file, err := c.FormFile("file")
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "No file attached"})
		return
	}

	fileStream, err := file.Open()
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to open file"})
		return
	}
	objectID, err := primitive.ObjectIDFromHex(id)

	var existingFile FileInfo
	res := Bucket.GetFilesCollection().FindOne(context.Background(), bson.M{
		"_id": objectID,
	})
	if res == nil {
		logger.Logger.Error(err)
		c.JSON(http.StatusNotFound, gin.H{"error": "File not found"})
		return
	}

	err = res.Decode(existingFile)
	if err != nil {
		logger.Logger.Error(err)
		c.JSON(http.StatusNotFound, gin.H{"error": "Updating file error."})
		return
	}

	// Open a new GridFS upload stream with the file ID
	uploadStream, err := Bucket.OpenUploadStreamWithID(existingFile.ID, file.Filename)
	if err != nil {
		fmt.Println(err)
		return
	}
	defer uploadStream.Close()

	_, err = io.Copy(uploadStream, fileStream)
	if err != nil {
		logger.Logger.Error(err)
		c.JSON(http.StatusNotFound, gin.H{"error": "Error updating file."})
		return
	}

	c.JSON(http.StatusOK, gin.H{"message": "File updated"})
} */

func DeleteFile(c *gin.Context) {
	id := c.Param("id")
	objectID, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		logger.Logger.Error(err)
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid file ID format"})
		return
	}

	err = Bucket.Delete(objectID)
	if err != nil {
		logger.Logger.Error(err)
		c.JSON(http.StatusNotFound, gin.H{"error": "File not found"})
		return
	}

	c.JSON(http.StatusOK, gin.H{"message": "File deleted"})
}
