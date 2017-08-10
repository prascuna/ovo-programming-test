package example.services

object AnswersPrinter {

  def print(answers: Seq[String]): Unit =
    answers.foldLeft((0, Seq.empty[String])) { (acc, curr) =>
      val (index, answers) = acc
      val newIndex = index + 1
      val newAnswers = answers :+ s"$newIndex. $curr"
      (newIndex, newAnswers)
    }
}
