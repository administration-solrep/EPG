package fr.dila.solonepg.ui.enums.pan;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.ui.enums.IColumnEnum;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.st.core.util.function.QuadriPredicate;
import fr.dila.st.ui.enums.SortOrder;
import java.util.Collection;
import java.util.Collections;
import java.util.function.BiPredicate;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;

public enum PanColumnEnum implements IColumnEnum<LoisSuiviesForm, PanFiltreEnum> {
    NUMERO_NOR(
        "pan.application.lois.table.lois.header.label.nor",
        "nor",
        LoisSuiviesForm::getNor,
        LoisSuiviesForm::getNorOrdre,
        null,
        null,
        PanFiltreEnum.NUMERO_NOR
    ),
    TITRE(
        "pan.application.lois.table.lois.header.label.titre",
        "titre",
        LoisSuiviesForm::getTitre,
        LoisSuiviesForm::getTitreOrdre,
        null,
        null,
        PanFiltreEnum.TITRE
    ),
    TITRE_DIRECTIVE(
        "pan.application.lois.table.lois.header.label.titre",
        "titre",
        LoisSuiviesForm::getTitre,
        LoisSuiviesForm::getTitreOrdre,
        null,
        null,
        PanFiltreEnum.TITRE_DIRECTIVE
    ),
    NUMERO(
        "pan.application.lois.table.lois.header.label.numero",
        "numero",
        LoisSuiviesForm::getNumero,
        LoisSuiviesForm::getNumeroOrdre,
        null,
        null,
        PanFiltreEnum.NUMERO
    ),
    MINISTERE_PILOTE(
        "pan.application.lois.table.lois.header.label.ministerePilote",
        "ministerePilote",
        LoisSuiviesForm::getMinistere,
        LoisSuiviesForm::getMinistereOrdre
    ),
    PROMULGATION(
        "pan.application.lois.table.lois.header.label.promulgation",
        "promulgation",
        LoisSuiviesForm::getPromulgation,
        LoisSuiviesForm::getPromulgationOrdre,
        Constants.IS_VISIBLE_ONLY_IN_TAB_AN_TABLEAU_LOIS,
        (currentSection, currentTab) ->
            Constants.VISIBLE_ONLY_IN_SECTIONS_AND_TABS.test(
                ImmutableList.of(
                    ActiviteNormativeEnum.APPLICATION_DES_LOIS.getId(),
                    ActiviteNormativeEnum.ORDONNANCES_38C.getId()
                ),
                currentSection,
                null,
                currentTab
            ),
        PanFiltreEnum.PROMULGATION
    ),
    DATE_PUBLICATION(
        "pan.application.lois.table.lois.header.label.datePublication",
        "datePublication",
        LoisSuiviesForm::getDatePublication,
        LoisSuiviesForm::getDatePublicationOrdre,
        null,
        (currentSection, currentTab) ->
            !StringUtils.equals(currentTab, ActiviteNormativeConstants.TAB_AN_TABLEAU_MAITRE) &&
            !StringUtils.equals(currentTab, ActiviteNormativeConstants.TAB_AN_RECHERCHE) ||
            !StringUtils.equals(currentSection, ActiviteNormativeEnum.APPLICATION_DES_LOIS.getId()),
        PanFiltreEnum.DATE_PUBLICATION
    ),
    PROCEDURE_VOTE(
        "pan.application.lois.table.lois.header.label.procedureVote",
        "procedureVote",
        LoisSuiviesForm::getProcedureVote,
        LoisSuiviesForm::getProcedureVoteOrdre,
        Constants.IS_VISIBLE_ONLY_IN_TAB_AN_TABLEAU_LOIS,
        Constants.IS_NEVER_VISIBLE,
        PanFiltreEnum.PROCEDURE_VOTE
    ),
    NATURE_TEXTE(
        "pan.application.lois.table.lois.header.label.natureTexte",
        "natureTexte",
        LoisSuiviesForm::getNatureTexte,
        LoisSuiviesForm::getNatureTexteOrdre,
        null,
        (currentSection, currentTab) ->
            StringUtils.equals(currentSection, ActiviteNormativeEnum.APPLICATION_DES_LOIS.getId()),
        PanFiltreEnum.NATURE_TEXTE
    ),
    LEGISLATURE_PUBLICATION(
        "pan.application.lois.table.lois.header.label.legislaturePublication",
        "legislaturePublication",
        LoisSuiviesForm::getLegislaturePublication,
        LoisSuiviesForm::getLegislaturePublicationOrdre,
        null,
        (currentSection, currentTab) ->
            StringUtils.equals(currentSection, ActiviteNormativeEnum.ORDONNANCES_38C.getId()),
        PanFiltreEnum.LEGISLATURE_PUBLICATION
    ),
    COMMISSION_AN(
        "pan.application.lois.table.lois.header.label.commissionAN",
        "commissionAN",
        LoisSuiviesForm::getCommissionAN,
        LoisSuiviesForm::getCommissionANOrdre,
        Constants.IS_VISIBLE_ONLY_IN_TAB_AN_TABLEAU_LOIS,
        Constants.IS_NEVER_VISIBLE,
        PanFiltreEnum.COMMISSION_AN
    ),
    COMMISSION_SENAT(
        "pan.application.lois.table.lois.header.label.commissionSenat",
        "commissionSenat",
        LoisSuiviesForm::getCommissionSenat,
        LoisSuiviesForm::getCommissionSenatOrdre,
        Constants.IS_VISIBLE_ONLY_IN_TAB_AN_TABLEAU_LOIS,
        Constants.IS_NEVER_VISIBLE,
        PanFiltreEnum.COMMISSION_SENAT
    ),
    NUMERO_INTERNE(
        "pan.application.lois.table.lois.header.label.numeroInterne",
        "numeroInterne",
        LoisSuiviesForm::getNumeroInterne,
        LoisSuiviesForm::getNumeroInterneOrdre,
        null,
        Constants.IS_NEVER_VISIBLE,
        PanFiltreEnum.NUMERO_INTERNE
    ),
    MOT_CLE(
        "pan.application.lois.table.lois.header.label.motCle",
        "motCle",
        LoisSuiviesForm::getMotCle,
        LoisSuiviesForm::getMotCleOrdre,
        null,
        (currentSection, currentTab) -> !StringUtils.equals(currentSection, ActiviteNormativeEnum.ORDONNANCES.getId()),
        PanFiltreEnum.MOT_CLE
    ),
    FONDEMENT_CONSTITUTIONNEL(
        "pan.ordonnances.fondementConstitutionnel",
        "fondementConstitutionnel",
        LoisSuiviesForm::getFondementConstitutionnel,
        LoisSuiviesForm::getFondementConstitutionnelOrdre,
        (currentSection, currentTab) ->
            Constants.VISIBLE_ONLY_IN_SECTIONS_AND_TABS.test(
                null,
                currentSection,
                Collections.singletonList(ActiviteNormativeConstants.TAB_AN_TABLEAU_ORDONNANCES),
                currentTab
            ),
        (currentSection, currentTab) ->
            Constants.VISIBLE_ONLY_IN_SECTIONS_AND_TABS.test(
                ImmutableList.of(
                    ActiviteNormativeEnum.ORDONNANCES.getId(),
                    ActiviteNormativeEnum.APPLICATION_DES_ORDONNANCES.getId()
                ),
                currentSection,
                null,
                currentTab
            ),
        PanFiltreEnum.FONDEMENT_CONSTITUTIONNEL
    ),
    CHAMP_LIBRE(
        "pan.application.lois.table.lois.header.label.champLibre",
        "champLibre",
        LoisSuiviesForm::getChampLibre,
        LoisSuiviesForm::getChampLibreOrdre,
        null,
        Constants.IS_NEVER_VISIBLE,
        PanFiltreEnum.CHAMP_LIBRE
    ),
    DATE_ADOPTION(
        "pan.directives.table.header.label.dateAdoption",
        "dateAdoption",
        LoisSuiviesForm::getDateAdoption,
        LoisSuiviesForm::getDateAdoptionOrdre
    ),
    DATE_ECHEANCE(
        "pan.directives.table.header.label.dateEcheance",
        "dateEcheance",
        LoisSuiviesForm::getDateEcheance,
        LoisSuiviesForm::getDateEcheanceOrdre
    ),
    DROIT_CONFORME(
        "pan.directives.table.header.label.droitConforme",
        "droitConforme",
        LoisSuiviesForm::getDroitConforme,
        LoisSuiviesForm::getDroitConformeOrdre
    ),
    DATE_PROCHAIN_TAB_AFFICHAGE(
        "pan.directives.table.header.label.dateProchainTabAffichage",
        "dateProchainTabAffichage",
        LoisSuiviesForm::getDateProchainTabAffichage,
        LoisSuiviesForm::getDateProchainTabAffichageOrdre
    ),
    ACHEVEE(
        "pan.directives.table.header.label.achevee",
        "achevee",
        LoisSuiviesForm::getAchevee,
        LoisSuiviesForm::getAcheveeOrdre
    ),
    OBSERVATION(
        "pan.application.lois.table.lois.header.label.observation",
        "observation",
        LoisSuiviesForm::getObservation,
        LoisSuiviesForm::getObservationOrdre,
        null,
        null,
        PanFiltreEnum.OBSERVATIONS
    ),
    CATEGORIE(
        "pan.traites.table.traites.header.label.categorie",
        LoisSuiviesForm.CATEGORIE_PARAM,
        LoisSuiviesForm::getCategorie,
        LoisSuiviesForm::getCategorieOrdre,
        null,
        null,
        PanFiltreEnum.CATEGORIE
    ),
    PAYS(
        "pan.traites.table.traites.header.label.pays",
        LoisSuiviesForm.PAYS_PARAM,
        LoisSuiviesForm::getPays,
        LoisSuiviesForm::getPaysOrdre,
        null,
        null,
        PanFiltreEnum.ORGANISATION
    ),
    THEMATIQUE(
        "pan.traites.table.traites.header.label.thematique",
        LoisSuiviesForm.THEMATIQUE_PARAM,
        LoisSuiviesForm::getThematique,
        LoisSuiviesForm::getThematiqueOrdre,
        null,
        null,
        PanFiltreEnum.THEMATIQUE
    ),
    INTITULE(
        "pan.traites.table.traites.header.label.intitule",
        LoisSuiviesForm.TITRE_ACTE_PARAM,
        LoisSuiviesForm::getTitreActe,
        LoisSuiviesForm::getTitreActeOrdre,
        null,
        null,
        PanFiltreEnum.INTITULE
    ),
    DATE_SIGNATURE(
        "pan.traites.table.traites.header.label.dateSignature",
        LoisSuiviesForm.DATE_SIGNATURE_PARAM,
        LoisSuiviesForm::getDateSignature,
        LoisSuiviesForm::getDateSignatureOrdre,
        null,
        null,
        PanFiltreEnum.DATE_SIGNATURE
    ),
    DATE_ENTREE_EN_VIGUEUR(
        "pan.traites.table.traites.header.label.dateEntreeVigueur",
        LoisSuiviesForm.DATE_ENTREE_EN_VIGUEUR_PARAM,
        LoisSuiviesForm::getDateEntreeEnVigueur,
        LoisSuiviesForm::getDateEntreeEnVigueurOrdre,
        null,
        null,
        PanFiltreEnum.DATE_ENTREE_VIGUEUR
    );

