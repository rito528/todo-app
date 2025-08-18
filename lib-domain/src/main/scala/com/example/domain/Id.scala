package com.example.domain

enum Id(val value: Option[Int]) {
  case NotNumbered extends Id(value = None)
  case Numbered(id: Int) extends Id(value = Some(id))
}

object Id {
  def apply(value: Int): Id = Numbered(value)
}
