package moviebuffs

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.util.Random

class OrderCreationSimulation extends Simulation {

  val httpConf = http
    .baseUrl("http://localhost:18080")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  // Now, we can write the scenario as a composition
  val scnCreateOrder = scenario("Create Order")
      .during(3.minutes, "Counter") {
        exec(Order.create).pause(2)
      }

  setUp(
    scnCreateOrder.inject(rampUsers(10) during  (2.seconds))
      //,setUp(scn.inject(atOnceUsers(10)).protocols(httpConf))
  ).protocols(httpConf)
    .assertions(
      global.responseTime.max.lt(800),
      global.successfulRequests.percent.gt(95)
    )

}

object Order {
  val searchFeeder = csv("data/feeders/search.csv").random
  val genreFeeder = csv("data/feeders/genres.csv").random
  val productFeeder = csv("data/feeders/products.csv").random

  val credentialsFeeder = csv("data/feeders/credentials.csv").random
  var randomString = Iterator.continually(Map("randstring" -> ( Random.alphanumeric.take(20).mkString )))
  var randomQuantity = Iterator.continually(Map("randomQuantity" -> ( Random.nextInt(4) + 1 )))

  var token = ""
  val login = feed(credentialsFeeder)
    .exec(http("Login")
      .post("/login")
      .formParamSeq(Seq(
        ("username", "${username}"),
        ("password", "${password}")
      ))
    )
    .pause(1)

  val createOrder = feed(randomQuantity)
      .feed(productFeeder)
      .feed(randomString)
      .exec(
        http("Add Item To Cart").post("/cart/items")
          .body(ElFileBody("data/feeders/item.json")).asJson
      )
      .exec(
        http("Create Order").post("/cart/checkout")
          .formParamSeq(Seq(
            ("customerName", "${randstring}"),
            ("customerEmail", "${randstring}@gmail.com"),
            ("deliveryAddress", "${randstring}"),
            ("creditCardNumber", "1111222233334444"),
            ("cvv", "123")
          ))
      )
      .pause(1)

  val create =
    exec(login)
    .pause(2)
    .exec(createOrder)

}
