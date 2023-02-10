package org.example.file.analyzer

package object domain {
  final case class HealthStatus(status: Boolean)
  final case class InputFile(path: String, fileName: String)
  final case class WorkItem(id: String, test: String)
}
