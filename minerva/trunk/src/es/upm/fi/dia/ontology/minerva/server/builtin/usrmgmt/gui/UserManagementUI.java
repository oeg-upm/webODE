package es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt.gui;

import javax.swing.*;
import java.awt.*;

import es.upm.fi.dia.ontology.minerva.server.builtin.*;
import es.upm.fi.dia.ontology.minerva.server.builtin.usrmgmt.*;
import es.upm.fi.dia.ontology.minerva.server.management.*;

/**
 * The container for all the widget to manage users, groups and services.
 *
 * @author  Julio César Arpírez Vega.
 * @version 0.5
 */
public class UserManagementUI extends MinervaUI
{
    public static final String USERS_IMAGE    = "images/user.gif";
    public static final String GROUPS_IMAGE   = "images/group.gif";
    public static final String SERVICES_IMAGE = "images/service.gif";

    private Icon usersImage, groupsImage, servicesImage;

    public UserManagementUI ()
    {
	usersImage    = new ImageIcon (USERS_IMAGE);
	groupsImage   = new ImageIcon (GROUPS_IMAGE);
	servicesImage = new ImageIcon (SERVICES_IMAGE);
    }
    
    public void setManager (MinervaManager manager)
    {
	removeAll();
	
	UserPanel userPanel = new UserPanel ();
	ServicePanel servicePanel = new ServicePanel();
	GroupPanel groupPanel = new GroupPanel(userPanel, servicePanel);

	setLayout (new BorderLayout());
	JTabbedPane jtp = new JTabbedPane ();
	jtp.addTab ("Users", usersImage, userPanel, "User Management Panel");
	jtp.addTab ("Groups", groupsImage, groupPanel, "Group Management Panel");
	jtp.addTab ("Service Access", servicesImage, servicePanel, "Service Access Management");

	userPanel.setManager (manager);
	groupPanel.setManager (manager);
	servicePanel.setManager (manager);

	add (jtp);
    }
}
