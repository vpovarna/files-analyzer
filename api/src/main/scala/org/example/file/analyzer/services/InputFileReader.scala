package org.example.file.analyzer.services

import scala.io.Source

import cats.effect.{IO, Resource}

trait InputFileReader {
  def read(fileName: String): IO[String]
}

object InputFileReader {

  def make: InputFileReader = LiveFileService

  object LiveFileService extends InputFileReader {
    override def read(fileName: String): IO[String] = makeResourceForRead(fileName).use(readLines)

    private def readLines(source: Source): IO[String] = IO(source.getLines().mkString("\n"))
    private def makeResourceForRead(fileName: String): Resource[IO, Source] =
      Resource.make(IO(Source.fromFile(fileName)))(source => IO(source.close()))
  }

}
