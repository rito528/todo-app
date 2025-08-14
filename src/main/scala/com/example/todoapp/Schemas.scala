package com.example.todoapp

import com.example.domain.TodoId
import sttp.tapir.*
import com.example.domain.Title
import com.example.domain.Body
import com.example.domain.TodoState
import com.example.domain.CategoryId
import com.example.domain.CategoryName
import com.example.domain.CategorySlug
import com.example.domain.CategoryColor

object Schemas {
  // Schema を実装するには given ではなく implicit def を用いる必要がある
  // ref: https://tapir.softwaremill.com/en/latest/endpoint/schemas.html#derivation-for-recursive-types

  implicit def todoIdSchema: Schema[TodoId]       = Schema.schemaForInt.as[TodoId]
  implicit def titleSchema: Schema[Title]         = Schema.string[Title]
  implicit def bodySchema: Schema[Body]           = Schema.string[Body]
  implicit def todoStateSchema: Schema[TodoState] = Schema.string[TodoState]

  implicit def categoryIdSchema: Schema[CategoryId]       = Schema.schemaForInt.as[CategoryId]
  implicit def categoryNameSchema: Schema[CategoryName]   = Schema.string[CategoryName]
  implicit def categorySlugSchema: Schema[CategorySlug]   = Schema.string[CategorySlug]
  implicit def categoryColorSchema: Schema[CategoryColor] = Schema.string[CategoryColor]
}
