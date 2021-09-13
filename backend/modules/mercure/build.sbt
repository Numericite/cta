name := """mercure"""


version := "BUILD"


scalaVersion := "2.11.7"

//resolvers := ("Atlassian Releases" at "https://maven.atlassian.com/public/") +: resolvers.value
//resolvers += "Atlassian Releases" at "https://maven.atlassian.com/public/"
//resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
resolvers += Resolver.jcenterRepo
libraryDependencies ++= Seq(
  cache,
  filters,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "com.mohiva" %% "play-silhouette" % "4.0.0",
  "com.mohiva" %% "play-silhouette-password-bcrypt" % "4.0.0",
  "com.mohiva" %% "play-silhouette-testkit" % "4.0.0" % "test",
  "com.mohiva" %% "play-silhouette-crypto-jca" % "4.0.0",
  "com.mohiva" %% "play-silhouette-cas" % "4.0.0",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.14",
  "com.mohiva" %% "play-silhouette-persistence-reactivemongo" % "4.0.0",
  "org.webjars" %% "webjars-play" % "2.5.0-2",
  //"org.reactivemongo" %% "play2-reactivemongo" % "0.12.0-SNAPSHOT",
  "com.typesafe.play" % "play-mailer_2.11" % "5.0.0-M1",
  "net.codingwell" %% "scala-guice" % "4.0.1",
  "com.iheart" %% "ficus" % "1.2.3",
  "net.kaliber" %% "play-s3" % "8.0.0",
  "commons-io" % "commons-io" % "2.5",
  "org.jsoup" % "jsoup" % "1.8.3"
)


resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

resolvers += Resolver.sonatypeRepo("snapshots")

//routesGenerator := InjectedRoutesGenerator

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"


sources in (Compile, doc) := Seq.empty
publishArtifact in (Compile, packageDoc) := false


// enable improved (experimental) incremental compilation algorithm called "name hashing"
incOptions := incOptions.value.withNameHashing(true)
