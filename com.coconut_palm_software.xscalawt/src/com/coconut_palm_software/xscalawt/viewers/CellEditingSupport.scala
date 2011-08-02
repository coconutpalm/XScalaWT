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
import org.eclipse.jface.viewers.EditingSupport
import org.eclipse.jface.viewers.ColumnViewer
import org.eclipse.jface.viewers.CellEditor

class CellEditingSupport[A, B](viewer: ColumnViewer, editor: CellEditor, valueGetter: A => B, valueSetter: (A, B) => Unit) extends EditingSupport(viewer) {
  def getCellEditor(element: AnyRef) = editor
  
  def canEdit(element: AnyRef) = true
  
  def getValue(element: AnyRef) = {
    try {
      valueGetter(element.asInstanceOf[A]).asInstanceOf[AnyRef]
    } catch {
      case _ => null
    }
  }
  
  def setValue(element: AnyRef, value: AnyRef) {
    valueSetter(element.asInstanceOf[A], value.asInstanceOf[B])
    viewer.refresh(element)
  }
}