package repository

import database.MongoClient
import org.specs2.mutable.Specification
import play.api.libs.json.{JsObject, Json}
import utils.TestInjector

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class CountryIntegrationTest extends Specification with TestInjector {

  "Country reposostory " should {
    "fetch country details for given country code " in {
      val mongoClient = getInjector.instanceOf[MongoClient]

      val countryRepository = new Country(mongoClient)

      val countryDetails = countryRepository.findByCodeOrName("qa")


      val actualJson: List[JsObject] = Await.result(countryDetails, Duration("3 seconds"))

      actualJson must be equalTo List[JsObject](Json.obj("code" -> "QA", "name" -> "Qatar"))

    }
  }

}
