package com.example.infra

import doobie.*
import doobie.implicits.*
import com.example.domain.TodoRepository
import com.example.domain.TodoId
import com.example.domain.Todo
import cats.effect.kernel.Async
import cats.implicits.*
import com.example.domain.TodoState
import com.example.domain.Title
import com.example.domain.CategoryId
import com.example.domain.Body
import com.example.domain.Id
import com.example.domain.NumberedTodoId

class TodoRepositoryImpl[F[_]: Async](
  using pool: DatabaseConnectionPool
) extends TodoRepository[F] {

  override def fetchAllTodo: F[Vector[Todo[Id.Numbered]]] = pool.transactor.use { xa =>
    given todoRead: Read[Todo[Id.Numbered]] = Read[(Int, Option[Int], String, String, Int)]
      .map { case (id, categoryId, title, body, state) =>
        val todoState = state match {
          case 2 => TodoState.Progressing
          case 3 => TodoState.Done
          case _ => TodoState.Todo
        }

        Todo(
          TodoId(id),
          categoryId.map(CategoryId.apply),
          Title(title),
          Body(body),
          todoState
        )
      }

    sql"SELECT id, category_id, title, body, state FROM to_do"
      .query[Todo[Id.Numbered]]
      .to[Vector]
      .transact(xa)
  }

  private def todoStateToInt(state: TodoState): Int = {
    state match {
      case TodoState.Todo        => 1
      case TodoState.Progressing => 2
      case TodoState.Done        => 3
    }
  }

  override def createTodo(todo: Todo[Id.NotNumbered.type]): F[NumberedTodoId] = pool.transactor.use { xa =>
    (for {
      _  <- sql"""
        | INSERT INTO to_do (category_id, title, body, state) 
        | VALUES (${todo.categoryId.map(_.unwrap)}, ${todo.title.unwrap}, ${todo.body.unwrap}, ${todoStateToInt(todo.state)})"""
        .stripMargin
        .update
        .run
      id <- sql"SELECT LAST_INSERT_ID()".query[Int].unique
    } yield TodoId(id))
      .transact(xa)
  }

  override def updateTodo(todo: Todo[Id.Numbered]): F[Unit] = pool.transactor.use { xa =>
    sql"""
      | UPDATE to_do SET 
      | category_id = ${todo.categoryId.map(_.unwrap)},
      | title = ${todo.title.unwrap},
      | body = ${todo.body.unwrap},
      | state = ${todoStateToInt(todo.state)}
      | WHERE id = ${todo.id.unwrap}"""
      .stripMargin
      .update
      .run
      .transact(xa)
      .void
  }

  override def deleteTodo(todoId: NumberedTodoId): F[Unit] = pool.transactor.use { xa =>
    sql"DELETE FROM to_do WHERE id = ${todoId.unwrap}"
      .update
      .run
      .transact(xa)
      .void
  }

}
