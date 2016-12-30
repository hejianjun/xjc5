package com.rx.cassandra

import com.rx.poi.HWPFReader
import org.apache.spark.sql.cassandra._
import org.apache.spark.sql.{Dataset, SaveMode, SparkSession}

/**
  * Created by hejianjun on 2016/12/18.
  */
object SavingPoi {
  def main(args: Array[String]) {

    val spark = SparkSession.builder
      .master("local")
      .appName("Simple Application")
      .config("spark.cassandra.connection.host", "127.0.0.1")
      .getOrCreate()
    import spark.implicits._
    val reader1 = new HWPFReader("doc/附件1 审判业务数据结构规范.doc")
    reader1.read(reader1.readDataType)
    writePoi(reader1.simpleType.toDS, "simple_type")
    writePoi(reader1.complexType.toDS, "complex_type")
    writePoi(reader1.element.toDS, "element")

    val reader2 = new HWPFReader("doc/附件2 审判业务标准代码.doc")
    reader2.read(reader2.readCode)
    writePoi(reader2.codeTable.toDS, "code_table")
    writePoi(reader2.item.toDS, "item")

    val reader3 = new HWPFReader("doc/附件3 审判业务逻辑与数据校验规则.doc")
    reader3.read(reader3.readRule)
    writePoi(reader3.rule.toDS, "rule")
  }

  def writePoi[T](df: Dataset[T], table: String) = {
    df.write.cassandraFormat(table, "poi", "Test Cluster").mode(SaveMode.Append).save()
  }

}
