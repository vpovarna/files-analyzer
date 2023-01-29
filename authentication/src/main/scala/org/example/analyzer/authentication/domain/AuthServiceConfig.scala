package org.example.analyzer.authentication.domain

case class HttpServerConfig(address: String, port: Int)

case class DatabaseConfig(
  driver: String,
  host: String,
  port: Int,
  databaseName: String,
  user: String,
  password: String,
  threadPoolSize: Int
)

case class AuthServiceConfig(
  databaseConfig: DatabaseConfig,
  httpServerConfig: HttpServerConfig
)
