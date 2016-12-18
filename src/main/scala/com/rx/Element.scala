package com.rx

import scala.xml.Node

/**
  * Created by hejianjun on 2016/12/11.
  */
case class Element(var file: String,var name:String,var dataType: String) {
  var minOccurs: Int = _
  var maxOccurs: Int = _
}
object Element {
  def fromXML(file:String,e: Node): Element = {
    val minOccurs=e\@"minOccurs"
    val maxOccurs=e\@"maxOccurs"
    val element=new Element(file,e\@"name",e\@"type")
    if(minOccurs!=""){
      element.minOccurs=minOccurs.toInt
    }
    if(maxOccurs!=""){
      element.maxOccurs=maxOccurs.toInt
    }
    element
  }
}