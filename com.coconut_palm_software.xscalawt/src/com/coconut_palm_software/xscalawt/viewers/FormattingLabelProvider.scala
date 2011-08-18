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
import org.eclipse.jface.viewers.CellLabelProvider
import org.eclipse.jface.viewers.ViewerCell

class FormattingLabelProvider[A, B](valueGetter: A => B, formatter: CellFormatter[B]) extends CellLabelProvider {
  def update(cell: ViewerCell) {
    val value = valueGetter(cell.getElement.asInstanceOf[A])
    formatter.formatCell(cell, value)
  }
}