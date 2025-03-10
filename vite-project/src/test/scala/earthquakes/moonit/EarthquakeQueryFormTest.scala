package earthquakes.moonit

import com.raquo.domtestutils.*
import com.raquo.laminar.api.L.*
import earthquakes.EarthquakeQueryForm
import earthquakes.Utils.displayElement
import earthquakes.model.Earthquake
import org.scalajs.dom.document

// TODO - Could also copy in following test from Laminar to show things working:
//          com.raquo.laminar.tests.SplitVarSpec
// TODO - do this as a PR?
//        Add test for a Left response

class EarthquakeQueryFormTest extends MUnitSpec:
  private def simulateEarthquakes(form: EarthquakeQueryForm, earthquakes: Seq[Earthquake]): Unit =
    form.model.setResponse(Right(earthquakes))

  test("Can display one earthquake") {
    displayElement(document.body)

    val containerElement = document.querySelector("div")
    val earthquakeQueryForm = EarthquakeQueryForm()

    render(
      container = containerElement,
      rootNode = earthquakeQueryForm.app
    )

    val tableBody = document.querySelector("tbody")

    expectNode(
      tableBody,
      tbody of sentinel
    )

    simulateEarthquakes(
      earthquakeQueryForm,
      Seq(Earthquake(magnitude = 9, place = "Ipswich, UK", time = 1741379415000L))
    )

    displayElement(document.body)

    expectNode(
      tableBody,
      tbody of(
        sentinel,
        tr of(
          sentinel,
          td of "9",
          td of "07/03/2025 20:30:15",
          td of "Ipswich, UK"
        )
      )
    )
  }

  test("Can display two earthquakes") {
    displayElement(document.body)

    val containerElement = document.querySelector("div")

    val earthquakeQueryForm = EarthquakeQueryForm()

    render(
      container = containerElement,
      rootNode = earthquakeQueryForm.app
    )

    val tableBody = document.querySelector("tbody")

    expectNode(
      tableBody,
      tbody of sentinel
    )

    simulateEarthquakes(
      earthquakeQueryForm,
      Seq(
        Earthquake(magnitude = 9, place = "Ipswich, UK", time = 1741379415000L),
        Earthquake(magnitude = 8.7, place = "Norwich, UK", time = 1640433657000L)
      )
    )

    displayElement(document.body)

    expectNode(
      tableBody,
      tbody of(
        sentinel,
        tr of(
          sentinel,
          td of "9",
          td of "07/03/2025 20:30:15",
          td of "Ipswich, UK"
        ),
        tr of(
          sentinel,
          td of "8.7",
          td of "25/12/2021 12:00:57",
          td of "Norwich, UK"
        )
      )
    )
  }

  test("Can display one earthquake then two earthquakes") {
    displayElement(document.body)

    val containerElement = document.querySelector("div")
    val earthquakeQueryForm = EarthquakeQueryForm()

    render(
      container = containerElement,
      rootNode = earthquakeQueryForm.app
    )

    val tableBody = document.querySelector("tbody")

    expectNode(
      tableBody,
      tbody of sentinel
    )

    simulateEarthquakes(
      earthquakeQueryForm,
      Seq(Earthquake(magnitude = 9, place = "Ipswich, UK", time = 1741379415000L))
    )

    displayElement(document.body)

    expectNode(
      tableBody,
      tbody of(
        sentinel,
        tr of(
          sentinel,
          td of "9",
          td of "07/03/2025 20:30:15",
          td of "Ipswich, UK"
        )
      )
    )

    simulateEarthquakes(
      earthquakeQueryForm,
      Seq(
        Earthquake(magnitude = 9, place = "Ipswich, UK", time = 1741379415000L),
        Earthquake(magnitude = 8.7, place = "Norwich, UK", time = 1640433657000L)
      )
    )

    displayElement(document.body)

    expectNode(
      tableBody,
      tbody of(
        sentinel,
        tr of(
          sentinel,
          td of "9",
          td of "07/03/2025 20:30:15",
          td of "Ipswich, UK"
        ),
        tr of(
          sentinel,
          td of "8.7",
          td of "25/12/2021 12:00:57",
          td of "Norwich, UK"
        )
      )
    )
  }