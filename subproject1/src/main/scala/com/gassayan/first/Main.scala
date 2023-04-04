package com.gassayan.first

import com.typesafe.scalalogging.Logger
import pureconfig._
import pureconfig.generic.auto._

object Main {

  val logger = Logger(getClass)

  def main(args: Array[String]): Unit = {
    val config = ConfigSource.default.loadOrThrow[FirstConfig]
    val _      = logger.info(s"Run first at version: ${config.toString()}")
  }
}
