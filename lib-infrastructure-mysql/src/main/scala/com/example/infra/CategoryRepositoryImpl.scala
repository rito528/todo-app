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
import com.example.domain.NumberedCategoryId
import cats.implicits.*
import com.example.domain.Id
import io.github.iltotore.iron.*

class CategoryRepositoryImpl[F[_]: Async](
  using pool: DatabaseConnectionPool
) extends CategoryRepository[F] {

  override def fetchAllCategory: F[Vector[Category[Id.Numbered]]] = pool.transactor.use { xa =>
    given categoryRead: Read[Category[Id.Numbered]] = Read[(Int, String, String, String)]
      .map { case (id, name, slug, color) =>
        Category(
          CategoryId(id.refineUnsafe),
          name.refineUnsafe[CategoryName],
          slug.refineUnsafe[CategorySlug],
          CategoryColor(color)
        )
      };

    sql"SELECT id, name, slug, color FROM category"
      .query[Category[Id.Numbered]]
      .to[Vector]
      .transact(xa)
  }

  override def createCategory(category: Category[Id.NotNumbered.type]): F[NumberedCategoryId] = pool.transactor.use { xa =>
    (for {
      _  <- sql"""
      | INSERT INTO category (name, slug, color)
      | VALUES (${category.name: String}, ${category.slug: String}, ${category.color.unwrap})"""
        .stripMargin
        .update
        .run
      id <- sql"SELECT LAST_INSERT_ID()".query[Int].unique
    } yield CategoryId(id.refineUnsafe))
      .transact(xa)
  }

  override def updateCategory(category: Category[Id.Numbered]): F[Unit] = pool.transactor.use { xa =>
    sql"""
    | UPDATE category SET
    | name = ${category.name: String},
    | slug = ${category.slug: String},
    | color = ${category.color.unwrap}
    | WHERE id = ${category.id.unwrap}"""
      .stripMargin
      .update
      .run
      .transact(xa)
      .void
  }

  override def deleteCategory(categoryId: NumberedCategoryId): F[Unit] = pool.transactor.use { xa =>
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
