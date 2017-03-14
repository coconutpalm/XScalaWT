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
package examples.binding

import scala.beans.BeanProperty

class LoginViewModel {
  @BeanProperty
  var username : String = "";

  @BeanProperty
  var password : String = "";

  var loggedIn = false
  def login() = { loggedIn = true }
}
