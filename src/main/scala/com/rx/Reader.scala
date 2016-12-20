package com.rx

import java.io.File

import scala.xml.XML

/**
  * Created by hejianjun on 2016/12/11.
  */
object Reader {

  def getFileList(filePath:String): Array[File] ={
    val root = new File(filePath)
    root.listFiles()
  }
  def getSimpleType(file: File) = {
    val fileName = file.getName
    val xml = XML.loadFile(file)
    (xml \ "simpleType").map(e => SimpleType(fileName,e))
  }
  def getInclude(file: File) = {
    val fileName = file.getName
    val xml = XML.loadFile(file)
    (xml \ "include").map(e => Include(fileName, e))
  }
  def getGroup(file: File) = {
    val fileName = file.getName
    val xml = XML.loadFile(file)
    (xml \ "group").map(e => Group(fileName, e))
  }

  def getComplexType(file: File) = {
    val fileName = file.getName
    val xml = XML.loadFile(file)
    (xml \ "complexType").map(e => ComplexType(fileName, e))
  }

  def getElement(file: File) = {
    val fileName = file.getName
    val xml = XML.loadFile(file)
    (xml \ "element").map(e => Element(fileName, e))
  }


}
