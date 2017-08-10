package example.services

import java.io.File

import example.errors.AddressBookError
import example.services.InputParser.Error.{FileNotFound, WrongArguments}


object InputParser {
  def parse(args: Array[String]): Either[Error, File] =
    args.length match {
      case 0 => Left(WrongArguments)
      case s if s > 0 =>
        val file = new File(args(0))
        if (file.canRead && file.isFile) {
          Right(file)
        } else {
          Left(FileNotFound(args(0)))
        }
    }

  sealed abstract class Error(message: String) extends AddressBookError

  object Error {

    case class FileNotFound(filename: String) extends Error(s"File $filename not found")

    case object WrongArguments extends Error("Wrong arguments")

  }

}
