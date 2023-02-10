package org.example.file.analyzer.services

import cats.effect.IO
import fs2.kafka.KafkaProducer
import io.circe.generic.auto._
import io.circe.syntax._
import org.apache.kafka.clients.producer.RecordMetadata
import org.example.file.analyzer.domain.WorkItem
import org.typelevel.log4cats.Logger

trait WorkItemSender {
  def send(workItem: WorkItem): IO[RecordMetadata]
}

object WorkItemSender {

  def make(
      topic: String,
      kafkaProducer: KafkaProducer[IO, String, Array[Byte]]
  )(implicit logger: Logger[IO]): WorkItemSender = new LiveWorkItemSender(topic, kafkaProducer)

  final class LiveWorkItemSender(
      topicName: String,
      kafkaProducer: KafkaProducer[IO, String, Array[Byte]]
  )(implicit
      logger: Logger[IO]
  ) extends WorkItemSender {
    override def send(workItem: WorkItem): IO[RecordMetadata] = {
      for {
        _ <- Logger[IO].info(s"Received workItem: $workItem")
        recordMetadata <- kafkaProducer
          .produceOne_(topicName, workItem.id, workItem.asJson.noSpaces.getBytes())
          .flatten
          .flatTap(recordMetadata =>
            Logger[IO].info(s"Publish work item : $workItem. Kafka metadata: $recordMetadata}")
          )
        _ <- Logger[IO].info(s"Record metadata is: $recordMetadata")
      } yield recordMetadata
    }
  }

}
