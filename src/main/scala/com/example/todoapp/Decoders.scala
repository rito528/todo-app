package com.example.todoapp

import io.circe.generic.auto.*
import io.circe.Decoder
import com.example.domain.TodoId
import com.example.domain.Title
import com.example.domain.Body
import com.example.domain.TodoState
import com.example.domain.CategoryId
import com.example.domain.CategoryName
import com.example.domain.CategorySlug
import com.example.domain.CategoryColor
import com.example.todoapp.Responses.TodoResponse
import com.example.domain.Category
import com.example.domain.NumberedTodoId
import com.example.domain.NumberedCategoryId
import com.example.domain.Id

object Decoders {
  given decodeTodoId: Decoder[NumberedTodoId] = Decoder[Int].map(TodoId.apply)
  given decodeTitle: Decoder[Title]           = Decoder[String].map(Title.apply)
  given decodeBody: Decoder[Body]             = Decoder[String].map(Body.apply)
  given decodeState: Decoder[TodoState]       = Decoder[String].map { state =>
    state match {
      case "Progressing" => TodoState.Progressing
      case "Done"        => TodoState.Done
      case _             => TodoState.Todo
    }
  }

  given decodeCategoryId: Decoder[NumberedCategoryId]  = Decoder[Int].map(CategoryId.apply)
  given decodeCategoryName: Decoder[CategoryName]      = Decoder[String].map(CategoryName.apply)
  given decodeCategorySlug: Decoder[CategorySlug]      = Decoder[String].map(CategorySlug.apply)
  given decodeCategoryColor: Decoder[CategoryColor]    = Decoder[String].map(CategoryColor.apply)
  given decodeCategory: Decoder[Category[Id.Numbered]] = Decoder.derived

  given decodeTodoResponses: Decoder[List[TodoResponse]]       = Decoder.decodeList[TodoResponse]
  given decodeCategories: Decoder[List[Category[Id.Numbered]]] = Decoder.decodeList[Category[Id.Numbered]]
}
