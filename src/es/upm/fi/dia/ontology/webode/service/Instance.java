package es.upm.fi.dia.ontology.webode.service;

import java.sql.*;
import java.util.*;

import es.upm.fi.dia.ontology.minerva.server.others.DBConnection;
import es.upm.fi.dia.ontology.webode.service.util.*;

/**
 * Instance descriptor.
 */
public class Instance implements java.io.Serializable
{
  /** The name of the instance. */
  public String name;

  /** The term the instance is tied to. */
  public String term;

  /** The name of the instance set. */
  public String instanceSet;

  /**
   * Description.
   */
  public String description;

  public Instance (String name, String term, String instanceSet, String description)
  {
    this.name = name;
    this.term = term;
    this.instanceSet = instanceSet;
    this.description = description;
  }

  public static Instance[] getInstances (DBConnection con, String ontology, String instanceSet, String name)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals(""))
      throw new WebODEException ("Invalid ontology parameter.");
    if (instanceSet == null || (instanceSet = instanceSet.trim()).equals(""))
      throw new WebODEException ("Invalid instance set parameter.");
    if (name == null || (name = name.trim()).equals(""))
      throw new WebODEException ("Invalid name parameter.");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt = con.prepareStatement ("select t2.name, t2.description " +
                                    " from ode_terms_glossary t, ode_terms_glossary t1, " +
                                    "   ode_terms_glossary t2, ode_ontology o, " +
                                    "   ode_instance i " +
                                    " where t.term_id = i.term_id and t.name = ? and " +
                                    "   t1.term_id = i.instance_set_id and t1.name = ? and " +
                                    "   t2.term_id = i.instance_id and " +
                                    "   t.ontology_id = o.ontology_id and o.name = ? " +
                                    " order by t2.name ");
      pstmt.setString (1, name);
      pstmt.setString (2, instanceSet);
      pstmt.setString (3, ontology);

      rset = pstmt.executeQuery();
      if (rset.next()) {
        Vector v = new Vector(200);
        do {
          v.addElement (new Instance (rset.getString (1), name,
                                      instanceSet, rset.getString (2)));
        } while (rset.next());

        Instance[] at = new Instance[v.size()];
        v.copyInto (at);

        return at;
      }
      else
        return null;
    } finally {
      try {
        if (rset  != null) rset.close();
        if (pstmt != null) pstmt.close();
        } catch (Exception e) {}
    }
  }

  /**
   * Gets an instance given the ontology and instanceSet it belongs to and its name
   */
  public static Instance getInstance (DBConnection con, String ontology, String instanceSet, String instanceName)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals(""))
      throw new WebODEException ("Invalid ontology parameter.");
    if (instanceSet == null || (instanceSet = instanceSet.trim()).equals(""))
      throw new WebODEException ("Invalid instance set parameter.");
    if (instanceName == null || (instanceName = instanceName.trim()).equals(""))
      throw new WebODEException ("Invalid instance name parameter.");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt = con.prepareStatement ("select t2.name, t.description " +
                                    " from ode_terms_glossary t, ode_terms_glossary t1, " +
                                    "   ode_terms_glossary t2, ode_ontology o, " +
                                    "   ode_instance i " +
                                    " where t.term_id = i.instance_id and t.name = ? and " +
                                    "   t1.term_id = i.instance_set_id and t1.name = ? and " +
                                    "   t.ontology_id = o.ontology_id and o.name = ? and " +
                                    "   t2.term_id = i.term_id");
      pstmt.setString (1, instanceName);
      pstmt.setString (2, instanceSet);
      pstmt.setString (3, ontology);

      rset = pstmt.executeQuery();
      if (rset.next())
        return (new Instance (instanceName, rset.getString (1),
                              instanceSet, rset.getString (2)));
      else
        return null;
    } finally {
      try {
        if (rset  != null) rset.close();
        if (pstmt != null) pstmt.close();
        } catch (Exception e) {}
    }
  }


  public static Instance[] getInstances (DBConnection con, String ontology, String instanceSet)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals(""))
      throw new WebODEException ("Invalid ontology parameter.");
    if (instanceSet == null || (instanceSet = instanceSet.trim()).equals(""))
      throw new WebODEException ("Invalid instance set parameter.");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt = con.prepareStatement ("select t2.name, t.name, t2.description " +
                                    " from ode_terms_glossary t, ode_terms_glossary t1, " +
                                    "   ode_terms_glossary t2, ode_ontology o, " +
                                    "   ode_instance i " +
                                    " where t.term_id = i.term_id and " +
                                    "   t1.term_id = i.instance_set_id and t1.name = ? and " +
                                    "   t2.term_id = i.instance_id and " +
                                    "   t.ontology_id = o.ontology_id and o.name = ? " +
                                    " order by t2.name ");
      pstmt.setString (1, instanceSet);
      pstmt.setString (2, ontology);

      rset = pstmt.executeQuery();
      if (rset.next()) {
        Vector v = new Vector(200);
        do {
          v.addElement (new Instance (rset.getString (1), rset.getString(2),
                                      instanceSet, rset.getString (3)));
        } while (rset.next());

        Instance[] at = new Instance[v.size()];
        v.copyInto (at);

        return at;
      }
      else
        return null;
    } finally {
      try {
        if (rset  != null) rset.close();
        if (pstmt != null) pstmt.close();
        } catch (Exception e) {}
    }
  }


  public void store (DBConnection con, String ontology) throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals(""))
      throw new WebODEException ("Invalid ontology parameter.");
    if (instanceSet == null || (instanceSet = instanceSet.trim()).equals(""))
      throw new WebODEException ("Invalid instance set parameter.");
    if (name == null || (name = name.trim()).equals(""))
      throw new WebODEException ("Invalid name parameter.");
    if (term == null || (term = term.trim()).equals(""))
      throw new WebODEException ("Invalid term parameter.");

    if(Concept.inExhaustiveGroup(con, ontology, this.term))
      throw new WebODEException("The concept '" + this.term + "' belongs to a group with an exhaustive relation");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      con.setAutoCommit (false);
      int instanceSetId = SQLUtil.getTermId (con, ontology, instanceSet, TermTypes.INSTANCE_SET);
      int instanceId = new Term (ontology, name, description, TermTypes.INSTANCE, instanceSetId).store (con);
      int termId     = SQLUtil.getTermId (con, ontology, term, TermTypes.CONCEPT);

      //System.out.println ("#" + instanceSetId + "#" + instanceId + "#" + termId);

      pstmt = con.prepareStatement ("insert into ode_instance " +
                                    "(instance_set_id, term_id, instance_id) values " +
                                    "(?, ?, ?)");
      pstmt.setInt (1, instanceSetId);
      pstmt.setInt (2, termId);
      pstmt.setInt (3, instanceId);
      pstmt.executeUpdate ();

      con.commit();
    } catch (SQLException e) {
      con.rollback();
      throw e;
    } finally {
      if (pstmt != null) pstmt.close();
      con.setAutoCommit (true);
    }
  }

  public static HashMap getInstanceValue (DBConnection con, String ontology,
      String instanceSet, String instance)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals(""))
      throw new WebODEException ("Invalid ontology parameter.");
    if (instanceSet == null || (instanceSet = instanceSet.trim()).equals(""))
      throw new WebODEException ("Invalid instance set parameter.");
    if (instance == null || (instance = instance.trim()).equals(""))
      throw new WebODEException ("Invalid instance name parameter.");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt=con.prepareStatement("select att.name, iv.value, concept.name " +
                                 " from ode_terms_glossary att, ode_instance_value iv, " +
                                 "      ode_terms_glossary instance, ode_terms_glossary instanceSet," +
                                 "      ode_terms_glossary concept, ode_ontology o " +
                                 " where " +
                                 "      o.name = ? and " + // Ontology name
                                 "      o.ontology_id = instance.ontology_id and " +
                                 "      instance.name = ? and " + // Name of the instance
                                 "      instance.parent_id = instanceSet.term_id and " +
                                 "      instanceSet.name = ? and " + // instance set
                                 "      instance.term_id = iv.instance_id and" +
                                 "      iv.attribute_id = att.term_id and " +
                                 "      att.parent_id = concept.term_id ");
      pstmt.setString (1, ontology);
      pstmt.setString (2, instance);
      pstmt.setString (3, instanceSet);
      rset = pstmt.executeQuery();
      if (rset.next()) {
        Vector v = new Vector();
        HashMap hash = new HashMap();
        String previous = "";
        String previousTerm = "";
        String current = "";
        String currentTerm = "";
        do {
          current = rset.getString (1);
          String hand = rset.getString (2);
          currentTerm = rset.getString (3);
          if (previous.equals ("")) {
            previous = current;
            previousTerm = currentTerm;
          }
          if (previous.equals (current) && previousTerm.equals (currentTerm)) {
            v.addElement (hand);
          }
          else {
            String[] aux = new String[v.size()] ;
            v.copyInto (aux);
            hash.put (new InstanceKey (previousTerm, previous), aux);
            v = new Vector();
            v.addElement (hand);
            previous = current;
            previousTerm = currentTerm;
          }
        } while (rset.next());
        String[] aux = new String[v.size()] ;
        v.copyInto (aux);
        hash.put (new InstanceKey (currentTerm, current), aux);

        return hash;
      }
      else
        return null;
    } finally {
      try {
        if (rset  != null) rset.close();
        if (pstmt != null) pstmt.close();
        } catch (Exception e) {}
    }
  }

  public static HashMap[] getInstancesValues(DBConnection con, String ontology,
                                             String instanceSet, String[] instances)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals(""))
      throw new WebODEException ("Invalid ontology parameter.");
    if (instanceSet == null || (instanceSet = instanceSet.trim()).equals(""))
      throw new WebODEException ("Invalid instance set parameter.");
    if (instances == null)
      throw new WebODEException ("Invalid instance name parameter.");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    if(instances.length==0)
      return null;
    try {
      HashMap values=new HashMap();
      StringBuffer names=new StringBuffer();
      for(int i=0; i<instances.length; i++) {
        if(instances[i]!=null) {
          values.put(instances[i], new HashMap());
          if(names.length()>0)
            names.append(", ?");
          else
            names.append("?");
        }
      }

      pstmt=con.prepareStatement("select instance.name, concept.name, att.name, iv.value " +
                                 " from ode_terms_glossary att, ode_instance_value iv, " +
                                 "      ode_terms_glossary instance, ode_terms_glossary instanceSet," +
                                 "      ode_terms_glossary concept, ode_ontology o " +
                                 " where " +
                                 "      o.name = ? and " + // Ontology name
                                 "      o.ontology_id = instanceSet.ontology_id and " +
                                 "      instanceSet.name = ? and " + // instance set
                                 "      instanceSet.term_id = instance.parent_id and " +
                                 "      instance.name in (" + names + ") and " + // Name of the instances
                                 "      instance.term_id = iv.instance_id and" +
                                 "      iv.attribute_id = att.term_id and " +
                                 "      att.parent_id = concept.term_id " +
                                 "order by instance.name, concept.name, att.name");
      pstmt.setString (1, ontology);
      pstmt.setString (2, instanceSet);
      int idx=3;
      for(Iterator it=values.keySet().iterator(); it.hasNext(); )
        pstmt.setString (idx++, (String)it.next());
      rset = pstmt.executeQuery();

      ArrayList v=null;
      String previousInstance=null, previousConcept=null, previousAttribute=null;
      String actualInstance, actualConcept, actualAttribute;
      while(rset.next()) {
        actualInstance=rset.getString(1);
        actualConcept=rset.getString(2);
        actualAttribute=rset.getString(3);
        if(previousInstance!=null &&
           previousInstance.equals(actualInstance) &&
           previousConcept.equals(actualConcept) &&
           previousAttribute.equals(actualAttribute)) {

          v.add(rset.getString(4));
        }
        else {
          if(previousInstance!=null) {
            if(v.size()>0)
              ((HashMap)values.get(previousInstance)).put(new InstanceKey(previousConcept, previousAttribute), v.toArray(new String[0]));
          }
          v=new ArrayList();
          v.add(rset.getString(4));
        }

        previousInstance=actualInstance;
        previousConcept=actualConcept;
        previousAttribute=actualAttribute;
      }
      if(v!=null && v.size()>0)
        ((HashMap)values.get(previousInstance)).put(new InstanceKey(previousConcept, previousAttribute), v.toArray(new String[0]));

      HashMap[] avalues=new HashMap[instances.length];
      for(int i=0; i<avalues.length; i++)
        avalues[i]=(HashMap)values.get(instances[i]);
      return avalues;
    } finally {
      try {
        if (rset  != null) rset.close();
        if (pstmt != null) pstmt.close();
        } catch (Exception e) {}
    }
  }

  public static HashMap getLogicalInstanceValue (DBConnection con, String ontology,
      String instanceSet, String instance)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals(""))
      throw new WebODEException ("Invalid ontology parameter.");
    if (instanceSet == null || (instanceSet = instanceSet.trim()).equals(""))
      throw new WebODEException ("Invalid instance set parameter.");
    if (instance == null || (instance = instance.trim()).equals(""))
      throw new WebODEException ("Invalid instance name parameter.");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt=con.prepareStatement("select att.name, iv.value, concept.name " +
                                 " from ode_terms_glossary att, ode_instance_value iv, " +
                                 "      ode_terms_glossary instance, ode_terms_glossary instanceSet," +
                                 "      ode_terms_glossary concept, ode_ontology o " +
                                 " where " +
                                 "      o.name = ? and " + // Ontology name
                                 "      o.ontology_id = instance.ontology_id and " +
                                 "      instance.name = ? and " + // Name of the instance
                                 "      instance.parent_id = instanceSet.term_id and " +
                                 "      instanceSet.name = ? and " + // instance set
                                 "      instance.term_id = iv.instance_id and" +
                                 "      iv.attribute_id = att.term_id and " +
                                 "      att.parent_id = concept.term_id " +
                                 " order by att.name, concept.name ");
      pstmt.setString (1, ontology);
      pstmt.setString (2, instance);
      pstmt.setString (3, instanceSet);
      rset = pstmt.executeQuery();
      if (rset.next()) {
        Vector v = new Vector();
        HashMap hash = new HashMap();
        HashMap hashTop = new HashMap();
        String previous = "";
        String previousTerm = "";
        String current = "";
        String currentTerm = "";
        do {
          current = rset.getString (1);
          String hand = rset.getString (2);
          currentTerm = rset.getString (3);
          if (previous.equals ("")) {
            previous = current;
            previousTerm = currentTerm;
          }
          if (previousTerm.equals (currentTerm)) {
            if (previous.equals (current)) {
              v.addElement (hand);
            }
            else {
              String[] aux = new String[v.size()] ;
              v.copyInto (aux);
              hash.put (previous, aux);
              v = new Vector();
              v.addElement (hand);
              previous = current;
            }
          }
          else {
            String[] aux = new String[v.size()] ;
            v.copyInto (aux);
            hash.put (previous, aux);
            v = new Vector();
            v.addElement (hand);
            previous = current;
            hashTop.put (previousTerm, hash);
            hash = new HashMap();
            previousTerm = currentTerm;
          }
        } while (rset.next());
        String[] aux = new String[v.size()] ;
        v.copyInto (aux);
        hash.put (current, aux);
        hashTop.put (currentTerm, hash);

        return hashTop;
      }
      else {
        return null;
      }
    } finally {
      try {
        if (rset  != null) rset.close();
        if (pstmt != null) pstmt.close();
        } catch (Exception e) {}
    }
  }

  /**
   * Gets the values of an instance attribute of an instance
   * @param con The database connnection
   * @param ontology Ontology name
   * @param instanceSet Instance set name
   * @param instance Instance name
   * @param attribute Attribute name
   * @return The values of the instance attribute
   * @throws SQLException
   * @throws WebODEException
   */
  public static String[] getInstanceAttrValues (DBConnection con, String ontology,
      String instanceSet, String instance, String attribute)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals(""))
      throw new WebODEException ("Invalid ontology parameter.");
    if (instanceSet == null || (instanceSet = instanceSet.trim()).equals(""))
      throw new WebODEException ("Invalid instance set parameter.");
    if (instance == null || (instance = instance.trim()).equals(""))
      throw new WebODEException ("Invalid instance name parameter.");
    if (attribute == null || (attribute = attribute.trim()).equals(""))
      throw new WebODEException ("Invalid attribute name parameter.");

    PreparedStatement pstmt = null;
    ResultSet          rset = null;
    try {
      pstmt = con.prepareStatement ("select iv.value " +
                                    " from ode_terms_glossary att, ode_instance_value iv, " +
                                    "      ode_terms_glossary instance, ode_terms_glossary instanceSet," +
                                    "      ode_ontology o " +
                                    " where " +
                                    "      o.name = ? and " + // Ontology name
                                    "      o.ontology_id = instance.ontology_id and " +
                                    "      instance.name = ? and " + // Instance name
                                    "      instance.parent_id = instanceSet.term_id and " +
                                    "      instanceSet.name = ? and " + // Instance set name
                                    "      instance.term_id = iv.instance_id and " +
                                    "      iv.attribute_id = att.term_id and " +
                                    "      att.name = ?"); // Attribute name
      pstmt.setString (1, ontology);
      pstmt.setString (2, instance);
      pstmt.setString (3, instanceSet);
      pstmt.setString (4, attribute);
      rset = pstmt.executeQuery();
      if (rset.next()) {
        Vector v = new Vector();
        do {
          String hand = rset.getString (1);
          v.addElement (hand);
        } while (rset.next());
        String[] aux = new String[v.size()] ;
        v.copyInto (aux);
        return aux;
      }
      else
        return null;
    } finally {
      try {
        if (rset  != null) rset.close();
        if (pstmt != null) pstmt.close();
        } catch (Exception e) {}
    }
  }


  public static void addInstanceValue (DBConnection con, String ontology, String instanceSet, String name,
                                       String instance, String attribute, String value)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals(""))
      throw new WebODEException ("Invalid ontology parameter.");
    if (instanceSet == null || (instanceSet = instanceSet.trim()).equals(""))
      throw new WebODEException ("Invalid instance set parameter.");
    if (name == null || (name = name.trim()).equals(""))
      throw new WebODEException ("Invalid name parameter.");
    if (instance == null || (instance = instance.trim()).equals(""))
      throw new WebODEException ("Invalid instance name parameter.");
    if (attribute == null || (attribute = attribute.trim()).equals(""))
      throw new WebODEException ("Invalid instance value parameter.");

    if (value == null || (value = value.trim()).equals(""))
      throw new WebODEException ("Invalid instance value parameter.");

    int f ;
    int instanceSetId = SQLUtil.getTermId (con, ontology, instanceSet, TermTypes.INSTANCE_SET);
    int instanceId    = SQLUtil.getTermId (con, ontology, instance, TermTypes.INSTANCE, instanceSetId);
    int attributeId   = SQLUtil.getTermId (con, ontology, attribute, TermTypes.INSTANCE_ATTRIBUTE,
        f = SQLUtil.getTermId (con, ontology, name, TermTypes.CONCEPT));
    //System.out.println ("%" + instanceSetId + "%" + instanceId + "%" + attributeId + "%" + f);

    InstanceAttributeDescriptor iad=InstanceAttributeDescriptor.getInstanceAttribute(con, ontology, name, attribute);
    if(iad!=null && iad.valueType>=ValueTypes.ENUMERATED) {
      EnumeratedType enum=EnumeratedType.getEnumeratedType(con, ontology, iad.getValueName());
      if(enum!=null && enum.values!=null) {
        int i;
        for(i=0; i<enum.values.length && !enum.values[i].equals(value); i++);
        if(i==enum.values.length)
          throw new WebODEException("The value '" + value + "' is not valid in the enumerated data type '" + iad.getValueName() + "'");
      }
    }

    PreparedStatement pstmt = null;
    try {
      pstmt = con.prepareStatement ("insert into ode_instance_value (instance_id, attribute_id, value) " +
                                    " values (?, ?, ?)");
      pstmt.setInt (1, instanceId);
      pstmt.setInt (2, attributeId);
      pstmt.setString (3, value);
      System.out.println(instanceId + ", " + attributeId + ", " + value);
      pstmt.executeUpdate();
    } finally {
      if (pstmt != null) pstmt.close();
    }
  }

  public static void removeInstanceValue (DBConnection con, String ontology, String instanceSet, String name,
      String instance, String attribute, String value)
      throws SQLException, WebODEException
  {
    if (ontology == null || (ontology = ontology.trim()).equals(""))
      throw new WebODEException ("Invalid ontology parameter.");
    if (instanceSet == null || (instanceSet = instanceSet.trim()).equals(""))
      throw new WebODEException ("Invalid instance set parameter.");
    if (name == null || (name = name.trim()).equals(""))
      throw new WebODEException ("Invalid name parameter.");
    if (instance == null || (instance = instance.trim()).equals(""))
      throw new WebODEException ("Invalid instance name parameter.");
    if (attribute == null || (attribute = attribute.trim()).equals(""))
      throw new WebODEException ("Invalid instance value parameter.");

    if (value == null || (value = value.trim()).equals(""))
      throw new WebODEException ("Invalid instance value parameter.");

    int f ;
    int instanceSetId = SQLUtil.getTermId (con, ontology, instanceSet, TermTypes.INSTANCE_SET);
    int instanceId    = SQLUtil.getTermId (con, ontology, instance, TermTypes.INSTANCE, instanceSetId);
    int attributeId   = SQLUtil.getTermId (con, ontology, attribute, TermTypes.INSTANCE_ATTRIBUTE,
        f = SQLUtil.getTermId (con, ontology, name, TermTypes.CONCEPT));
    //System.out.println ("%" + instanceSetId + "%" + instanceId + "%" + attributeId + "%" + f);

    PreparedStatement pstmt = null;
    try {
      pstmt = con.prepareStatement ("delete from ode_instance_value " +
                                    " where instance_id = ? and attribute_id = ? and value = ?");
      pstmt.setInt (1, instanceId);
      pstmt.setInt (2, attributeId);
      pstmt.setString (3, value);
      pstmt.executeUpdate();
    } finally {
      if (pstmt != null) pstmt.close();
    }
  }

  /**
   * Move one instance from its concept to another concept.
   * All attributes, relation from and to this instance could be removed
   * @param ontology Ontology Name of the instance
   * @param instanceSet Instance Set of the instance
   * @param instanceName Instance Name of the instance
   * @param targetConcept Target concept
   */
  public static void moveInstance(DBConnection conn, String ontology, String instanceSet, String instanceName, String targetConcept) throws SQLException, WebODEException {
    if (ontology == null || (ontology = ontology.trim()).equals(""))
      throw new WebODEException ("Invalid ontology parameter.");
    if (instanceSet == null || (instanceSet = instanceSet.trim()).equals(""))
      throw new WebODEException ("Invalid instance set parameter.");
    if (instanceName == null || (instanceName = instanceName.trim()).equals(""))
      throw new WebODEException ("Invalid instance name parameter.");
    if (targetConcept == null || (targetConcept = targetConcept.trim()).equals(""))
      throw new WebODEException ("Invalid concept name parameter.");

    int instanceSetID=SQLUtil.getTermId(conn, ontology, instanceSet, TermTypes.INSTANCE_SET);
    if(instanceSetID==-1)
      throw new WebODEException ("Instance Set not found in the ontology '" + ontology + "'");

    int instanceID=SQLUtil.getTermId(conn, ontology, instanceName, TermTypes.INSTANCE, instanceSetID);
    if(instanceID==-1)
      throw new WebODEException ("Instance '" + instanceName + "' not found in the ontology '" + ontology + "' and instance set '" + instanceSet + "'");
    Instance instance=getInstance(conn, ontology, instanceSet, instanceName);

    int targetConceptID=SQLUtil.getTermId(conn, ontology, targetConcept, TermTypes.CONCEPT);
    if(targetConceptID==-1)
      throw new WebODEException("Target concept '" + targetConcept + "' dosen't exists");

    if(Concept.inExhaustiveGroup(conn, ontology, targetConcept))
      throw new WebODEException("Target concept '" + targetConcept + "' belongs to a group with an exhaustive relation");

    HashSet parentConcepts=new HashSet();
    ArrayList concepts=new ArrayList();
    concepts.add(targetConcept);
    String concept;
    Term[] parents;
    for(int i=0; i<concepts.size(); i++) {
      concept=(String)concepts.get(i);
      parentConcepts.add(concept);
      parents=Concept.getParentConcepts(conn, ontology, concept);
      if(parents!=null) {
        for(int j=0; j<parents.length; j++) {
          if(!parentConcepts.contains(parents[j].term))
            concepts.add(parents[j].term);
        }
      }
    }

    StringBuffer params=null;
    for(Iterator it=concepts.iterator(); it.hasNext(); it.next()) {
      if(params!=null)
        params.append(", ");
      else
        params=new StringBuffer();
      params.append('?');
    }

    conn.setAutoCommit(false);
    PreparedStatement pstmt=null;
    try {
      if(!parentConcepts.contains(instance.term)) {
        // Remove instance attributes
        if(conn.dbManager==DBConnection.ORACLE)
          pstmt=conn.prepareStatement("delete from ode_instance_value iv " +
                                      "where " +
                                      "  iv.instance_id = ? and " +
                                      "  iv.attribute_id = (" +
                                      "    select att.term_id " +
                                      "    from ode_terms_glossary att, " +
                                      "         ode_terms_glossary concept " +
                                      "    where " +
                                      "      iv.attribute_id = att.term_id and " +
                                      "      att.parent_id = concept.term_id and " +
                                      "      concept.name not in (" + params + "))");
        else
          pstmt=conn.prepareStatement("delete from ode_instance_value " +
                                      "  using ode_terms_glossary att, ode_instance_value, " +
                                      "        ode_terms_glossary concept " +
                                      "  where " +
                                      "    ode_instance_value.instance_id = ? and " +
                                      "    ode_instance_value.attribute_id = att.term_id and " +
                                      "    att.parent_id = concept.term_id and " +
                                      "    concept.name not in (" + params + ")");
        pstmt.setInt(1, instanceID);
        int idx=2;
        for(Iterator it=concepts.iterator(); it.hasNext(); )
          pstmt.setString(idx++, (String)it.next());
        pstmt.executeUpdate();

        //Remove instance relation from this instance
        TermRelationInstance[] rels;
        rels=TermRelationInstance.getRelationInstancesFromInstance(conn, ontology, instanceSet, instanceName);
        if(rels!=null) {
          for(int i=0; i<rels.length; i++)
            if(!parentConcepts.contains(rels[i].termRelation.origin))
              Term.removeTerm(conn,ontology,rels[i].name,instanceSet);
        }

        //Remove instance relation from this instance
        rels=TermRelationInstance.getRelationInstancesToInstance(conn, ontology, instanceSet, instanceName);
        if(rels!=null) {
          for(int i=0; i<rels.length; i++)
            if(!parentConcepts.contains(rels[i].termRelation.destination))
              Term.removeTerm(conn,ontology,rels[i].name,instanceSet);
        }

        pstmt.close();
        pstmt=null;
      }

      pstmt=conn.prepareStatement("update ode_instance intance set term_id=? where instance_id=?");
      pstmt.setInt(1,targetConceptID);
      pstmt.setInt(2,instanceID);
      pstmt.executeUpdate();

      conn.commit();
    }
    catch(SQLException sqle) {
      conn.rollback();
      throw sqle;
    }
    finally {
      if(pstmt!=null) pstmt.close();
      conn.setAutoCommit(true);
    }
  }
}