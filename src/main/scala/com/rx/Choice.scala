package com.rx

import scala.xml.Node

/**
  * Created by hejianjun on 2016/12/20.
  */
case class Choice(file: String,group:Seq[Group]) extends Sequence{
  def toElements(): Seq[Element] = {
    group.flatMap(_.toElements)
  }
}
object Choice {
  def apply(file: String, e: Node): Choice = {
    Choice(file,(e\"group").map(g=>Group(file,g)))
  }

}
