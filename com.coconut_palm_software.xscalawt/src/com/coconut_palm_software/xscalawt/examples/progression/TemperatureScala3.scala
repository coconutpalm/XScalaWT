package com.coconut_palm_software.xscalawt.examples.temp

import org.eclipse.swt.SWT
import org.eclipse.swt.widgets._
import org.eclipse.swt.layout._
import org.eclipse.swt.events._
import org.eclipse.jface.layout._

class Temperature3(parent: Composite, style : Int) extends Composite(parent, style) {
  def applyLayoutData(c : Control) = GridDataFactory.defaultsFor(c).applyTo(c)
  implicit def unboxText2Double(t : Text) = java.lang.Double.parseDouble(t.getText())
  implicit def convertDouble2String(d : Double) = d.toString
  implicit def func2SelectionListener(handler : (SelectionEvent => Unit) ) = new SelectionAdapter() {
    override def widgetSelected(e : SelectionEvent) = handler(e)
  }
 
  setLayout(new GridLayout(2, true))
 
  new Label(this, SWT.NULL).setText("Fahrenheit")
  new Label(this, SWT.NULL).setText("Celcius")
 
  val fahrenheit = new Text(this, SWT.BORDER)
  applyLayoutData(fahrenheit)
 
  val celcius = new Text(this, SWT.BORDER)
  applyLayoutData(celcius)
 
  val fahrenheitToCelcius = new Button(this, SWT.PUSH)
  fahrenheitToCelcius.setText("Fareinheight -> Celcius")
  fahrenheitToCelcius.addSelectionListener(
		  {e : SelectionEvent => celcius.setText((5.0/9.0) * (fahrenheit - 32)) })
  applyLayoutData(fahrenheitToCelcius)
 
  val celciusToFahrenheit = new Button(this, SWT.PUSH)
  celciusToFahrenheit.setText("Celcius -> Fareinheight")
  celciusToFahrenheit.addSelectionListener(
		  {e : SelectionEvent => fahrenheit.setText((9.0/5.0) * celcius + 32) })
  applyLayoutData(celciusToFahrenheit)
}