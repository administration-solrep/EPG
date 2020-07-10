package fr.dila.solonepg.core.cases;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.Ordonnance;
import fr.dila.solonepg.api.cases.Traite;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;

/**
 * Implementation de {@link Ordonnance}
 * 
 * @author asatre
 * 
 */
public class TraiteImpl extends TexteMaitreImpl implements Traite {

    private static final long serialVersionUID = -3801345718539975375L;

    public TraiteImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public String getId() {
        return getDocument().getId();
    }

    @Override
    public String getCategorie() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.CATEGORIE);
    }

    @Override
    public void setCategorie(String categorie) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.CATEGORIE, categorie);        
    }

    @Override
    public String getOrganisation() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.ORGANISATION);
    }

    @Override
    public void setOrganisation(String organisation) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.ORGANISATION, organisation);        
    }

    @Override
    public String getThematique() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.THEMATIQUE);
    }

    @Override
    public void setThematique(String thematique) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.THEMATIQUE, thematique);       
    }

    @Override
    public Calendar getDateSignature() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_SIGNATURE);
    }

    @Override
    public void setDateSignature(Calendar dateSignature) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_SIGNATURE, dateSignature);
    }

    @Override
    public Boolean hasAutorisationRatification() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.AUTORISATION_RATIFICATION);
    }

    @Override
    public void setAutorisationRatification(Boolean autorisationRatification) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.AUTORISATION_RATIFICATION, autorisationRatification);
    }

    @Override
    public Boolean isPublication() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.PUBLICATION);
    }

    @Override
    public void setPublication(Boolean publication) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.PUBLICATION, publication);        
    }

    @Override
    public String getNorDecret() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMERO_NOR);
    }

    @Override
    public void setNorDecret(String norDecret) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMERO_NOR, norDecret);        
    }

    @Override
    public String getDegrePriorite() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DEGRE_PRIORITE);
    }

    @Override
    public void setDegrePriorite(String degreProrite) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DEGRE_PRIORITE, degreProrite);
    }

    @Override
    public Calendar getDatePJL() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_PJL);
    }

    @Override
    public void setDatePJL(Calendar datePJL) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_PJL, datePJL);        
    }

    @Override
    public Boolean hasEtudeImpact() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.ETUDE_IMPACT);
    }

    @Override
    public void setEtudeImpact(Boolean etudeImpact) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.ETUDE_IMPACT, etudeImpact);        
    }

    @Override
    public Boolean isDispoEtudeImpact() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DISPO_ETUDE_IMPACT);
    }

    @Override
    public void setDispoEtudeImpact(Boolean dispoEtudeImpact) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DISPO_ETUDE_IMPACT, dispoEtudeImpact);        
    }

    @Override
    public String getNorLoiRatification() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NOR_LOI_RATIFICATION);
    }

    @Override
    public void setNorLoiRatification(String norLoiRatification) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NOR_LOI_RATIFICATION, norLoiRatification);        
    }

    @Override
    public Calendar getDateDepotRatification() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_DEPOT_RATIFICATION);
    }

    @Override
    public void setDateDepotRatification(Calendar dateDepotRatification) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_DEPOT_RATIFICATION, dateDepotRatification);
    }
}
