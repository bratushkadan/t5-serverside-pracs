CREATE SCHEMA IF NOT EXISTS "pr5";

CREATE TABLE IF NOT EXISTS "pr5"."product" (
  "name" VARCHAR(300) NOT NULL,
  "seller_id" VARCHAR(200) NOT NULL,
  "type" VARCHAR(192) NOT NULL,
  "price" NUMERIC(100, 2) NOT NULL,
  "details" jsonb DEFAULT '{}' :: jsonb NOT NULL
);

CREATE TABLE IF NOT EXISTS "pr5"."client" (
  "name" VARCHAR(128) NOT NULL,
  "email" VARCHAR(256) UNIQUE NOT NULL,
  "username" VARCHAR(64) UNIQUE NOT NULL,
  "password" VARCHAR(128) NOT NULL
);
