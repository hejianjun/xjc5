package com.rx.poi

import com.rx.poi.model._
import org.apache.poi.hwpf.HWPFDocument

import org.apache.poi.hwpf.usermodel.{TableIterator, Table}


import com.rx.poi.model.Implicit._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

case class HWPFReader(path: String) {
  var rule = new ListBuffer[Rule]


  var customary = new ListBuffer[Customary]
  var simpleType = new ListBuffer[SimpleType]
  var complexType = new ListBuffer[ComplexType]
  var element = new ListBuffer[Element]
  var item = new ListBuffer[Item]
  var codeTable = new ListBuffer[CodeTable]

  type readFunction = (Table, String, Int) => Int

  def read(readFunction: readFunction) = {
    var start = 16
    val file = this.getClass.getClassLoader.getResourceAsStream(path)
    val doc = new HWPFDocument(file)
    val text = doc.getText.toString
    val range = doc.getRange
    val it = new TableIterator(range)
    while (it.hasNext) {
      val tb = it.next()
      start = readFunction(tb, text, start)
    }
    doc.close()
    file.close()
  }

  def readDataType(table: Table, text: String, start: Int) = {
    val tableName = HWPFReader.getTableName(table, text)
    val head = row2seq(table.getRow(0))
    complexType.append(ComplexType(tableName, ""))
    for (i <- 1 until table.numRows) {
      val tr = table.getRow(i)
      val seq = row2seq(tr)
      (head, seq) match {
        case (Seq("形式", "说明"), Seq(format, explain))
        => customary.append(Customary(format, explain))
        case (Seq("类型简称", "类型说明"), Seq(name, explain))
        => simpleType.append(SimpleType(name, explain))
        case (Seq("序号", "数据项名称", "标识", "类型", "说明"), Seq(name))
        => complexType.append(ComplexType(tableName, name))
        case (Seq("序号", "数据项名称", "标识", "类型", "说明"), Seq(sequence, name, id, data_type, explain, _*))
        => val (ajlx, cname) = ComplexType.unapply(complexType.last).getOrElse((tableName,""))
          element.append(Element(ajlx, cname, sequence, name, id, data_type, explain))
        case _ => if (seq.nonEmpty) {
          row2seq(tr, 5) match {
            case Seq(sequence, name, id, data_type, explain)
            => val (ajlx, cname) = ComplexType.unapply(complexType.last).getOrElse((tableName,""))
              element.append(Element(ajlx, cname, sequence, name, id, data_type, explain))
            case _ => throw new Exception("无法识别[" + tr.text + "]列，有" + tr.numCells + "列,head为" + head)
          }
        }
      }
    }
    start
  }

  def readCode(table: Table, text: String, start: Int): Int = {
    val (before, end) = HWPFReader.getBefore(table, text, start)
    codeTable.append(HWPFReader.getCodeTable(before))
    val number = codeTable.last.number
    val head = row2seq(table.getRow(0), 2)
    for (i <- 1 until table.numRows) {
      val tr = table.getRow(i)
      val seq = row2seq(tr, head.length)
      (head, seq) match {
        //case (Seq(), Seq()) =>
        case (Seq("代码", "名称"), Seq(code, name))
        => item.append(Item(code, name, null, null, number))
        case (Seq("代码", "名称", "备注"), Seq(code, name, explain))
        => item.append(Item(code, name, null, explain, number))
        case (Seq("代码", "名称", "说明"), Seq(code, name, explain))
        => item.append(Item(code, name, null, explain, number))
        case (Seq("代码", "上级代码", "名称"), Seq(code, parentCode, name))
        => item.append(Item(code, name, parentCode, null, number))
        case (Seq("代码", "上级代码", "内容"), Seq(code, parentCode, name))
        => item.append(Item(code, name, parentCode, null, number))
        case _ => if (seq.nonEmpty) throw new Exception("无法识别[" + tr.text + "]列，有" + tr.numCells + "列,head为" + head)
      }
    }
    end
  }

  def readRule(table: Table, text: String, start: Int): Int = {
    val (before, end) = HWPFReader.getBefore(table, text, start)
    if (!before.isEmpty) {

      val (name, number, explain) = HWPFReader.getNNE(before)
      var code, isNull, dataCheck, logicCheck, remarks: String = ""
      val map = new mutable.HashMap[String, String]
      for (i <- 1 until table.numRows) {
        val tr = table.getRow(i)
        val seq = row2seq(tr, 2)
        seq match {
          case Seq("", "") =>
          case Seq("代码", r) => code = r
          case Seq("是否为空", r) => isNull = r
          case Seq("数据正确校验", r) => dataCheck = r
          case Seq("业务逻辑校验", r) => logicCheck = r
          case Seq("备注", r) => remarks = r
          case Seq(t, r) => map.put(t, r)
          case _ => throw new Exception("无法识别[" + tr.text + "]列，有" + tr.numCells + "列")
        }
      }
      if (before.isEmpty) {
        println(code, isNull, dataCheck, logicCheck, remarks)
      }
      rule.append(Rule(name, number, explain, code, isNull, dataCheck, logicCheck, remarks, map.toMap))
    }
    end
  }
}

object HWPFReader {
  def getTableName(table: Table, text: String): String = {
    val end = table.getStartOffset
    var start = text.lastIndexOf(" ", end)
    if (end - start > 40) {
      start = end - 40
    }
    text.substring(start, end).trim.replaceAll("\r", "\n")
  }

  def getBefore(table: Table, text: String, start: Int): (String, Int) = {
    val end = table.getStartOffset
    val before = text.substring(start, end).trim.replaceAll("\r", "\n")
    (before, table.getEndOffset)
  }


  def getCodeTable(before: String): CodeTable = {
    val codeTable =
      """[\s\S]*\[[0-9]+\]([\w\W]+)\s1. 编号：(GF[0-9]+-[0-9]+)\s2. 说明\s([\w\W]+)\s3. 代码表[\s]*""".r
    val codeTable(name, number, explain) = before
    CodeTable(name, number, explain)
  }

  def getNNE(before: String): (String, String, String) = {
    val NNE =
      """[\s\S]*\[[0-9]+\]([\w\W]+)\s1.[\s]*编号[：|:]规则\[([0-9]+)\][\s]*2.[\s]*说明[：|:]*([\w\W]*)\s3.[\s]*代码表[\s\S]*""".r
    val NNE(name, number, explain) = before
    (name, number, explain)
  }
}