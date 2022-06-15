package fr.dila.solonepg.ui.services.mgpp.impl;

import static fr.dila.st.ui.enums.WidgetTypeEnum.INPUT_TEXT;
import static fr.dila.st.ui.enums.WidgetTypeEnum.MULTIPLE_INPUT_TEXT;
import static fr.dila.st.ui.enums.WidgetTypeEnum.RADIO;
import static fr.dila.st.ui.enums.WidgetTypeEnum.SELECT;

import com.google.common.collect.Lists;
import fr.dila.solonepg.ui.bean.MgppRechercheDynamique;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.enums.mgpp.MgppSelectValueType;
import fr.dila.solonepg.ui.enums.mgpp.MgppUIConstant;
import fr.dila.solonepg.ui.helper.mgpp.MgppEditWidgetFactory;
import fr.dila.solonepg.ui.services.mgpp.MgppRechercheUIService;
import fr.dila.solonepg.ui.th.bean.MessageListForm;
import fr.dila.solonmgpp.api.descriptor.EvenementTypeDescriptor;
import fr.dila.solonmgpp.api.dto.CritereRechercheDTO;
import fr.dila.solonmgpp.api.service.MetaDonneesService;
import fr.dila.solonmgpp.core.dto.CritereRechercheDTOImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.core.requete.recherchechamp.Parametre;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.bean.WidgetDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.enums.WidgetTypeEnum;
import fr.dila.st.ui.enums.parlement.CommunicationMetadonneeEnum;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

public class MgppRechercheUIServiceImpl implements MgppRechercheUIService {
    private static final String LABEL_MGPP_EVENEMENT = "label.mgpp.evenement.";
    private static final String LST_NIVEAU_LECTURE = "lstNiveauLecture";
    private static final String SUFFIXE_DEBUT = "Debut";
    private static final String SUFFIXE_FIN = "Fin";

