package org.example.file.analyzer.http

import cats.MonadThrow
import cats.syntax.all._
import io.circe.DecodingFailure
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, Request, Response}
import org.typelevel.log4cats.Logger

object decoder {
  implicit class DecoderClass[F[_]: MonadThrow: Logger](req: Request[F]) {
    def decoderR[A](f: A => F[Response[F]])(implicit entityDecoder: EntityDecoder[F, A]): F[Response[F]] = {
      val dsl = Http4sDsl[F]
      import dsl._

      req.as[A].attempt.flatMap {
        case Left(e) =>
          Logger[F].error(s"Failed to decode request with error: $e")
          e.getCause match {
            case d: DecodingFailure => BadRequest(d.show)
            case _                  => UnprocessableEntity(e.toString)
          }
        case Right(a) => f(a)
      }
    }
  }
}
