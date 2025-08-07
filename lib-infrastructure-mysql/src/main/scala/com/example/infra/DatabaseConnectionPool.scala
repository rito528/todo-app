package com.example.infra

import com.typesafe.config.ConfigFactory

case class DatabaseConnectionPool(
  url:      String,
  user:     String,
  password: String
)

object DatabaseConnectionPool {
  def fromConfig(): DatabaseConnectionPool = {
    val config = ConfigFactory.load()

    DatabaseConnectionPool(
      config.getString("url"),
      config.getString("user"),
      config.getString("password")
    )
  }
}
