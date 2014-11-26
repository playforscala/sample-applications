Play 2.2 Changes
================

This document contains some notes about the differences between Play 2.2 and Play 2.1, which is used in the book. For the full details, see the [Play 2.2 migration notes](https://www.playframework.com/documentation/2.2.0/Migration22).

Build file
==========

Play 2.1 used `project/Build.scala` as a build file. Play 2.2 is powered by SBT 0.13 and uses a `build.sbt` file in the project root by default. The syntax of this file is slightly different from `Build.scala`. A basic `build.sbt` looks like this:

    name := "my-app"

    version := "1.0-SNAPSHOT"

    libraryDependencies ++= Seq(
      jdbc,
      anorm,
      cache
    )

    play.Project.playScalaSettings



Async actions
=============

In Play 2.1, asynchronous actions looked like this:

    def asyncAction = Action {
      Async {
        Future(someExpensiveComputation)
      }
    }
    
In Play 2.2, there's a new method on the `Action` object called `async` which is to be used if you want to return a `Result` asynchronously. Separate wrapping with `Async` is no longer needed:

    def asyncAction = Action.async {
      Future(someExpensiveComputation)
    }

Migrating this is a bit trickier than this example when you have multiple branche that are not all asynchronous, like this Play 2.1 example:

    def asyncAction(age: Int) = Action {
      if (age < 18) {
        BadRequest("You need to be an adult!")
      } else {
        Async {
          Future(someExpensiveComputation)
        }
      }
    }
    
If you change the `Action` to `Action.async` and you remove the `Async` from the body, it's not good yet, because the first branch of the `if` expression returns a `Result` and not a `Future[Result]`. To fix that, wrap the other branch in a `Future` as well:

    def asyncAction(age: Int) = Action.async {
      if (age < 18) {
        Future.successful(BadRequest("You need to be an adult!"))
      } else {
        Future(someExpensiveComputation)
      }
    }

