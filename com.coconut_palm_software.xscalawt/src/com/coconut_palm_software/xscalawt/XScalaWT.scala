/*******************************************************************************
 * Copyright (c) 2009 David Orme and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     David Orme - initial API and implementation
 *******************************************************************************/
package com.coconut_palm_software.xscalawt

import reflect.Manifest
import java.lang.reflect._
import org.eclipse.swt._
import org.eclipse.swt.events._
import org.eclipse.swt.graphics._
import org.eclipse.swt.widgets._
import org.eclipse.swt.browser._
import org.eclipse.swt.custom._
import org.eclipse.swt.layout._
import org.eclipse.swt.SWT

import XScalaWTAPI._

object XScalaWT {
  /**
   * The manual, "specify everything" syntax.  Useful when none of the convenience methods works.
   * <p>
   * Note, depends on Manifest[T] which is experimental as-of 2.7.3final (this writing)
   */
  def *[T](style : Int)(setups:(T => Unit)*)(parent:Composite)(implicit manifest : Manifest[T]) = {
    val controlClass : Class[T] = manifest.erasure.asInstanceOf[Class[T]]
    val constructor = controlClass.getDeclaredConstructor(classOf[Composite], classOf[Int])
    constructor.setAccessible(true)
    val control = constructor.newInstance(parent, style.asInstanceOf[Object]).asInstanceOf[T]
    setups.foreach(setup => setup(control))
    control
  }

  // Conveninece methods, one or more per concrete SWT class

//  def animatedProgress(setups:(AnimatedProgress => Unit)*)(parent : Composite) {
//    setup(new AnimatedProgress(parent, SWT.BORDER), setups : _*)
//  }
  
  def browser(setups:(Browser => Unit)*)(parent : Composite) = {
    setupAndReturn(new Browser(parent, SWT.NULL), setups : _*)
  }
  
  def button(setups:(Button => Unit)*)(parent : Composite) = {
    setupAndReturn(new Button(parent, SWT.PUSH), setups : _*)
  }

  def cBanner(setups:(CBanner => Unit)*)(parent : Composite) = {
    setupAndReturn(new CBanner(parent, SWT.NULL), setups : _*)
  }
  
//  def cCombo(setups:(CCombo => Unit)*)(parent : Composite) = {
//    setup(new CCombo(parent, SWT.BORDER), setups : _*)
//  }
  
  def combo(setups:(Combo => Unit)*)(parent : Composite) = {
    setupAndReturn(new Combo(parent, SWT.BORDER), setups : _*)
  }
  
  def coolBar(setups:(CoolBar => Unit)*)(parent : Composite) = {
    setupAndReturn(new CoolBar(parent, SWT.NULL), setups : _*)
  }
  
  def cLabel(setups:(CLabel => Unit)*)(parent : Composite) = {
    setupAndReturn(new CLabel(parent, SWT.NULL), setups : _*)
  }
  
  def composite(setups:(Composite => Unit)*)(parent : Composite) = {
    val composite = new Composite(parent, SWT.NULL)
    composite.setLayout(new GridLayout())
    setups.foreach(setup => setup(composite))
    composite
  }
  
  def cTabFolder(setups:(CTabFolder => Unit)*)(parent : Composite) = {
    setupAndReturn(new CTabFolder(parent, SWT.NULL), setups : _*)
  }
  
  def dateTime(setups:(DateTime => Unit)*)(parent : Composite) = {
    setupAndReturn(new DateTime(parent, SWT.BORDER), setups : _*)
  }
  
  def expandBar(setups:(ExpandBar => Unit)*)(parent : Composite) = {
    setupAndReturn(new ExpandBar(parent, SWT.V_SCROLL), setups : _*)
  }
  
//  def glCanvas(data : GLData)(setups:(GLCanvas => Unit)*)(parent:Composite) = {
//    setup(new GLCanvas(parent, SWT.NULL, data), setups : _*)
//  }
  
  def group(setups:(Group => Unit)*)(parent:Composite) = {  
    val group = new Group(parent, SWT.NULL)  
    group.setLayout(new GridLayout());  
    setups.foreach(setup => setup(group))  
    group
  }
  
  def label(setups:(Label => Unit)*)(parent:Composite) = {  
    setupAndReturn(new Label(parent, SWT.NULL), setups : _*)  
  }
  
