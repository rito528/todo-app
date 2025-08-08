package com.example.infra

import com.typesafe.config.ConfigFactory
import doobie.hikari.HikariTransactor
import com.zaxxer.hikari.HikariConfig
import cats.effect.kernel.Resource
import cats.effect.kernel.Async

case class DatabaseConnectionPool(
  url:      String,
  user:     String,
  password: String
) {

  def transactor[F[_]: Async]: Resource[F, HikariTransactor[F]] = for {
    hikariConfig <- Resource.pure {
      val config = new HikariConfig();

      config.setDriverClassName("com.mysql.cj.jdbc.Driver")
      config.setJdbcUrl(s"jdbc:mysql://${url}/to_do")
      config.setUsername(user)
      config.setPassword(password)

      config
    }
    xa           <- HikariTransactor.fromHikariConfig[F](hikariConfig)
  } yield xa

}

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
