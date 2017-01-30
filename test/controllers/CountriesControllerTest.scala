package controllers

import org.specs2.execute.Results
import org.specs2.mock.Mockito
import play.api.libs.json.{JsObject, JsString, Json}
import play.api.test.{FakeRequest, PlaySpecification}
import repository.CountryRepository

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class CountriesControllerTest extends PlaySpecification with Results with Mockito {

  "Controller" should {
    "should fetch country name and code" in {
      val mockCountriesRepository = mock[CountryRepository]
      val mockJsObject = JsObject(Seq("code" -> JsString("QA"), "name" -> JsString("Qatar")))
      mockCountriesRepository.findByCodeOrName("qa") returns Future(List(mockJsObject))
      val countriesController = new CountriesController(mockCountriesRepository)
      val result = countriesController.getCountries("qa")(FakeRequest())

      contentAsJson(result) must be equalTo Json.parse("""[{"code":"QA","name":"Qatar"}]""")

    }
  }

}
