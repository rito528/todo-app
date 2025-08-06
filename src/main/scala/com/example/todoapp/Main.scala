package com.example.todoapp

import cats.effect.{ IO, IOApp, ExitCode }
import com.comcast.ip4s._
import org.http4s.ember.server._

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {

    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"9000")
      .withHttpApp(TodoappRoutes.pingRoutes)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }

}
