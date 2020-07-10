package fr.dila.solonepg.core.dto.activitenormative;

import java.util.Calendar;
import java.util.Date;

import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.solonepg.api.cases.Decret;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.dto.DecretApplicationDTO;
import fr.dila.solonepg.api.dto.OrdonnanceDTO;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.client.AbstractMapDTO;

public class DecretApplicationDTOImpl extends AbstractMapDTO implements DecretApplicationDTO {

    private static final long serialVersionUID = 78416213510924898L;
    
    private static final String VALIDATE = "validate";
    
    private static final String HAS_VALIDATION_LINK = "validationLink";

    public DecretApplicationDTOImpl(Decret decret, Dossier dossier, OrdonnanceDTO ordonnance) {

        setDatePublicationLocked(decret.isDatePublicationLocked());
        setTitreOfficielLocked(decret.isTitreOfficielLocked());
        setTitreDecretLocked(Boolean.FALSE);

        if (dossier != null) {
            RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);

            if (!getDatePublicationLocked()) {
                setDatePublication(retourDila.getDateParutionJorf() != null ? retourDila.getDateParutionJorf().getTime() : null);
            } else {
                setDatePublication(decret.getDatePublication() != null ? decret.getDatePublication().getTime() : null);
            }

            if (!getTitreDecretLocked()) {
                setTitreOfficiel(retourDila.getTitreOfficiel());
            } else {
                setTitreOfficiel(decret.getTitreOfficiel());
            }

            setNumeroNor(dossier.getNumeroNor());

        } else {
            setDatePublication(decret.getDatePublication() != null ? decret.getDatePublication().getTime() : null);
            setTitreOfficiel(decret.getTitreOfficiel());
            setNumeroNor(decret.getNumeroNor());
        }

        setId(decret.getId());

        setLienJORFLegifrance(getLienLegifranceFromJORF(decret.getNumeroNor()));

        setValidation(decret.hasValidation());

        setReferenceDispositionRatification(decret.getReferenceDispositionRatification());
        if (ordonnance.getDecretIdsInvalidated() == null) {
        	setValidationLink(Boolean.TRUE);
        } else {
        	setValidationLink(!ordonnance.getDecretIdsInvalidated().contains(getId()));
        }
    }

    public DecretApplicationDTOImpl() {
        // constructeur vide pour créer une ligne vide
        setValidation(Boolean.FALSE);
        setTitreDecretLocked(Boolean.FALSE);
        setTitreOfficielLocked(Boolean.FALSE);
    }

    public DecretApplicationDTOImpl(Dossier dossier) {
        if (dossier != null) {
            RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);

            setDatePublication(retourDila.getDateParutionJorf() != null ? retourDila.getDateParutionJorf().getTime() : null);
            setTitreOfficiel(retourDila.getTitreOfficiel());

            setId(null);

            setLienJORFLegifrance(getLienLegifranceFromJORF(dossier.getNumeroNor()));
            setNumeroNor(dossier.getNumeroNor());
        }
    }

    private String getLienLegifranceFromJORF(String jorfLegifrance) {
        return SolonEpgServiceLocator.getActiviteNormativeService().createLienJORFLegifrance(jorfLegifrance);
    }

    @Override
    public Decret remapField(Decret decret) throws ClientException {

        decret.setDatePublicationLocked(getDatePublicationLocked());
        decret.setTitreOfficielLocked(getTitreDecretLocked());

        if (getDatePublication() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDatePublication());
            decret.setDatePublication(cal);
        } else {
            decret.setDatePublication(null);
        }

        decret.setNumeroNor(getNumeroNor());
        decret.setTitreOfficiel(getTitreOfficiel());
        decret.setReferenceDispositionRatification(getReferenceDispositionRatification());

        return decret;
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
    public String getType() {
        return "Decret";
    }

    @Override
    public String getDocIdForSelection() {
        return null;
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
    public String getTitreOfficiel() {
        return getString(TexteMaitreConstants.TITRE_OFFICIEL);
    }

    @Override
    public void setTitreOfficiel(String titreOfficiel) {
        put(TexteMaitreConstants.TITRE_OFFICIEL, titreOfficiel);
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
    public String getLienJORFLegifrance() {
        return getString(TexteMaitreConstants.LIEN_JORF_LEGIFRANCE);
    }

    @Override
    public void setLienJORFLegifrance(String lienJORFLegifrance) {
        put(TexteMaitreConstants.LIEN_JORF_LEGIFRANCE, lienJORFLegifrance);
    }

    @Override
    public Boolean getDatePublicationLocked() {
        return getBoolean(TexteMaitreConstants.DATE_PUBLICATION_LOCKED);
    }

    @Override
    public void setDatePublicationLocked(Boolean datePublicationLocked) {
        put(TexteMaitreConstants.DATE_PUBLICATION_LOCKED, datePublicationLocked);
    }

    @Override
    public Boolean getTitreDecretLocked() {
        return getBoolean(TexteMaitreConstants.TITRE_ACTE_LOCKED);
    }

    @Override
    public void setTitreDecretLocked(Boolean titreDecretLocked) {
        put(TexteMaitreConstants.TITRE_ACTE_LOCKED, titreDecretLocked);
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
    public String getReferenceDispositionRatification() {
        return getString(TexteMaitreConstants.REFERENCE_DISPOSITION_RATIFICATION);
    }

    @Override
    public void setReferenceDispositionRatification(String referenceDispositionRatification) {
        put(TexteMaitreConstants.REFERENCE_DISPOSITION_RATIFICATION, referenceDispositionRatification);
    }

    @Override
    public String getTitreDecret() {
        return getString(TexteMaitreConstants.TITRE_ACTE);
    }

    @Override
    public void setTitreDecret(String titreDecret) {
        put(TexteMaitreConstants.TITRE_ACTE, titreDecret);
    }

    @Override
    public String getEtapeSolon() {
        return getString(TexteMaitreConstants.ETAPE_SOLON);
    }

    @Override
    public void setEtapeSolon(String etapeSolon) {
        put(TexteMaitreConstants.ETAPE_SOLON, etapeSolon);
    }

    @Override
    public String getArticleOrdonnance() {
        return getString(TexteMaitreConstants.ARTICLE);
    }

    @Override
    public void setArticleOrdonnance(String articleOrdonnance) {
        put(TexteMaitreConstants.ARTICLE, articleOrdonnance);
    }

    @Override
    public Boolean getTitreOfficielLocked() {
        return getBoolean(TexteMaitreConstants.TITRE_OFFICIEL_LOCKED);
    }

    @Override
    public void setTitreOfficielLocked(Boolean titreOfficielLocked) {
        put(TexteMaitreConstants.TITRE_OFFICIEL_LOCKED, titreOfficielLocked);
    }
    
    public Boolean isValidate() {
        return getBoolean(VALIDATE);
    }

    public void setValidate(Boolean validation) {
        put(VALIDATE, validation);
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
