package org.example.analyzer.authentication.domain

case class HttpServerConfig(address: String, port: Int)

final case class PostgresConfig(
  driver: String,
  host: String,
  port: Int,
  databaseName: String,
  user: String,
  password: String,
  threadPoolSize: Int
)

final case class RedisConfig(uri: String)

final case class AuthServiceConfig(
  postgresConfig: PostgresConfig,
  redisConfig: RedisConfig,
  httpServerConfig: HttpServerConfig
)
