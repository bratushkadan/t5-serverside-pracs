package server

import (
	"github.com/gin-gonic/gin"
)

type GinAppOpts struct {
	CookieName string
}

func App(opts GinAppOpts) *gin.Engine {
	r := gin.Default()

	r.Use(func(c *gin.Context) {
		c.Set("cookieName", opts.CookieName)
	})

	r.GET("/ping", HandlePingRequest)
	r.GET("/response", GetResponseFromCookie)
	r.POST("/request", HandleRequest)

	r.GET("/loop", LoopHandler)
	r.GET("/loop-parallel", LoopParallelHandler)

	filesApiRouter := r.Group("/files")

	filesApiRouter.GET("/:id", ValidateFileIdMiddleware, GetFile)
	filesApiRouter.GET("/:id/info", ValidateFileIdMiddleware, GetFileInfo)
	filesApiRouter.GET("/", GetFiles)
	filesApiRouter.POST("/", AddFile)
	// filesApiRouter.PATCH("/:id", ValidateFileIdMiddleware, UpdateFile)
	filesApiRouter.DELETE("/:id", ValidateFileIdMiddleware, DeleteFile)

	r.Use(func(c *gin.Context) {
		c.JSON(404, gin.H{"error": "not found"})
	})

	return r
}
