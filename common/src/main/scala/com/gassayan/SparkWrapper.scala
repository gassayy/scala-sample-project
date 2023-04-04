package com.gassayan

import org.apache.spark.sql.SparkSession


object SparkWrapper {

  def createSparkSession(appName: String, local: Boolean = false): SparkSession = {
    if (local) {
      SparkSession.builder().appName(appName).master("local[*]").getOrCreate()
    } else {
      SparkSession.builder().appName(appName).getOrCreate()
    }
  }

}
