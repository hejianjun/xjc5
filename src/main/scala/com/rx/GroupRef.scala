package com.rx

import scala.xml.Node

/**
  * Created by hejianjun on 2016/12/18.
  */
case class GroupRef(file: String, ref: String) {
  var minOccurs: Int = _
  var maxOccurs: Int = _
  var complexName: String = _
}

object GroupRef {
  def fromXML(file: String, ref: String, e: Node): GroupRef = {
    val minOccurs = e \@ "minOccurs"
    val maxOccurs = e \@ "maxOccurs"
    val groupRef = GroupRef(file, ref)
    if (minOccurs != "") {
      groupRef.minOccurs = minOccurs.toInt
    }
    if (maxOccurs != "") {
      groupRef.maxOccurs = maxOccurs.toInt
    }
    groupRef
  }
}
