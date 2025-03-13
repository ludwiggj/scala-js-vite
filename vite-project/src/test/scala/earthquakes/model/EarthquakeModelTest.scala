package earthquakes.model

import munit.FunSuite

class EarthquakeModelTest extends FunSuite:
    test("flip between earthquakesAndErrors") {
      val em = EarthquakeModel()

      val noEarthquakes = List.empty[Earthquake]
      val noError = ""

      assertEquals(em.earthquakeSignal.now(), noEarthquakes)
      assertEquals(em.errorSignal.now(), noError)

      val singleEarthquake = Seq(Earthquake(magnitude = 9, place = "Ipswich, UK", time = 1741379415000L))
      em.setResponse(Right(singleEarthquake))

      assertEquals(em.earthquakeSignal.now(), singleEarthquake)
      assertEquals(em.errorSignal.now(), noError)

      val firstError = "Sensors broken"
      em.setResponse(Left(firstError))

      assertEquals(em.earthquakeSignal.now(), noEarthquakes)
      assertEquals(em.errorSignal.now(), firstError)

      val doubleEarthquake = Seq(
        Earthquake(magnitude = 9.5, place = "London, UK", time = 1741379666000L),
        Earthquake(magnitude = 8.7, place = "Norwich, UK", time = 1640433657000L)
      )
      em.setResponse(Right(doubleEarthquake))

      assertEquals(em.earthquakeSignal.now(), doubleEarthquake)
      assertEquals(em.errorSignal.now(), noError)

      val secondError = "Orange alert"
      em.setResponse(Left(secondError))

      assertEquals(em.earthquakeSignal.now(), noEarthquakes)
      assertEquals(em.errorSignal.now(), secondError)
    }
end EarthquakeModelTest