import sbt.Keys._
import sbt.TestFrameworks.ScalaTest
import sbt.Tests.Argument
import sbt._
// import com.lightbend.sbt.SbtAspectj._
// import com.lightbend.sbt.SbtAspectj.autoImport._
import com.lucidchart.sbt.scalafmt.ScalafmtCorePlugin.autoImport._
import com.typesafe.sbt._
import org.scalastyle.sbt.ScalastylePlugin.autoImport._
import sbtassembly.AssemblyPlugin.autoImport._
import scoverage._
import wartremover._

object Settings extends Dependencies {

  val FunctionalTest: Configuration = config("fun") extend Test describedAs "Runs only functional tests"

  private val commonSettings = Seq(
    organization      := "com.gassayan",

    scalaOrganization := scalaOrganizationUsed,
    scalaVersion      := scalaVersionUsed,

    scalafmtVersion   := scalaFmtVersionUsed
  )

  private val rootSettings = commonSettings

  private val modulesSettings = commonSettings ++ Seq(
    scalacOptions ++= Seq(
      // standard settings
      "-target:jvm-1.8",
      "-encoding", "UTF-8",
      "-unchecked",
      "-deprecation",
      "-explaintypes",
      "-feature",
      // language features
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-language:postfixOps",
      // private options
      //"-Ybackend-parallelism", "8", required scala >= 2.12.5
      "-Yno-adapted-args",
      "-Ypartial-unification",
      // warnings
      "-Ywarn-dead-code",
      "-Ywarn-extra-implicit",
      "-Ywarn-inaccessible",
      "-Ywarn-infer-any",
      "-Ywarn-macros:after",
      "-Ywarn-nullary-override",
      "-Ywarn-nullary-unit",
      "-Ywarn-numeric-widen",
      "-Ywarn-unused:implicits",
      "-Ywarn-unused:imports",
      "-Ywarn-unused:locals",
      "-Ywarn-unused:params",
      "-Ywarn-unused:patvars",
      "-Ywarn-unused:privates",
      "-Ywarn-value-discard",
      // advanced options
      "-Xcheckinit",
      "-Xfatal-warnings",
      "-Xfuture",
      // linting
      "-Xlint",
      "-Xlint:adapted-args",
      "-Xlint:by-name-right-associative",
      "-Xlint:constant",
      "-Xlint:delayedinit-select",
      "-Xlint:doc-detached",
      "-Xlint:inaccessible",
      "-Xlint:infer-any",
      "-Xlint:missing-interpolator",
      "-Xlint:nullary-override",
      "-Xlint:nullary-unit",
      "-Xlint:option-implicit",
      "-Xlint:package-object-classes",
      "-Xlint:poly-implicit-overload",
      "-Xlint:private-shadow",
      "-Xlint:stars-align",
      "-Xlint:type-parameter-shadow",
      "-Xlint:unsound-match"
    ),
    console / scalacOptions := Seq(
      // standard settings
      "-target:jvm-1.8",
      "-encoding", "UTF-8",
      "-unchecked",
      "-deprecation",
      "-explaintypes",
      "-feature",
      // language features
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-language:postfixOps",
      // private options
      "-Yno-adapted-args",
      "-Ypartial-unification"
    ),

    Global / cancelable := true,

    Compile / fork := true,
    Compile / trapExit := false,
    Compile / connectInput := true,
    Compile / outputStrategy := Some(StdoutOutput),

    resolvers ++= commonResolvers,

    libraryDependencies ++= mainDeps,

    Compile / scalafmtOnCompile := true,

    scalastyleFailOnError := true,

    Compile / compile / wartremoverWarnings ++= Warts.allBut(
      Wart.Any,
      Wart.DefaultArguments,
      Wart.ExplicitImplicitTypes,
      Wart.ImplicitConversion,
      Wart.ImplicitParameter,
      Wart.Overloading,
      Wart.PublicInference,
      Wart.NonUnitStatements,
      Wart.Nothing
    )
  )

//  private val jarPublishSettings = Seq(
//    homepage := Some(url("https://project_paga.com")),
//    // TODO configure licenses???
//    //licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
//    // TODO: configure scm info
////    scmInfo := Some(
////      ScmInfo(url("https://github.com/org/project"), "scm:git:git@github.com:org/project.git")
////    ),
//    // TODO change this with real configuration
//    publishTo := {
//      val repoBase = "https://oss.sonatype.org/"
//      if (isSnapshot.value)
//        Some("snapshots" at repoBase + "content/repositories/snapshots")
//      else
//        Some("releases" at repoBase + "service/local/staging/deploy/maven2")
//    },
//    publishMavenStyle := true,
//    publishArtifact in Test := false,
//    pomIncludeRepository := { _ => false }
//  )
//
//  private val dockerPublishSettings = Seq(
//
//  )
//
//  private val noPublishSettings =
//    Seq(skip in publish := true, publishArtifact := false)

