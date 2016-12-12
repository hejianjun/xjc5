package com.rx

import scala.xml.Node

/**
  * Created by hejianjun on 2016/12/11.
  */
case class ComplexType(var file: String,var name: String,val element:Seq[Element]) {

}
object ComplexType {
  def fromXML(file:String,e: Node): ComplexType = {
    new ComplexType(file,e.label,(e\"sequence"\"element").map(n=>Element.fromXML(file,e.label,n)))
  }
}