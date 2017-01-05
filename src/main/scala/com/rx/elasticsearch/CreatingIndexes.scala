package com.rx.elasticsearch

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.analyzers.{StopAnalyzerDefinition, CustomAnalyzerDefinition}
import com.sksamuel.elastic4s.mappings.IdField
import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri}

/**
  * Created by hejianjun on 2017/1/4.
  */
object CreatingIndexes {
  val uri = ElasticsearchClientUri("elasticsearch://localhost:9300")

  def main(args: Array[String]) {
    val client = ElasticClient.transport(uri)
    /*
    val delReq=client.execute {
      deleteIndex("poi")
    }.await
    println(delReq)
    */
    val req=client.execute {
      createIndex("poi") mappings(
        mapping("CodeTable") as(
          textField("name"),
          textField("number"),
          textField("explain")
          ),
        mapping("ComplexType") as(
          textField("ajlx"),
          textField("name")
          ),
        mapping("Customary") as(
          textField("format"),
          textField("explain")
          ),
        mapping("Element") as(
          textField("ajlx"),
          textField("cname"),
          textField("sequence"),
          textField("name"),
          textField("id"),
          textField("data_type"),
          textField("explain")
          ),
        mapping("Item") as(
          textField("code"),
          textField("name"),
          textField("parent_code"),
          textField("explain"),
          textField("number")
          ),
        mapping("Rule") as(
          textField("name"),
          textField("number"),
          textField("explain"),
          textField("code"),
          textField("is_null"),
          textField("data_check"),
          textField("logic_check"),
          textField("remarks"),
          nestedField("map")
          ),
        mapping("SimpleType") as(
          textField("name"),
          textField("explain")
          )
        )
    }.await
    println(req)
    client.close()
  }
}
