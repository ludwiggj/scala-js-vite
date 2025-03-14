package earthquakes.model

import io.circe.*
import munit.FunSuite

class EarthquakeTest extends FunSuite:
    test("increment with initial step") {
        val earthquakeData =
                s"""
                  {
                    "features": [
                      {
                        "type": "Feature",
                        "properties": {
                          "mag": 6.1,
                          "place": "South Sandwich Islands region",
                          "time": 1735739304752,
                          "updated": 1739271898443,
                          "tz": null,
                          "url": "https://earthquake.usgs.gov/earthquakes/eventpage/us6000pgv4",
                          "detail": "https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us6000pgv4&format=geojson",
                          "felt": null,
                          "cdi": null,
                          "mmi": 4.22,
                          "alert": "green",
                          "status": "reviewed",
                          "tsunami": 0,
                          "sig": 572,
                          "net": "us",
                          "code": "6000pgv4",
                          "ids": ",us6000pgv4,usauto6000pgv4,pt25001000,at00spex0r,",
                          "sources": ",us,usauto,pt,at,",
                          "types": ",ground-failure,internal-moment-tensor,internal-origin,losspager,moment-tensor,origin,phase-data,shakemap,",
                          "nst": 216,
                          "dmin": 5.973,
                          "rms": 0.59,
                          "gap": 28,
                          "magType": "mww",
                          "type": "earthquake",
                          "title": "M 6.1 - South Sandwich Islands region"
                        },
                        "geometry": {
                          "type": "Point",
                          "coordinates": [
                            -26.7059,
                            -56.4095,
                            70
                          ]
                        },
                        "id": "us6000pgv4"
                      },
                      {
                        "type": "Feature",
                        "properties": {
                          "mag": 5.5,
                          "place": "153 km S of Isangel, Vanuatu",
                          "time": 1735706358208,
                          "updated": 1738973297523,
                          "tz": null,
                          "url": "https://earthquake.usgs.gov/earthquakes/eventpage/us6000pgsd",
                          "detail": "https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us6000pgsd&format=geojson",
                          "felt": 1,
                          "cdi": 3.4,
                          "mmi": 3.56,
                          "alert": "green",
                          "status": "reviewed",
                          "tsunami": 0,
                          "sig": 466,
                          "net": "us",
                          "code": "6000pgsd",
                          "ids": ",us6000pgsd,usauto6000pgsd,",
                          "sources": ",us,usauto,",
                          "types": ",dyfi,internal-moment-tensor,losspager,moment-tensor,origin,phase-data,shakemap,",
                          "nst": 152,
                          "dmin": 2.459,
                          "rms": 0.56,
                          "gap": 81,
                          "magType": "mww",
                          "type": "earthquake",
                          "title": "M 5.5 - 153 km S of Isangel, Vanuatu"
                        },
                        "geometry": {
                          "type": "Point",
                          "coordinates": [
                            169.3819,
                            -20.9273,
                            22
                          ]
                        },
                        "id": "us6000pgsd"
                      }
                    ],
                    "bbox": [
                      -100.6636,
                      -56.4095,
                      10,
                      169.3819,
                      -3.7219,
                      70
                    ]
                  }
                  """
      
        val result = parser.parse(earthquakeData).flatMap(_.as[Seq[Earthquake]])

        assert(result == Right(
            List(
              Earthquake(6.1, "South Sandwich Islands region", 1735739304752L),
              Earthquake(5.5, "153 km S of Isangel, Vanuatu", 1735706358208L)
            )
          )
        )
    }
end EarthquakeTest