package fr.dila.solonmgpp.core.domain;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

public class FichePresentationDRImpl implements FichePresentationDR, Serializable {
    private static final long serialVersionUID = -8873597187229419685L;

    private final DocumentModel document;

    public static final String DOC_TYPE = "FichePresentationDR";
    public static final String SCHEMA = "fiche_presentation_dr";
    public static final String PREFIX = "fpdr";

    public static final String ID_DOSSIER = "idDossier";
    public static final String ACTIF = "actif";
    public static final String RAPPORT_PARLEMENT = "rapportParlement";
    public static final String OBJET = "objet";
    public static final String DATE_DEPOT_EFFECTIF = "dateDepotEffectif";

    public static final String NATURE = "nature";
    public static final String POLE = "pole";
    public static final String PERIODICITE = "periodicite";
    public static final String RESPONSABILITE_ELABORATION = "responsabiliteElaboration";
    public static final String MINISTERES = "ministeres";
    public static final String TEXTE_REF = "texteRef";

    public static final String NUMERO_ORDRE = "numeroOrdre";
    public static final String LEGISLATURE = "legislature";
    public static final String SESSION = "session";
    public static final String DATE_PUBLICATION_TEXTE_REF = "datePublicationTexteRef";
    public static final String ARTICLE_TEXTE_REF = "articleTexteRef";
    public static final String DATE_RELANCE_SGG = "dateRelanceSGG";
    public static final String NUMERO_DEPOT_JO_SENAT = "numeroDepotJOSenat";
    public static final String OBSERVATION = "observation";
    public static final String RUBRIQUE = "rubrique";
    public static final String CREATEUR_DEPOT = "createurDepot";
    public static final String DESTINATAIRE_1_RAPPORT = "destinataire1Rapport";
    public static final String DESTINATAIRE_2_RAPPORT = "destinataire2Rapport";
    public static final String CONVENTION_CALCUL = "conventionCalcul";
    public static final String RAPPORT_SENAT = "rapportSenat";
    public static final String DIRECTEUR_CM = "directeurCM";
    public static final String DAJ_MINISTERE = "DAJMinistere";
    public static final String ANNEE_DUPLIQUEE = "anneeDupliquee";

    public FichePresentationDRImpl(DocumentModel doc) {
        this.document = doc;
    }

    @Override
    public String getIdDossier() {
        return PropertyUtil.getStringProperty(document, SCHEMA, ID_DOSSIER);
    }

    @Override
    public void setIdDossier(String idDossier) {
        PropertyUtil.setProperty(document, SCHEMA, ID_DOSSIER, idDossier);
    }

    @Override
    public DocumentModel getDocument() {
        return document;
    }

    @Override
    public Boolean getActif() {
        return PropertyUtil.getBooleanProperty(document, SCHEMA, ACTIF);
    }

    @Override
    public void setActif(Boolean actif) {
        PropertyUtil.setProperty(document, SCHEMA, ACTIF, actif);
    }

    @Override
    public void setRapportParlement(String rapportParlement) {
        PropertyUtil.setProperty(document, SCHEMA, RAPPORT_PARLEMENT, rapportParlement);
    }

    @Override
    public String getRapportParlement() {
        return PropertyUtil.getStringProperty(document, SCHEMA, RAPPORT_PARLEMENT);
    }

    @Override
    public void setObjet(String objet) {
        PropertyUtil.setProperty(document, SCHEMA, OBJET, objet);
    }

    @Override
    public String getObjet() {
        return PropertyUtil.getStringProperty(document, SCHEMA, OBJET);
    }

