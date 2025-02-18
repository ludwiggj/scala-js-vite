package basic.counter

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

object Main:
    def Counter(label: String, initialStep: Int): HtmlElement = {
        val model = Model(initialStep)

        div(
            p(
                "Step: ",
                select(
                    value <-- model.stepVar.signal.map(_.toString),
                    onChange.mapToValue.map { _.toInt } --> model.setStep,
                    model.allowedSteps.map { step =>
                        option(value := step.toString, step)
                    }
                )
            ),
            p(
                label + ": ",
                b(text <-- model.countSignal),
                button(
                    "-",
                    onClick --> {_ => model.decrement}
                ),
                button(
                    "+",
                    onClick --> {_ => model.increment}
                )
            )
        )
    }

    val rootElement = div(
        h1("Let's count!"),
        Counter("Sheep", initialStep = 3)
    )

@main
// This method bootstraps Laminar by installing a Laminar Element in an existing DOM element:
def Count(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.rootElement
  )