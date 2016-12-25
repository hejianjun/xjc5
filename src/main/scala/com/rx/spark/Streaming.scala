package com.rx.spark

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
/**
  * Created by hejianjun on 2016/12/22.
  */
object Streaming {
  def main(args: Array[String]) {
    val conf = new SparkConf().set("spark.cassandra.connection.host", "127.0.0.1").setMaster("local[2]").setAppName("Simple Application")
    val ssc = new StreamingContext(conf, Seconds(1))
    val customReceiverStream = ssc.receiverStream(new CustomReceiver("D:\\xsd2\\"))
    /*
    customReceiverStream.foreachRDD(complexTypeRDD=>{
      complexTypeRDD.saveToCassandra("xsd", "complex_type", SomeColumns("file", "name","sequence"))
    })
    */
    ssc.checkpoint("D:\\checkpointDirectory\\")
    customReceiverStream.countByWindow(Seconds(30), Seconds(10)).print()
    ssc.start()             // Start the computation
    ssc.awaitTermination()  // Wait for the computation to terminate
  }
}
