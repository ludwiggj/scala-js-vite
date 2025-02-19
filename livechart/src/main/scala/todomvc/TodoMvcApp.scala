package todomvc

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

object TodoMvcApp:
    lazy val node: HtmlElement = {
        p("Hello")
    }


@main
// This method bootstraps Laminar by installing a Laminar Element in an existing DOM element:
def ToDoMVC(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    TodoMvcApp.node
  )