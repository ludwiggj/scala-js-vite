package earthquakes.scalatest

import com.raquo.domtestutils.*
import com.raquo.laminar.api.L.*
import earthquakes.EarthquakeQueryForm
import earthquakes.model.Earthquake
import org.scalajs.dom.document

class EarthquakeQueryFormTest extends ScalaTestSpec:
  it("Can display a single earthquake") {
    def tableBody = document.querySelector("tbody")

    earthquakes.Utils.displayElement(document.body)
    val containerElement = document.createElement("div")
    earthquakes.Utils.displayElement(document.body)
    document.body.appendChild(containerElement)
    earthquakes.Utils.displayElement(document.body)
    render(
      container = containerElement,
      rootNode = EarthquakeQueryForm.app
    )
    earthquakes.Utils.displayElement(document.body)
    mount(tableBody)
    earthquakes.Utils.displayElement(document.body)

    expectNode(
      tbody of sentinel
    )

    EarthquakeQueryForm.model.setResponse(
      Right(
        Seq(
          Earthquake(magnitude = 9, place = "Ipswich, UK", time = 1741379415000L),
        )
      )
    )

    earthquakes.Utils.displayElement(document.body)
    
    expectNode(
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

  it("Can display two earthquakes") {
    //def tableBody = document.querySelector("tbody")

    earthquakes.Utils.displayElement(document.body)
    val containerElement = document.createElement("span")
    earthquakes.Utils.displayElement(document.body)
     document.body.appendChild(containerElement)
    // document.body.firstChild.appendChild(containerElement)
    // containerElement.firstChild
    earthquakes.Utils.displayElement(document.body)

    render(
      container = containerElement,
      rootNode = EarthquakeQueryForm.app
    )

    earthquakes.Utils.displayElement(document.body)

    println(1)

    mount(document.querySelector("tbody"))

    earthquakes.Utils.displayElement(document.body)

    println(2)

    expectNode(
      tbody of sentinel
    )

    EarthquakeQueryForm.model.setResponse(
      Right(
        Seq(
          Earthquake(magnitude = 9, place = "Ipswich, UK", time = 1741379415000L),
          Earthquake(magnitude = 8.7, place = "Norwich, UK", time = 1640433657000L)
        )
      )
    )

    earthquakes.Utils.displayElement(document.body)

    println(3)

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