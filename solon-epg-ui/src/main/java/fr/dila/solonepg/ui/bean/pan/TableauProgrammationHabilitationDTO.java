package fr.dila.solonepg.ui.bean.pan;

import fr.dila.solonepg.api.cases.ActiviteNormativeProgrammation;
import fr.dila.solonepg.api.cases.Habilitation;
import fr.dila.solonepg.api.cases.Ordonnance;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.cases.typescomplexe.LigneProgrammationHabilitation;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.core.cases.typescomplexe.LigneProgrammationHabilitationImpl;
import fr.dila.solonepg.core.dto.activitenormative.LigneProgrammationHabilitationDTO;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.core.service.STServiceLocator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoException;

public class TableauProgrammationHabilitationDTO extends TableauProgrammationOuSuiviDTO implements Serializable {
    private static final long serialVersionUID = 4171700236032022191L;

    @Override
    public void buildColonnes() {
        getListeColonnes().add(buildColonne("pan.application.table.mesures.header.label.ordre", null, null, true));
        getListeColonnes().add(buildColonne("pan.application.table.mesures.header.label.articleLoi", null, null, true));
        getListeColonnes().add(buildColonne("pan.application.table.mesures.header.label.objet", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.ministerePilote", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.termeHabilitation", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.dateTerme", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.conventionDepot", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.datePreviSaisineCE", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.datePreviCM", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.observations", null, null, true));

        if (!getIsTableauProgrammation()) { // tableau de suivi
            getListeColonnes().add(buildColonne("pan.habilitations.objetOrdo", null, null, true));
            getListeColonnes().add(buildColonne("pan.habilitations.legislature", null, null, true));
            getListeColonnes().add(buildColonne("pan.habilitations.numeroNor", null, null, true));
            getListeColonnes().add(buildColonne("pan.habilitations.dateSaisineCE", null, null, true));
            getListeColonnes().add(buildColonne("pan.habilitations.datePassageCM", null, null, true));
            getListeColonnes().add(buildColonne("pan.habilitations.datePublication", null, null, true));
            getListeColonnes().add(buildColonne("pan.habilitations.titre", null, null, true));
            getListeColonnes().add(buildColonne("pan.habilitations.numero", null, null, true));
            getListeColonnes().add(buildColonne("pan.habilitations.conventionDepot", null, null, true));
            getListeColonnes().add(buildColonne("pan.habilitations.dateLimiteDepot", null, null, true));
        }
    }

    @Override
    protected void setInfos(
        ActiviteNormativeProgrammation activiteNormativeProgrammation,
        boolean isTabProgrammation,
        String typTexte
    ) {
        setIsTableauProgrammation(isTabProgrammation);
        setTypeTexte(typTexte);
        if (isTabProgrammation) {
            if (activiteNormativeProgrammation.getLockHabDate() != null) {
                setLockDate(activiteNormativeProgrammation.getLockHabDate().getTime());
            }
            setLockUser(activiteNormativeProgrammation.getLockHabUser());
            setLocked(activiteNormativeProgrammation.getLockHabUser() != null);
        } else {
            if (activiteNormativeProgrammation.getTableauSuiviHabPublicationDate() != null) {
                setLockDate(activiteNormativeProgrammation.getTableauSuiviHabPublicationDate().getTime());
            }
            setLockUser(activiteNormativeProgrammation.getTableauSuiviHabPublicationUser());
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
     */
    public TableauProgrammationHabilitationDTO(
        ActiviteNormativeProgrammation activiteNormativeProgrammation,
        CoreSession session,
        Boolean isTabProgrammation,
        Boolean masquerApplique
    ) {
        setInfos(activiteNormativeProgrammation, isTabProgrammation, "habilitations");
        List<LigneProgrammationHabilitationDTO> liste = new ArrayList<>();
        HashMap<Integer, LigneProgrammationHabilitationDTO> listHabilitationMap = new HashMap<>();
        List<LigneProgrammationHabilitation> lignesProgrammations = new ArrayList<>();

        if (getIsTableauProgrammation()) {
            lignesProgrammations = activiteNormativeProgrammation.getLigneProgrammationHabilitation();
        }

        if (lignesProgrammations.isEmpty()) {
            setLocked(Boolean.FALSE);

            TexteMaitre texteMaitre = activiteNormativeProgrammation.getDocument().getAdapter(TexteMaitre.class);

            List<Habilitation> listHabilitation = SolonEpgServiceLocator
                .getActiviteNormativeService()
                .fetchListHabilitation(texteMaitre.getHabilitationIds(), session);
            int count = -1;

            for (Habilitation habilitation : listHabilitation) {
                if (!getIsTableauProgrammation()) {
                    // tableau de suivi

                    if (VocabularyConstants.TYPE_HABILITATION_ACTIVE.equals(habilitation.getTypeHabilitation())) {
                        List<Ordonnance> listOrdonnance = SolonEpgServiceLocator
                            .getActiviteNormativeService()
                            .fetchListOrdonnance(habilitation.getOrdonnanceIds(), session);

                        Boolean ordonnanceAffiche = Boolean.FALSE;

                        if (listOrdonnance != null && !listOrdonnance.isEmpty()) {
                            for (Ordonnance ordonnance : listOrdonnance) {
                                if (ordonnance.hasValidation()) {
                                    LigneProgrammationHabilitationDTO ligne = createLigne(habilitation);

                                    ligne.setNumeroNorOrdo(ordonnance.getNumeroNor());
                                    ligne.setObjetOrdo(ordonnance.getObjet());
                                    ligne.setMinisterePiloteOrdo(getNor(ordonnance.getMinisterePilote()));
                                    ligne.setLegislatureOrdo(ordonnance.getLegislature());
                                    ligne.setDateSaisineCEOrdo(
                                        ordonnance.getDateSaisineCE() != null
                                            ? ordonnance.getDateSaisineCE().getTime()
                                            : null
                                    );
                                    ligne.setDatePassageCMOrdo(
                                        ordonnance.getDatePassageCM() != null
                                            ? ordonnance.getDatePassageCM().getTime()
                                            : null
                                    );
                                    ligne.setDatePublicationOrdo(
                                        ordonnance.getDatePublication() != null
                                            ? ordonnance.getDatePublication().getTime()
                                            : null
                                    );
                                    ligne.setTitreOfficielOrdo(ordonnance.getTitreOfficiel());
                                    ligne.setNumeroOrdo(ordonnance.getNumero());
                                    ligne.setConventionDepotOrdo(ordonnance.getConventionDepot());
                                    ligne.setDateLimiteDepotOrdo(
                                        ordonnance.getDateLimiteDepot() != null
                                            ? ordonnance.getDateLimiteDepot().getTime()
                                            : null
                                    );

                                    listHabilitationMap.put(
                                        (
                                            ligne.getNumeroOrdre() != null && !ligne.getNumeroOrdre().equals("")
                                                ? Integer.valueOf(ligne.getNumeroOrdre())
                                                : count--
                                        ),
                                        ligne
                                    );
                                    ordonnanceAffiche = Boolean.TRUE;
                                }
                            }
                        }

                        if (listOrdonnance == null || listOrdonnance.isEmpty() || !ordonnanceAffiche) {
                            LigneProgrammationHabilitationDTO ligne = createLigne(habilitation);
                            listHabilitationMap.put(
                                (
                                    ligne.getNumeroOrdre() != null && !ligne.getNumeroOrdre().equals("")
                                        ? Integer.valueOf(ligne.getNumeroOrdre())
                                        : count--
                                ),
                                ligne
                            );
                        }
                    }
                } else {
                    LigneProgrammationHabilitationDTO ligne = createLigne(habilitation);
                    listHabilitationMap.put(
                        (
                            ligne.getNumeroOrdre() != null && !ligne.getNumeroOrdre().equals("")
                                ? Integer.valueOf(ligne.getNumeroOrdre())
                                : count--
                        ),
                        ligne
                    );
                }
            }
        } else {
            setLocked(Boolean.TRUE);
            int count = -1;
            for (LigneProgrammationHabilitation ligneProgrammation : lignesProgrammations) {
                listHabilitationMap.put(
                    (
                        ligneProgrammation.getNumeroOrdre() != null && !ligneProgrammation.getNumeroOrdre().equals("")
                            ? Integer.valueOf(ligneProgrammation.getNumeroOrdre())
                            : count--
                    ),
                    createLigne(ligneProgrammation)
                );
            }
        }
        // Trier par nb d'ordre
        SortedSet<Integer> keys = new TreeSet<>(listHabilitationMap.keySet());
        for (Integer key : keys) {
            liste.add(listHabilitationMap.get(key));
        }
        setListe(liste);
        setNbTotal(liste.size());
        buildColonnes();
    }

    private LigneProgrammationHabilitationDTO createLigne(LigneProgrammationHabilitation ligneProgrammation) {
        LigneProgrammationHabilitationDTO ligne = new LigneProgrammationHabilitationDTO();

        ligne.setArticle(ligneProgrammation.getArticle());
        ligne.setCodification(ligneProgrammation.getCodification());
        ligne.setConvention(ligneProgrammation.getConvention());
        ligne.setConventionDepot(ligneProgrammation.getConventionDepot());
        ligne.setDatePrevisionnelleCM(
            ligneProgrammation.getDatePrevisionnelleCM() != null
                ? ligneProgrammation.getDatePrevisionnelleCM().getTime()
                : null
        );
        ligne.setDatePrevisionnelleSaisineCE(
            ligneProgrammation.getDatePrevisionnelleSaisineCE() != null
                ? ligneProgrammation.getDatePrevisionnelleSaisineCE().getTime()
                : null
        );
        ligne.setDateTerme(
            ligneProgrammation.getDateTerme() != null ? ligneProgrammation.getDateTerme().getTime() : null
        );
        ligne.setLegislature(ligneProgrammation.getLegislature());
        ligne.setMinisterePilote(getNor(ligneProgrammation.getMinisterePilote()));
        ligne.setNumeroOrdre(ligneProgrammation.getNumeroOrdre());
        ligne.setObjetRIM(ligneProgrammation.getObjetRIM());
        ligne.setObservation(ligneProgrammation.getObservation());

        return ligne;
    }

    private LigneProgrammationHabilitationDTO createLigne(Habilitation habilitation) {
        LigneProgrammationHabilitationDTO ligne = new LigneProgrammationHabilitationDTO();

        ligne.setArticle(habilitation.getArticle());
        ligne.setCodification(habilitation.getCodification());
        ligne.setConvention(habilitation.getConvention());
        ligne.setConventionDepot(habilitation.getConventionDepot());
        ligne.setDatePrevisionnelleCM(
            habilitation.getDatePrevisionnelleCM() != null ? habilitation.getDatePrevisionnelleCM().getTime() : null
        );
        ligne.setDatePrevisionnelleSaisineCE(
            habilitation.getDatePrevisionnelleSaisineCE() != null
                ? habilitation.getDatePrevisionnelleSaisineCE().getTime()
                : null
        );
        ligne.setDateTerme(habilitation.getDateTerme() != null ? habilitation.getDateTerme().getTime() : null);
        ligne.setLegislature(habilitation.getLegislature());
        ligne.setMinisterePilote(getNor(habilitation.getMinisterePilote()));
        ligne.setNumeroOrdre(habilitation.getNumeroOrdre());
        ligne.setObjetRIM(habilitation.getObjetRIM());
        ligne.setObservation(habilitation.getObservation());

        return ligne;
    }

    private String getNor(String idMinistere) {
        EntiteNode node;
        try {
            node = STServiceLocator.getSTMinisteresService().getEntiteNode(idMinistere);
        } catch (NuxeoException e) {
            // node non trouvé
            node = null;
        }
        return node != null ? node.getNorMinistere() : idMinistere;
    }

    public List<LigneProgrammationHabilitation> remapField(List<LigneProgrammationHabilitation> list) {
        for (LigneProgrammationHabilitationDTO ligneProgrammationHabilitationDTO : (List<LigneProgrammationHabilitationDTO>) getListe()) {
            LigneProgrammationHabilitation ligneProgrammation = new LigneProgrammationHabilitationImpl();
            ligneProgrammation.setArticle(ligneProgrammationHabilitationDTO.getArticle());

            if (ligneProgrammationHabilitationDTO.getDatePrevisionnelleSaisineCE() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(ligneProgrammationHabilitationDTO.getDatePrevisionnelleSaisineCE());
                ligneProgrammation.setDatePrevisionnelleSaisineCE(cal);
            }
            if (ligneProgrammationHabilitationDTO.getDatePrevisionnelleCM() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(ligneProgrammationHabilitationDTO.getDatePrevisionnelleCM());
                ligneProgrammation.setDatePrevisionnelleCM(cal);
            }
            if (ligneProgrammationHabilitationDTO.getDateTerme() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(ligneProgrammationHabilitationDTO.getDateTerme());
                ligneProgrammation.setDateTerme(cal);
            }

            ligneProgrammation.setMinisterePilote(ligneProgrammationHabilitationDTO.getMinisterePilote());
            ligneProgrammation.setNumeroOrdre(ligneProgrammationHabilitationDTO.getNumeroOrdre());

            ligneProgrammation.setObjetRIM(ligneProgrammationHabilitationDTO.getObjetRIM());
            ligneProgrammation.setObservation(ligneProgrammationHabilitationDTO.getObservation());
            ligneProgrammation.setConvention(ligneProgrammationHabilitationDTO.getConvention());
            ligneProgrammation.setConventionDepot(ligneProgrammationHabilitationDTO.getConventionDepot());

            list.add(ligneProgrammation);
        }

        return list;
    }
}
