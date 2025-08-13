package com.example.todoapp

import com.example.domain.Title
import com.example.domain.Body
import com.example.domain.Todo
import sttp.tapir.Schema
import io.circe.Encoder
import io.circe.Decoder
import com.example.todoapp.Encoders.{ encodeTitle, encodeBody, encodeCategoryId }
import com.example.todoapp.Decoders.{ decodeTitle, decodeBody, decodeCategoryId }
import com.example.todoapp.Schemas.*
import com.example.domain.CategoryId

object Requests {
  case class CreateTodoRequestSchema(
    title:      Title,
    body:       Body,
    categoryId: Option[CategoryId],
  ) derives Encoder, Decoder, Schema

  object CreateTodoRequestSchema {
    extension (schema: CreateTodoRequestSchema) {
      def toTodo: Todo = Todo(schema.categoryId, schema.title, schema.body)
    }
  }
}
