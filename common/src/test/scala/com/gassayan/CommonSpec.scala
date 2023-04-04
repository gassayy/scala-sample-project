package com.gassayan

import org.scalatest.flatspec.AnyFlatSpec
import collection.mutable.ListBuffer
import org.scalatest.BeforeAndAfter

class CommonSpec extends AnyFlatSpec with BeforeAndAfter {

  val builder = new StringBuilder
  val buffer  = new ListBuffer[String]

  before {
    builder.append("ScalaTest is ")
  }

  after {
    builder.clear()
    buffer.clear()
  }

  "Testing" should "be easy" in {
    builder.append("easy!")
    assert(builder.toString === "ScalaTest is easy!")
    assert(buffer.isEmpty)
    buffer += "sweet"
  }

  it should "be fun" in {
    builder.append("fun!")
    assert(builder.toString === "ScalaTest is fun!")
    assert(buffer.isEmpty)
  }
}
