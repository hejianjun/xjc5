package com.rx

import scala.xml.Node

/**
  * Created by hejianjun on 2016/12/11.
  */
case class ComplexType(var file: String, var name: String, val element: Seq[Element], var choice: Seq[Seq[GroupRef]]) {

}

object ComplexType {
  def fromXML(file: String, e: Node): ComplexType = {
    val name = e \@ "name"
    new ComplexType(file,
      name,
      (e \ "sequence" \ "element").map(n => Element.fromXML(file, n)),
      (e \ "sequence" \ "choice").map(n => (n \ "group").map(g => GroupRef.fromXML(file, name, g))))
  }
}