  /* Project Strucutre */
  implicit class ProjectRoot(project: Project) {

    def root: Project = project in file(".")
  }

  implicit class ProjectFrom(project: Project) {

    def from(dir: String): Project = project in file(dir)
  }

  implicit class DataConfigurator(project: Project) {

    def setName(newName: String): Project = project.settings(name := newName)

    def setDescription(newDescription: String): Project = project.settings(description := newDescription)

    def setInitialImport(newInitialCommand: String): Project =
      project.settings(initialCommands := s"import com.gassayan._, $newInitialCommand")
  }

  implicit class RootConfigurator(project: Project) {

    def configureRoot: Project = project.settings(rootSettings: _*)
  }

  implicit class ModuleConfigurator(project: Project) {
    def configureModule: Project = project.settings(modulesSettings: _*).enablePlugins(GitVersioning)
  }

  implicit class DependsOnProject(project: Project) {

    private val testConfigurations = Set("test", "fun", "it")

    private def findCompileAndTestConfigs(p: Project) =
      (p.configurations.map(_.name).toSet intersect testConfigurations) + "compile"

    private val thisProjectsConfigs = findCompileAndTestConfigs(project)

    private def generateDepsForProject(p: Project) =
      p % (thisProjectsConfigs intersect findCompileAndTestConfigs(p) map (c => s"$c->$c") mkString ";")

    def compileAndTestDependsOn(projects: Project*): Project =
      project dependsOn (projects.map(generateDepsForProject): _*)
  }

  /* Projecgt Publish Configration */
//  implicit class PublishRootConfigurator(project: Project) {
//
//    def publishJar: Project = project.settings(jarPublishSettings)
//
//    def publishDocker: Project = project.setttings(dockerPublishSettings)
//
//    def noPublish: Project = project.settings(noPublishSettings)
//  }

//  implicit class PublishConfigurator(project: Project) {
//
//    def publishJar: Project = project
//      .settings(jarPublishSettings)
//
//    def publishDockerImage: Project = project.settings(dockerPublishSettings)
//
//    def publishNothing: Project = project
//      .settings(noPublishSettings)
//  }

  /* Project Execution and Test Configuration */
  implicit class RunConfigurator(project: Project) {

      def configureRun(main: String): Project = project
        .settings(inTask(assembly)(Seq(
          assemblyJarName := s"${name.value}.jar",
          assemblyMergeStrategy := {
            case strategy => MergeStrategy.defaultMergeStrategy(strategy)
          },
          mainClass := Some(main)
        )))
        .settings(Compile / run / mainClass := Some(main))
        // .settings(aspectjSettings)
        // .settings(Aspectj / aspectjVersion := aspectjVersionUsed)
        // .settings(reStart / javaOptions ++= (Aspectj / aspectjWeaverOptions).value)
    }

  abstract class TestConfigurator(project: Project, config: Configuration) {

    protected def configure(requiresFork: Boolean): Project = project
      .configs(config)
      .settings(inConfig(config)(Defaults.testSettings): _*)
      .settings(inConfig(config)(libraryDependencies ++= testDeps))
      .settings(inConfig(config)(scalafmtSettings))
      .settings(inConfig(config)(Seq(
        scalafmtOnCompile := true,
        scalastyleConfig := baseDirectory.value / "scalastyle-test-config.xml",
        scalastyleFailOnError := false,
        fork := requiresFork,
        testFrameworks := Seq(ScalaTest),
        testOptions += Argument(ScalaTest, "-oWDT"),
        parallelExecution := requiresFork
      )))
      .enablePlugins(ScoverageSbtPlugin)

    protected def configureIt(requiresFork: Boolean): Project = project
      .configs(config)
      .settings(libraryDependencies ++= testDeps)
      .settings(inConfig(config)(Defaults.testSettings): _*)
      .settings(inConfig(config)(scalafmtSettings))
      .settings(inConfig(config)(Seq(
        scalafmtOnCompile := true,
        // scalastyleConfig := baseDirectory.value / "scalastyle-test-config.xml",
        scalastyleFailOnError := false,
        fork := requiresFork,
        testFrameworks := Seq(ScalaTest),
        testOptions += Argument(ScalaTest, "-oWDT", "-DtempFileName=tempFileName.txt"),
        parallelExecution := false
      )))
      .enablePlugins(ScoverageSbtPlugin)

  }

  implicit class UnitTestConfigurator(project: Project) extends TestConfigurator(project, Test) {

    def configureTests(requiresFork: Boolean = false): Project = configure(requiresFork)

  }

  implicit class FunctionalTestConfigurator(project: Project) extends TestConfigurator(project, FunctionalTest) {

    def configureFunctionalTests(requiresFork: Boolean = false): Project = configure(requiresFork)

  }

  implicit class IntegrationTestConfigurator(project: Project) extends TestConfigurator(project, IntegrationTest) {

    def configureIntegrationTests(requiresFork: Boolean = false): Project = configureIt(requiresFork)

  }
}
