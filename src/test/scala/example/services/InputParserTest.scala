package example.services

import java.io.File

import example.services.InputParser.ParserError.{FileNotFound, WrongArguments}
import org.scalatest.{Matchers, path}

class InputParserTest extends path.FunSpec with Matchers {

  describe("InputParser") {
    describe("when parsing the arguments") {
      describe("empty array") {
        it("should return Left(Wrong Arguments)") {
          InputParser.parse(Array.empty[String]) shouldBe Left(WrongArguments)
        }
      }
      describe("not existing file") {
        it("should return Left(FileNotFound)") {
          InputParser.parse(Array("does_not_exist.txt")) shouldBe Left(FileNotFound("does_not_exist.txt"))
        }
      }
      describe("existing file") {
        it("should return Right(File) containing the content of the file") {
          val existingFileURI = getClass.getClassLoader.getResource("sample.csv").toURI
          val expectedFile = new File(existingFileURI)

          val existingFilePath = existingFileURI.getPath

          val returnedFile = InputParser.parse(Array(existingFilePath))
          returnedFile shouldBe 'right
          returnedFile.right.foreach { file =>
            io.Source.fromFile(file).mkString shouldBe io.Source.fromFile(expectedFile).mkString
          }
        }
      }
    }
  }

}
