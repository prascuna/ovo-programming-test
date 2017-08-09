package example.services

import java.io.File

import example.errors.AddressBookError

object InputParser {
  def parse(args: Array[String]): Either[Error, File] = ???

  sealed abstract class Error(message: String) extends AddressBookError

  object Error {

    case class FileNotFound(filename: String) extends Error(s"File $filename not found")

    case object WrongArguments extends Error("Wrong arguments")

  }

}
