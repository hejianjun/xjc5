package com.rx.poi.model

import org.apache.poi.hwpf.usermodel.TableRow

/**
  * Created by hejianjun on 2016/12/26.
  */
case class Element(complexType: ComplexType, sequence: String, name: String, id: String, data_type: String, explain: String) {

}

object Element {
  import com.rx.poi.model.Implicit._
  def apply(complexType: ComplexType, row: TableRow): Element = {
    Element(complexType, row.getCell(0), row.getCell(1), row.getCell(2), row.getCell(3), row.getCell(4))
  }
}