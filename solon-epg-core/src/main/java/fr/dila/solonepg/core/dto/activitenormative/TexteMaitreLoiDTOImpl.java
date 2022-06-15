package fr.dila.solonepg.core.dto.activitenormative;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.dto.TexteMaitreLoiDTO;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import fr.sword.xsd.solon.epp.NatureLoi;
import java.util.Calendar;
import java.util.Date;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public class TexteMaitreLoiDTOImpl extends AbstractMapDTO implements TexteMaitreLoiDTO {
    private static final long serialVersionUID = 78416213510924898L;

    public TexteMaitreLoiDTOImpl() {}

    public TexteMaitreLoiDTOImpl(ActiviteNormative activiteNormative) {
        if (activiteNormative == null) {
            return;
        }
        setId(activiteNormative.getDocument().getId());

        TexteMaitre texteMaitre = activiteNormative.getDocument().getAdapter(TexteMaitre.class);

        setNumeroNor(texteMaitre.getNumeroNor());

        setTitreActeLocked(texteMaitre.isTitreActeLocked());
        setMinisterePiloteLocked(texteMaitre.isMinistereRespLocked());
        setNumeroInterneLocked(texteMaitre.isNumeroInterneLocked());
        setDateEntreeEnVigueurLocked(texteMaitre.isDateEntreeEnVigueurLocked());
        setDatePublicationLocked(texteMaitre.isDatePublicationLocked());
        setTitreOfficielLocked(texteMaitre.isTitreOfficielLocked());
        setLegislaturePublicationLocked(texteMaitre.isLegislaturePublicationLocked());
        setNatureTexteLocked(texteMaitre.isNatureTexteLocked());
        setProcedureVoteLocked(texteMaitre.isProcedureVoteLocked());
        setCommissionAssNationaleLocked(texteMaitre.isCommissionAssNationaleLocked());
        setCommissionSenatLocked(texteMaitre.isCommissionSenatLocked());
        setNumeroLocked(texteMaitre.isNumeroLocked());

        setTitreActe(texteMaitre.getTitreActe());
        setMinisterePilote(texteMaitre.getMinisterePilote());
        setMinisterePiloteLabel(texteMaitre.getMinisterePiloteLabel());

        setLienJORFLegifrance(getLienLegifranceFromJORF(getNumeroNor()));
        setNumeroInterne(texteMaitre.getNumeroInterne());
        setMotCle(texteMaitre.getMotCle());
        setDateEntreeEnVigueur(DateUtil.toDate(texteMaitre.getDateEntreeEnVigueur()));
        setObservation(texteMaitre.getObservation());
        setChampLibre(texteMaitre.getChampLibre());
        setApplicationDirecte(texteMaitre.isApplicationDirecte());
        setDatePublication(DateUtil.toDate(texteMaitre.getDatePublication()));
        setDatePromulgation(DateUtil.toDate(texteMaitre.getDatePromulgation()));
        setTitreOfficiel(texteMaitre.getTitreOfficiel());
        setLegislaturePublication(texteMaitre.getLegislaturePublication());
        setNatureTexte(texteMaitre.getNatureTexte());
        setNatureTexteLabel(texteMaitre.getNatureTexteLabel());
        setProcedureVote(texteMaitre.getProcedureVote());
        setProcedureVoteLabel(texteMaitre.getProcedureVoteLabel());
        setCommissionAssNationale(texteMaitre.getCommissionAssNationale());
        setCommissionSenat(texteMaitre.getCommissionSenat());
        setNumero(texteMaitre.getNumero());
        setDispositionHabilitation(
            texteMaitre.isDispositionHabilitation() == null ? Boolean.TRUE : texteMaitre.isDispositionHabilitation()
        );

        setDateCirculationCompteRendu(DateUtil.toDate(texteMaitre.getDateCirculationCompteRendu()));
        setDateReunionProgrammation(DateUtil.toDate(texteMaitre.getDateReunionProgrammation()));
    }

    /**
     * refresh du DTO en fonction des locks
     *
     * @param texteMaitre
     * @param dossier
     * @param ficheLoiDoc
     */
    public void refresh(Dossier dossier, DocumentModel ficheLoiDoc) {
        if (dossier != null) {
            if (!getTitreActeLocked()) {
                setTitreActe(dossier.getTitreActe());
            }

            if (!getMinisterePiloteLocked()) {
                setMinisterePilote(dossier.getMinistereResp());
                setMinisterePiloteLabel(
                    STServiceLocator
                        .getSTMinisteresService()
                        .getEntiteNode(dossier.getMinistereResp())
                        .getNorMinistere()
                );
            }

            RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);

            if (!getTitreOfficielLocked()) {
                setTitreOfficiel(retourDila.getTitreOfficiel());
            }

            if (!getDatePublicationLocked()) {
                Calendar cal = retourDila.getDateParutionJorf();
                if (cal != null) {
                    setDatePublication(cal.getTime());
                } else {
                    setDatePublication(null);
                }
            }

            if (ficheLoiDoc != null && "FicheLoi".equals(ficheLoiDoc.getType())) {
                if (!getProcedureVoteLocked()) {
                    Boolean isProcAcceleree =
                        (PropertyUtil.getCalendarProperty(ficheLoiDoc, "fiche_loi", "dateProcedureAcceleree") != null);
                    if (isProcAcceleree != null) {
                        if (isProcAcceleree) {
                            setProcedureVote(VocabularyConstants.PROC_VOTE_VOCABULARY_ACCELEREE_ID);
                        } else {
                            setProcedureVote(VocabularyConstants.PROC_VOTE_VOCABULARY_NORMAL_ID);
                        }
                        setProcedureVoteLabel(
                            SolonEpgServiceLocator
                                .getSolonEpgVocabularyService()
                                .getLabelFromId(
                                    VocabularyConstants.PROCEDURE_VOTE_DIRECTORY,
                                    getProcedureVote(),
                                    STVocabularyConstants.COLUMN_LABEL
                                )
                        );
                    }
                }
                if (!getNatureTexteLocked()) {
                    String natureLoiStr = PropertyUtil.getStringProperty(ficheLoiDoc, "fiche_loi", "natureLoi");
                    if (natureLoiStr != null) {
                        NatureLoi natureLoi = NatureLoi.fromValue(natureLoiStr);
                        if (NatureLoi.PROJET.equals(natureLoi)) {
                            setNatureTexte(VocabularyConstants.NATURE_VOCABULARY_PROJ_LOI_ID);
                        } else if (NatureLoi.PROPOSITION.equals(natureLoi)) {
                            setNatureTexte(VocabularyConstants.NATURE_VOCABULARY_PROP_LOI_ID);
                        }
                        setNatureTexteLabel(
                            SolonEpgServiceLocator
                                .getSolonEpgVocabularyService()
                                .getLabelFromId(
                                    VocabularyConstants.NATURE_TEXTE_DIRECTORY,
                                    getNatureTexte(),
                                    STVocabularyConstants.COLUMN_LABEL
                                )
                        );
                    }
                }
            }
        }

        Calendar cal = SolonEpgServiceLocator.getActiviteNormativeService().extractDateFromTitre(getTitreOfficiel());
        if (cal != null) {
            setDatePromulgation(cal.getTime());
        } else {
            setDatePromulgation(null);
        }
    }

    public DocumentModel remapField(TexteMaitre texteMaitre, CoreSession session) {
        texteMaitre.setTitreActe(getTitreActe());
        texteMaitre.setMinisterePilote(getMinisterePilote());

        ActiviteNormativeService activiteNormativeService = SolonEpgServiceLocator.getActiviteNormativeService();
        activiteNormativeService.checkNumeroInterne(texteMaitre, getNumeroInterne(), session);

        texteMaitre.setNumeroInterne(getNumeroInterne());
        texteMaitre.setNumero(getNumero());
        texteMaitre.setMotCle(getMotCle());
        texteMaitre.setDateEntreeEnVigueur(DateUtil.toCalendar(getDateEntreeEnVigueur()));
        texteMaitre.setObservation(getObservation());
        texteMaitre.setChampLibre(getChampLibre());
        texteMaitre.setApplicationDirecte(getApplicationDirecte());
        texteMaitre.setDatePublication(DateUtil.toCalendar(getDatePublication()));

        Calendar calPromulgation = SolonEpgServiceLocator
            .getActiviteNormativeService()
            .extractDateFromTitre(getTitreOfficiel());
        if (calPromulgation != null) {
            texteMaitre.setDatePromulgation(calPromulgation);
        } else {
            texteMaitre.setDatePromulgation(null);
        }

        texteMaitre.setDateReunionProgrammation(DateUtil.toCalendar(getDateReunionProgrammation()));
        texteMaitre.setDateCirculationCompteRendu(DateUtil.toCalendar(getDateCirculationCompteRendu()));
        texteMaitre.setTitreOfficiel(getTitreOfficiel());
        texteMaitre.setLegislaturePublication(getLegislaturePublication());
        texteMaitre.setNatureTexte(getNatureTexte());
        texteMaitre.setProcedureVote(getProcedureVote());
        texteMaitre.setCommissionAssNationale(getCommissionAssNationale());
        texteMaitre.setCommissionSenat(getCommissionSenat());

        Calendar cal = activiteNormativeService.extractDateFromTitre(texteMaitre.getTitreOfficiel());
        texteMaitre.setDatePromulgation(cal);

        texteMaitre.setTitreActeLocked(getTitreActeLocked());
        texteMaitre.setMinistereRespLocked(getMinisterePiloteLocked());
        texteMaitre.setNumeroInterneLocked(getNumeroInterneLocked());
        texteMaitre.setDateEntreeEnVigueurLocked(getDateEntreeEnVigueurLocked());
        texteMaitre.setDatePublicationLocked(getDatePublicationLocked());
        texteMaitre.setTitreOfficielLocked(getTitreOfficielLocked());
        texteMaitre.setLegislaturePublicationLocked(getLegislaturePublicationLocked());
        texteMaitre.setNatureTexteLocked(getNatureTexteLocked());
        texteMaitre.setProcedureVoteLocked(getProcedureVoteLocked());
        texteMaitre.setCommissionAssNationaleLocked(getCommissionAssNationaleLocked());
        texteMaitre.setCommissionSenatLocked(getCommissionSenatLocked());
        texteMaitre.setNumeroLocked(getNumeroLocked());

        texteMaitre.setDispositionHabilitation(getDispositionHabilitation());

        return texteMaitre.getDocument();
    }

    private String getLienLegifranceFromJORF(String jorfLegifrance) {
        return SolonEpgServiceLocator.getActiviteNormativeService().createLienJORFLegifrance(jorfLegifrance);
    }

    @Override
    public String getNumeroInterne() {
        return getString(TexteMaitreConstants.NUMERO_INTERNE);
    }

    @Override
    public void setNumeroInterne(String numeroInterne) {
        put(TexteMaitreConstants.NUMERO_INTERNE, numeroInterne);
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
    public Date getDateEntreeEnVigueur() {
        return getDate(TexteMaitreConstants.DATE_ENTREE_EN_VIGUEUR);
    }

    @Override
    public void setDateEntreeEnVigueur(Date dateEntreeEnVigueur) {
        put(TexteMaitreConstants.DATE_ENTREE_EN_VIGUEUR, dateEntreeEnVigueur);
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
    public String getChampLibre() {
        return getString(TexteMaitreConstants.CHAMP_LIBRE);
    }

    @Override
    public void setChampLibre(String champLibre) {
        put(TexteMaitreConstants.CHAMP_LIBRE, champLibre);
    }

    @Override
    public Boolean getApplicationDirecte() {
        return getBoolean(TexteMaitreConstants.APPLICATION_DIRECTE);
    }

    @Override
    public void setApplicationDirecte(Boolean applicationDirecte) {
        put(TexteMaitreConstants.APPLICATION_DIRECTE, applicationDirecte);
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
    public Date getDatePromulgation() {
        return getDate(TexteMaitreConstants.DATE_PROMULGATION);
    }

    @Override
    public void setDatePromulgation(Date datePromulgation) {
        put(TexteMaitreConstants.DATE_PROMULGATION, datePromulgation);
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
    public String getLegislaturePublication() {
        return getString(TexteMaitreConstants.LEGISLATURE_PUBLICATION);
    }

    @Override
    public void setLegislaturePublication(String legislaturePublication) {
        put(TexteMaitreConstants.LEGISLATURE_PUBLICATION, legislaturePublication);
    }

    @Override
    public String getNatureTexte() {
        return getString(TexteMaitreConstants.NATURE_TEXTE);
    }

    @Override
    public void setNatureTexte(String natureTexte) {
        put(TexteMaitreConstants.NATURE_TEXTE, natureTexte);
    }

    public String getNatureTexteLabel() {
        return getString(TexteMaitreConstants.NATURE_TEXTE_LABEL);
    }

    public void setNatureTexteLabel(String natureTexte) {
        put(TexteMaitreConstants.NATURE_TEXTE_LABEL, natureTexte);
    }

    @Override
    public String getProcedureVote() {
        return getString(TexteMaitreConstants.PROCEDURE_VOTE);
    }

    @Override
    public void setProcedureVote(String procedureVote) {
        put(TexteMaitreConstants.PROCEDURE_VOTE, procedureVote);
    }

    public String getProcedureVoteLabel() {
        return getString(TexteMaitreConstants.PROCEDURE_VOTE_LABEL);
    }

    public void setProcedureVoteLabel(String procedureVote) {
        put(TexteMaitreConstants.PROCEDURE_VOTE_LABEL, procedureVote);
    }

    @Override
    public String getCommissionAssNationale() {
        return getString(TexteMaitreConstants.COMMISSION_ASS_NATIONALE);
    }

    @Override
    public void setCommissionAssNationale(String commissionAssNationale) {
        put(TexteMaitreConstants.COMMISSION_ASS_NATIONALE, commissionAssNationale);
    }

    @Override
    public String getCommissionSenat() {
        return getString(TexteMaitreConstants.COMMISSION_SENAT);
    }

    @Override
    public void setCommissionSenat(String commissionSenat) {
        put(TexteMaitreConstants.COMMISSION_SENAT, commissionSenat);
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
    public void setLienJORFLegifrance(String lienJORFLegifrance) {
        put(TexteMaitreConstants.LIEN_JORF_LEGIFRANCE, lienJORFLegifrance);
    }

    @Override
    public String getLienJORFLegifrance() {
        return getString(TexteMaitreConstants.LIEN_JORF_LEGIFRANCE);
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
    public Boolean getMinisterePiloteLocked() {
        return getBoolean(TexteMaitreConstants.MINISTERE_PILOTE_LOCKED);
    }

    @Override
    public void setMinisterePiloteLocked(Boolean ministerePiloteLocked) {
        put(TexteMaitreConstants.MINISTERE_PILOTE_LOCKED, ministerePiloteLocked);
    }

    @Override
    public Boolean getNumeroInterneLocked() {
        return getBoolean(TexteMaitreConstants.NUMERO_INTERNE_LOCKED);
    }

    @Override
    public void setNumeroInterneLocked(Boolean numeroInterneLocked) {
        put(TexteMaitreConstants.NUMERO_INTERNE_LOCKED, numeroInterneLocked);
    }

    @Override
    public Boolean getDateEntreeEnVigueurLocked() {
        return getBoolean(TexteMaitreConstants.DATE_ENTREE_EN_VIGUEUR_LOCKED);
    }

    @Override
    public void setDateEntreeEnVigueurLocked(Boolean dateEntreeEnVigueurLocked) {
        put(TexteMaitreConstants.DATE_ENTREE_EN_VIGUEUR_LOCKED, dateEntreeEnVigueurLocked);
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
    public Boolean getTitreOfficielLocked() {
        return getBoolean(TexteMaitreConstants.TITRE_OFFICIEL_LOCKED);
    }

    @Override
    public void setTitreOfficielLocked(Boolean titreOfficielLocked) {
        put(TexteMaitreConstants.TITRE_OFFICIEL_LOCKED, titreOfficielLocked);
    }

    @Override
    public Boolean getLegislaturePublicationLocked() {
        return getBoolean(TexteMaitreConstants.LEGISLATURE_PUBLICATION_LOCKED);
    }

    @Override
    public void setLegislaturePublicationLocked(Boolean legislaturePublicationLocked) {
        put(TexteMaitreConstants.LEGISLATURE_PUBLICATION_LOCKED, legislaturePublicationLocked);
    }

    @Override
    public Boolean getNatureTexteLocked() {
        return getBoolean(TexteMaitreConstants.NATURE_TEXTE_LOCKED);
    }

    @Override
    public void setNatureTexteLocked(Boolean natureTexteLocked) {
        put(TexteMaitreConstants.NATURE_TEXTE_LOCKED, natureTexteLocked);
    }

    @Override
    public Boolean getProcedureVoteLocked() {
        return getBoolean(TexteMaitreConstants.PROCEDURE_VOTE_LOCKED);
    }

    @Override
    public void setProcedureVoteLocked(Boolean procedureVoteLocked) {
        put(TexteMaitreConstants.PROCEDURE_VOTE_LOCKED, procedureVoteLocked);
    }

    @Override
    public Boolean getCommissionAssNationaleLocked() {
        return getBoolean(TexteMaitreConstants.COMMISSION_ASS_NATIONALE_LOCKED);
    }

    @Override
    public void setCommissionAssNationaleLocked(Boolean commissionAssNationaleLocked) {
        put(TexteMaitreConstants.COMMISSION_ASS_NATIONALE_LOCKED, commissionAssNationaleLocked);
    }

    @Override
    public Boolean getCommissionSenatLocked() {
        return getBoolean(TexteMaitreConstants.COMMISSION_SENAT_LOCKED);
    }

    @Override
    public void setCommissionSenatLocked(Boolean commissionSenatLocked) {
        put(TexteMaitreConstants.COMMISSION_SENAT_LOCKED, commissionSenatLocked);
    }

    @Override
    public void setNumeroLocked(Boolean numeroLocked) {
        put(TexteMaitreConstants.NUMERO_LOCKED, numeroLocked);
    }

    @Override
    public Boolean getNumeroLocked() {
        return getBoolean(TexteMaitreConstants.NUMERO_LOCKED);
    }

    @Override
    public void setNumero(String numero) {
        put(TexteMaitreConstants.NUMERO, numero);
    }

    @Override
    public String getNumero() {
        return getString(TexteMaitreConstants.NUMERO);
    }

    @Override
    public void setDispositionHabilitation(Boolean dispositionHabilitation) {
        put(TexteMaitreConstants.DISPOSITION_HABILITATION, dispositionHabilitation);
    }

    @Override
    public Boolean getDispositionHabilitation() {
        return getBoolean(TexteMaitreConstants.DISPOSITION_HABILITATION);
    }

    @Override
    public Date getDateReunionProgrammation() {
        return getDate(TexteMaitreConstants.DATE_REUNION_PROGRAMMATION);
    }

    @Override
    public void setDateReunionProgrammation(Date dateReunionProgrammation) {
        put(TexteMaitreConstants.DATE_REUNION_PROGRAMMATION, dateReunionProgrammation);
    }

    @Override
    public Date getDateCirculationCompteRendu() {
        return getDate(TexteMaitreConstants.DATE_CIRCULATION_COMPTE_RENDU);
    }

    @Override
    public void setDateCirculationCompteRendu(Date dateCirculationCompteRendu) {
        put(TexteMaitreConstants.DATE_CIRCULATION_COMPTE_RENDU, dateCirculationCompteRendu);
    }

    @Override
    public Date getDateModification() {
        return getDate(TexteMaitreConstants.DATE_MODIFICATION);
    }

    @Override
    public void setDateModification(Date dateModification) {
        put(TexteMaitreConstants.DATE_MODIFICATION, dateModification);
    }

    @Override
    public Date getDateInjection() {
        return getDate(TexteMaitreConstants.DATE_INJECTION);
    }

    @Override
    public void setDateInjection(Date dateInjection) {
        put(TexteMaitreConstants.DATE_INJECTION, dateInjection);
    }

    @Override
    public String getType() {
        return "TexteMaitre";
    }

    @Override
    public String getDocIdForSelection() {
        return null;
    }
}
