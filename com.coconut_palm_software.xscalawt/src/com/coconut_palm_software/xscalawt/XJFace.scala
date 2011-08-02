package com.coconut_palm_software.xscalawt

import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.jface.dialogs.IPageChangedListener
import org.eclipse.jface.dialogs.IPageChangingListener
import org.eclipse.jface.dialogs.PageChangedEvent
import org.eclipse.jface.dialogs.PageChangingEvent
import org.eclipse.jface.operation.IRunnableWithProgress
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
import com.coconut_palm_software.xscalawt.viewers.TableViewerBuilder
import com.coconut_palm_software.xscalawt.viewers.TreeViewerBuilder

object XJFace {
  implicit def viewer2XScalaWT[W <: Viewer](viewer: W) = new WidgetX[W](viewer)
  implicit def viewerColumn2XScalaWT[W <: ViewerColumn](viewerColumn: W) = new WidgetX[W](viewerColumn)

  def listViewer(setups: (ListViewer => Any)*) = (parent: Composite) =>
    setupAndReturn(new ListViewer(parent, SWT.BORDER), setups: _*)

  def comboViewer(setups: (ComboViewer => Any)*) = (parent: Composite) =>
    setupAndReturn(new ComboViewer(parent, SWT.BORDER), setups: _*)

  def tableViewer(setups: (TableViewer => Any)*) = (parent: Composite) =>
    setupAndReturn(new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER), setups: _*)

  def treeViewer(setups: (TreeViewer => Any)*) = (parent: Composite) =>
    setupAndReturn(new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER), setups: _*)

  def tableViewerColumn(setups: (TableViewerColumn => Any)*) = (parent: TableViewer) =>
    setupAndReturn(new TableViewerColumn(parent, SWT.LEFT), setups: _*)

  def treeViewerColumn(setups: (TreeViewerColumn => Any)*) = (parent: TreeViewer) =>
    setupAndReturn(new TreeViewerColumn(parent, SWT.LEFT), setups: _*)

  def tableViewerBuilder[A](setups: (TableViewerBuilder[A] => Any)*)(viewerSetups: (TableViewer => Any)*) = (parent: Composite) => {
    val builder = setupAndReturn(new TableViewerBuilder[A](parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER), setups: _*)
    val viewer = builder.viewer
    viewerSetups.foreach(_(viewer))
    builder
  }

  def treeViewerBuilder[A](setups: (TreeViewerBuilder[A] => Any)*)(viewerSetups: (TreeViewer => Any)*) = (parent: Composite) => {
    val builder = setupAndReturn(new TreeViewerBuilder[A](parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER), setups: _*)
    val viewer = builder.viewer
    viewerSetups.foreach(_(viewer))
    builder
  }

  def addSelectionChangedListener(l: ISelectionChangedListener) =
    (subject: ISelectionProvider) => subject.addSelectionChangedListener(l)

  implicit def selectionChangedListener(func: SelectionChangedEvent => Any): ISelectionChangedListener =
    new ISelectionChangedListener {
      def selectionChanged(e: SelectionChangedEvent) { func(e) }
    }

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

  def addTreeListener(l: ITreeViewerListener) =
    (subject: AbstractTreeViewer) => subject.addTreeListener(l)

  def treeViewerListener(collapsed: TreeExpansionEvent => Any = ignore, expanded: TreeExpansionEvent => Any = ignore): ITreeViewerListener =
    new ITreeViewerListener {
      override def treeCollapsed(e: TreeExpansionEvent) = collapsed(e)
      override def treeExpanded(e: TreeExpansionEvent) = expanded(e)
    }

  def onTreeViewer(collapsed: TreeExpansionEvent => Any = ignore, expanded: TreeExpansionEvent => Any = ignore) =
    addTreeListener(treeViewerListener(collapsed, expanded))

  private type AddPropertyChangeListener = { def addPropertyChangeListener(l: IPropertyChangeListener) }

  implicit def propertyChangeListener(func: PropertyChangeEvent => Any): IPropertyChangeListener =
    new IPropertyChangeListener {
      def propertyChange(e: PropertyChangeEvent) { func(e) }
    }

  implicit def onPropertyChangeImplicit[T <: AddPropertyChangeListener](func: PropertyChangeEvent => Any) =
    addPropertyChangeListener[T](func)

  def addPropertyChangeListener[T <: AddPropertyChangeListener](l: IPropertyChangeListener) =
    (subject: T) => subject.addPropertyChangeListener(l)

  def onPropertyChange[T <: AddPropertyChangeListener](func: PropertyChangeEvent => Any) =
    addPropertyChangeListener(func)

  def onPropertyChange[T <: AddPropertyChangeListener](func: => Unit) =
    addPropertyChangeListener((e: PropertyChangeEvent) => func)

  def addPageChangingListener(l: IPageChangingListener) =
    (subject: WizardDialog) => subject.addPageChangingListener(l)

  implicit def pageChangingListener(func: PageChangingEvent => Any): IPageChangingListener =
    new IPageChangingListener {
      def handlePageChanging(e: PageChangingEvent) { func(e) }
    }

  def onPageChanging(func: PageChangingEvent => Any) =
    addPageChangingListener(func)

  def onPageChanging(func: => Unit) =
    addPageChangingListener((e: PageChangingEvent) => func)

  implicit def onPageChangingImplicit(func: PageChangingEvent => Any) =
    addPageChangingListener(func)

  def addPageChangedListener(l: IPageChangedListener) =
    (subject: WizardDialog) => subject.addPageChangedListener(l)

  implicit def pageChangedListener(func: PageChangedEvent => Any): IPageChangedListener =
    new IPageChangedListener {
      def pageChanged(e: PageChangedEvent) { func(e) }
    }

  def onPageChanged(func: PageChangedEvent => Any) =
    addPageChangedListener(func)

  // can't be => Any, or we lose type inference
  def onPageChanged(func: => Unit) =
    addPageChangedListener((e: PageChangedEvent) => func)

  implicit def onPageChangedImplicit(func: PageChangedEvent => Any) =
    addPageChangedListener(func)

  def addOpenListener(l: IOpenListener) =
    (subject: StructuredViewer) => subject.addOpenListener(l)

  implicit def openListener(func: OpenEvent => Any): IOpenListener =
    new IOpenListener {
      def open(e: OpenEvent) { func(e) }
    }

  def onOpen(func: OpenEvent => Any) =
    addOpenListener(func)

  def onOpen(func: => Unit) =
    addOpenListener((e: OpenEvent) => func)

  implicit def onOpenImplicit(func: OpenEvent => Any) =
    addOpenListener(func)

  def addOpenEventListener(l: IOpenEventListener) =
    (subject: OpenStrategy) => subject.addOpenListener(l)

  implicit def openEventListener(func: SelectionEvent => Any): IOpenEventListener =
    new IOpenEventListener {
      def handleOpen(e: SelectionEvent) { func(e) }
    }

  def onOpenEvent(func: SelectionEvent => Any) =
    addOpenEventListener(func)

  def onOpenEvent(func: => Unit) =
    addOpenEventListener((e: SelectionEvent) => func)

  implicit def onOpenEventImplicit(func: SelectionEvent => Any) =
    addOpenEventListener(func)

  def addDoubleClickListener(l: IDoubleClickListener) =
    (subject: StructuredViewer) => subject.addDoubleClickListener(l)

  def onDoubleClick(func: DoubleClickEvent => Any) =
    addDoubleClickListener(func)

  // can't be => Any, or we lose type inference
  def onDoubleClick(func: => Unit) =
    addDoubleClickListener((e: DoubleClickEvent) => func)

  implicit def onDoubleClickImplicit(func: DoubleClickEvent => Any) =
    addDoubleClickListener(func)

  implicit def doubleClickListener(func: DoubleClickEvent => Any): IDoubleClickListener =
    new IDoubleClickListener {
      def doubleClick(e: DoubleClickEvent) { func(e) }
    }

  def addConcurrentModelListener(l: IConcurrentModelListener) =
    (subject: IConcurrentModel) => subject.addListener(l)

  def concurrentModelListener(addF: Array[AnyRef] => Any = ignore, removeF: Array[AnyRef] => Any = ignore, updateF: Array[AnyRef] => Any = ignore, setContentsF: Array[AnyRef] => Any = ignore): IConcurrentModelListener =
    new IConcurrentModelListener {
      def add(added: Array[AnyRef]) = addF(added)
      def remove(added: Array[AnyRef]) = removeF(added)
      def update(added: Array[AnyRef]) = updateF(added)
      def setContents(added: Array[AnyRef]) = setContentsF(added)
    }

  def onConcurrentModel(add: Array[AnyRef] => Any = ignore, remove: Array[AnyRef] => Any = ignore, update: Array[AnyRef] => Any = ignore, setContents: Array[AnyRef] => Any = ignore) =
    addConcurrentModelListener(concurrentModelListener(add, remove, update, setContents))

  def addCheckStateListener(l: ICheckStateListener) =
    (subject: ICheckable) => subject.addCheckStateListener(l)

  implicit def checkStateListener(func: CheckStateChangedEvent => Any): ICheckStateListener =
    new ICheckStateListener {
      def checkStateChanged(e: CheckStateChangedEvent) { func(e) }
    }

  def onCheckState(func: CheckStateChangedEvent => Any) =
    addCheckStateListener(func)

  def onCheckState(func: => Unit) =
    addCheckStateListener((e: CheckStateChangedEvent) => func)

  implicit def onCheckStateImplicit(func: CheckStateChangedEvent => Any) =
    addCheckStateListener(func)

  def addLabelProviderListener(l: ILabelProviderListener) =
    (subject: IBaseLabelProvider) => subject.addListener(l)

  implicit def labelProviderListener(func: LabelProviderChangedEvent => Any): ILabelProviderListener =
    new ILabelProviderListener {
      def labelProviderChanged(e: LabelProviderChangedEvent) { func(e) }
    }

  def onLabelProvider(func: LabelProviderChangedEvent => Any) =
    addLabelProviderListener(func)

  def onLabelProvider(func: => Unit) =
    addLabelProviderListener((e: LabelProviderChangedEvent) => func)

  implicit def onLabelProviderImplicit(func: LabelProviderChangedEvent => Any) =
    addLabelProviderListener(func)

  implicit def runnableWithProgress(f: IProgressMonitor => Any) =
    new IRunnableWithProgress { override def run(m: IProgressMonitor) { f(m) } }
}