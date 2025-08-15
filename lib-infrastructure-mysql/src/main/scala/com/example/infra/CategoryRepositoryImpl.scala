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
import cats.implicits.*

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

  override def createCategory(category: Category): F[CategoryId] = pool.transactor.use { xa =>
    (for {
      _  <- sql"""
      | INSERT INTO category (name, slug, color) 
      | VALUES (${category.name.unwrap}, ${category.slug.unwrap}, ${category.color.unwrap})"""
        .stripMargin
        .update
        .run
      id <- sql"SELECT LAST_INSERT_ID()".query[Int].unique
    } yield CategoryId(id))
      .transact(xa)
  }

  override def updateCategory(categoryId: CategoryId, category: Category): F[Unit] = pool.transactor.use { xa =>
    sql"""
    | UPDATE category SET
    | name = ${category.name.unwrap},
    | slug = ${category.slug.unwrap},
    | color = ${category.color.unwrap}
    | WHERE id = ${categoryId.unwrap}"""
      .stripMargin
      .update
      .run
      .transact(xa)
      .void
  }

  override def deleteCategory(categoryId: CategoryId): F[Unit] = pool.transactor.use { xa =>
    (for {
      _ <- sql"DELETE FROM category WHERE id = ${categoryId.unwrap}"
        .update
        .run
      _ <- sql"UPDATE to_do SET category_id = NULL WHERE category_id = ${categoryId.unwrap}"
        .update
        .run
    } yield ())
      .transact(xa)
      .void

  }

}
