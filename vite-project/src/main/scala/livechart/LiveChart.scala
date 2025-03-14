package livechart

import scala.scalajs.js
import scala.scalajs.js.annotation.* 

import org.scalajs.dom
import com.raquo.laminar.api.L.{*, given}

// import javascriptLogo from /javascript.svg
@js.native @JSImport("/javascript.svg", JSImport.Default)
val javascriptLogo: String = js.native

object Main:
    def appElement(): Element =
        div(
            a(
                href := "https://vitejs.dev", target := "_blank",
                img(
                    src := "/vite.svg", className := "logo", alt := "Vite logo"
                ),
            ),
            a(
                href := "https://developer.mozilla.org/en-US/docs/Web/JavaScript", target := "_blank",
                img(
                    src := javascriptLogo, className := "logo vanilla", alt := "JavaScript logo"
                ),
            ),
            h1("Hello Laminar!"),
            div(
                className := "card",
                counterButton(),
            ),
            p(
                className := "read-the-docs",
                "Click on the Vite logo to learn more",
            ),
        )
    end appElement
end Main

@main
// This method bootstraps Laminar by installing a Laminar Element in an existing DOM element:
def LiveChart(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement()
  )

// Laminar defines 3 symbolic bindings:
//   :=   A static binding. It can be seen as a <-- with a time-immutable value on the right.
//   <--  Makes data flow from the right to the left; it is usually used with DOM attributes
//        on the left and signals on the right.
//   -->  Makes data flow from the left to the right; it is usually used with DOM events on
//        the left and observers on the right.

// It helps to visualize the UI as being “on the left” and the application data model as
// being “on the right”.


def counterButton(): Element =
    val counter = Var(0)
    button(
        tpe := "button",
        "count is ",                            // first text child of button
        child.text <-- counter,                 // second text child of the button whose content will always reflect the value of counter
        onClick --> {
            event => counter.update(c => c + 1)
        },
    )
end counterButton

// The first definition is a Var containing an Int, like the counter we used before.
// It is initialized with the value 1, but its value can evolve over time.
val intVar: Var[Int] = Var(1)

// A Var is a read-write container. Often, we want to give access to its value in a read-only way,
// which is what intVar.signal does.

// A Signal is a read-only view of some time-varying value. Signals are similar to Scala immutable collections.
// Whereas collections give values as functions of indices, signals give them as functions of time. As time progresses,
// the value in a Signal can change.
val intSignal: Signal[Int] = intVar.signal

// Like collections, we can manipulate signals with the typical higher-order functions. For example,
// we use map here to get another times2Signal: Signal whose time-varying value is always twice that
// of intSignal.
val times2Signal: Signal[Int] = intSignal.map(_ * 2)
