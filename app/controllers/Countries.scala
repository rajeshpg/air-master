package controllers

import javax.inject.{Inject, Singleton}

import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}
import repository.Country

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class Countries @Inject()(country: Country) extends Controller {

  def getCountries(q: String): Action[AnyContent] = Action.async {

    country.findByCodeOrName(q).map(countries => Ok(Json.toJson(countries)))

  }

}
