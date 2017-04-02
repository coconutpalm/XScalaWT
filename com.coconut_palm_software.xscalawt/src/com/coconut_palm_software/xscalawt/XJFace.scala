package com.coconut_palm_software.xscalawt

import com.coconut_palm_software.xscalawt.XScalaWTAPI._
import com.coconut_palm_software.xscalawt.viewers.{TableViewerBuilder, TreeViewerBuilder}
import org.eclipse.jface.dialogs.{IPageChangedListener, IPageChangingListener, PageChangedEvent, PageChangingEvent}
import org.eclipse.jface.util.{IOpenEventListener, IPropertyChangeListener, OpenStrategy, PropertyChangeEvent}
import org.eclipse.jface.viewers._
import org.eclipse.jface.viewers.deferred.{IConcurrentModel, IConcurrentModelListener}
import org.eclipse.jface.wizard.WizardDialog
import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.widgets.Composite

import scala.language.{implicitConversions, reflectiveCalls}

object XJFace {
  implicit def viewer2XScalaWT[W <: Viewer](viewer: W): WidgetX[W] = new WidgetX[W](viewer)
  implicit def viewerColumn2XScalaWT[W <: ViewerColumn](viewerColumn: W): WidgetX[W] = new WidgetX[W](viewerColumn)

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

  def onSelectionChange(func: ISelectionChangedListener) =
    addSelectionChangedListener(func)

  // can't be => Any, or we lose type inference
  def onSelectionChange(func: => Unit) =
    addSelectionChangedListener((e: SelectionChangedEvent) => func)

  implicit def onSelectionChangeImplicit(func: ISelectionChangedListener): (ISelectionProvider) => Unit =
    addSelectionChangedListener(func)

  def addPostSelectionChangedListener(l: ISelectionChangedListener) =
    (subject: IPostSelectionProvider) => subject.addPostSelectionChangedListener(l)

  def postSelectionChange(func: ISelectionChangedListener) =
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

  implicit def onPropertyChangeImplicit[T <: AddPropertyChangeListener](func: IPropertyChangeListener): (T) => Unit =
    addPropertyChangeListener[T](func)

  def addPropertyChangeListener[T <: AddPropertyChangeListener](l: IPropertyChangeListener) =
    (subject: T) => subject.addPropertyChangeListener(l)

  def onPropertyChange[T <: AddPropertyChangeListener](func: IPropertyChangeListener) =
    addPropertyChangeListener(func)

  def onPropertyChange[T <: AddPropertyChangeListener](func: => Unit) =
    addPropertyChangeListener((e: PropertyChangeEvent) => func)

  def addPageChangingListener(l: IPageChangingListener) =
    (subject: WizardDialog) => subject.addPageChangingListener(l)

  def onPageChanging(func: IPageChangingListener) =
    addPageChangingListener(func)

  def onPageChanging(func: => Unit) =
    addPageChangingListener((e: PageChangingEvent) => func)

  implicit def onPageChangingImplicit(func: IPageChangingListener): (WizardDialog) => Unit =
    addPageChangingListener(func)

  def addPageChangedListener(l: IPageChangedListener) =
    (subject: WizardDialog) => subject.addPageChangedListener(l)

  def onPageChanged(func: IPageChangedListener) =
    addPageChangedListener(func)

  // can't be => Any, or we lose type inference
  def onPageChanged(func: => Unit) =
    addPageChangedListener((e: PageChangedEvent) => func)

  implicit def onPageChangedImplicit(func: IPageChangedListener): (WizardDialog) => Unit =
    addPageChangedListener(func)

  def addOpenListener(l: IOpenListener) =
    (subject: StructuredViewer) => subject.addOpenListener(l)

  def onOpen(func: IOpenListener) =
    addOpenListener(func)

  def onOpen(func: => Unit) =
    addOpenListener((e: OpenEvent) => func)

  implicit def onOpenImplicit(func: IOpenListener): (StructuredViewer) => Unit =
    addOpenListener(func)

  def addOpenEventListener(l: IOpenEventListener) =
    (subject: OpenStrategy) => subject.addOpenListener(l)

  def onOpenEvent(func: IOpenEventListener) =
    addOpenEventListener(func)

  def onOpenEvent(func: => Unit) =
    addOpenEventListener((e: SelectionEvent) => func)

  implicit def onOpenEventImplicit(func: IOpenEventListener): (OpenStrategy) => Unit =
    addOpenEventListener(func)

  def addDoubleClickListener(l: IDoubleClickListener) =
    (subject: StructuredViewer) => subject.addDoubleClickListener(l)

  def onDoubleClick(func: DoubleClickEvent => Any) =
    addDoubleClickListener(func)

  // can't be => Any, or we lose type inference
  def onDoubleClick(func: => Unit) =
    addDoubleClickListener((e: DoubleClickEvent) => func)

  implicit def onDoubleClickImplicit(func: DoubleClickEvent => Any): (StructuredViewer) => Unit =
    addDoubleClickListener(func)

  implicit def doubleClickListener(func: DoubleClickEvent => Any): IDoubleClickListener =
    (e: DoubleClickEvent) => {
      func(e)
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

  def onCheckState(func: ICheckStateListener) =
    addCheckStateListener(func)

  def onCheckState(func: => Unit) =
    addCheckStateListener((e: CheckStateChangedEvent) => func)

  implicit def onCheckStateImplicit(func: ICheckStateListener): (ICheckable) => Unit =
    addCheckStateListener(func)

  def addLabelProviderListener(l: ILabelProviderListener) =
    (subject: IBaseLabelProvider) => subject.addListener(l)

  def onLabelProvider(func: ILabelProviderListener) =
    addLabelProviderListener(func)

  def onLabelProvider(func: => Unit) =
    addLabelProviderListener((e: LabelProviderChangedEvent) => func)

  implicit def onLabelProviderImplicit(func: ILabelProviderListener): (IBaseLabelProvider) => Unit =
    addLabelProviderListener(func)
}