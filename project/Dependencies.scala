import sbt._

import Dependencies._

object Dependencies {

  // scala version
  val scalaOrganization = "org.scala-lang"

  val jdkV         = "1.8"
  val scalaVersion = "2.12.16"
  val scalaV       = "2.12.6"
  val awsV         = "1.11.372"
  val akkaV        = "2.5.19"
  val catsV        = "1.2.0"
  val circeV       = "0.9.3"
  val akkaMgtV     = "0.20.0"

  // build tools version
  val scalaFmtV = "1.5.1"

  // aspectj version
  // val aspectjVersion = "1.9.2"

  // include Sonatype repositories
  val resolvers = Seq(
    Resolver sonatypeRepo "public",
    Resolver typesafeRepo "releases"
  )

  // functional libraries
  val fpLibs      = Seq(
    "org.typelevel" %% "cats-core" % catsV,
    "org.typelevel" %% "cats-laws" % catsV,
    "com.chuusai"   %% "shapeless" % "2.3.3"
  )
  // akka
  val akka      = Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-cluster" % akkaV
  )

  // akka management
  val akkaMgt = Seq(
    "com.lightbend.akka.management" %% "akka-management" % akkaMgtV,
    "com.lightbend.akka.management" %% "akka-management-cluster-bootstrap" % akkaMgtV,
    "com.lightbend.akka.discovery"  %% "akka-discovery-dns"                % akkaMgtV,
    "com.lightbend.akka.management" %% "akka-management-cluster-http"      % akkaMgtV,
    "com.lightbend.akka.discovery"  %% "akka-discovery-config"             % akkaMgtV,
    "com.lightbend.akka.discovery"  %% "akka-discovery-kubernetes-api"     % akkaMgtV
  )
  // config
  val scalaConfig = "com.typesafe"          %  "config"     % "1.3.3"
  val pureConfig  = "com.github.pureconfig" %% "pureconfig" % "0.9.2"  excludeAll ExclusionRule("org.scala-lang")
  // logging
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging"    % "3.9.0"
  val logback      = "ch.qos.logback"             %  "logback-classic"  % "1.2.3"
  val log4j        = Seq(
    "org.apache.logging.log4j"   %  "log4j-api"        % "2.11.1",
    "org.apache.logging.log4j"   %  "log4j-core"       % "2.11.1",
    "org.apache.logging.log4j"   %  "log4j-slf4j-impl" % "2.11.1"
  )
  // testing
  val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.14.0" % "test"
  val akkaTest   = "com.typesafe.akka" %% "akka-testkit" % akkaV % "test"
  val scalactic  = "org.scalactic"  %% "scalactic"  % "3.0.5"
  val scalaTest  = "org.scalatest"  %% "scalatest"  % "3.0.5" % "it,test"
}

trait Dependencies {
  val scalaOrganizationUsed = scalaOrganization
  val scalaVersionUsed = scalaVersion
  val scalaFmtVersionUsed = scalaFmtV
  // val aspectjVersionUsed = aspectjVersion
  // resolvers
  val commonResolvers = resolvers
  val mainDeps   = Seq(scalaConfig, pureConfig, scalaLogging, logback) ++ akka ++ fpLibs ++ log4j
  val testDeps   = Seq(scalaTest, scalaCheck, scalactic, akkaTest)
}