    private static class Constants {
        private static final BiPredicate<Collection<String>, String> IS_VISIBLE_IN = (visibleIn, current) ->
            visibleIn == null || visibleIn.contains(current);

        private static final QuadriPredicate<Collection<String>, String, Collection<String>, String> VISIBLE_ONLY_IN_SECTIONS_AND_TABS = (visibleSections, currentSection, visibleTabs, currentTab) ->
            IS_VISIBLE_IN.test(visibleSections, currentSection) && IS_VISIBLE_IN.test(visibleTabs, currentTab);

        private static final BiPredicate<String, String> IS_VISIBLE_ONLY_IN_TAB_AN_TABLEAU_LOIS = (currentSection, currentTab) ->
            VISIBLE_ONLY_IN_SECTIONS_AND_TABS.test(
                null,
                currentSection,
                Collections.singletonList(ActiviteNormativeConstants.TAB_AN_TABLEAU_LOIS),
                currentTab
            );

        private static final BiPredicate<String, String> IS_NEVER_VISIBLE = (currentSection, currentTab) -> false;
    }

    /**
     * Clé de property associée
     */
    private final String label;

    /**
     * name nuxeo associé
     */
    private final String name;

    /**
     * La fonction de récupération du SortOrder associé
     */
    private final Function<LoisSuiviesForm, SortOrder> sortOrderGetter;

