package fr.dila.solonepg.core.dto.activitenormative;

import fr.dila.solonepg.api.cases.Habilitation;
import fr.dila.solonepg.api.cases.typescomplexe.LigneProgrammationHabilitation;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import java.util.Date;
import org.nuxeo.ecm.core.api.NuxeoException;

/**
 * representation d'une ordonnance dans le tableau de programmation
 * @author asatre
 *
 */
public class LigneProgrammationHabilitationDTO extends AbstractMapDTO {
    private static final long serialVersionUID = 8624909872239413667L;

    public LigneProgrammationHabilitationDTO(LigneProgrammationHabilitation ligneProgrammation) {
        LigneProgrammationHabilitationDTO ligne = new LigneProgrammationHabilitationDTO();

        ligne.setArticle(ligneProgrammation.getArticle());
        ligne.setCodification(ligneProgrammation.getCodification());
        ligne.setConvention(ligneProgrammation.getConvention());
        ligne.setConventionDepot(ligneProgrammation.getConventionDepot());
        ligne.setDatePrevisionnelleCM(DateUtil.toDate(ligneProgrammation.getDatePrevisionnelleCM()));
        ligne.setDatePrevisionnelleSaisineCE(DateUtil.toDate(ligneProgrammation.getDatePrevisionnelleSaisineCE()));
        ligne.setDateTerme(DateUtil.toDate(ligneProgrammation.getDateTerme()));
        ligne.setLegislature(ligneProgrammation.getLegislature());
        ligne.setMinisterePilote(getNor(ligneProgrammation.getMinisterePilote()));
        ligne.setNumeroOrdre(ligneProgrammation.getNumeroOrdre());
        ligne.setObjetRIM(ligneProgrammation.getObjetRIM());
        ligne.setObservation(ligneProgrammation.getObservation());
    }

    public LigneProgrammationHabilitationDTO(Habilitation habilitation) {
        LigneProgrammationHabilitationDTO ligne = new LigneProgrammationHabilitationDTO();

        ligne.setArticle(habilitation.getArticle());
        ligne.setCodification(habilitation.getCodification());
        ligne.setConvention(habilitation.getConvention());
        ligne.setConventionDepot(habilitation.getConventionDepot());
        ligne.setDatePrevisionnelleCM(DateUtil.toDate(habilitation.getDatePrevisionnelleCM()));
        ligne.setDatePrevisionnelleSaisineCE(DateUtil.toDate(habilitation.getDatePrevisionnelleSaisineCE()));
        ligne.setDateTerme(DateUtil.toDate(habilitation.getDateTerme()));
        ligne.setLegislature(habilitation.getLegislature());
        ligne.setMinisterePilote(getNor(habilitation.getMinisterePilote()));
        ligne.setNumeroOrdre(habilitation.getNumeroOrdre());
        ligne.setObjetRIM(habilitation.getObjetRIM());
        ligne.setObservation(habilitation.getObservation());
    }

    private String getNor(String idMinistere) {
        EntiteNode node;
        try {
            node = STServiceLocator.getSTMinisteresService().getEntiteNode(idMinistere);
        } catch (NuxeoException e) {
            // node non trouvé
            node = null;
        }
        return node != null ? node.getNorMinistere() : idMinistere;
    }

    public LigneProgrammationHabilitationDTO() {}

    public String getNumeroOrdre() {
        return getString("numeroOrdre");
    }

    public void setNumeroOrdre(String numeroOrdre) {
        put("numeroOrdre", numeroOrdre);
    }

    public String getMinisterePilote() {
        return getString("ministerePilote");
    }

    public void setMinisterePilote(String ministerePilote) {
        put("ministerePilote", ministerePilote);
    }

    public String getMinisterePiloteLabel() {
        return getString("ministerePiloteLabel");
    }

    public void setMinisterePiloteLabel(String ministerePiloteLabel) {
        put("ministerePiloteLabel", ministerePiloteLabel);
    }

    public String getObservation() {
        return getString("observation");
    }

    public void setObservation(String observation) {
        put("observation", observation);
    }

    public Boolean getCodification() {
        return getBoolean("codification");
    }

    public void setCodification(Boolean codification) {
        put("codification", codification);
    }

    public String getArticle() {
        return getString("article");
    }

