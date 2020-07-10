package fr.dila.solonepg.core.dto.activitenormative;

import java.util.Calendar;
import java.util.Date;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.LoiDeRatification;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.dto.LoiDeRatificationDTO;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.util.PropertyUtil;

public class LoiDeRatificationDTOImpl extends AbstractMapDTO implements LoiDeRatificationDTO {

    private static final long serialVersionUID = 78416213510924898L;

    public LoiDeRatificationDTOImpl(LoiDeRatification loiDeRatification) {
      this();
      if(loiDeRatification == null){
          return;
      }
      setFromLoiDeRatification(loiDeRatification);
    }
  
    public void setFromLoiDeRatification(LoiDeRatification loiDeRatification) {
        setId(loiDeRatification.getId());
        setValidation(loiDeRatification.hasValidation());
        setChambreDepot(loiDeRatification.getChambreDepot());
        setChambreDepotLocked(loiDeRatification.isChambreDepotLocked());
        setDateDepot(loiDeRatification.getDateDepot() != null ? loiDeRatification.getDateDepot().getTime() : null);
        setDateDepotLocked(loiDeRatification.isDateDepotLocked());
        setDateExamenCE(loiDeRatification.getDateExamenCE() != null ? loiDeRatification.getDateExamenCE().getTime() : null);
        setDateExamenCELocked(loiDeRatification.isDateExamenCELocked());
        setDateExamenCM(loiDeRatification.getDateExamenCM() != null ? loiDeRatification.getDateExamenCM().getTime() : null);
        setDateLimiteDepot(loiDeRatification.getDateLimiteDepot() != null ? loiDeRatification.getDateLimiteDepot().getTime() : null);
        setDateLimitePublication(loiDeRatification.getDateLimitePublication() != null ? loiDeRatification.getDateLimitePublication().getTime() : null);
        setDatePublication(loiDeRatification.getDatePublication() != null ? loiDeRatification.getDatePublication().getTime() : null);
        setDatePublicationLocked(loiDeRatification.isDatePublicationLocked());
        setDateSaisineCE(loiDeRatification.getDateSaisineCE() != null ? loiDeRatification.getDateSaisineCE().getTime() : null);
        setDateSaisineCELocked(loiDeRatification.isDateSaisineCELocked());
        setLienJORFLegifrance(getLienLegifranceFromJORF(loiDeRatification.getNumeroNor()));
        setNumeroDepot(loiDeRatification.getNumeroDepot());
        setNumeroDepotLocked(loiDeRatification.isNumeroDepotLocked());
        setNumeroNor(loiDeRatification.getNumeroNor());
        setRenvoiDecret(loiDeRatification.isRenvoiDecret());
        setSectionCE(loiDeRatification.getSectionCE());
        setSectionCELocked(loiDeRatification.isSectionCELocked());
        setTermeDepot(loiDeRatification.getTermeDepot());
        setTitreActe(loiDeRatification.getTitreActe());
        setTitreActeLocked(loiDeRatification.isTitreActeLocked());
        setTitreOfficiel(loiDeRatification.getTitreOfficiel());
        setTitreOfficielLocked(loiDeRatification.isTitreOfficielLocked());
    }

    public LoiDeRatificationDTOImpl() {
        // constructeur vide pour ligne vide
        setRenvoiDecret(Boolean.FALSE);
        setValidation(Boolean.FALSE);
    }

    public LoiDeRatificationDTOImpl(Dossier dossier) {
        this();

        if (dossier == null) {
            return;
        }

        RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
        ConseilEtat conseilEtat = dossier.getDocument().getAdapter(ConseilEtat.class);

        setId(null);
        setChambreDepot(null);
        setDateDepot(null);
        setDateExamenCE(null);
        setDateExamenCM(null);
        setDateLimiteDepot(null);
        setDatePublication(retourDila.getDateParutionJorf() != null ? retourDila.getDateParutionJorf().getTime() : null);
        setDateSaisineCE(conseilEtat.getDateSaisineCE() != null ? conseilEtat.getDateSaisineCE().getTime() : null);
        setDateExamenCE(conseilEtat.getDateSectionCe() != null ? conseilEtat.getDateSectionCe().getTime() : null);
        setLienJORFLegifrance(getLienLegifranceFromJORF(dossier.getNumeroNor()));
        setNumeroNor(dossier.getNumeroNor());
        setRenvoiDecret(Boolean.FALSE);
        setSectionCE(conseilEtat.getSectionCe());
        setTitreActe(dossier.getTitreActe());
        setTitreOfficiel(retourDila.getTitreOfficiel());
    }

