package example.services

import org.scalatest.{Matchers, path}

class AnswersFormatterTest extends path.FunSpec with Matchers {

  describe("AnswersFormatter") {
    describe("when formatting a sequence of answers") {
      it("should return a sequence of answers formatted with an incremental number") {
        AnswersFormatter.format(Seq("one", "two")) shouldBe Seq("1. one", "2. two")
      }
    }
  }

}
