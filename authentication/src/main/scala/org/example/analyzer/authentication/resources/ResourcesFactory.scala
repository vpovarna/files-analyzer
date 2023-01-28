package org.example.analyzer.authentication.resources

import cats.Applicative
import cats.effect.Resource
import cats.effect.kernel.Async
import cats.implicits.catsSyntaxApplicativeId
import org.example.analyzer.authentication.config.ConfigLoader
import org.example.analyzer.authentication.config.domain.AuthServiceConfig

object ResourceFactory {
  def getResources[F[_]: Async](configFile: String): Resource[F, Resources] =
    resources(ConfigLoader.loader(configFile))

  def getResources[F[_]: Applicative](
    config: AuthServiceConfig
  ): Resource[F, Resources] =
    resources(config.pure)

  private def resources[F[_]](
    config: F[AuthServiceConfig]
  ): Resource[F, Resources] =
    for {
      config <- Resource.eval(config)
    } yield Resources(config)

  final case class Resources(config: AuthServiceConfig)
}