    public LoiDeRatificationDTOImpl(LoiDeRatification loiDeRatification, Dossier dossier, DocumentModel doc) {
        this();

        if(dossier == null){
          setFromLoiDeRatification(loiDeRatification);
          return;
        }

        RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
        ConseilEtat conseilEtat = dossier.getDocument().getAdapter(ConseilEtat.class);

        setChambreDepotLocked(loiDeRatification.isChambreDepotLocked());
        setDateDepotLocked(loiDeRatification.isDateDepotLocked());
        setDateExamenCELocked(loiDeRatification.isDateExamenCELocked());
        setDatePublicationLocked(loiDeRatification.isDatePublicationLocked());
        setDateSaisineCELocked(loiDeRatification.isDateSaisineCELocked());
        setDateExamenCELocked(loiDeRatification.isDateExamenCELocked());
        setNumeroDepotLocked(loiDeRatification.isNumeroDepotLocked());
        setSectionCELocked(loiDeRatification.isSectionCELocked());
        setTitreActeLocked(loiDeRatification.isTitreActeLocked());
        setTitreOfficielLocked(loiDeRatification.isTitreOfficielLocked());
        setId(null);
        if (loiDeRatification.isChambreDepotLocked()) {
            setChambreDepot(loiDeRatification.getChambreDepot());
        } else {
            if (doc != null && "FicheLoi".equals(doc.getType())) {
                setChambreDepot(PropertyUtil.getStringProperty(doc, "fiche_loi", "assembleeDepot"));
            }else {
              setChambreDepot(null);
            }
        }

        if (loiDeRatification.isDateDepotLocked()) {
            setDateDepot(loiDeRatification.getDateDepot() != null ? loiDeRatification.getDateDepot().getTime() : null);
        } else {
            if (doc != null && "FicheLoi".equals(doc.getType())) {

                Calendar cal = PropertyUtil.getCalendarProperty(doc, "fiche_loi", "dateDepot");
                if (cal != null) {
                    setDateDepot(cal.getTime());
                } else {
                    setDateDepot(null);
                }
            }else{
              setDateDepot(null);
            }
        }

          if (doc != null && "FicheLoi".equals(doc.getType())) {

              Calendar calDateCM = PropertyUtil.getCalendarProperty(doc, "fiche_loi", "dateCM");
              if (calDateCM != null) {
                  setDateExamenCM(calDateCM.getTime());
              } else {
                  setDateExamenCM(null);
              }
          }else{
            setDateExamenCM(null);
          }

        if (loiDeRatification.isDateSaisineCELocked()) {
            setDateSaisineCE(loiDeRatification.getDateSaisineCE() != null ? loiDeRatification.getDateSaisineCE().getTime() : null);
        } else {
            setDateSaisineCE(conseilEtat.getDateSaisineCE() != null ? conseilEtat.getDateSaisineCE().getTime() : null);
        }
        
        if (loiDeRatification.isDateSaisineCELocked()) {
            setDateExamenCE(loiDeRatification.getDateExamenCE() != null ? loiDeRatification.getDateExamenCE().getTime() : null);
        } else {
            setDateExamenCE(conseilEtat.getDateSectionCe() != null ? conseilEtat.getDateSectionCe().getTime() : null);
        }

        
        // correction redmin 7628
        if (loiDeRatification.isDateExamenCELocked()) {
            setDateSaisineCE(loiDeRatification.getDateExamenCE() != null ? loiDeRatification.getDateExamenCE().getTime() : null);
        } else {
            setDateSaisineCE(conseilEtat.getDateSectionCe() != null ? conseilEtat.getDateSectionCe().getTime() : null);
        }
        
        
        if (loiDeRatification.isNumeroDepotLocked()) {
            setNumeroDepot(loiDeRatification.getNumeroDepot());
        } else {
            if (doc != null && "FicheLoi".equals(doc.getType())) {
                setNumeroDepot(PropertyUtil.getStringProperty(doc, "fiche_loi", "numeroDepot"));
            }else {
              setNumeroDepot(null);
            }
        }
        if (loiDeRatification.isDateExamenCELocked()) {
            setDateExamenCE(loiDeRatification.getDateExamenCE() != null ? loiDeRatification.getDateExamenCE().getTime() : null);
        } else {
            setDateExamenCE(null);
        }
        setDateLimiteDepot(loiDeRatification.getDateLimiteDepot() != null ? loiDeRatification.getDateLimiteDepot().getTime() : null);
        setDatePublication(retourDila.getDateParutionJorf() != null ? retourDila.getDateParutionJorf().getTime() : null);
        setLienJORFLegifrance(getLienLegifranceFromJORF(dossier.getNumeroNor()));

        setNumeroNor(dossier.getNumeroNor());

        setRenvoiDecret(Boolean.FALSE);

        if (loiDeRatification.isSectionCELocked()) {
            setSectionCE(loiDeRatification.getSectionCE());
        } else {
            setSectionCE(conseilEtat.getSectionCe());
        }

        if (loiDeRatification.isTitreActeLocked()) {
            setTitreActe(loiDeRatification.getTitreActe());
        } else {
            setTitreActe(dossier.getTitreActe());
        }

        if (loiDeRatification.isTitreOfficielLocked()) {
            setTitreOfficiel(loiDeRatification.getTitreOfficiel());
        } else {
            setTitreOfficiel(retourDila.getTitreOfficiel());
        }
    }

