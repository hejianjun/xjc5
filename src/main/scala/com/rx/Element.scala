package com.rx

import scala.xml.Node

/**
  * Created by hejianjun on 2016/12/11.
  */
case class Element(file: String, name: String, dataType: String) {
  var minOccurs: Int = _
  var maxOccurs: Int = _
  var complexName: String = _
  var groupName: String = _
}

object Element {
  def fromXML(file: String, e: Node): Element = {
    val minOccurs = e \@ "minOccurs"
    val maxOccurs = e \@ "maxOccurs"
    val element = Element(file, e \@ "name", e \@ "type")
    if (minOccurs != "") {
      element.minOccurs = minOccurs.toInt
    }
    if (maxOccurs != "") {
      element.maxOccurs = maxOccurs.toInt
    }
    element
  }
}