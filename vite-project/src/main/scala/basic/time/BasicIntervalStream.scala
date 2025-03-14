package basic.time

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import scala.util.Random

object BasicIntervalStream:
  val tickStream = EventStream.periodic(1000)
  
  val rootElement = div(
    div(
        "Tick #: ",
        child.text <-- tickStream.map(_.toString())
    ),
    div(
        "Random #: ",
        child.text <-- tickStream.mapTo(Random.nextInt() % 100)
    )
  )

@main
// This method bootstraps Laminar by installing a Laminar Element in an existing DOM element:
def Tick(): Unit =
    renderOnDomContentLoaded(
        dom.document.getElementById("app"),
        BasicIntervalStream.rootElement
    )