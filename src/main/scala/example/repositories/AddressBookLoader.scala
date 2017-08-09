package example.repositories

import java.io.File

import example.models.AddressBook.AddressBook

trait AddressBookLoader {
  def load: AddressBook
}

object AddressBookLoader {
  def apply(input: File) = new CVSLoader(input)
}

class CVSLoader(input: File) extends AddressBookLoader {
  def load: AddressBook = {
    ???
  }

}

