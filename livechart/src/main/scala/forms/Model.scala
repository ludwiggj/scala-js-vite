package forms

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

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

class Model { 
  val stateVar = Var(FormState())

  val zipWriter = stateVar.updater[String]((state, zip) => state.copy(zip = zip))

  val descriptionWriter = stateVar.updater[String]((state, description) => state.copy(description = description))

  def submitter(okAction: String => Unit) = Observer[FormState] { state =>
    if (state.hasErrors) {
        stateVar.update(_.copy(showErrors = true))
    } else {
        okAction(s"State: $state is valid")
    }
  }

  def errorSignal(error: FormState => Option[String]): Signal[Option[String]] =
    stateVar.signal.map(_.displayError(error))
}