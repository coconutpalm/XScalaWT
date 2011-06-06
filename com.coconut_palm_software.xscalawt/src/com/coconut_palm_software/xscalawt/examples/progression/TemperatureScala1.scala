package com.coconut_palm_software.xscalawt.examples.temp

import org.eclipse.swt.SWT
import org.eclipse.swt.widgets._
import org.eclipse.swt.layout._
import org.eclipse.swt.events._
import org.eclipse.jface.layout._

class TemperatureScala1(parent: Composite, style : Int) extends Composite(parent, style) {
  def applyLayoutData(c : Control) = GridDataFactory.defaultsFor(c).applyTo(c)
  def parseDouble(s : String) = java.lang.Double.parseDouble(s)
 
  setLayout(new GridLayout(2, true))
 
  new Label(this, SWT.NULL).setText("Fahrenheit")
  new Label(this, SWT.NULL).setText("Celcius")
 
  val fahrenheit = new Text(this, SWT.BORDER)
  applyLayoutData(fahrenheit)
 
  val celcius = new Text(this, SWT.BORDER)
  applyLayoutData(celcius)
 
  val fahrenheitToCelcius = new Button(this, SWT.PUSH)
  fahrenheitToCelcius.setText("Fareinheight -> Celcius")
  fahrenheitToCelcius.addSelectionListener(new SelectionAdapter() {
    override def widgetSelected(e : SelectionEvent) = {
      val fDouble = parseDouble(fahrenheit.getText())
      val cDouble = (5.0/9.0) * (fDouble-32)
      celcius.setText(cDouble.toString)
    }
  })
  applyLayoutData(fahrenheitToCelcius)
 
  val celciusToFahrenheit = new Button(this, SWT.PUSH)
  celciusToFahrenheit.setText("Celcius -> Fareinheight")
  celciusToFahrenheit.addSelectionListener(new SelectionAdapter() {
    override def widgetSelected(e : SelectionEvent) = {
      val cDouble = parseDouble(celcius.getText())
      val fDouble = (9.0/5.0) * cDouble + 32
      fahrenheit.setText(fDouble.toString)
    }
  })
  applyLayoutData(celciusToFahrenheit)
}