package fr.dila.solonepg.core.dto.activitenormative;

import java.util.Calendar;
import java.util.Date;

import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.cases.Decret;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.dto.DecretDTO;
import fr.dila.solonepg.api.dto.MesureApplicativeDTO;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.client.AbstractMapDTO;

public class DecretDTOImpl extends AbstractMapDTO implements DecretDTO {

    private static final String VALIDATE = "validate";

    private static final String HAS_VALIDATION_LINK = "validationLink";
    
    private static final long serialVersionUID = 78416213510924898L;

    public DecretDTOImpl() {
        // pour créer une ligne vide
        setDefault();
    }

    public DecretDTOImpl(Decret decret, Dossier dossier, MesureApplicativeDTO mesure, TexteMaitre texteMaitre) {
        setDefault();

        setDateSectionCELocked(decret.isDateSectionCeLocked());
        setDatePublicationLocked(decret.isDatePublicationLocked());
        setDateSaisineCELocked(decret.isDateSaisineCELocked());
        setDateSortieCELocked(decret.isDateSortieCELocked());
        setNumeroJOPublicationLocked(decret.isNumeroJOPublicationLocked());
        setNumeroPageLocked(decret.isNumeroPageLocked());
        setRapporteurCELocked(decret.isRapporteurCeLocked());
        setSectionCeLocked(decret.isSectionCeLocked());
        setTitreOfficielLocked(decret.isTitreOfficielLocked());
        setTypeActeLocked(decret.isTypeActeLocked());

        if (dossier == null) {
            setDateSectionCE(decret.getDateSectionCe() == null ? null : decret.getDateSectionCe().getTime());
            setDatePublication(decret.getDatePublication() == null ? null : decret.getDatePublication().getTime());
            setDateSaisineCE(decret.getDateSaisineCE() == null ? null : decret.getDateSaisineCE().getTime() );
            setDateSortieCE(decret.getDateSortieCE() == null ? null : decret.getDateSortieCE().getTime());
            setNumeroJOPublication(decret.getNumeroJOPublication());
            setNumeroPage(decret.getNumeroPage());
            setRapporteurCE(decret.getRapporteurCe());
            setSectionCE(decret.getSectionCe());
            setReferenceAvisCE(decret.getReferenceAvisCE());
            setTitreOfficiel(decret.getTitreOfficiel());
            setNumeroNor(decret.getNumeroNor());
            setTypeActe(decret.getTypeActe());
            setDateSignature(decret.getDateSignature() == null ? extractDateSignature(decret.getTitreOfficiel()) : decret.getDateSignature().getTime() );
            setNumerosTextes(decret.getNumerosTextes());

        } else {
            
            ConseilEtat conseilEtat = dossier.getDocument().getAdapter(ConseilEtat.class);
            RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);

            if (getDateSectionCELocked()) {
                setDateSectionCE(decret.getDateSectionCe() == null ? null : decret.getDateSectionCe().getTime());
            } else {
                setDateSectionCE(conseilEtat.getDateSectionCe() == null ? null : conseilEtat.getDateSectionCe().getTime());
            }

            if (getDatePublicationLocked()) {
                setDatePublication(decret.getDatePublication() == null ? null : decret.getDatePublication().getTime() );
            } else {
                setDatePublication(retourDila.getDateParutionJorf() == null ? null : retourDila.getDateParutionJorf().getTime());
            }

            if (getDateSaisineCELocked()) {
                setDateSaisineCE(decret.getDateSaisineCE() == null ? null : decret.getDateSaisineCE().getTime());
            } else {
                setDateSaisineCE(conseilEtat.getDateSaisineCE() == null ? null : conseilEtat.getDateSaisineCE().getTime()  );
            }

            if (getDateSortieCELocked()) {
                setDateSortieCE(decret.getDateSortieCE() == null ? null :decret.getDateSortieCE().getTime());
            } else {
                setDateSortieCE(conseilEtat.getDateSortieCE() == null ? null : conseilEtat.getDateSortieCE().getTime());
            }

            if (getNumeroJOPublicationLocked()) {
                setNumerosTextes(decret.getNumerosTextes());
            } else {
                setNumerosTextes(retourDila.getNumeroTexteParutionJorf());
            }

            setNumeroJOPublication(decret.getNumeroJOPublication());

            if (getNumeroPageLocked()) {
                setNumeroPage(decret.getNumeroPage());
            } else {
                setNumeroPage(retourDila.getPageParutionJorf());
            }

            if (getRapporteurCELocked()) {
                setRapporteurCE(decret.getRapporteurCe());
            } else {
                setRapporteurCE(conseilEtat.getRapporteurCe());
            }

            if (getSectionCeLocked()) {
                setSectionCE(decret.getSectionCe());
            } else {
                setSectionCE(conseilEtat.getSectionCe());
            }

            if (getTitreOfficielLocked()) {
                setTitreOfficiel(decret.getTitreOfficiel());
            } else {
                setTitreOfficiel(retourDila.getTitreOfficiel());
            }
            

            setReferenceAvisCE(conseilEtat.getNumeroISA());

            setNumeroNor(dossier.getNumeroNor());

            if (getTypeActeLocked()) {
                setTypeActe(decret.getTypeActe());
            } else {
                setTypeActe(dossier.getTypeActe());
            }
            setDateSignature(dossier.getDateSignature() == null ? null : dossier.getDateSignature().getTime());
            setNumerosTextes(retourDila.getNumeroTexteParutionJorf());
        }

