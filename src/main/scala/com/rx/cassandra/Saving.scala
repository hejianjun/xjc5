package com.rx.cassandra

import com.rx.xsd.Reader
import org.apache.spark.sql.cassandra._
import org.apache.spark.sql.{Dataset, SaveMode, SparkSession}
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
      writeXsd(simpleTypeDF,"simple_type")

      val includeDF = Reader.getInclude(file).toDS
      writeXsd(includeDF,"include")

      val groupDF = Reader.getGroup(file).toDS
      writeXsd(groupDF,"groups")

      val complexTypeDF = Reader.getComplexType(file).toDS
      writeXsd(complexTypeDF,"complex_type")

      val elementDF = Reader.getElement(file).toDS
      writeXsd(elementDF,"elements")

    })
  }
  def writeXsd[T](df:Dataset[T],table: String) ={
    df.write.cassandraFormat(table, "xsd", "Test Cluster").mode(SaveMode.Append).save()
  }
}
