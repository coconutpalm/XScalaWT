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

import reflect.ClassManifest
import java.lang.reflect._
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
   * Note, depends on ClassManifest[T] which is still experimental.
   */
  def *[T : ClassManifest](style : Int)(setups:(T => Any)*) = { (parent : Composite) =>
    val controlClass : Class[T] = classManifest[T].erasure.asInstanceOf[Class[T]]
    val constructor = controlClass.getDeclaredConstructor(classOf[Composite], classOf[Int])
    constructor.setAccessible(true)
    val control = constructor.newInstance(parent, style.asInstanceOf[Object]).asInstanceOf[T]
    setups.foreach(setup => setup(control))
    control
  }

  // Convenience methods, one or more per concrete SWT class

//  def animatedProgress(setups:(AnimatedProgress => Any)*)(parent : Composite) {
//    setup(new AnimatedProgress(parent, SWT.BORDER), setups : _*)
//  }
  
  def browser(setups:(Browser => Any)*) = { (parent : Composite) =>
    setupAndReturn(new Browser(parent, SWT.NONE), setups : _*)
  }
  
  def button(setups:(Button => Any)*) = { (parent : Composite) =>
    setupAndReturn(new Button(parent, SWT.PUSH), setups : _*)
  }

  def cBanner(setups:(CBanner => Any)*) = { (parent : Composite) =>
    setupAndReturn(new CBanner(parent, SWT.NONE), setups : _*)
  }
  
//  def cCombo(setups:(CCombo => Any)*) = { (parent : Composite) =>
//    setup(new CCombo(parent, SWT.BORDER), setups : _*)
//  }
  
  def combo(setups:(Combo => Any)*) = { (parent : Composite) =>
    setupAndReturn(new Combo(parent, SWT.BORDER), setups : _*)
  }
  
  def coolBar(setups:(CoolBar => Any)*) = { (parent : Composite) =>
    setupAndReturn(new CoolBar(parent, SWT.NONE), setups : _*)
  }
  
  def cLabel(setups:(CLabel => Any)*) = { (parent : Composite) =>
    setupAndReturn(new CLabel(parent, SWT.NONE), setups : _*)
  }
  
  def composite(setups:(Composite => Any)*) = { (parent : Composite) =>
    val composite = new Composite(parent, SWT.NONE)
    composite.setLayout(new GridLayout())
    setups.foreach(setup => setup(composite))
    composite
  }
  
  def cTabFolder(setups:(CTabFolder => Any)*) = { (parent : Composite) =>
    setupAndReturn(new CTabFolder(parent, SWT.NONE), setups : _*)
  }
  
  def dateTime(setups:(DateTime => Any)*) = { (parent : Composite) =>
    setupAndReturn(new DateTime(parent, SWT.BORDER), setups : _*)
  }
  
  def expandBar(setups:(ExpandBar => Any)*) = { (parent : Composite) =>
    setupAndReturn(new ExpandBar(parent, SWT.V_SCROLL), setups : _*)
  }
  
