package org.example.analyzer.authentication.resources

import cats.effect.{ IO, Resource }
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import org.example.analyzer.authentication.domain._

final case class AppResources(
  authServiceConfig: AuthServiceConfig,
  psql: HikariTransactor[IO]
)

object AppResources {
  def make(
    authServiceConfig: IO[AuthServiceConfig]
  ): Resource[IO, AppResources] =
    for {
      cfg  <- Resource.eval(authServiceConfig)
      psql <- mkPostgreSQLResource(cfg.databaseConfig)
    } yield AppResources(cfg, psql)

  private def mkPostgreSQLResource(
    databaseConfig: DatabaseConfig
  ): Resource[IO, HikariTransactor[IO]] = {
    for {
      ce <- ExecutionContexts.fixedThreadPool[IO](
        databaseConfig.threadPoolSize
      )
      xa <- HikariTransactor.newHikariTransactor[IO](
        driverClassName = databaseConfig.driver,
        url =
          s"jdbc:postgresql://${databaseConfig.host}:${databaseConfig.port}/${databaseConfig.databaseName}",
        user = databaseConfig.user,
        pass = databaseConfig.password,
        connectEC = ce
      )
    } yield xa
  }
}