    public void setArticle(String article) {
        put("article", article);
    }

    public String getObjetRIM() {
        return getString("objetRIM");
    }

    public void setObjetRIM(String objetRIM) {
        put("objetRIM", objetRIM);
    }

    public String getConvention() {
        return getString("convention");
    }

    public void setConvention(String convention) {
        put("convention", convention);
    }

    public Date getDateTerme() {
        return getDate("dateTerme");
    }

    public void setDateTerme(Date dateTerme) {
        put("dateTerme", dateTerme);
    }

    public String getConventionDepot() {
        return getString("conventionDepot");
    }

    public void setConventionDepot(String conventionDepot) {
        put("conventionDepot", conventionDepot);
    }

    public Date getDatePrevisionnelleCM() {
        return getDate("datePrevisionnelleCM");
    }

    public void setDatePrevisionnelleCM(Date datePrevisionnelleCM) {
        put("datePrevisionnelleCM", datePrevisionnelleCM);
    }

    public String getLegislature() {
        return getString("legislature");
    }

    public void setLegislature(String legislature) {
        put("legislature", legislature);
    }

    public void setDatePrevisionnelleSaisineCE(Date datePrevisionnelleSaisineCE) {
        put("datePrevisionnelleSaisineCE", datePrevisionnelleSaisineCE);
    }

    public Date getDatePrevisionnelleSaisineCE() {
        return getDate("datePrevisionnelleSaisineCE");
    }

    public String getNumeroNorOrdo() {
        return getString("numeroNorOrdo");
    }

    public void setNumeroNorOrdo(String numeroNorOrdo) {
        put("numeroNorOrdo", numeroNorOrdo);
    }

    public String getObjetOrdo() {
        return getString("objetOrdo");
    }

    public void setObjetOrdo(String objetOrdo) {
        put("objetOrdo", objetOrdo);
    }

    public String getMinisterePiloteOrdo() {
        return getString("ministerePiloteOrdo");
    }

    public void setMinisterePiloteOrdo(String ministerePiloteOrdo) {
        put("ministerePiloteOrdo", ministerePiloteOrdo);
    }

    public String getLegislatureOrdo() {
        return getString("legislatureOrdo");
    }

    public void setLegislatureOrdo(String legislatureOrdo) {
        put("legislatureOrdo", legislatureOrdo);
    }

    public Date getDateSaisineCEOrdo() {
        return getDate("dateSaisineCEOrdo");
    }

    public void setDateSaisineCEOrdo(Date dateSaisineCEOrdo) {
        put("dateSaisineCEOrdo", dateSaisineCEOrdo);
    }

    public Date getDatePassageCMOrdo() {
        return getDate("datePassageCMOrdo");
    }

    public void setDatePassageCMOrdo(Date datePassageCMOrdo) {
        put("datePassageCMOrdo", datePassageCMOrdo);
    }

    public Date getDatePublicationOrdo() {
        return getDate("datePublicationOrdo");
    }

    public void setDatePublicationOrdo(Date datePublicationOrdo) {
        put("datePublicationOrdo", datePublicationOrdo);
    }

    public String getTitreOfficielOrdo() {
        return getString("titreOfficielOrdo");
    }

    public void setTitreOfficielOrdo(String titreOfficielOrdo) {
        put("titreOfficielOrdo", titreOfficielOrdo);
    }

    public String getNumeroOrdo() {
        return getString("numeroOrdo");
    }

    public void setNumeroOrdo(String numeroOrdo) {
        put("numeroOrdo", numeroOrdo);
    }

    public String getConventionDepotOrdo() {
        return getString("conventionDepotOrdo");
    }

    public void setConventionDepotOrdo(String conventionDepotOrdo) {
        put("conventionDepotOrdo", conventionDepotOrdo);
    }

    public Date getDateLimiteDepotOrdo() {
        return getDate("dateLimiteDepotOrdo");
    }

    public void setDateLimiteDepotOrdo(Date dateLimiteDepotOrdo) {
        put("dateLimiteDepotOrdo", dateLimiteDepotOrdo);
    }

    @Override
    public String getType() {
        return "LigneProgrammationHabilitation";
    }

    @Override
    public String getDocIdForSelection() {
        return null;
    }
}
