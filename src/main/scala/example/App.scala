package example

import example.models.AddressBook.Gender.Male
import example.repositories.{AddressBookLoader, AddressBookRepository}
import example.services.{AnswersFormatter, AnswersService, InputParser}

object App {

  def main(args: Array[String]): Unit = {

    val answers = for {
      inputFile <- InputParser.parse(args)
      loader = AddressBookLoader(inputFile)
      repo = AddressBookRepository(loader)
      service = AnswersService(repo)

      numberOfMales <- service.countByGender(Male)
      oldestPerson <-service.oldestPerson
      ageDifference <- service.ageDifference("Bill McKnight", "Paul Robinson")

    } yield List(numberOfMales.toString, oldestPerson.name, ageDifference.toString)

    answers match {
      case Right(a) => AnswersFormatter.format(a).foreach(println)
      case Left(error) =>
        System.err.println("An Error has occurred:")
        System.err.println(error)
        System.err.println(usage)
    }

  }

  private val usage =
    """
      |sbt "run <inputFile>"
      |
      |Example:
      |
      |sbt "run AddressBook"
    """.stripMargin

}
