package repository

import database.MongoClient
import org.specs2.mutable.Specification
import play.api.libs.json.JsObject
import utils.TestInjector

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ReportRepositoryIntegrationTest extends Specification with TestInjector {

  "Report repository " should {
    val mongoClient = getInjector.instanceOf[MongoClient]

    val reportRepository = new ReportRepository(mongoClient)

    "fetch countries with highest number of aiports" in {
      val actualJson = Await.result(reportRepository.findCountriesWithHighestNoOfAirports, Duration("3 seconds"))

      val firstElement: JsObject = actualJson.head

      (firstElement \ "countryName").as[String] must be equalTo "United States"
      (firstElement \ "noOfAirports").as[Int] must be equalTo 21501
    }

    "fetch countries with lowest number of airports" in {
      val actualJson = Await.result(reportRepository.findCountriesWithLowestNoOfAirports, Duration("3 seconds"))

      val firstElement: JsObject = actualJson.head

      (firstElement \ "countryName").as[String] must be equalTo "Aruba"
      (firstElement \ "noOfAirports").as[Int] must be equalTo 1

    }

    "fetch countries and its airport's runways surfaces" in {
      val actualJson = Await.result(reportRepository.findSurfacesPerCountry, Duration("3 seconds"))

      val firstElement: JsObject = actualJson.head


      (firstElement \ "_id").as[String] must be equalTo "French Southern Territories"
      (firstElement \ "surface").as[List[String]] must be equalTo List[String]("dirt")
    }

    "fetch most common identification in runways" in {
      val actualJson = Await.result(reportRepository.findCommonRunwayIdentifications, Duration("3 seconds"))

      val firstElement: JsObject = actualJson.head


      (firstElement \ "_id").as[String] must be equalTo "H1"
      (firstElement \ "count").as[Int] must be equalTo 5566
    }
  }

}
