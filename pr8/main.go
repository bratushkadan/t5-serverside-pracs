package main

import (
	"fmt"
	"pr8/internal/server"
)

func main() {

	server.App(server.GinAppOpts{CookieName: AppOpts.CookieName}).Run(fmt.Sprintf(":%d", AppOpts.Port))
}
