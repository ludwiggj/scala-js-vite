package earthquakes

import com.raquo.laminar.api.L.*

import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.{Element, document}
import org.scalajs.dom.ext.*

import java.time.Instant

// Testing notes:

// See https://github.com/raquo/scala-dom-testutils
// Google searches:
// "testing scala laminar with jsdom"
// "scala.js tests"
// https://www.scala-js.org/doc/project/testing.html
// https: //www.scalatest.org/user_guide/using_scalajs
// https://javascript.info/modules-intro
// http://web.simmons.edu/~grabiner/comm244/weeknine/including-javascript.html
// Playwright - browser testing
//   https://github.com/microsoft/playwright
//   https://playwright.dev
//   https://github.com/gmkumar2005/scala-js-env-playwright
//   https://github.com/gmkumar2005/scalajs-sbt-vite-laminar-chartjs-example/blob/main/src/test/scala/SimpleTestSuite.scala
// Selenium - browser testing
//   https://github.com/scala-js/scala-js-env-selenium
// Laminar internal tests
//   https://github.com/raquo/Laminar/blob/master/src/test/scala/com/raquo/laminar/tests/SplitVarSpec.scala  
//   https://github.com/raquo/scala-dom-testutils/blob/master/src/main/scala/com/raquo/domtestutils/EventSimulator.scala
// This seems like the most promising laminar testing example (but still seems like a lot of work):
//   https://github.com/keynmol/http4s-laminar-stack
//   https://github.com/keynmol/http4s-laminar-stack/blob/master/modules/frontend/src/test/scala/FrontendSpec.scala  
class EarthquakeQueryFormTest extends munit.FunSuite:
  def displayElement(element: Element, depth: Int): Unit = {
    val buffer = "".padTo(depth, ' ')
    if (depth == 10)
      println(s"${buffer}STOP")
    else
      val childCount = element.children.length
      println(s"${buffer}${element.nodeName}($childCount)")
      element.children.foreach(e => displayElement(e, depth + 1))
    }
  
  test("Can get earthquake data") {
    val containerElement = document.createElement("div")
    document.body.appendChild(containerElement)

    render(
      container = containerElement,
      rootNode = EarthquakeQueryForm.app
    )

    EarthquakeQueryForm.formObserver.onNext(
      Right(
        Seq(
          Earthquake(magnitude = 9, place = "Ipswich, UK", time = 1741379415000),
          Earthquake(magnitude = 8.7, place = "Norwich, UK", time = 1640433657000)
        )
      )
    )

    displayElement(document.body, 1)

    val tableBody = document.querySelector("tbody")
    assert(tableBody.children(0).children(0).textContent == "9")
    assert(tableBody.children(0).children(1).textContent == "07/03/2025 20:30:15")
    assert(tableBody.children(0).children(2).textContent == "Ipswich, UK")
    assert(tableBody.children(1).children(0).textContent == "8.7")
    assert(tableBody.children(1).children(1).textContent == "25/12/2021 12:00:57")
    assert(tableBody.children(1).children(2).textContent == "Norwich, UK")
  }