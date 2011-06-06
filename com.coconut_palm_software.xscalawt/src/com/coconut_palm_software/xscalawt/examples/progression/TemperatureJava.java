package com.coconut_palm_software.xscalawt.examples.temp;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TemperatureJava extends Composite {
	public TemperatureJava(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, true));
 
		new Label(this, SWT.NULL).setText("Fahrenheit");
		new Label(this, SWT.NULL).setText("Celcius");
 
		final Text fahrenheit = new Text(this, SWT.BORDER);
		applyLayoutData(fahrenheit);
 
		final Text celcius = new Text(this, SWT.BORDER);
		applyLayoutData(celcius);
 
		Button fahrenheitToCelcius = new Button(this, SWT.PUSH);
		fahrenheitToCelcius.setText("Fareinheight -> Celcius");
		fahrenheitToCelcius.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				double fDouble = parseDouble(fahrenheit.getText());
				double cDouble = (5.0 / 9.0) * (fDouble - 32);
				celcius.setText(Double.toString(cDouble));
			}
		});
		applyLayoutData(fahrenheitToCelcius);
 
		Button celciusToFahrenheit = new Button(this, SWT.PUSH);
		celciusToFahrenheit.setText("Celcius -> Fareinheight");
		celciusToFahrenheit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				double cDouble = parseDouble(celcius.getText());
				double fDouble = (9.0 / 5.0) * cDouble + 32;
				fahrenheit.setText(Double.toString(fDouble));
			}
		});
		applyLayoutData(celciusToFahrenheit);
	}
 
	private void applyLayoutData(Control c) {
		GridDataFactory.defaultsFor(c).applyTo(c);
	}
 
	private double parseDouble(String s) {
		return Double.parseDouble(s);
	}
}