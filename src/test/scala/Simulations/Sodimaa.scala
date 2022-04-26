package Simulations

/**
 *
 * @author Vivekanand Dalavi
 */
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class Sodimac extends Simulation {

	private def getProperty(propertyName: String, defaultValue: String) = {
    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }
  def userCount: Int = getProperty("USERS", "10").toInt

  def rampDuration: Int = getProperty("RAMP_DURATION", "10").toInt

  def testDuration: Int = getProperty("DURATION", "900").toInt

  before {
    println(s"Running test with ${userCount} users")
    println(s"Ramping users over ${rampDuration} seconds")
    println(s"Total test duration: ${testDuration} seconds")
  }

   /**
   * Step1: Define a common http protocol config
   */

	val httpConf = http
		.baseUrl("https://www.sodimac.com.pe")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("*/*")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.9")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36")

	/**
   * Step 2: Define the variables for headers and feeders
   */
	val feeder = csv("data.csv").circular

	val headers_0 = Map(
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-origin",
		"sec-ch-ua" -> """ Not A;Brand";v="99", "Chromium";v="100", "Google Chrome";v="100""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows")

	val headers_2 = Map(
		"Content-Type" -> "application/json",
		"Origin" -> "https://www.sodimac.com.pe",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-origin",
		"sec-ch-ua" -> """ Not A;Brand";v="99", "Chromium";v="100", "Google Chrome";v="100""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows")

	val headers_3 = Map(
		"Content-Type" -> "application/json",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-origin",
		"sec-ch-ua" -> """ Not A;Brand";v="99", "Chromium";v="100", "Google Chrome";v="100""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows")

	val headers_4 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "same-origin",
		"Sec-Fetch-User" -> "?1",
		"Upgrade-Insecure-Requests" -> "1",
		"sec-ch-ua" -> """ Not A;Brand";v="99", "Chromium";v="100", "Google Chrome";v="100""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "Windows")

	/**
   * Step 3 : Define the scenarios
   */
   val scn1 = scenario("Sodimac Home page and open random product").forever{
	/**
     * This is the Scenario 1
     * Workflow includes going on sodimac website and select a random product
     * and checking the details of the random product
     */
		exec(http("01_Launch")
			.get("/rest/model/falabella/rest/browse/BrowseActor/get-location-info")
			.headers(headers_0)
			.check(jsonPath("$..value").findRandom.saveAs("c_StoreId"))
			.check(status.is(200)))

			.exec(http("02_Launch")
			.get("/rest/model/falabella/rest/common/CommonActor/analytics-info")
			.headers(headers_0)
			.check(status.is(200)))
		// .pause(1)

		.exec(http("03_Launch")
			.get("/s/search/v2/sope/product-details?store=${c_StoreId}&productId=2343185-3958620-3485560-1665278-4036743-2424665-1747940-8709718-2481804-1102184-1245570-2186934-")
			.headers(headers_3)
			.check(jsonPath("$..productId").findRandom.saveAs("c_productId"))
			.check(substring("""productDetailsJson""")))
		// .pause(99)
		.exec(http("04_Select Product")
			.get("/sodimac-pe/product/${c_productId}?cid=prdhom133255dy")
			.headers(headers_4))
		// .pause(3)
		.exec(http("05_Select Product")
			.get("/rest/model/falabella/rest/browse/BrowseActor/getPromotedProductInfo?productId=${c_productId}")
			.headers(headers_0)
			.check(jsonPath("$.success").is("true"))
			.resources(http("06_Select Product")
			.get("/s/products/v2/sope/related-products?productId=${c_productId}&zone=${c_StoreId}&priceGroup=2")
			.headers(headers_0),
            http("07_Select Product")
			.get("/rest/model/falabella/rest/common/CommonActor/analytics-info")
			.headers(headers_0)))
		// .pause(2)
		.exec(http("08_Select Product")
			.get("/s/search/v2/sope/product-details?store=${c_StoreId}&productId=2261316-3479919-2461765-1665294-2870525-372574X-2870509-2465027-2092166-267257X-2461803-3752720-2870517-1015478-1015419-2461811-2573326-101546X-2672588-1679821")
			.headers(headers_3)
			.resources(http("09_Select Product")
			.get("/s/search/v2/sope/product-details?store=${c_StoreId}&productId=3476227-2092166-1665286-2225123-2870525-2462044-3749231-1267396-3726339-2590859-2011328-13371X-439649-2022834-123403X-2678462-338087-254198X-2671719-338001")
			.headers(headers_3)))
			}

	 val scn2 = scenario("Sodimac Home page and Search Random String").forever{
	 /**
     * This is the Scenario 2
     * Workflow includes going on Sodimaa website and searching a string on the search bar
     */


    feed(feeder)
		.exec(http("01_Launch")
			.get("/rest/model/falabella/rest/browse/BrowseActor/get-location-info")
			.headers(headers_0)
			.check(jsonPath("$..value").findRandom.saveAs("c_StoreId"))
			.check(status.is(200)))

			.exec(http("02_Launch")
			.get("/rest/model/falabella/rest/common/CommonActor/analytics-info")
			.headers(headers_0)
			.check(status.is(200)))
		// .pause(1)

		.exec(http("03_Launch")
			.get("/s/search/v2/sope/product-details?store=${c_StoreId}&productId=2343185-3958620-3485560-1665278-4036743-2424665-1747940-8709718-2481804-1102184-1245570-2186934-")
			.headers(headers_3)
			.check(jsonPath("$..productId").findRandom.saveAs("c_productId")))

		.exec(http("04_Search Product")
			.get("/s/typeahead/v1/sope?term=ab&channel=web")
			.headers(headers_0)
			.resources(http("05_Search Product")
			.get("/s/typeahead/v1/sope?term=${searchterm}&channel=web")
			.headers(headers_0),
            http("06_Search Product")
			.get("/sodimac-pe/search/?Ntt=${searchterm}")
			.headers(headers_4)))
		// .pause(1)
		.exec(http("07_Search Product")
			.get("/rest/model/falabella/rest/browse/BrowseActor/fetchHeaderBanner?siteId=SPEStore&channel=web")
			.headers(headers_3)
			.resources(http("08_Search Product")
			.get("/rest/model/falabella/rest/browse/BrowseActor/fetchContentBanner?siteId=SPEStore&channel=web")
			.headers(headers_3)))
}
	/**
   * Step 4: Define Load injection strategy
   */

  setUp(
    scn1.inject(
      nothingFor(5 seconds),
      rampUsers(userCount) during (rampDuration second)
    ),
    scn2.inject(
      nothingFor(5 seconds),
      rampUsers(userCount) during (rampDuration second)
    )
  ).protocols(httpConf)
    .maxDuration(testDuration seconds)
}
