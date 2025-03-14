package earthquakes.munit

import com.raquo.laminar.api.L.*
import earthquakes.EarthquakeQueryForm
import earthquakes.Utils.*
import earthquakes.model.Earthquake
import laminar.domtestutils.munit.MUnitSpec
import org.scalajs.dom.document

class EarthquakeQueryFormTest extends MUnitSpec:
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

    simulateResponse(
      earthquakeQueryForm,
      Right(Seq(Earthquake(magnitude = 9, place = "Ipswich, UK", time = 1741379415000L)))
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

    simulateResponse(
      earthquakeQueryForm,
      Right(Seq(
        Earthquake(magnitude = 9, place = "Ipswich, UK", time = 1741379415000L),
        Earthquake(magnitude = 8.7, place = "Norwich, UK", time = 1640433657000L)
      ))
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

    simulateResponse(
      earthquakeQueryForm,
      Right(Seq(Earthquake(magnitude = 9, place = "Ipswich, UK", time = 1741379415000L)))
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

    simulateResponse(
      earthquakeQueryForm,
      Right(Seq(
        Earthquake(magnitude = 9, place = "Ipswich, UK", time = 1741379415000L),
        Earthquake(magnitude = 8.7, place = "Norwich, UK", time = 1640433657000L)
      ))
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

  test("Can display an error") {
    val containerElement = document.querySelector("div")

    val earthquakeQueryForm = EarthquakeQueryForm()

    render(
      container = containerElement,
      rootNode = earthquakeQueryForm.app
    )

    val errorMessage = "No earthquakes today!"

    simulateResponse(
      earthquakeQueryForm,
      Left(errorMessage)
    )

    displayElement(document.body)

    expectNode(
      document.getElementsByClassName("error")(0),
      p of errorMessage
    )
  }