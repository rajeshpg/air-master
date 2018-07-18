package repository

import database.MongoClient
import org.specs2.mutable.Specification
import play.api.libs.json.{JsObject, Json}
import utils.TestInjector

import scala.concurrent.Await
import scala.concurrent.duration.Duration


class SearchRepositoryIntegrationTest extends Specification with TestInjector{

  "Search repository " should {
    "fetch airpors and its runway details " in {
      val countryRepository = getInjector.instanceOf[Country]
      val mongoClient = getInjector.instanceOf[MongoClient]

      val searchRepository = new SearchRepository(countryRepository, mongoClient)

      val actualJson: Seq[JsObject] = Await.result(searchRepository.findAirportRunwaysByCountryCode("aw"), Duration("3 seconds"))


      val airport = actualJson.head
      val runway = (airport \ "runways").as[List[JsObject]].head

      (airport \ "name").as[String] must be equalTo "Queen Beatrix International Airport"
      (airport \ "type").as[String] must be equalTo "medium_airport"
      (runway \ "surface").as[String] must be equalTo "ASP"
      (runway \ "lighted").as[Int] must be equalTo 1

    }
  }

}
