package com.rx.poi

import com.rx.poi.model._
import org.apache.poi.hwpf.HWPFDocument
import org.apache.poi.hwpf.usermodel.{Table, TableIterator}

import scala.collection.immutable.IndexedSeq

import com.rx.poi.model.Implicit._

object HWPFMain extends App {
  val file = this.getClass.getClassLoader.getResource("doc/附件1 审判业务数据结构规范.doc").openStream()
  val doc = new HWPFDocument(file)
  val text = doc.getText
  val range = doc.getRange
  val it = new TableIterator(range)
  while (it.hasNext) {
    val tb = it.next()
    val tableName = getTableName(tb)
    getComplexType(tableName, tb).foreach(println)
  }
  doc.close()
  file.close()

  def getTableName(table: Table): String = {
    val end = table.getStartOffset
    val start = doc.getText.lastIndexOf(" ", end)
    text.substring(start, end).trim
  }

  def getComplexType(tableName: String, table: Table): IndexedSeq[Any] = {
    val head = row2seq(table.getRow(0), 2)
    var complexType = ComplexType(tableName, "")
    for {i <- 1 until table.numRows()
         tr = table.getRow(i)
         seq = row2seq(tr, head.length)
         row = (head, seq) match {
           case (Seq("形式", "说明"), Seq(name, explain)) => SimpleType(name, explain)
           case (Seq("类型简称", "类型说明"), Seq(name, explain)) => SimpleType(name, explain)
           case (Seq("序号", "数据项名称", "标识", "类型", "说明"), Seq(name)) => complexType = ComplexType(tableName, name); complexType
           case (Seq("序号", "数据项名称", "标识", "类型", "说明"), Seq(sequence, name, id, data_type, explain,_*)) => Element(complexType, sequence, name, id, data_type, explain)
           case _ => if (seq.nonEmpty) throw new Exception("无法识别" + seq + "列，有" + tr.numCells + "列,head为" + head)
         }
    } yield row
  }

  def getCodeTable(tableName: String, table: Table): IndexedSeq[Any] = {

    val head = row2seq(table.getRow(0), 2)
    for {i <- 1 until table.numRows()
         tr = table.getRow(i)
         seq = row2seq(tr, head.length)
         row = (head, seq) match {
           //case (Seq(), Seq()) =>
           case (Seq("代码", "名称"), Seq(code, name)) => CodeTable(code, name, null, null)
           case (Seq("代码", "名称", "备注"), Seq(code, name, explain)) => CodeTable(code, name, null, explain)
           case (Seq("代码", "名称", "说明"), Seq(code, name, explain)) => CodeTable(code, name, null, explain)
           case (Seq("代码", "上级代码", "名称"), Seq(code, parentCode, name)) => CodeTable(code, name, parentCode, null)
           case (Seq("代码", "上级代码", "内容"), Seq(code, parentCode, name)) => CodeTable(code, name, parentCode, null)
           case _ => if (seq.nonEmpty) throw new Exception("无法识别" + seq + "列，有" + tr.numCells + "列,head为" + head)
         }
    } yield row
  }
}