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
      dropKeyspace(session)
      createKeyspace(session)
      session.execute("use xsd")
      createTable(session)
    }
  }
  def createKeyspace(session: Session): Unit = {
    session.execute("CREATE KEYSPACE xsd WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1 }")
  }
  def createTable(session: Session): Unit = {

    session.execute("CREATE TABLE simple_type (name text PRIMARY KEY, base text,restriction list<frozen<tuple<text,text>>>)")
    session.execute("CREATE TABLE group (file text,name text,PRIMARY KEY(file,name))")
    session.execute("CREATE TABLE include (file text,schema_location text,PRIMARY KEY(file,schema_location))")
    session.execute("CREATE TABLE complex_type (file text,name text,PRIMARY KEY(file,name))")
    session.execute("CREATE TABLE group_ref (file text,ref text,min_occurs int,max_occurs int,complex_name text,PRIMARY KEY(file,ref))")
    session.execute("CREATE TABLE element (file text,name text,data_type text,min_occurs int,max_occurs int,complex_name text,group_name text, PRIMARY KEY(file,name))")
  }
  def dropKeyspace(session: Session): Unit = {
    session.execute("DROP KEYSPACE xsd")
  }
  def dropTable(session: Session): Unit = {
    session.execute("DROP TABLE simple_type")
    session.execute("DROP TABLE complex_type")
    session.execute("DROP TABLE element")
  }
}
