name := "Trout"

version := "0.1"

scalaVersion := "2.12.1"

scalacOptions ++= Seq("-unchecked", "-deprecation","-feature")

resolvers += ("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots").withAllowInsecureProtocol(true)
resolvers +=  ("releases" at "http://oss.sonatype.org/content/repositories/releases").withAllowInsecureProtocol(true)
	
libraryDependencies ++= Seq("org.scalactic" %% "scalactic" % "3.0.1",
	"org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "com.novocode" % "junit-interface" % "0.10" % "test")
