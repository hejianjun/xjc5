package com.rx

import com.datastax.spark.connector.SomeColumns
import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by hejianjun on 2016/12/18.
  */
object Saving {
  def main(args: Array[String]) {

    val conf = new SparkConf().set("spark.cassandra.connection.host", "127.0.0.1").setMaster("local").setAppName("Simple Application")
    val sc = new SparkContext(conf)
    val collection = sc.parallelize(Seq(SimpleType("dog", 50), SimpleType("cow", 60)))
    collection.saveToCassandra("test", "words", SomeColumns("word", "count"))

    sc.stop()
  }
}
