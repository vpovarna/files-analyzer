package org.example.analyzer.authentication.algebra

import cats.effect.IO
import org.example.analyzer.authentication.domain.HealthCheckDomain.AuthServiceHealthStatus

trait HealthCheckService {
  def status: IO[AuthServiceHealthStatus]
}
