package com.rx.poi

import org.apache.poi.hwpf.HWPFDocument
import org.apache.poi.hwpf.usermodel.TableIterator
import org.scalatest.FunSuite

/**
  * Created by hejianjun on 2016/12/27.
  */
class HWPFMain$Test extends FunSuite {

  test("testGetComplexType") {

    val file = this.getClass.getClassLoader.getResource("doc/附件1 审判业务数据结构规范.doc").openStream()
    val doc = new HWPFDocument(file)
    val h = HWPFMain(doc.getText)
    val range = doc.getRange
    val it = new TableIterator(range)

    while (it.hasNext) {
      val tb = it.next()
      val tableName = h.getTableName(tb)
      //println(tableName)
      h.getComplexType(tableName, tb).foreach(println)
    }
    doc.close()
    file.close()
  }

  test("testGetCodeTable") {
    val file = this.getClass.getClassLoader.getResource("doc/附件2 审判业务标准代码.doc").openStream()
    val doc = new HWPFDocument(file)
    val h = HWPFMain(doc.getText)
    val range = doc.getRange
    val it = new TableIterator(range)

    while (it.hasNext) {
      val tb = it.next()
      val before = h.getBefore(tb)
      val codeTable = h.getCodeTable(before)
      h.getItem(codeTable, tb).foreach(println)
    }
    doc.close()
    file.close()
  }

}
