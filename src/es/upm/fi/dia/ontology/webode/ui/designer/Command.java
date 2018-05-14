package es.upm.fi.dia.ontology.webode.ui.designer;

/**
 * The interface containing all operations codes.
 *
 * @author  Julio César Arpírez Vega
 * @version 0.1
 */
public interface Command 
{
    int RETRIEVE_VIEW = 1;
    int ADD_VIEW      = 2;
    int DELETE_VIEW   = 3;
    int COMMIT        = 4;
    int RETRIEVE_DEFAULT = 5;

    // Return codes
    int OK            = 0;
    int ERROR         = -1;
}
