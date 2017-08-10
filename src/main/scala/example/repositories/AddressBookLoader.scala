package example.repositories

import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import example.models.AddressBook.{AddressBook, Gender, Person}
import example.repositories.AddressBookLoader.LoaderError
import example.repositories.AddressBookLoader.LoaderError.{UnreadableSource, WrongFormat}

import scala.util.control.Exception._

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
  private val dtf = DateTimeFormatter.ofPattern("dd/MM/yy")

  def load: Either[LoaderError, AddressBook] =
    if (input.canRead && input.isFile) {
      nonFatalCatch.either {
        io.Source.fromFile(input).getLines().map { line =>
          val cols = line.split(",")
          require(cols.size == 3)

          // I want to intentionally explode in case of wrong input
          val dob = LocalDate.parse(cols(2).trim, dtf)
          val gender = Gender.fromString(cols(1).trim).get

          val name = cols(0).trim

          name -> Person(name, gender, dob)

        }.toMap
      }.left.map(_ => WrongFormat)
    } else {
      Left(UnreadableSource(input.getPath))
    }

}

