package org.example.file.analyzer.http.routes

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import org.example.file.analyzer.domain.HealthStatus
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

final class HealthRoutes extends Http4sDsl[IO] {
  private[routes] val prefixPath = "/health"

  private val httpRoute = HttpRoutes.of[IO] { case GET -> Root => Ok(HealthStatus(true).asJson) }

  val routes: HttpRoutes[IO] = Router(
    prefixPath -> httpRoute
  )
}
