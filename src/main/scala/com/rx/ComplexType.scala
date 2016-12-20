package com.rx

import scala.xml.Node

/**
  * Created by hejianjun on 2016/12/11.
  */
case class ComplexType(file: String, name: String, val sequence: Seq[Element]) {
}

object ComplexType {
  def apply(file: String, e: Node): ComplexType = {
    ComplexType(file, e \@ "name",
      (e \ "sequence" \ "_").flatMap(s => s match {
        case <element></element> => Seq(Element(file, s))
        case <group></group> => Group(file, s).toElements
        case <choice>{_*}</choice> => Choice(file, s).toElements
      }))
  }
}