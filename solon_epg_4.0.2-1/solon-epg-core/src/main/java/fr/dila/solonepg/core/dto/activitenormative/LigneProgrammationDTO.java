package fr.dila.solonepg.core.dto.activitenormative;

import fr.dila.solonepg.api.cases.LigneProgrammation;
import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.service.VocabularyService;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.service.STServiceLocator;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/**
 * representation d'un decret dans le tableau de programmation
 *
 * @author asatre
 *
 */
public class LigneProgrammationDTO extends AbstractMapDTO {
    private static final long serialVersionUID = -7165345773142018923L;

    public LigneProgrammationDTO() {}

    public LigneProgrammationDTO(LigneProgrammation ligneProgrammation) {
        setId(ligneProgrammation.getId());
        setNumeroOrdre(ligneProgrammation.getNumeroOrdre());
        setArticle(ligneProgrammation.getArticleLoi());
        setBaseLegale(ligneProgrammation.getBaseLegale());
        setObjet(ligneProgrammation.getObjet());
        setMinisterePilote(ligneProgrammation.getMinisterePilote());
        setDirectionResponsable(ligneProgrammation.getDirectionResponsable());
        setCategorieTexte(ligneProgrammation.getCategorieTexte());
        setConsultationObligatoireHCE(ligneProgrammation.getConsultationObligatoireHCE());
        setCalendrierConsultationObligatoireHCE(ligneProgrammation.getCalendrierConsultationHCE());
        setDateSaisineCE(getTime(ligneProgrammation.getDateSaisineCE()));
        setDatePrevisionnelleSaisineCE(getTime(ligneProgrammation.getDatePrevisionnelleSaisineCE()));
        setObjectifPublication(getTime(ligneProgrammation.getObjectifPublication()));
        setObservation(ligneProgrammation.getObservation());
        setTypeMesureId(ligneProgrammation.getTypeMesureId());
        setDateMiseApplication(getTime(ligneProgrammation.getDateMiseApplication()));
    }

    public LigneProgrammationDTO(MesureApplicative mesureApplicative) {
        setNumeroOrdre(mesureApplicative.getNumeroOrdre());
        setArticle(mesureApplicative.getArticle());
        setBaseLegale(mesureApplicative.getBaseLegale());
        setObjet(mesureApplicative.getObjetRIM());
        setMinisterePilote(getNor(mesureApplicative.getMinisterePilote()));
        setDirectionResponsable(getDirection(mesureApplicative.getDirectionResponsable()));
        setConsultationObligatoireHCE(mesureApplicative.getConsultationsHCE());
        setCalendrierConsultationObligatoireHCE(mesureApplicative.getCalendrierConsultationsHCE());
        setDatePrevisionnelleSaisineCE(getTime(mesureApplicative.getDatePrevisionnelleSaisineCE()));
        setObjectifPublication(getTime(mesureApplicative.getDateObjectifPublication()));
        setObservation(mesureApplicative.getObservation());

        final VocabularyService vocabularyService = STServiceLocator.getVocabularyService();

        if (StringUtils.isNotEmpty(mesureApplicative.getTypeMesure())) {
            setTypeMesure(
                vocabularyService.getEntryLabel(
                    VocabularyConstants.TYPE_MESURE_VOCABULARY,
                    mesureApplicative.getTypeMesure()
                )
            );
        } else {
            setTypeMesure(null);
        }

        setTypeMesureId(mesureApplicative.getTypeMesure());

        setDateMiseApplication(getTime(mesureApplicative.getDateMiseApplication()));
    }

    private String getDirection(String idDirection) {
        OrganigrammeNode node = STServiceLocator.getSTUsAndDirectionService().getUniteStructurelleNode(idDirection);
        return node != null ? node.getLabel() : idDirection;
    }

    private String getNor(String idMinistere) {
        EntiteNode node = STServiceLocator.getSTMinisteresService().getEntiteNode(idMinistere);
        return node != null ? node.getNorMinistere() : idMinistere;
    }

    private Date getTime(Calendar calendar) {
        return calendar != null ? calendar.getTime() : null;
    }

    public String getNumeroOrdre() {
        return getString(TexteMaitreConstants.NUMERO_ORDRE);
    }

    public void setNumeroOrdre(String numeroOrdre) {
        put(TexteMaitreConstants.NUMERO_ORDRE, numeroOrdre);
    }

    public String getArticle() {
        return getString(TexteMaitreConstants.ARTICLE);
    }

    public void setArticle(String article) {
        put(TexteMaitreConstants.ARTICLE, article);
    }

    public String getBaseLegale() {
        return getString(TexteMaitreConstants.BASE_LEGALE);
    }

    public void setBaseLegale(String baseLegale) {
        put(TexteMaitreConstants.BASE_LEGALE, baseLegale);
    }

    public String getObjet() {
        return getString(TexteMaitreConstants.OBJET);
    }

    public void setObjet(String objet) {
        put(TexteMaitreConstants.OBJET, objet);
    }

    public String getMinisterePilote() {
        return getString(TexteMaitreConstants.MINISTERE_PILOTE);
    }

