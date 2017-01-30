package controllers

import javax.inject.{Inject, Singleton}

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, Controller}
import repository.ReportRepository

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ReportsController @Inject()(reportRepository: ReportRepository) extends Controller {

  def getCountriesWithHigestAirports: Action[AnyContent] = Action.async { implicit request =>
    reportRepository.findCountriesWithHighestNoOfAirports.map { report => {
      Ok(views.html.reports.countries.airports("Countries with highest no of airports", Json.toJson(report)))
    }
    }
  }

  def getCountriesWithLowestAirports: Action[AnyContent] = Action.async { implicit request =>
    reportRepository.findCountriesWithLowestNoOfAirports.map { report =>
      Ok(views.html.reports.countries.airports("Countries with lowest no of airports", Json.toJson(report)))
    }
  }

  def getSurfacesPerCountry: Action[AnyContent] = Action.async { implicit request =>
    reportRepository.findSurfacesPerCountry.map {
      report => Ok(views.html.reports.countries.surfaces("Countries and runway surfaces", Json.toJson((report))))
    }
  }

  def getCommonRunwayIdentifications: Action[AnyContent] = Action.async { implicit request =>
    reportRepository.findCommonRunwayIdentifications.map {
      report => Ok(views.html.reports.commonIdents("Most common runway identifications", Json.toJson((report))))
    }

  }

}

