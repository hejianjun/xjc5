package com.rx

import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.cassandra._

/**
  * Created by hejianjun on 2016/12/18.
  */
object Saving2 {
  def main(args: Array[String]) {

    val sparkSession = SparkSession.builder
      .master("local")
      .appName("Simple Application")
      .config("spark.cassandra.connection.host", "127.0.0.1")
      .getOrCreate()
    Reader.getFileList("D:\\xsd2\\").foreach(file => {

      val simpleTypeDF = sparkSession.createDataFrame[SimpleType](Reader.getSimpleType(file))
      simpleTypeDF.write.cassandraFormat("simple_type", "xsd", "Test Cluster").mode(SaveMode.Append).save()

      val includeDF = sparkSession.createDataFrame[Include](Reader.getInclude(file)).withColumnRenamed("schemaLocation", "schema_location")
      includeDF.write.cassandraFormat("include", "xsd", "Test Cluster").mode(SaveMode.Append).save()

      val group = Reader.getGroup(file)
      val groupDF = sparkSession.createDataFrame[Group](group).drop("element")
      groupDF.write.cassandraFormat("group", "xsd", "Test Cluster").mode(SaveMode.Append).save()

      val groupElementDF = sparkSession
        .createDataFrame[Element](group.flatMap(es=>es.element.map(e=>{e.groupName=es.name;e})))
        .withColumnRenamed("dataType", "data_type")
        .withColumnRenamed("minOccurs", "min_occurs")
        .withColumnRenamed("maxOccurs", "max_occurs")
        .withColumnRenamed("groupName", "group_name")
      groupElementDF.write.cassandraFormat("element", "xsd", "Test Cluster").mode(SaveMode.Append).save()

      val complexType = Reader.getComplexType(file)
      val complexTypeDF = sparkSession.createDataFrame[ComplexType](complexType).drop("element").drop("group").drop("choice")

      complexTypeDF.write.cassandraFormat("complex_type", "xsd", "Test Cluster").mode(SaveMode.Append).save()

      val complexTypeElementDF = sparkSession
        .createDataFrame[Element](complexType.flatMap(es=>es.element.map(e=>{e.complexName=es.name;e})))
        .withColumnRenamed("dataType", "data_type")
        .withColumnRenamed("minOccurs", "min_occurs")
        .withColumnRenamed("maxOccurs", "max_occurs")
        .withColumnRenamed("complexName", "complex_name")

      complexTypeElementDF.write.cassandraFormat("element", "xsd", "Test Cluster").mode(SaveMode.Append).save()

      val groupRefDF = sparkSession
        .createDataFrame[GroupRef](complexType.flatMap(cs=>cs.group.map(c=>{c.complexName=cs.name;c})))
        .withColumnRenamed("minOccurs", "min_occurs")
        .withColumnRenamed("maxOccurs", "max_occurs")
        .withColumnRenamed("complexName", "complex_name")
      groupRefDF.write.cassandraFormat("group_ref", "xsd", "Test Cluster").mode(SaveMode.Append).save()

      val choiceDF = sparkSession
        .createDataFrame[GroupRef](complexType.flatMap(cs=>cs.choice.map(c=>{c.complexName=cs.name;c})))
        .withColumnRenamed("minOccurs", "min_occurs")
        .withColumnRenamed("maxOccurs", "max_occurs")
        .withColumnRenamed("complexName", "complex_name")
      choiceDF.write.cassandraFormat("group_ref", "xsd", "Test Cluster").mode(SaveMode.Append).save()

      val elementDF = sparkSession.createDataFrame[Element](Reader.getElement(file))
        .withColumnRenamed("dataType", "data_type")
        .withColumnRenamed("minOccurs", "min_occurs")
        .withColumnRenamed("maxOccurs", "max_occurs")
      elementDF.write.cassandraFormat("element", "xsd", "Test Cluster").mode(SaveMode.Append).save()
    })
  }
}
