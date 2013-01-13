name := "klara"

version := "1.0"

scalaVersion := "2.10.0"

// use src/main/webapp

unmanagedResourceDirectories in Compile <+= (baseDirectory) { _ / "src" / "main" / "webapp" }


// sbt-revolver

seq(Revolver.settings: _*)

// Repositories

resolvers ++= Seq(
	"spray.io nightlies" at "http://nightlies.spray.io/",
	"spray.io" at "http://repo.spray.io",
	"Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    "sgodbillon" at "https://bitbucket.org/sgodbillon/repository/raw/master/snapshots/"
)

// Libraries

libraryDependencies ++= Seq(
	"io.spray"								  %   "spray-routing"				% "1.1-20130108",
	"io.spray"								  %   "spray-can"					% "1.1-20130108",
	"io.spray"								  %   "spray-caching"				% "1.1-20130108",
    "io.spray"                                %%  "spray-json"                  % "1.2.3",
//    "io.spray"                              %%  "twirl-api"                   % "0.6.1",
	"org.scala-lang"                          %   "scala-reflect"               % "2.10.0",
    "com.typesafe.akka"                       %%  "akka-actor"                  % "2.1.0",
    "com.typesafe.akka"                       %%  "akka-slf4j"                  % "2.1.0",
    "com.typesafe.akka"                       %%  "akka-testkit"                % "2.1.0",
    "org.parboiled"                           %%  "parboiled-scala"             % "1.1.4",
    "com.chuusai"                             %%  "shapeless"                   % "1.2.3",
    "org.scalatest"                           %%  "scalatest"                   % "1.9.1",
    "org.specs2"                              %%  "specs2"                      % "1.12.3",
    "com.googlecode.concurrentlinkedhashmap"  %   "concurrentlinkedhashmap-lru" % "1.3.2",
    "ch.qos.logback"                          %   "logback-classic"             % "1.0.9",
    "org.jvnet.mimepull"                      %   "mimepull"                    % "1.9.1",
    "org.pegdown"                             %   "pegdown"                     % "1.2.1",
    "reactivemongo" %% "reactivemongo" % "0.1-SNAPSHOT" cross CrossVersion.full
)




