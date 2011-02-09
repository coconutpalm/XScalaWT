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
package examples.binding

import XScalaWT._
import XScalaWTBinding._
import XScalaWTStyles._
import Assignments._

import org.eclipse.swt.SWT
import org.eclipse.swt.widgets._
import org.eclipse.swt.layout._
import org.eclipse.swt.events._

class LoginView(loginData : LoginViewModel) {

  def createDialog = {
    object loginStyles extends Stylesheet(
	  $[Control] (
	    background = SWT.COLOR_WHITE
	  )
	)
    
    val window = shell("Please log in",
      dataBindingContext(),
      group("User information",
        layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false),
        layout = new GridLayout(1, false),
      
        label("Username"),
        text (
          layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false),
          _.textObservable <=> (loginData-->'username)
        ),

        label(""),

        label("Password"),
        textPasswd (
          layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false),
          _.textObservable <=> (loginData-->'password)
        )
      ),
      
      // OK/Cancel buttons
      composite (
        layoutData = new GridData(SWT.CENTER, SWT.CENTER, false, false),
        layout = new GridLayout(2, true),

        button("&OK", {e : SelectionEvent => loginData.login(); closeShell(e) }),
        button("&Cancel", {e : SelectionEvent => closeShell(e) })
      )
    )
    loginStyles.apply(window)
    
    window.pack()
    val size = window.getSize()
    window.setSize(250, size.y)
    
    window
  }
  
  private def closeShell(e : SelectionEvent) = e.widget.asInstanceOf[Control].getShell().close()
}
