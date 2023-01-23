ThisBuild / scalaVersion := "3.2.1"
ThisBuild / version := "0.1.0"
ThisBuild / organization := "com.example.analyzer"

lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-deprecation"
)

val catsEffectVersion = "3.4.5"
val catsVersion = "2.9.0"

val commonDeps = Seq(
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
  "org.typelevel" %% "cats-core" % catsVersion,
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
    ).dependsOn(commons)

lazy val injector = project
    .in(file("injector"))
    .settings(
        name:= "injector"
    ).dependsOn(commons)  

lazy val api = project
    .in(file("api"))
    .settings(
        name:="api"
    ).dependsOn(commons)

lazy val root = project
    .in(file("."))
    .settings(
      name := "files-analyzer",
      publish / skip := true
    )
    .settings(
      scalacOptions ++= compilerOptions
    )
    .aggregate(
      worker,
      injector,
      api
    )