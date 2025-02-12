package forms

import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.tags.HtmlTag
import org.scalajs.dom
import org.scalajs.dom.HTMLElement
import com.raquo.laminar.nodes.ReactiveHtmlElement

// https://laminar.dev/examples/form-state
case class FormState(
  zip: String = "",
  description: String = "",
  showErrors: Boolean = false
) {
    def hasErrors: Boolean = zipError.nonEmpty || descriptionError.nonEmpty

    def zipError: Option[String] = {
        if (zip.forall(Character.isDigit) && zip.length == 5) {
            None
        } else {
            Some("Zip code must consist of 5 digits")
        }
    }

   def descriptionError: Option[String] = {
        if (description.nonEmpty) {
            None
        } else {
            Some("Description must not be empty.")
        }
    }

    def displayError(error: FormState => Option[String]): Option[String] = {
      if (showErrors) {
        error(this)
      } else {
        None
      }
    }
}

val stateVar = Var(FormState())

val zipWriter = stateVar.updater[String]((state, zip) => state.copy(zip = zip))

val descriptionWriter = stateVar.updater[String]((state, description) => state.copy(description = description))

val submitter = Observer[FormState] { state =>
    if (state.hasErrors) {
        stateVar.update(_.copy(showErrors = true))
        //dom.window.alert(s"State: $state contains errors!")
    } else {
        dom.window.alert(s"State: $state is valid")
    }
}

def renderInputRow(error: FormState => Option[String])(mods: Modifier[ReactiveHtmlElement[HTMLElement]]*): HtmlElement = {
  val errorSignal = stateVar.signal.map(_.displayError(error))
  div(
    cls("-inputRow"),
    cls("x-hasError") <-- errorSignal.map(_.nonEmpty),
    p(mods),
    child.maybe <-- errorSignal.map(_.map(err => div(cls("error"), err)))
  )
}

val app = div(
  form(
    onSubmit
      .preventDefault
      .mapTo(stateVar.now()) --> submitter,

    renderInputRow(_.zipError)(
      label("Zip code: "),
      input(
        placeholder("12345"),
        controlled(
          value <-- stateVar.signal.map(_.zip),
          onInput.mapToValue.filter(_.forall(Character.isDigit)) --> zipWriter
        )
      ),
      p(
        button(
          typ("button"), // "submit" is the default in HTML
          "Set SF zip code",
          onClick.mapTo("94110") --> zipWriter
        )
      )
    ),
    renderInputRow(_.descriptionError)(
      label("Description: "),
      input(
        controlled(
          value <-- stateVar.signal.map(_.description),
          onInput.mapToValue --> descriptionWriter
        )
      ),
      p(
        button(
          typ("button"), // "submit" is the default in HTML
          "Clear",
          onClick.mapTo("") --> descriptionWriter
        )
      )
    ),
    p(
      button(typ("submit"), "Submit")
    ),
    p(
        text <-- stateVar.signal.map(_.toString())
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