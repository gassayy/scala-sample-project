# SBT Scala Multi-Project Template

A template for Scala in SBT:

 * two modules independent on `common`: `subproject1` and `subproject2`,
 * [Scalafmt](https://github.com/lucidsoftware/neo-sbt-scalafmt) configuration,
 * [Scoverage](https://github.com/scoverage/sbt-scoverage) configuration,
 * [Scalastyle](http://www.scalastyle.org/) configuration,
 * [WartRemover](http://www.wartremover.org/) configuration,
 * predefined [sub]tasks: `it:test`, `fun:test`, `test` which run tests as
   `IntegrationTest`/`FunctionalTest`/`Test` respectively,
 * some additional plugins I finding useful: sbt-revolver, sbt-lock, sbt-git, sbt-assembly,

## Required Environment Variables
TBD

## Project Configuration in SBT


Within `build.sbt` use existing modules as basis how to use small DSL for applying common settings:

 * `project.from("name")` will create module from `modules/name` directory and set its SBT name to `name`,
 * `setName("name")` and `setDescription("description")` can be used for settings up artifact name and description with
   fluent interface,
 * `setInitialImport("package")` will set console starting point to `import your.namespace, {package}`,
 * `configureModule` will apply all common settings from `Settings.scala`,
 * `configureUnitTests`/`configureFunctionalTests`/`configureIntegrationTests` will configure `test`/`func:test`/
   `it:test` task to subprojects,
 * `compileAndTestDependsOn(projects)` will set up both compile and test dependency (so `test:compile` will dependa on
   `test:compile` of a `test:compile` of another project allowing for reusing common test logic),
 * each of those commands will return project allowing normal `.settings(settings)` application. For individual settings
   one can also use `modules/name/build.sbt` individual per-module settings.
 * `project/Settings.scala` for tweaking `scalacOptions` and etc,
`* .scalafmt.conf` and `scalastyle-config.xml` for defining coding styles.
 * common resolvers and dependencies within `project/Dependencies.scala`

## Overriding Defaults Checks

If possible make defaults as strict as possible and just loosen them where absolutely needed:

 * coverage disabling:

   ```scala
   // $COVERAGE:OFF$ [reason]
   // not measured
   // $COVERAGE:ON$
   ```
 * formatting disabling:

   ```scala
   // format: OFF
   // not formatted
   // format: ON
   ```
 * style check disabling:

   ```scala
   // scalastyle:off [rule id]
   // not checked
   // scalastyle:on
   ```

It can be used for e.g disabling measurement of automatically generated code, formatting that merges lines into
something exceeding character limit or allowing null where interacting with Java code.

## Running main classes:

```bash
sbt "project subproject1" run // or
sbt subproject1/run

```

## Building jars

```bash
sbt "project subproject1" assembly // or
sbt subproject1/assembly

sbt "project subproject2" assembly // or
sbt subproject2/assembly
```

## Tests

### Running all tests with coverage and style check:

```bash
sbt clean coverage lock test it:test coverageReport coverageAggregate scalastyle
```

When measuring coverage, make sure to clean project otherwise it will not instrument properly. (To be precise coverage
cache should be clean if you want to have correct results - if you have just built project and haven't run any tests
with coverage enabled you don't have to clean anything).

### Selecting test suites

Running selected suite:

```bash
sbt common/test
sbt common/fun:test
sbt common/it:test
```

