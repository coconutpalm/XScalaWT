/*******************************************************************************
 * Copyright (c) 2008 Ralf Ebert
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Ralf Ebert - initial API and implementation
 * Alexey Romanov - generalization and translation to Scala 
 *******************************************************************************/
package com.coconut_palm_software.xscalawt.viewers

import org.eclipse.jface.viewers.ViewerCell

/**
 * A CellFormatter is responsible for formatting a cell. Should be used to
 * apply additional formatting to the cell, like setting colors / images.
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
trait CellFormatter[B] {
  def formatCell(cell: ViewerCell, value: B)
}

object CellFormatter {
  def apply[B]: CellFormatter[B] = apply(_.toString) 
  
  def apply[B](f: B => String): CellFormatter[B] = new CellFormatter[B] {
    def formatCell(cell: ViewerCell, value: B) {
      cell.setText(f(value))
    }
  }
  
  implicit def cellFormatter[B](f: B => String): CellFormatter[B] = apply(f)
}