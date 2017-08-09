package example.services

import example.errors.AddressBookError
import example.models.AddressBook.{Gender, Person}
import example.repositories.AddressBookRepository

class AnswersService(repository: AddressBookRepository) {
  def countByGender(gender: Gender): Either[Error, Int] = ???

  def oldestPerson: Either[Error, Person] = ???

  def ageDifference(nameA: String, nameB: String): Either[Error, Int] = ???
}

object AnswersService {
  def apply(repository: AddressBookRepository): AnswersService = new AnswersService(repository)


  sealed abstract class Error(message: String) extends AddressBookError

  object Error {

    case class PersonNotFound(name: String) extends Error(s"Person $name not found")

    case object EmptyAddressBook extends Error("Empty Address Book")

  }

}
