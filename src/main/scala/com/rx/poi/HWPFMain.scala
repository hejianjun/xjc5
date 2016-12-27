package com.rx.poi

import com.rx.poi.model._
import org.apache.poi.hwpf.HWPFDocument
import org.apache.poi.hwpf.usermodel.{Table, TableIterator}

import scala.collection.immutable.IndexedSeq


object HWPFMain extends App {
  val file = this.getClass.getClassLoader.getResource("doc/附件2 审判业务标准代码.doc").openStream()
  val doc = new HWPFDocument(file)
  val text = doc.getText
  val range = doc.getRange
  val it = new TableIterator(range)
  while (it.hasNext) {
    val tb = it.next()
    val end = tb.getStartOffset
    val start = doc.getText.lastIndexOf(" ", end)
    val tableName = text.substring(start, end).trim
    getCodeTable(tableName, tb).foreach(println)
  }
  doc.close()
  file.close()

  def getComplexType(tableName: String, table: Table): IndexedSeq[Any] = {
    var complexType = ComplexType(tableName, "")
    for {i <- 1 until table.numRows()
         tr = table.getRow(i)
         row = tr.numCells match {
           case 0 =>
           case 1 => complexType = ComplexType(tableName, tr); complexType
           case 2 => SimpleType(tr)
           case 5 => Element(complexType, tr)
           case _ => val arr = tr.text.split("\u0007")
             if (arr.length == 5) {
               Element(complexType, arr(0), arr(1), arr(2), arr(3), arr(4))
             } else if (arr.length == 4) {
               Element(complexType, arr(0), arr(1), arr(2), arr(3), null)
             } else {
               throw new Exception("无法识别" + tr.text + "列，有" + tr.numCells + "列")
             }
         }
    } yield row
  }

  def getCodeTable(tableName: String, table: Table): IndexedSeq[Any] = {
    import com.rx.poi.model.Implicit._
    val head = row2seq(table.getRow(0),2)
    for {i <- 1 until table.numRows()
         tr = table.getRow(i)
         seq = row2seq(tr,head.length)
         row = (head, seq) match {
           case (Seq(), Seq()) =>
           case (Seq("代码", "名称"), Seq(code, name)) => CodeTable(code, name, null, null)
           case (Seq("代码", "名称", "备注"), Seq(code, name, explain)) => CodeTable(code, name, null, explain)
           case (Seq("代码", "名称", "说明"), Seq(code, name, explain)) => CodeTable(code, name, null, explain)
           case (Seq("代码", "上级代码", "名称"), Seq(code, parentCode, name)) => CodeTable(code, name, parentCode, null)
           case (Seq("代码", "上级代码", "内容"), Seq(code, parentCode, name)) => CodeTable(code, name, parentCode, null)
           case _ => if(seq.nonEmpty)throw new Exception("无法识别" + seq+ "列，有" + tr.numCells+ "列,head为"+head)
         }
    } yield row
  }
}