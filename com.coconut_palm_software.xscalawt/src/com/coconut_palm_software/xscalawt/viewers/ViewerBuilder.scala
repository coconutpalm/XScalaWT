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
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.SWT
import org.eclipse.jface.layout.AbstractColumnLayout
import org.eclipse.jface.viewers.ColumnViewer
import org.eclipse.jface.layout.TableColumnLayout
import org.eclipse.jface.viewers.TableViewer
import org.eclipse.jface.viewers.TreeViewer
import org.eclipse.jface.layout.TreeColumnLayout
import org.eclipse.swt.events.SelectionListener

/**
 * Base class for Table/TreeViewerBuilder
 * 
 * @tparam A type of elements shown in the viewer
 */
abstract class ViewerBuilder[A](parent: Composite, style: Int) {
  // or we could check if parent has no children and no layout set,
  // but this is more reliable
  val composite = new Composite(parent, SWT.NONE)
  
  def layout: AbstractColumnLayout
  
  def viewer: ColumnViewer
  def control: Composite
  
  def createColumn[B](getter: A => B, header: String): ViewerColumnBuilder[A, B]
  
  def sortSelectionListener: SelectionListener
}

class TableViewerBuilder[A](parent: Composite, style: Int, useHashLookup: Boolean = true, headerVisible: Boolean = true, linesVisible: Boolean = true) extends ViewerBuilder[A](parent, style) {
  val viewer: TableViewer = new TableViewer(composite, style)
  val table = viewer.getTable
  def control = table
  val sortSelectionListener = new TableSortSelectionListener(viewer)
  
  val layout: TableColumnLayout = new TableColumnLayout
  composite.setLayout(layout)
  table.setHeaderVisible(headerVisible)
  table.setLinesVisible(linesVisible)
  viewer.setUseHashlookup(useHashLookup)
  viewer.setComparator(new SortColumnComparator)
  
  def createColumn[B](getter: A => B, header: String): TableViewerColumnBuilder[A, B] =
    new TableViewerColumnBuilder(this, getter, header)
}

class TreeViewerBuilder[A](parent: Composite, style: Int, useHashLookup: Boolean = true, headerVisible: Boolean = true, linesVisible: Boolean = true) extends ViewerBuilder[A](parent, style) {
  val viewer: TreeViewer = new TreeViewer(composite, style)
  val tree = viewer.getTree
  def control = tree
  val sortSelectionListener = new TreeSortSelectionListener(viewer)
  
  val layout: TreeColumnLayout = new TreeColumnLayout
  composite.setLayout(layout)
  tree.setHeaderVisible(headerVisible)
  tree.setLinesVisible(linesVisible)
  viewer.setUseHashlookup(useHashLookup)
  viewer.setComparator(new SortColumnComparator)
  
  def createColumn[B](getter: A => B, header: String): TreeViewerColumnBuilder[A, B] =
    new TreeViewerColumnBuilder(this, getter, header)
}