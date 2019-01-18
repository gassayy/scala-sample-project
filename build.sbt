import sbt._
import Settings._

lazy val root = project.root
  .setName("scala-hello-world")
  .setDescription("hello world built upon MateuszKubuszok/SBTScalaMultiproject2.g8")
  .configureRoot
  .aggregate(common, first, second)

lazy val common = project.from("common")
  .setName("common")
  .setDescription("Common utilities")
  .setInitialImport("_")
  .configureModule
  .configureTests()
  .configureFunctionalTests()
  .configureIntegrationTests()
  .settings(Compile / resourceGenerators += task[Seq[File]] {
    val file = (Compile / resourceManaged).value / "scala-hello-world-version.conf"
    IO.write(file, s"version=${version.value}")
    Seq(file)
  })

lazy val first = project.from("subproject1")
  .setName("subproject1")
  .setDescription("Subproject 1")
  .setInitialImport("first._")
  .configureModule
  .configureTests()
  .compileAndTestDependsOn(common)
  .configureRun("com.gassayan.first.First")

lazy val second = project.from("subproject2")
  .setName("subproject2")
  .setDescription("Subproject 2")
  .setInitialImport("second._")
  .configureModule
  .configureTests()
  .compileAndTestDependsOn(common)
  .configureRun("com.gassayan.second.Second")

addCommandAlias("fullTest", ";test;fun:test;it:test;scalastyle")

addCommandAlias("fullCoverageTest", ";coverage;test;fun:test;it:test;coverageReport;coverageAggregate;scalastyle")

addCommandAlias("relock", ";unlock;reload;update;lock")
