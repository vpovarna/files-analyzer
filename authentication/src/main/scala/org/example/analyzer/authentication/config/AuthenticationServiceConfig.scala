package org.example.analyzer.authentication.config

case class HttpServerConfig(address: String, port: Int)

case class AuthenticationDatabaseConfig(
  driver: String,
  host: String,
  user: String,
  password: String,
  threadPoolSize: Int
)

case class AuthenticationServiceConfig(
  databaseConfig: AuthenticationDatabaseConfig,
  httpServerConfig: HttpServerConfig
)
