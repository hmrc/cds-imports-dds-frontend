import uk.gov.hmrc.DefaultBuildSettings.integrationTestSettings
import uk.gov.hmrc.SbtArtifactory
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings
import play.core.PlayVersion.{current => currentPlayVersion}

val appName = "cds-imports-dds-frontend"

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory)
  .settings(
    majorVersion                     := 0,
    libraryDependencies              ++= compileDeps ++ testDeps
  )
  .settings(publishingSettings: _*)
  .settings(scoverageSettings)
  .configs(IntegrationTest)
  .settings(integrationTestSettings(): _*)
  .settings(resolvers += Resolver.jcenterRepo)
  .settings(resolvers += "hmrc-releases" at "https://artefacts.tax.service.gov.uk/artifactory/hmrc-releases/")


lazy val scoverageSettings = {
  import scoverage.ScoverageKeys
  Seq(
    ScoverageKeys.coverageExcludedPackages := List("<empty>"
      ,"Reverse.*"
      ,".*(BuildInfo|Routes|testOnly).*").mkString(";"),
    ScoverageKeys.coverageMinimum := 80,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true,
    parallelExecution in Test := false
  )
}

scalacOptions += "-Ypartial-unification"


val compileDeps = Seq(

  "uk.gov.hmrc"             %% "play-frontend-govuk"      % "0.19.0-play-26",
  "uk.gov.hmrc"             %% "play-ui"                  % "8.2.0-play-26",
  "uk.gov.hmrc"             %% "bootstrap-play-26"        % "1.1.0",
  "org.scala-lang.modules"  %% "scala-xml"                % "1.2.0",
  "org.typelevel"           %% "cats-core"                % "2.0.0",
  "uk.gov.hmrc"             %% "simple-reactivemongo"     % "7.20.0-play-26"

)

val testDeps = Seq(
  "org.mockito"             % "mockito-core"              % "3.1.0"                 % "test,it",
  "uk.gov.hmrc"             %% "bootstrap-play-26"        % "1.1.0" % Test classifier "tests",
  "org.scalatest"           %% "scalatest"                % "3.0.8"                 % "test",
  "org.jsoup"               %  "jsoup"                    % "1.10.2"                % "test",
  "com.typesafe.play"       %% "play-test"                % currentPlayVersion      % "test",
  "org.pegdown"             %  "pegdown"                  % "1.6.0"                 % "test, it",
  "org.scalatestplus.play"  %% "scalatestplus-play"       % "3.1.2"                 % "test, it"
)
