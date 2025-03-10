package earthquakes.moonit

import com.raquo.laminar.api.L.*
import earthquakes.EarthquakeQueryForm
import earthquakes.Utils.displayElement
import earthquakes.model.Earthquake
import org.scalajs.dom.document

class EarthquakeQueryFormTestWithNativeDomAsserts extends munit.FunSuite:
  private def simulateEarthquakes(form: EarthquakeQueryForm, earthquakes: Seq[Earthquake]): Unit =
    form.model.setResponse(Right(earthquakes))

  test("Can display one earthquake") {
    displayElement(document.body)

    val containerElement = document.createElement("div")
    document.body.appendChild(containerElement)

    val earthquakeQueryForm = EarthquakeQueryForm()

    render(
      container = containerElement,
      rootNode = earthquakeQueryForm.app
    )

    val tableBody = document.querySelector("tbody")
    assert(tableBody.children.length == 0)

    simulateEarthquakes(
      earthquakeQueryForm,
      Seq(Earthquake(magnitude = 9, place = "Ipswich, UK", time = 1741379415000L))
    )

    assert(tableBody.children.length == 1)
    assert(tableBody.children(0).children(0).textContent == "9")
    assert(tableBody.children(0).children(1).textContent == "07/03/2025 20:30:15")
    assert(tableBody.children(0).children(2).textContent == "Ipswich, UK")

    displayElement(document.body)

    // Must remove container element so its state does not pollute other tests
    document.body.removeChild(containerElement)

    displayElement(document.body)
  }

  test("Can display two earthquakes") {
    displayElement(document.body)

    val containerElement = document.createElement("div")
    document.body.appendChild(containerElement)

    val earthquakeQueryForm = EarthquakeQueryForm()

    render(
      container = containerElement,
      rootNode = earthquakeQueryForm.app
    )

    val tableBody = document.querySelector("tbody")
    assert(tableBody.children.length == 0)

    simulateEarthquakes(
      earthquakeQueryForm,
      Seq(
        Earthquake(magnitude = 9, place = "Ipswich, UK", time = 1741379415000L),
        Earthquake(magnitude = 8.7, place = "Norwich, UK", time = 1640433657000L)
      )
    )

    assert(tableBody.children.length == 2)
    assert(tableBody.children(0).children(0).textContent == "9")
    assert(tableBody.children(0).children(1).textContent == "07/03/2025 20:30:15")
    assert(tableBody.children(0).children(2).textContent == "Ipswich, UK")
    assert(tableBody.children(1).children(0).textContent == "8.7")
    assert(tableBody.children(1).children(1).textContent == "25/12/2021 12:00:57")
    assert(tableBody.children(1).children(2).textContent == "Norwich, UK")

    displayElement(document.body)

    // Must remove container element so its state does not pollute other tests
    document.body.removeChild(containerElement)

    displayElement(document.body)
  }

  test("Can display one earthquake then two earthquakes") {
    displayElement(document.body)

    val containerElement = document.createElement("div")
    document.body.appendChild(containerElement)

    val earthquakeQueryForm = EarthquakeQueryForm()

    render(
      container = containerElement,
      rootNode = earthquakeQueryForm.app
    )

    val tableBody = document.querySelector("tbody")
    assert(tableBody.children.length == 0)

    simulateEarthquakes(
      earthquakeQueryForm,
      Seq(Earthquake(magnitude = 9, place = "Ipswich, UK", time = 1741379415000L))
    )

    assert(tableBody.children.length == 1)
    assert(tableBody.children(0).children(0).textContent == "9")
    assert(tableBody.children(0).children(1).textContent == "07/03/2025 20:30:15")
    assert(tableBody.children(0).children(2).textContent == "Ipswich, UK")

    displayElement(document.body)

    simulateEarthquakes(
      earthquakeQueryForm,
      Seq(
        Earthquake(magnitude = 9, place = "Ipswich, UK", time = 1741379415000L),
        Earthquake(magnitude = 8.7, place = "Norwich, UK", time = 1640433657000L)
      )
    )

    assert(tableBody.children.length == 2)
    assert(tableBody.children(0).children(0).textContent == "9")
    assert(tableBody.children(0).children(1).textContent == "07/03/2025 20:30:15")
    assert(tableBody.children(0).children(2).textContent == "Ipswich, UK")
    assert(tableBody.children(1).children(0).textContent == "8.7")
    assert(tableBody.children(1).children(1).textContent == "25/12/2021 12:00:57")
    assert(tableBody.children(1).children(2).textContent == "Norwich, UK")

    displayElement(document.body)

    // Must remove container element so its state does not pollute other tests
    document.body.removeChild(containerElement)

    displayElement(document.body)
  }