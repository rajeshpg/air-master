package controllers

import org.jsoup.Jsoup
import org.specs2.mock.Mockito
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc.Result
import play.api.test.{FakeRequest, PlaySpecification}
import repository.SearchRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SearchControllerTest extends PlaySpecification with Mockito {

  "Controller " should {
    "build report for airports and its runways for a given country code" in {

      val mockSearchRepository = mock[SearchRepository]
      val json: JsValue = Json.arr(Json.obj("name" -> "Frankfurt International Airport", "type" -> "medium",
        "runways" -> Json.arr(Json.obj("lighted" -> 0, "surface" -> "ASP"))))

      mockSearchRepository.findAirportRunwaysByCountryCode("qa") returns Future(json.as[List[JsObject]])

      val expectedReportTableHeader: String = Jsoup.parse(views.html.airports_runways("Airports and Runways", json).body).select("#airports tbody tr td").get(0).`val`()

      val controller = new SearchController(mockSearchRepository)

      val response: Future[Result] = controller.searchByCountry("qa")(FakeRequest())

      val actualReportTableHeader: String = Jsoup.parse(contentAsString(response)).select("#airports tbody tr td").get(0).`val`()

      actualReportTableHeader must be equalTo expectedReportTableHeader

    }
  }

}
