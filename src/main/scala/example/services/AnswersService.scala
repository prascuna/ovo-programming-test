package example.services

import example.errors.AddressBookError
import example.models.AddressBook.{Gender, Person}
import example.repositories.AddressBookRepository
import example.services.AnswersService.ServiceError

class AnswersService(repository: AddressBookRepository) {
  def countByGender(gender: Gender): Either[ServiceError, Int] = ???

  def oldestPerson: Either[ServiceError, Person] = ???

  def ageDifference(nameA: String, nameB: String): Either[ServiceError, Int] = ???
}

object AnswersService {
  def apply(repository: AddressBookRepository): AnswersService = new AnswersService(repository)


  sealed abstract class ServiceError(message: String) extends AddressBookError

  object ServiceError {

    case class PersonNotFound(name: String) extends ServiceError(s"Person $name not found")

    case object EmptyAddressBook extends ServiceError("Empty Address Book")

  }

}
