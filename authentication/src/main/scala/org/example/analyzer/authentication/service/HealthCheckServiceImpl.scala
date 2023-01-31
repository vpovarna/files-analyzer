package org.example.analyzer.authentication.service

import scala.concurrent.duration.DurationInt

import cats.effect._
import cats.implicits.catsSyntaxTuple2Parallel
import dev.profunktor.redis4cats.RedisCommands
import doobie.hikari.HikariTransactor
import doobie.implicits._
import org.example.analyzer.authentication.algebra.HealthCheckService
import org.example.analyzer.authentication.domain._

object HealthCheckServiceImpl {

  def make(
    transactor: HikariTransactor[IO],
    redis: RedisCommands[IO, String, String]
  ): HealthCheckService = {

    val q: doobie.ConnectionIO[Option[Int]] =
      sql"select 1"
        .query[Int]
        .option

    val postgresHealth: IO[PostgresStatus] =
      q.transact[IO](transactor)
        .map(_.nonEmpty)
        .timeout(1.second)
        .map(PostgresStatus.apply)

    val redisHealth: IO[RedisStatus] =
      redis.ping
        .map(_.nonEmpty)
        .timeout(1.second)
        .orElse(IO(false))
        .map(RedisStatus.apply)

    new HealthCheckService {
      override def status: IO[AuthServiceHealthStatus] =
        (postgresHealth, redisHealth).parMapN(AuthServiceHealthStatus.apply)
    }
  }

}
