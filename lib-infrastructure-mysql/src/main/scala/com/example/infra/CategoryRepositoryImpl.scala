package com.example.infra

import doobie.*
import doobie.implicits.*
import cats.effect.kernel.Async
import com.example.domain.CategoryRepository
import com.example.domain.Category
import com.example.domain.CategorySlug
import com.example.domain.CategoryName
import com.example.domain.CategoryId
import com.example.domain.CategoryColor

class CategoryRepositoryImpl[F[_]: Async](
  using pool: DatabaseConnectionPool
) extends CategoryRepository[F] {

  override def fetchAllCategory: F[Vector[Category]] = pool.transactor.use { xa =>
    given categoryRead: Read[Category] = Read[(Int, String, String, String)]
      .map { case (id, name, slug, color) =>

        Category( 
          Some(CategoryId(id)),
          CategoryName(name),
          CategorySlug(slug),
          CategoryColor(color)
        )  
      };

    sql"SELECT id, name, slug, color FROM category"
      .query[Category]
      .to[Vector]
      .transact(xa)
  }
}
