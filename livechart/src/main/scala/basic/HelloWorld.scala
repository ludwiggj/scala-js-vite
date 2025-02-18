package basic

import org.scalajs.dom
import com.raquo.laminar.api.L.{*, given}

object Hello:
  val nameVar = Var("world")

  val rootElement = div(
      label("Your name:"),
      input(
          onMountFocus,
          placeholder := "Enter your name here",
          onInput.mapToValue --> nameVar
      ),
      span(
          "Hello, ",
          child.text <-- nameVar.signal.map(_.toUpperCase)
      )
  )

@main
// This method bootstraps Laminar by installing a Laminar Element in an existing DOM element:
def HelloWorld(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Hello.rootElement
  )