package org.example.analyzer.authentication.routes

import cats.data.Kleisli
import cats.effect.IO
import org.http4s.{ Request, Response }
import org.http4s.implicits._
import org.http4s.server.Router

final class HttpApis {
  private val healthRoutes = new HealthRoutes()

  val routes: Kleisli[IO, Request[IO], Response[IO]] = Router(
    v1 -> healthRoutes.routes
  ).orNotFound
}
