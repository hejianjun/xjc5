package com.rx.spark

import org.apache.spark.sql.{Row, ForeachWriter, SparkSession}

/**
  * Created by hejianjun on 2016/12/22.
  */
object Structured {
  def main(args: Array[String]) {
    val spark = SparkSession.builder
      .master("local[2]")
      .appName("Simple Application")
      .config("spark.cassandra.connection.host", "127.0.0.1")
      .config("spark.sql.warehouse.dir", "/tmp")
      .getOrCreate()
    import spark.implicits._
    // Subscribe to 1 topic
    val ds1 = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "test")
      .load()
    val words = ds1.selectExpr("CAST(value AS STRING)")
      .as[String]
    val wordCounts = words.groupBy("value").count()
    val query = wordCounts
      .writeStream
      .outputMode("complete")
      .format("console")
      .start()
    query.awaitTermination() // block until any one of them terminates
  }
}
