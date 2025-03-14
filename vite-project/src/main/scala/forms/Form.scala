package forms

import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.tags.HtmlTag
import org.scalajs.dom
import org.scalajs.dom.HTMLElement
import com.raquo.laminar.nodes.ReactiveHtmlElement

// https://laminar.dev/examples/form-state
object Main:
  val model = Model()
  import model.*

  def renderInputRow(error: FormState => Option[String])(mods: Modifier[ReactiveHtmlElement[HTMLElement]]*): HtmlElement = {
    val errorSignal = model.errorSignal(error)
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
        .preventDefault --> (_ => validate(dom.window.alert)),

      renderInputRow(_.zipError)(
        label("Zip code: "),
        input(
          placeholder("12345"),
          controlled(
            value <-- stateSignal.map(_.zip),
            onInput.mapToValue.filter(_.forall(Character.isDigit)) --> model.setZip
          )
        ),
        p(
          button(
            typ("button"), // "submit" is the default in HTML
            "Set SF zip code",
            onClick.mapTo("94110") --> model.setZip
          )
        )
      ),
      renderInputRow(_.descriptionError)(
        label("Description: "),
        input(
          controlled(
            value <-- stateSignal.map(_.description),
            onInput.mapToValue --> model.setDescription
          )
        ),
        p(
          button(
            typ("button"), // "submit" is the default in HTML
            "Clear",
            onClick.mapTo("") --> model.setDescription
          )
        )
      ),
      p(
        button(typ("submit"), "Submit")
      ),
      p(
          text <-- stateSignal.map(_.toString())
      )
    )
  )

@main
// This method bootstraps Laminar by installing a Laminar Element in an existing DOM element:
def ShowForm(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.app
  )