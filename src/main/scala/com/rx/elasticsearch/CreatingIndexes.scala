package com.rx.elasticsearch

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.analyzers.{StopAnalyzerDefinition, CustomAnalyzerDefinition}
import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri}

/**
  * Created by hejianjun on 2017/1/4.
  */
object CreatingIndexes {
  val uri = ElasticsearchClientUri("elasticsearch://localhost:9300")

  def main(args: Array[String]) {
    val client = ElasticClient.transport(uri)
    //client.execute {
    println (createIndex("poi")  mappings (
        mapping("CodeTable") as (
          keywordField("name"),
          textField("number"),
          textField("explain")
          )
        ) )
    //}
    client.close()
  }
}
