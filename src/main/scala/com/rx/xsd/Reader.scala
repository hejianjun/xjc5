package com.rx.xsd

import java.io.File

import com.rx.xsd.model._

import scala.collection.immutable.Seq
import scala.reflect.ClassTag
import scala.xml.XML

/**
  * Created by hejianjun on 2016/12/11.
  */
case class Reader(file: File) {
  val fileName = file.getName
  val xml = XML.loadFile(file)
  def get[T:ClassTag]: Seq[T] ={
    //(xml \ "simpleType").map(e => )
    Seq[T]()
  }
  def getSimpleType = {
    (xml \ "simpleType").map(e => SimpleType(fileName,e))
  }
  def getInclude = {
    (xml \ "include").map(e => Include(fileName, e))
  }
  def getGroup = {
    (xml \ "group").map(e => Group(fileName, e))
  }

  def getComplexType = {
    (xml \ "complexType").map(e => ComplexType(fileName, e))
  }

  def getElement = {
    (xml \ "element").map(e => Element(fileName, e))
  }


}
