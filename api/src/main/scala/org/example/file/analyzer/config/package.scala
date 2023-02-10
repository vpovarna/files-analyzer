package org.example.file.analyzer

import cats.effect.IO
import com.typesafe.config.ConfigFactory
import pureconfig._
import pureconfig.generic.auto._
import pureconfig.module.catseffect.syntax._

package object config {
  final case class ServerConfig(host: String, port: Int)
  final case class KafkaConfig(bootstrapServers: String, topicName: String)
  final case class FileAnalyzerConfig(server: ServerConfig, kafka: KafkaConfig)

  object FileAnalyzerConfig {
    def load(configFile: String = "application.conf"): IO[FileAnalyzerConfig] =
      ConfigSource
        .fromConfig(ConfigFactory.load(configFile))
        .loadF[IO, FileAnalyzerConfig]()
  }

}
