package earthquakes

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import org.scalajs.dom.experimental.Response
import io.circe.*
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.*
// import scala.scalajs.js

object EarthquakeQueryForm:
  val queryResult: Var[String] = Var("")
  val queryError: Var[String] = Var("")
  val random:Random = new Random()

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

  def queryForEarthquakes(from: String, to: String): EventStream[Unit] = {
    val url = if (random.nextBoolean())
      s"https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=$from&endtime=$to&minmagnitude=5&orderby=magnitude&limit=5"
    else
      "https://httpstat.us/random/400-426,428-429,431,440,444,449-451,460,463,494-508,510-511,520-527,530,561"
    FetchStream.get(
      url = url,
      fetchOptions => fetchOptions.headers(("Accept", "application/json"))
    ).map {
      responseText => {
        // js.Dynamic.global.console.log("Yo")
        // js.Dynamic.global.console.log(responseText)
        parser.parse(responseText).flatMap(_.as[Seq[Earthquake]]) match
          case Left(error) =>
            throw new Exception(s"Response: [$responseText], Error: [$error]")
          case Right(earthquakes) =>
            queryResult.update(_ => earthquakes.toString())
            queryError.update(_ => "")
      }        
    }.recover {
      case err: Throwable =>
        queryResult.update(_ => "")
        queryError.update(_ => err.toString())
        None
    }
  }

  val app = div(
    form(
      onSubmit
        .preventDefault
        .flatMap(_ => queryForEarthquakes(fromDate.ref.value, toDate.ref.value)) --> Observer.empty,
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
      label("Result:"),
      p(
        child.text <-- queryResult
      )
    ),
    p(
      label("Error: "),
      p(
        child.text <-- queryError
      )
    )
  )

@main
// This method bootstraps Laminar by installing a Laminar Element in an existing DOM element:
def EarthquakeQueries(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    EarthquakeQueryForm.app
  )