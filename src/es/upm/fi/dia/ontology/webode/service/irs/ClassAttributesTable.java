package es.upm.fi.dia.ontology.webode.service.irs;

/* importaciones */
import java.util.*;
import java.rmi.*;
import java.sql.*;
import es.upm.fi.dia.ontology.webode.service.*;

public class ClassAttributesTable extends Object /* implements intafaz */ {

public ClassAttributesTable() {
};

public Vector getRow(ODEService ode, String ontology) throws SQLException, WebODEException, RemoteException
    {
    try{

	Vector myVector= new Vector(48,4);
	Term[] conceptos;
 	//Get all concepts names of the ontology
	conceptos = ode.getTerms (ontology, new int [] {TermTypes.CONCEPT});
 	if (conceptos != null) {
		for (int i=0; i<conceptos.length; i++) {
			ClassAttributeDescriptor[] atributos=ode.getClassAttributes(ontology,conceptos[i].term);
 			if(atributos!=null){
 				int j=0;
 				while (j<atributos.length) {
						myVector.addElement(atributos[j].name);

						if (atributos[j].values==null){
							myVector.addElement(null);
						}else{
							Vector auxVector= new Vector(6,2);
							for (int ind=0; ind<atributos[j].values.length; ind++){
								auxVector.addElement(atributos[j].values[ind]);
							}/*End for*/
							myVector.addElement(auxVector);
						}/*End else*/

						myVector.addElement(atributos[j].termName);
                                                myVector.addElement(atributos[j].valueTypeName);
						myVector.addElement(atributos[j].measurementUnit);
						myVector.addElement(atributos[j].precision);
						if ((atributos[j].maxCardinality)==(-1)){
							myVector.addElement("(" + atributos[j].minCardinality + "," + "n" + ")");
						}else{
							myVector.addElement("(" + atributos[j].minCardinality + "," + atributos[j].maxCardinality + ")");
						}
				  		myVector.addElement("Not defined yet");
						j++;
						//myVector.addElement(atributos[j].???????);
					//}/*End if*/
				}/*End while*/
			}/*End if*/
		}/*End for*/
	}else{
		myVector.addElement("Empty");
	}/*End if*/
	return myVector;

}catch (Exception e){
	System.out.println("ERROR");
System.out.println(e.getMessage());
return null;
}
};

}