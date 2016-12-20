package com.rx

import scala.xml.Node


/**
  * Created by hejianjun on 2016/12/11.
  */
case class SimpleType(file: String,name: String, base: String,restriction:Seq[(String,String)]) {
}

object SimpleType {
  def apply(file:String,e: Node): SimpleType = {
    new SimpleType(file,e \@"name", e \ "restriction" \@ "base",(e \ "restriction" \"_").map(n=>(n.label,n\@"value")).toList)
  }
}

