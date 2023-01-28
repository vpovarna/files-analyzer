package org.example.analyzer.authentication

import cats.effect.{ IO, IOApp }
import org.example.analyzer.authentication.config.Loader

object Main extends IOApp.Simple {
  override def run: IO[Unit] =
    Loader.apply("application.conf").debug().void
}