    @Override
    public void setDateDepotEffectif(Calendar dateDepotEffectif) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_DEPOT_EFFECTIF, dateDepotEffectif);
    }

    @Override
    public Calendar getDateDepotEffectif() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_DEPOT_EFFECTIF);
    }

    @Override
    public String getNature() {
        return PropertyUtil.getStringProperty(document, SCHEMA, NATURE);
    }

    @Override
    public void setNature(String nature) {
        PropertyUtil.setProperty(document, SCHEMA, NATURE, nature);
    }

    @Override
    public List<String> getPole() {
        return PropertyUtil.getStringListProperty(document, SCHEMA, POLE);
    }

    @Override
    public void setPole(List<String> pole) {
        PropertyUtil.setProperty(document, SCHEMA, POLE, pole);
    }

    @Override
    public String getResponsabiliteElaboration() {
        return PropertyUtil.getStringProperty(document, SCHEMA, RESPONSABILITE_ELABORATION);
    }

    @Override
    public void setResponsabiliteElaboration(String responsabiliteElaboration) {
        PropertyUtil.setProperty(document, SCHEMA, RESPONSABILITE_ELABORATION, responsabiliteElaboration);
    }

    @Override
    public String getMinisteres() {
        return PropertyUtil.getStringProperty(document, SCHEMA, MINISTERES);
    }

    @Override
    public void setMinisteres(String ministeres) {
        PropertyUtil.setProperty(document, SCHEMA, MINISTERES, ministeres);
    }

    @Override
    public String getPeriodicite() {
        return PropertyUtil.getStringProperty(document, SCHEMA, PERIODICITE);
    }

    @Override
    public void setPeriodicite(String periodicite) {
        if (!DossierSolonEpgConstants.RAPPORT_PERIODIQUE_PERIODICITE_ID.equals(this.getRapportParlement())) {
            periodicite = null;
        }
        PropertyUtil.setProperty(document, SCHEMA, PERIODICITE, periodicite);
    }

    @Override
    public String getTexteRef() {
        return PropertyUtil.getStringProperty(document, SCHEMA, TEXTE_REF);
    }

    @Override
    public void setTexteRef(String texteRef) {
        PropertyUtil.setProperty(document, SCHEMA, TEXTE_REF, texteRef);
    }

    @Override
    public Long getNumeroOrdre() {
        return PropertyUtil.getLongProperty(document, SCHEMA, NUMERO_ORDRE);
    }

    @Override
    public String getLegislature() {
        return PropertyUtil.getStringProperty(document, SCHEMA, LEGISLATURE);
    }

    @Override
    public String getSession() {
        return PropertyUtil.getStringProperty(document, SCHEMA, SESSION);
    }

    @Override
    public Calendar getDatePublicationTexteReference() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_PUBLICATION_TEXTE_REF);
    }

    @Override
    public String getArticleTexteReference() {
        return PropertyUtil.getStringProperty(document, SCHEMA, ARTICLE_TEXTE_REF);
    }

    @Override
    public Calendar getDateRelanceSGG() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_RELANCE_SGG);
    }

    @Override
    public Long getNumeroDepotJOSenat() {
        return PropertyUtil.getLongProperty(document, SCHEMA, NUMERO_DEPOT_JO_SENAT);
    }

    @Override
    public String getObservation() {
        return PropertyUtil.getStringProperty(document, SCHEMA, OBSERVATION);
    }

    @Override
    public String getRubrique() {
        return PropertyUtil.getStringProperty(document, SCHEMA, RUBRIQUE);
    }

    @Override
    public String getCreateurDepot() {
        return PropertyUtil.getStringProperty(document, SCHEMA, CREATEUR_DEPOT);
    }

    @Override
    public List<String> getDestinataire1Rapport() {
        return PropertyUtil.getStringListProperty(document, SCHEMA, DESTINATAIRE_1_RAPPORT);
    }

    @Override
    public String getDestinataire2Rapport() {
        return PropertyUtil.getStringProperty(document, SCHEMA, DESTINATAIRE_2_RAPPORT);
    }

    @Override
    public String getConventionCalcul() {
        return PropertyUtil.getStringProperty(document, SCHEMA, CONVENTION_CALCUL);
    }

    @Override
    public Boolean getRapportSenat() {
        return PropertyUtil.getBooleanProperty(document, SCHEMA, RAPPORT_SENAT);
    }

    @Override
    public String getDirecteurCM() {
        return PropertyUtil.getStringProperty(document, SCHEMA, DIRECTEUR_CM);
    }

    @Override
    public String getDAJMinistere() {
        return PropertyUtil.getStringProperty(document, SCHEMA, DAJ_MINISTERE);
    }

    @Override
    public Long getAnneeDupliquee() {
        return PropertyUtil.getLongProperty(document, SCHEMA, ANNEE_DUPLIQUEE);
    }
}
