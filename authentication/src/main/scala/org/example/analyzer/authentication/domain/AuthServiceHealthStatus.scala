package org.example.analyzer.authentication.domain

final case class PostgresStatus(status: Boolean)
final case class RedisStatus(status: Boolean)
final case class HealthCheckResponse(status: Boolean)

final case class AuthServiceHealthStatus(
  postgresStatus: PostgresStatus,
  redisStatus: RedisStatus
)
