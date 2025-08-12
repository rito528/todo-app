package com.example.todoapp

import cats.effect.{ IO, IOApp, ExitCode }
import com.comcast.ip4s._
import org.http4s.ember.server._
import org.http4s.client.middleware.Logger
import org.http4s.client.Client
import scala.concurrent.duration._
import com.example.infra.TodoRepositoryImpl
import com.example.domain.TodoRepository
import com.example.infra.DatabaseConnectionPool

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    given databaseConnectionPool: DatabaseConnectionPool = DatabaseConnectionPool.fromConfig()
    given todoRepository: TodoRepository[IO] = new TodoRepositoryImpl[IO]

    val client = Client.fromHttpApp(TodoappRoutes.routes.orNotFound)

    val loggerClient = Logger[IO](
      logHeaders = true,
      logBody    = true,
      logAction  = Some((msg: String) => IO.println(msg))
    )(client)

    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"9000")
      .withHttpApp(loggerClient.toHttpApp)
      .withShutdownTimeout(1.second)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }

}
