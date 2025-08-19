package com.example.domain

enum Id(val value: Option[PositiveInt]) {
  case NotNumbered extends Id(value = None)
  case Numbered(id: PositiveInt) extends Id(value = Some(id))
}

object Id {
  def apply(value: PositiveInt): Id = Numbered(value)
}
