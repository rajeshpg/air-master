package utils

import play.api.Application
import play.api.inject.Injector
import play.api.inject.guice.GuiceApplicationBuilder

import scala.concurrent.Future

trait TestInjector {
  val applicationBuilder: Application = new GuiceApplicationBuilder().build()

  def getInjector: Injector = {
    applicationBuilder.injector
  }

  def stopApplication: Future[_] = {
    applicationBuilder.stop()
  }
}