  def labelSeparator(setups:(Label => Unit)*)(parent:Composite) = {  
    setupAndReturn(new Label(parent, SWT.SEPARATOR|SWT.HORIZONTAL), setups : _*)  
  }
  
  def labelSeparatorVertical(setups:(Label => Unit)*)(parent:Composite) = {  
    setupAndReturn(new Label(parent, SWT.SEPARATOR|SWT.VERTICAL), setups : _*)  
  }
  
  def link(setups:(Link => Unit)*)(parent: Composite) = {
    setupAndReturn(new Link(parent, SWT.NULL), setups : _*)
  }
  
  def list(setups:(List => Unit)*)(parent: Composite) = {
    setupAndReturn(new List(parent, SWT.BORDER), setups : _*)
  }
  
  def progressBar(setups:(ProgressBar => Unit)*)(parent: Composite) = {
    setupAndReturn(new ProgressBar(parent, SWT.NULL), setups : _*)
  }
  
  def sashForm(setups:(SashForm => Unit)*)(parent: Composite) = {
    setupAndReturn(new SashForm(parent, SWT.NULL), setups : _*)
  }
  
  def scaleHorizontal(setups:(Scale => Unit)*)(parent: Composite) = {
    setupAndReturn(new Scale(parent, SWT.HORIZONTAL), setups : _*)
  }
  
  def scaleVertical(setups:(Scale => Unit)*)(parent: Composite) = {
    setupAndReturn(new Scale(parent, SWT.VERTICAL), setups : _*)
  }
  
  private def setupScrolledComposite(parent: Composite, style : Int, setups : (ScrolledComposite => Unit)*) = {
    val composite = new ScrolledComposite(parent, style)
    setups.foreach(setup => setup(composite))
    composite.addControlListener(new ControlAdapter() {
      override def controlResized(e : ControlEvent) = {
      val content = composite.getChildren()(0)
        if (content.isInstanceOf[Composite]) {
          content.asInstanceOf[Composite].pack
        }
      }
    })
    composite.setContent(composite.getChildren()(0))
    composite
  }
  
  def scrolledCompositeVertical(setups: (ScrolledComposite => Unit)*)(parent : Composite) = {
    setupScrolledComposite(parent, SWT.V_SCROLL, setups : _*)
  }
  
  def scrolledCompositeHorizontal(setups: (ScrolledComposite => Unit)*)(parent : Composite) = {
    setupScrolledComposite(parent, SWT.H_SCROLL, setups : _*)
  }
  
  def scrolledCompositeBoth(setups: (ScrolledComposite => Unit)*)(parent : Composite) = {
    setupScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL, setups : _*)
  }
  
  def spinner(setups : (Spinner => Unit)*)(parent : Composite) = {
    setupAndReturn(new Spinner(parent, SWT.BORDER), setups : _*)
  }
  
  def shell(setups:(Shell => Unit)*) = {
    val display = Display.getDefault
    val shell = new Shell(display)
    shell.setLayout(new GridLayout());  
    
    setups.foreach(setup => setup(shell))
    shell
  }
  
  def shellNoTrim(setups:(Shell => Unit)*) = {
    val display = Display.getDefault
    val shell = new Shell(display, SWT.NO_TRIM)
    shell.setLayout(new GridLayout());  
    
    setups.foreach(setup => setup(shell))
    shell
  }
  
  def runEventLoop(window : Shell) = {
    val display = Display.getDefault
    window.open
    while (!window.isDisposed) {
      if (!display.readAndDispatch) display.sleep
    }
  }
  
  def sliderHorizontal(setups:(Slider => Unit)*)(parent: Composite) = {
    setupAndReturn(new Slider(parent, SWT.HORIZONTAL), setups : _*)
  }
  
  def sliderVertical(setups:(Slider => Unit)*)(parent: Composite) = {
    setupAndReturn(new Slider(parent, SWT.VERTICAL), setups : _*)
  }
  
//  def styledText(setups:(StyledText => Unit)*)(parent: Composite) = {
//    setup(new StyledText(parent, SWT.NULL), setups : _*)
//  }
  
  def tabFolder(setups:(TabFolder => Unit)*)(parent: Composite) = {
    setupAndReturn(new TabFolder(parent, SWT.NULL), setups : _*)
  }
  
  def table(setups:(Table => Unit)*)(parent: Composite) = {
    setupAndReturn(new Table(parent, SWT.BORDER), setups : _*)
  }
  
