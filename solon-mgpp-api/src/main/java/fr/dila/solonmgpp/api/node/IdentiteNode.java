package fr.dila.solonmgpp.api.node;

import fr.sword.xsd.solon.epp.Identite;
import java.util.Date;

public interface IdentiteNode extends TableReferenceEppNode {
    void remapField(Identite identite);

    Identite toIdentiteXsd();

    String getCivilite();

    void setCivilite(String civilite);

    String getPrenom();

    void setPrenom(String prenom);

    String getNom();

    void setNom(String nom);

    Date getDateNaissance();

    void setDateNaissance(Date dateNaissance);

    String getLieuNaissance();

    void setLieuNaissance(String lieuNaissance);

    String getDeptNaissance();

    void setDeptNaissance(String deptNaissance);

    String getPaysNaissance();

    void setPaysNaissance(String paysNaissance);

    ActeurNode getActeur();

    void setActeur(ActeurNode acteur);

    String getMandatId();

    void setMandatId(String mandatId);

    String getCiviliteEtNomComplet();

    String getNomComplet();
}
