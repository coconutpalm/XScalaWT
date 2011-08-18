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
import XJFace._
import org.eclipse.swt.layout._
import org.eclipse.swt.widgets._
import org.eclipse.swt.events._
import org.eclipse.swt.SWT
import org.eclipse.jface.viewers.ITreeContentProvider
import org.eclipse.jface.viewers.IStructuredContentProvider
import org.eclipse.jface.viewers.Viewer

trait Place {
  def name: String
  def population: Int
}

object Model {
  case class City(name: String, population: Int, country: Country) extends Place
  case class Country(name: String, population: Int) extends Place
  // build model (data from Wikipedia
  val Russia = Country("Russia", 142905200)
  val Germany = Country("Germany", 81751602)
  val Norway = Country("Norway", 4960300)
  val countries = Array[Place](Russia, Germany, Norway)

  val Moscow = City("Moscow", 11514330, Russia)
  val Stpetersburg = City("Saint Petersburg", 4848742, Russia)
  val Novosibirsk = City("Novosibirsk", 1473737, Russia)

  val Berlin = City("Berlin", 3459218, Germany)
  val Hamburg = City("Hamburg", 1786278, Germany)
  val Munchen = City("München", 1330440, Germany)

  val Oslo = City("Oslo", 590041, Norway)
  val Bergen = City("Bergen", 223593, Norway)
  val Trondheim = City("Trondheim", 156794, Norway)
  val cities = Array(Moscow, Stpetersburg, Novosibirsk, Berlin, Hamburg, Munchen, Oslo, Bergen, Trondheim)
  
  object TreeProvider extends ITreeContentProvider with ProviderImplicits[City] {
    def getElements(i: Object) = countries.asInstanceOf[Array[Object]]

    def getChildren(place: Object) = place match {
      case c: Country => cities.filter(_.country == c)
      case _ => Array()
    }
    
    def getParent(place: Object) = place match {
      case City(_, _, country) => country
      case _ => null
    }
    
    def hasChildren(place: Object) = !getChildren(place).isEmpty
    
    def inputChanged(v: Viewer, oldInput: Object, newInput: Object) {}
    
    def dispose() {}
  }
  
  object TableProvider extends IStructuredContentProvider with ProviderImplicits[City] {
    def getElements(i: Object) = cities    
    
    def inputChanged(v: Viewer, oldInput: Object, newInput: Object) {}
    
    def dispose() {}
  }
}

object ViewerBuilders {
  def main(args : Array[String]) : Unit = {
    
    val window = shell("Viewer builders",
      _.setSize(200, 400),
      fillLayout(),
      
      tabFolder(
        composite(
          tabItem("Table builder"),
          fillLayout(),
          tableViewerBuilder[Place](
            _.createColumn(_.name, "Name").sortable.useAsDefaultSortColumn.build(),
            _.createColumn(_.population, "Population").sortable.build()
          )(
            _.setContentProvider(Model.TableProvider),
            _.setInput()
          )
        ),
        composite(
          tabItem("Tree builder"),
          fillLayout(),
          treeViewerBuilder[Place](
            _.createColumn(_.name, "Name").sortable.build(),
            _.createColumn(_.population, "Population").sortable.build()
          )(
            _.setContentProvider(Model.TreeProvider),
            _.setInput()
          )
        )
      )
    )
    
    runEventLoop(window)
  }
}
