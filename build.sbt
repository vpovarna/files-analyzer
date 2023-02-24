ThisBuild / scalaVersion := "2.13.8"
ThisBuild / version      := "0.1.0"
ThisBuild / organization := "com.example.analyzer"

lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-deprecation"
)

val catsEffectVersion = "3.4.6"
val catsVersion       = "2.9.0"
val fs2KafkaVersion   = "2.5.0"
val circeVersion      = "0.14.4"
val http4sVersion     = "1.0.0-M21"
val pureConfigVersion = "0.17.2"
val log4catsVersion   = "2.5.0"
val logbackVersion    = "1.4.5"

val commonDeps = Seq(
  "org.typelevel"         %% "cats-effect"            % catsEffectVersion,
  "org.typelevel"         %% "cats-core"              % catsVersion,
  "io.circe"              %% "circe-generic"          % circeVersion,
  "io.circe"              %% "circe-parser"           % circeVersion,
  "com.github.pureconfig" %% "pureconfig"             % pureConfigVersion,
  "com.github.pureconfig" %% "pureconfig-cats-effect" % pureConfigVersion,
  "org.typelevel"         %% "log4cats-core"          % log4catsVersion,
  "org.typelevel"         %% "log4cats-slf4j"         % log4catsVersion,
  "ch.qos.logback"         % "logback-classic"        % logbackVersion
)

lazy val commons = project
  .in(file("commons"))
  .settings(
    name := "commons"
  )

lazy val worker = project
  .in(file("worker"))
  .settings(
    name := "worker",
    libraryDependencies ++= commonDeps
  )
  .dependsOn(commons)

lazy val api = project
  .in(file("api"))
  .settings(
    name := "api",
    libraryDependencies ++= commonDeps ++ List(
      "com.github.fd4s" %% "fs2-kafka"           % fs2KafkaVersion,
      "org.http4s"      %% "http4s-blaze-server" % http4sVersion,
      "org.http4s"      %% "http4s-circe"        % http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % http4sVersion,
      "org.http4s"      %% "http4s-blaze-client" % http4sVersion
    )
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
    api
  )
