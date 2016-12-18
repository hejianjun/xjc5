package com.rx

import scala.xml.Node

/**
  * Created by hejianjun on 2016/12/18.
  */
case class GroupRef(var file:String,var ref:String){
  var minOccurs: Int = _
  var maxOccurs: Int = _
}
object GroupRef {
  def fromXML(file: String, ref: String, e: Node): GroupRef = {
    val minOccurs=e\@"minOccurs"
    val maxOccurs=e\@"maxOccurs"
    val groupRef=new GroupRef(file,ref)
    if(minOccurs!=""){
      groupRef.minOccurs=minOccurs.toInt
    }
    if(maxOccurs!=""){
      groupRef.maxOccurs=maxOccurs.toInt
    }
    groupRef
  }
}
