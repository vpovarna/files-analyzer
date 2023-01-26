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

val commonDeps = Seq(
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
  "org.typelevel" %% "cats-core"   % catsVersion,
  // pureconfig
  "com.github.pureconfig" %% "pureconfig"             % pureConfigVersion,
  "com.github.pureconfig" %% "pureconfig-cats-effect" % pureConfigVersion
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
    libraryDependencies ++= commonDeps
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
