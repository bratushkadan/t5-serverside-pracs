package server

import "github.com/gin-gonic/gin"

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

	return r
}
