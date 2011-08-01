package com.coconut_palm_software.xscalawt

import org.eclipse.jface.dialogs.IPageChangedListener
import org.eclipse.jface.dialogs.IPageChangingListener
import org.eclipse.jface.dialogs.PageChangedEvent
import org.eclipse.jface.dialogs.PageChangingEvent
import org.eclipse.jface.util.IOpenEventListener
import org.eclipse.jface.util.IPropertyChangeListener
import org.eclipse.jface.util.OpenStrategy
import org.eclipse.jface.util.PropertyChangeEvent
import org.eclipse.jface.viewers.deferred.IConcurrentModel
import org.eclipse.jface.viewers.deferred.IConcurrentModelListener
import org.eclipse.jface.viewers.AbstractTreeViewer
import org.eclipse.jface.viewers.CheckStateChangedEvent
import org.eclipse.jface.viewers.ComboViewer
import org.eclipse.jface.viewers.DoubleClickEvent
import org.eclipse.jface.viewers.IBaseLabelProvider
import org.eclipse.jface.viewers.ICheckStateListener
import org.eclipse.jface.viewers.ICheckable
import org.eclipse.jface.viewers.IDoubleClickListener
import org.eclipse.jface.viewers.ILabelProviderListener
import org.eclipse.jface.viewers.IOpenListener
import org.eclipse.jface.viewers.IPostSelectionProvider
import org.eclipse.jface.viewers.ISelectionChangedListener
import org.eclipse.jface.viewers.ISelectionProvider
import org.eclipse.jface.viewers.ITreeViewerListener
import org.eclipse.jface.viewers.LabelProviderChangedEvent
import org.eclipse.jface.viewers.ListViewer
import org.eclipse.jface.viewers.OpenEvent
import org.eclipse.jface.viewers.SelectionChangedEvent
import org.eclipse.jface.viewers.StructuredViewer
import org.eclipse.jface.viewers.TableViewer
import org.eclipse.jface.viewers.TableViewerColumn
import org.eclipse.jface.viewers.TreeExpansionEvent
import org.eclipse.jface.viewers.TreeViewer
import org.eclipse.jface.viewers.TreeViewerColumn
import org.eclipse.jface.viewers.Viewer
import org.eclipse.jface.viewers.ViewerColumn
import org.eclipse.jface.wizard.WizardDialog
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.SWT
import XScalaWTAPI._

object XJFace {
  implicit def viewer2XScalaWT[W <: Viewer](viewer: W) = new WidgetX[W](viewer)
  implicit def viewerColumn2XScalaWT[W <: ViewerColumn](viewerColumn: W) = new WidgetX[W](viewerColumn)

  def listViewer(setups:(ListViewer => Any)*) = (parent : Composite) =>
    setupAndReturn(new ListViewer(parent, SWT.BORDER), setups : _*)

  def comboViewer(setups:(ComboViewer => Any)*) = (parent : Composite) =>
    setupAndReturn(new ComboViewer(parent, SWT.BORDER), setups : _*)

  def tableViewer(setups:(TableViewer => Any)*) = (parent : Composite) =>
    setupAndReturn(new TableViewer(parent, SWT.BORDER), setups : _*)

  def treeViewer(setups:(TreeViewer => Any)*) = (parent : Composite) =>
    setupAndReturn(new TreeViewer(parent, SWT.BORDER), setups : _*)

  def tableViewerColumn(setups:(TableViewerColumn => Any)*) = (parent : TableViewer) =>
    setupAndReturn(new TableViewerColumn(parent, SWT.BORDER), setups : _*)

  def treeViewerColumn(setups:(TreeViewerColumn => Any)*) = (parent : TreeViewer) =>
    setupAndReturn(new TreeViewerColumn(parent, SWT.BORDER), setups : _*)
  
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

  case class SelectionChangedListener(func: SelectionChangedEvent => Any) extends ISelectionChangedListener {
    def selectionChanged(e: SelectionChangedEvent) { func(e) }
  }
  
  def addTreeListener(l: ITreeViewerListener) =
    (subject : AbstractTreeViewer) => subject.addTreeListener(l)
  
