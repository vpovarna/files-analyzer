package org.example.analyzer.authentication.domain

object HealthCheckDomain {
  final case class PostgresStatus(status: Boolean)
  final case class RedisStatus(status: Boolean)
  final case class HealthCheckResponse(status: Boolean)

  final case class AuthServiceHealthStatus(
    postgresStatus: PostgresStatus,
    redisStatus: RedisStatus
  )

  final case class AuthServiceHealthStatus2(
    postgresStatus: Boolean = true,
    redisStatus: Int = 10
  )
}
