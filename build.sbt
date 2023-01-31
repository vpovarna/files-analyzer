ThisBuild / scalaVersion := "2.13.8"
ThisBuild / version      := "0.1.0"
ThisBuild / organization := "com.example.analyzer"

lazy val compilerOptions = Seq(
  "-deprecation",
  "-language:higherKinds",
  "-Xfatal-warnings",
  "-Ywarn-value-discard",
  "-Xlint:missing-interpolator"
)

lazy val catsEffectVersion = "3.4.5"
lazy val catsVersion       = "2.9.0"
lazy val pureConfigVersion = "0.17.2"
lazy val doobieVersion     = "1.0.0-RC1"
lazy val http4sVersion     = "1.0.0-M21"
lazy val redis4cats        = "1.4.0"
lazy val circeVersion      = "0.14.3"
lazy val logbackVersion    = "1.2.11"

val commonDeps = Seq(
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
  "org.typelevel" %% "cats-core"   % catsVersion,
  // pureconfig
  "com.github.pureconfig" %% "pureconfig"             % pureConfigVersion,
  "com.github.pureconfig" %% "pureconfig-cats-effect" % pureConfigVersion,
  // circe
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-core"    % circeVersion,
  "io.circe" %% "circe-parser"  % circeVersion,

  // Metrics
  "org.http4s" %% "http4s-prometheus-metrics" % http4sVersion,

  // Logs
  "ch.qos.logback" % "logback-classic" % logbackVersion
)

lazy val commons = project
  .in(file("commons"))
  .settings(
    name := "commons"
  )

lazy val worker = project
  .in(file("worker"))
  .settings(
    name := "worker"
  )
  .dependsOn(commons)

lazy val authentication = project
  .in(file("authentication"))
  .settings(
    name := "authentication",
    libraryDependencies ++= commonDeps ++ Seq(
      // Repository Postgres
      "org.tpolecat" %% "doobie-core"     % doobieVersion,
      "org.tpolecat" %% "doobie-postgres" % doobieVersion,
      "org.tpolecat" %% "doobie-hikari"   % doobieVersion,

      // Http4s libs
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-circe"        % http4sVersion,
      "org.http4s" %% "http4s-dsl"          % http4sVersion,

      // Redis
      "dev.profunktor" %% "redis4cats-effects"  % redis4cats,
      "dev.profunktor" %% "redis4cats-log4cats" % redis4cats
    )
  )
  .dependsOn(commons)

lazy val api = project
  .in(file("api"))
  .settings(
    name := "api"
  )
  .dependsOn(commons)

lazy val root = project
  .in(file("."))
  .settings(
    name           := "files-analyzer",
    publish / skip := true
  )
  .settings(
    scalacOptions ++= compilerOptions
  )
  .aggregate(
    worker,
    authentication,
    api
  )