    /**
     * La fonction de récupération du sens de tri associé (peut être null)
     */
    private final Function<LoisSuiviesForm, Integer> orderGetter;

    /**
     * Condition d'apparition de la colonne dans les onglets tableau des ordonnances
     * ou tableau des lois
     */
    private final BiPredicate<String, String> visibilityFunctionInTabOrdoLois;

    /**
     * Filtre associé
     */
    private final PanFiltreEnum filtre;

    /**
     * Condition d'apparition de la colonne dans les autres sections/onglets (espaces Application des lois/ordonnances)
     */
    private final BiPredicate<String, String> visibilityFunctionElseWhere;

    PanColumnEnum(
        String label,
        String name,
        Function<LoisSuiviesForm, SortOrder> sortOrderFunction,
        Function<LoisSuiviesForm, Integer> orderFunction
    ) {
        this(label, name, sortOrderFunction, orderFunction, null, null, null);
    }

    PanColumnEnum(
        String label,
        String name,
        Function<LoisSuiviesForm, SortOrder> sortOrderFunction,
        Function<LoisSuiviesForm, Integer> orderFunction,
        BiPredicate<String, String> visibilityFunctionInTabOrdoLois,
        BiPredicate<String, String> visibilityFunctionElseWhere,
        PanFiltreEnum filtre
    ) {
        this.label = label;
        this.name = name;
        this.sortOrderGetter = sortOrderFunction;
        this.orderGetter = orderFunction;
        this.visibilityFunctionInTabOrdoLois = visibilityFunctionInTabOrdoLois;
        this.visibilityFunctionElseWhere = visibilityFunctionElseWhere;
        this.filtre = filtre;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getNxPropName() {
        return name;
    }

    @Override
    public Function<LoisSuiviesForm, SortOrder> getSortOrderGetter() {
        return sortOrderGetter;
    }

    @Override
    public Function<LoisSuiviesForm, Integer> getOrderGetter() {
        return orderGetter;
    }

    public boolean isVisibleInTabOrdoLois(String currentSection, String currentTab) {
        if (visibilityFunctionInTabOrdoLois == null) {
            // No condition, always visible
            return true;
        }
        return visibilityFunctionInTabOrdoLois.test(currentSection, currentTab);
    }

    public boolean isVisibleElseWhere(String currentSection, String currentTab) {
        if (visibilityFunctionElseWhere == null) {
            // No condition, always visible
            return true;
        }
        return visibilityFunctionElseWhere.test(currentSection, currentTab);
    }

    @Override
    public PanFiltreEnum getFiltre() {
        return filtre;
    }
}
