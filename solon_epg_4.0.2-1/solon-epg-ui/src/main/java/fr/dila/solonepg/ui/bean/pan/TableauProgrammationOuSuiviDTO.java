package fr.dila.solonepg.ui.bean.pan;

import fr.dila.solonepg.api.cases.ActiviteNormativeProgrammation;
import fr.dila.solonepg.api.cases.Decret;
import fr.dila.solonepg.api.cases.LigneProgrammation;
import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.LigneProgrammationConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.core.dto.activitenormative.LigneProgrammationDTO;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.service.VocabularyServiceImpl;
import fr.dila.st.core.util.SolonDateConverter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

public class TableauProgrammationOuSuiviDTO extends AbstractPanUnsortedList implements Serializable {
    private static final long serialVersionUID = 4171700236032022191L;

    private boolean isTableauProgrammation = true;

    private String typeTexte = "Loi";

    private boolean locked;

    private String lockUser;

    private Date lockDate;

    public TableauProgrammationOuSuiviDTO() {
        super();
    }

    @Override
    public void buildColonnes() {
        getListeColonnes().add(buildColonne("pan.application.table.mesures.header.label.ordre", null, null, true));

        if (StringUtils.equals(currentSection, ActiviteNormativeEnum.APPLICATION_DES_LOIS.getId())) {
            getListeColonnes()
                .add(buildColonne("pan.application.table.mesures.header.label.articleLoi", null, null, true));
        } else {
            getListeColonnes()
                .add(
                    buildColonne(
                        "pan.application.ordonnances.table.mesures.header.label.articleOrdonnance",
                        null,
                        null,
                        true
                    )
                );
        }

        getListeColonnes().add(buildColonne("pan.application.table.mesures.header.label.baseLegale", null, null, true));
        if (StringUtils.equals(currentSection, ActiviteNormativeEnum.APPLICATION_DES_LOIS.getId())) {
            getListeColonnes().add(buildColonne("pan.application.table.suivi.header.label.objetRIM", null, null, true));
        } else {
            getListeColonnes().add(buildColonne("pan.application.table.suivi.header.label.objet", null, null, true));
        }
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.ministerePilote", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.directionResponsable", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.categorieTexte", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.consultationHorsCE", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.calendrierHorsCE", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.datePreviSaisineCE", null, null, true));

        if (isTableauProgrammation) { // tableau de programmation
            getListeColonnes()
                .add(buildColonne("pan.application.table.mesures.header.label.objectifPubli", null, null, true));
            getListeColonnes()
                .add(buildColonne("pan.application.table.mesures.header.label.observations", null, null, true));
        } else { // tableau de suivi
            getListeColonnes()
                .add(buildColonne("pan.application.lois.table.decrets.header.label.dateSaisineCE", null, null, true));
            getListeColonnes()
                .add(buildColonne("pan.application.table.suivi.header.label.dateSortieCE", null, null, true));
            getListeColonnes()
                .add(buildColonne("pan.application.table.mesures.header.label.objectifPubli", null, null, true));
            getListeColonnes()
                .add(buildColonne("pan.application.table.mesures.header.label.observations", null, null, true));
            getListeColonnes()
                .add(buildColonne("pan.application.table.mesures.header.label.typeMesure", null, null, true));
            getListeColonnes()
                .add(buildColonne("pan.application.table.suivi.header.label.norDecret", null, null, true));
            getListeColonnes()
                .add(buildColonne("pan.application.table.suivi.header.label.titreDecret", null, null, true));
            getListeColonnes()
                .add(buildColonne("pan.application.table.suivi.header.label.datePublicationDecret", null, null, true));
            getListeColonnes()
                .add(buildColonne("pan.application.table.mesures.header.label.dateApplication", null, null, true));
        }
    }

    protected void setInfos(
        ActiviteNormativeProgrammation activiteNormativeProgrammation,
        boolean isTabProgrammation,
        String typTexte
    ) {
        setIsTableauProgrammation(isTabProgrammation);
        setTypeTexte(typTexte);
        if (isTabProgrammation) {
            if (activiteNormativeProgrammation.getLockDate() != null) {
                setLockDate(activiteNormativeProgrammation.getLockDate().getTime());
            }
            setLockUser(activiteNormativeProgrammation.getLockUser());
            setLocked(activiteNormativeProgrammation.getLockUser() != null);
        } else {
            if (activiteNormativeProgrammation.getTableauSuiviPublicationDate() != null) {
                setLockDate(activiteNormativeProgrammation.getTableauSuiviPublicationDate().getTime());
            }
            setLockUser(activiteNormativeProgrammation.getTableauSuiviPublicationUser());
            setLocked(false);
        }
    }

    /**
     *
     * @param activiteNormativeProgrammation
     * @param session
     * @param isTabProgrammation
     *            true verifie si le tableau est en base (ie tableau de programmation), false pour tableau de suivi
     * @param masquerApplique
     *            true pour masquer les mesures appliquées
     * @param typTexte
     *            loi ou ordonnance pour le libellé des colonnes
     */
    public TableauProgrammationOuSuiviDTO(
        ActiviteNormativeProgrammation activiteNormativeProgrammation,
        CoreSession session,
        boolean isTabProgrammation,
        boolean masquerApplique,
        String typTexte,
        String section
    ) {
        currentSection = section;
        setInfos(activiteNormativeProgrammation, isTabProgrammation, typTexte);
        List<LigneProgrammationDTO> liste = new ArrayList<>();
        List<LigneProgrammation> lignesProgrammations = new ArrayList<>();

        if (isTableauProgrammation) {
            lignesProgrammations = activiteNormativeProgrammation.getLigneProgrammation(session);
        }

        if (CollectionUtils.isEmpty(lignesProgrammations)) {
            TexteMaitre texteMaitre = activiteNormativeProgrammation.getDocument().getAdapter(TexteMaitre.class);

            final ActiviteNormativeService actNormService = SolonEpgServiceLocator.getActiviteNormativeService();
            List<MesureApplicative> listMesure = actNormService.fetchMesure(texteMaitre.getMesuresIds(), session);

            for (MesureApplicative mesureApplicative : listMesure) {
                if (
                    !isTableauProgrammation &&
                    (
                        StringUtils.isBlank(mesureApplicative.getNumeroOrdre()) ||
                        "0".equalsIgnoreCase(mesureApplicative.getNumeroOrdre().trim())
                    )
                ) {
                    // Mantis #39252
                    continue;
                }

                if (!isTableauProgrammation && masquerApplique && mesureApplicative.getApplicationRecu()) {
                    continue;
                }

                List<Decret> listDecret = actNormService.fetchDecrets(mesureApplicative.getDecretIds(), session);

                boolean decretAffiche = false;
                if (CollectionUtils.isNotEmpty(listDecret)) {
                    for (Decret decret : listDecret) {
                        if (!isTableauProgrammation && !decret.hasValidation()) {
                            // Mantis #39252
                            continue;
                        }

                        LigneProgrammationDTO programmationDecret = new LigneProgrammationDTO(mesureApplicative);

                        programmationDecret.setDateSaisineCE(getTime(decret.getDateSaisineCE()));
                        programmationDecret.setCategorieTexte(
                            decret.getTypeActe() != null ? getTypeActe(decret.getTypeActe()) : null
                        );
                        programmationDecret.setDateSortieCE(getTime(decret.getDateSortieCE()));
                        programmationDecret.setDatePublicationDecret(getTime(decret.getDatePublication()));
                        programmationDecret.setNorDecret(decret.getNumeroNor());
                        programmationDecret.setTitreDecret(decret.getTitreOfficiel());

                        liste.add(programmationDecret);
                        decretAffiche = true;
                    }
                }
                if (CollectionUtils.isEmpty(listDecret) || !decretAffiche) {
                    LigneProgrammationDTO programmationDecret = new LigneProgrammationDTO(mesureApplicative);
                    if (!isTableauProgrammation) {
                        programmationDecret.setTypeTableau(LigneProgrammationConstants.TYPE_TABLEAU_VALUE_SUIVI);
                    }

                    liste.add(programmationDecret);
                }
            }
        } else {
            lignesProgrammations.stream().map(LigneProgrammationDTO::new).forEach(liste::add);
        }
        liste.sort(
            (ligneProgrammation1, ligneProgrammation2) -> {
                if (ligneProgrammation1.getNumeroOrdre() != null && ligneProgrammation2.getNumeroOrdre() != null) {
                    String str1 = String.format("%9s", ligneProgrammation1.getNumeroOrdre());
                    String str2 = String.format("%9s", ligneProgrammation2.getNumeroOrdre());
                    return str1.compareTo(str2);
                }
                return -1;
            }
        );

        setListe(liste);
        setNbTotal(liste.size());
        buildColonnes();
    }

    private Date getTime(Calendar calendar) {
        return calendar != null ? calendar.getTime() : null;
    }

    private String getTypeActe(String idTypeActe) {
        String label = STServiceLocator
            .getVocabularyService()
            .getEntryLabel(VocabularyConstants.TYPE_ACTE_VOCABULARY, idTypeActe);
        return label.replace(VocabularyServiceImpl.UNKNOWN_ENTRY, "");
    }

    public List<LigneProgrammation> remapField(CoreSession documentManager, DocumentModel activiteNormativeDoc) {
        List<LigneProgrammation> list = new ArrayList<>();
        ActiviteNormativeProgrammation actiNormProgrammation = activiteNormativeDoc.getAdapter(
            ActiviteNormativeProgrammation.class
        );
        actiNormProgrammation.emptyLigneProgrammation(
            documentManager,
            LigneProgrammationConstants.TYPE_TABLEAU_VALUE_PROGRAMMATION
        );

        for (LigneProgrammationDTO ligneProgrammationDTO : (List<LigneProgrammationDTO>) getListe()) {
            LigneProgrammation ligneProgrammation;
            DocumentModel doc = null;
            if (
                StringUtils.isNotEmpty(ligneProgrammationDTO.getId()) &&
                documentManager.exists(new IdRef(ligneProgrammationDTO.getId()))
            ) {
                doc = documentManager.getDocument(new IdRef(ligneProgrammationDTO.getId()));
            } else {
                String titreDoc = "LigneProgrammation-" + SolonDateConverter.DATE_DASH.formatNow();
                doc =
                    documentManager.createDocumentModel(
                        activiteNormativeDoc.getPathAsString(),
                        titreDoc,
                        LigneProgrammationConstants.LIGNE_PROGRAMMATION_DOC_TYPE
                    );
            }
            ligneProgrammation = doc.getAdapter(LigneProgrammation.class);
            ligneProgrammation.setArticleLoi(ligneProgrammationDTO.getArticle());
            ligneProgrammation.setBaseLegale(ligneProgrammationDTO.getBaseLegale());

            ligneProgrammation.setCalendrierConsultationObligatoireHCE(
                ligneProgrammationDTO.getCalendrierConsultationObligatoireHCE()
            );

            ligneProgrammation.setCategorieTexte(ligneProgrammationDTO.getCategorieTexte());

            if (ligneProgrammationDTO.getDateSaisineCE() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(ligneProgrammationDTO.getDateSaisineCE());
                ligneProgrammation.setDateSaisineCE(cal);
            }

            ligneProgrammation.setDirectionResponsable(ligneProgrammationDTO.getDirectionResponsable());
            ligneProgrammation.setMinisterePilote(ligneProgrammationDTO.getMinisterePilote());
            ligneProgrammation.setNumeroOrdre(ligneProgrammationDTO.getNumeroOrdre());

            if (ligneProgrammationDTO.getObjectifPublication() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(ligneProgrammationDTO.getObjectifPublication());
                ligneProgrammation.setObjectifPublication(cal);
            }

            if (ligneProgrammationDTO.getDatePrevisionnelleSaisineCE() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(ligneProgrammationDTO.getDatePrevisionnelleSaisineCE());
                ligneProgrammation.setDatePrevisionnelleSaisineCE(cal);
            }

            ligneProgrammation.setObjet(ligneProgrammationDTO.getObjet());
            ligneProgrammation.setObservation(ligneProgrammationDTO.getObservation());
            ligneProgrammation.setTypeMesureId(ligneProgrammationDTO.getTypeMesureId());
            ligneProgrammation.setConsultationObligatoireHCE(ligneProgrammationDTO.getConsultationObligatoireHCE());
            ligneProgrammation.setTypeTableau(LigneProgrammationConstants.TYPE_TABLEAU_VALUE_PROGRAMMATION);

            if (
                StringUtils.isNotEmpty(ligneProgrammationDTO.getId()) &&
                documentManager.exists(new IdRef(ligneProgrammationDTO.getId()))
            ) {
                documentManager.saveDocument(ligneProgrammation.getDocument());
            } else {
                documentManager.createDocument(ligneProgrammation.getDocument());
            }
            documentManager.save();
            list.add(ligneProgrammation);
        }

        return list;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean getIsTableauProgrammation() {
        return isTableauProgrammation;
    }

    public void setIsTableauProgrammation(Boolean isTableauProgrammation) {
        this.isTableauProgrammation = isTableauProgrammation;
    }

    public String getTypeTexte() {
        return typeTexte;
    }

    public void setTypeTexte(String typeTexte) {
        this.typeTexte = typeTexte;
    }

    public String getLockUser() {
        return lockUser;
    }

    public void setLockUser(String lockUser) {
        this.lockUser = lockUser;
    }

    public Date getLockDate() {
        return lockDate;
    }

    public void setLockDate(Date lockDate) {
        this.lockDate = lockDate;
    }
}
