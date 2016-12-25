package com.rx.spark

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

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
    for (i <- 1 to 100)
      producer.send(new ProducerRecord[String, String]("test", i.toString, i.toString))

    producer.close()
  }
}
