package com.example.domain

opaque type CategoryId[V <: Id] = Id

type NumberedCategoryId    = CategoryId[Id.Numbered]
type NotNumberedCategoryId = CategoryId[Id.NotNumbered.type]

object CategoryId    {
  val None: NotNumberedCategoryId = Id.NotNumbered

  def apply(value: Int): NumberedCategoryId = {
    require(value >= 0)

    Id(value)
  }

  extension (id: CategoryId[Id.Numbered]) {
    def unwrap: Int = id.value.get
  }
}

opaque type CategoryName = String

object CategoryName  {
  def apply(value: String): CategoryName = {
    require(value.length <= 32)

    val regex = "\r\n|\n|\r".r
    require(!regex.matches(value))

    value
  }

  extension (value: CategoryName) {
    def unwrap: String = value
  }
}

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
  name:  CategoryName,
  slug:  CategorySlug,
  color: CategoryColor
)

object Category {
  def apply(name: CategoryName, slug: CategorySlug, color: CategoryColor): Category[Id.NotNumbered.type] = {
    Category(CategoryId.None, name, slug, color)
  }
}
