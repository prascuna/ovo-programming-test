package example.repositories

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import example.models.AddressBook.{AddressBook, Person}
import example.models.AddressBook.Gender.{Female, Male}
import example.repositories.AddressBookLoader.LoaderError.{UnreadableSource, WrongFormat}
import example.repositories.AddressBookRepository.RepositoryError.{EmptyRepository, EntryNotFound}
import org.scalatest.{Matchers, path}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar

class AddressBookRepositoryTest extends path.FunSpec with Matchers with MockitoSugar {

  describe("AddressBookRepository") {
    describe("when the data cannot be loaded because it's") {
      val loader = mock[AddressBookLoader]
      describe("unreadable") {
        when(loader.load) thenReturn Left(UnreadableSource("file.txt"))
        val repo = AddressBookRepository(loader)
        it should behave like emptyRepository(repo)
      }
      describe("in the wrong format") {
        when(loader.load) thenReturn Left(WrongFormat)
        val repo = AddressBookRepository(loader)
        it should behave like emptyRepository(repo)
      }
    }
    describe("when the data is readable") {
      describe("but there is no data in the addressbook") {
        val loader = mock[AddressBookLoader]
        val data: AddressBook = Map.empty
        when(loader.load) thenReturn Right(data)
        val repo = AddressBookRepository(loader)
        it should behave like emptyRepository(repo)
      }
      describe("when there is data in the addressbook") {
        val dtf = DateTimeFormatter.ofPattern("dd/MM/yy")
        val data: AddressBook = Map(
          "Bill McKnight" -> Person("Bill McKnight", Male, LocalDate.parse("16/03/77", dtf)),
          "Paul Robinson" -> Person("Paul Robinson", Male, LocalDate.parse("15/01/85", dtf)),
          "Gemma Lane" -> Person("Gemma Lane", Female, LocalDate.parse("20/11/91", dtf)),
          "Sarah Stone" -> Person("Sarah Stone", Female, LocalDate.parse("20/09/80", dtf)),
          "Wes Jackson" -> Person("Wes Jackson", Male, LocalDate.parse("14/08/74", dtf))
        )
        val loader = mock[AddressBookLoader]
        when(loader.load) thenReturn Right(data)
        val repo = AddressBookRepository(loader)
        describe("when finding by name") {
          describe("and the person is found") {
            it("should return Right(Person)") {
              repo.findByName("Gemma Lane") shouldBe Right(Person("Gemma Lane", Female, LocalDate.parse("20/11/91", dtf)))
            }
          }
          describe("and the person is not found") {
            it("should return Left(EntryNotFound)") {
              repo.findByName("John Doe") shouldBe Left(EntryNotFound("John Doe"))
            }
          }
        }
        describe("when counting by gender") {
          it("should return the number of person of the given gender") {
            repo.countByGender(Male) shouldBe Right(3)
            repo.countByGender(Female) shouldBe Right(2)
          }
        }
        describe("when finding the oldest person") {
          it("should return the oldest person") {
            repo.oldestPerson shouldBe Right(Person("Wes Jackson", Male, LocalDate.parse("14/08/74", dtf)))
          }
        }
      }
    }

  }


  def emptyRepository(repository: AddressBookRepository) = {
    describe("when finding by name") {
      it("should return Left(EmptyRepository)") {
        repository.findByName("anyone") shouldBe Left(EmptyRepository)
      }
    }
    describe("when counting by gender") {
      it("should return Left(EmptyRepository)") {
        repository.countByGender(Male) shouldBe Left(EmptyRepository)
        repository.countByGender(Female) shouldBe Left(EmptyRepository)
      }
    }
    describe("when finding the oldest person") {
      it("should return Left(EmptyRepository)") {
        repository.oldestPerson shouldBe Left(EmptyRepository)
      }
    }
  }

}
