ThisBuild / scalaVersion := "3.3.6"
ThisBuild / version := "1.0.0"
ThisBuild / organization := "com.example"

lazy val doobieVersion = "1.0.0-RC10"
val Http4sVersion = "0.23.30"
val CirceVersion = "0.14.10"
val MunitVersion = "1.1.0"
val LogbackVersion = "1.5.16"
val MunitCatsEffectVersion = "2.0.0"

lazy val `lib-util` = (project in file("./lib-util"))

lazy val `lib-infrastructure-mysql` = (project in file("./lib-infrastructure-mysql"))
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.4.4",
      "com.mysql" % "mysql-connector-j" % "9.4.0",
      "org.tpolecat" %% "doobie-core"     % doobieVersion,
      "org.tpolecat" %% "doobie-specs2"   % doobieVersion,
      "org.tpolecat" %% "doobie-hikari"   % doobieVersion,
      "com.zaxxer" % "HikariCP" % "7.0.0"
    )
  )

lazy val `lib-domain` = (project in file("./lib-domain"))

lazy val `todo-app` = (project in file("."))
  .aggregate(`lib-util`, `lib-infrastructure-mysql`, `lib-domain`)
  .settings(
    name := "todo-app",
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-ember-server" % Http4sVersion,
      "org.http4s"      %% "http4s-ember-client" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "org.scalameta"   %% "munit"               % MunitVersion           % Test,
      "org.typelevel"   %% "munit-cats-effect"   % MunitCatsEffectVersion % Test,
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion         % Runtime,
    ),
    Compile / unmanagedSourceDirectories += baseDirectory.value / "scala",
    Compile / unmanagedResourceDirectories += baseDirectory.value / "resources",
    assembly / assemblyMergeStrategy := {
      case "module-info.class" => MergeStrategy.discard
      case x => (assembly / assemblyMergeStrategy).value.apply(x)
    }
  )
