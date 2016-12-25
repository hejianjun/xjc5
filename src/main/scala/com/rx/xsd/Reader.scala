package com.rx.xsd

import java.io.File

import com.rx._

import scala.collection
import scala.collection.immutable.Seq
import scala.reflect.ClassTag
import scala.xml.XML

/**
  * Created by hejianjun on 2016/12/11.
  */
object Reader {

  def getFileList(filePath:String): Array[File] ={
    val root = new File(filePath)
    root.listFiles()
  }
  def get[T:ClassTag](file: File): Seq[T] ={
    val fileName = file.getName
    val xml = XML.loadFile(file)
    //(xml \ "simpleType").map(e => )
    Seq[T]()
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
