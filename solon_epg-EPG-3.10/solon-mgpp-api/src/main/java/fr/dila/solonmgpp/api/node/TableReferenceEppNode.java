package fr.dila.solonmgpp.api.node;

import java.util.Date;

public interface TableReferenceEppNode {

  public String getIdentifiant();

  public void setIdentifiant(String identifiant);

  public String getTypeValue();

  public void setTypeValue(String typeValue);

  public Date getDateDebut();

  public void setDateDebut(Date dateDebut);

  public Date getDateFin();

  public void setDateFin(Date dateFin);

  public String getLabel();
  
  public String getStyle();
  
  public Boolean isHasChildren();

  public void setHasChildren(Boolean value);
}
