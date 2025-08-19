package com.example.todoapp.endpoints

import sttp.tapir.*
import sttp.tapir.json.circe.*
import com.example.todoapp.Responses.TodoResponse
import com.example.todoapp.Encoders.{ encodeCategory, encodeCategories, encodeTodoResponses }
import com.example.todoapp.Decoders.{ decodeCategory, decodeCategories, decodeTodoResponses }
import com.example.todoapp.Schemas.*
import com.example.todoapp.Codecs.{ numberedTodoIdCodec, numberedCategoryIdCodec }
import com.example.domain.TodoRepository
import com.example.domain.CategoryRepository
import cats.effect.IO
import sttp.tapir.server.ServerEndpoint
import com.example.todoapp.Requests.CreateTodoRequestSchema
import com.example.domain.Todo
import com.example.domain.Category
import com.example.todoapp.Requests.PutTodoRequestSchema
import com.example.todoapp.Requests.CreateCategoryRequestSchema
import com.example.todoapp.Requests.PutCategoryRequestSchema
import com.example.domain.Id
import com.example.domain.NumberedTodoId
import com.example.domain.NumberedCategoryId

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
          TodoResponse.fromTodoWithCategoryOpt(
            todo,
            categories.find(category => Some(category.id) == todo.categoryId)
          )
        ).toList)
      }

    def createTodoLogic(schema: CreateTodoRequestSchema): IO[Either[Unit, TodoResponse]] = {
      for {
        createdTodoId <- todoRepository.createTodo(schema.toTodo)
        categories    <- categoryRepository.fetchAllCategory
      } yield {
        Right(
          TodoResponse.fromTodoWithCategoryOpt(
            schema.toTodo.copy(id = createdTodoId),
            categories.find(category => Some(category.id) == schema.categoryId)
          )
        )
      }
    }

    def getAllCategoriesLogic: Unit => IO[Either[Unit, List[Category[Id.Numbered]]]] = _ => {
      categoryRepository.fetchAllCategory.map { categories =>
        Right(categories.toList)
      }
    }

    def putTodoLogic(todoId: NumberedTodoId, schema: PutTodoRequestSchema): IO[Either[Unit, TodoResponse]] = {
      val updatedTodo = schema.toTodo(todoId)

      for {
        _          <- todoRepository.updateTodo(updatedTodo)
        categories <- categoryRepository.fetchAllCategory
      } yield {
        Right(
          TodoResponse.fromTodoWithCategoryOpt(
            updatedTodo,
            categories.find(category => Some(category.id) == updatedTodo.categoryId)
          )
        )
      }
    }

    def deleteTodoLogic(todoId: NumberedTodoId): IO[Either[Unit, Unit]] = {
      todoRepository.deleteTodo(todoId).map(Right.apply)
    }

    def createCategoryLogic(schema: CreateCategoryRequestSchema): IO[Either[Unit, Category[Id.Numbered]]] = {
      for {
        categoryId <- categoryRepository.createCategory(schema.toCategory)
      } yield Right(schema.toCategory.copy(id = categoryId))
    }

    def putCategoryLogic(categoryId: NumberedCategoryId, schema: PutCategoryRequestSchema): IO[Either[Unit, Category[Id.Numbered]]] = {
      val category = schema.toCategory(categoryId)

      for {
        _ <- categoryRepository.updateCategory(category)
      } yield Right(category)
    }

    def deleteCategoryLogic(categoryId: NumberedCategoryId): IO[Either[Unit, Unit]] = {
      categoryRepository.deleteCategory(categoryId).map(Right.apply)
    }
  }

  private def createTodoEndpoint: PublicEndpoint[CreateTodoRequestSchema, Unit, TodoResponse, Any] = {
    endpoint.post.in("api" / "todos").in(jsonBody[CreateTodoRequestSchema]).out(jsonBody[TodoResponse])
  }

  private def getTodoEndpoint: PublicEndpoint[Unit, Unit, List[TodoResponse], Any] = {
    endpoint.get.in("api" / "todos").out(jsonBody[List[TodoResponse]])
  }

  private def putTodoEndpoint: PublicEndpoint[(NumberedTodoId, PutTodoRequestSchema), Unit, TodoResponse, Any] = {
    endpoint.put.in("api" / "todos" / path[NumberedTodoId]).in(jsonBody[PutTodoRequestSchema]).out(jsonBody[TodoResponse])
  }

  private def deleteTodoEndpoint: PublicEndpoint[NumberedTodoId, Unit, Unit, Any] = {
    endpoint.delete.in("api" / "todos" / path[NumberedTodoId])
  }

  private def getAllCategoriesEndpoint: PublicEndpoint[Unit, Unit, List[Category[Id.Numbered]], Any] = {
    endpoint.get.in("api" / "categories").out(jsonBody[List[Category[Id.Numbered]]])
  }

  private def createCategoryEndpoint: PublicEndpoint[CreateCategoryRequestSchema, Unit, Category[Id.Numbered], Any] = {
    endpoint.post.in("api" / "categories").in(jsonBody[CreateCategoryRequestSchema]).out(jsonBody[Category[Id.Numbered]])
  }

  private def putCategoryEndpoint: PublicEndpoint[(NumberedCategoryId, PutCategoryRequestSchema), Unit, Category[Id.Numbered], Any] = {
    endpoint.put.in("api" / "categories" / path[NumberedCategoryId]).in(jsonBody[PutCategoryRequestSchema]).out(jsonBody[Category[Id.Numbered]])
  }

  private def deleteCategoryEndpoint: PublicEndpoint[NumberedCategoryId, Unit, Unit, Any] = {
    endpoint.delete.in("api" / "categories" / path[NumberedCategoryId])
  }

  val endpoints: List[ServerEndpoint[Any, IO]] = List(
    getTodoEndpoint.serverLogic(ApiEndpointServerLogics.getTodoLogic),
    createTodoEndpoint.serverLogic(ApiEndpointServerLogics.createTodoLogic),
    putTodoEndpoint.serverLogic(ApiEndpointServerLogics.putTodoLogic),
    deleteTodoEndpoint.serverLogic(ApiEndpointServerLogics.deleteTodoLogic),
    getAllCategoriesEndpoint.serverLogic(ApiEndpointServerLogics.getAllCategoriesLogic),
    createCategoryEndpoint.serverLogic(ApiEndpointServerLogics.createCategoryLogic),
    putCategoryEndpoint.serverLogic(ApiEndpointServerLogics.putCategoryLogic),
    deleteCategoryEndpoint.serverLogic(ApiEndpointServerLogics.deleteCategoryLogic)
  )
}
