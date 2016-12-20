package com.rx

import scala.xml.Node

/**
  * Created by hejianjun on 2016/12/18.
  */
case class Include(file: String,schema_location:String) {

}
object Include {
  def apply(file:String,e: Node): Include = {
    Include(file,e\@"schemaLocation")
  }
}