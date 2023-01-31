package org.example.analyzer.authentication

import scala.concurrent.ExecutionContext

import cats.data.Kleisli
import cats.effect.{ ExitCode, IO }
import org.example.analyzer.authentication.config.ConfigLoader
import org.example.analyzer.authentication.resources.AppResources
import org.example.analyzer.authentication.routes.HttpApi
import org.example.analyzer.authentication.service.HealthCheckServiceImpl
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.{ Request, Response }

object HttpServer {

  def create(configFile: String = "application.conf"): IO[ExitCode] = {
    AppResources.make(ConfigLoader(configFile)).use(createHttpServer)
  }

  private def createHttpServer(resources: AppResources): IO[ExitCode] =
    BlazeServerBuilder[IO](ExecutionContext.global)
      .bindHttp(
        port = resources.authServiceConfig.httpServerConfig.port,
        host = resources.authServiceConfig.httpServerConfig.address
      )
      .withHttpApp(httpApp = getHttpRoutes(resources))
      .serve
      .compile
      .drain
      .as(ExitCode.Success)

  private def getHttpRoutes(
    appResources: AppResources
  ): Kleisli[IO, Request[IO], Response[IO]] = {
    val healthCheckService =
      HealthCheckServiceImpl.make(appResources.psql, appResources.redis)
    HttpApi.make(healthCheckService)
  }
}
