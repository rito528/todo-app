package com.example.domain

trait TodoRepository[F[_]] {

  /**
    * @return 全ての [[Todo]] を返す作用
    */
  def fetchAllTodo: F[Vector[Todo[Id.Numbered]]]

  /**
    * @param todo 作成したい [[Todo]]
    * @return [[Todo]] を作成し、作成した [[Todo]] の [[TodoId]] を返す作用
    */
  def createTodo(todo: Todo[Id.NotNumbered.type]): F[NumberedTodoId]

  /**
    * @param todo 更新したい [[Todo]]
    * @return `todoId` を `todo` に更新する作用
    */
  def updateTodo(todo: Todo[Id.Numbered]): F[Unit]

  /**
    * @param todoId 削除したい [[Todo]] の [[TodoId]]
    * @return `todoId` を削除する作用
    */
  def deleteTodo(todoId: NumberedTodoId): F[Unit]

}