  case class TreeListenerForwarder(collapsed: TreeExpansionEvent => Any, expanded: TreeExpansionEvent => Any) extends ITreeViewerListener {
    override def treeCollapsed(e: TreeExpansionEvent) = collapsed(e)
    override def treeExpanded(e: TreeExpansionEvent) = expanded(e)
  }
  
  def onTree(collapsed: TreeExpansionEvent => Any = ignore, expanded: TreeExpansionEvent => Any = ignore) = 
    addTreeListener(new TreeListenerForwarder(collapsed, expanded))    

  private type AddPropertyChangeListener = { def addPropertyChangeListener(l: IPropertyChangeListener) }
  
  implicit def onPropertyChangeImplicit[T <: AddPropertyChangeListener](func: PropertyChangeEvent => Any) =
    addPropertyChangeListener[T](func)
    
  def addPropertyChangeListener[T <: AddPropertyChangeListener](l: IPropertyChangeListener) =
    (subject: T) => subject.addPropertyChangeListener(l)

  def onPropertyChange[T <: AddPropertyChangeListener](func: PropertyChangeEvent => Any) =
    addPropertyChangeListener(func)

  // can't be => Any, or we lose type inference
  def onPropertyChange[T <: AddPropertyChangeListener](func: => Unit) =
    addPropertyChangeListener((e: PropertyChangeEvent) => func)

  implicit def func2SelectionChangedListener(func: PropertyChangeEvent => Any): IPropertyChangeListener =
    new PropertyChangeListener(func)

  case class PropertyChangeListener(func: PropertyChangeEvent => Any) extends IPropertyChangeListener {
    def propertyChange(e: PropertyChangeEvent) { func(e) }
  }

  def addPageChangingListener(l: IPageChangingListener) =
    (subject: WizardDialog) => subject.addPageChangingListener(l)

  def onPageChanging(func: PageChangingEvent => Any) =
    addPageChangingListener(func)

  // can't be => Any, or we lose type inference
  def onPageChanging(func: => Unit) =
    addPageChangingListener((e: PageChangingEvent) => func)

  implicit def onPageChangingImplicit(func: PageChangingEvent => Any) =
    addPageChangingListener(func)

  implicit def func2PageChangingListener(func: PageChangingEvent => Any): IPageChangingListener =
    new PageChangingListener(func)

  case class PageChangingListener(func: PageChangingEvent => Any) extends IPageChangingListener {
    def handlePageChanging(e: PageChangingEvent) { func(e) }
  }

  def addPageChangedListener(l: IPageChangedListener) =
    (subject: WizardDialog) => subject.addPageChangedListener(l)

  def onPageChanged(func: PageChangedEvent => Any) =
    addPageChangedListener(func)

  // can't be => Any, or we lose type inference
  def onPageChanged(func: => Unit) =
    addPageChangedListener((e: PageChangedEvent) => func)

  implicit def onPageChangedImplicit(func: PageChangedEvent => Any) =
    addPageChangedListener(func)

  implicit def func2PageChangedListener(func: PageChangedEvent => Any): IPageChangedListener =
    new PageChangedListener(func)

  case class PageChangedListener(func: PageChangedEvent => Any) extends IPageChangedListener {
    def pageChanged(e: PageChangedEvent) { func(e) }
  }

  def addOpenListener(l: IOpenListener) =
    (subject: StructuredViewer) => subject.addOpenListener(l)

  def onOpen(func: OpenEvent => Any) =
    addOpenListener(func)

  // can't be => Any, or we lose type inference
  def onOpen(func: => Unit) =
    addOpenListener((e: OpenEvent) => func)

  implicit def onOpenImplicit(func: OpenEvent => Any) =
    addOpenListener(func)

  implicit def func2OpenListener(func: OpenEvent => Any): IOpenListener =
    new OpenListener(func)

  case class OpenListener(func: OpenEvent => Any) extends IOpenListener {
    def open(e: OpenEvent) { func(e) }
  }

  def addOpenEventListener(l: IOpenEventListener) =
    (subject: OpenStrategy) => subject.addOpenListener(l)