//  def tableTree(setups:(TableTree => Unit)*)(parent: Composite) = {
//    setup(new TableTree(parent, SWT.BORDER), setups : _*)
//  }
  
  def text(setups:(Text => Unit)*)(parent:Composite) = {  
    setupAndReturn(new Text(parent, SWT.BORDER), setups : _*)  
  }
  
  def textPasswd(setups:(Text => Unit)*)(parent:Composite) = {  
    setupAndReturn(new Text(parent, SWT.BORDER | SWT.PASSWORD), setups : _*)  
  }
  
  def toolBar(setups:(ToolBar => Unit)*)(parent: Composite) = {
    setupAndReturn(new ToolBar(parent, SWT.NULL), setups : _*)
  }
  
  def tree(setups:(Tree => Unit)*)(parent: Composite) = {
    setupAndReturn(new Tree(parent, SWT.BORDER), setups : _*)
  }
  
  def viewForm(setups:(ViewForm => Unit)*)(parent: Composite) = {
    setupAndReturn(new ViewForm(parent, SWT.BORDER), setups : _*)
  }
  
  def viewFormFlat(setups:(ViewForm => Unit)*)(parent: Composite) = {
    setupAndReturn(new ViewForm(parent, SWT.BORDER | SWT.FLAT), setups : _*)
  }
  
  // Items here
  
  def tabItem(setups:(TabItem => Unit)*)(parent: Control) = {
    val tabItem = new TabItem(parent.getParent().asInstanceOf[TabFolder], SWT.NULL)
    tabItem.setControl(parent)
    setups.foreach(setup => setup(tabItem))
    tabItem
  }
  
  def coolItem(setups:(CoolItem => Unit)*)(parent: Control) = {
    val coolItem = new CoolItem(parent.getParent().asInstanceOf[CoolBar], SWT.NULL)
    val size = parent.computeSize(SWT.DEFAULT, SWT.DEFAULT)
    coolItem.setPreferredSize(coolItem.computeSize(size.x, size.y))
    coolItem.setControl(parent)
    setups.foreach(setup => setup(coolItem))
    coolItem
  }
  
  def expandItem(setups:(ExpandItem => Unit)*)(parent: Control) = {
    val item = new ExpandItem(parent.getParent().asInstanceOf[ExpandBar], SWT.NULL)
    item.setHeight(parent.computeSize(SWT.DEFAULT, SWT.DEFAULT).y)
    item.setControl(parent)
    setups.foreach(setup => setup(item))
    item
  }
  
  // Start with any Widget and add children from there...
  
  class WidgetX[W](w : W) {
    def apply(setups : (W => Unit)*) : W = {
      setupAndReturn(w, setups : _*)
    }
    def contains(setups : (W => Unit)*) : W = {
      setupAndReturn(w, setups : _*)
    }
  }
  
  implicit def widget2XScalaWT[W <: Widget](widget : W) = new WidgetX[W](widget)  

