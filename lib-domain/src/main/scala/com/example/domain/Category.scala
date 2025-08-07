package com.example.domain

opaque type CategoryId = Int

object CategoryId {

  def apply(value: Int): CategoryId = {
    require(value >= 0)
    value
  }

  def intoInner(value: CategoryId): Int = value

}

opaque type CategoryName = String

object CategoryName {

  def apply(value: String): CategoryName = {
    require(value.length <= 32)
    value
  }

  def intoInner(value: CategoryName): String = value

}

opaque type CategorySlug = String

object CategorySlug {

  def apply(value: String): CategorySlug = {
    require(value.length <= 32)
    value
  }

  def intoInner(value: CategorySlug): String = value

}

opaque type CategoryColor = String

object CategoryColor {
  
  def apply(value: String): CategoryColor = {
    require(value.startsWith("#") && value.length == 8)
    value
  }

  def intoInner(value: CategoryColor): String = value

}

final case class Category(
  id: Option[CategoryId],
  name: CategoryName,
  slug: CategorySlug,
  color: CategoryColor
)
