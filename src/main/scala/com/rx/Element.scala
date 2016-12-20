package com.rx

import scala.xml.Node

/**
  * Created by hejianjun on 2016/12/11.
  */
case class Element(file: String, name: String, dataType: String, minOccurs: String, maxOccurs: String) extends Sequence{
}

object Element {
  def apply(file: String, e: Node): Element = {
    Element(file, e \@ "name", e \@ "type", e \@ "minOccurs", e \@ "maxOccurs")
  }
}