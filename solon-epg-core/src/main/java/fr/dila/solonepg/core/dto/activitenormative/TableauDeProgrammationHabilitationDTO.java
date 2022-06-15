package fr.dila.solonepg.core.dto.activitenormative;

import fr.dila.solonepg.api.cases.ActiviteNormativeProgrammation;
import fr.dila.solonepg.api.cases.Habilitation;
import fr.dila.solonepg.api.cases.Ordonnance;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.cases.typescomplexe.LigneProgrammationHabilitation;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.core.cases.typescomplexe.LigneProgrammationHabilitationImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoException;

public class TableauDeProgrammationHabilitationDTO implements Serializable {
    private static final long serialVersionUID = 4171700236032022191L;

    private List<LigneProgrammationHabilitationDTO> listProgrammation;

    private Boolean locked;

    /**
     *
     * @param dossier
     * @param session
     * @param checkBDD
     *            true verifie si le tableau est en base (ie tableau de programmation), false pour tableau de suivi
     * @param masquerApplique
     *            true pour masquer les mesures appliquées
     */
    public TableauDeProgrammationHabilitationDTO(
        ActiviteNormativeProgrammation activiteNormativeProgrammation,
        CoreSession session,
        Boolean isTableauProgrammation,
        Boolean masquerApplique
    ) {
        listProgrammation = new ArrayList<>();
        HashMap<Integer, LigneProgrammationHabilitationDTO> listHabilitationMap = new HashMap<>();

        List<LigneProgrammationHabilitation> lignesProgrammations = new ArrayList<>();

        if (isTableauProgrammation) {
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
                if (!isTableauProgrammation) {
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
                                    ligne.setDateSaisineCEOrdo(DateUtil.toDate(ordonnance.getDateSaisineCE()));
                                    ligne.setDatePassageCMOrdo(DateUtil.toDate(ordonnance.getDatePassageCM()));
                                    ligne.setDatePublicationOrdo(DateUtil.toDate(ordonnance.getDatePublication()));
                                    ligne.setTitreOfficielOrdo(ordonnance.getTitreOfficiel());
                                    ligne.setNumeroOrdo(ordonnance.getNumero());
                                    ligne.setConventionDepotOrdo(ordonnance.getConventionDepot());
                                    ligne.setDateLimiteDepotOrdo(DateUtil.toDate(ordonnance.getDateLimiteDepot()));

                                    listHabilitationMap.put(
                                        (
                                            StringUtils.isNotBlank(ligne.getNumeroOrdre())
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
                                    StringUtils.isNotBlank(ligne.getNumeroOrdre())
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
                            StringUtils.isNotBlank(ligne.getNumeroOrdre())
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
                        StringUtils.isNotBlank(ligneProgrammation.getNumeroOrdre())
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
            listProgrammation.add(listHabilitationMap.get(key));
        }
    }

    private LigneProgrammationHabilitationDTO createLigne(LigneProgrammationHabilitation ligneProgrammation) {
        LigneProgrammationHabilitationDTO ligne = new LigneProgrammationHabilitationDTO();

        ligne.setArticle(ligneProgrammation.getArticle());
        ligne.setCodification(ligneProgrammation.getCodification());
        ligne.setConvention(ligneProgrammation.getConvention());
        ligne.setConventionDepot(ligneProgrammation.getConventionDepot());
        ligne.setDatePrevisionnelleCM(DateUtil.toDate(ligneProgrammation.getDatePrevisionnelleCM()));
        ligne.setDatePrevisionnelleSaisineCE(DateUtil.toDate(ligneProgrammation.getDatePrevisionnelleSaisineCE()));
        ligne.setDateTerme(DateUtil.toDate(ligneProgrammation.getDateTerme()));
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
        ligne.setDatePrevisionnelleCM(DateUtil.toDate(habilitation.getDatePrevisionnelleCM()));
        ligne.setDatePrevisionnelleSaisineCE(DateUtil.toDate(habilitation.getDatePrevisionnelleSaisineCE()));
        ligne.setDateTerme(DateUtil.toDate(habilitation.getDateTerme()));
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

    public void setListProgrammation(List<LigneProgrammationHabilitationDTO> listProgrammation) {
        this.listProgrammation = listProgrammation;
    }

    public List<LigneProgrammationHabilitationDTO> getListProgrammation() {
        return listProgrammation;
    }

    public List<LigneProgrammationHabilitation> remapField(List<LigneProgrammationHabilitation> list) {
        for (LigneProgrammationHabilitationDTO ligneProgrammationHabilitationDTO : getListProgrammation()) {
            LigneProgrammationHabilitation ligneProgrammation = new LigneProgrammationHabilitationImpl();
            ligneProgrammation.setArticle(ligneProgrammationHabilitationDTO.getArticle());

            ligneProgrammation.setDatePrevisionnelleSaisineCE(
                DateUtil.toCalendarFromNotNullDate(ligneProgrammationHabilitationDTO.getDatePrevisionnelleSaisineCE())
            );
            ligneProgrammation.setDatePrevisionnelleCM(
                DateUtil.toCalendar(ligneProgrammationHabilitationDTO.getDatePrevisionnelleCM())
            );
            ligneProgrammation.setDateTerme(
                DateUtil.toCalendarFromNotNullDate(ligneProgrammationHabilitationDTO.getDateTerme())
            );

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

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean isLocked() {
        return locked;
    }
}
