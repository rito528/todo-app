package com.example.domain

opaque type TodoId = Int

object TodoId {
  def apply(value: Int): TodoId = {
    require(value >= 0)
    value
  }

  def intoInner(value: TodoId): Int = value
}

opaque type Title = String

object Title {
  def apply(value: String): Title = {
    require(value.length <= 255)
    value
  }

  def intoInner(value: Title): String = value
}

opaque type Body = String

object Body {
  def intoInner(value: Body): String = value
}

enum TodoState {
  case Todo
  case Progressing
  case Done
}

object TodoState {
  val initial = Todo
}

final case class Todo(
  id: Option[TodoId],
  categoryId: CategoryId,
  title: Title,
  body: Body,
  state: TodoState,
)

object Todo {
  def apply(categoryId: CategoryId, title: Title, body: Body): Todo = {
    Todo(
      id = None,
      categoryId = categoryId,
      title = title,
      body = body,
      state = TodoState.initial
    )
  }
}
