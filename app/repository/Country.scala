package repository

import javax.inject.{Inject, Singleton}

import database.MongoClient
import play.api.libs.json.{JsObject, Json}
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.play.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class Country @Inject()(mongoClient: MongoClient) {

  def findByCodeOrName(searchText: String): Future[List[JsObject]] = {
    val regex = Json.obj("$regex" -> s"""^$searchText""", "$options" -> "i")
    val query = Json.obj("$or" -> Json.arr(
      Json.obj("code" -> regex),
      Json.obj("name" -> regex)
    ))
    mongoClient.getCollection("countries").flatMap {
      countries =>
        countries.find(query, Json.obj("name"-> 1, "code"->1, "_id" -> 0))
          .cursor[JsObject](ReadPreference.primary).collect[List](-1, Cursor.FailOnError[List[JsObject]]())
    }
  }

}
