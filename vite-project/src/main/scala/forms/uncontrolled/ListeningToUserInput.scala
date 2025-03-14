package forms.uncontrolled

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

// "Uncontrolled input" basically means a unidirectional data binding: you're only listening
// to the user's input, your own code isn't updating the input element's value property with
// value <-- observable.

object Listening:
  val inputTextVar = Var("")

  val checkedVar = Var(false)

  val app = div(
    p(
      label("Name: "),
      input(
        onInput.mapToValue --> inputTextVar
      )
    ),
    p(
      "You typed: ",
      child.text <-- inputTextVar
    ),
    p(
      label("I like to check boxes: "),
      input(
        typ("checkbox"),
        onInput.mapToChecked --> checkedVar
      )
    ),
    p(
      "You checked the box: ",
      child.text <-- checkedVar
    )
  )

@main
// This method bootstraps Laminar by installing a Laminar Element in an existing DOM element:
def ListeningToUserInput(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Listening.app
  )