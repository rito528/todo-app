package com.example.infra

import doobie.*
import doobie.implicits.*
import com.example.domain.TodoRepository
import com.example.domain.TodoId
import com.example.domain.Todo
import cats.effect.kernel.Async
import cats.implicits.*

class TodoRepositoryImpl[F[_]: Async](
  using pool: DatabaseConnectionPool
) extends TodoRepository[F] {

  override def fetchAllTodo: F[Vector[Todo]] = pool.transactor.use { xa =>
    for {
      todo <- sql"SELECT id, category_id, title, body, state FROM to_do"
            .query[Vector[Todo]]
            .to[Vector]
            .transact(xa)
    } yield todo
  }

  override def createTodo(todo: Todo): F[Todo] = ???

  override def updateTodo(todoId: TodoId, todo: Todo): F[Todo] = ???

  override def deleteTodo(todoId: TodoId): F[Unit] = ???

}
