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
package com.coconut_palm_software.xscalawt.examples

import com.coconut_palm_software.xscalawt.XScalaWT._
import com.coconut_palm_software.xscalawt.XScalaWTStyles._

import org.eclipse.swt.SWT
import org.eclipse.swt.widgets._
import org.eclipse.swt.layout._
import org.eclipse.swt.events._

object LoginBoxStyled {
  def main(args : Array[String]) : Unit = {
	var passwordLabel : Label = null
    
    val INVALID_INPUT="INVALID_INPUT"

    object loginStyles extends Stylesheet(
	  $[Control] (
	    setBackground(SWT.COLOR_WHITE)
	  ),
	  
	  $class[Control](INVALID_INPUT) (
	    setForeground(SWT.COLOR_RED)
	  )
	)
    
    val window = shell("Please log in",
      group("User information",
        setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false)),
        setLayout(new GridLayout(1, false)),
      
        label("Username"),
        text (
          setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false))
        ),

        label(""),

        label("Password", passwordLabel = _),
        textPasswd (
          setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false))
        )
      ),
      
      // OK/Cancel buttons
      composite (
        setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false)),
        setLayout(new GridLayout(2, true)),

        button("OK", { e : SelectionEvent => passwordLabel.setStyleClass(INVALID_INPUT) }),
        button("Cancel", { e : SelectionEvent => e.widget.asInstanceOf[Button].getShell.close() } )
      )
    )
    loginStyles.apply(window)

    window.pack
    val size = window.getSize()
    window.setSize(250, size.y)

    runEventLoop(window)
  }
}
