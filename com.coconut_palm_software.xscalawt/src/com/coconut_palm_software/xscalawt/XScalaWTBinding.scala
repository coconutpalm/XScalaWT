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

import java.beans.PropertyChangeListener

import org.eclipse.swt.SWT
import org.eclipse.swt.widgets._
import org.eclipse.core.databinding.observable.value.IObservableValue
import org.eclipse.core.databinding.beans.BeansObservables
import org.eclipse.core.databinding.beans.PojoObservables
import org.eclipse.jface.databinding.swt.SWTObservables
import org.eclipse.jface.databinding.swt.ISWTObservableValue
import org.eclipse.core.databinding.DataBindingContext
import org.eclipse.core.databinding.UpdateValueStrategy
import org.eclipse.core.databinding.observable.Realm

import XScalaWTAPI._

object XScalaWTBinding {
  val DBC_KEY="DBC_KEY"
  
  /**
   * Set the default data biding threading realm and run a function with that 
   * realm set.
   * 
   * @param realm The realm to set as the default
   * @param f A function (f : => Any) to execute with realm set
   */
  def runWithRealm(realm : Realm)(f : => Any) =
    Realm.runWithDefault(realm, new Runnable() {
      override def run() = f
    })

  /**
   * Set the default data biding threading realm and run a function with that 
   * realm set.
   * 
   * @param f A function (f : => Any) to execute with the SWT default realm set
   */
  def runWithDefaultRealm(f : => Any) =
    Realm.runWithDefault(SWTObservables.getRealm(Display.getDefault), new Runnable() {
      override def run() = f
    })

