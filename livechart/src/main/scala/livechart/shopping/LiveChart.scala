package livechart.shopping

import scala.scalajs.js
import scala.scalajs.js.annotation.* 

import org.scalajs.dom
import com.raquo.laminar.api.L.{*, given}
import livechart.shopping.Main.renderDataTable
import livechart.shopping.Main.renderDataItem

object Main:
    val model = new Model
    import model.*

    def appElement(): Element =
        div(
            h1("Live Chart"),
            renderDataTable(),
        )
    end appElement

    def renderDataTable(): Element =
        table(
            thead(tr(th("Label"), th("Price"), th("Count"), th("Full Price"), th("Action"))),
            tbody(
                children <-- dataSignal.split(xxx => xxx.id) {
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

    def renderDataItem(id: DataItemID, itemSignal: Signal[DataItem]): Element =
        tr(
            td(child.text <-- itemSignal.map(_.label)),
            td(child.text <-- itemSignal.map(item =>"%.3f".format(item.price))),
            td(child.text <-- itemSignal.map(_.count)),
            td(child.text <-- itemSignal.map(item => "%.3f".format(item.fullPrice))),
            td(button("-", onClick --> (_ => removeDataItem(id)))),
        )
    end renderDataItem
end Main

@main
// This method bootstraps Laminar by installing a Laminar Element in an existing DOM element:
def LiveChart(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement()
  )

