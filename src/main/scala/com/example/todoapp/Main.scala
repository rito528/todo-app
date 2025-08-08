package com.example.todoapp

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple:
  val run = TodoappServer.run[IO]
