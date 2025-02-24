package earthquakes

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import org.scalajs.dom.experimental.Response
import io.circe.*
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.*

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

  // https://demo.laminar.dev/app/integrations/network-requests
  // val responses = clicks.flatMapSwitch { opt =>
            //    FetchStream.get(url = opt.url, _.abortStream(abortStream))
            //     .map(resp => if (resp.length >= 1000) resp.substring(0, 1000) else resp)
            //     .map("Response (first 1000 chars): " + _)
            //     .recover { case err: Throwable => Some(err.getMessage) }
            // }


  def queryForEarthquakes(from: String, to: String): EventStream[String] = {
    val url = s"https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=$from&endtime=$to&minmagnitude=5&orderby=magnitude&limit=5"
    FetchStream.get(url).map {
      responseText => {
        parser.parse(responseText).flatMap(_.as[Seq[Earthquake]]) match
          case Left(failure) =>
             s"${failure.toString} Response: >>>$responseText<<<"
          case Right(value) =>
            s"PARSED! => ${value.toString()}"
      }
    }
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