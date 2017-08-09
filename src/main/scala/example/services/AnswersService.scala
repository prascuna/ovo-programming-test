package example.services

import example.errors.AddressBookError
import example.models.AddressBook.{Gender, Person}
import example.repositories.AddressBookRepository
import example.repositories.AddressBookRepository.RepositoryError
import example.services.AnswersService.ServiceError
import example.services.AnswersService.ServiceError.{EmptyAddressBook, PersonNotFound, UnexpectedError}

class AnswersService(repository: AddressBookRepository) {
  def countByGender(gender: Gender): Either[ServiceError, Int] =
    repository.countByGender(gender).left.map {
      case RepositoryError.EmptyRepository => EmptyAddressBook
      case _ => UnexpectedError
    }

  def oldestPerson: Either[ServiceError, Person] =
    repository.oldestPerson.left.map {
      case RepositoryError.EmptyRepository => EmptyAddressBook
      case _ => UnexpectedError
    }

  def ageDifference(nameA: String, nameB: String): Either[ServiceError, Long] = {
    (for {
      personA <- repository.findByName(nameA)
      personB <- repository.findByName(nameB)

    } yield Math.abs(personA.dateOfBirth.toEpochDay - personB.dateOfBirth.toEpochDay))
      .left.map {
      case RepositoryError.EmptyRepository => EmptyAddressBook
      case RepositoryError.EntryNotFound(id) => PersonNotFound(id)
    }
  }


}

object AnswersService {
  def apply(repository: AddressBookRepository): AnswersService = new AnswersService(repository)


  sealed abstract class ServiceError(message: String) extends AddressBookError

  object ServiceError {

    case class PersonNotFound(name: String) extends ServiceError(s"Person $name not found")

    case object EmptyAddressBook extends ServiceError("Empty Address Book")

    case object UnexpectedError extends ServiceError("An unexpected error has occurred")

  }

}
