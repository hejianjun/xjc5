package com.rx.elasticsearch

import com.rx.poi.HWPFReader
import com.rx.poi.model._
import com.sksamuel.elastic4s.{Indexable, ElasticsearchClientUri, ElasticClient}
import com.sksamuel.elastic4s.ElasticDsl._
import org.json4s.NoTypeHints
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization._

/**
  * Created by hejianjun on 2016/12/28.
  */
object Saving {
  val uri = ElasticsearchClientUri("elasticsearch://localhost:9300")

  def main(args: Array[String]) {

    val client = ElasticClient.transport(uri)
    implicit val formats = Serialization.formats(NoTypeHints)
    implicit object SimpleTypeIndexable extends Indexable[SimpleType] {
      override def json(t: SimpleType): String = write(t)
    }
    implicit object ComplexTypeIndexable extends Indexable[ComplexType] {
      override def json(t: ComplexType): String = write(t)
    }
    implicit object ElementIndexable extends Indexable[Element] {
      override def json(t: Element): String = write(t)
    }
    implicit object CodeTableIndexable extends Indexable[CodeTable] {
      override def json(t: CodeTable): String = write(t)
    }
    implicit object ItemIndexable extends Indexable[Item] {
      override def json(t: Item): String = write(t)
    }
    implicit object RuleIndexable extends Indexable[Rule] {
      override def json(t: Rule): String = write(t)
    }
    val reader1 = new HWPFReader("doc/附件1 审判业务数据结构规范.doc")
    reader1.read(reader1.readDataType)

    val reader2 = new HWPFReader("doc/附件2 审判业务标准代码.doc")
    reader2.read(reader2.readCode)

    val reader3 = new HWPFReader("doc/附件3 审判业务逻辑与数据校验规则.doc")
    reader3.read(reader3.readRule)

    val req = client.execute {
      bulk(
        reader1.simpleType.map(s => indexInto("poi" / "SimpleType").doc(s).id(s.name)) ++
          reader1.complexType.map(s => indexInto("poi" / "ComplexType").doc(s).id(s.ajlx + ":" + s.name)) ++
          reader1.element.map(s => indexInto("poi" / "Element").doc(s).id(s.ajlx + ":" + s.cname + ":" + s.name)) ++
          //reader1.customary.map(s => indexInto("poi" / "Customary").doc(s).id(s.format)) ++
          reader2.codeTable.map(s => indexInto("poi" / "CodeTable").doc(s).id(s.number)) ++
          reader2.item.map(s => indexInto("poi" / "Item").doc(s).id(s.number+""+s.code)) ++
          reader3.rule.map(s => indexInto("poi" / "Rule").doc(s).id(s.number))
      )
    }.await
    println(req.failures)
    println(req.successes)
    client.close()
  }
}
