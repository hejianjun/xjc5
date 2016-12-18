package com.rx

import scala.xml.Node

/**
  * Created by hejianjun on 2016/12/18.
  */
case class Group(var file:String,var name:String,val element:Seq[Element]){

}
object Group {
  def getGroup(file: String, name: String, e: Node): Seq[String] = {
    (e\"group").map(n=>n\@"ref")
  }

  def fromXML(file:String,e: Node): Group = {
    val name=e\@"name"
    new Group(file,name,(e\"sequence"\"element").map(n=>Element.fromXML(file,n)))
  }
}
