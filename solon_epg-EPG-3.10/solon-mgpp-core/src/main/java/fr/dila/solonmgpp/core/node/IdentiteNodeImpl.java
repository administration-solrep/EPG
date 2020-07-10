package fr.dila.solonmgpp.core.node;

import java.util.Date;

import fr.dila.solonmgpp.api.node.ActeurNode;
import fr.dila.solonmgpp.api.node.IdentiteNode;
import fr.dila.st.core.util.DateUtil;
import fr.sword.xsd.solon.epp.Identite;
import fr.sword.xsd.solon.epp.Civilite;

public class IdentiteNodeImpl  extends TableReferenceEppNodeImpl implements IdentiteNode{
  
  private String identifiant;
  private String civilite;
  private String prenom;
  private String nom;
  private Date dateNaissance;
  private String lieuNaissance;
  private String deptNaissance;
  private String paysNaissance;
  private ActeurNode acteur;
  private String mandatId;

  public IdentiteNodeImpl(){
    acteur = new ActeurNodeImpl();
  }
  
  public void remapField(Identite identite) {
    setIdentifiant(identite.getId());
    setCivilite(identite.getCivilite()==null ? null : (identite.getCivilite().value().equals(Civilite.MME.value()) ? "Madame" : "Monsieur"));
    setPrenom(identite.getPrenom());
    setNom(identite.getNom());
    setDateNaissance(DateUtil.xmlGregorianCalendarToDate(identite.getDateNaissance()));
    setLieuNaissance(identite.getLieuNaissance());
    setDeptNaissance(identite.getDeptNaissance());
    setPaysNaissance(identite.getPaysNaissance());
    setDateDebut(DateUtil.xmlGregorianCalendarToDate(identite.getDateDebut()));
    setDateFin(DateUtil.xmlGregorianCalendarToDate(identite.getDateFin()));
    this.acteur.setIdentifiant(identite.getIdActeur());
  }
  
  public Identite toIdentiteXsd() {
    Identite identite = new Identite();
    identite.setId(this.getIdentifiant());

    
    identite.setCivilite("Madame".equals(civilite) ? Civilite.MME : Civilite.M);
//    identite.setCivilite(Civilite.M);
    identite.setPrenom(this.getPrenom());
    identite.setNom(this.getNom());
    identite.setDateNaissance(DateUtil.dateToXmlGregorianCalendar(this.getDateNaissance()));
    identite.setLieuNaissance(this.getLieuNaissance());
    identite.setDeptNaissance(this.getDeptNaissance());
    identite.setPaysNaissance(this.getPaysNaissance());
    identite.setDateDebut(DateUtil.dateToXmlGregorianCalendar(this.getDateDebut()));
    identite.setDateFin(DateUtil.dateToXmlGregorianCalendar(this.getDateFin()));
    identite.setIdActeur(this.getActeur().getIdentifiant());
    return identite;
  }
  
  
  public String getIdentifiant() {
    return identifiant;
  }
  public void setIdentifiant(String identifiant) {
    this.identifiant = identifiant;
  }
  public String getCivilite() {
    return civilite;
  }
  public void setCivilite(String civilite) {
    this.civilite = civilite;
  }
  public String getPrenom() {
    return prenom;
  }
  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }
  public String getNom() {
    return nom;
  }
  public void setNom(String nom) {
    this.nom = nom;
  }
  public Date getDateNaissance() {
    return DateUtil.copyDate(dateNaissance);
  }
  public void setDateNaissance(Date dateNaissance) {
    this.dateNaissance = DateUtil.copyDate(dateNaissance);
  }
  public String getLieuNaissance() {
    return lieuNaissance;
  }
  public void setLieuNaissance(String lieuNaissance) {
    this.lieuNaissance = lieuNaissance;
  }
  public String getDeptNaissance() {
    return deptNaissance;
  }
  public void setDeptNaissance(String deptNaissance) {
    this.deptNaissance = deptNaissance;
  }
  public String getPaysNaissance() {
    return paysNaissance;
  }
  public void setPaysNaissance(String paysNaissance) {
    this.paysNaissance = paysNaissance;
  }
  public ActeurNode getActeur() {
    return acteur;
  }
  public void setActeur(ActeurNode acteur) {
    this.acteur = acteur;
  }

  public String getMandatId() {
    return mandatId;
  }

  public void setMandatId(String mandatId) {
    this.mandatId = mandatId;
  }

}
