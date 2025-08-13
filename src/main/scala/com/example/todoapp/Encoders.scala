package com.example.todoapp

import io.circe.generic.auto.*
import io.circe.Encoder
import com.example.domain.TodoId
import com.example.domain.CategoryId
import com.example.domain.Title
import com.example.domain.Body
import com.example.domain.TodoState
import com.example.domain.CategoryName
import com.example.domain.CategorySlug
import com.example.domain.CategoryColor
import com.example.todoapp.Responses.TodoResponse
import com.example.domain.Todo
import com.example.domain.Category

object Encoders {
  // NOTE: Opaque type の Encoder は明示的に実装する必要がある
  // ref: https://github.com/circe/circe/issues/1829

  given encodeTodoId: Encoder[TodoId]   = Encoder[Int].contramap(_.unwrap)
  given encodeTitle: Encoder[Title]     = Encoder[String].contramap(_.unwrap)
  given encodeBody: Encoder[Body]       = Encoder[String].contramap(_.unwrap)
  given encodeState: Encoder[TodoState] = Encoder[String].contramap(_.toString)
  given encodeTodo: Encoder[Todo]       = Encoder.derived

  given encodeCategoryId: Encoder[CategoryId]       = Encoder[Int].contramap(_.unwrap)
  given encodeCategoryName: Encoder[CategoryName]   = Encoder[String].contramap(_.unwrap)
  given encodeCategorySlug: Encoder[CategorySlug]   = Encoder[String].contramap(_.unwrap)
  given encodeCategoryColor: Encoder[CategoryColor] = Encoder[String].contramap(_.unwrap)

  given encodeTodoResponses: Encoder[List[TodoResponse]] = Encoder.encodeList[TodoResponse]
  given encodeCategories: Encoder[List[Category]]        = Encoder.encodeList[Category]
}
