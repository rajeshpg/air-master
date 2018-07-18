package repository

import javax.inject.{Inject, Singleton}

import database.MongoClient
import play.api.libs.json.{JsObject, JsString, Json}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class SearchRepository @Inject()(countryRepository: Country, mongoClient: MongoClient) {

  def findAirportRunwaysByCountryCode(countryCode: String): Future[List[JsObject]] = {
    countryRepository.findByCodeOrName(countryCode).flatMap {
      country => {
        val cc = (country.head \ "code").as[String]
        findAirportsByCountryCode(cc)
      }
    }

  }

  private def findAirportsByCountryCode(countryCode: String) = {
    mongoClient.getCollection("airports").flatMap {
      collection => {
        aggregateQuery(countryCode, collection)
      }
    }
  }

  private def aggregateQuery(countryCode: String, countryCollection: JSONCollection) = {
    import countryCollection.BatchCommands.AggregationFramework._

    val $match = Match(Json.obj("iso_country" -> Json.obj("$regex" ->s"""^$countryCode""", "$options" -> "i")))
    val $lookup = Lookup("runways", "ident", "airport_ident", "ar")
    val $group = Group(JsString("$id"))(
      "name" -> FirstField("name"),
      "type" -> FirstField("type"),
      "ident" -> FirstField("ident"),
      "latitude_deg" -> FirstField("latitude_deg"),
      "elevation_ft" -> FirstField("elevation_ft"),
      "runways" -> PushField("ar")
    )
    val $project = Project(Json.obj("_id" -> 0,
      "runways._id" -> 0,
      "runways.id" -> 0,
      "runways.airport_ref" -> 0,
      "runways.airport_ident" -> 0)
    )

    countryCollection.aggregate(
      $match,
      List($lookup,
        UnwindField("ar"),
        $group,
        Sort(Ascending("name")),
        $project
      )
    ).map(_.head[JsObject])
  }
}
