package com.rx.cassandra

import com.rx.poi.HWPFReader
import com.rx.xsd.model.{ComplexType, Element, SimpleType}
import org.apache.spark.sql.cassandra._
import org.apache.spark.sql.{Dataset, SparkSession}

/**
  * Created by hejianjun on 2016/12/18.
  */
object Loading2 {

  val spark = SparkSession.builder
    .master("local")
    .appName("Simple Application")
    .config("spark.cassandra.connection.host", "127.0.0.1")
    .getOrCreate()

  import spark.implicits._

  def main(args: Array[String]) {

    val reader = new HWPFReader("doc/附件2 审判业务标准代码.doc")
    reader.read(reader.readCode)
    reader.item.toDS.filter(_.code=="").show

  }

}