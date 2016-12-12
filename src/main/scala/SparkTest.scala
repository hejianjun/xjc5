/**
  * Created by hejianjun on 2016/12/7.
  */

import com.datastax.spark.connector.cql.CassandraConnector
import com.datastax.spark.connector._
import org.apache.spark.{SparkConf, SparkContext}

object SparkTest {
  def main(args: Array[String]) {

    val conf = new SparkConf().set("spark.cassandra.connection.host", "127.0.0.1").setMaster("local").setAppName("Simple Application")
    val sc = new SparkContext(conf)
    val rdd = sc.cassandraTable("test2", "words")
    // rdd: com.datastax.spark.connector.rdd.CassandraRDD[com.datastax.spark.connector.rdd.reader.CassandraRow] = CassandraRDD[0] at RDD at CassandraRDD.scala:41

    rdd.collect.foreach(println)

    CassandraConnector(conf).withSessionDo { session =>
      session.execute("CREATE KEYSPACE test2 WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1 }")
      session.execute("CREATE TABLE test2.words (word text PRIMARY KEY, count int)")
    }
    sc.stop()
  }
}
