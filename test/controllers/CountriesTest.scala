package controllers

import org.specs2.execute.Results
import org.specs2.mock.Mockito
import play.api.libs.json.{JsObject, JsString, Json}
import play.api.test.{FakeRequest, PlaySpecification}
import repository.Country

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class CountriesTest extends PlaySpecification with Results with Mockito {

  "Controller" should {
    "should fetch country by name or code" in {
      val mockCountriesRepository = mock[Country]
      val mockJsObject = JsObject(Seq("code" -> JsString("QA"), "name" -> JsString("Qatar")))
      mockCountriesRepository.findByCodeOrName("qa") returns Future(List(mockJsObject))
      val countriesController = new Countries(mockCountriesRepository)
      val result = countriesController.getCountries("qa")(FakeRequest())

      contentAsJson(result) must be equalTo Json.parse("""[{"code":"QA","name":"Qatar"}]""")

    }
  }

}