    public void setMinisterePilote(String ministerePilote) {
        put(TexteMaitreConstants.MINISTERE_PILOTE, ministerePilote);
    }

    public String getDirectionResponsable() {
        return getString(TexteMaitreConstants.DIRECTION_RESPONSABLE);
    }

    public void setDirectionResponsable(String directionResponsable) {
        put(TexteMaitreConstants.DIRECTION_RESPONSABLE, directionResponsable);
    }

    public String getCategorieTexte() {
        return getString(TexteMaitreConstants.CATEGORIE);
    }

    public void setCategorieTexte(String categorieTexte) {
        put(TexteMaitreConstants.CATEGORIE, categorieTexte);
    }

    public String getConsultationObligatoireHCE() {
        return getString(TexteMaitreConstants.CONSULTATIONS_HCE);
    }

    public void setConsultationObligatoireHCE(String consultationObligatoireHCE) {
        put(TexteMaitreConstants.CONSULTATIONS_HCE, consultationObligatoireHCE);
    }

    public String getCalendrierConsultationObligatoireHCE() {
        return getString(TexteMaitreConstants.CALENDRIER_CONSULTATIONS_HCE);
    }

    public void setCalendrierConsultationObligatoireHCE(String calendrierConsultationObligatoireHCE) {
        put(TexteMaitreConstants.CALENDRIER_CONSULTATIONS_HCE, calendrierConsultationObligatoireHCE);
    }

    public Date getDateSaisineCE() {
        return getDate(TexteMaitreConstants.DATE_SAISINE_CE);
    }

    public void setDateSaisineCE(Date dateSaisineCE) {
        put(TexteMaitreConstants.DATE_SAISINE_CE, dateSaisineCE);
    }

    public Date getObjectifPublication() {
        return getDate(TexteMaitreConstants.DATE_OBJECTIF_PUBLICATION);
    }

    public void setObjectifPublication(Date objectifPublication) {
        put(TexteMaitreConstants.DATE_OBJECTIF_PUBLICATION, objectifPublication);
    }

    public String getObservation() {
        return getString(TexteMaitreConstants.OBSERVATION);
    }

    public void setObservation(String observation) {
        put(TexteMaitreConstants.OBSERVATION, observation);
    }

    public Date getDateSortieCE() {
        return getDate(TexteMaitreConstants.DATE_SORTIE_CE);
    }

    public void setDateSortieCE(Date dateSortieCE) {
        put(TexteMaitreConstants.DATE_SORTIE_CE, dateSortieCE);
    }

    public String getTypeMesure() {
        return getString(TexteMaitreConstants.TYPE_MESURE_LABEL);
    }

    public void setTypeMesure(String typeMesure) {
        put(TexteMaitreConstants.TYPE_MESURE_LABEL, typeMesure);
    }

    public String getNorDecret() {
        return getString(TexteMaitreConstants.NOR_DECRET);
    }

    public void setNorDecret(String norDecret) {
        put(TexteMaitreConstants.NOR_DECRET, norDecret);
    }

    public String getTitreDecret() {
        return getString(TexteMaitreConstants.TITRE_ACTE);
    }

    public void setTitreDecret(String titreDecret) {
        put(TexteMaitreConstants.TITRE_ACTE, titreDecret);
    }

    public Date getDatePublicationDecret() {
        return getDate(TexteMaitreConstants.DATE_PUBLICATION);
    }

    public void setDatePublicationDecret(Date datePublicationDecret) {
        put(TexteMaitreConstants.DATE_PUBLICATION, datePublicationDecret);
    }

    public void setTypeMesureId(String typeMesureId) {
        put(TexteMaitreConstants.TYPE_MESURE, typeMesureId);
    }

    public String getTypeMesureId() {
        return getString(TexteMaitreConstants.TYPE_MESURE);
    }

    public void setDatePrevisionnelleSaisineCE(Date datePrevisionnelleSaisineCE) {
        put(TexteMaitreConstants.DATE_PREVISIONNELLE_SAISINE_CE, datePrevisionnelleSaisineCE);
    }

    public Date getDatePrevisionnelleSaisineCE() {
        return getDate(TexteMaitreConstants.DATE_PREVISIONNELLE_SAISINE_CE);
    }

    public void setDateMiseApplication(Date dateMiseApplication) {
        put(TexteMaitreConstants.DATE_MISE_APPLICATION, dateMiseApplication);
    }

    public Date getDateMiseApplication() {
        return getDate(TexteMaitreConstants.DATE_MISE_APPLICATION);
    }

    /**
     * @return the id
     */
    public String getId() {
        return getString(TexteMaitreConstants.ID);
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        put(TexteMaitreConstants.ID, id);
    }

    /**
     * @return the typeTableau
     */
    public String getTypeTableau() {
        return getString("typeTableau");
    }

    /**
     * @param typeTableau
     *            the typeTableau to set
     */
    public void setTypeTableau(String typeTableau) {
        put("typeTableau", typeTableau);
    }

    @Override
    public String getType() {
        return "LigneProgrammation";
    }

    @Override
    public String getDocIdForSelection() {
        return null;
    }
}
