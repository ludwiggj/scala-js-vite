# Testing Notes

* See https://github.com/raquo/scala-dom-testutils
* Google searches: "testing scala laminar with jsdom", "scala.js tests"
* https://www.scala-js.org/doc/project/testing.html
* https://www.scalatest.org/user_guide/using_scalajs
* https://javascript.info/modules-intro
* http://web.simmons.edu/~grabiner/comm244/weeknine/including-javascript.html
* Playwright - browser testing
  * https://github.com/microsoft/playwright
  * https://playwright.dev
  * https://github.com/gmkumar2005/scala-js-env-playwright
  * https://github.com/gmkumar2005/scalajs-sbt-vite-laminar-chartjs-example/blob/main/src/test/scala/SimpleTestSuite.scala
* Selenium - browser testing
  https://github.com/scala-js/scala-js-env-selenium
* Laminar internal tests
  * https://github.com/raquo/Laminar/blob/master/src/test/scala/com/raquo/laminar/tests/SplitVarSpec.scala  
  * https://github.com/raquo/scala-dom-testutils/blob/master/src/main/scala/com/raquo/domtestutils/EventSimulator.scala
* This seems like the most promising laminar testing example (but still seems like a lot of work):
  * https://github.com/keynmol/http4s-laminar-stack
  * https://github.com/keynmol/http4s-laminar-stack/blob/master/modules/frontend/src/test/scala/FrontendSpec.scala

# Earthquakes

Example query:
https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2025-01-01&endtime=2025-01-02&minmagnitude=5&orderby=magnitude&limit=5

Docs:
https://earthquake.usgs.gov/fdsnws/event/1/#parameters

# To Do
// import scala.scalajs.js
// js.Dynamic.global.console.log("Yo")