package com.example.todoapp.endpoints

import sttp.tapir.*
import cats.effect.IO
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.files.*

object InternalEndpoints {
  private object InternalEndpointServerLogics {
    def pingLogic: Unit => IO[Either[Unit, String]] = {
      _ => IO.pure(Right("ok"))
    }
  }

  private def pingEndpoint: PublicEndpoint[Unit, Unit, String, Any] = {
    endpoint.get.in("ping").out(stringBody)
  }

  val endpoints: List[ServerEndpoint[Any, IO]] = List(
    pingEndpoint.serverLogic(InternalEndpointServerLogics.pingLogic),
    staticFilesGetServerEndpoint("assets")("public/"),
  )
}
