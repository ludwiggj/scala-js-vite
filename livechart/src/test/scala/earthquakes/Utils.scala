package earthquakes

import org.scalajs.dom.Element

object Utils:
  def displayElement(element: Element, depth: Int): Unit = {
    val buffer = "".padTo(depth, ' ')
    if (depth == 10)
      println(s"${buffer}STOP")
    else
      val childCount = element.children.length
      println(s"${buffer}${element.nodeName}($childCount)")
      element.children.foreach(e => displayElement(e, depth + 1))
  }