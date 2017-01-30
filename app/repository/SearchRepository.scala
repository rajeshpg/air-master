package repository

import javax.inject.{Inject, Singleton}

import database.MongoClient
import play.api.libs.json.{JsObject, JsString, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class SearchRepository @Inject()(countryRepository: CountryRepository, mongoClient: MongoClient) {

  def findAirportRunwaysByCountryCode(countryCode: String): Future[List[JsObject]] = {
    countryRepository.findByCodeOrName(countryCode).flatMap {
      country => {
        val cc = (country.head \ "code").as[String]
        mongoClient.getCollection("airports").flatMap {
          collection => {
            import collection.BatchCommands.AggregationFramework._
            collection.aggregate(
              Match(Json.obj("iso_country" -> Json.obj("$regex" ->s"""^$cc""", "$options" -> "i"))),
              List(Lookup("runways", "ident", "airport_ident", "ar"),
                UnwindField("ar"),
                Group(JsString("$id"))(
                  "name" -> FirstField("name"),
                  "type" -> FirstField("type"),
                  "ident" -> FirstField("ident"),
                  "latitude_deg" -> FirstField("latitude_deg"),
                  "elevation_ft" -> FirstField("elevation_ft"),
                  "runways" -> PushField("ar")
                ),
                Sort(Ascending("name")),
                Project(Json.obj("_id" -> 0,
                  "runways._id" -> 0,
                  "runways.id" -> 0,
                  "runways.airport_ref" -> 0,
                  "runways.airport_ident" -> 0)
                )
              )
            ).map(_.head[JsObject])
          }
        }
      }
    }

  }

}
