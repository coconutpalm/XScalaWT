/**
 * *****************************************************************************
 * Copyright (c) 2009 David Orme and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     David Orme - initial API and implementation
 * *****************************************************************************
 */
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
import org.eclipse.jface.layout.GridDataFactory

import XScalaWTAPI._

object XScalaWT {
  /**
   * The manual, "specify everything" syntax.  Useful when none of the convenience methods works.
   * <p>
   * Note, depends on ClassManifest[T] which is still experimental.
   */
  def *[T: ClassManifest](style: Int)(setups: (T => Any)*) = { (parent: Composite) =>
    val controlClass: Class[T] = classManifest[T].erasure.asInstanceOf[Class[T]]
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

  def browser(setups: (Browser => Any)*) = { (parent: Composite) =>
    setupAndReturn(new Browser(parent, SWT.NONE), setups: _*)
  }

  def button(setups: (Button => Any)*) = { (parent: Composite) =>
    setupAndReturn(new Button(parent, SWT.PUSH), setups: _*)
  }

  def cBanner(setups: (CBanner => Any)*) = { (parent: Composite) =>
    setupAndReturn(new CBanner(parent, SWT.NONE), setups: _*)
  }

  //  def cCombo(setups:(CCombo => Any)*) = { (parent : Composite) =>
  //    setup(new CCombo(parent, SWT.BORDER), setups : _*)
  //  }

  def combo(setups: (Combo => Any)*) = { (parent: Composite) =>
    setupAndReturn(new Combo(parent, SWT.BORDER), setups: _*)
  }

  def coolBar(setups: (CoolBar => Any)*) = { (parent: Composite) =>
    setupAndReturn(new CoolBar(parent, SWT.NONE), setups: _*)
  }

  def cLabel(setups: (CLabel => Any)*) = { (parent: Composite) =>
    setupAndReturn(new CLabel(parent, SWT.NONE), setups: _*)
  }

  def composite(setups: (Composite => Any)*) = { (parent: Composite) =>
    val composite = new Composite(parent, SWT.NONE)
    composite.setLayout(new GridLayout())
    setups.foreach(setup => setup(composite))
    composite
  }

  def cTabFolder(setups: (CTabFolder => Any)*) = { (parent: Composite) =>
    setupAndReturn(new CTabFolder(parent, SWT.NONE), setups: _*)
  }

  def dateTime(setups: (DateTime => Any)*) = { (parent: Composite) =>
    setupAndReturn(new DateTime(parent, SWT.BORDER), setups: _*)
  }

  def expandBar(setups: (ExpandBar => Any)*) = { (parent: Composite) =>
    setupAndReturn(new ExpandBar(parent, SWT.V_SCROLL), setups: _*)
  }

  //  def glCanvas(data : GLData)(setups:(GLCanvas => Any)*)(parent:Composite) = {
  //    setup(new GLCanvas(parent, SWT.NONE, data), setups : _*)
  //  }

  def group(setups: (Group => Any)*)(parent: Composite) = {
    val group = new Group(parent, SWT.NONE)
    group.setLayout(new GridLayout());
    setups.foreach(setup => setup(group))
    group
  }

  def label(setups: (Label => Any)*)(parent: Composite) = {
    setupAndReturn(new Label(parent, SWT.NONE), setups: _*)
  }

  def labelSeparator(setups: (Label => Any)*)(parent: Composite) = {
    setupAndReturn(new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL), setups: _*)
  }

  def labelSeparatorVertical(setups: (Label => Any)*)(parent: Composite) = {
    setupAndReturn(new Label(parent, SWT.SEPARATOR | SWT.VERTICAL), setups: _*)
  }

  def link(setups: (Link => Any)*) = { (parent: Composite) =>
    setupAndReturn(new Link(parent, SWT.NONE), setups: _*)
  }

  def list(setups: (List => Any)*) = { (parent: Composite) =>
    setupAndReturn(new List(parent, SWT.BORDER), setups: _*)
  }

  def progressBar(setups: (ProgressBar => Any)*) = { (parent: Composite) =>
    setupAndReturn(new ProgressBar(parent, SWT.NONE), setups: _*)
  }

  def sashForm(setups: (SashForm => Any)*) = { (parent: Composite) =>
    setupAndReturn(new SashForm(parent, SWT.NONE), setups: _*)
  }

  def scaleHorizontal(setups: (Scale => Any)*) = { (parent: Composite) =>
    setupAndReturn(new Scale(parent, SWT.HORIZONTAL), setups: _*)
  }

