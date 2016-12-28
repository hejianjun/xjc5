package com.rx.poi

import org.apache.poi.hwpf.HWPFDocument
import org.apache.poi.hwpf.usermodel.TableIterator
import org.scalatest.FunSuite

/**
  * Created by hejianjun on 2016/12/27.
  */
class HWPFReader$Test extends FunSuite {

  test("testReadDataType") {
    val reader = new HWPFReader("doc/附件1 审判业务数据结构规范.doc")
    reader.read(reader.readDataType)
    reader.simpleType.foreach(println)
    reader.complexType.foreach(println)
    reader.element.foreach(println)
  }

  test("testGetCodeTable") {
    val reader = new HWPFReader("doc/附件2 审判业务标准代码.doc")
    reader.read(reader.readCode)
    reader.codeTable.foreach(println)
    reader.item.foreach(println)
  }

}
