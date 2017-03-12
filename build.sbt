name := """playScalaBasic"""

version := "1.0-SNAPSHOT"

lazy val commonSettings = Seq(
  scalaVersion := "2.11.8",
  routesGenerator := InjectedRoutesGenerator
)

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLayoutPlugin)
  .dependsOn(domain, port)
  .settings(commonSettings ++ Seq(
    libraryDependencies ++= Seq(
      jdbc,
      cache,
      ws,
      filters,
      specs2 % Test,
      evolutions,
      "mysql" % "mysql-connector-java" % "5.1.39"
    ))
  )

lazy val application =
  (project in file("application"))
    .dependsOn(utility)
    .settings(
      libraryDependencies ++= Seq("com.typesafe.play" %% "anorm" % "2.5.0")
    )
    .settings(commonSettings)

lazy val domain =
  (project in file("domain"))
    .dependsOn(utility, application)
    .settings(commonSettings)

lazy val port =
  (project in file("port"))
    .settings(commonSettings)
    .aggregate(portWebService, portDatabase)
    .dependsOn(portWebService, portDatabase)

lazy val portWebService =
  (project in file("port/primary/webService"))
    .enablePlugins(PlayScala)
    .dependsOn(utility, application, domain)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(ws,filters)
    )

lazy val portDatabase =
  (project in file("port/secondary/database"))
    .enablePlugins(PlayScala)
    .disablePlugins(PlayLayoutPlugin)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq("com.typesafe.play" %% "anorm" % "2.5.0")
    )
    .dependsOn(utility, application, domain)

lazy val utility =
  (project in file("utility"))
    .settings(commonSettings)
    .enablePlugins(PlayScala)
    .disablePlugins(PlayLayoutPlugin)
    .settings(
    libraryDependencies ++= Seq(jdbc
  ,"com.typesafe.play" %% "anorm" % "2.5.0"
  ,"com.typesafe.play" %% "play-mailer" % "4.0.0"
  ,"org.scalaz" %% "scalaz-core" % "7.1.5"
  ,"com.github.tototoshi" %% "scala-csv" % "1.3.1"))
