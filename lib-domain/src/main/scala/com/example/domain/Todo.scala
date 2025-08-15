package com.example.domain

opaque type TodoId = Int

object TodoId {
  def apply(value: Int): TodoId = {
    require(value >= 0)
    value
  }

  extension (value: TodoId) {
    def unwrap: Int = value
  }
}

opaque type Title = String

object Title  {
  def apply(value: String): Title = {
    require(value.length <= 255)
    value
  }

  extension (value: Title) {
    def unwrap: String = value
  }
}

opaque type Body = String

object Body   {
  def apply(value: String): Body = value

  extension (value: Body) {
    def unwrap: String = value
  }
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
  id:         Option[TodoId],
  categoryId: Option[CategoryId],
  title:      Title,
  body:       Body,
  state:      TodoState,
)

object Todo {
  def apply(categoryId: Option[CategoryId], title: Title, body: Body): Todo = {
    Todo(
      id         = None,
      categoryId = categoryId,
      title      = title,
      body       = body,
      state      = TodoState.initial
    )
  }
}
