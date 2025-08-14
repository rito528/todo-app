package com.example.todoapp.endpoints

import sttp.tapir.generic.auto.*
import sttp.tapir.*
import sttp.tapir.json.circe.*
import com.example.todoapp.Responses.TodoResponse
import com.example.todoapp.Encoders.encodeTodoResponses
import com.example.todoapp.Decoders.decodeTodoResponses
import com.example.todoapp.Schemas.*
import com.example.domain.TodoRepository
import com.example.domain.CategoryRepository
import cats.effect.IO
import sttp.tapir.server.ServerEndpoint

class ApiEndpoints(
  using
  todoRepository:     TodoRepository[IO],
  categoryRepository: CategoryRepository[IO]
) {
  private object ApiEndpointServerLogics {
    def getTodoLogic: Unit => IO[Either[Unit, List[TodoResponse]]] = _ =>
      for {
        todos      <- todoRepository.fetchAllTodo
        categories <- categoryRepository.fetchAllCategory
      } yield {
        Right(todos.map(todo =>
          TodoResponse.fromTodoWithCategoryOpt(todo, categories.find(_.id == todo.categoryId))
        ).toList)
      }
  }

  private def getTodoEndpoint: PublicEndpoint[Unit, Unit, List[TodoResponse], Any] = {
    endpoint.get.in("api" / "todos").out(jsonBody[List[TodoResponse]])
  }

  val endpoints: List[ServerEndpoint[Any, IO]] = List(
    getTodoEndpoint.serverLogic(ApiEndpointServerLogics.getTodoLogic),
  )
}
