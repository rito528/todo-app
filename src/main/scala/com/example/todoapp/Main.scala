package com.example.todoapp

import cats.effect.{ IO, IOApp, ExitCode }
import com.comcast.ip4s._
import org.http4s.ember.server._
import scala.concurrent.duration._
import com.example.infra.DatabaseConnectionPool
import com.example.domain.TodoRepository
import com.example.domain.CategoryRepository
import com.example.infra.CategoryRepositoryImpl
import com.example.infra.TodoRepositoryImpl
import sttp.tapir.server.http4s.Http4sServerInterpreter
import org.http4s.server.Router

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    given databaseConnectionPool: DatabaseConnectionPool = DatabaseConnectionPool.fromConfig()
    given todoRepository: TodoRepository[IO]             = new TodoRepositoryImpl[IO]
    given cateogryRepository: CategoryRepository[IO]     = new CategoryRepositoryImpl[IO]

    val routes = Http4sServerInterpreter[IO]().toRoutes(TodoappEndpoints.endpoints)

    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"9000")
      .withHttpApp(Router("/" -> routes).orNotFound)
      .withShutdownTimeout(1.second)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }

}
