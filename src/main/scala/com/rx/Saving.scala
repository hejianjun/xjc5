package com.rx


import com.datastax.spark.connector.SomeColumns
import com.datastax.spark.connector._
import org.apache.spark.{SparkContext, SparkConf}


/**
  * Created by hejianjun on 2016/12/18.
  */
object Saving {
  def main(args: Array[String]) {

    val conf = new SparkConf().set("spark.cassandra.connection.host", "127.0.0.1").setMaster("local").setAppName("Simple Application")
    val sc = new SparkContext(conf)
    Reader.getFileList("D:\\xsd2\\").foreach(file => {
      val simpleTypeRDD = sc.parallelize[SimpleType](Reader.getSimpleType(file))
      simpleTypeRDD.saveToCassandra("xsd", "simple_type", SomeColumns("name", "base", "restriction"))

      val includeRDD = sc.parallelize[Include](Reader.getInclude(file))
      includeRDD.saveToCassandra("xsd", "include", SomeColumns("file", "schema_location"))

      val group = Reader.getGroup(file)
      val groupRDD = sc.parallelize[Group](group)
      groupRDD.saveToCassandra("xsd", "group", SomeColumns("file", "name"))

      val groupElementRDD = sc.parallelize[Element](group.flatMap(es=>es.element.map(e=>{e.groupName=es.name;e})))
      groupElementRDD.saveToCassandra("xsd", "element", SomeColumns("file", "name", "data_type", "min_occurs", "max_occurs","group_name"))

      val complexType = Reader.getComplexType(file)
      val complexTypeRDD = sc.parallelize[ComplexType](complexType)
      complexTypeRDD.saveToCassandra("xsd", "complex_type", SomeColumns("file", "name"))

      val complexTypeElementRDD = sc.parallelize[Element](group.flatMap(es=>es.element.map(e=>{e.complexName=es.name;e})))
      complexTypeElementRDD.saveToCassandra("xsd", "element", SomeColumns("file", "name", "data_type", "min_occurs", "max_occurs","complex_name"))

      val elementRDD = sc.parallelize[Element](Reader.getElement(file))
      elementRDD.saveToCassandra("xsd", "element", SomeColumns("file", "name", "data_type", "min_occurs", "max_occurs"))

    })
    sc.stop()
  }
}
