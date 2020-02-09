package moviebuffs

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class MovieBuffsAPISimulation extends Simulation {

  val httpConf = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")


  val getAllProducts = exec(http("AllProducts").get("/api/products")).pause(1)
  val getAllOrders = exec(http("AllOrders").get("/api/orders")).pause(1)


  // Now, we can write the scenario as a composition
  val scnGetAllProducts = scenario("Get All Products").exec(getAllProducts).pause(2)
  val scnGetAllOrders = scenario("Get All Orders").exec(getAllOrders).pause(2)

  //setUp(scn.inject(atOnceUsers(10)).protocols(httpConf))

  setUp(
      scnGetAllProducts.inject(rampUsers(500) during  (10 seconds)),
      scnGetAllOrders.inject(rampUsers(500) during  (10 seconds)),
  ).protocols(httpConf)

}
