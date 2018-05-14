package es.upm.fi.dia.ontology.webode.ui.designer.model;

public interface DesignModelListener extends java.util.EventListener
{
    void relationAdded (DesignEvent de);
    void elementAdded (DesignEvent de);
    void metapropertiesAdded (DesignEvent de);
    void relationRemoved (DesignEvent de);
    void elementRemoved (DesignEvent de);
}
