package org.example.analyzer.authentication.config

import cats.effect.IO
import com.typesafe.config.ConfigFactory
import org.example.analyzer.authentication.domain.AuthServiceConfig
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import pureconfig.module.catseffect.syntax._

object ConfigLoader {

  def apply(configFile: String = "application.conf"): IO[AuthServiceConfig] =
    ConfigSource
      .fromConfig(ConfigFactory.load(configFile))
      .loadF[IO, AuthServiceConfig]
}
