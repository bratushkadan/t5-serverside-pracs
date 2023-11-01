package util

import (
	"os"
)

func GetenvDefault(key string, def string) string {
	val := os.Getenv(key)
	if val == "" {
		return def
	}
	return val
}
