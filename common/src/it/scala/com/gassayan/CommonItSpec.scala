package com.gassayan

import org.scalatest.flatspec
import java.io._
import org.scalatest.BeforeAndAfterAllConfigMap
import org.scalatest.Outcome
import org.scalatest.ConfigMap

trait TempFileExistsSpec extends flatspec.FixtureAnyFlatSpec with BeforeAndAfterAllConfigMap {

  type FixtureParam = File
  override def withFixture(test: OneArgTest): Outcome = {
    val fileName = test.configMap("tempFileName").asInstanceOf[String]
    val file     = new File(fileName)
    withFixture(test.toNoArgTest(file))
  }

  "The temp file" should ("exist in " + suiteName) in { file =>
    assert(file.exists)
  }

  private val tempFileName = "tempFileName"

  // Set up the temp file needed by the test, taking
  // a file name from the configMap
  override def beforeAll(configMap: ConfigMap): Unit = {

    require(
      configMap.isDefinedAt(tempFileName),
      "must place a temp file name in the configMap under the key: " + tempFileName
    )

    val fileName = configMap(tempFileName).asInstanceOf[String]

    val writer = new FileWriter(fileName)
    try writer.write("Hello, suite of tests!")
    finally writer.close()
  }

  // Delete the temp file
  override def afterAll(configMap: ConfigMap): Unit = {
    // No need to require that configMap contains the key again because it won't get
    // here if it didn't contain the key in beforeAll
    val fileName = configMap("tempFileName").asInstanceOf[String]
    val file     = new File(fileName)
    val _        = file.delete()
  }
}

class OneSpec extends TempFileExistsSpec
class TwoSpec extends TempFileExistsSpec
class RedSpec extends TempFileExistsSpec
class BlueSpec extends TempFileExistsSpec
