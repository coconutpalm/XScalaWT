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

import org.eclipse.jface.viewers.TableViewer
import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.widgets.Table
import org.eclipse.swt.widgets.TableColumn
import org.eclipse.jface.viewers.TreeViewer
import org.eclipse.swt.widgets.TreeColumn

/**
 * TableSortSelectionListener is a selection listener for {@link TableColumn}
 * objects. When a column is selected (= header is clicked), it switches the
 * sort direction if the column is already active sort column, otherwise it sets
 * the active sort column.
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
final class TableSortSelectionListener(viewer: TableViewer) extends SelectionAdapter {
  override def widgetSelected(e: SelectionEvent) {
    val column = e.getSource.asInstanceOf[TableColumn]
    val table = column.getParent
    val alreadyActiveSortColumn = (column == table.getSortColumn)
    if (alreadyActiveSortColumn) {
      table.setSortDirection(if (table.getSortDirection() == SWT.DOWN) SWT.UP else SWT.DOWN);
    } else {
      table.setSortColumn(column);
      table.setSortDirection(SWT.UP);
    }
    viewer.refresh();
  }
}

/**
 * TreeSortSelectionListener is a selection listener for {@link TreeColumn}
 * objects. When a column is selected (= header is clicked), it switches the
 * sort direction if the column is already active sort column, otherwise it sets
 * the active sort column.
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
final class TreeSortSelectionListener(viewer: TreeViewer) extends SelectionAdapter {
  override def widgetSelected(e: SelectionEvent) {
    val column = e.getSource.asInstanceOf[TreeColumn]
    val table = column.getParent
    val alreadyActiveSortColumn = (column == table.getSortColumn)
    if (alreadyActiveSortColumn) {
      table.setSortDirection(if (table.getSortDirection() == SWT.DOWN) SWT.UP else SWT.DOWN);
    } else {
      table.setSortColumn(column);
      table.setSortDirection(SWT.UP);
    }
    viewer.refresh();
  }
}
