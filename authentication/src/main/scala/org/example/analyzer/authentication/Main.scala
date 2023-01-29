package org.example.analyzer.authentication

import cats.effect.{ ExitCode, IO, IOApp }

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = HttpServer.create()
}
