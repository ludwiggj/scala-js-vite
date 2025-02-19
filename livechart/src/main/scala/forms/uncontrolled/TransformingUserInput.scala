package forms.uncontrolled

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

// In other UI libraries if you want to transform user input you need to use controlled components.
// We have that too, but for relatively simple cases you can use setAsValue and setAsChecked event
// processor operators instead.

// The way it works is simple: when the event processor reaches the setAsValue operator, it writes
// the string provided to it into event.target.value. For this reason you shouldn't use the filter
// event processor operator before setAsValue: setAsValue will not be called if the predicate
// doesn't match. Note that we use the map operator here, not filter. The filter we use is actually
// a method we call on String to transform it.

// If you want to filter user input, for example if you want to prevent any input containing
// non-digits (as opposed to stripping out non-digits from such input), you should use Laminar
// controlled inputs instead.
object Transforming:
  val zipVar = Var("")

  val app = div(
    p(
      label("Zip code: "),
      input(
        placeholder("12345"),
        maxLength(5), // HTML can help block some undesired input
        onInput
          .mapToValue
          .map(_.filter(Character.isDigit))
          .setAsValue --> zipVar
      )
    ),
    p(
      "Your zip code: ",
      child.text <-- zipVar
    ),
    button(
      onClick.mapTo(zipVar.now()) --> (zip => dom.window.alert(zip)),
      "Submit"
    )
  )

@main
// This method bootstraps Laminar by installing a Laminar Element in an existing DOM element:
def TransformingUserInput(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Transforming.app
  )