    private static final List<CommunicationMetadonneeEnum> METADONNEES_DISTRIBUTION = Lists.newArrayList(
        CommunicationMetadonneeEnum.EMETTEUR,
        CommunicationMetadonneeEnum.DESTINATAIRE,
        CommunicationMetadonneeEnum.DESTINATAIRE_COPIE,
        CommunicationMetadonneeEnum.HORODATAGE,
        CommunicationMetadonneeEnum.DATE_AR,
        CommunicationMetadonneeEnum.DATE_DEMANDE,
        CommunicationMetadonneeEnum.NIVEAU_LECTURE
    );
    private static final List<CommunicationMetadonneeEnum> METADONNEES_PUBLICATION = Lists.newArrayList(
        CommunicationMetadonneeEnum.NUMERO_JO,
        CommunicationMetadonneeEnum.PAGE_JO,
        CommunicationMetadonneeEnum.ANNEE_JO,
        CommunicationMetadonneeEnum.DATE_JO,
        CommunicationMetadonneeEnum.DATE_PROMULGATION,
        CommunicationMetadonneeEnum.DATE_PUBLICATION,
        CommunicationMetadonneeEnum.NUMERO_LOI
    );
    private static final List<CommunicationMetadonneeEnum> METADONNEES_AUTRES = Lists.newArrayList(
        CommunicationMetadonneeEnum.ABSTENTION,
        CommunicationMetadonneeEnum.ANNEE_RAPPORT,
        CommunicationMetadonneeEnum.AUTEUR,
        CommunicationMetadonneeEnum.BASE_LEGALE,
        CommunicationMetadonneeEnum.BULLETIN_BLANC,
        CommunicationMetadonneeEnum.COAUTEUR,
        CommunicationMetadonneeEnum.DESCRIPTION,
        CommunicationMetadonneeEnum.COMMISSIONS,
        CommunicationMetadonneeEnum.COMMISSION_SAISIE,
        CommunicationMetadonneeEnum.COMMISSION_SAISIE_AU_FOND,
        CommunicationMetadonneeEnum.COMMISSION_SAISIE_POUR_AVIS,
        CommunicationMetadonneeEnum.COSIGNATAIRE,
        CommunicationMetadonneeEnum.ORGANISME,
        CommunicationMetadonneeEnum.GROUPE_PARLEMENTAIRE,
        CommunicationMetadonneeEnum.DEMANDE_VOTE,
        CommunicationMetadonneeEnum.DATE_PRESENTATION,
        CommunicationMetadonneeEnum.DATE_LETTRE_PM,
        CommunicationMetadonneeEnum.DATE_VOTE,
        CommunicationMetadonneeEnum.DATE_DEMANDE,
        CommunicationMetadonneeEnum.DATE_DECLARATION,
        CommunicationMetadonneeEnum.DATE_AUDITION,
        CommunicationMetadonneeEnum.DATE,
        CommunicationMetadonneeEnum.DATE_ACTE,
        CommunicationMetadonneeEnum.DATE_ADOPTION,
        CommunicationMetadonneeEnum.DATE_CONVOCATION,
        CommunicationMetadonneeEnum.DATE_CADUCITE,
        CommunicationMetadonneeEnum.DATE_DEPOT_RAPPORT,
        CommunicationMetadonneeEnum.DATE_DEPOT_TEXTE,
        CommunicationMetadonneeEnum.DATE_DISTRIBUTION_ELECTRONIQUE,
        CommunicationMetadonneeEnum.DATE_ENGAGEMENT_PROCEDURE,
        CommunicationMetadonneeEnum.DATE_REFUS_ENGAGEMENT_PROCEDURE,
        CommunicationMetadonneeEnum.DATE_RETRAIT,
        CommunicationMetadonneeEnum.DATE_REFUS,
        CommunicationMetadonneeEnum.DATE_CMP,
        CommunicationMetadonneeEnum.DATE_CONGRES,
        CommunicationMetadonneeEnum.DATE_DESIGNATION,
        CommunicationMetadonneeEnum.DOSSIER_CIBLE,
        CommunicationMetadonneeEnum.DOSSIER_LEGISLATIF,
        CommunicationMetadonneeEnum.ECHEANCE,
        CommunicationMetadonneeEnum.IDENTIFIANT_METIER,
        CommunicationMetadonneeEnum.INTITULE,
        CommunicationMetadonneeEnum.LIBELLE_ANNEXE,
        CommunicationMetadonneeEnum.MOTIF_IRRECEVABILITE,
        CommunicationMetadonneeEnum.NATURE_LOI,
        CommunicationMetadonneeEnum.NATURE_RAPPORT,
        CommunicationMetadonneeEnum.SUFFRAGE_EXPRIME,
        CommunicationMetadonneeEnum.NOR,
        CommunicationMetadonneeEnum.NOR_LOI,
        CommunicationMetadonneeEnum.NUMERO_DEPOT_RAPPORT,
        CommunicationMetadonneeEnum.NUMERO_DEPOT_TEXTE,
        CommunicationMetadonneeEnum.NUMERO_RUBRIQUE,
        CommunicationMetadonneeEnum.NUMERO_TEXTE_ADOPTE,
        CommunicationMetadonneeEnum.OBJET,
        CommunicationMetadonneeEnum.PARLEMENTAIRE_SUPPLEANT_LIST,
        CommunicationMetadonneeEnum.PARLEMENTAIRE_TITULAIRE_LIST,
        CommunicationMetadonneeEnum.POSITION_ALERTE,
        CommunicationMetadonneeEnum.RAPPORT_PARLEMENT,
        CommunicationMetadonneeEnum.RAPPORTEUR_LIST,
        CommunicationMetadonneeEnum.RECTIFICATIF,
        CommunicationMetadonneeEnum.REDEPOT,
        CommunicationMetadonneeEnum.RESULTAT_CMP,
        CommunicationMetadonneeEnum.SENS_AVIS,
        CommunicationMetadonneeEnum.SORT_ADOPTION,
        CommunicationMetadonneeEnum.TITRE,
        CommunicationMetadonneeEnum.TYPE_ACTE,
        CommunicationMetadonneeEnum.TYPE_LOI,
        CommunicationMetadonneeEnum.URL_BASE_LEGALE,
        CommunicationMetadonneeEnum.URL_DOSSIER_AN,
        CommunicationMetadonneeEnum.URL_DOSSIER_SENAT,
        CommunicationMetadonneeEnum.URL_PUBLICATION,
        CommunicationMetadonneeEnum.VOTE_CONTRE,
        CommunicationMetadonneeEnum.VOTE_POUR,
        CommunicationMetadonneeEnum.PERSONNE,
        CommunicationMetadonneeEnum.FONCTION,
        CommunicationMetadonneeEnum.DATE_REFUS_ASSEMBLEE_1,
        CommunicationMetadonneeEnum.DATE_CONFERENCE_ASSEMBLEE_2,
        CommunicationMetadonneeEnum.DECISION_PROC_ACC
    );

