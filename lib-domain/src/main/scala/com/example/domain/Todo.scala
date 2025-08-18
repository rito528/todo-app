package com.example.domain

opaque type TodoId[V <: Id] = Id

type NumberedTodoId    = TodoId[Id.Numbered]
type NotNumberedTodoId = TodoId[Id.NotNumbered.type]

object TodoId {
  val None: NotNumberedTodoId = Id.NotNumbered

  def apply(value: Int): NumberedTodoId = Id.apply(value)

  extension (id: NumberedTodoId) {
    def unwrap: Int = id.value.get
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

final case class Todo[I <: Id](
  id:         TodoId[I],
  categoryId: Option[NumberedCategoryId],
  title:      Title,
  body:       Body,
  state:      TodoState,
)

object Todo {
  def apply(categoryId: Option[NumberedCategoryId], title: Title, body: Body): Todo[Id.NotNumbered.type] = {
    Todo(
      id         = TodoId.None,
      categoryId = categoryId,
      title      = title,
      body       = body,
      state      = TodoState.initial
    )
  }
}
