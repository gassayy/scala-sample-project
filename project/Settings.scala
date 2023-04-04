import sbt.Keys._
import sbt.TestFrameworks.ScalaTest
import sbt.Tests.Argument
import sbt._
import com.typesafe.sbt._
import org.scalastyle.sbt.ScalastylePlugin.autoImport._
import sbtassembly.AssemblyPlugin.autoImport._
import scoverage._
import wartremover._

object Settings extends Dependencies {

  val FunctionalTest: Configuration = config("fun") extend Test describedAs "Runs only functional tests"

  private val commonSettings = Seq(
    organization := "com.gassayan",
    scalaOrganization := scalaOrganizationUsed,
    scalaVersion := scalaVersionUsed
  )

  private val rootSettings = commonSettings

  private val modulesSettings = commonSettings ++ Seq(
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8",
      "-unchecked",
      "-deprecation",
      "-explaintypes",
      "-feature",
      // language features
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-language:postfixOps",
      // warnings
      "-Ywarn-dead-code",
      "-Ywarn-extra-implicit",
      "-Ywarn-macros:after",
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
      // linting
      "-Xlint",
      "-Xlint:adapted-args",
      "-Xlint:constant",
      "-Xlint:delayedinit-select",
      "-Xlint:doc-detached",
      "-Xlint:infer-any",
      "-Xlint:missing-interpolator",
      "-Xlint:nullary-unit",
      "-Xlint:option-implicit",
      "-Xlint:package-object-classes",
      "-Xlint:poly-implicit-overload",
      "-Xlint:private-shadow",
      "-Xlint:stars-align",
      "-Xlint:type-parameter-shadow",
      // https://github.com/pureconfig/pureconfig/issues/1258
      "-Wconf:site=com.gassayan.first.*&cat=lint-byname-implicit:silent",
      "-Wconf:site=com.gassayan.second.*&cat=lint-byname-implicit:silent"
    ),
    Global / cancelable := true,
    Compile / fork := true,
    Compile / connectInput := true,
    Compile / outputStrategy := Some(StdoutOutput),
    resolvers ++= commonResolvers,
    libraryDependencies ++= mainDeps,
    scalastyleFailOnError := true
  )

  /* Project Structure */
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

  /* Project Execution and Test Configuration */
  implicit class RunConfigurator(project: Project) {

    def configureRun(main: String): Project = project
      .settings(
        inTask(assembly)(
          Seq(
            assemblyJarName := s"${name.value}.jar",
            assemblyMergeStrategy := { case strategy =>
              MergeStrategy.defaultMergeStrategy(strategy)
            },
            mainClass := Some(main)
          )
        )
      )
      .settings(Compile / run / mainClass := Some(main))
  }

  abstract class TestConfigurator(project: Project, config: Configuration) {

    protected def configure(requiresFork: Boolean): Project = project
      .configs(config)
      .settings(inConfig(config)(Defaults.testSettings): _*)
      .settings(inConfig(config)(libraryDependencies ++= testDeps))
      .settings(
        inConfig(config)(
          Seq(
            scalastyleConfig := baseDirectory.value / "scalastyle-test-config.xml",
            scalastyleFailOnError := false,
            fork := requiresFork,
            testFrameworks := Seq(ScalaTest),
            testOptions += Argument(ScalaTest, "-oWDT"),
            parallelExecution := requiresFork
          )
        )
      )
      .enablePlugins(ScoverageSbtPlugin)

    protected def configureIt(requiresFork: Boolean): Project = project
      .configs(config)
      .settings(libraryDependencies ++= testDeps)
      .settings(inConfig(config)(Defaults.testSettings): _*)
      .settings(
        inConfig(config)(
          Seq(
            scalastyleFailOnError := false,
            fork := requiresFork,
            testFrameworks := Seq(ScalaTest),
            testOptions += Argument(ScalaTest, "-oWDT", "-DtempFileName=tempFileName.txt"),
            parallelExecution := false
          )
        )
      )
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
