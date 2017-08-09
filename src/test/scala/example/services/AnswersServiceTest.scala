package example.services

import java.time.LocalDate

import example.models.AddressBook.Gender.{Female, Male}
import example.models.AddressBook.{Gender, Person}
import example.repositories.AddressBookRepository
import example.repositories.AddressBookRepository.RepositoryError.EmptyRepository
import example.services.AnswersService.ServiceError.{EmptyAddressBook, PersonNotFound}
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, path}

class AnswersServiceTest extends path.FunSpec with Matchers with MockitoSugar {


  describe("AnswersService") {
    describe("when working on an empty repository") {

      val repository = mock[AddressBookRepository]
      when(repository.findByName(anyString)) thenReturn Left(EmptyRepository)
      when(repository.countByGender(any[Gender])) thenReturn Left(EmptyRepository)
      when(repository.oldestPerson) thenReturn Left(EmptyRepository)

      val service = AnswersService(repository)

      describe("when counting by gender") {
        it("should return Left(EmptyAddressBook]") {
          service.countByGender(Male) shouldBe Left(EmptyAddressBook)
        }
      }
      describe("when finding the oldest person") {
        it("should return Left(EmptyAddressBook]") {
          service.oldestPerson shouldBe Left(EmptyAddressBook)
        }
      }
      describe("when calculating the age difference") {
        it("should return Left(EmptyAddressBook]") {
          service.ageDifference("name 1", "name 2") shouldBe Left(EmptyAddressBook)
        }
      }
    }
    describe("when working on a non empty repository") {
      val repository = mock[AddressBookRepository]
      describe("when counting by gender") {
        when(repository.countByGender(Male)) thenReturn Right(10)
        when(repository.countByGender(Female)) thenReturn Right(20)
        val service = AnswersService(repository)
        describe("the number of males") {
          it("should be the number returned by the repo") {
            service.countByGender(Male) shouldBe Right(10)
          }
        }
        describe("the number of females") {
          it("should be the number returned by the repo") {
            service.countByGender(Male) shouldBe Right(20)
          }
        }
      }
      describe("when finding the oldest person") {
        val oldestPerson = Right(Person("Old Joe", Male, LocalDate.of(1932, 10, 12)))
        when(repository.oldestPerson) thenReturn oldestPerson
        val service = AnswersService(repository)
        it("should return the oldest one") {
          service.oldestPerson shouldBe oldestPerson
        }
      }
      describe("when calculating the age difference") {
        val personA = Person("Person A", Male, LocalDate.of(1977, 12, 12))
        val personB = Person("Person B", Male, LocalDate.of(1978, 1, 2))
        describe("and both name exist in the addressbook") {
          when(repository.findByName(personA.name)) thenReturn Right(Some(personA))
          when(repository.findByName(personB.name)) thenReturn Right(Some(personB))
          val service = AnswersService(repository)
          it("should return the age difference in days of the two given names") {
            service.ageDifference(personA.name, personB.name) shouldBe Right(21)
          }
        }
        describe("and one of the names does not exist in the addressbook") {
          when(repository.findByName(personA.name)) thenReturn Right(Some(personA))
          when(repository.findByName(personB.name)) thenReturn Right(None)
          val service = AnswersService(repository)
          it("should return a Left(PersonNotFound)") {
            service.ageDifference(personA.name, personB.name) shouldBe Left(PersonNotFound(personB.name))
          }
        }
      }
    }
  }
}