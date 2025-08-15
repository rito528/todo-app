package com.example.domain

trait CategoryRepository[F[_]] {

  /**
    * @return 全ての [[Category]] を返す作用
    */
  def fetchAllCategory: F[Vector[Category]]

  /**
    * @param category 作成したい [[Category]]
    * @return [[Category]] 作成し、作成した [[Category]] の [[CategoryId]] を返す作用
    */
  def createCategory(category: Category): F[CategoryId]

  /**
    * @param categoryId 更新したい [[Category]] の [[CategoryId]]
    * @param category 更新後の [[Category]]
    * @return `categoryId` に紐づく [[Category]] を `category` に更新する作用
    */
  def updateCategory(categoryId: CategoryId, category: Category): F[Unit]

  /**
    * @param categoryId 削除したい [[Category]] に紐づく [[CategoryId]]
    * @return `categoryId` に紐づく [[Category]] を削除する作用
    */
  def deleteCategory(categoryId: CategoryId): F[Unit]

}
