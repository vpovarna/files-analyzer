package org.example.analyzer.authentication.config

import cats.effect.IO
import com.typesafe.config.ConfigFactory
import org.example.analyzer.authentication.config.domain.AuthServiceConfig
import pureconfig.ConfigSource
import pureconfig.module.catseffect.syntax._
import pureconfig.generic.auto._

object Loader {

  def apply(configFile: String = "application.conf"): IO[AuthServiceConfig] =
    ConfigSource
      .fromConfig(ConfigFactory.load(configFile))
      .loadF[IO, AuthServiceConfig]
}
