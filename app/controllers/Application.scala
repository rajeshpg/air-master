package controllers

import javax.inject._

import play.api.mvc.{Action, Controller}

@Singleton
class Application extends Controller{

  def index = Action{ implicit request =>
    Ok(views.html.index())
  }
}
