package com.example.todoapp.endpoints

import sttp.tapir.*
import sttp.tapir.json.circe.*
import com.example.todoapp.Responses.TodoResponse
import com.example.todoapp.Encoders.{ encodeCategories, encodeTodoResponses }
import com.example.todoapp.Decoders.{ decodeCategories, decodeTodoResponses }
import com.example.todoapp.Schemas.*
import com.example.domain.TodoRepository
import com.example.domain.CategoryRepository
import cats.effect.IO
import sttp.tapir.server.ServerEndpoint
import com.example.todoapp.Requests.CreateTodoRequestSchema
import com.example.domain.Todo
import com.example.domain.Category
import com.example.todoapp.Requests.PutTodoRequestSchema
import com.example.domain.TodoId

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

    def createTodoLogic(schema: CreateTodoRequestSchema): IO[Either[Unit, TodoResponse]] = {
      for {
        createdTodoId <- todoRepository.createTodo(schema.toTodo)
        categories    <- categoryRepository.fetchAllCategory
      } yield {
        Right(
          TodoResponse.fromTodoWithCategoryOpt(schema.toTodo.copy(id = Some(createdTodoId)), categories.find(_.id == schema.categoryId))
        )
      }
    }

    def getAllCategoriesLogic: Unit => IO[Either[Unit, List[Category]]] = _ => {
      categoryRepository.fetchAllCategory.map { categories =>
        Right(categories.toList)
      }
    }

    def putTodoLogic: ((Int, PutTodoRequestSchema)) => IO[Either[Unit, TodoResponse]] = (todoIdWithRequestSchema: (Int, PutTodoRequestSchema)) => {
      val todoId      = TodoId(todoIdWithRequestSchema._1)
      val updatedTodo = todoIdWithRequestSchema._2.toTodo(todoId)

      for {
        _          <- todoRepository.updateTodo(
          todoId,
          updatedTodo
        )
        categories <- categoryRepository.fetchAllCategory
      } yield {
        Right(
          TodoResponse.fromTodoWithCategoryOpt(updatedTodo, categories.find(_.id == updatedTodo.categoryId))
        )
      }
    }

    def deleteTodoLogic: (Int) => IO[Either[Unit, Unit]] = (todoId) => {
      todoRepository.deleteTodo(TodoId(todoId)).map(Right.apply)
    }
  }

  private def createTodoEndpoint: PublicEndpoint[CreateTodoRequestSchema, Unit, TodoResponse, Any] = {
    endpoint.post.in("api" / "todos").in(jsonBody[CreateTodoRequestSchema]).out(jsonBody[TodoResponse])
  }

  private def getTodoEndpoint: PublicEndpoint[Unit, Unit, List[TodoResponse], Any] = {
    endpoint.get.in("api" / "todos").out(jsonBody[List[TodoResponse]])
  }

  private def putTodoEndpoint: PublicEndpoint[(Int, PutTodoRequestSchema), Unit, TodoResponse, Any] = {
    endpoint.put.in("api" / "todos" / path[Int]).in(jsonBody[PutTodoRequestSchema]).out(jsonBody[TodoResponse])
  }

  private def deleteTodoEndpoint: PublicEndpoint[Int, Unit, Unit, Any] = {
    endpoint.delete.in("api" / "todos" / path[Int])
  }

  private def getAllCategoriesEndpoint: PublicEndpoint[Unit, Unit, List[Category], Any] = {
    endpoint.get.in("api" / "categories").out(jsonBody[List[Category]])
  }

  val endpoints: List[ServerEndpoint[Any, IO]] = List(
    getTodoEndpoint.serverLogic(ApiEndpointServerLogics.getTodoLogic),
    createTodoEndpoint.serverLogic(ApiEndpointServerLogics.createTodoLogic),
    putTodoEndpoint.serverLogic(ApiEndpointServerLogics.putTodoLogic),
    deleteTodoEndpoint.serverLogic(ApiEndpointServerLogics.deleteTodoLogic),
    getAllCategoriesEndpoint.serverLogic(ApiEndpointServerLogics.getAllCategoriesLogic),
  )
}
