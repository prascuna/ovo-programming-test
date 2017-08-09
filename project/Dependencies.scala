import sbt._

object Dependencies {
  lazy val testDependencies = Seq(
    "org.scalatest" %% "scalatest" % "3.0.3" % Test,
    "org.mockito" % "mockito-core" % "2.8.47" % Test
  )
}
