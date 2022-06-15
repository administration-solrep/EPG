package fr.dila.solonmgpp.core.node;

import fr.dila.solonmgpp.api.node.ActeurNode;
import fr.dila.solonmgpp.api.node.IdentiteNode;
import fr.dila.st.core.util.DateUtil;
import fr.sword.xsd.solon.epp.Civilite;
import fr.sword.xsd.solon.epp.Identite;
import java.util.Date;

public class IdentiteNodeImpl extends TableReferenceEppNodeImpl implements IdentiteNode {
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

    public IdentiteNodeImpl() {
        acteur = new ActeurNodeImpl();
    }

    @Override
    public void remapField(Identite identite) {
        setIdentifiant(identite.getId());
        setCivilite(
            identite.getCivilite() == null
                ? null
                : (identite.getCivilite().value().equals(Civilite.MME.value()) ? "Madame" : "Monsieur")
        );
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

    @Override
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

    @Override
    public String getIdentifiant() {
        return identifiant;
    }

    @Override
    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    @Override
    public String getCivilite() {
        return civilite;
    }

    @Override
    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    @Override
    public String getPrenom() {
        return prenom;
    }

    @Override
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public Date getDateNaissance() {
        return DateUtil.copyDate(dateNaissance);
    }

    @Override
    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = DateUtil.copyDate(dateNaissance);
    }

    @Override
    public String getLieuNaissance() {
        return lieuNaissance;
    }

    @Override
    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    @Override
    public String getDeptNaissance() {
        return deptNaissance;
    }

    @Override
    public void setDeptNaissance(String deptNaissance) {
        this.deptNaissance = deptNaissance;
    }

    @Override
    public String getPaysNaissance() {
        return paysNaissance;
    }

    @Override
    public void setPaysNaissance(String paysNaissance) {
        this.paysNaissance = paysNaissance;
    }

    @Override
    public ActeurNode getActeur() {
        return acteur;
    }

    @Override
    public void setActeur(ActeurNode acteur) {
        this.acteur = acteur;
    }

    @Override
    public String getMandatId() {
        return mandatId;
    }

    @Override
    public void setMandatId(String mandatId) {
        this.mandatId = mandatId;
    }

    @Override
    public String getCiviliteEtNomComplet() {
        return String.join(" ", civilite, nom, prenom);
    }

    @Override
    public String getNomComplet() {
        return String.join(" ", prenom, nom);
    }
}
