package fr.dila.solonepg.core.dto.activitenormative;

import fr.dila.solonepg.api.cases.Habilitation;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.dto.HabilitationDTO;
import fr.dila.st.core.util.DateUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link Habilitation}
 *
 * @author asatre
 *
 */
public class HabilitationDTOImpl extends AbstractMapTexteMaitreTableDTO implements HabilitationDTO {
    private static final long serialVersionUID = -4703926338907819603L;

    private static final String VALIDATE = "validate";

    public HabilitationDTOImpl() {
        setCodification(Boolean.FALSE);
        setValidate(Boolean.FALSE);
        setValidation(Boolean.TRUE);
    }

    public HabilitationDTOImpl(Habilitation habilitation) {
        this();
        setNumeroOrdre(habilitation.getNumeroOrdre());

        setId(habilitation.getId());
        setOrdonnanceIds(habilitation.getOrdonnanceIds());

        setArticle(habilitation.getArticle());
        setCodification(habilitation.getCodification());
        setConvention(habilitation.getConvention());
        setConventionDepot(habilitation.getConventionDepot());

        setDatePrevisionnelleCM(DateUtil.toDate(habilitation.getDatePrevisionnelleCM()));
        setDatePrevisionnelleSaisineCE(DateUtil.toDate(habilitation.getDatePrevisionnelleSaisineCE()));
        setDateTerme(DateUtil.toDate(habilitation.getDateTerme()));

        setLegislature(habilitation.getLegislature());
        setMinisterePilote(habilitation.getMinisterePilote());
        setMinisterePiloteLabel(habilitation.getMinisterePiloteLabel());
        setObjetRIM(habilitation.getObjetRIM());
        setObservation(habilitation.getObservation());
        setTypeHabilitation(habilitation.getTypeHabilitation());
        setTypeHabilitationLabel(habilitation.getTypeHabilitationLabel());

        setValidation(habilitation.hasValidation());
        setOrdonnanceIdsInvalidated(habilitation.getOrdonnanceIdsInvalidated());
    }

    @Override
    public Habilitation remapField(Habilitation habilitation) {
        habilitation.setArticle(getArticle());
        habilitation.setCodification(getCodification());
        habilitation.setConvention(getConvention());
        habilitation.setConventionDepot(getConventionDepot());
        habilitation.setDatePrevisionnelleCM(DateUtil.toCalendar(getDatePrevisionnelleCM()));
        habilitation.setDatePrevisionnelleSaisineCE(DateUtil.toCalendar(getDatePrevisionnelleSaisineCE()));
        habilitation.setDateTerme(DateUtil.toCalendar(getDateTerme()));
        habilitation.setLegislature(getLegislature());
        habilitation.setMinisterePilote(getMinisterePilote());
        habilitation.setNumeroOrdre(getNumeroOrdre());
        habilitation.setObjetRIM(getObjetRIM());
        habilitation.setObservation(getObservation());
        habilitation.setTypeHabilitation(getTypeHabilitation());

        if (isValidate() || getId() == null) {
            habilitation.setValidation(Boolean.TRUE);
        }

        return habilitation;
    }

    @Override
    public String getId() {
        return getString("uuid");
    }

    @Override
    public void setId(String id) {
        put("uuid", id);
    }

    @Override
    public String getNumeroOrdre() {
        return getString(TexteMaitreConstants.NUMERO_ORDRE);
    }

