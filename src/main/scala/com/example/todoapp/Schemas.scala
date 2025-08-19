package com.example.todoapp

import sttp.tapir.*
import com.example.domain.Title
import com.example.domain.Body
import com.example.domain.TodoState
import com.example.domain.CategoryId
import com.example.domain.CategoryName
import com.example.domain.CategorySlug
import com.example.domain.CategoryColor
import com.example.domain.Category
import com.example.domain.NumberedTodoId
import com.example.domain.NumberedCategoryId
import com.example.domain.Id
import io.github.iltotore.iron.*

object Schemas {
  // Schema を実装するには given ではなく implicit def を用いる必要がある
  // ref: https://tapir.softwaremill.com/en/latest/endpoint/schemas.html#derivation-for-recursive-types

  implicit def todoIdSchema: Schema[NumberedTodoId] = Schema.schemaForInt.as[NumberedTodoId]
  implicit def titleSchema: Schema[Title]           = Schema.string[Title]
  implicit def bodySchema: Schema[Body]             = Schema.string[Body]
  implicit def todoStateSchema: Schema[TodoState]   = Schema.string[TodoState]

  implicit def categoryIdSchema: Schema[NumberedCategoryId]         = Schema.schemaForInt.as[NumberedCategoryId]
  implicit def categoryNameSchema: Schema[String :| CategoryName]   = Schema.string[String :| CategoryName]
  implicit def categorySlugSchema: Schema[String :| CategorySlug]   = Schema.string[String :| CategorySlug]
  implicit def categoryColorSchema: Schema[String :| CategoryColor] = Schema.string[String :| CategoryColor]
  implicit def categorySchema: Schema[Category[Id.Numbered]]        = Schema.derived
}
