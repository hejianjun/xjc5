package com.rx


import org.apache.spark.sql.SparkSession

/**
  * Created by hejianjun on 2016/12/18.
  */
object Loading {
  def main(args: Array[String]) {

    val sparkSession = SparkSession.builder
      .master("local")
      .appName("Simple Application")
      .config("spark.cassandra.connection.host", "127.0.0.1")
      .getOrCreate()
    sparkSession.sql(
      """CREATE TEMPORARY TABLE complex_type
        |USING org.apache.spark.sql.cassandra
        |OPTIONS (
        |  table "complex_type",
        |  keyspace "xsd",
        |  cluster "Test Cluster",
        |  pushdown "true"
        |)""".stripMargin)
    val df = sparkSession.sql("select sequence.name from complex_type where sequence.data_type='N'")
    df.show()
  }
}