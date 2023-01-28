package org.example.analyzer.authentication.config

object domain {
  case class HttpServerConfig(address: String, port: Int)

  case class AuthenticationDatabaseConfig(
    driver: String,
    host: String,
    port: Int,
    databaseName: String,
    user: String,
    password: String,
    threadPoolSize: Int
  )

  case class AuthServiceConfig(
    databaseConfig: AuthenticationDatabaseConfig,
    httpServerConfig: HttpServerConfig
  )
}
