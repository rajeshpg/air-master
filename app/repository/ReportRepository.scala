package repository

import javax.inject.{Inject, Singleton}

import database.MongoClient
import play.api.libs.json.{JsObject, JsString}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class ReportRepository @Inject()(mongoClient: MongoClient) {

  def findCountriesWithHighestNoOfAirports: Future[List[JsObject]] = findCountriesWithAirports("desc")

  def findCountriesWithLowestNoOfAirports: Future[List[JsObject]] = findCountriesWithAirports("asc")

  private def findCountriesWithAirports(sort: String): Future[List[JsObject]] = {

    mongoClient.getCollection("airports").flatMap {
      collection => {
        import collection.BatchCommands.AggregationFramework._
        collection.aggregate(
          Group(JsString("$iso_country"))("count" -> SumAll),
          List(Sort(if (sort == "asc") Ascending("count") else Descending("count")),
            Limit(10),
            Lookup("countries", "_id", "code", "country"),
            UnwindField("country"),
            Group(JsString("$_id"))("countryName" -> FirstField("country.name"), "noOfAirports" -> FirstField("count")),
            Sort(if (sort == "asc") Ascending("noOfAirports") else Descending("noOfAirports"))
          )
        ).map(_.head[JsObject])

      }

    }

  }

  def findSurfacesPerCountry: Future[List[JsObject]] = {
    mongoClient.getCollection("runways").flatMap {
      collection => {
        import collection.BatchCommands.AggregationFramework._
        collection.aggregate(
          Group(JsString("$surface"))("airportIdents" -> AddFieldToSet("airport_ident")),
          List(Lookup("airports", "airportIdents", "ident", "airport"),
            UnwindField("airport"),
            Group(JsString("$_id"))("countryCode" -> AddFieldToSet("airport.iso_country")),
            Lookup("countries", "countryCode", "code", "country"),
            UnwindField("country"),
            Group(JsString("$country.name"))("surface" -> AddFieldToSet("_id"))
          )
        ).map(_.head[JsObject])
      }
    }
  }

  def findCommonRunwayIdentifications: Future[List[JsObject]] = {
    mongoClient.getCollection("runways").flatMap {
      collection => {
        import collection.BatchCommands.AggregationFramework._
        collection.aggregate(
          Group(JsString("$le_ident"))("count" -> SumAll),
          List(Sort(Descending("count")), Limit(10))
        ).map(_.head[JsObject])
      }
    }
  }
}
