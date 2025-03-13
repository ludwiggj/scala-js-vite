package earthquakes.moonit

import com.raquo.domtestutils.MUnitSpec
import com.raquo.laminar.api.L.*
import earthquakes.EarthquakeQueryForm
import earthquakes.Utils.*
import earthquakes.model.Earthquake
import org.scalajs.dom.document

class EarthquakeQueryFormTestWithNativeDomAsserts extends MUnitSpec:
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
    tableBody.children.length shouldBe 0

    simulateResponse(
      earthquakeQueryForm,
      Right(Seq(Earthquake(magnitude = 9, place = "Ipswich, UK", time = 1741379415000L)))
    )

    tableBody.children.length shouldBe 1
    tableBody.children(0).children(0).textContent shouldBe "9"
    tableBody.children(0).children(1).textContent shouldBe "07/03/2025 20:30:15"
    tableBody.children(0).children(2).textContent shouldBe "Ipswich, UK"

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
    tableBody.children.length shouldBe 0

    simulateResponse(
      earthquakeQueryForm,
      Right(Seq(
        Earthquake(magnitude = 9, place = "Ipswich, UK", time = 1741379415000L),
        Earthquake(magnitude = 8.7, place = "Norwich, UK", time = 1640433657000L)
      ))
    )

    tableBody.children.length shouldBe 2
    tableBody.children(0).children(0).textContent shouldBe "9"
    tableBody.children(0).children(1).textContent shouldBe "07/03/2025 20:30:15"
    tableBody.children(0).children(2).textContent shouldBe "Ipswich, UK"
    tableBody.children(1).children(0).textContent shouldBe "8.7"
    tableBody.children(1).children(1).textContent shouldBe "25/12/2021 12:00:57"
    tableBody.children(1).children(2).textContent shouldBe "Norwich, UK"

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
    tableBody.children.length shouldBe 0

    simulateResponse(
      earthquakeQueryForm,
      Right(Seq(Earthquake(magnitude = 9, place = "Ipswich, UK", time = 1741379415000L)))
    )

    tableBody.children.length shouldBe 1
    tableBody.children(0).children(0).textContent shouldBe "9"
    tableBody.children(0).children(1).textContent shouldBe "07/03/2025 20:30:15"
    tableBody.children(0).children(2).textContent shouldBe "Ipswich, UK"

    displayElement(document.body)

    simulateResponse(
      earthquakeQueryForm,
      Right(Seq(
        Earthquake(magnitude = 9, place = "Ipswich, UK", time = 1741379415000L),
        Earthquake(magnitude = 8.7, place = "Norwich, UK", time = 1640433657000L)
      ))
    )

    tableBody.children.length shouldBe 2
    tableBody.children(0).children(0).textContent shouldBe "9"
    tableBody.children(0).children(1).textContent shouldBe "07/03/2025 20:30:15"
    tableBody.children(0).children(2).textContent shouldBe "Ipswich, UK"
    tableBody.children(1).children(0).textContent shouldBe "8.7"
    tableBody.children(1).children(1).textContent shouldBe "25/12/2021 12:00:57"
    tableBody.children(1).children(2).textContent shouldBe "Norwich, UK"

    displayElement(document.body)

    // Must remove container element so its state does not pollute other tests
    document.body.removeChild(containerElement)

    displayElement(document.body)
  }