package com.example.todoapp

import com.example.domain.TodoId
import com.example.domain.Title
import com.example.domain.Body
import com.example.domain.TodoState
import com.example.domain.Category
import com.example.domain.Todo
import io.circe.Encoder
import io.circe.Decoder
import sttp.tapir.Schema
import com.example.todoapp.Encoders.{ encodeTodoId, encodeCategory, encodeTitle, encodeBody, encodeState }
import com.example.todoapp.Decoders.{ decodeTodoId, decodeCategory, decodeTitle, decodeBody, decodeState }
import com.example.todoapp.Schemas.*
import com.example.domain.Id
import com.example.domain.NumberedTodoId

object Responses {
  case class TodoResponse(
    id:       NumberedTodoId,
    category: Option[Category],
    title:    Title,
    body:     Body,
    state:    TodoState
  ) derives Encoder, Decoder, Schema

  object TodoResponse {

    def fromTodoWithCategoryOpt(todo: Todo[Id.Numbered], category: Option[Category]): TodoResponse = {
      require(todo.categoryId == category.flatMap(_.id))

      TodoResponse(
        todo.id,
        category,
        todo.title,
        todo.body,
        todo.state
      )
    }
  }
}
