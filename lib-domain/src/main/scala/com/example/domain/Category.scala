package com.example.domain

opaque type CategoryId = Int

object CategoryId {
  def apply(value: Int): CategoryId = {
    require(value >= 0)
    value
  }

  extension (value: CategoryId) {
    def unwrap: Int = value
  }
}

opaque type CategoryName = String

object CategoryName {
  def apply(value: String): CategoryName = {
    require(value.length <= 32)
    value
  }

  extension (value: CategoryName) {
    def unwrap: String = value
  }
}

opaque type CategorySlug = String

object CategorySlug {
  def apply(value: String): CategorySlug = {
    require(value.length <= 32)
    value
  }

  extension (value: CategorySlug) {
    def unwrap: String = value
  }
}

opaque type CategoryColor = String

object CategoryColor {
  def apply(value: String): CategoryColor = {
    require(value.startsWith("#") && value.length == 8)
    value
  }

  extension (value: CategoryColor) {
    def unwrap: String = value
  }
}

final case class Category(
  id: Option[CategoryId],
  name: CategoryName,
  slug: CategorySlug,
  color: CategoryColor
)

object Category {
  def apply(name: CategoryName, slug: CategorySlug, color: CategoryColor): Category = {
    Category(None, name, slug, color)
  }
}
