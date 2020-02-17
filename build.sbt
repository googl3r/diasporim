name := "diasporim"

version := "0.1"

ThisBuild / scalaVersion := "2.13.1"

resolvers += Resolver.sonatypeRepo("releases")

val catsVersion = "2.0.0"
val console4CatsVersion = "0.8.0"
val catsMtlVersion = "0.7.0"
val newtypeVersion = "0.4.3"
val refinedVersion = "0.9.10"
val scalazCoreVersion = "7.2.29"
val akkaActorVersion = "2.5.23"
val log4catsCoreVersion = "1.0.1"
val log4catsSlf4jVersion = "1.0.1"
val scalaticVersion = "3.1.0"
val scalatestVersion = "3.1.0"
val ScalaCheckVersion = "1.14.3"
val catsRetryVersion = "0.3.1"
val CatsVersion = "2.1.0"
val CirceVersion = "0.12.3"
val CirceGenericExVersion = "0.12.2"
val CirceConfigVersion = "0.7.0"
val DoobieVersion = "0.8.7"
val FlywayVersion = "6.1.3"
val mysqlVersion = "5.1.24"
val http4sVersion = "0.21.0-M6"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-effect" % catsVersion,
  "io.circe" %% "circe-generic" % CirceVersion,
  "io.circe" %% "circe-literal" % CirceVersion,
  "io.circe" %% "circe-generic-extras" % CirceGenericExVersion,
  "io.circe" %% "circe-parser" % CirceVersion,
  "io.circe" %% "circe-config" % CirceConfigVersion,
  "org.tpolecat" %% "doobie-core" % DoobieVersion,
  "org.tpolecat" %% "doobie-h2" % DoobieVersion,
  "org.tpolecat" %% "doobie-scalatest" % DoobieVersion,
  "org.tpolecat" %% "doobie-hikari" % DoobieVersion,
  "org.flywaydb" % "flyway-core" % FlywayVersion,
  "mysql" % "mysql-connector-java" % mysqlVersion,
  "dev.profunktor" %% "console4cats" % console4CatsVersion,
  "org.typelevel" %% "cats-mtl-core" % catsMtlVersion,
  "io.estatico" %% "newtype" % newtypeVersion,
  "eu.timepit" %% "refined" % refinedVersion,
  "org.scalaz" %% "scalaz-core" % scalazCoreVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaActorVersion,
  "com.github.cb372" %% "cats-retry-core"        % catsRetryVersion,
  "com.github.cb372" %% "cats-retry-cats-effect" % catsRetryVersion,
  "io.chrisdavenport" %% "log4cats-core"    % log4catsCoreVersion,
  "io.chrisdavenport" %% "log4cats-slf4j"   % log4catsSlf4jVersion,
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "org.scalactic" %% "scalactic" % scalaticVersion,
  "org.scalatest" %% "scalatest" % scalatestVersion % "test",
  "org.scalacheck" %% "scalacheck" % ScalaCheckVersion % Test,
  compilerPlugin("org.typelevel" % "kind-projector" % "0.11.0" cross CrossVersion.full)
)

scalacOptions += "-Ymacro-annotations"
