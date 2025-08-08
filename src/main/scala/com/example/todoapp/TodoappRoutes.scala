package com.example.todoapp

import org.http4s.dsl.io.*;
import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.Response
import org.http4s.Request
import org.http4s.StaticFile
import fs2.io.file.Path
import com.example.domain.TodoRepository
import io.circe.syntax.*
import org.http4s.circe.*
import com.example.todoapp.Encoders.encodeTodos

object TodoappRoutes {
  def routes(using todoRepository: TodoRepository[IO]): HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case GET -> Root / "ping"          =>
        Ok("ok")
      case GET -> Root / "api" / "todos" =>
        Ok(todoRepository.fetchAllTodo.map(_.asJson))
      case req @ GET -> "assets" /: rest =>
        StaticFile
          .fromPath(Path(s"public/${rest}"), Some(req))
          .getOrElseF(NotFound())
      case req @ GET -> _                =>
        StaticFile
          .fromPath(Path("public/ngx/browser/index.html"), Some(req))
          .getOrElseF(NotFound())
    }
  }
}
