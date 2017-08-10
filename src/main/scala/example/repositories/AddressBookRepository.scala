package example.repositories

import java.time.LocalDate

import example.models.AddressBook.{AddressBook, Gender, Person}
import example.repositories.AddressBookRepository.RepositoryError
import example.repositories.AddressBookRepository.RepositoryError.{EmptyRepository, EntryNotFound}

trait AddressBookRepository {
  def findByName(name: String): Either[RepositoryError, Person]

  def countByGender(gender: Gender): Either[RepositoryError, Int]

  def oldestPerson: Either[RepositoryError, Person]
}

object AddressBookRepository {
  def apply(loader: AddressBookLoader): AddressBookRepository = new InMemoryAddressBook(loader)

  sealed abstract class RepositoryError(message: String)

  object RepositoryError {

    case class EntryNotFound(id: String) extends RepositoryError(s"Entry $id not found")

    case object EmptyRepository extends RepositoryError("Empty Repository")

  }

}

class InMemoryAddressBook(loader: AddressBookLoader) extends AddressBookRepository {

  private val data: AddressBook = loader.load.getOrElse(Map.empty)

  override def findByName(name: String): Either[RepositoryError, Person] =
    emptyRepositoryOr {
      data.get(name).toRight(EntryNotFound(name))
    }


  override def countByGender(gender: Gender): Either[RepositoryError, Int] =
    emptyRepositoryOr {
      Right(data.values.count(_.gender == gender))
    }

  override def oldestPerson: Either[RepositoryError, Person] =
    emptyRepositoryOr {
      implicit val localDateOrdering: Ordering[LocalDate] = Ordering.by(_.toEpochDay)
      implicit val dateOfBirthOrdering = Ordering.by[Person, LocalDate](_.dateOfBirth)
      Right(data.values.min)
    }


  private def emptyRepositoryOr[T](body: => Either[RepositoryError, T]) =
    if (data.nonEmpty) {
      body
    } else {
      Left(EmptyRepository)
    }
}
