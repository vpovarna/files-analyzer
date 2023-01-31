package org.example.analyzer.authentication.routes

import cats.effect.IO
import cats.syntax.all._
import org.example.analyzer.authentication.algebra.HealthCheckService
import org.http4s.HttpRoutes
import org.http4s.implicits._
import org.http4s.server.Router

object HttpApi {

  def make(healthCheckService: HealthCheckService) = {
    val healthRoute: HttpRoutes[IO] = new HealthRoute().routes
    val healthDetailedRoute: HttpRoutes[IO] = new HealthDetailedRoute(
      healthCheckService
    ).routes

    val healthRoutes: HttpRoutes[IO] =
      healthRoute <+> healthDetailedRoute

    Router(v1 -> healthRoutes).orNotFound
  }
}
