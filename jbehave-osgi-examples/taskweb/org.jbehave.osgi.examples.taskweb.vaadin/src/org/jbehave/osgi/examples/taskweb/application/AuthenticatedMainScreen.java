/*******************************************************************************
 * Copyright (c) 2011 - 2012, Cristiano Gavião - C4Biz
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cristiano Gavião - initial API and implementation
 *******************************************************************************/
package org.jbehave.osgi.examples.taskweb.application;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class AuthenticatedMainScreen extends VerticalLayout
{
	private static final long serialVersionUID = 1L;

	public AuthenticatedMainScreen()
    {
        super();

        Subject currentUser = SecurityUtils.getSubject();
        Label label = new Label("Logged in as " +  currentUser.getPrincipal().toString());
        
        Button admin = new Button("For administrators only");
        Button user  = new Button("For users only");
        if (!currentUser.hasRole("admin"))
        {
        	admin.setEnabled(false);
        } 
        else if (!currentUser.hasRole("user"))
        {
        	user.setEnabled(false);
        }
        
        
        Button perm = new Button("For all with permission 'permission_2' only");
        if(!currentUser.isPermitted("permission_2"))
        {
        	perm.setEnabled(false);
        }
        
        
        Button logout = new Button("logout");
        logout.addListener(new TaskManagerApp.LogoutListener());

        this.addComponent(label);
        
        this.addComponent(admin);
        this.addComponent(user);
        this.addComponent(perm);
        this.addComponent(logout);
    
    }
}
