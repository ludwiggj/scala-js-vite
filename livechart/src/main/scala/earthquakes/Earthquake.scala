package earthquakes

import io.circe.*
import io.circe.Decoder.decodeSeq

case class Earthquake(magnitude: BigDecimal, place: String, time: Long)

object Earthquake {
  given Decoder[Earthquake] = Decoder.instance { c =>
      val cProps = c.downField("properties")
      for {
        magnitude <- cProps.downField("mag").as[BigDecimal]
        place <- cProps.downField("place").as[String]
        time <- cProps.downField("time").as[Long]
      } yield Earthquake(magnitude, place, time)
    }

  // Explicitly pass Earthquake decoder to avoid recursive definition
  given Decoder[Seq[Earthquake]] = Decoder[Seq[Earthquake]](using decodeSeq(given_Decoder_Earthquake)).prepare(
  _.downField("features")
  )
}