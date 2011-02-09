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
package examples

import XScalaWT._
import Assignments._

import org.eclipse.swt.layout._
import org.eclipse.swt.widgets._
import org.eclipse.swt.events._
import org.eclipse.swt.SWT

/*
 * What's left?
 * <li> Table / TableColumn / TableItem
 * <li> TableTree, etc
 * <li> Tree / TreeItem
 * <li> Menu / MenuItem
 * <li> ToolBar / ToolItem
 * <li> functions and implicits to make events easy like already done with SelectionEvent
 * <li> data binding!
 * <li> JFace?
 */
object SWTControls {
  def main(args : Array[String]) : Unit = {
    
    val window = shell("SWT Controls",
      layout = new FillLayout(),
      
      tabFolder (
        scrolledCompositeVertical (
          tabItem("Simple controls"),

          composite (
            group("Bunch o' controls",
              label("Hello, world"),
              button("Push me", { e : SelectionEvent => println("Bonk!") }),
              link("http://www.google.com")
            ),
            progressBar(
              minimum = 0,
              maximum = 50,
              selection = 20
            ),
//            animatedProgress(
//              _.start()
//            ),
            scaleHorizontal(
              minimum = 0,
              maximum = 30,
              selection = 5
            ),
            scaleVertical(
              minimum = 0,
              maximum = 40,
              selection = 5
            ),
            cLabel("Hello, CLabel"),
//            cCombo(),
            combo(),
            dateTime(),
            spinner(
              selection = 3
            ),
            list(
              _.add("One"),
              _.add("Two"),
              _.add("Three"),
              selection = 1
            ),
            text("Edit me, please"),
            sliderHorizontal(),
            sliderVertical()
          )
        ),
        
        expandBar(
          tabItem("ExpandBar"),
          
          composite (
            label("Some progress meters"),
            progressBar(
              minimum = 0,
              maximum = 50,
              selection = 20
            ),
//            animatedProgress(
//              _.start()
//            ),
            expandItem("Item 1")
          ),
          button("Bonk", expandItem("Item 2"))
        ),
        
        composite (
          tabItem("CoolBar"),
          
          coolBar(
            layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false),
            
            button("one", coolItem()),
            button("two", coolItem()),
            button("three", coolItem()),
            composite(
              layout = new RowLayout(),
              button("four"),
              button("five"),
              button("six"),
              button("seven"),
              coolItem()
            )
          )
        ),
        
        sashForm (
          tabItem("SashForm"),
          
          button("Pane 1"),
          button("Pane 2"),
          button("Pane 3"),
          
          _.setWeights(Array(30, 40, 30))
        ),
        
        browser (
          tabItem("Browser"),
          
          _.setUrl("http://www.google.com")
        )
        
//        table (
//          tabItem("Table"),
//        )
      )
    )
    
    runEventLoop(window)
  }
}
