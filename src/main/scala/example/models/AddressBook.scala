package example.models

import java.time.LocalDate


object AddressBook {

  sealed trait Gender

  object Gender {
    def fromString(value: String): Option[Gender] =
      value match {
        case "Male" => Some(Male)
        case "Female" => Some(Female)
        case _ => None
      }

    object Male extends Gender

    object Female extends Gender

  }


  case class Person(name: String, gender: Gender, dateOfBirth: LocalDate)


  type AddressBook = Map[String, Person]

}