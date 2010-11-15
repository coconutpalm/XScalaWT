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

import org.eclipse.swt.widgets._
import org.eclipse.swt.layout._
import org.eclipse.jface.layout._

/**
 * XScalaWTSetups is a utility object containing functions making it easier for
 * clients to extend XScalaWT.
 */
object XScalaWTAPI {
  /**
   * def setup[T].  Set up a new control and call all of its setup functions.
   * 
   * T is the control class
   * @param control the SWT Control
   * @param setups A vararg list of setup functions of the type (T => Unit)
   * @returns T the T that was set up
   */
  def setupAndReturn[T](control : T, setups:(T => Unit)*) : T = {
    setups.foreach(setup => setup(control))

    if (control.isInstanceOf[Composite]) {
      val composite = control.asInstanceOf[Composite]
      if (composite.getLayout().isInstanceOf[GridLayout]) {
        composite.getChildren.foreach { child =>
          if (child.getLayoutData == null) {
            GridDataFactory.defaultsFor(child).applyTo(child)
          }
        }
      }
    }
    control
  }
}
