package earthquakes

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import org.scalajs.dom.experimental.Response
import io.circe.*
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.*
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime

object EarthquakeQueryForm:
  private val earthquakesVar: Var[Seq[Earthquake]] = Var(Seq())
  private val earthquakeSignal: StrictSignal[Seq[Earthquake]] = earthquakesVar.signal 
  private val errorVar: Var[String] = Var("")
  private val random:Random = new Random()

  private val fromDate = input(
    fontFamily := "courier",
    defaultValue("2025-01-01"),
    maxLength(10), // HTML can help block some undesired input
    onInput
      .mapToValue
      .map(_.filter(c => Character.isDigit(c) || c == '-'))
      .setAsValue --> Observer.empty
  )

  private val toDate = input(
    fontFamily := "courier",
    defaultValue("2025-01-02"),
    maxLength(10), // HTML can help block some undesired input
    onInput
      .mapToValue
      .map(_.filter(c => Character.isDigit(c) || c == '-'))
      .setAsValue --> Observer.empty
  )

  private val limit = select(
    fontFamily := "courier",
    option(value := "5", 5, selected := true),
    option(value := "10", 10),
    option(value := "20", 20),
  )

  private val minMagnitude = select(
    fontFamily := "courier",
    option(value := "1", 1),
    option(value := "3", 3),
    option(value := "5", 5, selected := true),
    option(value := "7", 7),
    option(value := "9", 9)
  )

  private def hideIfNoItems: Mod[HtmlElement] =
    display <-- earthquakeSignal.map { items =>
      if (items.nonEmpty) "" else "none"
    }

  private def hideIfNoError: Mod[HtmlElement] =
    display <-- errorVar.signal.map { error =>
      if (error.nonEmpty) "" else "none"
    }

  private def epochTimeToLocalDateTime(epochTimeMillis: Long): String = {
    val instant = Instant.ofEpochMilli(epochTimeMillis)
    val utcZone = ZoneOffset.UTC
    val localDateTime = instant.atZone(utcZone).toLocalDateTime()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    localDateTime.format(formatter)    
  }

  private def renderEarthquake(
    place: String,
    initialEarthquake: Earthquake,
    earthquakeSignal: Signal[Earthquake]
  ): HtmlElement = {
    tbody(
      child <-- earthquakeSignal.map {
        earthquake =>
          tr(
            td(border := style.px(1), borderStyle := "solid", fontFamily := "courier", earthquake.magnitude.toString()),
            td(border := style.px(1), borderStyle := "solid", fontFamily := "courier", epochTimeToLocalDateTime(earthquake.time)),
            td(border := style.px(1), borderStyle := "solid", fontFamily := "courier", earthquake.place)
          )
      }
    )
  }

  def queryForEarthquakes(
    startTime: String,
    endTime: String,
    minMagnitude: String,
    limit: String
  ): EventStream[Either[String, Seq[Earthquake]]] = {
    val queryForEarthquakes = random.nextBoolean()
    val url = if (queryForEarthquakes)
      s"/earthquake-api?format=geojson&starttime=$startTime&endtime=$endTime&minmagnitude=$minMagnitude&orderby=magnitude&limit=$limit"
    else
      "https://httpstat.us/random/400-426,428-429,431,440,444,449-451,460,463,494-508,510-511,520-527,530,561"
        
    FetchStream.get(
      url = url,
      fetchOptions => fetchOptions.headers(("Accept", "application/json"))
    ).map {
      responseText => {
        if (queryForEarthquakes)
          parser
            .parse(responseText)
            .flatMap(_.as[Seq[Earthquake]])
            .left.map(error => s"Response: [$responseText], Error: [${error.toString()}]")
        else
          Left(responseText)
      }        
    }
    .recover {
      case err: Throwable =>
        Some(Left(err.toString()))
    }
  }

  def app(
    queryForEarthquakes: (String, String, String, String) => EventStream[Either[String, Seq[Earthquake]]]
  ) = div(
    h1("Earthquakes!"),
    form(
      onSubmit
        .preventDefault
        .flatMap(
          _ => queryForEarthquakes(fromDate.ref.value, toDate.ref.value, minMagnitude.ref.value, limit.ref.value)
        ) --> Observer[Either[String, Seq[Earthquake]]] {
            case Right(earthquakes) =>
              Var.set(earthquakesVar -> earthquakes, errorVar -> "")
            case Left(error) =>
              Var.set(earthquakesVar -> Seq.empty, errorVar -> error)
        },   
      p(
        fromDate,
        label("<-- From", fontFamily := "courier")
      ),
      p(
        toDate,
        label("<-- To", fontFamily := "courier")
      ),
      p(
        label("Minimum Magnitude:  ", fontFamily := "courier"),
        minMagnitude
      ),
      p(
        label("Max No of Results:  ", fontFamily := "courier"),
        limit
      ),
      p(
        button(typ("submit"), "Submit", fontFamily := "courier")
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
              th(border := style.px(3), borderStyle := "solid", fontFamily := "courier", "Magnitude"),
              th(border := style.px(3), borderStyle := "solid", fontFamily := "courier", "Time"),
              th(border := style.px(3), borderStyle := "solid", fontFamily := "courier", "Place")
            )
          ),
          children <-- earthquakeSignal.split(_.place)(renderEarthquake)
        )
      )
    ),
    p(
      hideIfNoError,
      label(fontFamily := "courier", "Error: "),
      p(
        fontFamily := "courier",
        child.text <-- errorVar
      )
    )
  )

@main
// This method bootstraps Laminar by installing a Laminar Element in an existing DOM element:
def EarthquakeQueries(): Unit =
  def queryForEarthquakesFake(
    startTime: String,
    endTime: String,
    minMagnitude: String,
    limit: String
  ): EventStream[Either[String, Seq[Earthquake]]] =
    EventStream.fromValue(
      Right(Seq(Earthquake(
        magnitude = 9, place = "Ipswich, UK", time = Instant.now().toEpochMilli()
      )))
    )

  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    //EarthquakeQueryForm.app(queryForEarthquakesFake)
    EarthquakeQueryForm.app(EarthquakeQueryForm.queryForEarthquakes)
  )