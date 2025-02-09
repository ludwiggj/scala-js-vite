package livechart.shopping

import scala.scalajs.js
import scala.scalajs.js.annotation.* 

import org.scalajs.dom
import com.raquo.laminar.api.L.{*, given}
import livechart.shopping.Main.renderDataTable
import livechart.shopping.Main.renderDataItem
import livechart.shopping.Main.renderDataList

object Main:
    val model = new Model
    import model.*

    def appElement(): Element =
        div(
            h1("Live Chart"),
            renderDataTable(),
            renderDataList(),
        )
    end appElement

    def renderDataTable(): Element =
        table(
            thead(tr(th("Label"), th("Price"), th("Count"), th("Full Price"), th("Action"))),
            tbody(
                children <-- dataSignal.split(_.id) {
                    (id, initial, itemSignal) => renderDataItem(id, itemSignal)
                }
            ),
            tfoot(
                tr(
                    td(button("+", onClick --> (_ => addDataItem(DataItem())))),
                    td(),
                    td(),
                    td(child.text <-- dataSignal.map(data => "%.2f".format(data.map(_.fullPrice).sum)))
                )
            ),
        )
    end renderDataTable

    def makeDataItemUpdater[A](id: DataItemID, f: (DataItem, A) => DataItem): Observer[A] =
        dataVar.updater[A] { (data, newValue) =>
            data.map { item => if (item.id == id) then f(item, newValue) else item }
        }
    end makeDataItemUpdater

    // inputForString represents an input text whose value is linked to Signal[String], which can be updated through some Observer[String].
    // This method is a component. It takes a data model value as an argument, and returns a Laminar element manipulating those values.
    // In Laminar, components are nothing but methods manipulating time-varying data and returning Laminar elements.
    def inputForString(valueSignal: Signal[String], valueUpdater: Observer[String]): Input =
        input(
            typ := "text",
            value <-- valueSignal,
            onInput.mapToValue --> valueUpdater
        )
    end inputForString


    def renderDataItem(id: DataItemID, itemSignal: Signal[DataItem]): Element =
        tr(
            td(
                inputForString(
                    itemSignal.map(_.label),
                    makeDataItemUpdater[String](id, (item, newLabel) => item.copy(label = newLabel))
                )
            ),
            td(child.text <-- itemSignal.map(item =>"%.3f".format(item.price))),
            td(child.text <-- itemSignal.map(_.count)),
            td(child.text <-- itemSignal.map(item => "%.3f".format(item.fullPrice))),
            td(button("-", onClick --> (_ => removeDataItem(id)))),
        )
    end renderDataItem

    def renderDataList(): Element =
        ul(
            children <-- dataSignal.split(_.id) { (id, initial, itemSignal) =>
                li(child.text <-- itemSignal.map(item => s"${item.count} ${item.label}"))
                }

        )
    end renderDataList
end Main

@main
// This method bootstraps Laminar by installing a Laminar Element in an existing DOM element:
def LiveChart(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement()
  )

