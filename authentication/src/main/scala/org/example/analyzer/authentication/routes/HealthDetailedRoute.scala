package org.example.analyzer.authentication.routes

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import org.example.analyzer.authentication.algebra.HealthCheckService
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

final class HealthDetailedRoute(healthCheckService: HealthCheckService) extends Http4sDsl[IO] {

  private[routes] val httpDetailedRoutePrefix = "/health-detailed"

  private val httpDetailedRoute = HttpRoutes.of[IO] { case GET -> Root =>
    for {
      status <- healthCheckService.status
      response <- Ok(status.asJson)
    } yield response
  }

  val routes: HttpRoutes[IO] = Router(
    httpDetailedRoutePrefix -> httpDetailedRoute
  )

}
