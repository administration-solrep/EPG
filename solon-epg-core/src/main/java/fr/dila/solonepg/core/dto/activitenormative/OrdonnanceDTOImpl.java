package fr.dila.solonepg.core.dto.activitenormative;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.Ordonnance;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.dto.OrdonnanceDTO;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;

public class OrdonnanceDTOImpl extends AbstractMapDTO implements OrdonnanceDTO {
    private static final long serialVersionUID = 78416213510924898L;

    private static final STLogger LOGGER = STLogFactory.getLog(OrdonnanceDTOImpl.class);

    public OrdonnanceDTOImpl() {
        // private default constructor
    }

    public OrdonnanceDTOImpl(ActiviteNormative activiteNormative, CoreSession session) {
        Ordonnance ordonnance = activiteNormative.getDocument().getAdapter(Ordonnance.class);

        setNumeroNor(ordonnance.getNumeroNor());

        setId(ordonnance.getDocument().getId());

        setNumeroInterne(ordonnance.getNumeroInterne());
        setTitreActe(ordonnance.getTitreActe());
        setMinisterePilote(ordonnance.getMinisterePilote());
        setMinisterePiloteLabel(ordonnance.getMinisterePiloteLabel());
        setTitreOfficiel(ordonnance.getTitreOfficiel());
        setDatePublication(DateUtil.toDate(ordonnance.getDatePublication()));

        setObjet(ordonnance.getObjet());
        setMotCle(ordonnance.getMotCle());
        setCodification(ordonnance.getCodification());
        setObservation(ordonnance.getObservation());

        setLienJORFLegifrance(getLienLegifranceFromJORF(getNumeroNor()));

        setTitreActeLocked(ordonnance.isTitreActeLocked());
        setMinisterePiloteLocked(ordonnance.isMinistereRespLocked());
        setDatePublicationLocked(ordonnance.isDatePublicationLocked());
        setTitreOfficielLocked(ordonnance.isTitreOfficielLocked());
        setNumeroInterneLocked(ordonnance.isNumeroInterneLocked());

        setNumero(ordonnance.getNumero());
        setNumeroLocked(ordonnance.isNumeroLocked());

        setDecretIds(ordonnance.getDecretIds());
        setRenvoiDecret(ordonnance.isRenvoiDecret());
        setRatifie(ordonnance.isRatifie());

        setDecretIdsInvalidated(ordonnance.getDecretsIdsInvalidated());

        remapHabilitation(ordonnance, session);
    }

    private String getLienLegifranceFromJORF(String nor) {
        return SolonEpgServiceLocator.getActiviteNormativeService().createLienJORFLegifrance(nor);
    }

    private void remapHabilitation(TexteMaitre texteMaitre, CoreSession session) {
        if (texteMaitre.isDispositionHabilitation() == null || texteMaitre.isDispositionHabilitation()) {
            setDispositionHabilitation(Boolean.TRUE);
        } else {
            setDispositionHabilitation(Boolean.FALSE);
        }
    }

    @Override
    public DocumentModel remapField(TexteMaitre texteMaitre) {
        texteMaitre.setNumeroNor(getNumeroNor());

        texteMaitre.setMinisterePilote(getMinisterePilote());
        texteMaitre.setTitreActe(getTitreActe());
        texteMaitre.setNumero(getNumero());
        texteMaitre.setDispositionHabilitation(isDispositionHabilitation());
        texteMaitre.setTitreOfficiel(getTitreOfficiel());
        texteMaitre.setDatePublication(DateUtil.toCalendar(getDatePublication()));
        texteMaitre.setObjet(getObjet());
        texteMaitre.setMotCle(getMotCle());
        texteMaitre.setCodification(getCodification());
        texteMaitre.setObservation(getObservation());

        texteMaitre.setTitreActeLocked(getTitreActeLocked());
        texteMaitre.setMinistereRespLocked(isMinisterePiloteLocked());
        texteMaitre.setDatePublicationLocked(getDatePublicationLocked());
        texteMaitre.setNumeroInterne(getNumeroInterne());
        texteMaitre.setTitreOfficielLocked(getTitreOfficielLocked());
        texteMaitre.setNumeroLocked(isNumeroLocked());
        texteMaitre.setRenvoiDecret(getRenvoiDecret());
        texteMaitre.setRatifie(getRatifie());

        return texteMaitre.getDocument();
    }

    @Override
    public void refresh(Dossier dossier, CoreSession session) {
        if (!getTitreActeLocked()) {
            setTitreActe(dossier.getTitreActe());
        }

        if (!isMinisterePiloteLocked()) {
            setMinisterePilote(dossier.getMinistereResp());
            setMinisterePiloteLabel(
                STServiceLocator.getSTMinisteresService().getEntiteNode(dossier.getMinistereResp()).getNorMinistere()
            );
        }
        ActiviteNormative activiteNormative = SolonEpgServiceLocator
            .getActiviteNormativeService()
            .findActiviteNormativeByNor(getNumeroNor(), session);

        if (activiteNormative == null || activiteNormative.getDocument() == null) {
            LOGGER.error(
                session,
                STLogEnumImpl.NPE_PARAM_METH_TEC,
                "le texte maitre avec nor " + getNumeroNor() + " ne doit pas etre  null"
            );
            throw new NuxeoException("le texte maitre avec nor " + getNumeroNor() + " ne doit pas etre  null");
        }

        TexteMaitre texteMaitre = activiteNormative.getDocument().getAdapter(TexteMaitre.class);

        if (!isNumeroInterneLocked()) {
            setNumeroInterne(texteMaitre.getNumeroInterne());
        }

        RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);

