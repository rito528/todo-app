package com.example

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.numeric.Greater

package object domain {
  type PositiveInt = Int :| Greater[0]
}
