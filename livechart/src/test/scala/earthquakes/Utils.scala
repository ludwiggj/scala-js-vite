package earthquakes

import org.scalajs.dom.Element

object Utils:
  def displayElement(element: Element, maxDepth: Int = 10): Unit = {
    def loop(element: Element, depth: Int = 1): Unit = {
      val buffer = "".padTo(depth, ' ')
      if (depth == maxDepth)
        println(s"${buffer}STOP")
      else
        val childCount = element.children.length
        println(s"$buffer${element.nodeName}($childCount)")
        element.children.foreach(e => loop(e, depth + 1))
    }
    println(">>>>>")
    loop(element)
    println("<<<<<")
  }