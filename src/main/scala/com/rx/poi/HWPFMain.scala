package com.rx.poi

import com.rx.poi.model._

import org.apache.poi.hwpf.usermodel.Table


import com.rx.poi.model.Implicit._

case class HWPFMain(text: java.lang.StringBuilder) {
  var start = 16

  def getTableName(table: Table): String = {
    val end = table.getStartOffset
    start = text.lastIndexOf(" ", end)
    if (end - start > 40) {
      start = end - 40
    }
    text.substring(start, end).trim.replaceAll("\r", "\n")
  }

  def getBefore(table: Table): String = {
    val end = table.getStartOffset
    val before = text.substring(start, end).trim.replaceAll("\r", "\n")
    start = table.getEndOffset
    before
  }

  def getComplexType(tableName: String, table: Table): IndexedSeq[Any] = {
    val head = row2seq(table.getRow(0))
    var complexType = ComplexType(tableName, "")
    for {i <- 1 until table.numRows()
         tr = table.getRow(i)
         seq = row2seq(tr)
         row = (head, seq) match {
           case (Seq("形式", "说明"), Seq(format, explain)) => Customary(format, explain)
           case (Seq("类型简称", "类型说明"), Seq(name, explain)) => SimpleType(name, explain)
           case (Seq("序号", "数据项名称", "标识", "类型", "说明"), Seq(name)) => complexType = ComplexType(tableName, name); complexType
           case (Seq("序号", "数据项名称", "标识", "类型", "说明"), Seq(sequence, name, id, data_type, explain, _*)) => Element(complexType, sequence, name, id, data_type, explain)
           case _ => if (seq.nonEmpty) {
             row2seq(tr, 5) match {
               case Seq(sequence, name, id, data_type, explain) => Element(complexType, sequence, name, id, data_type, explain)
               case _ => throw new Exception("无法识别" + seq + "列，有" + tr.numCells + "列,head为" + head)
             }
           }
         }
    } yield row
  }

  def getCodeTable(before: String): CodeTable = {
    val codeTable =
      """[\s\S]*\[[0-9]+\]([\w\W]+)\s1. 编号：(GF[0-9]+-[0-9]+)\s2. 说明\s([\w\W]+)\s3. 代码表[\s]*""".r
    val codeTable(name, number, explain) = before
    CodeTable(name, number, explain)
  }

  def getItem(codeTable: CodeTable, table: Table): IndexedSeq[Any] = {
    val number = codeTable.number
    val head = row2seq(table.getRow(0), 2)
    for {i <- 1 until table.numRows()
         tr = table.getRow(i)
         seq = row2seq(tr, head.length)

         row = (head, seq) match {
           //case (Seq(), Seq()) =>
           case (Seq("代码", "名称"), Seq(code, name)) => Item(code, name, null, null,number)
           case (Seq("代码", "名称", "备注"), Seq(code, name, explain)) => Item(code, name, null, explain,number)
           case (Seq("代码", "名称", "说明"), Seq(code, name, explain)) => Item(code, name, null, explain,number)
           case (Seq("代码", "上级代码", "名称"), Seq(code, parentCode, name)) => Item(code, name, parentCode, null,number)
           case (Seq("代码", "上级代码", "内容"), Seq(code, parentCode, name)) => Item(code, name, parentCode, null,number)
           case _ => if (seq.nonEmpty) throw new Exception("无法识别" + seq + "列，有" + tr.numCells + "列,head为" + head)
         }
    } yield row
  }
}