    @Override
    public MgppRechercheDynamique getCriteresRechercheDynamique(SpecificContext context) {
        MgppRechercheDynamique criteres = new MgppRechercheDynamique();
        criteres.setMetadonneesTechniques(getMetadonneesTechniquesWidgets(context));
        criteres.setMetadonneesDistribution(getFilteredMetadonneesWidgets(context, METADONNEES_DISTRIBUTION));
        criteres.setMetadonneesPublication(getFilteredMetadonneesWidgets(context, METADONNEES_PUBLICATION));
        criteres.setAutresMetadonnees(getFilteredMetadonneesWidgets(context, METADONNEES_AUTRES));
        criteres.setPiecesJointes(getPiecesJointesWidgets(context));
        return criteres;
    }

    private static List<WidgetDTO> getMetadonneesTechniquesWidgets(SpecificContext context) {
        List<WidgetDTO> widgets = new ArrayList<>();
        String procedure = context.getFromContextData(STContextDataKey.ID);
        List<SelectValueDTO> eventTypeList = SolonMgppServiceLocator
            .getEvenementTypeService()
            .findEvenementTypeByProcedure(procedure)
            .stream()
            .map(evtType -> new SelectValueDTO(evtType.getName(), evtType.getLabel()))
            .sorted(Comparator.comparing(SelectValueDTO::getId))
            .collect(Collectors.toList());
        widgets.add(
            buildWidgetDTO(context, CritereRechercheDTOImpl.TYPE_EVENEMENT, MULTIPLE_INPUT_TEXT, eventTypeList)
        );
        widgets.add(buildWidgetDTO(context, CritereRechercheDTOImpl.ID_EVENEMENT, INPUT_TEXT));
        widgets.add(buildWidgetDTO(context, CritereRechercheDTOImpl.ID_EVENEMENT_PRECEDENT, INPUT_TEXT));
        widgets.add(buildWidgetDTO(context, CritereRechercheDTOImpl.ID_DOSSIER, INPUT_TEXT));

        return widgets;
    }

    private static List<WidgetDTO> getFilteredMetadonneesWidgets(
        SpecificContext context,
        List<CommunicationMetadonneeEnum> metadonnees
    ) {
        return metadonnees
            .stream()
            .filter(meta -> isMetadonneeVisibleInProcedure(context, meta.getName()))
            .map(meta -> buildWidgetDTO(context, meta))
            .collect(Collectors.toList());
    }

    private static List<WidgetDTO> getPiecesJointesWidgets(SpecificContext context) {
        List<WidgetDTO> widgets = new ArrayList<>();
        widgets.add(buildWidgetDTO(context, CritereRechercheDTOImpl.PIECE_JOINTE_LABEL, INPUT_TEXT));
        widgets.add(buildWidgetDTO(context, CritereRechercheDTOImpl.PIECE_JOINTE_FULL_TEXT, INPUT_TEXT));

        return widgets;
    }

    private static boolean isMetadonneeVisibleInProcedure(SpecificContext context, String metadonnee) {
        String procedure = context.getFromContextData(STContextDataKey.ID);
        if (procedure != null) {
            MetaDonneesService metaDonneesService = SolonMgppServiceLocator.getMetaDonneesService();
            return SolonMgppServiceLocator
                .getEvenementTypeService()
                .findEvenementTypeByProcedure(procedure)
                .stream()
                .map(evtType -> metaDonneesService.getMapProperty(evtType.getName()))
                .filter(map -> map.containsKey(metadonnee))
                .map(map -> map.get(metadonnee))
                .findFirst()
                .map(property -> property != null && !property.isVisibility())
                .orElse(false);
        }
        return false;
    }

    private static WidgetDTO buildWidgetDTO(SpecificContext context, String name, WidgetTypeEnum typeChamp) {
        return buildWidgetDTO(context, name, typeChamp, null);
    }

    private static WidgetDTO buildWidgetDTO(SpecificContext context, CommunicationMetadonneeEnum metadonnee) {
        WidgetTypeEnum widgetType = metadonnee.getEditWidgetType();
        if (metadonnee == CommunicationMetadonneeEnum.DESTINATAIRE_COPIE) {
            widgetType = SELECT;
        } else if (metadonnee == CommunicationMetadonneeEnum.POSITION_ALERTE) {
            widgetType = RADIO;
        } else if (
            MgppEditWidgetFactory.METADONNEES_IDENTITE.contains(metadonnee.getName()) ||
            MgppEditWidgetFactory.METADONNEES_IDENTITE_LIST.contains(metadonnee.getName()) ||
            MgppEditWidgetFactory.METADONNEES_ORGANISME.contains(metadonnee.getName()) ||
            MgppEditWidgetFactory.METADONNEES_ORGANISME_LIST.contains(metadonnee.getName()) ||
            MgppEditWidgetFactory.METADONNEES_LISTSIMPLE.contains(metadonnee.getName())
        ) {
            widgetType = INPUT_TEXT;
        }
        List<SelectValueDTO> selectValues = new ArrayList<>();
        MgppSelectValueType selectValueType = MgppEditWidgetFactory.METADONNEES_INSTITUTION.contains(metadonnee)
            ? MgppSelectValueType.INSTITUTION
            : MgppSelectValueType.fromValue(metadonnee);
        if (selectValueType != MgppSelectValueType.DEFAULT) {
            selectValues = selectValueType.getSelectValues();
        }
        return buildWidgetDTO(context, metadonnee.getName(), widgetType, selectValues);
    }

