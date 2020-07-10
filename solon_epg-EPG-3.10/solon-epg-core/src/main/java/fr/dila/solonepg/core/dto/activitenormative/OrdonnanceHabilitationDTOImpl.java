package fr.dila.solonepg.core.dto.activitenormative;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.Ordonnance;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.dto.HabilitationDTO;
import fr.dila.solonepg.api.dto.OrdonnanceHabilitationDTO;
import fr.dila.st.core.client.AbstractMapDTO;

public class OrdonnanceHabilitationDTOImpl extends AbstractMapDTO implements OrdonnanceHabilitationDTO {

    private static final long serialVersionUID = 1922121777609238106L;

    private static final String VALIDATE = "validate";
    private static final String HAS_VALIDATION_LINK = "validationLink";

    public OrdonnanceHabilitationDTOImpl(String numeroNor) {
        setNumeroNor(numeroNor);
        setDatePassageCMLocked(Boolean.FALSE);
        setDatePublicationLocked(Boolean.FALSE);
        setDateSaisineCELocked(Boolean.FALSE);
        setNumeroLocked(Boolean.FALSE);
        setTitreOfficielLocked(Boolean.FALSE);
        setMinisterePiloteLocked(Boolean.FALSE);
        setObjetLocked(Boolean.FALSE);
        setValidate(Boolean.TRUE);
        setValidation(Boolean.TRUE);
    }

    public OrdonnanceHabilitationDTOImpl(Ordonnance ordonnance, HabilitationDTO habilitationDTO, Dossier dossier) {
        this(ordonnance.getNumeroNor());

        setConventionDepot(StringUtils.isNotBlank(ordonnance.getConventionDepot()) ? ordonnance.getConventionDepot() : habilitationDTO
                .getConventionDepot());

        if (dossier != null) {

            setMinisterePilote(dossier.getMinistereResp());

            if (!ordonnance.isDateSaisineCELocked()) {
                ConseilEtat conseilEtat = dossier.getDocument().getAdapter(ConseilEtat.class);
                setDateSaisineCE(conseilEtat.getDateSaisineCE() != null ? conseilEtat.getDateSaisineCE().getTime() : null);
            } else {
                setDateSaisineCE(ordonnance.getDateSaisineCE() != null ? ordonnance.getDateSaisineCE().getTime() : null);
            }

            setDatePassageCM(ordonnance.getDatePassageCM() != null ? ordonnance.getDatePassageCM().getTime() : null);

            RetourDila retourDila = null;

            if (!ordonnance.isDatePublicationLocked()) {

                if (retourDila == null) {
                    retourDila = dossier.getDocument().getAdapter(RetourDila.class);
                }

                setDatePublication(retourDila.getDateParutionJorf() != null ? retourDila.getDateParutionJorf().getTime() : null);
            } else {
                setDatePublication(ordonnance.getDatePublication() != null ? ordonnance.getDatePublication().getTime() : null);
            }

            if (!ordonnance.isTitreOfficielLocked()) {

                if (retourDila == null) {
                    retourDila = dossier.getDocument().getAdapter(RetourDila.class);
                }

                setTitreOfficiel(retourDila.getTitreOfficiel());
            } else {
                setTitreOfficiel(ordonnance.getTitreOfficiel());
            }

            if (!ordonnance.isNumeroLocked()) {

                if (retourDila == null) {
                    retourDila = dossier.getDocument().getAdapter(RetourDila.class);
                }

                setNumero(retourDila.getNumeroTexteParutionJorf());
            } else {
                setNumero(ordonnance.getNumero());
            }

            if (!ordonnance.isObjetLocked()) {
                setObjet(dossier.getTitreActe());
            } else {
                setObjet(ordonnance.getObjet());
            }

            setLegislature(ordonnance.getLegislature()); // calcul systeme...

        } else {

            setDateLimiteDepot(ordonnance.getDateLimiteDepot() != null ? ordonnance.getDateLimiteDepot().getTime() : null);
            setDatePassageCM(ordonnance.getDatePassageCM() != null ? ordonnance.getDatePassageCM().getTime() : null);
            setDatePublication(ordonnance.getDatePublication() != null ? ordonnance.getDatePublication().getTime() : null);
            setDateSaisineCE(ordonnance.getDateSaisineCE() != null ? ordonnance.getDateSaisineCE().getTime() : null);
            setLegislature(ordonnance.getLegislature());
            setMinisterePilote(ordonnance.getMinisterePilote());
            setNumero(ordonnance.getNumero());
            setObjet(ordonnance.getObjet());
            setTitreOfficiel(ordonnance.getTitreOfficiel());
        }

        setId(ordonnance.getId());

        setDatePassageCMLocked(ordonnance.isDatePassageCMLocked());
        setDatePublicationLocked(ordonnance.isDatePublicationLocked());
        setDateSaisineCELocked(ordonnance.isDateSaisineCELocked());
        setNumeroLocked(ordonnance.isNumeroLocked());
        setTitreOfficielLocked(ordonnance.isTitreOfficielLocked());
        setMinisterePiloteLocked(ordonnance.isMinisterePiloteLocked());
        setObjetLocked(ordonnance.isObjetLocked());

        setValidation(ordonnance.hasValidation());
        if (habilitationDTO.getOrdonnanceIdsInvalidated() == null) {
        	setValidationLink(Boolean.TRUE);
        } else {
        	setValidationLink(!habilitationDTO.getOrdonnanceIdsInvalidated().contains(getId()));
        }
    }

