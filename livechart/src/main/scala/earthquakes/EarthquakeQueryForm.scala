package earthquakes

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import org.scalajs.dom.experimental.Response
import io.circe.*
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.*

object EarthquakeQueryForm:
  private val earthquakesVar: Var[Seq[Earthquake]] = Var(Seq())
  private val earthquakeSignal: StrictSignal[Seq[Earthquake]] = earthquakesVar.signal 
  private val errorVar: Var[String] = Var("")
  private val random:Random = new Random()

  private val fromDate = input(
    defaultValue("2025-01-01"),
    maxLength(10), // HTML can help block some undesired input
    onInput
      .mapToValue
      .map(_.filter(c => Character.isDigit(c) || c == '-'))
      .setAsValue --> Observer.empty
  )

  private val toDate = input(
    defaultValue("2025-01-02"),
    maxLength(10), // HTML can help block some undesired input
    onInput
      .mapToValue
      .map(_.filter(c => Character.isDigit(c) || c == '-'))
      .setAsValue --> Observer.empty
  )

  private def queryForEarthquakes(from: String, to: String): EventStream[Unit] = {
    val url = if (random.nextBoolean())
      s"/earthquake-api?format=geojson&starttime=$from&endtime=$to&minmagnitude=5&orderby=magnitude&limit=5"
    else
      "https://httpstat.us/random/400-426,428-429,431,440,444,449-451,460,463,494-508,510-511,520-527,530,561"
    FetchStream.get(
      url = url,
      fetchOptions => fetchOptions.headers(("Accept", "application/json"))
    ).map {
      responseText => {
        parser.parse(responseText).flatMap(_.as[Seq[Earthquake]]) match
          case Left(error) =>
            throw new Exception(s"Response: [$responseText], Error: [$error]")
          case Right(earthquakes) =>
            earthquakesVar.update(_ => earthquakes)
            errorVar.update(_ => "")
      }        
    }.recover {
      case err: Throwable =>
        earthquakesVar.update(_ => Seq())
        errorVar.update(_ => err.toString())
        None
    }
  }

  private def hideIfNoItems: Mod[HtmlElement] =
    display <-- earthquakeSignal.map { items =>
      if (items.nonEmpty) "" else "none"
    }

  private def hideIfNoError: Mod[HtmlElement] =
    display <-- errorVar.signal.map { error =>
      if (error.nonEmpty) "" else "none"
    }

  private def renderEarthquake(place: String, initialEarthquake: Earthquake, earthquakeSignal: Signal[Earthquake]): HtmlElement = {
    tbody(
      child <-- earthquakeSignal.map {
        earthquake =>
          tr(
            td(border := style.px(1), borderStyle := "solid", earthquake.magnitude.toString()),
            td(border := style.px(1), borderStyle := "solid", earthquake.time.toString()),
            td(border := style.px(1), borderStyle := "solid", earthquake.place)
          )
      }
    )
  }

  val app = div(
    h1("Earthquakes!"),
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
      hideIfNoItems, // Hide table if no results!
      label(
        "Result:"
      ),
      p(
        table(
          border := style.px(5),
          borderStyle := "solid",
          thead(
            tr(
              th(border := style.px(3), borderStyle := "solid", "Magnitude"),
              th(border := style.px(3), borderStyle := "solid", "Time"),
              th(border := style.px(3), borderStyle := "solid", "Place")
            )
          ),
          children <-- earthquakeSignal.split(_.place)(renderEarthquake)
        )
      )
    ),
    p(
      hideIfNoError,
      label("Error: "),
      p(
        child.text <-- errorVar
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