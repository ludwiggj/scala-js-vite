package basic

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

def Counter(label: String, initialStep: Int): HtmlElement = {
    val allowedSteps = List(1, 2, 3, 5, 10)

    val stepVar = Var(initialStep)

    val diffBus = new EventBus[Int]

    val countSignal: Signal[Int] = diffBus.events.scanLeft(initial = 0)(_ + _)

    div(
        p(
            "Step: ",
            select(
                value <-- stepVar.signal.map(_.toString),
                onChange.mapToValue.map { _.toInt } --> stepVar,
                allowedSteps.map { step =>
                    option(value := step.toString, step)
                }
            )
        ),
        p(
            label + ": ",
            b(text <-- countSignal),
            button(
                "-",
                onClick.mapTo(-1 * stepVar.now()) --> diffBus
            ),
            button(
                "+",
                onClick.compose(_.sample(stepVar.signal)) --> diffBus
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
    rootElement
  )