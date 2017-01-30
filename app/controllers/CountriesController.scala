package controllers

import javax.inject.{Inject, Singleton}

import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}
import repository.CountryRepository

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class CountriesController @Inject()(countryRepository: CountryRepository) extends Controller {

  def getCountries(searchText: String): Action[AnyContent] = Action.async {

    countryRepository.findByCodeOrName(searchText).map(countries => Ok(Json.toJson(countries)))

  }

}
