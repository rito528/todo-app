package com.example.domain

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

opaque type CategoryId[I <: Id] = Id

type NumberedCategoryId    = CategoryId[Id.Numbered]
type NotNumberedCategoryId = CategoryId[Id.NotNumbered.type]

object CategoryId    {
  val None: NotNumberedCategoryId = Id.NotNumbered

  def apply(value: PositiveInt): NumberedCategoryId = {
    Id(value)
  }

  extension (id: CategoryId[Id.Numbered]) {
    def unwrap: Int = id.value.get
  }
}

type CategoryName = MinLength[1] & MaxLength[32] & Not[Contain["\r\n"]] & Not[Contain["\n"]] & Not[Contain["\r"]]

opaque type CategorySlug = String

object CategorySlug  {
  def apply(value: String): CategorySlug = {
    require(value.length <= 32)

    val regex = "^[0-9a-zA-Z]+$".r
    require(regex.matches(value))

    value
  }

  extension (value: CategorySlug) {
    def unwrap: String = value
  }
}

opaque type CategoryColor = String

object CategoryColor {
  def apply(value: String): CategoryColor = {
    require(value.startsWith("#") && value.length == 7)
    value
  }

  extension (value: CategoryColor) {
    def unwrap: String = value
  }
}

final case class Category[I <: Id](
  id:    CategoryId[I],
  name:  String :| CategoryName,
  slug:  CategorySlug,
  color: CategoryColor
)

object Category {
  def apply(name: String :| CategoryName, slug: CategorySlug, color: CategoryColor): Category[Id.NotNumbered.type] = {
    Category(CategoryId.None, name, slug, color)
  }
}
