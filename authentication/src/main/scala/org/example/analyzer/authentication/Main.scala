package org.example.analyzer.authentication

import cats.effect.{ IO, IOApp }
import org.example.analyzer.authentication.config.ConfigLoader

object Main extends IOApp.Simple {
  override def run: IO[Unit] =
    ConfigLoader.loader[IO]("application.conf").debug().void
}
