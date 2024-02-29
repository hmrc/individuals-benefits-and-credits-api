import sbt.Tests.{Group, SubProcess}
import uk.gov.hmrc.DefaultBuildSettings.addTestReportOption

val appName = "individuals-benefits-and-credits-api"

lazy val ComponentTest = config("component") extend Test

lazy val microservice =
  Project(appName, file("."))
    .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
    .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
    .settings(CodeCoverageSettings.settings *)
    .settings(scalaVersion := "2.13.12")
    .settings(scalafmtOnCompile := true)
    .settings(onLoadMessage := "")
    .settings(
      scalacOptions += "-Wconf:src=routes/.*:s",
      scalacOptions += "-Wconf:cat=unused-imports&src=txt/.*:s",
      libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test(),
      Test / testOptions := Seq(Tests.Filter((name: String) => name startsWith "unit")),
      routesImport := Seq("uk.gov.hmrc.individualsbenefitsandcreditsapi.Binders._")
    )
    .settings(Compile / unmanagedResourceDirectories += baseDirectory.value / "resources")
    .configs(IntegrationTest)
    .settings(inConfig(IntegrationTest)(Defaults.itSettings) *)
    .settings(
      IntegrationTest / Keys.fork := false,
      IntegrationTest / unmanagedSourceDirectories := (IntegrationTest / baseDirectory)(base => Seq(base / "test")).value,
      IntegrationTest / testOptions := Seq(Tests.Filter((name: String) => name startsWith "it")),
      addTestReportOption(IntegrationTest, "int-test-reports"),
      IntegrationTest / testGrouping := oneForkedJvmPerTest((IntegrationTest / definedTests).value),
      IntegrationTest / parallelExecution := false,
      // Disable default sbt Test options (might change with new versions of bootstrap)
      IntegrationTest / testOptions -= Tests
        .Argument("-o", "-u", "target/int-test-reports", "-h", "target/int-test-reports/html-report"),
      IntegrationTest / testOptions += Tests.Argument(
        TestFrameworks.ScalaTest,
        "-oNCHPQR",
        "-u",
        "target/int-test-reports",
        "-h",
        "target/int-test-reports/html-report")
    )
    .configs(ComponentTest)
    .settings(inConfig(ComponentTest)(Defaults.testSettings) *)
    .settings(
      ComponentTest / testOptions := Seq(Tests.Filter((name: String) => name startsWith "component")),
      ComponentTest / unmanagedSourceDirectories := (ComponentTest / baseDirectory)(base => Seq(base / "test")).value,
      ComponentTest / testGrouping := oneForkedJvmPerTest((ComponentTest / definedTests).value),
      ComponentTest / parallelExecution := false,
      // Disable default sbt Test options (might change with new versions of bootstrap)
      ComponentTest / testOptions -= Tests
        .Argument("-o", "-u", "target/component-test-reports", "-h", "target/component-test-reports/html-report"),
      ComponentTest / testOptions += Tests.Argument(
        TestFrameworks.ScalaTest,
        "-oNCHPQR",
        "-u",
        "target/component-test-reports",
        "-h",
        "target/component-test-reports/html-report")
    )
    .settings(PlayKeys.playDefaultPort := 9654)
    .settings(majorVersion := 0)
    // Disable default sbt Test options (might change with new versions of bootstrap)
    .settings(Test / testOptions -= Tests
      .Argument("-o", "-u", "target/test-reports", "-h", "target/test-reports/html-report"))
    // Suppress successful events in Scalatest in standard output (-o)
    // Options described here: https://www.scalatest.org/user_guide/using_scalatest_with_sbt
    .settings(Test / testOptions += Tests.Argument(
      TestFrameworks.ScalaTest,
      "-oNCHPQR",
      "-u",
      "target/test-reports",
      "-h",
      "target/test-reports/html-report"))

def oneForkedJvmPerTest(tests: Seq[TestDefinition]) =
  tests.map { test =>
    new Group(test.name, Seq(test), SubProcess(ForkOptions().withRunJVMOptions(Vector(s"-Dtest.name=${test.name}"))))
  }
