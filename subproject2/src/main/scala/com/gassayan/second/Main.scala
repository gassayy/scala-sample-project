package com.gassayan.second

import com.typesafe.scalalogging.Logger
import pureconfig._
import pureconfig.generic.auto._

object Main {

  val config = ConfigSource.default.loadOrThrow[SecondConfig]

  val logger = Logger(getClass)

  def main(args: Array[String]): Unit = logger.info(s"Run first at version: ${config.version}")
}
