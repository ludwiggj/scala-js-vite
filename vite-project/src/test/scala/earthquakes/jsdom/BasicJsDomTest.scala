package earthquakes.jsdom

import com.raquo.laminar.api.L.{*, given}
import munit.FunSuite
import org.scalajs.dom
import org.scalajs.dom.{Element, document}

class BasicJsDomTest extends FunSuite:

  def displayElement(element: Element, depth: Int): Unit = {
    val buffer = "".padTo(depth, ' ')
    if (depth == 10)
      println(s"${buffer}STOP")
    else
      val childCount = element.children.length
      println(s"${buffer}${element.nodeName}($childCount)")
      element.children.foreach(e => displayElement(e, depth + 1))
  }

  test("Can load laminar node") {
    val jsDomNode: org.scalajs.dom.Node = div(
      rel := "yolo",
      span("Hello, "),
      p("bizzare ", a(href := "http://y2017.com", "2017"), span(" world")),
      h1("Earthquakes!")
    ).ref

    println("+++ Hi +++")
    document.body.appendChild(jsDomNode)
    displayElement(document.body, 1)

    assert(document.querySelectorAll("h1").count(_.textContent == "Earthquakes!") == 1)
  }