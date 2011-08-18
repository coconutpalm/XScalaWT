/**
 * *****************************************************************************
 * Copyright (c) 2008 Ralf Ebert
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Ralf Ebert - initial API and implementation
 * Alexey Romanov - generalization and translation to Scala
 * *****************************************************************************
 */
package com.coconut_palm_software.xscalawt.viewers
import org.eclipse.jface.viewers.ViewerColumn
import org.eclipse.jface.viewers.CellEditor
import org.eclipse.jface.viewers.CellLabelProvider
import org.eclipse.jface.viewers.ColumnLayoutData
import org.eclipse.swt.SWT
import org.eclipse.jface.viewers.ColumnWeightData
import org.eclipse.jface.viewers.ColumnPixelData
import org.eclipse.jface.viewers.TextCellEditor
import org.eclipse.swt.widgets.Item
import org.eclipse.swt.events.SelectionListener
import org.eclipse.jface.viewers.TableViewerColumn
import org.eclipse.swt.widgets.TableColumn
import org.eclipse.jface.viewers.TreeViewerColumn
import org.eclipse.swt.widgets.TreeColumn

abstract class ViewerColumnBuilder[A, B](builder: ViewerBuilder[A], valueGetter: A => B, header: String) {
  protected var _labelProvider: CellLabelProvider = new FormattingLabelProvider(valueGetter, CellFormatter[B])
  protected var _layoutData: ColumnLayoutData = new ColumnPixelData(100)
  protected var _align: Int = SWT.LEFT
  protected var _editingSupport: Option[CellEditingSupport[A, B]] = None
  protected var _defaultSort: Boolean = false
  protected var _sortBy: Option[Ordering[B]] = None

  type Column <: ViewerColumn

  /**
   * Sets formatter
   */
  def format(formatter: CellFormatter[B]): this.type = {
    _labelProvider = new FormattingLabelProvider(valueGetter, formatter)
    this
  }

  /**
   * Sets label provider (formatter will be ignored)
   */
  def customLabelProvider(labelProvider: CellLabelProvider): this.type = {
    _labelProvider = labelProvider
    this
  }

  def width_%(percent: Int): this.type = {
    _layoutData = new ColumnWeightData(percent)
    this
  }

  def width_px(pixels: Int): this.type = {
    _layoutData = new ColumnPixelData(pixels)
    this
  }

  def alignLeft: this.type = {
    _align = SWT.LEFT
    this
  }

  def alignCenter: this.type = {
    _align = SWT.CENTER
    this
  }

  def alignRight: this.type = {
    _align = SWT.RIGHT
    this
  }

  /**
   * Makes the column cells editable.
   * 
   * @param valueSetter sets the property of the object corresponding to the row
   * 
   * @param cellEditor the cell editor. Must belong to the builder's 
   * control (table or tree). By default TextCellEditor is used.
   * 
   * @param editorValueGetter converts the value set by the editor into a B to 
   * call valueSetter. By default this is a cast.
   */
  def editable(valueSetter: (A, B) => Unit, cellEditor: CellEditor = new TextCellEditor(builder.control), editorValueGetter: AnyRef => B = (_: AnyRef).asInstanceOf[B]): this.type = {
    if (cellEditor.getControl.getParent != builder.control) {
      throw new RuntimeException("Parent of cell editor needs to be the builder's control!")
    }
    _editingSupport = Some(new CellEditingSupport(builder.viewer, cellEditor, valueGetter, valueSetter, editorValueGetter))
    this
  }

  /**
   * Makes the column sortable
   * 
   * @param ord the ordering to use
   */
  def sortable(implicit ord: Ordering[B]): this.type = {
    _sortBy = Some(ord)
    this
  }

  /**
   * Sets this column as the default sort column.
   * 
   * If called on several column builders, the last one wins.
   */
  def useAsDefaultSortColumn(): this.type = {
    _defaultSort = true
    this
  }

  protected def createColumn(): Column
  protected def baseColumn(column: Column): Item
  protected def makeColumnSortable(column: Column, ord: Ordering[B])
  protected def afterColumnCreated(column: Column) {
    val baseColumn = this.baseColumn(column)

    baseColumn.setText(header)
    builder.layout.setColumnData(baseColumn, _layoutData)

    column.setLabelProvider(_labelProvider)

    // set editing support
    _editingSupport.foreach(column.setEditingSupport(_))

    // activate column sorting
    _sortBy.foreach(makeColumnSortable(column, _))
  }
}

class TableViewerColumnBuilder[A, B](builder: TableViewerBuilder[A], valueGetter: A => B, header: String) extends ViewerColumnBuilder[A, B](builder, valueGetter, header) {
  type Column = TableViewerColumn

  def createColumn() = new TableViewerColumn(builder.viewer, _align)

  def baseColumn(column: Column) = column.getColumn

  def makeColumnSortable(column: Column, ord: Ordering[B]) {
    val tableColumn = column.getColumn
    val upcastOrd = ord.on[Any](x => valueGetter(x.asInstanceOf[A]))
    tableColumn.setData(SortColumnComparator.SORT_BY, upcastOrd)
    tableColumn.addSelectionListener(builder.sortSelectionListener)
    if (_defaultSort) {
      builder.table.setSortColumn(tableColumn)
      builder.table.setSortDirection(SWT.UP)
    }
  }
  
  def build(setups: (TableColumn => Any)*) = {
    val column = createColumn()
    afterColumnCreated(column)
    val tableColumn = column.getColumn
    setups.foreach(_(tableColumn))
    column
  }
}

class TreeViewerColumnBuilder[A, B](builder: TreeViewerBuilder[A], valueGetter: A => B, header: String) extends ViewerColumnBuilder[A, B](builder, valueGetter, header) {
  type Column = TreeViewerColumn

  def createColumn() = new TreeViewerColumn(builder.viewer, _align)

  def baseColumn(column: Column) = column.getColumn

  def makeColumnSortable(column: Column, ord: Ordering[B]) {
    val treeColumn = column.getColumn
    val ordOnObject = ord.on[Object](x => valueGetter(x.asInstanceOf[A]))
    treeColumn.setData(SortColumnComparator.SORT_BY, ordOnObject)
    treeColumn.addSelectionListener(builder.sortSelectionListener)
    if (_defaultSort) {
      builder.tree.setSortColumn(treeColumn)
      builder.tree.setSortDirection(SWT.UP)
    }
  }
  
  def build(setups: (TreeColumn => Any)*) = {
    val column = createColumn()
    afterColumnCreated(column)
    val treeColumn = column.getColumn
    setups.foreach(_(treeColumn))
    column
  }
}