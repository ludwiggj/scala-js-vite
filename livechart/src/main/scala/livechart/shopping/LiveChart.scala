package livechart.shopping

import scala.scalajs.js
import scala.scalajs.js.annotation.* 

import org.scalajs.dom
import com.raquo.laminar.api.L.{*, given}
import livechart.shopping.Main.renderDataTable
import livechart.shopping.Main.renderDataItem

// import javascriptLogo from /javascript.svg
@js.native @JSImport("/javascript.svg", JSImport.Default)
val javascriptLogo: String = js.native

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
                children <-- dataSignal.map(data => data.map { item =>
                    renderDataItem(item.id, item)
                })
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

    def renderDataItem(id: DataItemID, item: DataItem): Element =
        tr(
            td(item.label),
            td("%.3f".format(item.price)),
            td(item.count),
            td("%.3f".format(item.fullPrice)),
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

def counterButton(): Element =
    val counter = Var(0)
    button(
        tpe := "button",
        "count is ",
        child.text <-- counter,
        onClick --> {
            event => counter.update(c => c + 1)
        },
    )
end counterButton
