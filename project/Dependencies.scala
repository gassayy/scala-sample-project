import sbt._

import Dependencies._
import javax.naming.spi.Resolver

object Dependencies {

  // scala version
  val scalaOrganization = "org.scala-lang"

  val jdkV          = "1.8"
  val scalaVersion  = "2.12.17"
  val awsV          = "1.11.372"
  val akkaV         = "2.8.0"
  val catsV         = "2.9.0"
  val circeV        = "0.14.5"
  val shapelessV    = "2.3.10"
  val akkaMgtV      = "1.3.0"
  val sparkV        = "3.2.3"
  val hudiV         = "0.13.0"
  val deltaV        = "2.2.0"
  val log4jV        = "2.20.0"
  val scalaLoggingV = "3.9.5"
  val logbackV      = "1.4.6"
  val scalaTestV    = "3.2.15"
  val scalaCheckV   = "1.17.0"
  val flinkV        = "1.17.0"

  // build tools version
  val scalaFmtV = "3.7.2"

  val resolvers = Seq(
    "Maven Central".at("https://mvnrepository.com"),
    "Confluent".at("https://packages.confluent.io/maven"),
    "scala-tools".at("https://oos.sonatype.org/content/groups/scala-tools"),
    "Typesafe repository".at("https://repo.typesafe.com/typesafe/release"),
    "Typesafe second repository".at("https://repo.typesafe.com/typesafe/maven-release"),
    "Spark packages".at("https://repos.spark-packages.org"),
    "Sonatype OSS Snapshots".at("https://oss.sonatype.org/content/repositories/snapshots")
  )

  // functional libraries
  val fpLibs = Seq(
    "org.typelevel" %% "cats-core" % catsV,
    "org.typelevel" %% "cats-laws" % catsV,
    "com.chuusai" %% "shapeless" % shapelessV
  )
  // akka
  val akka = Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-cluster" % akkaV
  )

  // akka management
  val akkaMgt = Seq(
    "com.lightbend.akka.management" %% "akka-management" % akkaMgtV,
    "com.lightbend.akka.management" %% "akka-management-cluster-bootstrap" % akkaMgtV,
    "com.lightbend.akka.discovery" %% "akka-discovery-dns" % akkaMgtV,
    "com.lightbend.akka.management" %% "akka-management-cluster-http" % akkaMgtV,
    "com.lightbend.akka.discovery" %% "akka-discovery-config" % akkaMgtV,
    "com.lightbend.akka.discovery" %% "akka-discovery-kubernetes-api" % akkaMgtV
  )

  // apache spark
  val sparkBundle = Seq(
    "org.apache.spark" %% "spark-core" % sparkV,
    "org.apache.spark" %% "spark-sql" % sparkV,
    "org.apache.spark" %% "spark-mllib" % sparkV
  )

  // apache hudi
  val hudi = Seq(
    "org.apache.hudi" %% "hudi-spark-bundle" % hudiV excludeAll ExclusionRule("org.scala-lang")
  )
  // delta.io
  val deltaIO = Seq(
    "io.delta" %% "delta-core" % deltaV
  )
  // config
  val pureConfig = "com.github.pureconfig" %% "pureconfig" % "0.17.2"
  // logging
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingV
  val logback      = "ch.qos.logback" % "logback-classic" % logbackV % Test
  val log4j = Seq(
    "org.apache.logging.log4j" % "log4j-api" % log4jV,
    "org.apache.logging.log4j" % "log4j-core" % log4jV,
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jV
  )
  // flink
  val flink = Seq(
    "org.apache.flink" %% "flink-clients" % flinkV,
    "org.apache.flink" %% "flink-scala" % flinkV,
    "org.apache.flink" %% "flink-streaming-scala" % flinkV,
    "org.apache.flink" %% "flink-connector-kafka" % flinkV
  )
  // testing
  val scalaCheck = "org.scalacheck" %% "scalacheck" % scalaCheckV % "test"
  val akkaTest   = "com.typesafe.akka" %% "akka-testkit" % akkaV % "test"
  val scalactic  = "org.scalactic" %% "scalactic" % scalaTestV
  val scalaTest  = "org.scalatest" %% "scalatest" % scalaTestV % "it,test"
}

trait Dependencies {
  val scalaOrganizationUsed = scalaOrganization
  val scalaVersionUsed      = scalaVersion
  val scalaFmtVersionUsed   = scalaFmtV
  // resolvers
  val commonResolvers = resolvers
  val mainDeps =
    Seq(pureConfig, scalaLogging, logback) ++ akka ++ fpLibs ++ log4j
  val testDeps = Seq(scalaTest, scalaCheck, scalactic, akkaTest)
}
