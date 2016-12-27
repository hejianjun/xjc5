package com.rx.poi.model

import org.apache.poi.hwpf.usermodel.{TableRow, TableCell}

import scala.collection.immutable.IndexedSeq


/**
  * Created by hejianjun on 2016/12/26.
  */
object Implicit {

  implicit def cell2seq(cell: TableCell): Seq[String] = {
    for {i <- 0 until cell.numParagraphs
         text = cell.getParagraph(i).text.replaceAll("\r", "\n").replaceAll("\u0007", "")
    } yield text
  }

  implicit def cell2string(cell: TableCell): String = {
    cell2seq(cell).mkString
  }

  implicit def row2seq(row: TableRow): Seq[String] = {
    for {i <- 0 until row.numCells
         text = cell2string(row.getCell(i))
    } yield text
  }

  def row2seq(row: TableRow, min: Int): Seq[String] = {
    if (row.numCells >= min) {
      return row2seq(row)
    }
    val seq=string2seq(row.text)
    seq++Seq.fill[String](min-seq.length)("")
  }

  implicit def string2seq(str: String): Seq[String] = {
    str.split("\u0007").toSeq
  }
}