  /**
   * Set the default data biding threading realm and run a function with that 
   * realm set.
   * 
   * @param realm The SWT Display whose realm should be set
   * @param f A function (f : => Any) to execute with realm set
   */
  def runWithSWTRealm(display : Display)(f : => Any) =
    Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
      override def run() = f
    })

  /**
   * Associate a data binding context with this level of the containership 
   * hierarchy and all children.
   */
  def dataBindingContext(setups:(DataBindingContext => Any)*)(parent: Control) = {
    val dbc = new DataBindingContext()
    parent.setData(DBC_KEY, dbc)
    setupAndReturn(dbc, setups : _*)
  }

  class BeanObservable(bean: Object) {
    /**
     * Convert a property of this object into a JavaBean observable value.
     *
     * @param property a Symbol naming the property to observe
     * @return An IObservableValue on the specified property of the receiver
     */
    def -->(property: Symbol) = BeansObservables.observeValue(bean, property.name)

    /**
     * Convert a property of this object into a JavaBean observable list.
     *
     * @param property a Symbol naming the property to observe
     * @return An IObservableList on the specified property of the receiver
     */
    def -->>(property: Symbol) = BeansObservables.observeList(Realm.getDefault(), bean, property.name)
  }

  class PojoObservable(pojo: Object) {
    /**
     * Convert a property of this object into a POJO observable value.
     *
     * @param property a Symbol naming the property to observe
     * @return An IObservableValue on the specified property of the receiver
     */
    def -->(property: Symbol) = PojoObservables.observeValue(pojo, property.name)

    /**
     * Convert a property of this object into a POJO observable list.
     *
     * @param property a Symbol naming the property to observe
     * @return An IObservableList on the specified property of the receiver
     */
    def -->>(property: Symbol) = PojoObservables.observeList(Realm.getDefault(), pojo, property.name)

  }

  /**
   * Used to determine whether to observe an object as a BeanObservable or PojoObservable
   */
  private type HasBeanPropertyChangeSupport = {
    def addPropertyChangeListener(l: PropertyChangeListener): Unit
    def removePropertyChangeListener(l: PropertyChangeListener): Unit
  }
  
  implicit def objectToBeanObservableValue[T <: HasBeanPropertyChangeSupport](bean: T) = new BeanObservable(bean)
  implicit def objectToPojoObservableValue(pojo: Object) = new PojoObservable(pojo)

  private def findDBC(c : Control) : DataBindingContext = {
    if (c == null) return new DataBindingContext()
    val maybeDBC = c.getData(DBC_KEY)
    if (maybeDBC != null) maybeDBC.asInstanceOf[DataBindingContext]
    else findDBC(c.getParent)
  }

  class BindableValue(target : ISWTObservableValue) {
    /**
     * Bind two observables together using default update strategy objects.
     * This is a synonmym for #bindTo(IObservableValue)
     */
    def <=> (model : IObservableValue) = bindTo(model)
    
    /**
     * Bind two observables together using default update strategy objects.
     */
    def bindTo(model : IObservableValue) = {
      val dbc = findDBC(target.getWidget.asInstanceOf[Control])
      target.getWidget.setData(DBC_KEY, dbc)
      dbc.bindValue(target, model, null, null)
    }

    /**
     * Bind two observables together using explicit update strategy objects.
     */
    def bindTo(model : IObservableValue, target2Model : UpdateValueStrategy, model2target : UpdateValueStrategy) = {
      findDBC(target.getWidget.asInstanceOf[Control]).bindValue(target, model, target2Model, model2target)
    }
  }
  implicit def SWTObservable2Bindable(target : ISWTObservableValue) = 
    new BindableValue(target)
  
  
  class _ControlObservables(c : Control) {
    /**
     * @return an observable value tracking the enabled state of the given
     *         control
     */
    def enabledObservable = SWTObservables.observeEnabled(c)
    
    /**
     * @return an observable value tracking the visible state of the given
     *         control
     */
    def visibleObservable = SWTObservables.observeVisible(c)
    
	/**
	 * @param control
	 * @return an observable value tracking the tooltip text of the given
	 *         control
	 */
    def tooltipTextObservable = SWTObservables.observeTooltipText(c)
    
	/**
	 * @return an observable value tracking the foreground color of the given
	 *         control
	 */
    def foregroundObservable = SWTObservables.observeForeground(c)
    
	/**
	 * @return an observable value tracking the background color of the given
	 *         control
	 */
    def backgroundObservable = SWTObservables.observeBackground(c)
    
	/**
	 * @return an observable value tracking the font of the given control
	 */
    def fontObservable = SWTObservables.observeFont(c)
  }
  implicit def control2observable(c: Control) = new _ControlObservables(c)

  class _TextObservables(t : Text) {
	/**
	 * Returns an observable observing the text attribute of the provided
	 * <code>control</code>. 
     * 
	 * @param event event type to register for change events
	 * @return observable value
	 */
    def textObservable(event : Int) = SWTObservables.observeText(t, event)

	/**
	 * Returns an observable observing the text attribute of the provided
	 * <code>control</code>. 
     * 
	 * @return observable value
	 */
    def textObservable() = SWTObservables.observeText(t, SWT.Modify)

	/**
	 * Returns an observable observing the editable attribute of
	 * the provided <code>control</code>.
	 * 
	 * @return observable value
	 */
    def editableObservable = SWTObservables.observeEditable(t)
  }
  implicit def text2TextObservables(t : Text) = new _TextObservables(t)

  class _LabelObservables(l : Label) {
	/**
	 * Returns an observable observing the text attribute of the provided
	 * <code>control</code>. 
     * 
	 * @return observable value
	 */
    def textObservable = SWTObservables.observeText(l)
  }
  implicit def label2LabelObservables(l : Label) = new _LabelObservables(l)
  
  class _LinkObservables(l : Link) {
  	/**
	 * Returns an observable observing the text attribute of the provided
	 * <code>control</code>. 
     * 
	 * @return observable value
	 */
    def textObservable = SWTObservables.observeText(l)
  }
  implicit def link2LinkObservables(l : Link) = new _LinkObservables(l)
  
  class _ShellObservables(s : Shell) {
  	/**
	 * Returns an observable observing the text attribute of the provided
	 * <code>control</code>. 
     * 
	 * @return observable value
	 */
    def textObservable = SWTObservables.observeText(s)
  }
  implicit def shell2ShellObservables(s : Shell) = new _ShellObservables(s)

  class _ButtonObservables(b : Button) {
	/**
	 * Returns an observable observing the selection attribute of the provided
	 * <code>control</code>.
	 * 
	 * @return observable value
	 */
    def selectionObservable = SWTObservables.observeSelection(b)
  }
  implicit def button2ButtonObservables(b : Button) = new _ButtonObservables(b)
  
  class _SpinnerObservables(s : Spinner) {
	/**
	 * Returns an observable observing the minimum attribute of the provided
	 * <code>control</code>.
	 * @return observable value
	 */
    def minObservable() = SWTObservables.observeMin(s)

  	/**
	 * Returns an observable observing the maximum attribute of the provided
	 * <code>control</code>.
	 * @return observable value
	 */
    def maxObservable() = SWTObservables.observeMax(s)

	/**
	 * Returns an observable observing the selection attribute of the provided
	 * <code>control</code>.
	 * 
	 * @return observable value
	 */
    def selectionObservable = SWTObservables.observeSelection(s)
  }
  implicit def control2MinMax1(s : Spinner) = new _SpinnerObservables(s)
  
  class _ScaleObservables(s : Scale) {
	/**
	 * Returns an observable observing the minimum attribute of the provided
	 * <code>control</code>.
	 * @return observable value
	 */
    def minObservable() = SWTObservables.observeMin(s)

  	/**
	 * Returns an observable observing the maximum attribute of the provided
	 * <code>control</code>.
	 * @return observable value
	 */
    def maxObservable() = SWTObservables.observeMax(s)
    
	/**
	 * Returns an observable observing the selection attribute of the provided
	 * <code>control</code>.
	 * 
	 * @return observable value
	 */
    def selectionObservable = SWTObservables.observeSelection(s)
  }
  implicit def control2MinMax1(s : Scale) = new _ScaleObservables(s)

  class _ComboObservables(c : Combo) {
	/**
	 * Returns an observable observing the items attribute of the provided
	 * <code>control</code>. 
	 * 
	 * @return observable list
	 */
    def itemsObservable = SWTObservables.observeItems(c)
    
	/**
	 * Returns an observable observing the single selection index attribute of
	 * the provided <code>control</code>. 
	 * 
	 * @return observable value
	 */
    def singleSelectionIndexObservable = SWTObservables.observeSingleSelectionIndex(c)

	/**
	 * Returns an observable observing the text attribute of the provided
	 * <code>control</code>. 
     * 
	 * @return observable value
	 */
    def textObservable = SWTObservables.observeText(c)
    
	/**
	 * Returns an observable observing the selection attribute of the provided
	 * <code>control</code>.
	 * 
	 * @return observable value
	 */
    def selectionObservable = SWTObservables.observeSelection(c)
  }
  implicit def combo2ComboObservables(c : Combo) = new _ComboObservables(c)
  
