package com.example.domain

trait CategoryRepository[F[_]] {

  /**
    * @return 全ての [[Category]] を返す作用
    */
  def fetchAllCaqtegory: F[Vector[Category]]

}
