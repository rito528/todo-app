package com.example.todoapp

import org.http4s.dsl.io.*;
import org.http4s.implicits.*;
import cats.effect.IO
import org.http4s.HttpRoutes
import cats.data.Kleisli
import org.http4s.Response
import org.http4s.Request

object TodoappRoutes {
  def pingRoutes: Kleisli[IO, Request[cats.effect.IO], Response[cats.effect.IO]] = {
    HttpRoutes.of[IO] {
      case GET -> Root / "ping" =>
        Ok("ok")
    }.orNotFound
  }
}
