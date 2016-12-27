package com.rx.poi.model

import org.apache.poi.hwpf.usermodel.TableRow

/**
  * Created by hejianjun on 2016/12/26.
  */
case class ComplexType(ajlx: String, name: String) {

}

object ComplexType {
  import com.rx.poi.model.Implicit._
  def apply(ajlx: String, row: TableRow): ComplexType = {
    ComplexType(ajlx, row.getCell(0))
  }
}