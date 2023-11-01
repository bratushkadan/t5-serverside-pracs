package server

import (
	"context"
	"fmt"
	"log"
	"pr9/internal/util"

	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/gridfs"
	"go.mongodb.org/mongo-driver/mongo/options"
)

var Client *mongo.Client
var Bucket *gridfs.Bucket
var Ctx = context.TODO()

var (
	MONGO_HOST = util.GetenvDefault("MONGODB_HOST", "localhost")
	MONGO_PORT = util.GetenvDefault("MONGO_PORT", "27017")
	MONGO_DB   = util.GetenvDefault("MONGO_DB", "appDB")
)

func init() {
	clientOptions := options.
		Client().
		SetTLSConfig(nil).SetAuth(options.Credential{
		Username: "root",
		Password: "example",
	}).
		ApplyURI(fmt.Sprintf("mongodb://%s:%s/%s", MONGO_HOST, MONGO_PORT, MONGO_DB))
	client, err := mongo.Connect(Ctx, clientOptions)
	if err != nil {
		log.Fatal(err)
	}

	err = client.Ping(Ctx, nil)
	if err != nil {
		log.Fatal(err)
	}

	db := client.Database("appDB")
	var bucketName string = "files"
	Bucket, err = gridfs.NewBucket(db, &options.BucketOptions{
		Name: &bucketName,
	})
	if err != nil {
		log.Fatal(err)
	}

	log.Println("Connected to MongoDB!")
}
