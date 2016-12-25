package com.rx.cassandra

import com.rx.{ComplexType, Element, SimpleType}
import org.apache.spark.sql.cassandra._
import org.apache.spark.sql.{Dataset, SparkSession}

/**
  * Created by hejianjun on 2016/12/18.
  */
object Loading {
  val spark = SparkSession.builder
    .master("local")
    .appName("Simple Application")
    .config("spark.cassandra.connection.host", "127.0.0.1")
    .getOrCreate()
  import spark.implicits._
  def main(args: Array[String]) {


    /*
        spark.sql(
          """CREATE TEMPORARY TABLE complex_type
            |USING org.apache.spark.sql.cassandra
            |OPTIONS (
            |  table "complex_type",
            |  keyspace "xsd",
            |  cluster "Test Cluster",
            |  pushdown "true"
            |)""".stripMargin)
        val df = spark.sql("select * from complex_type")
        df.show()
    */
/*
    val df = getSimpleType()
      .filter("file=='_A_类型_基础.xsd'")
    val df2 =getComplexType()
      .flatMap(c => c.sequence.map(e => (c.file, c.name, e)))
    val count=df2.join(df, df2.col("_3.data_type") === df.col("name"))
      .groupBy("_1", "_2")
      .count()
      .count()
    println(count)
    */
    getComplexType().flatMap(_.sequence).filter("max_occurs > 1").show(1000)
  }
  def getSimpleType():Dataset[SimpleType]={
    spark
      .read
      .cassandraFormat("simple_type", "xsd", "Test Cluster")
      .load()
      .as[SimpleType]
  }
  def getComplexType():Dataset[ComplexType]={
    spark
      .read
      .cassandraFormat("complex_type", "xsd", "Test Cluster")
      .load()
      .as[ComplexType]
  }
  def getElement():Dataset[Element]={
    spark
      .read
      .cassandraFormat("elements", "xsd", "Test Cluster")
      .load()
      .as[Element]
  }
}