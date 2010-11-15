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

import org.eclipse.swt.SWT
import org.eclipse.swt.widgets._
import org.eclipse.swt.layout._
import org.eclipse.swt.events._

object LoginBox {
  val modifyListener = new ModifyListener {
    def modifyText(e : ModifyEvent) = {
      println(e.widget.asInstanceOf[Text].getText)
    }
  }
  
  def main(args : Array[String]) : Unit = {
    var username : Text = null
    
    val window = shell("Please log in",
      
      // username/password group
      group("User information",
        setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false)),
        setLayout(new GridLayout(1, false)),
      
        label("Username"),
        text (
          username=_,       // Save the username Text widget for later, say for data binding...
          setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false)),
          _.addModifyListener(modifyListener)    // or we could data bind right here if we want to...
        ),

        label(""),

        label("Password"),
        textPasswd (
          setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false)),
          _.addModifyListener(modifyListener)
        )
      ),
      
      // OK/Cancel buttons
      composite (
        setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false)),
        setLayout(new GridLayout(2, true)),

        button("OK", 
          setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL)),
          { e : SelectionEvent => println("OK") }
        ),
        button("Cancel", 
          setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL)),
          { e : SelectionEvent => println("Cancel") }
        )
      ),
      
      // Set a default shell size
      { window =>
        window.pack
        val size = window.getSize()
        window.setSize(250, size.y)
      }
    )
    runEventLoop(window)
  }
}
