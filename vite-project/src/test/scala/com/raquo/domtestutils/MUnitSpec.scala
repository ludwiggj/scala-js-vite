package com.raquo.domtestutils

import munit.{BaseFunSuite, FunSuite, Location}
import org.scalactic.Prettifier
import org.scalactic.source.Position

trait MUnitMountSpec extends BaseFunSuite with MountOps:
  // If condition is false, fail the test with a given message This method exists for compatibility with different test frameworks.
  override def doAssert(condition: Boolean, message: String)(implicit prettifier: Prettifier, pos: Position): Unit =
    assert(condition)(loc = Location(path = pos.filePathname, line = pos.lineNumber))

  // Fail the test with a given message This method exists for compatibility with different test frameworks.
  override def doFail(message: String)(implicit pos: Position): Nothing =
    fail(message = message)(loc = Location(path = pos.filePathname, line = pos.lineNumber))

  // Runs in the beginning of each test
  override def beforeEach(context: BeforeEach): Unit = {
    resetDOM("withFixture-begin")
    super.beforeEach(context)
  }

  // Runs at the end of each test, regardless of the result
  override def afterEach(context: AfterEach): Unit = {
    clearDOM("withFixture-end")
    super.afterEach(context)
  }
  
class MUnitSpec extends FunSuite with UnitSpec with MUnitMountSpec
// TODO - Test is "ignored"
//        Move these to forked version of domtestutils? 