package org.example.analyzer.authentication.resources

// TODO: Create an execution context dedicated for https? 
import scala.concurrent.ExecutionContext
import cats.Applicative
import cats.effect.Resource
import doobie.util.ExecutionContexts
import cats.effect.kernel.Async
import cats.implicits.catsSyntaxApplicativeId
import org.example.analyzer.authentication.config.domain._
import cats.effect.IO
import org.example.analyzer.authentication.config.domain
import doobie.hikari.HikariTransactor
import org.http4s.server.blaze.BlazeServerBuilder

final case class AppResources(psql: HikariTransactor[IO])

object AppResources {
  def make(cfg: AuthServiceConfig): Resource[IO, AppResources] =
    for {
      psql <- mkPostgreSQLResource(cfg.databaseConfig)
    } yield AppResources(psql)

    def mkPostgreSQLResource(
      databaseConfig: AuthenticationDatabaseConfig
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
