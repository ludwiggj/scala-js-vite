package com.raquo.domtestutils.matching

import munit.Assertions.assertEquals
import munit.Compare.compareSupertypeWithSubtype
import munit.{Compare, Location}

class AssertSyntax[A](val actual: A) {
  def shouldBe[B](expected: B)(implicit loc: Location, ev: B <:< A): Unit = {
    assertEquals(actual, expected)
  }
}