    @Override
    public LoiDeRatification remapField(LoiDeRatification loiDeRatification) throws ClientException {
        loiDeRatification.setValidation(hasValidation());
        loiDeRatification.setChambreDepot(getChambreDepot());
        loiDeRatification.setChambreDepotLocked(getChambreDepotLocked());

        if (getDateDepot() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDateDepot());
            loiDeRatification.setDateDepot(cal);
        } else {
            loiDeRatification.setDateDepot(null);
        }

        if (getDateLimiteDepot() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDateLimiteDepot());
            loiDeRatification.setDateLimiteDepot(cal);
        } else {
            loiDeRatification.setDateLimiteDepot(null);
        }

        loiDeRatification.setDateDepotLocked(getDateDepotLocked());

        if (getDateExamenCE() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDateExamenCE());
            loiDeRatification.setDateExamenCE(cal);
        } else {
            loiDeRatification.setDateExamenCE(null);
        }

        loiDeRatification.setDateExamenCELocked(getDateExamenCELocked());

        if (getDateExamenCM() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDateExamenCM());
            loiDeRatification.setDateExamenCM(cal);
        } else {
            loiDeRatification.setDateExamenCM(null);
        }

        if (getDateLimitePublication() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDateLimitePublication());
            loiDeRatification.setDateLimitePublication(cal);
        } else {
            loiDeRatification.setDateLimitePublication(null);
        }

        if (getDatePublication() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDatePublication());
            loiDeRatification.setDatePublication(cal);
        } else {
            loiDeRatification.setDatePublication(null);
        }

        loiDeRatification.setDatePublicationLocked(getDatePublicationLocked());

        if (getDateSaisineCE() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDateSaisineCE());
            loiDeRatification.setDateSaisineCE(cal);
        } else {
            loiDeRatification.setDateSaisineCE(null);
        }
        loiDeRatification.setDateSaisineCELocked(getDateSaisineCELocked());

        loiDeRatification.setNumeroDepot(getNumeroDepot());
        loiDeRatification.setNumeroDepotLocked(getNumeroDepotLocked());
        loiDeRatification.setNumeroNor(getNumeroNor());
        loiDeRatification.setRenvoiDecret(getRenvoiDecret());
        loiDeRatification.setSectionCE(getSectionCE());
        loiDeRatification.setSectionCELocked(getSectionCELocked());
        loiDeRatification.setTermeDepot(getTermeDepot());
        loiDeRatification.setTitreActe(getTitreActe());
        loiDeRatification.setTitreActeLocked(getTitreActeLocked());
        loiDeRatification.setTitreOfficiel(getTitreOfficiel());
        loiDeRatification.setTitreOfficielLocked(getTitreOfficielLocked());

        return loiDeRatification;
    }

    private String getLienLegifranceFromJORF(String jorfLegifrance) {
        return SolonEpgServiceLocator.getActiviteNormativeService().createLienJORFLegifrance(jorfLegifrance);
    }

    @Override
    public String getType() {
        return "LoiRatification";
    }

    @Override
    public String getDocIdForSelection() {
        return getId();
    }

    @Override
    public String getId() {
        return getString(STSchemaConstant.ECM_UUID_PROPERTY);
    }

    @Override
    public void setId(String id) {
        put(STSchemaConstant.ECM_UUID_PROPERTY, id);
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
    public String getTermeDepot() {
        return getString(TexteMaitreConstants.TERME_DEPOT);
    }

    @Override
    public void setTermeDepot(String termeDepot) {
        put(TexteMaitreConstants.TERME_DEPOT, termeDepot);
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
    public Date getDateLimitePublication() {
        return getDate(TexteMaitreConstants.DATE_LIMITE_PUBLICATION);
    }

    @Override
    public void setDateLimitePublication(Date dateLimitePublication) {
        put(TexteMaitreConstants.DATE_LIMITE_PUBLICATION, dateLimitePublication);
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
    public String getTitreActe() {
        return getString(TexteMaitreConstants.TITRE_ACTE);
    }

    @Override
    public void setTitreActe(String titreActe) {
        put(TexteMaitreConstants.TITRE_ACTE, titreActe);
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
    public Date getDateExamenCE() {
        return getDate(TexteMaitreConstants.DATE_EXAMEN_CE);
    }

    @Override
    public void setDateExamenCE(Date dateExamenCE) {
        put(TexteMaitreConstants.DATE_EXAMEN_CE, dateExamenCE);
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
    public Date getDateExamenCM() {
        return getDate(TexteMaitreConstants.DATE_EXAMEN_CM);
    }

    @Override
    public void setDateExamenCM(Date dateExamenCM) {
        put(TexteMaitreConstants.DATE_EXAMEN_CM, dateExamenCM);
    }

    @Override
    public Boolean getRenvoiDecret() {
        return getBoolean(TexteMaitreConstants.RENVOI_DECRET);
    }

    @Override
    public void setRenvoiDecret(Boolean renvoiDecret) {
        put(TexteMaitreConstants.RENVOI_DECRET, renvoiDecret);
    }

    @Override
    public String getChambreDepot() {
        return getString(TexteMaitreConstants.CHAMBRE_DEPOT);
    }

    @Override
    public void setChambreDepot(String chambreDepot) {
        put(TexteMaitreConstants.CHAMBRE_DEPOT, chambreDepot);
    }

    @Override
    public Date getDateDepot() {
        return getDate(TexteMaitreConstants.DATE_DEPOT);
    }

    @Override
    public void setDateDepot(Date dateDepot) {
        put(TexteMaitreConstants.DATE_DEPOT, dateDepot);

    }

    @Override
    public String getNumeroDepot() {
        return getString(TexteMaitreConstants.NUMERO_DEPOT);
    }

    @Override
    public void setNumeroDepot(String numeroDepot) {
        put(TexteMaitreConstants.NUMERO_DEPOT, numeroDepot);
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
    public Boolean getTitreActeLocked() {
        return getBoolean(TexteMaitreConstants.TITRE_ACTE_LOCKED);
    }

    @Override
    public void setTitreActeLocked(Boolean titreActeLocked) {
        put(TexteMaitreConstants.TITRE_ACTE_LOCKED, titreActeLocked);
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
    public Boolean getDateExamenCELocked() {
        return getBoolean(TexteMaitreConstants.DATE_EXAMEN_CE_LOCKED);
    }

    @Override
    public void setDateExamenCELocked(Boolean dateExamenCELocked) {
        put(TexteMaitreConstants.DATE_EXAMEN_CE_LOCKED, dateExamenCELocked);
    }

    @Override
    public Boolean getSectionCELocked() {
        return getBoolean(TexteMaitreConstants.SECTION_CE_LOCKED);
    }

    @Override
    public void setSectionCELocked(Boolean sectionCELocked) {
        put(TexteMaitreConstants.SECTION_CE_LOCKED, sectionCELocked);
    }

    @Override
    public Boolean getTitreOfficielLocked() {
        return getBoolean(TexteMaitreConstants.TITRE_OFFICIEL_LOCKED);
    }

    @Override
    public void setTitreOfficielLocked(Boolean titreOfficielLocked) {
        put(TexteMaitreConstants.TITRE_OFFICIEL_LOCKED, titreOfficielLocked);
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
    public Boolean getChambreDepotLocked() {
        return getBoolean(TexteMaitreConstants.CHAMBRE_DEPOT_LOCKED);
    }

    @Override
    public void setChambreDepotLocked(Boolean chambreDepotLocked) {
        put(TexteMaitreConstants.CHAMBRE_DEPOT_LOCKED, chambreDepotLocked);
    }

    @Override
    public Boolean getDateDepotLocked() {
        return getBoolean(TexteMaitreConstants.DATE_DEPOT_LOCKED);
    }

    @Override
    public void setDateDepotLocked(Boolean dateDepotLocked) {
        put(TexteMaitreConstants.DATE_DEPOT_LOCKED, dateDepotLocked);
    }

    @Override
    public Boolean getNumeroDepotLocked() {
        return getBoolean(TexteMaitreConstants.NUMERO_DEPOT_LOCKED);
    }

    @Override
    public void setNumeroDepotLocked(Boolean numeroDepotLocked) {
        put(TexteMaitreConstants.NUMERO_DEPOT_LOCKED, numeroDepotLocked);
    }

    @Override
    public String getLienJORFLegifrance() {
        return getString(TexteMaitreConstants.LIEN_JORF_LEGIFRANCE);
    }

    @Override
    public void setLienJORFLegifrance(String lienJORFLegifrance) {
        put(TexteMaitreConstants.LIEN_JORF_LEGIFRANCE, lienJORFLegifrance);
    }

}
