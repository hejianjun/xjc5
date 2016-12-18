package com.rx

import java.io.File

import scala.xml.XML

/**
  * Created by hejianjun on 2016/12/11.
  */
object Reader {
  def main(args: Array[String]) {
    val filePath = "D:\\xsd2\\"
    val root = new File(filePath)
    val files = root.listFiles()
    files.foreach(file => {
      val fileName = file.getName
      val xml = XML.loadFile(file)
      val list = (xml \ "_").map(e => e match {
        case <simpleType>{_*}</simpleType> => SimpleType.fromXML(e)
        case <include>{_*}</include> => Include.fromXML(fileName, e)
        case <group>{_*}</group> => Group.fromXML(fileName, e)
        case <complexType>{_*}</complexType> => ComplexType.fromXML(fileName, e)
        case <element>{_*}</element> => Element.fromXML(fileName, e)
        case _ => throw new Exception("未知类型"+e)
      })
      println(list)
    })
  }
}
