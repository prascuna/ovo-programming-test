package example.services

object AnswersFormatter {

  def format(answers: Seq[String]): Seq[String] = {
    val result = answers.foldLeft((0, Seq.empty[String])) { (acc, curr) =>
      val (index, answers) = acc
      val newIndex = index + 1
      val newAnswers = answers :+ s"$newIndex. $curr"
      (newIndex, newAnswers)
    }
    result._2
  }
}
