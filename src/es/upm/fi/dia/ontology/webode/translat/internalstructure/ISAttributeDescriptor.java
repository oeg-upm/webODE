
package es.upm.fi.dia.ontology.webode.translat.internalstructure;
import java.util.Vector;


public abstract class ISAttributeDescriptor extends ISStructureElement
{
  protected String name;
  protected String description;
  protected String type;
  protected String minimum_cardinality;
  protected String maximum_cardinality;
  protected String measurement_unit;
  protected String precision;
  protected Vector values;
  protected Vector related_references;
  protected Vector synonyms;
  protected Vector abbreviations;
  protected Vector related_formulas;
  protected Vector inferreds;

  public void	setDescription (String attr_description)
  {
    description=attr_description;

  }

  public String getDescription()
  {
    return description;
  }

  public String getName()
  {
    return name;
  }

  public String getType()
  {
    return type;
  }

  public String getMinCardinality()
  {
    return minimum_cardinality;
  }
  public String getMaxCardinality()
  {
    return maximum_cardinality;
  }




  public void	setPrecision (String attr_precision)
  {
    precision=attr_precision;
  }

  public void	setRelatedReferences (Vector attr_related_references)
  {
    related_references=attr_related_references;
  }

  public void	setSynonyms (Vector attr_synonyms)
  {
    synonyms=attr_synonyms;
  }


  public void	setAbbreviations (Vector attr_abbreviations)
  {
    abbreviations=attr_abbreviations;

  }

  public void	setRelatedFormulas (Vector attr_related_formulas)
  {
    related_formulas=attr_related_formulas;

  }


  public void	setInferreds (Vector attr_inferreds)
  {
    inferreds=attr_inferreds;

  }

  public void	setType (String attr_type)
  {
    type=attr_type;

  }

  public void	setMeasurementUnit (String attr_measurement_unit)
  {
    measurement_unit=attr_measurement_unit;

  }



  public void setMinCardinality (int min_cardinality)
  {
    minimum_cardinality=(new Integer(min_cardinality)).toString();
  }
  public void setMaxCardinality (int max_cardinality)
  {
    maximum_cardinality=(new Integer(max_cardinality)).toString();
  }

  public void setMinCardinality (String min_cardinality)
  {
    minimum_cardinality=min_cardinality;
  }

  public void setMaxCardinality (String max_cardinality)
  {
    maximum_cardinality=max_cardinality;
  }

  public void addValue (String new_value)
  {
    values.add(new_value);
  }


}
