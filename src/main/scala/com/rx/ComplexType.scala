package com.rx

import scala.xml.Node

/**
  * Created by hejianjun on 2016/12/11.
  */
case class ComplexType(file: String, name: String, val element: Seq[Element], val group: Seq[GroupRef], val choice: Seq[GroupRef]) {
}

object ComplexType {
  def fromXML(file: String, e: Node): ComplexType = {
    val name = e \@ "name"
    ComplexType(file,
      name,
      (e \ "sequence" \ "element").map(n => Element.fromXML(file, n)),
      (e \ "sequence" \ "group").map(g => GroupRef.fromXML(file, name, g)),
      (e \ "sequence" \ "choice" \ "group").map(g => GroupRef.fromXML(file, name, g)))
  }
}