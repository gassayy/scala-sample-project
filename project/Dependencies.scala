import sbt._

import Dependencies._

object Dependencies {

  // scala version
  val scalaOrganization = "org.scala-lang"
  val scalaVersion      = "2.12.7"

  // build tools version
  val scalaFmtVersion = "1.5.1"

  // aspectj version
  // val aspectjVersion = "1.9.2"

  // libraries versions
  val catsVersion     = "1.2.0"
  val monixVersion    = "3.0.0-RC2"
  val specs2Version   = "4.3.3"

  // resolvers
  val resolvers = Seq(
    Resolver sonatypeRepo "public",
    Resolver typesafeRepo "releases"
  )

  // functional libraries
  val cats               = "org.typelevel"                %% "cats-core"                 % catsVersion
  val catsLaws           = "org.typelevel"                %% "cats-laws"                 % catsVersion
  val shapeless          = "com.chuusai"                  %% "shapeless"                 % "2.3.3"
  // async
  val monixExecution     = "io.monix"                     %% "monix-execution"           % monixVersion
  val monixEval          = "io.monix"                     %% "monix-eval"                % monixVersion
  // config
  val scopt              = "com.github.scopt"             %% "scopt"                     % "3.7.0"
  val scalaConfig        = "com.typesafe"                 %  "config"                    % "1.3.3"
  val pureConfig         = "com.github.pureconfig"        %% "pureconfig"                % "0.9.2"  excludeAll (
          ExclusionRule(   "org.scala-lang")
  )
  // logging
  val scalaLogging       = "com.typesafe.scala-logging"   %% "scala-logging"             % "3.9.0"
  val logback            = "ch.qos.logback"               %  "logback-classic"           % "1.2.3"
  // testing
  val spec2Core          = "org.specs2"                   %% "specs2-core"               % specs2Version
  val spec2Mock          = "org.specs2"                   %% "specs2-mock"               % specs2Version
  val spec2Scalacheck    = "org.specs2"                   %% "specs2-scalacheck"         % specs2Version
}

trait Dependencies {

  val scalaOrganizationUsed = scalaOrganization
  val scalaVersionUsed = scalaVersion

  val scalaFmtVersionUsed = scalaFmtVersion

  // val aspectjVersionUsed = aspectjVersion

  // resolvers
  val commonResolvers = resolvers

  val mainDeps = Seq(cats, shapeless, scopt, scalaConfig, pureConfig, monixExecution, monixEval, scalaLogging, logback)

  val testDeps = Seq(catsLaws, spec2Core, spec2Mock, spec2Scalacheck)


}