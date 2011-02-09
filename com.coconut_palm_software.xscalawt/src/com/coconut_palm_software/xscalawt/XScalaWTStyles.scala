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

import reflect.ClassManifest
import org.eclipse.swt.widgets.Widget
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Display

object XScalaWTStyles {

  val XScalaWTID = "XSCALAWT_ID"
  val XScalaWTClass = "XSCALAWT_CLASS"

  /**
   * A type for all Style objects
   */
  sealed abstract class Style {
    def apply(control : Widget)
  }
  
  /**
   * Things like:
   * $[Composite](
   *    _.setBackground(green)
   * )
   */
  private class TypedStyle[T <: Widget](val styleType: java.lang.Class[T], selectors:(T => Any)*) 
    extends Style 
  {
    def apply(control : Widget) = {
      if (styleType.isAssignableFrom(control.getClass))
        selectors.foreach(selector => selector(control.asInstanceOf[T]))
    }
    def getType = styleType
  }
  
  /**
   * Define a style that applies for all subclasses of a particular type
   */
  def $[T <: Widget : ClassManifest](selectorList:(T => Any)*) : Style = {
    new TypedStyle[T](classManifest[T].erasure.asInstanceOf[Class[T]], selectorList : _*)
  }

  /**
   * Things like:
   * $class[Text]("Browse-Only") (
   *   _.setEnabled(false)
   * )
   */
  private class NamedStyle[T <: Widget](val name : String, override val styleType: java.lang.Class[T], val selectors:(T => Any)*) 
    extends TypedStyle (styleType, selectors : _*)
  {
    override def apply(control : Widget) = {
      val id = control.getData(XScalaWTClass)
      if (id != null && id == name)
        super.apply(control)
    }
  }

  /**
   * Style classes apply to all subclasses of a given type that are assigned a
   * specific name using:
   * 
   * Widget#setStyleClass
   */
  def $class[T <: Widget : ClassManifest](name : String)(selectorList:(T => Any)*) : Style = {
    new NamedStyle[T](name, classManifest[T].erasure.asInstanceOf[Class[T]], selectorList : _*)
  }
  
  /**
   * Define a stylesheet, which is a collection of styles applied to a widget
   */
  class Stylesheet(val styles : Style*) {
    def apply(widget : Widget) {
      styles.foreach(style => style(widget))
      widget.setData(XScalaWTID, this)
      if (widget.isInstanceOf[Composite]) {
        val container = widget.asInstanceOf[Composite]
        container.getChildren.foreach(control => apply(control))
      }
    }
  }
  
  def stylesheet(widget : Widget)(styles : Style*) = {
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
  
  implicit def widget2widgetStyleClass(widget : Widget) = new WidgetStyleClass(widget)
}
