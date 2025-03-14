package laminar.domtestutils.munit

import com.raquo.domtestutils.MountOps
import laminar.domtestutils.UnitSpec
import laminar.domtestutils.matching.AssertSyntax
import munit.{FunSuite, Location}
import org.scalactic.Prettifier
import org.scalactic.source.Position

trait MUnitSpec extends FunSuite with UnitSpec with MountOps:
  given genericToAssertSyntax[A]: Conversion[A, AssertSyntax[A]] with
    def apply(v: A): AssertSyntax[A] = new AssertSyntax(v)

  // If condition is false, fail the test with a given message This method exists for compatibility with different test frameworks.
  override def doAssert(condition: Boolean, message: String)(implicit prettifier: Prettifier, pos: Position): Unit =
    assert(condition, message)(loc = Location(path = pos.filePathname, line = pos.lineNumber))

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