package com.rx

import scala.xml.Node

/**
  * Created by hejianjun on 2016/12/11.
  */
case class Element(var file: String,var complexName: String,var name:String) {
  var dataType: String = _
  var minOccurs: Int = _
  var maxOccurs: Int = _
}
object Element {
  def fromXML(file:String,complexName: String,e: Node): Element = {
    new Element(file,complexName,e.label)
  }
}