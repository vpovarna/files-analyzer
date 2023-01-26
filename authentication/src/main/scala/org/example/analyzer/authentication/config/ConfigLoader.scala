package org.example.analyzer.authentication.config

import cats.effect.kernel.Async
import com.typesafe.config.ConfigFactory
import pureconfig.ConfigSource
import pureconfig.module.catseffect.syntax._
import pureconfig.generic.auto._

object ConfigLoader {

  def loader[F[_]: Async](
    configFile: String = "application.conf"
  ): F[AuthenticationServiceConfig] =
    ConfigSource
      .fromConfig(ConfigFactory.load(configFile))
      .loadF[F, AuthenticationServiceConfig]()

}
