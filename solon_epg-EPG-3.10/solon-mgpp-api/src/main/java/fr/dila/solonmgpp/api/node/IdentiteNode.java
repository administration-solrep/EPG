package fr.dila.solonmgpp.api.node;

import java.util.Date;

import fr.sword.xsd.solon.epp.Identite;

public interface IdentiteNode extends TableReferenceEppNode{
  
  public void remapField(Identite identite);
  
  public Identite toIdentiteXsd();
  
  public String getIdentifiant();
  public void setIdentifiant(String identifiant);
  public String getCivilite();
  public void setCivilite(String civilite);
  public String getPrenom();
  public void setPrenom(String prenom);
  public String getNom();
  public void setNom(String nom);
  public Date getDateNaissance();
  public void setDateNaissance(Date dateNaissance);
  public String getLieuNaissance();
  public void setLieuNaissance(String lieuNaissance);
  public String getDeptNaissance();
  public void setDeptNaissance(String deptNaissance);
  public String getPaysNaissance();
  public void setPaysNaissance(String paysNaissance);
  public ActeurNode getActeur();
  public void setActeur(ActeurNode acteur);
  public String getMandatId();
  public void setMandatId(String mandatId);
}
