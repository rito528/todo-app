package com.example.todoapp.endpoints

import sttp.tapir.generic.auto.*
import sttp.tapir.*
import sttp.tapir.json.circe.*
import com.example.todoapp.Responses.TodoResponse
import com.example.todoapp.Encoders.{ encodeTodo, encodeCategories, encodeTodoResponses }
import com.example.todoapp.Decoders.{ decodeTodo, decodeCategories, decodeTodoResponses }
import com.example.todoapp.Schemas.*
import com.example.domain.TodoRepository
import com.example.domain.CategoryRepository
import cats.effect.IO
import sttp.tapir.server.ServerEndpoint
import com.example.todoapp.Requests.CreateTodoRequestSchema
import com.example.domain.Todo
import com.example.domain.Category

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

    def createTodoLogic(schema: CreateTodoRequestSchema): IO[Either[Unit, Todo]] = {
      val todo = schema.toTodo
      todoRepository.createTodo(schema.toTodo).map { todoId =>
        Right(todo.copy(id = Some(todoId)))
      }
    }

    def getAllCategoriesLogic: Unit => IO[Either[Unit, List[Category]]] = _ => {
      categoryRepository.fetchAllCategory.map { categories =>
        Right(categories.toList)
      }
    }
  }

  private def createTodoEndpoint: PublicEndpoint[CreateTodoRequestSchema, Unit, Todo, Any] = {
    endpoint.post.in("api" / "todos").in(jsonBody[CreateTodoRequestSchema]).out(jsonBody[Todo])
  }

  private def getTodoEndpoint: PublicEndpoint[Unit, Unit, List[TodoResponse], Any] = {
    endpoint.get.in("api" / "todos").out(jsonBody[List[TodoResponse]])
  }

  private def getAllCategoriesEndpoint: PublicEndpoint[Unit, Unit, List[Category], Any] = {
    endpoint.get.in("api" / "categories").out(jsonBody[List[Category]])
  }

  val endpoints: List[ServerEndpoint[Any, IO]] = List(
    getTodoEndpoint.serverLogic(ApiEndpointServerLogics.getTodoLogic),
    createTodoEndpoint.serverLogic(ApiEndpointServerLogics.createTodoLogic),
    getAllCategoriesEndpoint.serverLogic(ApiEndpointServerLogics.getAllCategoriesLogic)
  )
}
