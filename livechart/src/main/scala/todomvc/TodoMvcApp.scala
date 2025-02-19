package todomvc

import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import org.scalajs.dom.KeyboardEvent
//import scala.scalajs.js
//import scala.scalajs.js.annotation.JSImport

object TodoMvcApp:
    //@js.native @JSImport("@find/**/TodoMvcApp.css")
    //private object Stylesheet extends js.Object
    //def useImport(importedObject: js.Any): Unit = ()
    //useImport(Stylesheet)

    // --- Models ---
    case class TodoItem(id: Int, text: String, completed: Boolean)

    sealed abstract class Filter(val name: String, val passes: TodoItem => Boolean)

    object ShowAll extends Filter("All", _ => true)

    object ShowActive extends Filter("Active", !_.completed)

    object ShowCompleted extends Filter("Completed", _.completed)

    val filters: List[Filter] = ShowAll :: ShowActive :: ShowCompleted :: Nil

    sealed trait Command
    
    case class Create(itemText: String) extends Command

    // --- State ---

    // Var-s are reactive state variables suitable for both local state and redux-like global stores.
    // Laminar uses my library Airstream as its reactive layer https://github.com/raquo/Airstream

    private val itemsVar = Var(List[TodoItem]())

    private val filterVar = Var[Filter](ShowAll)

    private var lastId = 0 // just for auto-incrementing IDs

    private val commandObserver = Observer[Command] {
      case item@Create(itemText) =>
        lastId += 1
      //   if (filterVar.now() == ShowCompleted)
      //     filterVar.set(ShowAll)
        itemsVar.update(_ :+ TodoItem(id = lastId, text = itemText, completed = false))
        dom.window.alert(s"Created item $item\nToDos: ${itemsVar.now()}")
      // case UpdateText(itemId, text) =>
      //   itemsVar.update(_.map(item => if (item.id == itemId) item.copy(text = text) else item))
      // case UpdateCompleted(itemId, completed) =>
      //   itemsVar.update(_.map(item => if (item.id == itemId) item.copy(completed = completed) else item))
      // case Delete(itemId) =>
      //   itemsVar.update(_.filterNot(_.id == itemId))
      // case DeleteCompleted =>
      //   itemsVar.update(_.filterNot(_.completed))
    }

    // --- Views ---
    lazy val node: HtmlElement = {
      val todoItemsSignal = itemsVar
        .signal
        .combineWith(filterVar.signal)
        .mapN{
          case (todos, filter) => todos.filter(todo => filter.passes(todo))
        }
    
      div(
        cls("todoapp"),
        div(
          cls("header"),
          h1("todos"),
          renderNewTodoInput,
        ),
        div(
        //  hideIfNoItems,
          cls("main"),
          ul(
            cls("todo-list"),
            children <-- todoItemsSignal.split(_.id) {
              (id, todoItem, todoItemSignal) => li(s"id: $id, todo: $todoItem")
            }
          )
        )//,
        //renderStatusBar
      )
    }

    private def renderNewTodoInput =
      input(
        cls("new-todo"),
        placeholder("What needs to be done?"),
        autoFocus(true),
        onEnterPress
          .mapToValue
          .filter(_.nonEmpty)
          .map(Create(_))
          .setValue("") --> commandObserver
      )

    // --- Generic helpers ---
    private val onEnterPress: EventProcessor[KeyboardEvent, KeyboardEvent] =
      onKeyPress.filter(_.keyCode == dom.KeyCode.Enter)


@main
// This method bootstraps Laminar by installing a Laminar Element in an existing DOM element:
def ToDoMVC(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    TodoMvcApp.node
  )