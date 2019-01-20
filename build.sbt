import sbt._
import Settings._

lazy val buildVersion = sys.props.getOrElse("buildVersion", "0.1")

val noPublishSettings = Seq(skip in publish := true, publishArtifact := false)

val jarPublishSettings = Seq(
    publishTo := Some("Artifactory Realm" at "URI"),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false }
  )

val repo_user   = sys.env.get("ARTIFACTORY_USER")
val repo_passwd = sys.env.get("ARTIFACTORY_PASSWD")

//credentials += (repo_user, repo_passwd) match {
//  case (Some(user), Some(passwd)) => { Seq(Credentials("Artifactory Realm", "URI", user, passwd)) }
//  case (_, _)         => { println("missing credential..."); Seq() }
//}


lazy val root = project.root
  .setName("scala-hello-world")
  .setDescription("hello world built upon MateuszKubuszok/SBTScalaMultiproject2.g8")
  .configureRoot
  .settings(noPublishSettings)
  .aggregate(common, subproject1)


lazy val common = project.from("common")
  .setName("common")
  .setDescription("Common utilities")
  .setInitialImport("Common")
  .configureModule
  .configureTests()
  .configureFunctionalTests()
  .configureIntegrationTests()
  .settings(Compile / resourceGenerators += task[Seq[File]] {
    val file = (Compile / resourceManaged).value / "scala-hello-world-version.conf"
    IO.write(file, s"version=${version.value}")
    Seq(file)
  })
  .settings(jarPublishSettings)

lazy val subproject1 = project.from("subproject1")
  .setName("subproject1")
  .setDescription("Subproject 1")
  .setInitialImport("first._")
  .configureModule
  .configureTests()
  .compileAndTestDependsOn(common)
  .configureRun("com.gassayan.first.First")
  .settings(
    version in Docker  := buildVersion,
    dockerBaseImage    := "openjdk:8-jre",
    // dockerRepository   := Some("docker-registry"),
    dockerUpdateLatest := true,
  )
  .enablePlugins(JavaAppPackaging, sbtdocker.DockerPlugin)

lazy val subproject2 = project.from("subproject2")
  .setName("subproject2")
  .setDescription("Subproject 2")
  .setInitialImport("second._")
  .configureModule
  .configureTests()
  .compileAndTestDependsOn(common)
  .configureRun("com.gassayan.second.Second")
  .settings(
    version in Docker  := buildVersion,
    dockerBaseImage    := "openjdk:8-jre",
    // dockerRepository   := Some("repository"),
    dockerUpdateLatest := true,
  )
  .enablePlugins(JavaAppPackaging, sbtdocker.DockerPlugin)

addCommandAlias("fullTest", ";test;fun:test;it:test;scalastyle")

addCommandAlias("fullCoverageTest", ";coverage;test;fun:test;it:test;coverageReport;coverageAggregate;scalastyle")

addCommandAlias("relock", ";unlock;reload;update;lock")
