package com.rx

import scala.xml.Node

/**
  * Created by hejianjun on 2016/12/18.
  */
case class Group(file: String, name: String, ref: String, min_occurs: String, max_occurs: String, val sequence: Seq[Element]) extends Sequence {
  def toElements(): Seq[Element] = {
    sequence
  }
}

object Group {
  def apply(file: String, e: Node): Group = {
    Group(file, e \@ "name", e \@ "ref", e \@ "minOccurs", e \@ "maxOccurs",
      (e \ "sequence" \ "_").flatMap(s => s match {
        case <element></element> => Seq(Element(file, s))
        case <group></group> => Group(file, s).toElements
        case <choice>{_*}</choice> => Choice(file, s).toElements
      }))
  }

}
