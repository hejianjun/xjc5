package com.rx.spark

import java.io.File

import com.datastax.spark.connector.util.Logging
import com.rx.xsd.model.ComplexType
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver

import scala.xml.XML

/**
  * Created by hejianjun on 2016/12/22.
  */
class CustomReceiver(filePath: String) extends Receiver[ComplexType](StorageLevel.MEMORY_AND_DISK_2) with Logging {
  override def onStart(): Unit = {
    val root = new File(filePath)
    val files = root.listFiles()
    // Start the thread that receives data over a connection
    files.foreach(file => {
      new Thread("Socket Receiver") {
        override def run() {
          receive(file)
        }
      }.start()
    })
  }

  override def onStop(): Unit = {

  }

  private def receive(file: File): Unit = {
    val fileName = file.getName
    val xml = XML.loadFile(file)
    (xml \ "complexType").foreach(e => store(ComplexType(fileName, e)))
  }
}
