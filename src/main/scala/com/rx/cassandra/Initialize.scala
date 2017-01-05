package com.rx.cassandra

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
      createUdt(session)
      createTable(session)
    }
  }
  def createKeyspace(session: Session): Unit = {
    session.execute("CREATE KEYSPACE xsd WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1 }")
    session.execute("CREATE KEYSPACE poi WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1 }")
  }
  def createTable(session: Session): Unit = {
    session.execute("CREATE TABLE xsd.include (file text,schema_location text,PRIMARY KEY(file,schema_location))")
    session.execute("CREATE TABLE xsd.simple_type (file text,name text, base text,restriction list<frozen<tuple<text,text>>>,PRIMARY KEY(file,name))")
    session.execute("CREATE TABLE xsd.complex_type (file text,name text,sequence list<frozen<element>>,PRIMARY KEY(file,name))")
    session.execute("CREATE TABLE xsd.groups (file text,name text,ref text,min_occurs text,max_occurs text,sequence list<frozen<element>>,PRIMARY KEY(file,name))")
    session.execute("CREATE TABLE xsd.elements (file text,name text,data_type text,min_occurs text,max_occurs text,PRIMARY KEY(file,name))")

    session.execute("CREATE TABLE poi.code_table (name text,number text,explain text,PRIMARY KEY(number))")
    session.execute("CREATE TABLE poi.complex_type (ajlx text,name text,PRIMARY KEY(ajlx,name))")
    session.execute("CREATE TABLE poi.customary (format text,explain text,PRIMARY KEY(format,explain))")
    session.execute("CREATE TABLE poi.element (ajlx text,cname text,sequence text,name text,id text,data_type text,explain text,PRIMARY KEY(ajlx,cname,name))")
    session.execute("CREATE TABLE poi.item (code text,name text,parent_code text,explain text,number text,PRIMARY KEY(number,code))")
    session.execute("CREATE TABLE poi.rule (name text,number text,explain text,code text,not_null text,data_check text,logic_check text,remarks text,map map<text,text>,PRIMARY KEY(number))")
    session.execute("CREATE TABLE poi.simple_type (name text,explain text,PRIMARY KEY(name))")
  }

  def createUdt(session: Session): Unit ={
    session.execute("CREATE TYPE xsd.element (file text,name text,data_type text,min_occurs text,max_occurs text)")
    session.execute("CREATE TYPE xsd.group (file text,name text,ref text,min_occurs text,max_occurs text,sequence list<frozen<element>>)")

  }
  def dropKeyspace(session: Session): Unit = {
    session.execute("DROP KEYSPACE xsd")
    session.execute("DROP KEYSPACE poi")
  }
  def dropTable(session: Session): Unit = {
    session.execute("DROP TABLE xsd.include")
    session.execute("DROP TABLE xsd.simple_type")
    session.execute("DROP TABLE xsd.complex_type")
    session.execute("DROP TABLE xsd.groups")
    session.execute("DROP TABLE xsd.elements")
  }
}
