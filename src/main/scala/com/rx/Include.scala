package com.rx

import scala.xml.Node

/**
  * Created by hejianjun on 2016/12/18.
  */
case class Include(var file: String,var schemaLocation:String) {

}
object Include {
  def fromXML(file:String,e: Node): Include = {
    new Include(file,e\@"schemaLocation")
  }
}