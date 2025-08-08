package com.example.domain

trait TodoRepository[F[_]] {

  /**
    * @return 全ての [[Todo]] を返す作用
    */
  def fetchAllTodo: F[Vector[Todo]]

  /**
    * @param todo 作成したい [[Todo]]
    * @return [[Todo]] を作成する作用
    */
  def createTodo(todo: Todo): F[Unit]

  /**
    * @param todoId 更新したい [[Todo]] の [[TodoId]]
    * @param todo 更新したい [[Todo]]
    * @return `todoId` を `todo` に更新する作用
    */
  def updateTodo(todoId: TodoId, todo: Todo): F[Unit]

  /**
    * @param todoId 削除したい [[Todo]] の [[TodoId]]
    * @return `todoId` を削除する作用
    */
  def deleteTodo(todoId: TodoId): F[Unit]

}
