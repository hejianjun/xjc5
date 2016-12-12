package com.rx

import scala.xml.XML

/**
  * Created by hejianjun on 2016/12/11.
  */
object Reader {
  def main(args: Array[String]) {
    val xml=XML.loadFile("D:\\xsd2\\_A_类型_基础.xsd")
    (xml\"simpleType").foreach(e=>println(SimpleType.fromXML(e)))

  }
}
