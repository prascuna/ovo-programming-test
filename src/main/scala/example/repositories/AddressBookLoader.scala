package example.repositories

import java.io.File

import example.models.AddressBook.AddressBook
import example.repositories.AddressBookLoader.LoaderError

trait AddressBookLoader {
  def load: Either[LoaderError, AddressBook]
}

object AddressBookLoader {
  def apply(input: File) = new CVSLoader(input)

  sealed abstract class LoaderError(message: String)

  object LoaderError {

    case class UnreadableSource(source: String) extends LoaderError(s"Source $source cannot be read")
    case object WrongFormat extends LoaderError("Source has a wrong format")

  }

}

class CVSLoader(input: File) extends AddressBookLoader {
  def load: Either[LoaderError, AddressBook] = {
    ???
  }

}

