package moviebuffs

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.util.Random

class OrderCreationSimulation extends Simulation {

  val httpConf = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  // Now, we can write the scenario as a composition
  val scnCreateOrder = scenario("Create Order").exec(Order.create).pause(2)

  setUp(
    scnCreateOrder.inject(rampUsers(500) during  (10 seconds))
      //,setUp(scn.inject(atOnceUsers(10)).protocols(httpConf))
  ).protocols(httpConf)
    .assertions(
      global.responseTime.max.lt(800),
      global.successfulRequests.percent.gt(95)
    )

}

object Order {
  val searchFeeder = csv("data/search.csv").random
  val genreFeeder = csv("data/genres.csv").random
  val productFeeder = csv("data/products.csv").random

  val credentialsFeeder = csv("data/credentials.csv").random
  var randomString = Iterator.continually(Map("randstring" -> ( Random.alphanumeric.take(20).mkString )))
  var randomQuantity = Iterator.continually(Map("randomQuantity" -> ( Random.nextInt(4) + 1 )))

  var token = ""
  val login = feed(credentialsFeeder)
    .exec(http("Login")
      .post("/api/auth/login")
      .body(StringBody(
        """
                    {
                      "username":"${username}",
                      "password":"${password}"
                    }
                  """)).asJson
      .check(status.is(200),jsonPath("$..access_token").saveAs("token"))
    )
    .exec(session => {
      token = session("token").as[String].trim
      session
    })
    .pause(1)

  val createOrder = feed(randomQuantity)
      .feed(productFeeder)
      .feed(randomString)
      .exec(
        http("Create Order").post("/api/orders")
          .header("Authorization", "Bearer ${token}")
          .body(ElFileBody("data/order.json")).asJson
      )
      .pause(1)

  val create =
    exec(login)
    .pause(2)
    .exec(createOrder)

}
