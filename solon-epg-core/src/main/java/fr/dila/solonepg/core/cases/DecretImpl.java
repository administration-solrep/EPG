package fr.dila.solonepg.core.cases;

import fr.dila.solonepg.api.cases.Decret;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.constant.STVocabularyConstants;
import java.util.Calendar;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Implementation de {@link Decret} .
 *
 * @author asatre
 *
 */
public class DecretImpl extends TexteMaitreImpl implements Decret {
    private static final long serialVersionUID = 4376293619619241293L;

    public DecretImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public String getReferenceAvisCE() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.REFERENCE_AVIS_CE);
    }

    @Override
    public void setReferenceAvisCE(String referenceAvisCE) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.REFERENCE_AVIS_CE, referenceAvisCE);
    }

    @Override
    public String getNumerosTextes() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMEROS_TEXTES);
    }

    @Override
    public void setNumerosTextes(String numerosTextes) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMEROS_TEXTES, numerosTextes);
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
    public String getDelaiPublication() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DELAI_PUBLICATION);
    }

    @Override
    public void setDelaiPublication(String delaiPublication) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DELAI_PUBLICATION, delaiPublication);
    }

    @Override
    public String getId() {
        return document.getId();
    }

    @Override
    public Calendar getDateSectionCe() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_SECTION_CE);
    }

    @Override
    public void setDateSectionCe(Calendar dateSectionCe) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_SECTION_CE, dateSectionCe);
    }

    @Override
    public Calendar getDateSaisineCE() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_SAISINE_CE);
    }

    @Override
    public void setDateSaisineCE(Calendar dateSaisineCE) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_SAISINE_CE, dateSaisineCE);
    }

    @Override
    public Calendar getDateSortieCE() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_SORTIE_CE);
    }

    @Override
    public void setDateSortieCE(Calendar dateSortieCE) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_SORTIE_CE, dateSortieCE);
    }

    @Override
    public Long getNumeroPage() {
        return getLongProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMERO_PAGE);
    }

    @Override
    public void setNumeroPage(Long numeroPage) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMERO_PAGE, numeroPage);
    }

    @Override
    public String getRapporteurCe() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.RAPPORTEUR_CE);
    }

    @Override
    public void setRapporteurCe(String rapporteurCe) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.RAPPORTEUR_CE, rapporteurCe);
    }

    @Override
    public String getSectionCe() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.SECTION_CE);
    }

    @Override
    public void setSectionCe(String sectionCe) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.SECTION_CE, sectionCe);
    }

    @Override
    public String getTypeActe() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TYPE_ACTE);
    }

    @Override
    public void setTypeActe(String typeActe) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TYPE_ACTE, typeActe);
    }

    @Override
    public String getTypeActeLabel() {
        return SolonEpgServiceLocator
            .getSolonEpgVocabularyService()
            .getLabelFromId(
                VocabularyConstants.TYPE_ACTE_VOCABULARY,
                getTypeActe(),
                STVocabularyConstants.COLUMN_LABEL
            );
    }

    @Override
    public List<String> getMesureIds() {
        return getListStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.MESURE_IDS);
    }

    @Override
    public void setMesureIds(List<String> idMesures) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.MESURE_IDS, idMesures);
    }

    @Override
    public String getNumeroJOPublication() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMERO_JO_PUBLICATION);
    }

    @Override
    public void setNumeroJOPublication(String numeroJOPublication) {
        setProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.NUMERO_JO_PUBLICATION,
            numeroJOPublication
        );
    }

    @Override
    public Boolean isDateSectionCeLocked() {
        return getBooleanProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.DATE_SECTION_CE_LOCKED
        );
    }

    @Override
    public void setDateSectionCeLocked(Boolean dateSectionCeLocked) {
        setProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.DATE_SECTION_CE_LOCKED,
            dateSectionCeLocked
        );
    }

    @Override
    public Boolean isDateSaisineCELocked() {
        return getBooleanProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.DATE_SAISINE_CE_LOCKED
        );
    }

    @Override
    public void setDateSaisineCELocked(Boolean dateSaisineCELocked) {
        setProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.DATE_SAISINE_CE_LOCKED,
            dateSaisineCELocked
        );
    }

    @Override
    public Boolean isDateSortieCELocked() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_SORTIE_CE_LOCKED);
    }

    @Override
    public void setDateSortieCELocked(Boolean dateSortieCELocked) {
        setProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.DATE_SORTIE_CE_LOCKED,
            dateSortieCELocked
        );
    }

    @Override
    public Boolean isNumeroJOPublicationLocked() {
        return getBooleanProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.NUMERO_JO_PUBLICATION_LOCKED
        );
    }

    @Override
    public void setNumeroJOPublicationLocked(Boolean numeroJOPublication) {
        setProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.NUMERO_JO_PUBLICATION_LOCKED,
            numeroJOPublication
        );
    }

    @Override
    public Boolean isNumeroPageLocked() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMERO_PAGE_LOCKED);
    }

    @Override
    public void setNumeroPageLocked(Boolean numeroPageLocked) {
        setProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.NUMERO_PAGE_LOCKED,
            numeroPageLocked
        );
    }

    @Override
    public Boolean isRapporteurCeLocked() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.RAPPORTEUR_CE_LOCKED);
    }

    @Override
    public void setRapporteurCeLocked(Boolean rapporteurCeLocked) {
        setProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.RAPPORTEUR_CE_LOCKED,
            rapporteurCeLocked
        );
    }

    @Override
    public Boolean isSectionCeLocked() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.SECTION_CE_LOCKED);
    }

    @Override
    public void setSectionCeLocked(Boolean sectionCeLocked) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.SECTION_CE_LOCKED, sectionCeLocked);
    }

    @Override
    public Boolean isTypeActeLocked() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TYPE_ACTE_LOCKED);
    }

    @Override
    public void setTypeActeLocked(Boolean typeActeLocked) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TYPE_ACTE_LOCKED, typeActeLocked);
    }

    @Override
    public List<String> getOrdonnanceIds() {
        return getListStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.ORDONNANCE_IDS);
    }

    @Override
    public void setOrdonnanceIds(List<String> idsLoiDeRatification) {
        setProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.ORDONNANCE_IDS,
            idsLoiDeRatification
        );
    }

    @Override
    public String getReferenceDispositionRatification() {
        return getStringProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.REFERENCE_DISPOSITION_RATIFICATION
        );
    }

    @Override
    public void setReferenceDispositionRatification(String referenceDispositionRatification) {
        setProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.REFERENCE_DISPOSITION_RATIFICATION,
            referenceDispositionRatification
        );
    }
}
