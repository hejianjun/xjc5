package com.rx

import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.cassandra._
/**
  * Created by hejianjun on 2016/12/18.
  */
object Saving {
  def main(args: Array[String]) {

    val sparkSession = SparkSession.builder
      .master("local")
      .appName("Simple Application")
      .config("spark.cassandra.connection.host", "127.0.0.1")
      .getOrCreate()
    import sparkSession.implicits._
    Reader.getFileList("D:\\xsd2\\").foreach(file => {

      val simpleTypeDF = Reader.getSimpleType(file).toDS
      simpleTypeDF.write.cassandraFormat("simple_type", "xsd", "Test Cluster").mode(SaveMode.Append).save()

      val includeDF = Reader.getInclude(file).toDS
      includeDF.write.cassandraFormat("include", "xsd", "Test Cluster").mode(SaveMode.Append).save()

      val groupDF = Reader.getGroup(file).toDS
      groupDF.write.cassandraFormat("groups", "xsd", "Test Cluster").mode(SaveMode.Append).save()


      val complexTypeDF = Reader.getComplexType(file).toDS
      complexTypeDF.write.cassandraFormat("complex_type", "xsd", "Test Cluster").mode(SaveMode.Append).save()

      val elementDF = Reader.getElement(file).toDS
      elementDF.write.cassandraFormat("elements", "xsd", "Test Cluster").mode(SaveMode.Append).save()

    })
  }
}