//  def glCanvas(data : GLData)(setups:(GLCanvas => Any)*)(parent:Composite) = {
//    setup(new GLCanvas(parent, SWT.NONE, data), setups : _*)
//  }
  
  def group(setups:(Group => Any)*)(parent:Composite) = {  
    val group = new Group(parent, SWT.NONE)  
    group.setLayout(new GridLayout());  
    setups.foreach(setup => setup(group))  
    group
  }
  
  def label(setups:(Label => Any)*)(parent:Composite) = {  
    setupAndReturn(new Label(parent, SWT.NONE), setups : _*)  
  }
  
  def labelSeparator(setups:(Label => Any)*)(parent:Composite) = {  
    setupAndReturn(new Label(parent, SWT.SEPARATOR|SWT.HORIZONTAL), setups : _*)  
  }
  
  def labelSeparatorVertical(setups:(Label => Any)*)(parent:Composite) = {  
    setupAndReturn(new Label(parent, SWT.SEPARATOR|SWT.VERTICAL), setups : _*)  
  }
  
  def link(setups:(Link => Any)*) = { (parent : Composite) =>
    setupAndReturn(new Link(parent, SWT.NONE), setups : _*)
  }
  
  def list(setups:(List => Any)*) = { (parent : Composite) =>
    setupAndReturn(new List(parent, SWT.BORDER), setups : _*)
  }
  
  def progressBar(setups:(ProgressBar => Any)*) = { (parent : Composite) =>
    setupAndReturn(new ProgressBar(parent, SWT.NONE), setups : _*)
  }
  
  def sashForm(setups:(SashForm => Any)*) = { (parent : Composite) =>
    setupAndReturn(new SashForm(parent, SWT.NONE), setups : _*)
  }
  
  def scaleHorizontal(setups:(Scale => Any)*) = { (parent : Composite) =>
    setupAndReturn(new Scale(parent, SWT.HORIZONTAL), setups : _*)
  }
  
  def scaleVertical(setups:(Scale => Any)*) = { (parent : Composite) =>
    setupAndReturn(new Scale(parent, SWT.VERTICAL), setups : _*)
  }
  
  private def setupScrolledComposite(parent: Composite, style : Int, setups : (ScrolledComposite => Any)*) = {
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
  
  def scrolledCompositeVertical(setups: (ScrolledComposite => Any)*) = { (parent : Composite) =>
    setupScrolledComposite(parent, SWT.V_SCROLL, setups : _*)
  }
  
  def scrolledCompositeHorizontal(setups: (ScrolledComposite => Any)*) = { (parent : Composite) =>
    setupScrolledComposite(parent, SWT.H_SCROLL, setups : _*)
  }
  
  def scrolledCompositeBoth(setups: (ScrolledComposite => Any)*) = { (parent : Composite) =>
    setupScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL, setups : _*)
  }
  
  def spinner(setups : (Spinner => Any)*) = { (parent : Composite) =>
    setupAndReturn(new Spinner(parent, SWT.BORDER), setups : _*)
  }
  
  def shell(setups:(Shell => Any)*) = {
    val display = Display.getDefault
    val shell = new Shell(display)
    shell.setLayout(new GridLayout());  
    
    setups.foreach(setup => setup(shell))
    shell
  }
  
  def shellNoTrim(setups:(Shell => Any)*) = {
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
  
  def sliderHorizontal(setups:(Slider => Any)*) = { (parent : Composite) =>
    setupAndReturn(new Slider(parent, SWT.HORIZONTAL), setups : _*)
  }
  
  def sliderVertical(setups:(Slider => Any)*) = { (parent : Composite) =>
    setupAndReturn(new Slider(parent, SWT.VERTICAL), setups : _*)
  }
  
//  def styledText(setups:(StyledText => Any)*) = { (parent : Composite) =>
//    setup(new StyledText(parent, SWT.NONE), setups : _*)
//  }
  
  def tabFolder(setups:(TabFolder => Any)*) = { (parent : Composite) =>
    setupAndReturn(new TabFolder(parent, SWT.NONE), setups : _*)
  }
  
  def table(setups:(Table => Any)*) = { (parent : Composite) =>
    setupAndReturn(new Table(parent, SWT.BORDER), setups : _*)
  }
  
//  def tableTree(setups:(TableTree => Any)*) = { (parent : Composite) =>
//    setup(new TableTree(parent, SWT.BORDER), setups : _*)
//  }
  
  def text(setups:(Text => Any)*)(parent:Composite) = {  
    setupAndReturn(new Text(parent, SWT.BORDER), setups : _*)  
  }
  
  def textPasswd(setups:(Text => Any)*)(parent:Composite) = {  
    setupAndReturn(new Text(parent, SWT.BORDER | SWT.PASSWORD), setups : _*)  
  }
  
  def toolBar(setups:(ToolBar => Any)*) = { (parent : Composite) =>
    setupAndReturn(new ToolBar(parent, SWT.NONE), setups : _*)
  }
  
  def tree(setups:(Tree => Any)*) = { (parent : Composite) =>
    setupAndReturn(new Tree(parent, SWT.BORDER), setups : _*)
  }
  
  def viewForm(setups:(ViewForm => Any)*) = { (parent : Composite) =>
    setupAndReturn(new ViewForm(parent, SWT.BORDER), setups : _*)
  }
  
  def viewFormFlat(setups:(ViewForm => Any)*) = { (parent : Composite) =>
    setupAndReturn(new ViewForm(parent, SWT.BORDER | SWT.FLAT), setups : _*)
  }
  
  // Items here
  
  def tabItem(setups:(TabItem => Any)*)= { (parent: Control) =>
    val tabItem = new TabItem(parent.getParent().asInstanceOf[TabFolder], SWT.NONE)
    tabItem.setControl(parent)
    setups.foreach(setup => setup(tabItem))
    tabItem
  }
  
  def coolItem(setups:(CoolItem => Any)*)= { (parent: Control) =>
    val coolItem = new CoolItem(parent.getParent().asInstanceOf[CoolBar], SWT.NONE)
    val size = parent.computeSize(SWT.DEFAULT, SWT.DEFAULT)
    coolItem.setPreferredSize(coolItem.computeSize(size.x, size.y))
    coolItem.setControl(parent)
    setups.foreach(setup => setup(coolItem))
    coolItem
  }
  
  def expandItem(setups:(ExpandItem => Any)*)= { (parent: Control) =>
    val item = new ExpandItem(parent.getParent().asInstanceOf[ExpandBar], SWT.NONE)
    item.setHeight(parent.computeSize(SWT.DEFAULT, SWT.DEFAULT).y)
    item.setControl(parent)
    setups.foreach(setup => setup(item))
    item
  }
  
  // Start with any Widget and add children from there...
  
  class WidgetX[W](w : W) {
    def apply(setups : (W => Any)*) : W = {
      setupAndReturn(w, setups : _*)
    }
    def contains(setups : (W => Any)*) : W = {
      setupAndReturn(w, setups : _*)
    }
  }
  
  implicit def widget2XScalaWT[W <: Widget](widget : W) = new WidgetX[W](widget)  

//    (setups:(Browser => Any)*)
    
  //
  // Declarative setters here
  //
  
  private type HasSetImage = { def setImage(image: Image) }
  private type HasSetText = { def setText(txt: String) }

  implicit def image2setImage[T <: HasSetImage](image: Image): T => Any = Assignments.image_=(image)
  
  implicit def string2setText[T <: HasSetText](txt: String): T => Any = Assignments.caption_=(txt)
  
  /**
   * Import Assignments._ if you want to be able to use lines like "layout = new FillLayout()", "minimum = 10", etc.
   * in your widget setup lines, instead of "_.setLayout(new FillLayout())", "_.setMinimum(10)", etc.
   * The good thing is that assignment looks nicer; the bad thing is that it fills the namespace with the
   * SWT widget properties defined here.
   */
  object Assignments {

    private def nothing: Nothing = error("this method is not meant to be called")

     // cannot use text and text_= as they are already builders for the Text widget
    def caption(implicit ev: Nothing) = nothing
    def caption_=[T <: HasSetText](txt: String) =
      (subject: T) => subject.setText(txt)
  
    def image(implicit ev: Nothing) = nothing
    def image_=[T <: HasSetImage](image: Image) =
      (subject: T) => subject.setImage(image)

    def control(implicit ev: Nothing) = nothing
    def control_=[T <: { def setControl(control: Control) }](control: Control) =
      (subject: T) => subject.setControl(control)

    def background(implicit ev: Nothing) = nothing
    def background_=[T <: { def setBackground(c: Color) }](c: Color) =
      (subject: T) => subject.setBackground(c)

    def foreground(implicit ev: Nothing) = nothing
    def foreground_=[T <: { def setForeground(c: Color) }](c: Color) =
      (subject: T) => subject.setForeground(c)

    def layout(implicit ev: Nothing) = nothing
    def layout_=[T <: { def setLayout(layout: Layout) }](layout: Layout) =
      (subject: T) => subject.setLayout(layout)
  
    def layoutData(implicit ev: Nothing) = nothing
    def layoutData_=[T <: { def setLayoutData(data: Object) }](data: Object) =
      (subject: T) => subject.setLayoutData(data)
  
    def minimum(implicit ev: Nothing) = nothing
    def minimum_=[T <: { def setMinimum(value: Int) }](value: Int) =
      (subject: T) => subject.setMinimum(value)
  
    def maximum(implicit ev: Nothing) = nothing
    def maximum_=[T <: { def setMaximum(value: Int) }](value: Int) =
      (subject: T) => subject.setMaximum(value)
  
    def selection(implicit ev: Nothing) = nothing
    def selection_=[T <: { def setSelection(value: Int) }](value: Int) =
      (subject: T) => subject.setSelection(value)
  }

  //
  // Event handling here
  //
    
  // SelectionListener-------------------------------------------------------------
    
  private type AddSelectionListener = { def addSelectionListener(l : SelectionListener) }
  
  private class SelectionListenerForwarder(func: SelectionEvent => Any) extends SelectionAdapter {
    override def widgetSelected(e : SelectionEvent) = func(e)
    override def widgetDefaultSelected(e : SelectionEvent) = func(e)
  }
  
  def addSelectionListener[T <: AddSelectionListener](l: SelectionListener) =
    (subject: T) => subject.addSelectionListener(l)
    
  implicit def onSelection[T <: AddSelectionListener](func: SelectionEvent => Any) =
    addSelectionListener[T](func)
    
  // can't be => Any, or we lose type inference
  def onSelection[T <: AddSelectionListener](func: => Unit) =
    addSelectionListener[T]((e: SelectionEvent) => func)
  
  implicit def func2SelectionListener(func: SelectionEvent => Any): SelectionListener =
	  new SelectionListenerForwarder(func)
  
  // MouseListener-----------------------------------------------------------------

  private type AddMouseListener = { def addMouseListener(l : MouseListener) }
  
  private class MouseListenerForwarder(func: MouseEvent => Any) extends MouseAdapter {
	  override def mouseDown(e : MouseEvent) = func(e)
  }

  def addMouseListener[T <: AddMouseListener](l: MouseListener) = 
	  (subject:T) => subject.addMouseListener(l)
  
  implicit def onMouseDown[T <: AddMouseListener](func: MouseEvent => Any) =
	  addMouseListener[T](func)
	  
  def onMouseDown[T <: AddMouseListener](func: => Unit) =
    addMouseListener[T]((e: MouseEvent) => func)
  
  implicit def func2MouseListener(func: MouseEvent => Any): MouseListener =
	  new MouseListenerForwarder(func)
  
  // ModifyListener-----------------------------------------------------------------
	
  private type AddModifyListener = { def addModifyListener(l: ModifyListener) }
  
  def addModifyListener[T <: AddModifyListener](l: ModifyListener) =
	  (subject : T) => subject.addModifyListener(l)
  
  private class ModifyListenerForwarder(func: ModifyEvent => Any) extends ModifyListener {
    override def modifyText(e: ModifyEvent) = func(e)
  }
  
  implicit def onModify[T <: AddModifyListener](func: ModifyEvent => Any) = 
	  addModifyListener[T](func)
	  
	def onModify[T <: AddModifyListener](func: => Unit) = 
    addModifyListener[T]((e: ModifyEvent) => func)
  
  implicit def func2ModifyListener(func: ModifyEvent => Any): ModifyListener = 
    new ModifyListenerForwarder(func)
  
  // TraverseListener--------------------------------------------------------------
  
  private type AddTraverseListener = { def addTraverseListener(l: TraverseListener) }
  
  def addTraverseListener[T <: AddTraverseListener](l: TraverseListener) =
	  (subject: T) => subject.addTraverseListener(l)
  
  private class TraverseListenerForwarder(func: TraverseEvent => Any) extends TraverseListener {
    override def keyTraversed(e : TraverseEvent) = func(e)
  }
  
  implicit def onTraverse[T <: AddTraverseListener](func: TraverseEvent => Any) =
	  addTraverseListener[T](func)
	  
	def onTraverse[T <: AddTraverseListener](func: => Unit) =
    addTraverseListener[T]((e: TraverseEvent) => func)
  
  implicit def func2TraverseListener(func: TraverseEvent => Any): TraverseListener = 
    new TraverseListenerForwarder(func)
  
    
  // 
  // Other convenience methods
  //
  
  def display = Display.getDefault

  implicit def int2Color(swtColorConstant : Int) = display.getSystemColor(swtColorConstant)

  implicit def func2Runnable(f : => Any) = new Runnable { override def run() = f }

}

