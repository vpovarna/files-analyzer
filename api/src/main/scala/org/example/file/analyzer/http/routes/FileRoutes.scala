package org.example.file.analyzer.http.routes

import java.io.FileNotFoundException
import java.util.UUID

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import org.example.file.analyzer.domain._
import org.example.file.analyzer.http.decoder._
import org.example.file.analyzer.services.{InputFileReader, WorkItemSender}
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.typelevel.log4cats.Logger

final class FileRoutes(fileService: InputFileReader, workItemSender: WorkItemSender)(implicit val logger: Logger[IO])
    extends Http4sDsl[IO] {
  import org.http4s.circe.CirceEntityDecoder.circeEntityDecoder
  private[routes] val prefixPath = "/inject"

  private val fileRoutes = HttpRoutes.of[IO] { case req @ POST -> Root / "file" =>
    req
      .decoderR[InputFile] { inputFile =>
        for {
          _ <- Logger[IO].info(s"Received $inputFile")
          file = s"${inputFile.path}/${inputFile.fileName}"
          _           <- Logger[IO].info(s"Reading file: $file")
          fileContent <- fileService.read(file)
          workItem = WorkItem(UUID.randomUUID().toString, fileContent)
          recordMetadata <- workItemSender.send(workItem)
          _ <- Logger[IO].info(s"Successfully publish workItem: $workItem to kafka. Record Metadata: $recordMetadata")
          response <- Accepted(s"Successfully parsed input file and publish workItem: ${workItem.id}")
        } yield response
      }
      .handleErrorWith {
        case _: FileNotFoundException => BadRequest("Input file not found!".asJson)
        case _: Exception             => BadGateway("Unable to process request".asJson)
      }
  }

  val routes: HttpRoutes[IO] = Router {
    prefixPath -> fileRoutes
  }
}
