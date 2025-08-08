package com.example.todoapp

import org.http4s.dsl.io.*;
import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.Response
import org.http4s.Request

object TodoappRoutes {
  def routes: HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case GET -> Root / "ping" =>
        Ok("ok")
    }
  }
}
