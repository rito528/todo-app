package com.example.todoapp

import cats.effect.{ IO, IOApp, ExitCode }
import com.comcast.ip4s._
import org.http4s.ember.server._
import org.http4s.client.middleware.Logger
import org.http4s.client.Client

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    val client = Client.fromHttpApp(TodoappRoutes.routes.orNotFound)

    val loggerClient = Logger[IO](
      logHeaders = true,
      logBody    = true,
      logAction  = Some((msg: String) => IO.println(msg))
    )(client)

    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"9000")
      .withHttpApp(loggerClient.toHttpApp)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }

}
