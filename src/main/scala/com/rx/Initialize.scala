package com.rx

import com.datastax.driver.core.Session
import com.datastax.spark.connector.cql.CassandraConnector
import org.apache.spark.SparkConf

/**
  * Created by hejianjun on 2016/12/11.
  */
object Initialize {
  def main(args: Array[String]) {
    val conf = new SparkConf().set("spark.cassandra.connection.host", "127.0.0.1").setMaster("local").setAppName("Simple Application")
    CassandraConnector(conf).withSessionDo { session =>
      createKeyspace(session)
      session.execute("use xsd")
      createTable(session)
    }
  }
  def createKeyspace(session: Session): Unit = {
    session.execute("CREATE KEYSPACE xsd WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1 }")
  }
  def createTable(session: Session): Unit = {
    session.execute("CREATE TABLE simpleType (name text PRIMARY KEY, base text,restriction map<text,text>)")
    session.execute("CREATE TABLE complexType (file text,name text,PRIMARY KEY(file,name))")
    session.execute("CREATE TABLE element (file text,complexName text,name text, dataType text,minOccurs int,maxOccurs int, PRIMARY KEY(file,complexName,name))")
  }
  def dropKeyspace(session: Session): Unit = {
    session.execute("DROP KEYSPACE xsd")
  }
  def dropTable(session: Session): Unit = {
    session.execute("DROP TABLE simpleType")
    session.execute("DROP TABLE complexType")
    session.execute("DROP TABLE element")
  }
}
