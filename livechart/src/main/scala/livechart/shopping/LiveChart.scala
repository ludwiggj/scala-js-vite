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
            data.map { item =>
                if
                    (item.id == id)
                then
                    f(item, newValue)
                else
                    item 
            }
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

    // Note that the <-- and --> binders connecting strValue with valueSignal and valueUpdater are arguments to the Laminar input element.
    // This may seem suspicious, as none of them nor their callbacks have any direct relationship to the DOM input element. This is done
    // to tie the lifetime of the binders to the lifetime of the input element. When the latter gets unmounted, we release the binder
    // connections, possibly allowing resources to be reclaimed.
    // In general, every binder must be owned by a Laminar element. It only gets activated when that element is mounted. This prevents
    // memory leaks.
    def inputForDouble(valueSignal: Signal[Double], valueUpdater: Observer[Double]): Input =
        val strValue = Var[String]("")
        input(
            typ := "text",
            value <-- strValue.signal,
            onInput.mapToValue --> strValue,
            valueSignal --> strValue.updater[Double] { (prevStr, newValue) =>
                if
                    (prevStr.toDoubleOption.contains(newValue))
                then
                    prevStr
                else
                    "%.3f".format(newValue)
            },
            strValue.signal --> { valueStr =>
                valueStr.toDoubleOption.foreach(valueUpdater.onNext)
            } 
        )
    end inputForDouble

    // def inputForInt(valueSignal: Signal[Int], valueUpdater: Observer[Int]): Input =
    //     val strValue = Var[String]("")
    //     input(
    //         typ := "text",
    //         value <-- strValue.signal,
    //         onInput.mapToValue --> strValue,
    //         valueSignal --> strValue.updater[Int] { (prevStr, newValue) =>
    //             if
    //                 (prevStr.toIntOption.contains(newValue))
    //             then
    //                 prevStr
    //             else
    //                 newValue.toString
    //         },
    //         strValue.signal --> { valueStr =>
    //             valueStr.toIntOption.foreach(valueUpdater.onNext)
    //         } 
    //     )
    // end inputForInt

    def inputForInt(valueSignal: Signal[Int], valueUpdater: Observer[Int]): Input =
        input(
            typ := "text",
            controlled(
              value <-- valueSignal.map(_.toString),
              onInput.mapToValue.map(_.toIntOption).collect {
                case Some(newCount) => newCount
                case _ => 0
              } --> valueUpdater
            )
        )
    end inputForInt

    def renderDataItem(id: DataItemID, itemSignal: Signal[DataItem]): Element =
        tr(
            td(
                inputForString(
                    itemSignal.map(_.label),
                    makeDataItemUpdater[String](id, (item, newLabel) => item.copy(label = newLabel))
                )
            ),
            td(
                inputForDouble(
                    itemSignal.map(_.price),
                    makeDataItemUpdater[Double](id, (item, newPrice) => item.copy(price = newPrice))
                )
            ),
            td(
                inputForInt(
                    itemSignal.map(_.count),
                    makeDataItemUpdater[Int](id, (item, newCount) => item.copy(count = newCount))
                )
            ),
            td(child.text <-- itemSignal.map(item => "%.3f".format(item.fullPrice))),
            td(button("-", onClick --> (_ => removeDataItem(id)))),
        )
    end renderDataItem

    def renderDataList(): Element =
        ul(
            children <-- dataSignal.split(_.id) { (id, initial, itemSignal) =>
                li(child.text <-- itemSignal.map(item => s"${item.label} ${"%.3f".format(item.price)}"))
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

