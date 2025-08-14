package com.example.infra

import com.typesafe.config.ConfigFactory
import doobie.hikari.HikariTransactor
import com.zaxxer.hikari.HikariConfig
import cats.effect.kernel.Resource
import cats.effect.kernel.Async

case class DatabaseConnectionPool(
  host:     String,
  port:     Short,
  user:     String,
  password: String
) {
  require(port >= 0)

  def transactor[F[_]: Async]: Resource[F, HikariTransactor[F]] = for {
    hikariConfig <- Resource.pure {
      val config = new HikariConfig();

      config.setDriverClassName("com.mysql.cj.jdbc.Driver")
      config.setJdbcUrl(s"jdbc:mysql://$host:$port/to_do")
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
      config.getString("db.host"),
      config.getInt("db.port").toShort,
      config.getString("db.user"),
      config.getString("db.password")
    )
  }
}
