package earthquakes

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

object EarthquakeQueryForm:
  val queryResult: Var[String] = Var("")

  val fromDate = input(
    defaultValue("2025-01-01"),
    maxLength(10), // HTML can help block some undesired input
    onInput
      .mapToValue
      .map(_.filter(c => Character.isDigit(c) || c == '-'))
      .setAsValue --> Observer.empty
  )

  val toDate = input(
    defaultValue("2025-01-02"),
    maxLength(10), // HTML can help block some undesired input
    onInput
      .mapToValue
      .map(_.filter(c => Character.isDigit(c) || c == '-'))
      .setAsValue --> Observer.empty
  )

  def queryForEarthquakes(from: String, to: String): EventStream[String] = {
    val url = s"https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=$from&endtime=$to&minmagnitude=5&orderby=magnitude&limit=5"
    FetchStream.get(url)
  }

  val app = div(
    form(
      onSubmit
        .preventDefault
        .flatMap(_ => queryForEarthquakes(fromDate.ref.value, toDate.ref.value)) --> queryResult.writer,
      p(
        fromDate,
        label("<-- From")
      ),
      p(
        toDate,
        label("<-- To")
      ),
      p(
        button(typ("submit"), "Submit")
      )
    ),
    p(
      "Result: ",
      child.text <-- queryResult
    )
  )

@main
// This method bootstraps Laminar by installing a Laminar Element in an existing DOM element:
def EarthquakeQueries(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    EarthquakeQueryForm.app
  )