package com.coconut_palm_software.xscalawt

import org.eclipse.jface.viewers.Viewer
import XScalaWTAPI._
import org.eclipse.jface.viewers.ISelectionChangedListener
import org.eclipse.jface.viewers.SelectionChangedEvent
import org.eclipse.jface.viewers.ViewerColumn
import org.eclipse.jface.viewers.ITreeViewerListener
import org.eclipse.jface.viewers.TreeExpansionEvent
import org.eclipse.jface.viewers.ISelectionProvider
import org.eclipse.jface.viewers.IPostSelectionProvider
import org.eclipse.jface.viewers.AbstractTreeViewer

object XJFace {
  implicit def viewer2XScalaWT[W <: Viewer](viewer: W) = new WidgetX[W](viewer)
  implicit def viewerColumn2XScalaWT[W <: ViewerColumn](viewerColumn: W) = new WidgetX[W](viewerColumn)

  def addSelectionChangedListener(l: ISelectionChangedListener) =
    (subject: ISelectionProvider) => subject.addSelectionChangedListener(l)

  def onSelectionChange(func: SelectionChangedEvent => Any) =
    addSelectionChangedListener(func)

  // can't be => Any, or we lose type inference
  def onSelectionChange(func: => Unit) =
    addSelectionChangedListener((e: SelectionChangedEvent) => func)

  implicit def onSelectionChangeImplicit(func: SelectionChangedEvent => Any) =
    addSelectionChangedListener(func)

  def addPostSelectionChangedListener(l: ISelectionChangedListener) =
    (subject: IPostSelectionProvider) => subject.addPostSelectionChangedListener(l)

  def postSelectionChange(func: SelectionChangedEvent => Any) =
    addPostSelectionChangedListener(func)

  def postSelectionChange(func: => Unit) =
    addPostSelectionChangedListener((e: SelectionChangedEvent) => func)

  implicit def func2SelectionChangedListener(func: SelectionChangedEvent => Any): ISelectionChangedListener =
    new SelectionChangedListener(func)

  case class SelectionChangedListener(f: SelectionChangedEvent => Any) extends ISelectionChangedListener {
    def selectionChanged(e: SelectionChangedEvent) { f(e) }
  }
  
  def addTreeListener(l: ITreeViewerListener) =
    (subject : AbstractTreeViewer) => subject.addTreeListener(l)
  
  case class TreeListenerForwarder(collapsed: TreeExpansionEvent => Any, expanded: TreeExpansionEvent => Any) extends ITreeViewerListener {
    override def treeCollapsed(e: TreeExpansionEvent) = collapsed(e)
    override def treeExpanded(e: TreeExpansionEvent) = expanded(e)
  }
  
  def onTree(collapsed: TreeExpansionEvent => Any = ignore, expanded: TreeExpansionEvent => Any = ignore) = 
    addTreeListener(new TreeListenerForwarder(collapsed, expanded))    
}