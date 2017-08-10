package example.repositories

import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import example.models.AddressBook.Gender.{Female, Male}
import example.models.AddressBook.Person
import example.repositories.AddressBookLoader.LoaderError.{UnreadableSource, WrongFormat}
import org.scalatest.{Matchers, path}

class CSVLoaderTest extends path.FunSpec with Matchers {
  describe("CSVLoader") {
    describe("when loading") {
      describe("a not existing file") {
        val inputFile = new File("does_not_exist.txt")
        val loader = AddressBookLoader(inputFile)
        it("should return Left(UnreadableSource)") {
          loader.load shouldBe Left(UnreadableSource(inputFile.getPath))
        }
      }
      describe("an existing file") {
        describe("with the wrong format") {
          val existingFileURI = getClass.getClassLoader.getResource("brokensample.csv").toURI
          val inputFile = new File(existingFileURI)
          val loader = AddressBookLoader(inputFile)
          it("should return Left(WrongFormat)") {
            loader.load shouldBe Left(WrongFormat)
          }
        }
        describe("with the right format") {
          val existingFileURI = getClass.getClassLoader.getResource("sample.csv").toURI
          val inputFile = new File(existingFileURI)
          val loader = AddressBookLoader(inputFile)
          it("should return Right(AddressBook) containing all items") {
            val dtf = DateTimeFormatter.ofPattern("dd/MM/yy")
            val expectedAddressBook = Map(
              "Paul Robinson" -> Person("Paul Robinson", Male, LocalDate.parse("15/01/85", dtf)),
              "Gemma Lane" -> Person("Gemma Lane", Female, LocalDate.parse("20/11/91", dtf))
            )
            loader.load shouldBe expectedAddressBook
          }
        }
      }
    }
  }
}
