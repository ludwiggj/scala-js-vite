package forms.uncontrolled

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

// You don't need to keep track of state in Vars. It is often useful, and more complex
// code tends to need that for auxiliary reasons, but you can fetch the state from the
// DOM instead:
object NoVars:
  val inputEl = input(
    placeholder("12345"),
    maxLength(5), // HTML can help block some undesired input
    onInput
      .mapToValue
      .map(_.filter(Character.isDigit))
      .setAsValue --> Observer.empty
  )

  val app = div(
    form(
      onSubmit
        .preventDefault
        .mapTo(inputEl.ref.value) --> (zip => dom.window.alert(zip)),
      p(
        label("Zip code: "),
        inputEl
      ),
      p(
        button(typ("submit"), "Submit")
      )
    )
  )

@main
// This method bootstraps Laminar by installing a Laminar Element in an existing DOM element:
def FormsWithoutVars(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    NoVars.app
  )