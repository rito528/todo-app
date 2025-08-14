package com.example.todoapp.endpoints

import cats.effect.IO
import sttp.tapir.*
import scala.io.Source
import sttp.tapir.server.ServerEndpoint

object SpaEndpoints {
  private object SpaServerLogics {
    def angularAppLogic: List[String] => IO[Either[Unit, String]] = _ =>
      IO {
        val html = Source.fromFile("public/ngx/browser/index.html").mkString
        Right(html)
      }
  }

  private def angularAppEndpoint: PublicEndpoint[List[String], Unit, String, Any] = {
    endpoint.get.in(paths).out(htmlBodyUtf8)
  }

  val endpoints: List[ServerEndpoint[Any, IO]] = List(
    angularAppEndpoint.serverLogic(SpaServerLogics.angularAppLogic)
  )
}