//    (setups:(Browser => Unit)*)
    
  // Declarative setters here
  
  def setText[T <: {def setText(txt:String)}](txt:String)(subject:T) = {  
    subject.setText(txt)  
  }
  
  implicit def string2setText[T <: {def setText(s:String)}](txt : String) : T => Unit = {
    setText[T](txt)_
  }
  
  def setImage[T <: {def setImage(image:Image)}](image : Image)(subject:T) = {
    subject.setImage(image)
  }
  
  implicit def image2setImage[T <: {def setImage(image:Image)}](image : Image)(subject:T) = {
    subject.setImage(image)
  }

  def setControl[T <: {def setControl(control:Control)}](control : Control)(subject:T) = {
    subject.setControl(control)
  }

  def setBackground[T <: {def setBackground(c : Color)}](c : Color)(subject:T) = {  
    subject.setBackground(c)  
  }

  def setForeground[T <: {def setForeground(c : Color)}](c : Color)(subject:T) = {  
    subject.setForeground(c)  
  }

  def setLayout[T <: {def setLayout(layout : Layout)}](layout : Layout)(subject:T) = {  
    subject.setLayout(layout)  
  }

  def setLayoutData[T <: {def setLayoutData(data : Object)}](data : Object)(subject:T) = {  
    subject.setLayoutData(data)  
  }
  
  def setMinimum[T <: {def setMinimum(value : Int)}](value : Int)(subject:T) = {
    subject.setMinimum(value)
  }
  
  def setMaximum[T <: {def setMaximum(value : Int)}](value : Int)(subject:T) = {
    subject.setMaximum(value)
  }
  
  def setSelection[T <: {def setSelection(value : Int)}](value : Int)(subject:T) = {
    subject.setSelection(value)
  }
  
  // Event handling here
  
  // SelectionListener-------------------------------------------------------------
  
  def addSelectionListener[T <: {def addSelectionListener(l : SelectionListener)}](l : SelectionListener)(subject:T) = {
    subject.addSelectionListener(l)
  }
  
  private class SelectionListenerForwarder(l : (SelectionEvent => Unit)) extends SelectionAdapter {
    override def widgetSelected(e : SelectionEvent) = l(e)
    override def widgetDefaultSelected(e : SelectionEvent) = l(e)
  }
  
  def addSelectionListener[T <: {def addSelectionListener(l : SelectionListener)}](l : (SelectionEvent => Unit))(subject:T) = {
    subject.addSelectionListener(new SelectionListenerForwarder(l))
  }
  
  implicit def selectionFn2addSelectionListener[T <: {def addSelectionListener(l : SelectionListener)}](l : (SelectionEvent => Unit)) = {
    addSelectionListener[T](l)_
  }
  
  implicit def func2SelectionListener[T <: { def apply(e : SelectionEvent) }](func : T) = new SelectionAdapter() {
    override def widgetSelected(e : SelectionEvent) = func.apply(e) 
  }
  
  // MouseListener-----------------------------------------------------------------

  def addMouseListener[T <: {def addMouseListener(l : MouseListener)}](l : MouseListener)(subject:T) = {
    subject.addMouseListener(l)
  }
  
  class _MouseListenerForwarder(l : (MouseEvent => Unit)) extends MouseAdapter {
    override def mouseDown(e : MouseEvent) = l(e)
  }
  
  def addMouseListener[T <: {def addMouseListener(l : MouseListener)}](l : (MouseEvent => Unit))(subject:T) = {
    subject.addMouseListener(new _MouseListenerForwarder(l))
  }
  
  implicit def mouseFunc2addMouseListener[T <: {def addMouseListener(l : MouseListener)}](func : (MouseEvent => Unit))(subject:T) {
    subject.addMouseListener(new _MouseListenerForwarder(func))
  }
  
  implicit def func2MouseListener[T <: (MouseEvent => Unit)](func : T) = new _MouseListenerForwarder(func)
  
  // ModifyListener-----------------------------------------------------------------
  
  def addModifyListener[T <: {def addModifyListener(l : ModifyListener)}](l : ModifyListener)(subject : T) = 
    subject.addModifyListener(l)
  
  class _ModifyListenerForwarder(l : (ModifyEvent => Unit)) extends ModifyListener {
    override def modifyText(e : ModifyEvent) = l(e)
  }
  
  implicit def modifyFn2addModifyListener[T <: {def addModifyListener(l : ModifyListener)}](l : (ModifyEvent => Unit))(subject : T) =
    subject.addModifyListener(new _ModifyListenerForwarder(l))
  
  implicit def func2ModifyListener[T <: (ModifyEvent => Unit)](func : T) : ModifyListener = 
    new _ModifyListenerForwarder(func)
  
  // TraverseListener--------------------------------------------------------------
  
  def addTraverseListener[T <: {def addTraverseListener(l : TraverseListener)}](l : TraverseListener)(subject : T) = {
    subject.addTraverseListener(l)
  }
  
  class _TraverseListenerForwarder(l : (TraverseEvent => Unit)) extends TraverseListener {
    override def keyTraversed(e : TraverseEvent) = l(e)
  }
  
  implicit def modifyFn2addTraverseListener[T <: {def addTraverseListener(l : TraverseListener)}](l : (TraverseEvent => Unit))(subject : T) =
    subject.addTraverseListener(new _TraverseListenerForwarder(l))
  
  implicit def func2TraverseListener[T <: (TraverseEvent => Unit)](func : T) : TraverseListener = 
    new _TraverseListenerForwarder(func)
  
  // Convenience methods
  def display = Display.getDefault
  implicit def int2Color(swtColorConstant : Int) = display.getSystemColor(swtColorConstant)

  class RunnableForwarder(f : => Unit) extends Runnable {
    override def run() = f
  }
  implicit def func2Runnable(f : => Unit) = new RunnableForwarder(f)

}

