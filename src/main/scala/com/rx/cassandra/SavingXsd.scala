package com.rx.cassandra

import com.rx.xsd.Reader
import org.apache.spark.sql.cassandra._
import org.apache.spark.sql.{Dataset, SaveMode, SparkSession}

import scala.reflect.io.Path

/**
  * Created by hejianjun on 2016/12/18.
  */
object SavingXsd {
  def main(args: Array[String]) {

    val spark = SparkSession.builder
      .master("local")
      .appName("Simple Application")
      .config("spark.cassandra.connection.host", "127.0.0.1")
      .getOrCreate()
    import spark.implicits._
    val resource=this.getClass.getClassLoader.getResource("")
    Path(resource.getPath+"/xsd/").walkFilter(p=>{p.isFile && p.extension=="xsd"}).map(f=>Reader(f.jfile)).foreach(reader => {

      val simpleTypeDF = reader.getSimpleType.toDS
      writeXsd(simpleTypeDF,"simple_type")

      val includeDF = reader.getInclude.toDS
      writeXsd(includeDF,"include")

      val groupDF = reader.getGroup.toDS
      writeXsd(groupDF,"groups")

      val complexTypeDF = reader.getComplexType.toDS
      writeXsd(complexTypeDF,"complex_type")

      val elementDF = reader.getElement.toDS
      writeXsd(elementDF,"elements")

    })
  }
  def writeXsd[T](df:Dataset[T],table: String) ={
    df.write.cassandraFormat(table, "xsd", "Test Cluster").mode(SaveMode.Append).save()
  }

}
