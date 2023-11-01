package main

import (
	"pr9/internal/util"
	"strconv"
)

type Opts struct {
	Host string
	Port int

	CookieName string
}

var AppOpts Opts

func init() {
	AppOpts = getOpts()
}

func getOpts() Opts {
	host := util.GetenvDefault("APP_HOST", "localhost")
	envPort := util.GetenvDefault("APP_PORT", "8080")
	port, err := strconv.Atoi(envPort)
	if err != nil {
		panic(err)
	}

	cookieName := util.GetenvDefault("APP_COOKIE_NAME", "response")

	return Opts{
		Host:       host,
		Port:       port,
		CookieName: cookieName,
	}
}
