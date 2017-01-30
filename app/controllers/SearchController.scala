package controllers

import javax.inject.{Inject, Singleton}

import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}
import repository.SearchRepository

import scala.concurrent.ExecutionContext.Implicits.global


@Singleton
class SearchController @Inject()(searchRepository: SearchRepository) extends Controller {

  def searchByCountry(countryCode: String): Action[AnyContent] = Action.async {
    searchRepository.findAirportRunwaysByCountryCode(countryCode).map {
      rep => {
        Ok(views.html.airports_runways("Airports and Runways", Json.toJson(rep)))
      }
    }
  }

}
