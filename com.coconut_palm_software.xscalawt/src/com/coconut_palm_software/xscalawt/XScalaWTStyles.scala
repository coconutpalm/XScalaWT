/*******************************************************************************
 * Copyright (c) 2009 David Orme and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     David Orme - initial API and implementation
 *******************************************************************************/
package com.coconut_palm_software.xscalawt

import org.eclipse.swt.widgets.Widget
import org.eclipse.swt.widgets.Composite

import scala.language.experimental.macros
import scala.language.implicitConversions
import scala.reflect.macros.blackbox

object XScalaWTStyles {
  private object XScalaWTStylesMacros {
    private def widgetFn[T <: Widget : c.WeakTypeTag](c: blackbox.Context)(setups: c.Tree*)
                                                     (cb: (c.universe.TermName, c.Tree) => c.Tree) = {
      import c.universe._

      val t = implicitly[c.WeakTypeTag[T]].tpe
      val widget = TermName(c.freshName("widget"))
      val typedWidget = TermName(c.freshName("typedWidget"))

      val body = q"""..${for(setup <- setups) yield q"""${c.untypecheck(setup)}($typedWidget)"""}"""
      q"""
        ($widget: _root_.org.eclipse.swt.widgets.Widget) =>
          if($widget.isInstanceOf[$t]) {
            val $typedWidget = $widget.asInstanceOf[$t]
            ${cb(typedWidget, body)}
          }
      """
    }

    def dollar_impl[T <: Widget : c.WeakTypeTag](c: blackbox.Context)(setups: c.Tree*) =
      widgetFn(c)(setups : _*)((name, tree) => tree)

    def dollar_class_impl[T <: Widget : c.WeakTypeTag](c: blackbox.Context)(nameParam: c.Tree)(setups: c.Tree*) = {
      import c.universe._

      val name = TermName(c.freshName("name"))
      q"""
        val $name = $nameParam
        ${widgetFn(c)(setups : _*)((typedWidget, tree) => {
          val id = TermName(c.freshName("id"))
          q"""
             val $id = $typedWidget.getData(_root_.com.coconut_palm_software.xscalawt.XScalaWTStyles.XScalaWTClass)
             if($id != null && $id != $name) $tree
           """
        })}
       """
    }
  }

  val XScalaWTID = "XSCALAWT_ID"
  val XScalaWTClass = "XSCALAWT_CLASS"

  /**
   * Define a style that applies for all subclasses of a particular type
   */
  def $[T <: Widget](setups: (T => Any)*) : Widget => Unit =
    macro XScalaWTStylesMacros.dollar_impl[T]

  /**
   * Style classes apply to all subclasses of a given type that are assigned a
   * specific name using:
   * 
   * Widget#setStyleClass
   */
  def $class[T <: Widget](nameParam: String)(setups: (T => Any)*) : Widget => Unit =
    macro XScalaWTStylesMacros.dollar_class_impl[T]
  
  /**
   * Define a stylesheet, which is a collection of styles applied to a widget
   */
  class Stylesheet(val styles : (Widget => Unit)*) {
    def apply(widget : Widget) {
      styles.foreach(style => style(widget))
      widget.setData(XScalaWTID, this)
      widget match {
        case container: Composite =>
          container.getChildren.foreach(control => apply(control))
        case _ =>
      }
    }
  }
  
  def stylesheet(widget : Widget)(styles : (Widget => Unit)*) = {
    val stylesheet = new Stylesheet(styles : _*)
    stylesheet.apply(widget)
  }

  // Add a setStyleClass to all subclasses of Widget
  class WidgetStyleClass(widget : Widget) {
    def setStyleClass(className : String) = {
      widget.setData(XScalaWTClass, className)

      val maybestylesheet = widget.getData(XScalaWTID)
      if (maybestylesheet != null) {
        val styles = maybestylesheet.asInstanceOf[Stylesheet]
        styles.apply(widget)
      }
    }
  }
  
  implicit def widget2widgetStyleClass(widget : Widget): WidgetStyleClass = new WidgetStyleClass(widget)
}
