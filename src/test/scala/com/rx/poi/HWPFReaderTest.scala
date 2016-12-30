package com.rx.poi

import org.scalatest.FunSuite

/**
  * Created by hejianjun on 2016/12/29.
  */
class HWPFReaderTest extends FunSuite {

  test("testReadRule") {
    val reader = new HWPFReader("doc/附件3 审判业务逻辑与数据校验规则.doc")
    reader.read(reader.readRule)
    reader.rule.foreach(println)
  }

  test("testReadDataType") {
    val reader = new HWPFReader("doc/附件1 审判业务数据结构规范.doc")
    reader.read(reader.readDataType)
    reader.simpleType.foreach(println)
    reader.complexType.foreach(println)
    reader.element.foreach(println)
  }

  test("testReadCode") {
    val reader = new HWPFReader("doc/附件2 审判业务标准代码.doc")
    reader.read(reader.readCode)
    reader.codeTable.foreach(println)
    reader.item.foreach(println)
  }

}
