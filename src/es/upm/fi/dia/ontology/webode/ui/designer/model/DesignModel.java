package es.upm.fi.dia.ontology.webode.ui.designer.model;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The general interface for the designer model.
 *
 * @author   Julio César Arpírez Vega
 * @version  1.1
 */
public interface DesignModel
{
    /**
     * Gets all the elements in the design (only concepts, not relations).
     *
     * @return All the elements.
     */
    Element[] getElements ();
    
    String getMetaproperties (String name);

    Element getElement (String name);

    /**
     * Gets all the relationships in the design.
     *
     * @return All the relationships.
     */
    Relation[] getRelations ();

    /**
     * Gets relations for a particular node.
     *
     * @param name The element for the relations.
     */
    Relation[] getRelations (String name);

    /**
     * Gets the maximum x coordinate.
     *
     * @return The maximum x coordinate.
     */
    int getMaxX ();

    /**
     * Gets the maximum y coordinate.
     *
     * @return The maximum y coordinate.
     */
    int getMaxY ();

    /**
     * Returns the relationship between two terms.
     */
    //Relation getRelation (String origin, String destination);


    /**
     * Adds a new element.
     * 
     * @param el The element to be added.
     */
    void addElement (Element el) throws DesignException;
    
    String[] getMetaproperties ();

    void addMetaproperties (String values) throws DesignException;
    
    void addMetapropertiesI (String values) throws DesignException;
    
    void removeMetaproperties(String name);
    
    int getMetapropertiesModified();
    
    boolean metapropertiesCoherent(DefaultDesignModel ddm);
    /**
     * Removes a given element.
     *
     * @param name The element's name.
     */
    void removeElement (String name);

    /**
     * Adds a new relationship.
     *
     * @param rel The relationship to be added.
     */
    void addRelation (Relation rel) throws DesignException;

    /**
     * Removes a given relationship.
     *
     * @param rel The relation descriptor.
     */
    void removeRelation (Relation rel);

    /**
     * Gets all available groups.
     */
    public Group[] getGroups ();

    /**
     * Gets a given group.
     */
    public Group getGroup (String name);

    /**
     * Gets the groups a term belongs to.
     */
    public Group[] getGroupsFor (String element);

    /**
     * Adds a new model listener.
     *
     * @param dml The listener to be added.
     */
    public void addDesignModelListener (DesignModelListener dml);

    /**
     * Removes a model listener.
     *
     * @param dml The listener to be removed.
     */
    public void removeDesignModelListener (DesignModelListener dml);

    /**
     * Adds a listener to be notified when a group change occurs.
     */
    public void addGroupListener (GroupListener dml);
    
    /**
     * Adds a listener to be notified when a group change occurs.
     */
    public void removeGroupListener (GroupListener dml);

    /**
     * Sets the supervisor model.
     */
    public void setSupervisor (DesignModel dm);

    /**
     * Gets the supervisor model.
     */
    public DesignModel getSupervisor ();

    /**
     * Returns whether an element is within this
     * view.
     */
    public boolean containsElement (Element el);

    /**
     * Updates an element.
     */
    void updateElement (String oldName, String newName) throws DesignException;

    Relation[] getRelationsByDestination (String destination);

    Relation[] getRelationsByOrigin (String origin);
    
    
}












