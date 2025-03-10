package earthquakes.model

import com.raquo.laminar.api.L.{StrictSignal, Var}

class EarthquakeModel:
  private val earthquakesVar: Var[Seq[Earthquake]] = Var(Seq())
  val earthquakeSignal: StrictSignal[Seq[Earthquake]] = earthquakesVar.signal
  private val errorVar: Var[String] = Var("")
  val errorSignal: StrictSignal[String] = errorVar.signal

  def setResponse(response: Either[String, Seq[Earthquake]]): Unit = response match {
    case Right(earthquakes) =>
      Var.set(earthquakesVar -> earthquakes, errorVar -> "")
    case Left(error) =>
      Var.set(earthquakesVar -> Seq.empty, errorVar -> error)
  }
