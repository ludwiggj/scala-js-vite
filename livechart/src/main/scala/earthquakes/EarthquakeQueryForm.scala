package earthquakes

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import org.scalajs.dom.experimental.Response
import io.laminext.fetch.*
import io.laminext.fetch.circe.*
import io.laminext.syntax.core.*
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.*

object EarthquakeQueryForm:
  val queryResult: Var[String] = Var("")

  val (responsesStream, responseReceived) = EventStream.withCallback[FetchResponse[Seq[Earthquake]]]

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

  def earthquakeUrl(from: String, to: String): String = 
    s"https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=$from&endtime=$to&minmagnitude=5&orderby=magnitude&limit=5"
  
  val app = div(
    form(
      onSubmit
        .preventDefault
        .flatMap(_ => Fetch.get(earthquakeUrl(fromDate.ref.value, toDate.ref.value)).decode[Seq[Earthquake]]) --> responseReceived,
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
      child.text <-- responsesStream.recoverToTry.map {
        case Success(response) =>
          s"${response.status} ${response.data}"
        case Failure(exception) =>
          s"Error ${exception}"
      }
    )
  )

@main
// This method bootstraps Laminar by installing a Laminar Element in an existing DOM element:
def EarthquakeQueries(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    EarthquakeQueryForm.app
  )