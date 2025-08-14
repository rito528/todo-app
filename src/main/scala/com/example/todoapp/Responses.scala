package com.example.todoapp

import com.example.domain.TodoId
import com.example.domain.Title
import com.example.domain.Body
import com.example.domain.TodoState
import com.example.domain.Category
import com.example.domain.Todo

object Responses {
  case class TodoResponse(
    id:       TodoId,
    category: Option[Category],
    title:    Title,
    body:     Body,
    state:    TodoState
  )

  object TodoResponse {

    def fromTodoWithCategoryOpt(todo: Todo, category: Option[Category]): TodoResponse = {
      // NOTE: TodoId が None になるのは ID の採番がされていない時のみである。
      //       レスポンスとして Todo を返したい時は ID が採番済であることを保証すべき。
      require(todo.id.isDefined && todo.categoryId == category.flatMap(_.id))

      TodoResponse(
        todo.id.get,
        category,
        todo.title,
        todo.body,
        todo.state
      )
    }
  }
}
