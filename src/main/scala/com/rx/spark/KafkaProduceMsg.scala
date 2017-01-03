package com.rx.spark

import java.util.Properties

import org.apache.kafka.clients.producer.{RecordMetadata, KafkaProducer, ProducerRecord}

/**
  * Created by hejianjun on 2016/12/22.
  */
object KafkaProduceMsg {
  def main(args: Array[String]) {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("acks", "all")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)
    val list=for {i <- 1 to 100
         recordMetadata = producer.send(new ProducerRecord[String, String]("test", i.toString, i.toString))
    } yield recordMetadata
    list.foreach(r=>println(r.get()))
    producer.close()
  }
}
