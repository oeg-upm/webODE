package es.upm.fi.dia.ontology.webode.ui.designer;

import java.awt.*;
import es.upm.fi.dia.ontology.webode.ui.designer.model.*;

public interface RelationRenderer
{
    /**
     * Renders the relation.
     *
     */
    void render (Designer designer, Relation rel, boolean selected, Graphics g,boolean a, boolean b, boolean c);
     
    void render (Designer designer, Relation rel, boolean selected, Graphics g);

    void render (Designer designer, Relation rel, int destX, int destY, int widthDes, Graphics g);

    /**
     * Is it inside?
     */
    boolean isWithin (Designer designer, Relation rel, int x, int y);
}
