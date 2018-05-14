package es.upm.fi.dia.ontology.webode.translat.internalstructure;
import java.io.*;


public class ISStructureBuilder
{
  public static final int DAML = 0;
  public static final int RDFS = 1;
  public static final int OWL_Lite = 2;
  public static final int OWL_DL = 3;
  public static final int OWL_Full = 4;

  public ISStructure buildISStructure(int type,Reader in,String name,String namespace)
  {
    ISStructure structure=null;
    switch (type)
    {
      case OWL_DL:
        structure =new OWLDLISStructureImpl(in,name,namespace);
        break;

     case DAML:
       structure =new DAMLISStructureImpl(in,name,namespace);
       break;

     case RDFS:
       structure =new RDFSISStructureImpl(in,name,namespace);
       break;

    }
    return structure;
  }

}