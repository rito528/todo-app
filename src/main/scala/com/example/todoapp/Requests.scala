package com.example.todoapp

import com.example.domain.Title
import com.example.domain.Body
import com.example.domain.Todo
import sttp.tapir.Schema
import io.circe.Encoder
import io.circe.Decoder
import com.example.todoapp.Encoders.{ encodeTitle, encodeBody }
import com.example.todoapp.Decoders.{ decodeTitle, decodeBody }
import com.example.todoapp.Schemas.*

object Requests {
  case class CreateTodoRequestSchema(
    title: Title,
    body:  Body
  ) derives Encoder, Decoder, Schema

  object CreateTodoRequestSchema {
    extension (schema: CreateTodoRequestSchema) {
      def toTodo: Todo = Todo(schema.title, schema.body)
    }
  }
}
