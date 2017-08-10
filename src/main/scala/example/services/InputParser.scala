package example.services

import java.io.File

import example.errors.AddressBookError
import example.services.InputParser.ParserError.{FileNotFound, WrongArguments}


object InputParser {
  def parse(args: Array[String]): Either[ParserError, File] =
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

  sealed abstract class ParserError(message: String) extends AddressBookError

  object ParserError {

    case class FileNotFound(filename: String) extends ParserError(s"File $filename not found")

    case object WrongArguments extends ParserError("Wrong arguments")

  }

}
