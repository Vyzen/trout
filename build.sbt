name := "Trout"

version := "0.1"

scalaVersion := "2.10.2"

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
    "releases" at "http://oss.sonatype.org/content/repositories/releases")
	
libraryDependencies ++= Seq("org.scalatest" % "scalatest_2.10" % "2.0" % "test",
    "com.novocode" % "junit-interface" % "0.10" % "test")
