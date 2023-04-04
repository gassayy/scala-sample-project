package com.gassayan

import org.scalatest.tagobjects.Slow
import org.scalatest.{ FunSpec, Matchers, Tag }

object DynamoDBTest extends Tag("com.company.tags.DynamoDBTest")

class CommonFunSpec extends FunSpec with Matchers {

  describe("DynamoDB") {
    describe("when empty") {
      it("should have zero record") {
        assert(1 === 1)
      }

      it("should produce NoSuchElementException when a query is executed", Slow, DynamoDBTest) {
        assert(true === true)
      }
    }
  }

}
