package org.example.analyzer.authentication.routes

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax.EncoderOps
import org.example.analyzer.authentication.domain.HealthCheckResponse
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

final class HealthRoute() extends Http4sDsl[IO] {

  private[routes] val healthPrefixPath = "/health"

  private val httpHealthRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] { case GET -> Root =>
    Ok(HealthCheckResponse(true).asJson)
  }

  val routes: HttpRoutes[IO] = Router(
    healthPrefixPath -> httpHealthRoutes
  )

}
