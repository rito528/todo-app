package com.example.infra

import com.typesafe.config.ConfigFactory
import doobie.hikari.HikariTransactor
import com.zaxxer.hikari.HikariConfig
import cats.effect.kernel.Resource
import cats.effect.kernel.Async

case class DatabaseConnectionPool(
  host:     String,
  port:     Int,
  user:     String,
  password: String
) {
  require(port >= 0 && port <= 65535)

  def transactor[F[_]: Async]: Resource[F, HikariTransactor[F]] = for {
    hikariConfig <- Resource.pure {
      val config = new HikariConfig();

      val jdbcUrl =
        if (host == "localhost") {
          s"jdbc:aws-wrapper:mysql://$host:$port/to_do?allowPublicKeyRetrieval=true&useSSL=false&wrapperPlugins="
        } else {
          s"jdbc:aws-wrapper:mysql://$host:$port/to_do"
        }

      config.setDriverClassName("software.amazon.jdbc.Driver")
      config.setJdbcUrl(jdbcUrl)
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
      config.getInt("db.port"),
      config.getString("db.user"),
      config.getString("db.password")
    )
  }
}
