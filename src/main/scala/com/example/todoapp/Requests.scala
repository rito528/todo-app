package com.example.todoapp

import com.example.domain.Title
import com.example.domain.Body
import com.example.domain.Todo
import sttp.tapir.Schema
import io.circe.Encoder
import io.circe.Decoder
import com.example.todoapp.Encoders.{ encodeTitle, encodeBody, encodeState, encodeCategoryId, encodeCategoryName, encodeCategorySlug, encodeCategoryColor }
import com.example.todoapp.Decoders.{ decodeTitle, decodeBody, decodeState, decodeCategoryId, decodeCategoryName, decodeCategorySlug, decodeCategoryColor }
import com.example.todoapp.Schemas.*
import com.example.domain.CategoryId
import com.example.domain.TodoState
import com.example.domain.CategorySlug
import com.example.domain.CategoryName
import com.example.domain.CategoryColor
import com.example.domain.Category
import com.example.domain.Id
import com.example.domain.NumberedTodoId

object Requests {
  case class CreateTodoRequestSchema(
    title:      Title,
    body:       Body,
    categoryId: Option[CategoryId],
  ) derives Encoder, Decoder, Schema

  object CreateTodoRequestSchema {
    extension (schema: CreateTodoRequestSchema) {
      def toTodo: Todo[Id.NotNumbered.type] = Todo(schema.categoryId, schema.title, schema.body)
    }
  }

  case class PutTodoRequestSchema(
    title:      Title,
    body:       Body,
    categoryId: Option[CategoryId],
    state:      TodoState
  ) derives Encoder, Decoder, Schema

  object PutTodoRequestSchema {
    extension (schema: PutTodoRequestSchema) {
      def toTodo(todoId: NumberedTodoId): Todo[Id.Numbered] = Todo(
        todoId,
        schema.categoryId,
        schema.title,
        schema.body,
        schema.state
      )
    }
  }

  case class CreateCategoryRequestSchema(
    name:  CategoryName,
    slug:  CategorySlug,
    color: CategoryColor
  ) derives Encoder, Decoder, Schema

  object CreateCategoryRequestSchema {
    extension (schema: CreateCategoryRequestSchema) {
      def toCategory: Category = Category(schema.name, schema.slug, schema.color)
    }
  }

  case class PutCategoryRequestSchema(
    name:  CategoryName,
    slug:  CategorySlug,
    color: CategoryColor
  ) derives Encoder, Decoder, Schema

  object PutCategoryRequestSchema {
    extension (schema: PutCategoryRequestSchema) {
      def toCategory: Category = Category(schema.name, schema.slug, schema.color)
    }
  }
}
