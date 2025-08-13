package com.example.todoapp

import cats.effect.IO
import cats.syntax.all.*
import com.example.domain.TodoRepository
import com.example.todoapp.Encoders.encodeTodoResponses
import com.example.todoapp.Decoders.decodeTodoResponses
import com.example.todoapp.Schemas.*
import sttp.tapir.generic.auto.*
import sttp.tapir.*
import sttp.tapir.json.circe.*
import com.example.todoapp.Responses.TodoResponse
import com.example.domain.TodoId
import com.example.domain.CategoryRepository
import sttp.tapir.files.*
import scala.io.Source
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.swagger.SwaggerUIOptions

class TodoappEndpoints(
  using
  todoRepository:     TodoRepository[IO],
  categoryRepository: CategoryRepository[IO]
) {

  private def pingEndpoint: PublicEndpoint[Unit, Unit, String, Any] = {
    endpoint.get.in("ping").out(stringBody)
  }

  private def pingLogic: Unit => IO[Either[Unit, String]] = {
    _ => IO.pure(Right("ok"))
  }

  private def todoEndpoint: PublicEndpoint[Unit, Unit, List[TodoResponse], Any] = {
    endpoint.get.in("api" / "todos").out(jsonBody[List[TodoResponse]])
  }

  private def todoLogic(
    using
    todoRepository:     TodoRepository[IO],
    categoryRepository: CategoryRepository[IO]
  ): Unit => IO[Either[Unit, List[TodoResponse]]] = _ =>
    for {
      todos      <- todoRepository.fetchAllTodo
      categories <- categoryRepository.fetchAllCategory
    } yield {
      Right(todos.map(todo =>
        TodoResponse.fromTodoWithCategoryOpt(todo, categories.find(_.id == todo.categoryId))
      ).toList)
    }

  private def angularAppEndpoint: PublicEndpoint[List[String], Unit, String, Any] = {
    endpoint.get.in(paths).out(htmlBodyUtf8)
  }

  private def angularAppLogic: List[String] => IO[Either[Unit, String]] = _ =>
    IO {
      val html = Source.fromFile("public/ngx/browser/index.html").mkString
      Right(html)
    }

  private val apiEndpoints: List[ServerEndpoint[Any, IO]] = List(
    pingEndpoint.serverLogic(pingLogic),
    todoEndpoint.serverLogic(todoLogic),
    staticFilesGetServerEndpoint("assets")("public/"),
    angularAppEndpoint.serverLogic(angularAppLogic),
  )

  private val apiDocumentEndpoint: List[ServerEndpoint[Any, IO]] =
    SwaggerInterpreter(
      swaggerUIOptions = SwaggerUIOptions.default.pathPrefix(List("document", "openapi", "docs"))
    )
      .fromServerEndpoints[IO](apiEndpoints, "TodoApp OpenAPI Docs", "1.0.0")

  val allEndpoints: List[ServerEndpoint[Any, IO]] = apiDocumentEndpoint ++ apiEndpoints
}
