package com.rx.poi.model

import org.apache.poi.hwpf.usermodel.TableRow



/**
  * Created by hejianjun on 2016/12/11.
  */
case class SimpleType(name: String,explain: String) {
}

object SimpleType {
  import com.rx.poi.model.Implicit._
  def apply(row: TableRow): SimpleType = {
    SimpleType(row.getCell(0).text.trim,row.getCell(1))
  }
}

