name := "xjc5"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "com.datastax.spark" %% "spark-cassandra-connector" % "2.0.0-M3"

libraryDependencies += "org.apache.lucene" % "lucene-core" % "6.3.0"

libraryDependencies += "com.sksamuel.elastic4s" %% "elastic4s-core" % "5.1.1"

libraryDependencies += "org.apache.kafka" % "kafka-clients" % "0.10.0.1"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.0.2",

  "org.apache.spark" %% "spark-sql" % "2.0.2",

  "org.apache.spark" %% "spark-streaming" % "2.0.2",

  "org.apache.spark" %% "spark-sql-kafka-0-10" % "2.0.2"
)


// Add the POI core and OOXML support dependencies into your build.sbt
libraryDependencies ++= Seq(
  "org.apache.poi" % "poi" % "3.16-beta1",
  "org.apache.poi" % "poi-ooxml" % "3.16-beta1",
  "org.apache.poi" % "poi-ooxml-schemas" % "3.16-beta1",
  "org.apache.poi" % "poi-scratchpad" % "3.16-beta1"
)
libraryDependencies += "org.scalatest" %% "scalatest" % "2.0" % "test"