//  class _CComboObservables(c : CCombo) {
//	/**
//	 * Returns an observable observing the items attribute of the provided
//	 * <code>control</code>. 
//	 * 
//	 * @return observable list
//	 */
//    def itemsObservable = SWTObservables.observeItems(c)
//
//	/**
//	 * Returns an observable observing the single selection index attribute of
//	 * the provided <code>control</code>. 
//	 * 
//	 * @return observable value
//	 */
//    def singleSelectionIndexObservable = SWTObservables.observeSingleSelectionIndex(c)
//
//	/**
//	 * Returns an observable observing the selection attribute of the provided
//	 * <code>control</code>.
//	 * 
//	 * @return observable value
//	 */
//    def selectionObservable = SWTObservables.observeSelection(c)
//
//    /**
//	 * Returns an observable observing the text attribute of the provided
//	 * <code>control</code>. 
//     * 
//	 * @return observable value
//	 */
//    def textObservable = SWTObservables.observeText(c)
//  }
//  implicit def ccombo2CComboObservables(c : CCombo) = new _CComboObservables(c)
  
  class _ListObservables(l : List) {
	/**
	 * Returns an observable observing the items attribute of the provided
	 * <code>control</code>. 
	 * 
	 * @return observable list
	 */
    def itemsObservable = SWTObservables.observeItems(l)

	/**
	 * Returns an observable observing the single selection index attribute of
	 * the provided <code>control</code>. 
	 * 
	 * @return observable value
	 */
    def singleSelectionIndexObservable = SWTObservables.observeSingleSelectionIndex(l)

	/**
	 * Returns an observable observing the selection attribute of the provided
	 * <code>control</code>.
	 * 
	 * @return observable value
	 */
    def selectionObservable = SWTObservables.observeSelection(l)
  }
  implicit def list2ListObservables(l : List) = new _ListObservables(l)
  
  class _TableObservables(t : Table) {
	/**
	 * Returns an observable observing the single selection index attribute of
	 * the provided <code>control</code>. 
	 * 
	 * @return observable value
	 */
    def singleSelectionIndexObservable = SWTObservables.observeSingleSelectionIndex(t)
  }
  implicit def table2TableObservables(t : Table) = new _TableObservables(t)
}
