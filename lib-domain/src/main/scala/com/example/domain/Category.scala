package com.example.domain

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

opaque type CategoryId[I <: Id] = Id

type NumberedCategoryId    = CategoryId[Id.Numbered]
type NotNumberedCategoryId = CategoryId[Id.NotNumbered.type]

object CategoryId {
  val None: NotNumberedCategoryId = Id.NotNumbered

  def apply(value: PositiveInt): NumberedCategoryId = {
    Id(value)
  }

  extension (id: CategoryId[Id.Numbered]) {
    def unwrap: Int = id.value.get
  }
}

type CategoryName = MinLength[1] & MaxLength[32] & Not[Match["\r\n|\n|\r"]]

type CategorySlug = MinLength[1] & MaxLength[32] & Match["^[0-9a-zA-Z]+$"]

type CategoryColor = Match["^#[0-9a-fA-F]{6}$"]

final case class Category[I <: Id](
  id:    CategoryId[I],
  name:  String :| CategoryName,
  slug:  String :| CategorySlug,
  color: String :| CategoryColor
)

object Category {
  def apply(name: String :| CategoryName, slug: String :| CategorySlug, color: String :| CategoryColor): Category[Id.NotNumbered.type] = {
    Category(CategoryId.None, name, slug, color)
  }
}
