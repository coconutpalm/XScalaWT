package com.coconut_palm_software.xscalawt.examples.temp

import com.coconut_palm_software.xscalawt.XScalaWT._
import org.eclipse.swt.widgets._
import org.eclipse.swt.layout._
import org.eclipse.swt.events._

class Temperature4(parent: Composite, style : Int) extends Composite(parent, style) {
  implicit def unboxText2Double(t : Text) = java.lang.Double.parseDouble(t.getText())
  implicit def convertDouble2String(d : Double) = d.toString
 
  var fahrenheit : Text = null
  var celcius : Text = null
 
  this.contains (
    _.setLayout(new GridLayout(2, true)),
 
    label("Fahrenheit"),
    label("Celcius"),
 
    text(fahrenheit = _),
    text(celcius = _),
 
    button("Fahrenheit -> Celcius", onSelection {e => celcius.text = ((5.0/9.0) * (fahrenheit - 32)) }),
    button("Celcius -> Fahrenheit", {e : SelectionEvent => fahrenheit.text = ((9.0/5.0) * celcius + 32) })
  )
}