        if (!getTitreOfficielLocked()) {
            setTitreOfficiel(retourDila.getTitreOfficiel());
        }

        if (!getDatePublicationLocked()) {
            setDatePublication(DateUtil.toDate(retourDila.getDateParutionJorf()));
        }

        if (!isNumeroLocked()) {
            setNumero(texteMaitre.getNumero());
        }

        setDecretIds(texteMaitre.getDecretIds());
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
    public String getDocIdForSelection() {
        return getId();
    }

    @Override
    public String getLienJORFLegifrance() {
        return getString(TexteMaitreConstants.LIEN_JORF_LEGIFRANCE);
    }

    @Override
    public void setLienJORFLegifrance(String lienJORFLegifrance) {
        put(TexteMaitreConstants.LIEN_JORF_LEGIFRANCE, lienJORFLegifrance);
    }

    /**
     *
     * @return true si Article 38
     */
    @Override
    public Boolean isDispositionHabilitation() {
        return getBoolean(TexteMaitreConstants.DISPOSITION_HABILITATION);
    }

    @Override
    public void setDispositionHabilitation(Boolean dispositionHabilitation) {
        put(TexteMaitreConstants.DISPOSITION_HABILITATION, dispositionHabilitation);
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
    public String getMinisterePilote() {
        return getString(TexteMaitreConstants.MINISTERE_PILOTE);
    }

    @Override
    public void setMinisterePilote(String ministerePilote) {
        put(TexteMaitreConstants.MINISTERE_PILOTE, ministerePilote);
    }

    public String getMinisterePiloteLabel() {
        return getString(TexteMaitreConstants.MINISTERE_PILOTE_LABEL);
    }

    public void setMinisterePiloteLabel(String ministerePilote) {
        put(TexteMaitreConstants.MINISTERE_PILOTE_LABEL, ministerePilote);
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
    public String getObjet() {
        return getString(TexteMaitreConstants.OBJET);
    }

    @Override
    public void setObjet(String objet) {
        put(TexteMaitreConstants.OBJET, objet);
    }

    @Override
    public String getMotCle() {
        return getString(TexteMaitreConstants.MOT_CLE);
    }

    @Override
    public void setMotCle(String motCle) {
        put(TexteMaitreConstants.MOT_CLE, motCle);
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
    public void setObservation(String observation) {
        put(TexteMaitreConstants.OBSERVATION, observation);
    }

    @Override
    public String getObservation() {
        return getString(TexteMaitreConstants.OBSERVATION);
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
    public Boolean isNumeroInterneLocked() {
        return getBoolean(TexteMaitreConstants.NUMERO_INTERNE_LOCKED);
    }

    @Override
    public void setNumeroInterneLocked(Boolean numeroInterneLocked) {
        put(TexteMaitreConstants.NUMERO_INTERNE_LOCKED, numeroInterneLocked);
    }

    @Override
    public void setDatePublicationLocked(Boolean datePublicationLocked) {
        put(TexteMaitreConstants.DATE_PUBLICATION_LOCKED, datePublicationLocked);
    }

    @Override
    public Boolean getDatePublicationLocked() {
        return getBoolean(TexteMaitreConstants.DATE_PUBLICATION_LOCKED);
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
    public String getTitreActe() {
        return getString(TexteMaitreConstants.TITRE_ACTE);
    }

    @Override
    public void setTitreActe(String titreActe) {
        put(TexteMaitreConstants.TITRE_ACTE, titreActe);
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
    public String getNumeroInterne() {
        return getString(TexteMaitreConstants.NUMERO_INTERNE);
    }

    @Override
    public void setNumeroInterne(String numeroInterne) {
        put(TexteMaitreConstants.NUMERO_INTERNE, numeroInterne);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getDecretIds() {
        return (List<String>) get(TexteMaitreConstants.DECRET_IDS);
    }

    @Override
    public void setDecretIds(List<String> decretIds) {
        put(TexteMaitreConstants.DECRET_IDS, (Serializable) decretIds);
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
    public String getNumero() {
        return getString(TexteMaitreConstants.NUMERO);
    }

    @Override
    public void setNumero(String numero) {
        put(TexteMaitreConstants.NUMERO, numero);
    }

    public Boolean getRenvoiDecret() {
        return getBoolean(TexteMaitreConstants.RENVOI_DECRET);
    }

    public void setRenvoiDecret(Boolean renvoiDecret) {
        put(TexteMaitreConstants.RENVOI_DECRET, renvoiDecret);
    }

    @Override
    public Boolean getRatifie() {
        return getBoolean(TexteMaitreConstants.RATIFIE);
    }

    @Override
    public void setRatifie(Boolean ratifie) {
        put(TexteMaitreConstants.RATIFIE, ratifie);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getDecretIdsInvalidated() {
        return (List<String>) get(TexteMaitreConstants.IDS_INVALIDATED);
    }

    @Override
    public void setDecretIdsInvalidated(List<String> decretIds) {
        put(TexteMaitreConstants.IDS_INVALIDATED, (Serializable) decretIds);
    }

    @Override
    public String getType() {
        return Ordonnance.class.getSimpleName();
    }
}
