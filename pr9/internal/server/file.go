package server

import (
	"time"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
)

type FileInfo struct {
	ID          primitive.ObjectID `bson:"_id" json:"id"`
	Length      int64              `bson:"length" json:"length"`
	ChunkSize   int32              `bson:"chunkSize" json:"chunkSize"`
	UploadDate  time.Time          `bson:"uploadDate" json:"uploadDate"`
	Md5         string             `bson:"md5" json:"md5"`
	Filename    string             `bson:"filename" json:"filename"`
	ContentType string             `bson:"contentType,omitempty" json:"contentType,omitempty"`
	Metadata    bson.Raw           `bson:"metadata,omitempty" json:"metadata,omitempty"`
}
