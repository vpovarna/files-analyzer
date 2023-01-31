package org.example.analyzer.authentication.resources

import cats.effect.{ IO, Resource }
import dev.profunktor.redis4cats.effect.Log.Stdout._
import dev.profunktor.redis4cats.{ Redis, RedisCommands }
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import org.example.analyzer.authentication.domain._

final case class AppResources(
  authServiceConfig: AuthServiceConfig,
  psql: HikariTransactor[IO],
  redis: RedisCommands[IO, String, String]
)

object AppResources {
  def make(
    authServiceConfig: IO[AuthServiceConfig]
  ): Resource[IO, AppResources] =
    for {
      cfg <- Resource.eval(authServiceConfig)
      psql <- mkPostgresResource(cfg.postgresConfig)
      redis <- mkRedisResource(cfg.redisConfig)
    } yield AppResources(cfg, psql, redis)

  private def mkPostgresResource(
    postgresConfig: PostgresConfig
  ): Resource[IO, HikariTransactor[IO]] = {
    for {
      ce <- ExecutionContexts.fixedThreadPool[IO](
        postgresConfig.threadPoolSize
      )
      xa <- HikariTransactor.newHikariTransactor[IO](
        driverClassName = postgresConfig.driver,
        url =
          s"jdbc:postgresql://${postgresConfig.host}:${postgresConfig.port}/${postgresConfig.databaseName}",
        user = postgresConfig.user,
        pass = postgresConfig.password,
        connectEC = ce
      )
    } yield xa
  }

  private def mkRedisResource(
    redisConfig: RedisConfig
  ): Resource[IO, RedisCommands[IO, String, String]] =
    Redis[IO].utf8(redisConfig.uri)
}
