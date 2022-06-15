package fr.dila.solonepg.core.dto.activitenormative;

import fr.dila.solonepg.api.cases.TranspositionDirective;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.dto.TranspositionDirectiveDTO;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.util.DateUtil;
import java.util.Date;

/**
 * DTo implementation for {@link TranspositionDirective}
 *
 * @author admin
 */
public class TranspositionDirectiveDTOImpl extends AbstractMapDTO implements TranspositionDirectiveDTO {
    private static final long serialVersionUID = 1610950990094969530L;

    private TranspositionDirectiveDTOImpl() {
        // private default constructor
    }

    public TranspositionDirectiveDTOImpl(TranspositionDirective transpositionDirective) {
        this();
        setId(transpositionDirective.getId());
        setNumero(transpositionDirective.getNumero());
        setDateDirective(DateUtil.toDate(transpositionDirective.getDateDirective()));
        setDateProchainTabAffichage(DateUtil.toDate(transpositionDirective.getDateProchainTabAffichage()));
        setDateEcheance(DateUtil.toDate(transpositionDirective.getDateEcheance()));
        setTitreOfficiel(transpositionDirective.getTitre());
        setMinisterePilote(transpositionDirective.getMinisterePilote());
        setMinisterePiloteLabel(transpositionDirective.getMinisterePiloteLabel());
        setAchevee(transpositionDirective.isAchevee());
        setObservation(transpositionDirective.getObservation());
        setTabAffichageMarcheInt(transpositionDirective.isTabAffichageMarcheInt());
        setDirectionResponsable(transpositionDirective.getDirectionResponsable());
        setDateTranspositionEffective(DateUtil.toDate(transpositionDirective.getDateTranspositionEffective()));
        setChampLibre(transpositionDirective.getChampLibre());
        setEtat(transpositionDirective.getEtat());
        String etatLabel = SolonEpgServiceLocator
            .getSolonEpgVocabularyService()
            .getLabelFromId(
                VocabularyConstants.VOC_DIRECTIVE_ETAT,
                transpositionDirective.getEtat(),
                STVocabularyConstants.COLUMN_LABEL
            );
        setEtatLabel(etatLabel);
    }

    @Override
    public TranspositionDirective remapField(TranspositionDirective transpositionDirective) {
        transpositionDirective.setAchevee(getAchevee());
        transpositionDirective.setDateDirective(DateUtil.toCalendar(getDateDirective()));
        transpositionDirective.setDateEcheance(DateUtil.toCalendar(getDateEcheance()));
        transpositionDirective.setDateProchainTabAffichage(DateUtil.toCalendar(getDateProchainTabAffichage()));
        transpositionDirective.setDateTranspositionEffective(DateUtil.toCalendar(getDateTranspositionEffective()));
        transpositionDirective.setMinisterePilote(getMinisterePilote());
        transpositionDirective.setDirectionResponsable(getDirectionResponsable());
        transpositionDirective.setChampLibre(getChampLibre());
        transpositionDirective.setNumero(getNumero());
        transpositionDirective.setObservation(getObservation());
        transpositionDirective.setTitre(getTitreOfficiel());
        transpositionDirective.setTabAffichageMarcheInt(getTabAffichageMarcheInt());
        transpositionDirective.setEtat(getEtat());
        return transpositionDirective;
    }

    @Override
    public String getId() {
        return getString(TexteMaitreConstants.ID);
    }

    @Override
    public void setId(String id) {
        put(TexteMaitreConstants.ID, id);
    }

    @Override
    public String getNumero() {
        return getString(TexteMaitreConstants.NUMERO);
    }

    @Override
    public void setNumero(String numero) {
        put(TexteMaitreConstants.NUMERO, numero);
    }

    @Override
    public Date getDateDirective() {
        return getDate("dateDirective");
    }

    @Override
    public void setDateDirective(Date dateDirective) {
        put("dateDirective", dateDirective);
    }

    @Override
    public String getTitreOfficiel() {
        return getString(TexteMaitreConstants.TITRE_OFFICIEL);
    }

    @Override
    public void setTitreOfficiel(String titreOfficiel) {
        put(TexteMaitreConstants.TITRE_OFFICIEL, titreOfficiel);
    }

    @Override
    public Date getDateEcheance() {
        return getDate("dateEcheance");
    }

    @Override
    public void setDateEcheance(Date dateEcheance) {
        put("dateEcheance", dateEcheance);
    }

    @Override
    public Boolean getTabAffichageMarcheInt() {
        return getBoolean("tabAffichageMarcheInt");
    }

    @Override
    public void setTabAffichageMarcheInt(Boolean tabAffichageMarcheInt) {
        put("tabAffichageMarcheInt", tabAffichageMarcheInt);
    }

    @Override
    public Date getDateProchainTabAffichage() {
        return getDate("dateProchainTabAffichage");
    }

    @Override
    public void setDateProchainTabAffichage(Date dateProchainTabAffichage) {
        put("dateProchainTabAffichage", dateProchainTabAffichage);
    }

    @Override
    public String getMinisterePilote() {
        return getString(TexteMaitreConstants.MINISTERE_PILOTE);
    }

    @Override
    public void setMinisterePilote(String ministerePilote) {
        put(TexteMaitreConstants.MINISTERE_PILOTE, ministerePilote);
    }

    @Override
    public String getMinisterePiloteLabel() {
        return getString(TexteMaitreConstants.MINISTERE_PILOTE_LABEL);
    }

    @Override
    public void setMinisterePiloteLabel(String ministerePilote) {
        put(TexteMaitreConstants.MINISTERE_PILOTE_LABEL, ministerePilote);
    }

    @Override
    public Boolean getAchevee() {
        return getBoolean("achevee");
    }

    @Override
    public void setAchevee(Boolean achevee) {
        put("achevee", achevee);
    }

    @Override
    public String getObservation() {
        return getString(TexteMaitreConstants.OBSERVATION);
    }

    @Override
    public void setObservation(String observation) {
        put(TexteMaitreConstants.OBSERVATION, observation);
    }

    @Override
    public void setDirectionResponsable(String directionResponsable) {
        put("directionResponsable", directionResponsable);
    }

    @Override
    public String getDirectionResponsable() {
        return getString("directionResponsable");
    }

    public void setDateTranspositionEffective(Date dateTranspositionEffective) {
        put("dateTranspositionEffective", dateTranspositionEffective);
    }

    public Date getDateTranspositionEffective() {
        return getDate("dateTranspositionEffective");
    }

    public void setChampLibre(String champLibre) {
        put(TexteMaitreConstants.CHAMP_LIBRE, champLibre);
    }

    public String getChampLibre() {
        return getString(TexteMaitreConstants.CHAMP_LIBRE);
    }

    @Override
    public String getEtat() {
        return getString("etat");
    }

    @Override
    public void setEtat(String etat) {
        put("etat", etat);
    }

    public String getEtatLabel() {
        return getString("etatLabel");
    }

    public void setEtatLabel(String etatLabel) {
        put("etatLabel", etatLabel);
    }

    @Override
    public String getType() {
        return TranspositionDirective.class.getSimpleName();
    }

    @Override
    public String getDocIdForSelection() {
        return getId();
    }
}
