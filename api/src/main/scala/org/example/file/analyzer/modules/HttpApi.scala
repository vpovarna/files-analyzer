package org.example.file.analyzer.modules

import scala.concurrent.duration.DurationInt

import cats.data.Kleisli
import cats.effect.IO
import cats.syntax.all._
import fs2.kafka.KafkaProducer
import org.example.file.analyzer.http.Version
import org.example.file.analyzer.http.routes.{FileRoutes, HealthRoutes}
import org.example.file.analyzer.services.{WorkItemSender, InputFileReader}
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router
import org.http4s.server.middleware.{AutoSlash, Timeout}
import org.http4s.{HttpRoutes, Request, Response}
import org.typelevel.log4cats.Logger

object HttpApi {
  def make(
      topicName: String,
      kafkaProducer: KafkaProducer[IO, String, Array[Byte]]
  )(implicit
      logger: Logger[IO]
  ): Kleisli[IO, Request[IO], Response[IO]] = {
    val fileService    = InputFileReader.make
    val workItemSender = WorkItemSender.make(topicName, kafkaProducer)

    val httpRoutes = new HealthRoutes().routes
    val fileRoutes = new FileRoutes(fileService, workItemSender).routes

    val allRoutes: HttpRoutes[IO] = httpRoutes <+> fileRoutes

    val routes = Router(
      Version.v1 -> allRoutes
    )

    val middleware: HttpRoutes[IO] => HttpRoutes[IO] = { (http: HttpRoutes[IO]) =>
      AutoSlash(http)
    }.andThen { (http: HttpRoutes[IO]) =>
      Timeout(60.seconds)(http)
    }

    // TODO: Add middleware logger

    middleware(routes).orNotFound
  }
}
