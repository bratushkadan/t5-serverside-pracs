package main

import (
	"fmt"
	"pr9/internal/server"
)

func main() {

	// server.App(server.GinAppOpts{CookieName: AppOpts.CookieName}).Run(fmt.Sprintf("localhost:%d", AppOpts.Port))
	server.App(
		server.
			GinAppOpts{CookieName: AppOpts.CookieName}).
		Run(fmt.Sprintf(":%d", AppOpts.Port))
}
