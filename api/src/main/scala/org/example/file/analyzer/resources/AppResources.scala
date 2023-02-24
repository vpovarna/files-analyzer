package org.example.file.analyzer.resources

import cats.effect.{IO, Resource}
import fs2.kafka.{KafkaProducer, ProducerSettings}
import org.example.file.analyzer.config.{FileAnalyzerConfig, KafkaConfig}
import org.typelevel.log4cats.Logger

final case class AppResources(
    kafkaProducer: KafkaProducer[IO, String, Array[Byte]]
)

object AppResources {

  def make(config: FileAnalyzerConfig)(implicit logger: Logger[IO]): Resource[IO, AppResources] =
    for {
      kafkaProducer <- buildKafkaProducer(config.kafka)
    } yield AppResources(kafkaProducer)

  private def buildKafkaProducer(
      kafkaConfig: KafkaConfig
  )(implicit logger: Logger[IO]): Resource[IO, KafkaProducer.Metrics[IO, String, Array[Byte]]] = {
    val producerSettings = ProducerSettings[IO, String, Array[Byte]]
      .withBootstrapServers(kafkaConfig.bootstrapServers)

    KafkaProducer
      .resource(producerSettings)
      .evalTap(_ => Logger[IO].info("Started Kafka Producer"))
      .onFinalize(Logger[IO].info("Shutting down Kafka producer"))
  }

}
