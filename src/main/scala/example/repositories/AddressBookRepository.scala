package example.repositories

import example.models.AddressBook.{Gender, Person}

trait AddressBookRepository {
  def findByName(name: String): Either[Error, Option[Person]]

  def countByGender(gender: Gender): Either[Error, Int]

  def oldestPerson: Either[Error, Person]
}

object AddressBookRepository {
  def apply(loader: AddressBookLoader): AddressBookRepository = new InMemoryAddressBook(loader)

  sealed abstract class Error(message: String)

  object Error {

    case object EmptyRepository extends Error("Empty Repository")

  }

}

class InMemoryAddressBook(loader: AddressBookLoader) extends AddressBookRepository {
  override def findByName(name: String): Either[Error, Option[Person]] = ???

  override def countByGender(gender: Gender): Either[Error, Int] = ???

  override def oldestPerson: Either[Error, Person] = ???
}
