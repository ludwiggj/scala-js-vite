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

  def queryForEarthquakes(from: String, to: String): EventStream[Seq[Earthquake]] = {
    val url = s"https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=$from&endtime=$to&minmagnitude=5&orderby=magnitude&limit=5"
    FetchStream.get(url).map {
      responseText => {
        // js.Dynamic.global.console.log("Yo")
        // js.Dynamic.global.console.log(responseText)
        parser.parse(responseText).flatMap(_.as[Seq[Earthquake]]) match
          case Left(error) =>
            throw new Exception(s"Response: [$responseText], Error: [$error]")
          case Right(value) => value
      }        
    }.recover {
      case err: Throwable =>
        queryError.update(_ => err.toString())
        None
    }
  }

  val app = div(
    form(
      onSubmit
        .preventDefault
        .flatMap(_ => queryForEarthquakes(fromDate.ref.value, toDate.ref.value).map(_.toString)) --> queryResult.writer,
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