package fr.dila.solonepg.core.cases;

import fr.dila.solonepg.api.cases.Habilitation;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.constant.STVocabularyConstants;
import java.util.Calendar;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Implementation de {@link Habilitation}
 *
 * @author asatre
 *
 */
public class HabilitationImpl extends TexteMaitreImpl implements Habilitation {
    private static final long serialVersionUID = -3801345718539975375L;

    public HabilitationImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public String getArticle() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.ARTICLE);
    }

    @Override
    public void setArticle(String article) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.ARTICLE, article);
    }

    @Override
    public String getObjetRIM() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.OBJET_RIM);
    }

    @Override
    public void setObjetRIM(String objetRIM) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.OBJET_RIM, objetRIM);
    }

    @Override
    public String getTypeHabilitation() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TYPE_HABILITATION);
    }

    @Override
    public void setTypeHabilitation(String typeHabilitation) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TYPE_HABILITATION, typeHabilitation);
    }

    @Override
    public String getTypeHabilitationLabel() {
        return SolonEpgServiceLocator
            .getSolonEpgVocabularyService()
            .getLabelFromId(
                VocabularyConstants.TYPE_HABILITATION_DIRECTORY,
                getTypeHabilitation(),
                STVocabularyConstants.COLUMN_LABEL
            );
    }

    @Override
    public String getConvention() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.CONVENTION);
    }

    @Override
    public void setConvention(String convention) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.CONVENTION, convention);
    }

    @Override
    public Calendar getDateTerme() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_TERME);
    }

    @Override
    public void setDateTerme(Calendar dateTerme) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_TERME, dateTerme);
    }

    @Override
    public String getConventionDepot() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.CONVENTION_DEPOT);
    }

    @Override
    public void setConventionDepot(String conventionDepot) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.CONVENTION_DEPOT, conventionDepot);
    }

    @Override
    public Calendar getDatePrevisionnelleSaisineCE() {
        return getDateProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.DATE_PREVISIONNELLE_SAISINE_CE
        );
    }

    @Override
    public void setDatePrevisionnelleSaisineCE(Calendar datePrevisionnelleSaisineCE) {
        setProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.DATE_PREVISIONNELLE_SAISINE_CE,
            datePrevisionnelleSaisineCE
        );
    }

    @Override
    public Calendar getDatePrevisionnelleCM() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_PREVISIONNELLE_CM);
    }

    @Override
    public void setDatePrevisionnelleCM(Calendar datePrevisionnelleSaisineCM) {
        setProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.DATE_PREVISIONNELLE_CM,
            datePrevisionnelleSaisineCM
        );
    }

    @Override
    public String getLegislature() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.LEGISLATURE);
    }

    @Override
    public void setLegislature(String legislature) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.LEGISLATURE, legislature);
    }

    @Override
    public List<String> getOrdonnanceIds() {
        return getListStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.ORDONNANCE_IDS);
    }

    @Override
    public void setOrdonnanceIds(List<String> ordonnanceIds) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.ORDONNANCE_IDS, ordonnanceIds);
    }

    @Override
    public List<String> getOrdonnanceIdsInvalidated() {
        return getListStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.IDS_INVALIDATED);
    }

    @Override
    public void setOrdonnanceIdsInvalidated(List<String> ordonnanceIdsInvalidated) {
        setProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.IDS_INVALIDATED,
            ordonnanceIdsInvalidated
        );
    }
}
