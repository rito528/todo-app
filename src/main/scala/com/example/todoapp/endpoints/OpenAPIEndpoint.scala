package com.example.todoapp.endpoints

import sttp.tapir.server.ServerEndpoint
import cats.effect.IO
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.swagger.SwaggerUIOptions
import com.example.domain.TodoRepository
import com.example.domain.CategoryRepository

class OpenAPIEndpoint(
  using
  todoRepository:     TodoRepository[IO],
  categoryRepository: CategoryRepository[IO]
) {
  private val apiEndpoints = new ApiEndpoints

  val endpoints: List[ServerEndpoint[Any, IO]] =
    SwaggerInterpreter(
      swaggerUIOptions = SwaggerUIOptions.default.pathPrefix(List("document", "openapi", "docs"))
    )
      .fromServerEndpoints[IO](apiEndpoints.endpoints, "TodoApp OpenAPI Docs", "1.0.0")
}
