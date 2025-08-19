package com.example.todoapp

import sttp.tapir.*
import sttp.tapir.CodecFormat.TextPlain
import com.example.domain.TodoId
import com.example.domain.NumberedTodoId
import com.example.domain.PositiveInt
import io.github.iltotore.iron.*
import com.example.domain.NumberedCategoryId
import com.example.domain.CategoryId

object Codecs {
  given numberedTodoIdCodec: Codec[String, NumberedTodoId, TextPlain] = Codec.int.mapDecode { id =>
    val todoId: Either[String, PositiveInt] = id.refineEither

    todoId match {
      case Right(id) => DecodeResult.Value(TodoId(id))
      case Left(err) => DecodeResult.Error(err, new Exception(s"Invalid TodoId: $id"))
    }
  }(_.unwrap)

  given numberedCategoryIdCodec: Codec[String, NumberedCategoryId, TextPlain] = Codec.int.mapDecode { id =>
    val categoryId: Either[String, PositiveInt] = id.refineEither

    categoryId match {
      case Right(id) => DecodeResult.Value(CategoryId(id))
      case Left(err) => DecodeResult.Error(err, new Exception(s"Invalid CategoryId: $id"))
    }
  }(_.unwrap)
}
