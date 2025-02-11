package forms

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

// https://laminar.dev/examples/form-state
case class FormState(
  zip: String = "",
  description: String = ""  
)

val stateVar = Var(FormState())

val zipWriter = stateVar.updater[String]((state, zip) => state.copy(zip = zip))

val descriptionWriter = stateVar.updater[String]((state, description) => state.copy(description = description))

val submitter = Observer[FormState] { state =>
  dom.window.alert(s"State: $state")
}

val app = div(
  form(
    onSubmit
      .preventDefault
      .mapTo(stateVar.now()) --> submitter,
    p(
      label("Zip code: "),
      input(
        placeholder("12345"),
        controlled(
          value <-- stateVar.signal.map(_.zip),
          onInput.mapToValue.filter(_.forall(Character.isDigit)) --> zipWriter
        )
      ),
    ),
    p(
      label("Description: "),
      input(
        controlled(
          value <-- stateVar.signal.map(_.description),
          onInput.mapToValue --> descriptionWriter
        )
      ),
    ),
    p(
      button(typ("submit"), "Submit")
    )
  )
)

@main
// This method bootstraps Laminar by installing a Laminar Element in an existing DOM element:
def ShowForm(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    app
  )