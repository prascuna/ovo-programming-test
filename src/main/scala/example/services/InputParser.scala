package example.services

import java.io.File

import example.errors.AddressBookError
import example.services.InputParser.ParserError.{FileNotFound, WrongArguments}


object InputParser {
  def parse(args: Array[String]): Either[ParserError, File] =
    if (args.isEmpty) {
      Left(WrongArguments)
    } else {
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
