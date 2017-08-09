package example.repositories

import example.models.AddressBook.{Gender, Person}
import example.repositories.AddressBookRepository.RepositoryError

trait AddressBookRepository {
  def findByName(name: String): Either[RepositoryError, Option[Person]]

  def countByGender(gender: Gender): Either[RepositoryError, Int]

  def oldestPerson: Either[RepositoryError, Person]
}

object AddressBookRepository {
  def apply(loader: AddressBookLoader): AddressBookRepository = new InMemoryAddressBook(loader)

  sealed abstract class RepositoryError(message: String)

  object RepositoryError {

    case object EmptyRepository extends RepositoryError("Empty Repository")

  }

}

class InMemoryAddressBook(loader: AddressBookLoader) extends AddressBookRepository {
  override def findByName(name: String): Either[RepositoryError, Option[Person]] = ???

  override def countByGender(gender: Gender): Either[RepositoryError, Int] = ???

  override def oldestPerson: Either[RepositoryError, Person] = ???
}
