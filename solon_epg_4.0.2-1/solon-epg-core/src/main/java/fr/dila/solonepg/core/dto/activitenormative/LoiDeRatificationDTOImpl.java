package fr.dila.solonepg.core.dto.activitenormative;

import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.LoiDeRatification;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.dto.LoiDeRatificationDTO;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.core.util.DateUtil;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.util.Calendar;
import java.util.Date;
import org.nuxeo.ecm.core.api.DocumentModel;

public class LoiDeRatificationDTOImpl extends AbstractMapTexteMaitreTableDTO implements LoiDeRatificationDTO {
    private static final long serialVersionUID = 78416213510924898L;

    public LoiDeRatificationDTOImpl(LoiDeRatification loiDeRatification) {
        this();
        if (loiDeRatification == null) {
            return;
        }
        setFromLoiDeRatification(loiDeRatification);
    }

    public void setFromLoiDeRatification(LoiDeRatification loiDeRatification) {
        setId(loiDeRatification.getId());
        setValidation(loiDeRatification.hasValidation());
        setChambreDepot(loiDeRatification.getChambreDepot());
        setChambreDepotLocked(loiDeRatification.isChambreDepotLocked());
        setDateDepot(DateUtil.toDate(loiDeRatification.getDateDepot()));
        setDateDepotLocked(loiDeRatification.isDateDepotLocked());
        setDateExamenCE(DateUtil.toDate(loiDeRatification.getDateExamenCE()));
        setDateExamenCELocked(loiDeRatification.isDateExamenCELocked());
        setDateExamenCM(DateUtil.toDate(loiDeRatification.getDateExamenCM()));
        setDateLimiteDepot(DateUtil.toDate(loiDeRatification.getDateLimiteDepot()));
        setDateLimitePublication(DateUtil.toDate(loiDeRatification.getDateLimitePublication()));
        setDatePublication(DateUtil.toDate(loiDeRatification.getDatePublication()));
        setDatePublicationLocked(loiDeRatification.isDatePublicationLocked());
        setDateSaisineCE(DateUtil.toDate(loiDeRatification.getDateSaisineCE()));
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
        setDatePublication(DateUtil.toDate(retourDila.getDateParutionJorf()));
        setDateSaisineCE(DateUtil.toDate(conseilEtat.getDateSaisineCE()));
        setDateExamenCE(DateUtil.toDate(conseilEtat.getDateSectionCe()));
        setNumeroNor(dossier.getNumeroNor());
        setRenvoiDecret(Boolean.FALSE);
        setSectionCE(conseilEtat.getSectionCe());
        setTitreActe(dossier.getTitreActe());
        setTitreOfficiel(retourDila.getTitreOfficiel());
    }

    public LoiDeRatificationDTOImpl(LoiDeRatification loiDeRatification, Dossier dossier, DocumentModel doc) {
        this();
        if (dossier == null) {
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
            } else {
                setChambreDepot(null);
            }
        }

        if (loiDeRatification.isDateDepotLocked()) {
            setDateDepot(DateUtil.toDate(loiDeRatification.getDateDepot()));
        } else {
            if (doc != null && "FicheLoi".equals(doc.getType())) {
                Calendar cal = PropertyUtil.getCalendarProperty(doc, "fiche_loi", "dateDepot");
                if (cal != null) {
                    setDateDepot(cal.getTime());
                } else {
                    setDateDepot(null);
                }
            } else {
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
        } else {
            setDateExamenCM(null);
        }

        if (loiDeRatification.isDateSaisineCELocked()) {
            setDateSaisineCE(DateUtil.toDate(loiDeRatification.getDateSaisineCE()));
        } else {
            setDateSaisineCE(DateUtil.toDate(conseilEtat.getDateSaisineCE()));
        }

        if (loiDeRatification.isDateSaisineCELocked()) {
            setDateExamenCE(DateUtil.toDate(loiDeRatification.getDateExamenCE()));
        } else {
            setDateExamenCE(DateUtil.toDate(conseilEtat.getDateSectionCe()));
        }

        // correction redmin 7628
        if (loiDeRatification.isDateExamenCELocked()) {
            setDateSaisineCE(DateUtil.toDate(loiDeRatification.getDateExamenCE()));
        } else {
            setDateSaisineCE(DateUtil.toDate(conseilEtat.getDateSectionCe()));
        }

        if (loiDeRatification.isNumeroDepotLocked()) {
            setNumeroDepot(loiDeRatification.getNumeroDepot());
        } else {
            if (doc != null && "FicheLoi".equals(doc.getType())) {
                setNumeroDepot(PropertyUtil.getStringProperty(doc, "fiche_loi", "numeroDepot"));
            } else {
                setNumeroDepot(null);
            }
        }
        if (loiDeRatification.isDateExamenCELocked()) {
            setDateExamenCE(DateUtil.toDate(loiDeRatification.getDateExamenCE()));
        } else {
            setDateExamenCE(null);
        }
        setDateLimiteDepot(DateUtil.toDate(loiDeRatification.getDateLimiteDepot()));
        setDatePublication(DateUtil.toDate(retourDila.getDateParutionJorf()));
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
    public LoiDeRatification remapField(LoiDeRatification loiDeRatification) {
        loiDeRatification.setValidation(hasValidation());
        loiDeRatification.setChambreDepot(getChambreDepot());
        loiDeRatification.setChambreDepotLocked(getChambreDepotLocked());
        loiDeRatification.setDateDepot(DateUtil.toCalendar(getDateDepot()));
        loiDeRatification.setDateLimiteDepot(DateUtil.toCalendar(getDateLimiteDepot()));
        loiDeRatification.setDateDepotLocked(getDateDepotLocked());
        loiDeRatification.setDateExamenCE(DateUtil.toCalendar(getDateExamenCE()));
        loiDeRatification.setDateExamenCELocked(getDateExamenCELocked());
        loiDeRatification.setDateExamenCM(DateUtil.toCalendar(getDateExamenCM()));
        loiDeRatification.setDateLimitePublication(DateUtil.toCalendar(getDateLimitePublication()));
        loiDeRatification.setDatePublication(DateUtil.toCalendar(getDatePublication()));
        loiDeRatification.setDatePublicationLocked(getDatePublicationLocked());
        loiDeRatification.setDateSaisineCE(DateUtil.toCalendar(getDateSaisineCE()));
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
