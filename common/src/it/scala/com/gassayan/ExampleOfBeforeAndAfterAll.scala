package com.gassayan

import org.scalatest.{BeforeAndAfterAll, FlatSpec}


class ExampleOfBeforeAndAfterAll extends FlatSpec with BeforeAndAfterAll {

  override def beforeAll(): Unit = {
    println("in beforeAll")
    super.beforeAll()
  }

  override def afterAll(): Unit = {
    println("in afterAll")
    val _ = super.afterAll()
  }

  behavior of "example"

  it should "succeed" in {
    println("test 1")
  }

  it should "succeed again" in {
    println("test2")
  }
}