    @Override
    public void setNumeroOrdre(String numeroOrdre) {
        put(TexteMaitreConstants.NUMERO_ORDRE, numeroOrdre);
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
    public Boolean getCodification() {
        return getBoolean(TexteMaitreConstants.CODIFICATION);
    }

    @Override
    public void setCodification(Boolean codification) {
        put(TexteMaitreConstants.CODIFICATION, codification);
    }

    @Override
    public String getArticle() {
        return getString(TexteMaitreConstants.ARTICLE);
    }

    @Override
    public void setArticle(String article) {
        put(TexteMaitreConstants.ARTICLE, article);
    }

    @Override
    public String getObjetRIM() {
        return getString(TexteMaitreConstants.OBJET_RIM);
    }

    @Override
    public void setObjetRIM(String objetRIM) {
        put(TexteMaitreConstants.OBJET_RIM, objetRIM);
    }

    @Override
    public String getTypeHabilitation() {
        return getString(TexteMaitreConstants.TYPE_HABILITATION);
    }

    @Override
    public void setTypeHabilitation(String typeHabilitation) {
        put(TexteMaitreConstants.TYPE_HABILITATION, typeHabilitation);
    }

    public String getTypeHabilitationLabel() {
        return getString(TexteMaitreConstants.TYPE_HABILITATION_LABEL);
    }

    public void setTypeHabilitationLabel(String typeHabilitation) {
        put(TexteMaitreConstants.TYPE_HABILITATION_LABEL, typeHabilitation);
    }

    @Override
    public String getConvention() {
        return getString(TexteMaitreConstants.CONVENTION);
    }

    @Override
    public void setConvention(String convention) {
        put(TexteMaitreConstants.CONVENTION, convention);
    }

    @Override
    public Date getDateTerme() {
        return getDate(TexteMaitreConstants.DATE_TERME);
    }

    @Override
    public void setDateTerme(Date dateTerme) {
        put(TexteMaitreConstants.DATE_TERME, dateTerme);
    }

    @Override
    public String getConventionDepot() {
        return getString(TexteMaitreConstants.CONVENTION_DEPOT);
    }

    @Override
    public void setConventionDepot(String conventionDepot) {
        put(TexteMaitreConstants.CONVENTION_DEPOT, conventionDepot);
    }

    @Override
    public Date getDatePrevisionnelleSaisineCE() {
        return getDate(TexteMaitreConstants.DATE_PREVISIONNELLE_SAISINE_CE);
    }

    @Override
    public void setDatePrevisionnelleSaisineCE(Date datePrevisionnelleSaisineCE) {
        put(TexteMaitreConstants.DATE_PREVISIONNELLE_SAISINE_CE, datePrevisionnelleSaisineCE);
    }

    @Override
    public Date getDatePrevisionnelleCM() {
        return getDate(TexteMaitreConstants.DATE_PREVISIONNELLE_CM);
    }

    @Override
    public void setDatePrevisionnelleCM(Date datePrevisionnelleCM) {
        put(TexteMaitreConstants.DATE_PREVISIONNELLE_CM, datePrevisionnelleCM);
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
    public String getLegislature() {
        return getString("legislature");
    }

    @Override
    public void setLegislature(String legislature) {
        put("legislature", legislature);
    }

    @Override
    public List<String> getOrdonnanceIds() {
        return getListString(TexteMaitreConstants.ORDONNANCE_IDS);
    }

    @Override
    public void setOrdonnanceIds(List<String> ordonnanceIds) {
        putListString(TexteMaitreConstants.ORDONNANCE_IDS, ordonnanceIds);
    }

    @Override
    public String getType() {
        return Habilitation.class.getSimpleName();
    }

    @Override
    public String getDocIdForSelection() {
        return getId();
    }

    @Override
    public Boolean isValidate() {
        return getBoolean(VALIDATE);
    }

    @Override
    public void setValidate(Boolean validation) {
        put(VALIDATE, validation);
    }

    @Override
    public Boolean hasValidation() {
        return getBoolean(TexteMaitreConstants.HAS_VALIDATION);
    }

    @Override
    public void setValidation(Boolean validation) {
        put(TexteMaitreConstants.HAS_VALIDATION, validation);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getOrdonnanceIdsInvalidated() {
        return (List<String>) get(TexteMaitreConstants.IDS_INVALIDATED);
    }

    @Override
    public void setOrdonnanceIdsInvalidated(List<String> ordonnanceIds) {
        put(TexteMaitreConstants.IDS_INVALIDATED, (Serializable) ordonnanceIds);
    }
}
