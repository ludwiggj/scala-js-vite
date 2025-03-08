package earthquakes.scalatest

import com.raquo.domtestutils.*
import com.raquo.laminar.api.L.*
import earthquakes.EarthquakeQueryForm
import earthquakes.model.Earthquake
import org.scalajs.dom.document

class EarthquakeQueryFormTest extends ScalaTestSpec:
  it("Can get earthquake data") {
    def tableBody = document.querySelector("tbody")

    val containerElement = document.createElement("div")
    document.body.appendChild(containerElement)

    render(
      container = containerElement,
      rootNode = EarthquakeQueryForm.app
    )

    mount(tableBody)

    expectNode(
      tbody of sentinel
    )

    EarthquakeQueryForm.formObserver.onNext(
      Right(
        Seq(
          Earthquake(magnitude = 9, place = "Ipswich, UK", time = 1741379415000L),
          Earthquake(magnitude = 8.7, place = "Norwich, UK", time = 1640433657000L)
        )
      )
    )

    expectNode(
      tbody of (
        sentinel,
        tr of (
          sentinel,
          td of "9",
          td of "07/03/2025 20:30:15",
          td of "Ipswich, UK"
        ),
        tr of (
          sentinel,
          td of "8.7",
          td of "25/12/2021 12:00:57",
          td of "Norwich, UK"
        )
      )
    )
  }