        setId(decret.getId());


        setLienJORFLegifrance(getLienLegifranceFromJORF(decret.getNumeroNor()));


        setValidation(decret.hasValidation());

        computeDelaiPublication(mesure, texteMaitre);
        
        if (mesure.getDecretIdsInvalidated() == null) {
        	setValidationLink(Boolean.TRUE);
        } else {
        	setValidationLink(!mesure.getDecretIdsInvalidated().contains(getId()));
        }
        

    }

    public DecretDTOImpl(String numeorNor, Dossier dossier, MesureApplicativeDTO mesure, TexteMaitre texteMaitre) {
        setDefault();

        if (dossier != null) {
            ConseilEtat conseilEtat = dossier.getDocument().getAdapter(ConseilEtat.class);
            RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);

            if (!getDateSectionCELocked()) {
                setDateSectionCE(conseilEtat.getDateSectionCe() == null ? null : conseilEtat.getDateSectionCe().getTime());
            }

            if (!getDatePublicationLocked()) {
                setDatePublication(retourDila.getDateParutionJorf() == null ? null : retourDila.getDateParutionJorf().getTime());
            }

            if (!getDateSaisineCELocked()) {
                setDateSaisineCE(conseilEtat.getDateSaisineCE() == null ? null : conseilEtat.getDateSaisineCE().getTime());
            }

            if (!getDateSortieCELocked()) {
                setDateSortieCE(conseilEtat.getDateSortieCE() == null ? null : conseilEtat.getDateSortieCE().getTime());
            }

            if (!getNumeroJOPublicationLocked()) {
                setNumerosTextes(retourDila.getNumeroTexteParutionJorf());
            }

            if (!getNumeroPageLocked()) {
                setNumeroPage(retourDila.getPageParutionJorf());
            }

            if (!getRapporteurCELocked()) {
                setRapporteurCE(conseilEtat.getRapporteurCe());
            }

            if (!getSectionCeLocked()) {
                setSectionCE(conseilEtat.getSectionCe());
            }

            if (!getTitreOfficielLocked()) {
                setTitreOfficiel(retourDila.getTitreOfficiel());
            }

            setNumeroNor(dossier.getNumeroNor());

            if (!getTypeActeLocked()) {
                setTypeActe(dossier.getTypeActe());
            }
        }

        setLienJORFLegifrance(getLienLegifranceFromJORF(numeorNor));
        computeDelaiPublication(mesure, texteMaitre);
        setValidationLink(!mesure.getDecretIdsInvalidated().contains(getId()));
    }

    // RG-LOI-MAI-38 - RG-LOI-MAI-18
    private void computeDelaiPublication(MesureApplicativeDTO mesure, TexteMaitre texteMaitre) {
        String delaiPublication = null;

        if (mesure != null && texteMaitre != null) {
            Date datePublicationDecret = getDatePublication();

            if (datePublicationDecret != null) {
              long dtTime = -1;
              if (mesure.getDiffere() && mesure.getDateEntreeEnVigueur() != null) {
                dtTime = mesure.getDateEntreeEnVigueur().getTime();
              }
              else {
                if(texteMaitre.getDateEntreeEnVigueur() == null) {
                    if(texteMaitre.getDatePublication() != null) {
                        dtTime = texteMaitre.getDatePublication().getTimeInMillis();
                      }
                }
                else {
                    dtTime = texteMaitre.getDateEntreeEnVigueur().getTimeInMillis();
                }
              }
              if(dtTime!=-1){
                long diff = datePublicationDecret.getTime() - dtTime;
                Double result = diff / 1000.0 / 60.0 / 60.0 / 24.0 / 30.0;
                // mantis #39655
                if (result <= 0) {
                    delaiPublication = "0";
                } else {
                    delaiPublication = String.valueOf(Math.round(result));
                }
              }
            }
        }

        setDelaiPublication(delaiPublication);
    }

    private void setDefault() {
        setDateSectionCELocked(Boolean.FALSE);
        setDatePublicationLocked(Boolean.FALSE);
        setDateSaisineCELocked(Boolean.FALSE);
        setDateSortieCELocked(Boolean.FALSE);
        setNumeroJOPublicationLocked(Boolean.FALSE);
        setNumeroPageLocked(Boolean.FALSE);
        setRapporteurCELocked(Boolean.FALSE);
        setSectionCeLocked(Boolean.FALSE);
        setTitreOfficielLocked(Boolean.FALSE);
        setTypeActeLocked(Boolean.FALSE);
        setValidate(Boolean.FALSE);

    }

    private Date extractDateSignature(String titreDecret) {
        Calendar cal = SolonEpgServiceLocator.getActiviteNormativeService().extractDateFromTitre(titreDecret);
        if (cal != null) {
            return cal.getTime();
        }
        return null;
    }

    private String getLienLegifranceFromJORF(String jorfLegifrance) {
        return SolonEpgServiceLocator.getActiviteNormativeService().createLienJORFLegifrance(jorfLegifrance);
    }

    @Override
    public Decret remapField(Decret decret) throws ClientException {

        decret.setDateSectionCeLocked(getDateSectionCELocked());
        decret.setDatePublicationLocked(getDatePublicationLocked());
        decret.setDateSaisineCELocked(getDateSaisineCELocked());
        decret.setDateSortieCELocked(getDateSortieCELocked());
        decret.setNumeroJOPublicationLocked(getNumeroJOPublicationLocked());
        decret.setNumeroPageLocked(getNumeroPageLocked());
        decret.setRapporteurCeLocked(getRapporteurCELocked());
        decret.setSectionCeLocked(getSectionCeLocked());
        decret.setTitreOfficielLocked(getTitreOfficielLocked());
        decret.setTypeActeLocked(getTypeActeLocked());

        if (getDateExamenCE() == null) {
            decret.setDateSectionCe(null);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDateExamenCE());
            decret.setDateSectionCe(cal);
        }

        if (getDatePublication() == null) {
            decret.setDatePublication(null);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDatePublication());
            decret.setDatePublication(cal);
        }

        if (getDateSaisineCE() == null) {
            decret.setDateSaisineCE(null);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDateSaisineCE());
            decret.setDateSaisineCE(cal);
        }

        if (getDateSignature() == null) {
            decret.setDateSignature(null);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDateSignature());
            decret.setDateSignature(cal);
        }

        if (getDateSectionCE() == null) {
            decret.setDateSortieCE(null);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDateSectionCE());
            decret.setDateSortieCE(cal);
        }

        decret.setNumeroJOPublication(getNumeroJOPublication());
        decret.setNumeroNor(getNumeroNor());
        decret.setNumeroPage(getNumeroPage());
        decret.setNumerosTextes(getNumerosTextes());
        decret.setRapporteurCe(getRapporteurCE());
        decret.setReferenceAvisCE(getReferenceAvisCE());
        decret.setSectionCe(getSectionCE());
        decret.setTitreOfficiel(getTitreOfficiel());
        decret.setTypeActe(getTypeActe());

        return decret;
    }

    @Override
    public Decret remapFieldReprise(Decret decret) throws ClientException {

        decret.setDateSectionCeLocked(getDateSectionCELocked());
        decret.setDatePublicationLocked(getDatePublicationLocked());
        decret.setDateSaisineCELocked(getDateSaisineCELocked());
        decret.setDateSortieCELocked(getDateSortieCELocked());
        decret.setNumeroJOPublicationLocked(getNumeroJOPublicationLocked());
        decret.setNumeroPageLocked(getNumeroPageLocked());
        decret.setRapporteurCeLocked(getRapporteurCELocked());
        decret.setSectionCeLocked(getSectionCeLocked());
        decret.setTitreOfficielLocked(getTitreOfficielLocked());
        decret.setTypeActeLocked(getTypeActeLocked());

        if (getDateExamenCE() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDateExamenCE());
            decret.setDateSectionCe(cal);
        }

        if (getDatePublication() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDatePublication());
            decret.setDatePublication(cal);
        }

        if (getDateSaisineCE() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDateSaisineCE());
            decret.setDateSaisineCE(cal);
        }

        if (getDateSignature() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDateSignature());
            decret.setDateSignature(cal);
        }

        if (getDateSectionCE() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDateSectionCE());
            decret.setDateSortieCE(cal);
        }

        if (getNumeroJOPublication() != null && !getNumeroJOPublication().trim().equals("")) {
            decret.setNumeroJOPublication(getNumeroJOPublication());
        }
        if (getNumeroNor() != null && !getNumeroNor().trim().equals("")) {
            decret.setNumeroNor(getNumeroNor());
        }
        if (getNumerosTextes() != null && !getNumerosTextes().trim().equals("")) {
            String numerosTextes = getNumerosTextes();
            decret.setNumerosTextes(numerosTextes);
        }
        if (getRapporteurCE() != null && !getRapporteurCE().trim().equals("")) {
            decret.setRapporteurCe(getRapporteurCE());
        }
        if (getReferenceAvisCE() != null && !getReferenceAvisCE().trim().equals("")) {
            decret.setReferenceAvisCE(getReferenceAvisCE());
        }
        if (getSectionCE() != null && !getSectionCE().trim().equals("")) {
            decret.setSectionCe(getSectionCE());
        }
        if (getTitreOfficiel() != null && !getTitreOfficiel().trim().equals("")) {
            decret.setTitreOfficiel(getTitreOfficiel());
        }
        if (getTypeActe() != null && !getTypeActe().trim().equals("")) {
            decret.setTypeActe(getTypeActe());
        }
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
    public String getTypeActe() {
        return getString(TexteMaitreConstants.TYPE_ACTE);
    }

    @Override
    public void setTypeActe(String typeActe) {
        put(TexteMaitreConstants.TYPE_ACTE, typeActe);
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
    public String getSectionCE() {
        return getString(TexteMaitreConstants.SECTION_CE);
    }

    @Override
    public void setSectionCE(String sectionCE) {
        put(TexteMaitreConstants.SECTION_CE, sectionCE);
    }

    @Override
    public Date getDateExamenCE() {
        return getDate(TexteMaitreConstants.DATE_SECTION_CE);
    }

    @Override
    public void setDateSectionCE(Date dateExamenCE) {
        put(TexteMaitreConstants.DATE_SECTION_CE, dateExamenCE);
    }

    @Override
    public Date getDateSectionCE() {
        return getDate(TexteMaitreConstants.DATE_SORTIE_CE);
    }

    @Override
    public void setDateSortieCE(Date dateSortieCE) {
        put(TexteMaitreConstants.DATE_SORTIE_CE, dateSortieCE);
    }

    @Override
    public String getRapporteurCE() {
        return getString(TexteMaitreConstants.RAPPORTEUR_CE);
    }

    @Override
    public void setRapporteurCE(String rapporteurCE) {
        put(TexteMaitreConstants.RAPPORTEUR_CE, rapporteurCE);
    }

    @Override
    public String getReferenceAvisCE() {
        return getString(TexteMaitreConstants.REFERENCE_AVIS_CE);
    }

    @Override
    public void setReferenceAvisCE(String referenceAvisCE) {
        put(TexteMaitreConstants.REFERENCE_AVIS_CE, referenceAvisCE);
    }

    @Override
    public String getNumerosTextes() {
        return getString(TexteMaitreConstants.NUMEROS_TEXTES);
    }

    @Override
    public void setNumerosTextes(String numerosTextes) {
        put(TexteMaitreConstants.NUMEROS_TEXTES, numerosTextes);
    }

    @Override
    public Date getDateSignature() {
        return getDate(TexteMaitreConstants.DATE_SIGNATURE);
    }

    @Override
    public void setDateSignature(Date dateSignature) {
        put(TexteMaitreConstants.DATE_SIGNATURE, dateSignature);
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
    public String getDelaiPublication() {
        return getString(TexteMaitreConstants.DELAI_PUBLICATION);
    }

    @Override
    public void setDelaiPublication(String delaiPublication) {
        put(TexteMaitreConstants.DELAI_PUBLICATION, delaiPublication);
    }

    @Override
    public String getNumeroJOPublication() {
        return getString(TexteMaitreConstants.NUMERO_JO_PUBLICATION);
    }

    @Override
    public void setNumeroJOPublication(String numeroJOPublication) {
        put(TexteMaitreConstants.NUMERO_JO_PUBLICATION, numeroJOPublication);
    }

    @Override
    public Long getNumeroPage() {
        return getLong(TexteMaitreConstants.NUMERO_PAGE);
    }

    @Override
    public void setNumeroPage(Long numeroPage) {
        put(TexteMaitreConstants.NUMERO_PAGE, numeroPage);
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
    public String getLienJORFLegifrance() {
        return getString(TexteMaitreConstants.LIEN_JORF_LEGIFRANCE);
    }

    @Override
    public void setLienJORFLegifrance(String lienJORFLegifrance) {
        put(TexteMaitreConstants.LIEN_JORF_LEGIFRANCE, lienJORFLegifrance);
    }

    @Override
    public Boolean getDateSectionCELocked() {
        return getBoolean(TexteMaitreConstants.DATE_SECTION_CE_LOCKED);
    }

    @Override
    public void setDateSectionCELocked(Boolean dateExamenCELocked) {
        put(TexteMaitreConstants.DATE_SECTION_CE_LOCKED, dateExamenCELocked);
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
    public Boolean getDateSaisineCELocked() {
        return getBoolean(TexteMaitreConstants.DATE_SAISINE_CE_LOCKED);
    }

    @Override
    public void setDateSaisineCELocked(Boolean dateSaisineCELocked) {
        put(TexteMaitreConstants.DATE_SAISINE_CE_LOCKED, dateSaisineCELocked);
    }

    @Override
    public Boolean getDateSortieCELocked() {
        return getBoolean(TexteMaitreConstants.DATE_SORTIE_CE_LOCKED);
    }

    @Override
    public void setDateSortieCELocked(Boolean dateSortieCELocked) {
        put(TexteMaitreConstants.DATE_SORTIE_CE_LOCKED, dateSortieCELocked);
    }

    @Override
    public Boolean getNumeroJOPublicationLocked() {
        return getBoolean(TexteMaitreConstants.NUMERO_JO_PUBLICATION_LOCKED);
    }

    @Override
    public void setNumeroJOPublicationLocked(Boolean numeroJOPublication) {
        put(TexteMaitreConstants.NUMERO_JO_PUBLICATION_LOCKED, numeroJOPublication);
    }

    @Override
    public Boolean getNumeroPageLocked() {
        return getBoolean(TexteMaitreConstants.NUMERO_PAGE_LOCKED);
    }

    @Override
    public void setNumeroPageLocked(Boolean numeroPageLocked) {
        put(TexteMaitreConstants.NUMERO_PAGE_LOCKED, numeroPageLocked);
    }

    @Override
    public Boolean getRapporteurCELocked() {
        return getBoolean(TexteMaitreConstants.RAPPORTEUR_CE_LOCKED);
    }

    @Override
    public void setRapporteurCELocked(Boolean rapporteurCeLocked) {
        put(TexteMaitreConstants.RAPPORTEUR_CE_LOCKED, rapporteurCeLocked);
    }

    @Override
    public Boolean getSectionCeLocked() {
        return getBoolean(TexteMaitreConstants.SECTION_CE_LOCKED);
    }

    @Override
    public void setSectionCeLocked(Boolean sectionCeLocked) {
        put(TexteMaitreConstants.SECTION_CE_LOCKED, sectionCeLocked);
    }

    @Override
    public Boolean getTitreOfficielLocked() {
      return getBoolean("titreOfficielLocked");
    }

    @Override
    public void setTitreOfficielLocked(Boolean titreOfficielLocked) {
        put("titreOfficielLocked", titreOfficielLocked);
    }

    @Override
    public Boolean getTypeActeLocked() {
        return getBoolean(TexteMaitreConstants.TYPE_ACTE_LOCKED);
    }

    @Override
    public void setTypeActeLocked(Boolean typeActeLocked) {
        put(TexteMaitreConstants.TYPE_ACTE_LOCKED, typeActeLocked);
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
    public Boolean getValidate() {
        return getBoolean(VALIDATE);
    }

    @Override
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
