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
import org.eclipse.jface.viewers.ViewerComparator
import org.eclipse.jface.viewers.Viewer
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Table
import org.eclipse.swt.widgets.Tree
import org.eclipse.swt.widgets.Item

private[viewers] class SortColumnComparator extends ViewerComparator {
  override def compare(viewer: Viewer, e1: Object, e2: Object) = {
    viewer.getControl match {
      case table: Table =>
        val column = table.getSortColumn
        table.getSortDirection match {
          case SWT.NONE => 0
          case SWT.DOWN => doCompare(column, e2, e1)
          case SWT.UP => doCompare(column, e1, e2)
        }
      case tree: Tree =>
        val column = tree.getSortColumn
        tree.getSortDirection match {
          case SWT.NONE => 0
          case SWT.DOWN => doCompare(column, e2, e1)
          case SWT.UP => doCompare(column, e1, e2)
        }
      case _ =>
        throw new IllegalArgumentException("viewer control is not table or tree")
    }
  }
  
  private def doCompare(column: Item, e1: Object, e2: Object) = {
    val ordering = column.getData(SortColumnComparator.SORT_BY).asInstanceOf[Ordering[Object]]
    ordering.compare(e1, e2)
  }
}

private[viewers] object SortColumnComparator {
  val SORT_BY = "com.coconut_palm_software.xscalawt.viewers.sort_by"
}