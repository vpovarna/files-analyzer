package org.example.file.analyzer

import scala.concurrent.ExecutionContext

import cats.effect.{ExitCode, IO, IOApp}
import org.example.file.analyzer.modules.HttpApi
import org.example.file.analyzer.resources.AppResources
import org.http4s.server.blaze.BlazeServerBuilder
import org.typelevel.log4cats.{Logger, SelfAwareStructuredLogger}
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp {
  implicit val unsafeLogger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  override def run(args: List[String]): IO[ExitCode] =
    config.FileAnalyzerConfig.load().flatMap { config =>
      Logger[IO].info(s"Loaded config $config") >>
        AppResources.make(config).use { resources =>
          val httpApp = HttpApi.make(config.kafka.topicName, resources.kafkaProducer)

          BlazeServerBuilder[IO](ExecutionContext.global)
            .bindHttp(
              host = config.server.host,
              port = config.server.port
            )
            .withHttpApp(httpApp)
            .serve
            .compile
            .drain
            .as(ExitCode.Success)
        }
    }

}
