import sbt.Setting
import scoverage.ScoverageKeys

object CodeCoverageSettings {

  private val excludedPackages: Seq[String] = Seq(
    "<empty>",
    "Reverse.*",
    "uk.gov.hmrc.individualsbenefitsandcreditsapi.views.*",
    ".*BuildInfo.",
    "uk.gov.hmrc.BuildInfo",
    ".*Routes",
    ".*RoutesPrefix*",
    "uk.gov.hmrc.individualsbenefitsandcreditsapi.audit.models.*",
  )

  val settings: Seq[Setting[?]] = Seq(
    ScoverageKeys.coverageExcludedPackages := excludedPackages.mkString(";"),
    ScoverageKeys.coverageMinimumStmtTotal := 80,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )
}
