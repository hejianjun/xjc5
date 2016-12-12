package com.rx

import scala.xml.Node


/**
  * Created by hejianjun on 2016/12/11.
  */
case class SimpleType(var name: String, var base: String,var restriction:Map[String,String]) {
}

object SimpleType {
  def fromXML(e: Node): SimpleType = {
    new SimpleType(e \@"name", e \ "restriction" \@ "base",(e \ "restriction" \"_").map(n=>(n.label,n\@"value")).toMap)
  }
}

