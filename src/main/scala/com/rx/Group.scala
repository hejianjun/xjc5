package com.rx

import scala.xml.Node

/**
  * Created by hejianjun on 2016/12/18.
  */
case class Group(file:String,name:String,ref:String,minOccurs:String, maxOccurs:String,val sequence:Seq[Sequence]) extends Sequence{

}
object Group {
  def apply(file:String,e: Node): Group = {
    Group(file,e\@"name",e\@"ref",e\@"minOccurs",e\@"maxOccurs",
      (e \ "sequence" \ "_").map(s => s match {
        case <element></element> => Element(file, s)
        case <group></group> => Group(file, s)
        case <choice></choice> => Choice(file, s)
      }))
  }
}