  def onOpenEvent(func: SelectionEvent => Any) =
    addOpenEventListener(func)

  // can't be => Any, or we lose type inference
  def onOpenEvent(func: => Unit) =
    addOpenEventListener((e: SelectionEvent) => func)

  implicit def onOpenEventImplicit(func: SelectionEvent => Any) =
    addOpenEventListener(func)

  implicit def func2OpenEventListener(func: SelectionEvent => Any): IOpenEventListener =
    new OpenEventListener(func)

  case class OpenEventListener(func: SelectionEvent => Any) extends IOpenEventListener {
    def handleOpen(e: SelectionEvent) { func(e) }
  }

  def addDoubleClickListener(l: IDoubleClickListener) =
    (subject: StructuredViewer) => subject.addDoubleClickListener(l)

  def onDoubleClick(func: DoubleClickEvent => Any) =
    addDoubleClickListener(func)

  // can't be => Any, or we lose type inference
  def onDoubleClick(func: => Unit) =
    addDoubleClickListener((e: DoubleClickEvent) => func)

  implicit def onDoubleClickImplicit(func: DoubleClickEvent => Any) =
    addDoubleClickListener(func)

  implicit def func2DoubleClickListener(func: DoubleClickEvent => Any): IDoubleClickListener =
    new DoubleClickListener(func)

  case class DoubleClickListener(func: DoubleClickEvent => Any) extends IDoubleClickListener {
    def doubleClick(e: DoubleClickEvent) { func(e) }
  }
  
  def addConcurrentModelListener(l: IConcurrentModelListener) =
    (subject : IConcurrentModel) => subject.addListener(l)
  
  case class ConcurrentModelListener(addF: Array[AnyRef] => Any, removeF: Array[AnyRef] => Any, updateF: Array[AnyRef] => Any, setContentsF: Array[AnyRef] => Any) extends IConcurrentModelListener {
    def add(added: Array[AnyRef]) = addF(added)
    def remove(added: Array[AnyRef]) = removeF(added)
    def update(added: Array[AnyRef]) = updateF(added)
    def setContents(added: Array[AnyRef]) = setContentsF(added)
  }
  
  def onConcurrentModel(add: Array[AnyRef] => Any, remove: Array[AnyRef] => Any, update: Array[AnyRef] => Any, setContents: Array[AnyRef] => Any) = 
    addConcurrentModelListener(new ConcurrentModelListener(add, remove, update, setContents))    

  def addCheckStateListener(l: ICheckStateListener) =
    (subject: ICheckable) => subject.addCheckStateListener(l)

  def onCheckState(func: CheckStateChangedEvent => Any) =
    addCheckStateListener(func)
  
  def onCheckState(func: => Unit) =
    addCheckStateListener((e: CheckStateChangedEvent) => func)

  implicit def onCheckStateImplicit(func: CheckStateChangedEvent => Any) =
    addCheckStateListener(func)

  implicit def func2CheckStateListener(func: CheckStateChangedEvent => Any): ICheckStateListener =
    new CheckStateListener(func)

  case class CheckStateListener(func: CheckStateChangedEvent => Any) extends ICheckStateListener {
    def checkStateChanged(e: CheckStateChangedEvent) { func(e) }
  }

  def addLabelProviderListener(l: ILabelProviderListener) =
    (subject: IBaseLabelProvider) => subject.addListener(l)

  def onLabelProvider(func: LabelProviderChangedEvent => Any) =
    addLabelProviderListener(func)
  
  def onLabelProvider(func: => Unit) =
    addLabelProviderListener((e: LabelProviderChangedEvent) => func)

  implicit def onLabelProviderImplicit(func: LabelProviderChangedEvent => Any) =
    addLabelProviderListener(func)

  implicit def func2LabelProviderListener(func: LabelProviderChangedEvent => Any): ILabelProviderListener =
    new LabelProviderListener(func)

  case class LabelProviderListener(func: LabelProviderChangedEvent => Any) extends ILabelProviderListener {
    def labelProviderChanged(e: LabelProviderChangedEvent) { func(e) }
  }
}