package com.raquo.domtestutils

import com.raquo.domtestutils.matching.*
import com.raquo.laminar.keys.{HtmlAttr, HtmlProp, StyleProp, SvgAttr}
import com.raquo.laminar.nodes.CommentNode
import com.raquo.laminar.tags.Tag
import scala.language.implicitConversions

trait UnitSpec extends RuleImplicits[Tag.Base, CommentNode, HtmlProp, HtmlAttr, SvgAttr, StyleProp] {

  override implicit def makeTagTestable(tag: Tag.Base): ExpectedNode = {
    ExpectedNode.element(tag.name)
  }

  override implicit def makeCommentBuilderTestable(commentBuilder: () => CommentNode): ExpectedNode = {
    ExpectedNode.comment
  }

  override implicit def makeAttrTestable[V](attr: HtmlAttr[V]): TestableHtmlAttr[V] = {
    new TestableHtmlAttr[V](attr.name, attr.codec.encode, attr.codec.decode)
  }

  override implicit def makePropTestable[V, DomV](prop: HtmlProp[V, DomV]): TestableProp[V, DomV] = {
    new TestableProp[V, DomV](prop.name, prop.codec.decode)
  }

  override implicit def makeStyleTestable[V](style: StyleProp[V]): TestableStyleProp[V] = {
    new TestableStyleProp[V](style.name)
  }

  // Changed last parameter from `svgAttr.namespace` to None to remove infinite loop warning ¯\_(ツ)_/¯
  override implicit def makeSvgAttrTestable[V](svgAttr: SvgAttr[V]): TestableSvgAttr[V] = {
    new TestableSvgAttr[V](svgAttr.name, svgAttr.codec.encode, svgAttr.codec.decode, None)
  }

  // Taken from LaminarSpec
  val sentinel: ExpectedNode = ExpectedNode.comment
}