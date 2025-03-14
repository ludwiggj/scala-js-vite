package laminar.domtestutils.scalatest

import com.raquo.domtestutils.scalatest.MountSpec
import laminar.domtestutils.UnitSpec
import org.scalatest.funspec.AnyFunSpec

trait ScalaTestSpec extends AnyFunSpec with UnitSpec with MountSpec