    public OrdonnanceHabilitationDTOImpl(String numeroNor, HabilitationDTO habilitationDTO) {
        this(numeroNor);
        setConventionDepot(habilitationDTO.getConventionDepot());
    }

    @Override
    public Ordonnance remapField(Ordonnance ordonnance) throws ClientException {

        ordonnance.setConventionDepot(getConventionDepot());

        if (getDateLimiteDepot() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDateLimiteDepot());
            ordonnance.setDateLimiteDepot(cal);
        } else {
            ordonnance.setDateLimiteDepot(null);
        }

        if (getDatePassageCM() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDatePassageCM());
            ordonnance.setDatePassageCM(cal);
        } else {
            ordonnance.setDatePassageCM(null);
        }

        if (getDatePublication() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDatePublication());
            ordonnance.setDatePublication(cal);
        } else {
            ordonnance.setDatePublication(null);
        }

        if (getDateSaisineCE() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDateSaisineCE());
            ordonnance.setDateSaisineCE(cal);
        } else {
            ordonnance.setDateSaisineCE(null);
        }

        ordonnance.setLegislature(getLegislature());
        ordonnance.setMinisterePilote(getMinisterePilote());
        ordonnance.setNumero(getNumero());
        ordonnance.setNumeroNor(getNumeroNor());
        ordonnance.setObjet(getObjet());
        ordonnance.setTitreOfficiel(getTitreOfficiel());

        ordonnance.setDatePassageCMLocked(isDatePassageCMLocked());
        ordonnance.setDatePublicationLocked(isDatePublicationLocked());
        ordonnance.setDateSaisineCELocked(isDateSaisineCELocked());
        ordonnance.setNumeroLocked(isNumeroLocked());
        ordonnance.setTitreOfficielLocked(isTitreOfficielLocked());
        ordonnance.setObservation(getObservation());

        if (isValidate()) {
            ordonnance.setValidation(Boolean.TRUE);
        }

        return ordonnance;
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
    public String getNumeroNor() {
        return getString(TexteMaitreConstants.NUMERO_NOR);
    }

    @Override
    public void setNumeroNor(String numeroNor) {
        put(TexteMaitreConstants.NUMERO_NOR, numeroNor);
    }

    @Override
    public String getObjet() {
        return getString(TexteMaitreConstants.OBJET);
    }

    @Override
    public void setObjet(String objet) {
        put(TexteMaitreConstants.OBJET, objet);
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
    public String getLegislature() {
        return getString(TexteMaitreConstants.LEGISLATURE);
    }

    @Override
    public void setLegislature(String legislature) {
        put(TexteMaitreConstants.LEGISLATURE, legislature);
    }

    @Override
    public Date getDateSaisineCE() {
        return getDate(TexteMaitreConstants.DATE_SAISINE_CE);
    }

    @Override
    public void setDateSaisineCE(Date dateSaisineCE) {
        put(TexteMaitreConstants.DATE_SAISINE_CE, dateSaisineCE);
    }

    @Override
    public Date getDatePassageCM() {
        return getDate(TexteMaitreConstants.DATE_PASSAGE_CM);
    }

    @Override
    public void setDatePassageCM(Date datePassageCM) {
        put(TexteMaitreConstants.DATE_PASSAGE_CM, datePassageCM);
    }

    @Override
    public Date getDatePublication() {
        return getDate(TexteMaitreConstants.DATE_PUBLICATION);
    }

    @Override
    public void setDatePublication(Date datePublication) {
        put(TexteMaitreConstants.DATE_PUBLICATION, datePublication);
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
    public String getNumero() {
        return getString(TexteMaitreConstants.NUMERO);
    }

    @Override
    public void setNumero(String numero) {
        put(TexteMaitreConstants.NUMERO, numero);
    }

    @Override
    public String getConventionDepot() {
        return getString(TexteMaitreConstants.CONVENTION_DEPOT);
    }

    @Override
    public void setConventionDepot(String convention) {
        put(TexteMaitreConstants.CONVENTION_DEPOT, convention);
    }

    @Override
    public Date getDateLimiteDepot() {
        return getDate(TexteMaitreConstants.DATE_LIMITE_DEPOT);
    }

    @Override
    public void setDateLimiteDepot(Date dateLimiteDepot) {
        put(TexteMaitreConstants.DATE_LIMITE_DEPOT, dateLimiteDepot);
    }

    @Override
    public String getType() {
        return Ordonnance.class.getSimpleName();
    }

    @Override
    public String getDocIdForSelection() {
        return getId();
    }

    @Override
    public Boolean isDateSaisineCELocked() {
        return getBoolean(TexteMaitreConstants.DATE_SAISINE_CE_LOCKED);
    }

    @Override
    public void setDateSaisineCELocked(Boolean dateSaisineCELocked) {
        put(TexteMaitreConstants.DATE_SAISINE_CE_LOCKED, dateSaisineCELocked);
    }

    @Override
    public Boolean isDatePassageCMLocked() {
        return getBoolean(TexteMaitreConstants.DATE_PASSAGE_CM_LOCKED);
    }

    @Override
    public void setDatePassageCMLocked(Boolean datePassageCMLocked) {
        put(TexteMaitreConstants.DATE_PASSAGE_CM_LOCKED, datePassageCMLocked);
    }

    @Override
    public Boolean isDatePublicationLocked() {
        return getBoolean(TexteMaitreConstants.DATE_PUBLICATION_LOCKED);
    }

    @Override
    public void setDatePublicationLocked(Boolean datePublicationLocked) {
        put(TexteMaitreConstants.DATE_PUBLICATION_LOCKED, datePublicationLocked);
    }

    @Override
    public Boolean isTitreOfficielLocked() {
        return getBoolean(TexteMaitreConstants.TITRE_OFFICIEL_LOCKED);
    }

    @Override
    public void setTitreOfficielLocked(Boolean titreOfficielLocked) {
        put(TexteMaitreConstants.TITRE_OFFICIEL_LOCKED, titreOfficielLocked);
    }

    @Override
    public Boolean isNumeroLocked() {
        return getBoolean(TexteMaitreConstants.NUMERO_LOCKED);
    }

    @Override
    public void setNumeroLocked(Boolean numeroLocked) {
        put(TexteMaitreConstants.NUMERO_LOCKED, numeroLocked);
    }

    @Override
    public Boolean isObjetLocked() {
        return getBoolean(TexteMaitreConstants.OBJET_LOCKED);
    }

    @Override
    public void setObjetLocked(Boolean objetLocked) {
        put(TexteMaitreConstants.OBJET_LOCKED, objetLocked);
    }

    @Override
    public Boolean isMinisterePiloteLocked() {
        return getBoolean(TexteMaitreConstants.MINISTERE_PILOTE_LOCKED);
    }

    @Override
    public void setMinisterePiloteLocked(Boolean ministerePiloteLocked) {
        put(TexteMaitreConstants.MINISTERE_PILOTE_LOCKED, ministerePiloteLocked);
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

    @Override
    public void setObservation(String observation) {
        put(TexteMaitreConstants.OBSERVATION, observation);
    }

    @Override
    public String getObservation() {
        return TexteMaitreConstants.OBSERVATION;
    }

    @Override
	public Boolean hasValidationLink() {
		return getBoolean(HAS_VALIDATION_LINK);
	}

	@Override
	public void setValidationLink(Boolean validationLink) {
		put(HAS_VALIDATION_LINK, validationLink);
	}
}
