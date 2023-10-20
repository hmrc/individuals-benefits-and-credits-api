import play.sbt.routes.RoutesKeys
import sbt.Keys.compile
import sbt.Tests.{Group, SubProcess}
import uk.gov.hmrc.DefaultBuildSettings.{addTestReportOption, defaultSettings, scalaSettings}

RoutesKeys.routesImport := Seq("uk.gov.hmrc.individualsbenefitsandcreditsapi.Binders._")

val appName = "individuals-benefits-and-credits-api"

lazy val scoverageSettings = {
  import scoverage.ScoverageKeys
  Seq(
    // Semicolon-separated list of regexs matching classes to exclude
    ScoverageKeys.coverageExcludedPackages := "<empty>;Reverse.*;uk.gov.hmrc.individualsbenefitsandcreditsapi.views.*;" +
      ".*BuildInfo.;uk.gov.hmrc.BuildInfo;.*Routes;.*RoutesPrefix*;",
    ScoverageKeys.coverageMinimumStmtTotal := 80,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )
}

def intTestFilter(name: String): Boolean = name startsWith "it"
def unitFilter(name: String): Boolean = name startsWith "unit"
def componentFilter(name: String): Boolean = name startsWith "component"

lazy val microservice =
  Project(appName, file("."))
    .enablePlugins(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin)
    .settings(scalaSettings: _*)
    .settings(scoverageSettings: _*)
    .settings(ThisBuild / useSuperShell := false)
    .settings(scalaVersion := "2.13.8")
    .settings(onLoadMessage := "")
    .settings(defaultSettings(): _*)
    .settings(
      scalacOptions += "-Wconf:src=routes/.*:s",
      scalacOptions += "-Wconf:cat=unused-imports&src=txt/.*:s",
      libraryDependencies ++= (AppDependencies.compile ++ AppDependencies
        .test()),
      Test / testOptions := Seq(Tests.Filter(unitFilter)),
      retrieveManaged := true,
    )
    .settings(Compile / unmanagedResourceDirectories += baseDirectory.value / "resources")
    .configs(IntegrationTest)
    .settings(inConfig(IntegrationTest)(Defaults.itSettings): _*)
    .settings(
      IntegrationTest / Keys.fork := false,
      IntegrationTest / unmanagedSourceDirectories := (IntegrationTest / baseDirectory)(
        base => Seq(base / "test")).value,
      IntegrationTest / testOptions := Seq(Tests.Filter(intTestFilter)),
      addTestReportOption(IntegrationTest, "int-test-reports"),
      IntegrationTest / testGrouping := oneForkedJvmPerTest(
        (IntegrationTest / definedTests).value),
      IntegrationTest / parallelExecution := false
    )
    .configs(ComponentTest)
    .settings(inConfig(ComponentTest)(Defaults.testSettings): _*)
    .settings(
      ComponentTest / testOptions := Seq(Tests.Filter(componentFilter)),
      ComponentTest / unmanagedSourceDirectories := (ComponentTest / baseDirectory)(
        base => Seq(base / "test")).value,
      ComponentTest / testGrouping := oneForkedJvmPerTest(
        (ComponentTest / definedTests).value),
      ComponentTest / parallelExecution := false
    )
    .settings(resolvers ++= Seq(
      Resolver.jcenterRepo
    ))
    .settings(PlayKeys.playDefaultPort := 9654)
    .settings(majorVersion := 0)

lazy val ComponentTest = config("component") extend Test

def oneForkedJvmPerTest(tests: Seq[TestDefinition]) =
  tests.map { test =>
    new Group(
      test.name,
      Seq(test),
      SubProcess(
        ForkOptions().withRunJVMOptions(Vector(s"-Dtest.name=${test.name}"))))
  }

lazy val compileAll = taskKey[Unit]("Compiles sources in all configurations.")

compileAll := {
  val a = (Test / compile).value
  val b = (IntegrationTest / compile).value
  ()
}
