name := """cta"""

version := "1.0-SNAPSHOT"


lazy val mercure = (project in file("modules/mercure")).enablePlugins(PlayScala)
lazy val root = (project in file(".")).enablePlugins(PlayScala).dependsOn(mercure).aggregate(mercure)

scalaVersion := "2.11.7"



libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "com.iheart" %% "play-swagger" % "0.4.0",
  "org.webjars" % "swagger-ui" % "2.1.4",
  "io.github.cloudify" %% "spdf" % "1.4.0"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
resolvers += Resolver.jcenterRepo


resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

resolvers += Resolver.sonatypeRepo("snapshots")

routesGenerator := InjectedRoutesGenerator

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
//activator run settings
PlayKeys.devSettings := Seq("play.server.http.port" -> "7000")

//javaOptions in Test ++= Seq("-Dconfig.file=conf/application-test.conf", "-Dlogger.resource=test-logger.xml")
//javaOptions ++= Seq("-Xms512m -Xmx512m")
//javaOptions in run := Seq("-Xms512m", "-Xmx128m")



