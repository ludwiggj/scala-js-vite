package livechart.shopping

import scala.util.Random
import com.raquo.laminar.api.L.{*, given}

final class DataItemID

// NOTE - apply(id) method required by split operation
object DataItemID {
    def apply(): DataItemID = new DataItemID
    def apply(id: DataItemID): DataItemID = id
}

case class DataItem(id: DataItemID, label: String, price: Double, count: Int):
    def fullPrice: Double = price * count

object DataItem:
    def apply(): DataItem =
        DataItem(DataItemID(), "?", Random().nextDouble(), Random.nextInt(5) + 1)
end DataItem

type DataList = List[DataItem]

final class Model:
    val dataVar: Var[DataList] = Var(List(DataItem(DataItemID(), "one", 1.0, 0)))
    val dataSignal = dataVar.signal

    def addDataItem(item: DataItem): Unit =
        dataVar.update(data => data :+ item)

    def removeDataItem(id: DataItemID): Unit =
        dataVar.update(data => data.filter(_.id != id)) 
end Model