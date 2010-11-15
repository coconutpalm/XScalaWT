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
import org.eclipse.swt.layout._
import org.eclipse.swt.SWT
import org.eclipse.swt.graphics._

import XScalaWTAPI._

import java.util.Iterator

import XScalaWTAPI._

object XScalaWTForms {
  class CompositeFillHorizontal(parent: Composite, style: Int) extends Composite(parent, style) {
    override def computeSize(wHint : Int, hHint : Int, changed: Boolean) : Point = {
      val result = super.computeSize(wHint, hHint, changed)
      result.x = parent.getSize.x
      result
    }
  }

  /**
   * A Composite intended for use within a vertical ScrolledComposite that knows how to fill the
   * entre horizontal space alotted.
   */
  def compositePreferredWidthFillsParent(setups:(CompositeFillHorizontal => Unit)*)(parent : Composite) = {
    setupAndReturn(new CompositeFillHorizontal(parent, SWT.NULL), setups : _*)
  }

  
  def dataCompositesForAll[T](collection : Iterable[T])(content : ( (Composite, T) => Unit))(parent : Composite) : Composite = {
    collection.foreach {
      element =>
      val row = new Composite(parent, SWT.NULL)
      content(row, element)
    }
    parent
  }
}