    private static WidgetDTO buildWidgetDTO(
        SpecificContext context,
        String name,
        WidgetTypeEnum typeChamp,
        List<SelectValueDTO> lstSelectValues
    ) {
        String label = LABEL_MGPP_EVENEMENT + name;
        WidgetDTO widget = new WidgetDTO(name, label, typeChamp.toString());
        List<Parametre> parametres = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(lstSelectValues)) {
            parametres.add(
                new Parametre(
                    typeChamp == WidgetTypeEnum.NIVEAU_LECTURE
                        ? LST_NIVEAU_LECTURE
                        : MgppEditWidgetFactory.LST_VALUES_PARAM_NAME,
                    lstSelectValues
                )
            );
        }
        parametres.addAll(getParametresValeur(context, name, typeChamp));
        widget.setParametres(parametres);

        return widget;
    }

    private static List<Parametre> getParametresValeur(SpecificContext context, String name, WidgetTypeEnum typeChamp) {
        List<Parametre> parametres = new ArrayList<>();
        Map<String, Serializable> oldValues = context.getFromContextData(MgppContextDataKey.MAP_SEARCH);
        if (MapUtils.isNotEmpty(oldValues)) {
            Serializable value = oldValues.get(name);
            if (typeChamp == WidgetTypeEnum.DATE) {
                if (oldValues.containsKey(name + SUFFIXE_DEBUT)) {
                    parametres.add(
                        new Parametre(MgppEditWidgetFactory.VALEUR + SUFFIXE_DEBUT, oldValues.get(name + SUFFIXE_DEBUT))
                    );
                }
                if (oldValues.containsKey(name + SUFFIXE_FIN)) {
                    parametres.add(
                        new Parametre(MgppEditWidgetFactory.VALEUR + SUFFIXE_FIN, oldValues.get(name + SUFFIXE_FIN))
                    );
                }
            } else if (typeChamp == WidgetTypeEnum.NIVEAU_LECTURE) {
                parametres.add(
                    new Parametre(
                        MgppEditWidgetFactory.VALEUR,
                        (Serializable) Collections.singletonMap(MgppUIConstant.NIVEAU_LECTURE_CODE, value)
                    )
                );
                parametres.add(
                    new Parametre(
                        MgppEditWidgetFactory.NIVEAU_LECTURE_NUMERO,
                        oldValues.get(MgppEditWidgetFactory.NIVEAU_LECTURE_NUMERO)
                    )
                );
            } else if (oldValues.containsKey(name)) {
                parametres.add(new Parametre(MgppEditWidgetFactory.VALEUR, value));
            }
        }
        return parametres;
    }

    @Override
    public CritereRechercheDTO buildCriteresForRechercheAvancee(SpecificContext context) {
        Map<String, Serializable> mapSearch = context.getFromContextData(MgppContextDataKey.MAP_SEARCH);
        if (MapUtils.isEmpty(mapSearch)) {
            return new CritereRechercheDTOImpl();
        }
        String procedure = (String) mapSearch.getOrDefault("typeParlementaire", "");
        CritereRechercheDTO critereRecherche = new CritereRechercheDTOImpl(procedure);
        critereRecherche.putAll(mapSearch);
        if (CollectionUtils.isEmpty(critereRecherche.getTypeEvenement())) {
            critereRecherche.setTypeEvenement(
                SolonMgppServiceLocator
                    .getEvenementTypeService()
                    .findEvenementTypeByProcedure(procedure)
                    .stream()
                    .map(EvenementTypeDescriptor::getName)
                    .collect(Collectors.toList())
            );
        }
        MessageListForm messageListForm = context.getFromContextData(MgppContextDataKey.MESSAGE_LIST_FORM);
        critereRecherche.setSortInfos(messageListForm.getSortInfos());
        return critereRecherche;
    }
}
