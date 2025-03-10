package forms

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import forms.FormState.zipErrorMsg
import forms.FormState.descriptionErrorMsg

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
            Some(zipErrorMsg)
        }
    }

    def descriptionError: Option[String] = {
        if (description.nonEmpty) {
            None
        } else {
            Some(descriptionErrorMsg)
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

object FormState {
  val zipErrorMsg = "Zip code must consist of 5 digits"
  val descriptionErrorMsg = "Description must not be empty"
}

class Model { 
  private val stateVar = Var(FormState())

  private val zipWriter = stateVar.updater[String]((state, zip) => state.copy(zip = zip))

  private val descriptionWriter = stateVar.updater[String]((state, description) => state.copy(description = description))

  // Signal is read-only, so it's safe to expose it
  val stateSignal: StrictSignal[FormState] = stateVar.signal
  
  def setZip(zip: String): Unit = zipWriter.onNext(zip)

  def setDescription(description: String): Unit = descriptionWriter.onNext(description)

  def validate(okAction: String => Unit): Unit = Observer[FormState] { state =>
    if (state.hasErrors) {
        stateVar.update(_.copy(showErrors = true))
    } else {
        okAction(s"State: $state is valid")
    }
  }.onNext(stateVar.now())

  def errorSignal(error: FormState => Option[String]): StrictSignal[Option[String]] =
    stateVar.signal.mapLazy(_.displayError(error))
}