  def scaleVertical(setups: (Scale => Any)*) = { (parent: Composite) =>
    setupAndReturn(new Scale(parent, SWT.VERTICAL), setups: _*)
  }

  private def setupScrolledComposite(parent: Composite, style: Int, setups: (ScrolledComposite => Any)*) = {
    val composite = new ScrolledComposite(parent, style)
    setups.foreach(setup => setup(composite))
    composite.addControlListener(new ControlAdapter() {
      override def controlResized(e: ControlEvent) = {
        val content = composite.getChildren()(0)
        if (content.isInstanceOf[Composite]) {
          content.asInstanceOf[Composite].pack
        }
      }
    })
    composite.setContent(composite.getChildren()(0))
    composite
  }

  def scrolledCompositeVertical(setups: (ScrolledComposite => Any)*) = { (parent: Composite) =>
    setupScrolledComposite(parent, SWT.V_SCROLL, setups: _*)
  }

  def scrolledCompositeHorizontal(setups: (ScrolledComposite => Any)*) = { (parent: Composite) =>
    setupScrolledComposite(parent, SWT.H_SCROLL, setups: _*)
  }

  def scrolledCompositeBoth(setups: (ScrolledComposite => Any)*) = { (parent: Composite) =>
    setupScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL, setups: _*)
  }

  def spinner(setups: (Spinner => Any)*) = { (parent: Composite) =>
    setupAndReturn(new Spinner(parent, SWT.BORDER), setups: _*)
  }

  def shell(setups: (Shell => Any)*) = {
    val display = Display.getDefault
    val shell = new Shell(display)
    shell.setLayout(new GridLayout());

    setups.foreach(setup => setup(shell))
    shell
  }

  def shellNoTrim(setups: (Shell => Any)*) = {
    val display = Display.getDefault
    val shell = new Shell(display, SWT.NO_TRIM)
    shell.setLayout(new GridLayout());

    setups.foreach(setup => setup(shell))
    shell
  }

  def runEventLoop(window: Shell) = {
    val display = Display.getDefault
    window.open
    while (!window.isDisposed) {
      if (!display.readAndDispatch) display.sleep
    }
  }

  def sliderHorizontal(setups: (Slider => Any)*) = { (parent: Composite) =>
    setupAndReturn(new Slider(parent, SWT.HORIZONTAL), setups: _*)
  }

  def sliderVertical(setups: (Slider => Any)*) = { (parent: Composite) =>
    setupAndReturn(new Slider(parent, SWT.VERTICAL), setups: _*)
  }

  //  def styledText(setups:(StyledText => Any)*) = { (parent : Composite) =>
  //    setup(new StyledText(parent, SWT.NONE), setups : _*)
  //  }

  def tabFolder(setups: (TabFolder => Any)*) = { (parent: Composite) =>
    setupAndReturn(new TabFolder(parent, SWT.NONE), setups: _*)
  }

  def table(setups: (Table => Any)*) = { (parent: Composite) =>
    setupAndReturn(new Table(parent, SWT.BORDER), setups: _*)
  }

  //  def tableTree(setups:(TableTree => Any)*) = { (parent : Composite) =>
  //    setup(new TableTree(parent, SWT.BORDER), setups : _*)
  //  }

  def text(setups: (Text => Any)*)(parent: Composite) = {
    setupAndReturn(new Text(parent, SWT.BORDER), setups: _*)
  }

  def textPasswd(setups: (Text => Any)*)(parent: Composite) = {
    setupAndReturn(new Text(parent, SWT.BORDER | SWT.PASSWORD), setups: _*)
  }

  def toolBar(setups: (ToolBar => Any)*) = { (parent: Composite) =>
    setupAndReturn(new ToolBar(parent, SWT.NONE), setups: _*)
  }

  def tree(setups: (Tree => Any)*) = { (parent: Composite) =>
    setupAndReturn(new Tree(parent, SWT.BORDER), setups: _*)
  }

  def viewForm(setups: (ViewForm => Any)*) = { (parent: Composite) =>
    setupAndReturn(new ViewForm(parent, SWT.BORDER), setups: _*)
  }

  def viewFormFlat(setups: (ViewForm => Any)*) = { (parent: Composite) =>
    setupAndReturn(new ViewForm(parent, SWT.BORDER | SWT.FLAT), setups: _*)
  }

  // Items here

  def tabItem(setups: (TabItem => Any)*) = { (parent: Control) =>
    val tabItem = new TabItem(parent.getParent().asInstanceOf[TabFolder], SWT.NONE)
    tabItem.setControl(parent)
    setups.foreach(setup => setup(tabItem))
    tabItem
  }

  def coolItem(setups: (CoolItem => Any)*) = { (parent: Control) =>
    val coolItem = new CoolItem(parent.getParent().asInstanceOf[CoolBar], SWT.NONE)
    val size = parent.computeSize(SWT.DEFAULT, SWT.DEFAULT)
    coolItem.setPreferredSize(coolItem.computeSize(size.x, size.y))
    coolItem.setControl(parent)
    setups.foreach(setup => setup(coolItem))
    coolItem
  }

  def expandItem(setups: (ExpandItem => Any)*) = { (parent: Control) =>
    val item = new ExpandItem(parent.getParent().asInstanceOf[ExpandBar], SWT.NONE)
    item.setHeight(parent.computeSize(SWT.DEFAULT, SWT.DEFAULT).y)
    item.setControl(parent)
    setups.foreach(setup => setup(item))
    item
  }

  implicit def widget2XScalaWT[W <: Widget](widget: W) = new WidgetX[W](widget)

  // Layouts here

  def fillLayout(setups: (FillLayout => Any)*) = (c: Composite) => {
    c.setLayout(setupAndReturn(new FillLayout, setups: _*))
  }

  def rowLayout(setups: (RowLayout => Any)*) = (c: Composite) => {
    c.setLayout(setupAndReturn(new RowLayout, setups: _*))
  }

  def vertical = (_: Layout) match {
    case row: RowLayout => row.`type` = SWT.VERTICAL
    case fill: FillLayout => fill.`type` = SWT.VERTICAL
    case _ => throw new IllegalArgumentException("Wrong layout class")
  }

  def horizontal = (_: Layout) match {
    case row: RowLayout => row.`type` = SWT.HORIZONTAL
    case fill: FillLayout => fill.`type` = SWT.HORIZONTAL
    case _ => throw new IllegalArgumentException("Wrong layout class")
  }

  def formLayout(setups: (FormLayout => Any)*) = (c: Composite) => {
    c.setLayout(setupAndReturn(new FormLayout, setups: _*))
  }

  def formData(width: Int = SWT.DEFAULT, height: Int = SWT.DEFAULT)(setups: (FormData => Any)*) = (c: Control) => {
    c.setLayoutData(setupAndReturn(new FormData(width, height), setups: _*))
  }

  def gridLayout(columns: Int = 1, equalWidth: Boolean = false)(setups: (GridLayout => Any)*) = (c: Composite) => {
    c.setLayout(setupAndReturn(new GridLayout(columns, equalWidth), setups: _*))
  }

  def defaultGridData = (c: Control) => GridDataFactory.defaultsFor(c).applyTo(c)

  def modifiedDefaultGridData(setups: GridDataFactory => GridDataFactory*) =
    (c: Control) => setups.foldRight(GridDataFactory.defaultsFor(c))(_(_)).applyTo(c)

  def fillGridData = (c: Control) => GridDataFactory.fillDefaults().applyTo(c)

  def modifiedFillGridData(setups: GridDataFactory => GridDataFactory*) =
    (c: Control) => setups.foldRight(GridDataFactory.fillDefaults())(_(_)).applyTo(c)

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

  case class PimpGetText[T <: { def getText(): String; def setText(text: String) }](control: T) {
    def text = control.getText
    def text_=(text: String) { control.setText(text) }
  }
  implicit def pimpGetText[T <: { def getText(): String; def setText(text: String) }](control: T) = new PimpGetText(control)

  case class PimpGetImage[T <: { def getImage(): Image; def setImage(image: Image) }](control: T) {
    def image = control.getImage
    def image_=(image: Image) { control.setImage(image) }
  }
  implicit def pimpGetImage[T <: { def getImage(): Image; def setImage(image: Image) }](control: T) = new PimpGetImage(control)

  case class PimpGetControl[T <: { def getControl(): Control; def setControl(control: Control) }](c: T) {
    def control = c.getControl
    def control_=(control: Control) { c.setControl(control) }
  }
  implicit def pimpGetControl[T <: { def getControl(): Control; def setControl(control: Control) }](control: T) = new PimpGetControl(control)

  case class PimpGetBackground[T <: { def getBackground(): Color; def setBackground(background: Color) }](control: T) {
    def background = control.getBackground
    def background_=(background: Color) { control.setBackground(background) }
  }
  implicit def pimpGetBackground[T <: { def getBackground(): Color; def setBackground(background: Color) }](control: T) = new PimpGetBackground(control)

  case class PimpGetForeground[T <: { def getForeground(): Color; def setForeground(foreground: Color) }](control: T) {
    def foreground = control.getForeground
    def foreground_=(foreground: Color) { control.setForeground(foreground) }
  }
  implicit def pimpGetForeground[T <: { def getForeground(): Color; def setForeground(foreground: Color) }](control: T) = new PimpGetForeground(control)

  case class PimpGetLayout[T <: { def getLayout(): Layout; def setLayout(layout: Layout) }](control: T) {
    def layout = control.getLayout
    def layout_=(layout: Layout) { control.setLayout(layout) }
  }
  implicit def pimpGetLayout[T <: { def getLayout(): Layout; def setLayout(layout: Layout) }](control: T) = new PimpGetLayout(control)

  case class PimpGetLayoutData[T <: { def getLayoutData(): AnyRef; def setLayoutData(layoutData: AnyRef) }](control: T) {
    def layoutData = control.getLayoutData
    def layoutData_=(layoutData: AnyRef) { control.setLayoutData(layoutData) }
  }
  implicit def pimpGetLayoutData[T <: { def getLayoutData(): AnyRef; def setLayoutData(layoutData: AnyRef) }](control: T) = new PimpGetLayoutData(control)

  // for JFace viewers
  case class PimpGetInput[T <: { def getInput(): AnyRef; def setInput(input: AnyRef) }](control: T) {
    def input = control.getInput
    def input_=(input: AnyRef) { control.setInput(input) }
  }
  implicit def pimpGetInput[T <: { def getInput(): AnyRef; def setInput(input: AnyRef) }](control: T) = new PimpGetInput(control)

  //
  // Event handling here
  //

  // SelectionListener-------------------------------------------------------------

  private type AddSelectionListener = { def addSelectionListener(l: SelectionListener) }

  def addSelectionListener[T <: AddSelectionListener](l: SelectionListener) =
    (subject: T) => subject.addSelectionListener(l)

  implicit def selectionListener(func: SelectionEvent => Any): SelectionListener =
    selectionListener(func, func)

  def selectionListener(normal: SelectionEvent => Any, defaultSelected: SelectionEvent => Any): SelectionListener =
    new SelectionListener {
      override def widgetSelected(e: SelectionEvent) { normal(e) }
      override def widgetDefaultSelected(e: SelectionEvent) { defaultSelected(e) }
    }

  def onSelection[T <: AddSelectionListener](func: SelectionEvent => Any) =
    addSelectionListener[T](func)

  def onSelection[T <: AddSelectionListener](normal: SelectionEvent => Any, defaultSelected: SelectionEvent => Any) =
    addSelectionListener[T](selectionListener(normal, defaultSelected))

  // can't be => Any, or we lose type inference
  def onSelection[T <: AddSelectionListener](func: => Unit) =
    addSelectionListener[T]((e: SelectionEvent) => func)

  implicit def onSelectionImplicit[T <: AddSelectionListener](func: SelectionEvent => Any) =
    addSelectionListener[T](func)

  // MouseListener-----------------------------------------------------------------

  private type AddMouseListener = { def addMouseListener(l: MouseListener) }

  def addMouseListener[T <: AddMouseListener](l: MouseListener) =
    (subject: T) => subject.addMouseListener(l)

  def mouseListener(doubleClick: MouseEvent => Any = ignore, down: MouseEvent => Any = ignore, up: MouseEvent => Any = ignore): MouseListener =
    new MouseListener {
      override def mouseDoubleClick(e: MouseEvent) { doubleClick(e) }
      override def mouseDown(e: MouseEvent) { down(e) }
      override def mouseUp(e: MouseEvent) { up(e) }
    }

  def onMouse[T <: AddMouseListener](doubleClick: MouseEvent => Any = ignore, down: MouseEvent => Any = ignore, up: MouseEvent => Any = ignore) =
    addMouseListener[T](mouseListener(doubleClick, down, up))

  // since we often only need to handle mouse down
  def onMouseDown[T <: AddMouseListener](func: MouseEvent => Any) =
    addMouseListener[T](func)

  def onMouseDown[T <: AddMouseListener](func: => Unit) =
    addMouseListener[T]((e: MouseEvent) => func)

  implicit def onMouseDownImplicit[T <: AddMouseListener](func: MouseEvent => Any) =
    addMouseListener[T](func)

  implicit def func2MouseListener(func: MouseEvent => Any): MouseListener =
    mouseListener(ignore, func, ignore)

  // ModifyListener-----------------------------------------------------------------

  private type AddModifyListener = { def addModifyListener(l: ModifyListener) }

  def addModifyListener[T <: AddModifyListener](l: ModifyListener) =
    (subject: T) => subject.addModifyListener(l)

  implicit def modifyListener(func: ModifyEvent => Any): ModifyListener =
    new ModifyListener {
      override def modifyText(e: ModifyEvent) = func(e)
    }

  def onModify[T <: AddModifyListener](func: ModifyEvent => Any) =
    addModifyListener[T](func)

  def onModify[T <: AddModifyListener](func: => Unit) =
    addModifyListener[T]((e: ModifyEvent) => func)

  implicit def onModifyImplicit[T <: AddModifyListener](func: ModifyEvent => Any) =
    addModifyListener[T](func)

  // TraverseListener--------------------------------------------------------------

  private type AddTraverseListener = { def addTraverseListener(l: TraverseListener) }

  def addTraverseListener[T <: AddTraverseListener](l: TraverseListener) =
    (subject: T) => subject.addTraverseListener(l)

  implicit def traverseListener(func: TraverseEvent => Any): TraverseListener =
    new TraverseListener {
      override def keyTraversed(e: TraverseEvent) = func(e)
    }

  def onTraverse[T <: AddTraverseListener](func: TraverseEvent => Any) =
    addTraverseListener[T](func)

  def onTraverse[T <: AddTraverseListener](func: => Unit) =
    addTraverseListener[T]((e: TraverseEvent) => func)

  implicit def onTraverseImplicit[T <: AddTraverseListener](func: TraverseEvent => Any) =
    addTraverseListener[T](func)

  // ArmListener-----------------------------------------------------------------

  private type AddArmListener = { def addArmListener(l: ArmListener) }

  def addArmListener[T <: AddArmListener](l: ArmListener) =
    (subject: T) => subject.addArmListener(l)

  implicit def armListener(func: ArmEvent => Any): ArmListener =
    new ArmListener {
      override def widgetArmed(e: ArmEvent) = func(e)
    }

  def onArm[T <: AddArmListener](func: ArmEvent => Any) =
    addArmListener[T](func)

  def onArm[T <: AddArmListener](func: => Unit) =
    addArmListener[T]((e: ArmEvent) => func)

  implicit def onArmImplicit[T <: AddArmListener](func: ArmEvent => Any) =
    addArmListener[T](func)

  // ControlListener-----------------------------------------------------------------

  private type AddControlListener = { def addControlListener(l: ControlListener) }

  def addControlListener[T <: AddControlListener](l: ControlListener) =
    (subject: T) => subject.addControlListener(l)

  def controlListener(moved: ControlEvent => Any, resized: ControlEvent => Any): ControlListener =
    new ControlListener {
      override def controlMoved(e: ControlEvent) = moved(e)
      override def controlResized(e: ControlEvent) = resized(e)
    }

  def onControl[T <: AddControlListener](moved: ControlEvent => Any = ignore, resized: ControlEvent => Any = ignore) =
    addControlListener[T](controlListener(moved, resized))

  // DisposeListener-----------------------------------------------------------------

  private type AddDisposeListener = { def addDisposeListener(l: DisposeListener) }

  def addDisposeListener[T <: AddDisposeListener](l: DisposeListener) =
    (subject: T) => subject.addDisposeListener(l)

  implicit def disposeListener(func: DisposeEvent => Any): DisposeListener =
    new DisposeListener {
      override def widgetDisposed(e: DisposeEvent) = func(e)
    }

  def onDispose[T <: AddDisposeListener](func: DisposeEvent => Any) =
    addDisposeListener[T](func)

  def onDispose[T <: AddDisposeListener](func: => Unit) =
    addDisposeListener[T]((e: DisposeEvent) => func)

  implicit def onDisposeImplicit[T <: AddDisposeListener](func: DisposeEvent => Any) =
    addDisposeListener[T](func)

  // DragDetectListener-----------------------------------------------------------------

  private type AddDragDetectListener = { def addDragDetectListener(l: DragDetectListener) }

  def addDragDetectListener[T <: AddDragDetectListener](l: DragDetectListener) =
    (subject: T) => subject.addDragDetectListener(l)

  implicit def dragDetectListener(func: DragDetectEvent => Any): DragDetectListener =
    new DragDetectListener {
      override def dragDetected(e: DragDetectEvent) = func(e)
    }

  def onDragDetect[T <: AddDragDetectListener](func: DragDetectEvent => Any) =
    addDragDetectListener[T](func)

  def onDragDetect[T <: AddDragDetectListener](func: => Unit) =
    addDragDetectListener[T]((e: DragDetectEvent) => func)

  implicit def onDragDetectImplicit[T <: AddDragDetectListener](func: DragDetectEvent => Any) =
    addDragDetectListener[T](func)

  // ExpandListener-----------------------------------------------------------------

  private type AddExpandListener = { def addExpandListener(l: ExpandListener) }

  def addExpandListener[T <: AddExpandListener](l: ExpandListener) =
    (subject: T) => subject.addExpandListener(l)

  def expandListener(collapsed: ExpandEvent => Any, expanded: ExpandEvent => Any): ExpandListener =
    new ExpandListener {
      def itemCollapsed(e: ExpandEvent) = collapsed(e)
      def itemExpanded(e: ExpandEvent) = expanded(e)
    }

  def onExpand[T <: AddExpandListener](collapsed: ExpandEvent => Any = ignore, expanded: ExpandEvent => Any = ignore) =
    addExpandListener[T](expandListener(collapsed, expanded))

  // FocusListener-----------------------------------------------------------------

  private type AddFocusListener = { def addFocusListener(l: FocusListener) }

  def addFocusListener[T <: AddFocusListener](l: FocusListener) =
    (subject: T) => subject.addFocusListener(l)

  def focusListener(gained: FocusEvent => Any = ignore, lost: FocusEvent => Any = ignore): FocusListener =
    new FocusListener {
      override def focusGained(e: FocusEvent) = gained(e)
      override def focusLost(e: FocusEvent) = lost(e)
    }

  def onFocus[T <: AddFocusListener](gained: FocusEvent => Any = ignore, lost: FocusEvent => Any = ignore) =
    addFocusListener[T](focusListener(gained, lost))

  // HelpListener-----------------------------------------------------------------

  private type AddHelpListener = { def addHelpListener(l: HelpListener) }

  def addHelpListener[T <: AddHelpListener](l: HelpListener) =
    (subject: T) => subject.addHelpListener(l)

  implicit def helpListener(func: HelpEvent => Any): HelpListener =
    new HelpListener {
      override def helpRequested(e: HelpEvent) = func(e)
    }

  def onHelp[T <: AddHelpListener](func: HelpEvent => Any) =
    addHelpListener[T](func)

  def onHelp[T <: AddHelpListener](func: => Unit) =
    addHelpListener[T]((e: HelpEvent) => func)

  implicit def onHelpImplicit[T <: AddHelpListener](func: HelpEvent => Any) =
    addHelpListener[T](func)

  // KeyListener-----------------------------------------------------------------

  private type AddKeyListener = { def addKeyListener(l: KeyListener) }

  def addKeyListener[T <: AddKeyListener](l: KeyListener) =
    (subject: T) => subject.addKeyListener(l)

  def keyListener(pressed: KeyEvent => Any = ignore, released: KeyEvent => Any = ignore): KeyListener =
    new KeyListener {
      override def keyPressed(e: KeyEvent) = pressed(e)
      override def keyReleased(e: KeyEvent) = released(e)
    }

  def onKey[T <: AddKeyListener](pressed: KeyEvent => Any = ignore, released: KeyEvent => Any = ignore) =
    addKeyListener[T](keyListener(pressed, released))

  // MenuDetectListener-----------------------------------------------------------------

  private type AddMenuDetectListener = { def addMenuDetectListener(l: MenuDetectListener) }

  def addMenuDetectListener[T <: AddMenuDetectListener](l: MenuDetectListener) =
    (subject: T) => subject.addMenuDetectListener(l)

  implicit def menuDetectListener(func: MenuDetectEvent => Any): MenuDetectListener =
    new MenuDetectListener {
      override def menuDetected(e: MenuDetectEvent) = func(e)
    }

  def onMenuDetect[T <: AddMenuDetectListener](func: MenuDetectEvent => Any) =
    addMenuDetectListener[T](func)

  def onMenuDetect[T <: AddMenuDetectListener](func: => Unit) =
    addMenuDetectListener[T]((e: MenuDetectEvent) => func)

  implicit def onMenuDetectImplicit[T <: AddMenuDetectListener](func: MenuDetectEvent => Any) =
    addMenuDetectListener[T](func)

  // MenuListener-----------------------------------------------------------------

  private type AddMenuListener = { def addMenuListener(l: MenuListener) }

  def addMenuListener[T <: AddMenuListener](l: MenuListener) =
    (subject: T) => subject.addMenuListener(l)

  def menuListener(hidden: MenuEvent => Any = ignore, shown: MenuEvent => Any = ignore): MenuListener =
    new MenuListener {
      override def menuHidden(e: MenuEvent) = hidden(e)
      override def menuShown(e: MenuEvent) = shown(e)
    }

  def onMenu[T <: AddMenuListener](hidden: MenuEvent => Any = ignore, shown: MenuEvent => Any = ignore) =
    addMenuListener[T](menuListener(hidden, shown))

  // MouseMoveListener-----------------------------------------------------------------

  private type AddMouseMoveListener = { def addMouseMoveListener(l: MouseMoveListener) }

  def addMouseMoveListener[T <: AddMouseMoveListener](l: MouseMoveListener) =
    (subject: T) => subject.addMouseMoveListener(l)

  implicit def mouseMoveListener(func: MouseEvent => Any): MouseMoveListener =
    new MouseMoveListener {
      override def mouseMove(e: MouseEvent) = func(e)
    }

  def onMouseMove[T <: AddMouseMoveListener](func: MouseEvent => Any) =
    addMouseMoveListener[T](func)

  def onMouseMove[T <: AddMouseMoveListener](func: => Unit) =
    addMouseMoveListener[T]((e: MouseEvent) => func)

  implicit def onMouseMoveImplicit[T <: AddMouseMoveListener](func: MouseEvent => Any) =
    addMouseMoveListener[T](func)

  // MouseTrackListener-----------------------------------------------------------------

  private type AddMouseTrackListener = { def addMouseTrackListener(l: MouseTrackListener) }

  def mouseTrackListener(enter: MouseEvent => Any = ignore, exit: MouseEvent => Any = ignore, hover: MouseEvent => Any = ignore): MouseTrackListener =
    new MouseTrackListener {
      override def mouseEnter(e: MouseEvent) = enter(e)
      override def mouseExit(e: MouseEvent) = exit(e)
      override def mouseHover(e: MouseEvent) = hover(e)
    }

  def addMouseTrackListener[T <: AddMouseTrackListener](l: MouseTrackListener) =
    (subject: T) => subject.addMouseTrackListener(l)

  def onMouseTrack[T <: AddMouseTrackListener](enter: MouseEvent => Any = ignore, exit: MouseEvent => Any = ignore, hover: MouseEvent => Any = ignore) =
    addMouseTrackListener[T](mouseTrackListener(enter, exit, hover))

  // MouseWheelListener-----------------------------------------------------------------

  private type AddMouseWheelListener = { def addMouseWheelListener(l: MouseWheelListener) }

  def addMouseWheelListener[T <: AddMouseWheelListener](l: MouseWheelListener) =
    (subject: T) => subject.addMouseWheelListener(l)

  implicit def mouseWheelListener(func: MouseEvent => Any): MouseWheelListener =
    new MouseWheelListener {
      override def mouseScrolled(e: MouseEvent) = func(e)
    }

  def onMouseWheel[T <: AddMouseWheelListener](func: MouseEvent => Any) =
    addMouseWheelListener[T](func)

  def onMouseWheel[T <: AddMouseWheelListener](func: => Unit) =
    addMouseWheelListener[T]((e: MouseEvent) => func)

  implicit def onMouseWheelImplicit[T <: AddMouseWheelListener](func: MouseEvent => Any) =
    addMouseWheelListener[T](func)

  // PaintListener-----------------------------------------------------------------

  private type AddPaintListener = { def addPaintListener(l: PaintListener) }

  def addPaintListener[T <: AddPaintListener](l: PaintListener) =
    (subject: T) => subject.addPaintListener(l)

  implicit def paintListener(func: PaintEvent => Any): PaintListener =
    new PaintListener {
      override def paintControl(e: PaintEvent) = func(e)
    }

  def onPaint[T <: AddPaintListener](func: PaintEvent => Any) =
    addPaintListener[T](func)

  def onPaint[T <: AddPaintListener](func: => Unit) =
    addPaintListener[T]((e: PaintEvent) => func)

  implicit def onPaintImplicit[T <: AddPaintListener](func: PaintEvent => Any) =
    addPaintListener[T](func)

  // ShellListener-----------------------------------------------------------------

  private type AddShellListener = { def addShellListener(l: ShellListener) }

  def addShellListener[T <: AddShellListener](l: ShellListener) =
    (subject: T) => subject.addShellListener(l)

  def shellListener(activated: ShellEvent => Any = ignore, closed: ShellEvent => Any = ignore, deactivated: ShellEvent => Any = ignore, deiconified: ShellEvent => Any = ignore, iconified: ShellEvent => Any = ignore): ShellListener =
    new ShellListener {
      override def shellActivated(e: ShellEvent) = activated(e)
      override def shellClosed(e: ShellEvent) = closed(e)
      override def shellDeactivated(e: ShellEvent) = deactivated(e)
      override def shellDeiconified(e: ShellEvent) = deiconified(e)
      override def shellIconified(e: ShellEvent) = iconified(e)
    }

  def onShell[T <: AddShellListener](activated: ShellEvent => Any = ignore, closed: ShellEvent => Any = ignore, deactivated: ShellEvent => Any = ignore, deiconified: ShellEvent => Any = ignore, iconified: ShellEvent => Any = ignore) =
    addShellListener[T](shellListener(activated, closed, deactivated, deiconified, iconified))

  // TreeListener-----------------------------------------------------------------

  private type AddTreeListener = { def addTreeListener(l: TreeListener) }

  def addTreeListener[T <: AddTreeListener](l: TreeListener) =
    (subject: T) => subject.addTreeListener(l)

  def treeListener(collapsed: TreeEvent => Any = ignore, expanded: TreeEvent => Any = ignore): TreeListener =
    new TreeListener {
      override def treeCollapsed(e: TreeEvent) = collapsed(e)
      override def treeExpanded(e: TreeEvent) = expanded(e)
    }

  def onTree[T <: AddTreeListener](collapsed: TreeEvent => Any = ignore, expanded: TreeEvent => Any = ignore) =
    addTreeListener[T](treeListener(collapsed, expanded))

  // VerifyListener-----------------------------------------------------------------

  private type AddVerifyListener = { def addVerifyListener(l: VerifyListener) }

  def addVerifyListener[T <: AddVerifyListener](l: VerifyListener) =
    (subject: T) => subject.addVerifyListener(l)

  implicit def verifyListener(func: VerifyEvent => Any): VerifyListener =
    new VerifyListener {
      override def verifyText(e: VerifyEvent) = func(e)
    }

  def onVerify[T <: AddVerifyListener](func: VerifyEvent => Any) =
    addVerifyListener[T](func)

  def onVerify[T <: AddVerifyListener](func: => Unit) =
    addVerifyListener[T]((e: VerifyEvent) => func)

  implicit def onVerifyImplicit[T <: AddVerifyListener](func: VerifyEvent => Any) =
    addVerifyListener[T](func)

  // 
  // Other convenience methods
  //

  implicit def display = Display.getDefault

  implicit def int2Color(swtColorConstant: Int)(implicit d: Display) =
    d.getSystemColor(swtColorConstant)

  implicit def runnable(f: => Any) = new Runnable {
    override def run() { f }
  }

  implicit def tuple2Point(pair: (Int, Int)) = new Point(pair._1, pair._2)

  def syncExecInUIThread(f: => Any)(implicit d: Display) {
    // is it worth checking if we are already on the UI thread?
    d.syncExec(f)
  }

  def asyncExecInUIThread(f: => Any)(implicit d: Display) {
    d.asyncExec(f)
  }

  import scala.util.control.Exception._

  def syncEvalInUIThread[A](f: => A)(implicit d: Display): A = {
    @volatile var result: Either[Throwable, A] = null
    d.syncExec {
      result = allCatch.either(f)
    }
    result match {
      case Left(e) => throw e
      case Right(res) => res
    }
  }
  
  
/* TODO: reimplement this with newer future class
  // import scala.parallel.future
  import scala.concurrent._ 

  def asyncEvalInUIThread[A](f: => A)(implicit d: Display): Future[A] = {
    val future = new Future[A] {
      import java.util.concurrent.CountDownLatch

      private val latch = new CountDownLatch(1)
      @volatile private var result: Option[Either[Throwable, A]] = None

      def isDone = result.isDefined

      def apply() = {
        if (!isDone) { latch.await() }

        result.get match {
          case Left(e) => throw e
          case Right(res) => res
        }
      }

      def begin() {
        d.asyncExec {
          result = Some(allCatch.either(f))
          latch.countDown()
        }
      }

    }

    future.begin()
    future
  }
*/
}