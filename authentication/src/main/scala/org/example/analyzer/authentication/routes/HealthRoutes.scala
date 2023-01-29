package org.example.analyzer.authentication.routes

import cats.effect.IO
import io.circe.Encoder
import io.circe.syntax.EncoderOps
import org.example.analyzer.authentication.domain.HealthCheckDomain.HealthCheckResponse
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.http4s.circe._

final class HealthRoutes() extends Http4sDsl[IO] {

  private implicit val encoderImportance: Encoder[HealthCheckResponse] =
    Encoder.encodeBoolean.contramap[HealthCheckResponse](_.status)

  private[routes] val prefixPath = "/health"

  private val httpRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root =>
      Ok(HealthCheckResponse(true).asJson)
  }

  val routes: HttpRoutes[IO] = Router(
    prefixPath -> httpRoutes
  )

}
