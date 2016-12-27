package com.rx.poi


import org.apache.poi.extractor.ExtractorFactory
import org.apache.poi.hwpf.extractor.WordExtractor
import org.apache.poi.poifs.filesystem.POIFSFileSystem


/**
  * Created by hejianjun on 2016/12/25.
  */
object Extraction extends App {

  val file = this.getClass.getClassLoader.getResourceAsStream("doc/附件2 审判业务标准代码.doc")
  val embeddedExtractor = ExtractorFactory.createExtractor(file)

  embeddedExtractor match {
    case wordExtractor: WordExtractor =>
      println(wordExtractor.getText.replaceAll("\r","\n"))
    case _ =>
  }
  file.close()
}
