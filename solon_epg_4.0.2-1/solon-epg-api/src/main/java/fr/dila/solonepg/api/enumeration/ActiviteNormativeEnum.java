package fr.dila.solonepg.api.enumeration;

import com.google.common.collect.ImmutableMap;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants.MAJ_TARGET;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;

public enum ActiviteNormativeEnum {
    APPLICATION_DES_LOIS(
        new Builder("lois")
            .setLabel("espace.activite.normative.application.lois")
            .setRight(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_READER)
            .setMenuIndex(ActiviteNormativeConstants.MENU_0)
            .setTypeActiviteNormative(ActiviteNormativeConstants.TYPE_APPLICATION_LOIS)
            .setAttributSchema(ActiviteNormativeConstants.ATTRIBUT_APPLICATION_LOIS)
            .setMajTarget(ActiviteNormativeConstants.MAJ_TARGET.APPLICATION_LOI)
            .setNorType(TypeNorAutoComplete.LOI)
            .setDefaultSortColumn(ActiviteNormativeConstants.DEFAULT_TEXM_SORT_COLUMN.PROMULGATION)
            .setRightsForProfile(
                ImmutableMap.of(
                    ActiviteNormativeProfileEnum.READER,
                    SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_READER,
                    ActiviteNormativeProfileEnum.UPDATER,
                    SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_UPDATER,
                    ActiviteNormativeProfileEnum.MINISTERE,
                    SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_LOI_MINISTERE
                )
            )
    ),
    APPLICATION_DES_ORDONNANCES(
        new Builder("ordonnances")
            .setLabel("espace.activite.normative.application.ordonnances")
            .setRight(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_READER)
            .setMenuIndex(ActiviteNormativeConstants.MENU_5)
            .setTypeActiviteNormative(ActiviteNormativeConstants.TYPE_APPLICATION_ORDONNANCES)
            .setAttributSchema(ActiviteNormativeConstants.ATTRIBUT_APPLICATION_ORDONNANCES)
            .setMajTarget(ActiviteNormativeConstants.MAJ_TARGET.APPLICATION_ORDONNANCE)
            .setNorType(TypeNorAutoComplete.ORDONNANCE)
            .setDefaultSortColumn(ActiviteNormativeConstants.DEFAULT_TEXM_SORT_COLUMN.PUBLICATION)
            .setRightsForProfile(
                ImmutableMap.of(
                    ActiviteNormativeProfileEnum.READER,
                    SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_READER,
                    ActiviteNormativeProfileEnum.UPDATER,
                    SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_UPDATER,
                    ActiviteNormativeProfileEnum.MINISTERE,
                    SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_ORDONNANCE_MINISTERE
                )
            )
    ),
    TRANSPOSITION(
        new Builder("directives")
            .setLabel("espace.activite.normative.transposition")
            .setRight(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRANSPOSITION_READER)
            .setMenuIndex(ActiviteNormativeConstants.MENU_3)
            .setTypeActiviteNormative(ActiviteNormativeConstants.TYPE_TRANSPOSITIONS)
            .setAttributSchema(ActiviteNormativeConstants.ATTRIBUT_TRANSPOSITION)
            .setMajTarget(MAJ_TARGET.TRANSPOSITION)
            .setDefaultSortColumn(ActiviteNormativeConstants.DEFAULT_TEXM_SORT_COLUMN.ADOPTION)
            .setRightsForProfile(
                ImmutableMap.of(
                    ActiviteNormativeProfileEnum.READER,
                    SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRANSPOSITION_READER,
                    ActiviteNormativeProfileEnum.UPDATER,
                    SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRANSPOSITION_UPDATER
                )
            )
    ),
    ORDONNANCES_38C(
        new Builder("habilitations")
            .setLabel("espace.activite.normative.ordonnances.38C")
            .setRight(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_SUIVIS_READER)
            .setMenuIndex(ActiviteNormativeConstants.MENU_4)
            .setTypeActiviteNormative(ActiviteNormativeConstants.TYPE_ORDONNANCES_38C)
            .setAttributSchema(ActiviteNormativeConstants.ATTRIBUT_ORDONNANCE_38C)
            .setMajTarget(ActiviteNormativeConstants.MAJ_TARGET.HABILITATION)
            .setNorType(TypeNorAutoComplete.LOI)
            .setDefaultSortColumn(ActiviteNormativeConstants.DEFAULT_TEXM_SORT_COLUMN.PROMULGATION)
            .setRightsForProfile(
                ImmutableMap.of(
                    ActiviteNormativeProfileEnum.READER,
                    SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_SUIVIS_READER,
                    ActiviteNormativeProfileEnum.UPDATER,
                    SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_SUIVIS_UPDATER
                )
            )
    ),
    ORDONNANCES(
        new Builder("ratification")
            .setLabel("espace.activite.normative.ordonnances")
            .setRight(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_READER)
            .setMenuIndex(ActiviteNormativeConstants.MENU_1)
            .setTypeActiviteNormative(ActiviteNormativeConstants.TYPE_ORDONNANCES)
            .setAttributSchema(ActiviteNormativeConstants.ATTRIBUT_ORDONNANCE)
            .setMajTarget(ActiviteNormativeConstants.MAJ_TARGET.ORDONNANCE)
            .setNorType(TypeNorAutoComplete.ORDONNANCE)
            .setDefaultSortColumn(ActiviteNormativeConstants.DEFAULT_TEXM_SORT_COLUMN.PUBLICATION)
            .setRightsForProfile(
                ImmutableMap.of(
                    ActiviteNormativeProfileEnum.READER,
                    SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_READER,
                    ActiviteNormativeProfileEnum.UPDATER,
                    SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_UPDATER
                )
            )
    ),
    TRAITES_ET_ACCORDS(
        new Builder("traites")
            .setLabel("espace.activite.normative.traites.accords")
            .setRight(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRAITE_READER)
            .setMenuIndex(ActiviteNormativeConstants.MENU_2)
            .setTypeActiviteNormative(ActiviteNormativeConstants.TYPE_TRAITES_ACCORDS)
            .setAttributSchema(ActiviteNormativeConstants.ATTRIBUT_TRAITE_ACCORD)
            .setRightsForProfile(
                ImmutableMap.of(
                    ActiviteNormativeProfileEnum.READER,
                    SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRAITE_READER,
                    ActiviteNormativeProfileEnum.UPDATER,
                    SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRAITE_UPDATER
                )
            )
    ),
    APPLICATION_DES_LOIS_MINISTERE(
        new Builder("APPLICATION_DES_LOIS_MINISTERE")
            .setLabel("espace.activite.normative.application.lois")
            .setRight(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_LOI_MINISTERE)
            .setMenuIndex(ActiviteNormativeConstants.MENU_0)
            .setTypeActiviteNormative(ActiviteNormativeConstants.TYPE_APPLICATION_LOIS)
            .setAttributSchema(ActiviteNormativeConstants.ATTRIBUT_APPLICATION_LOIS)
    ),
    APPLICATION_DES_ORDONNANCES_MINISTERE(
        new Builder("APPLICATION_DES_ORDONNANCES_MINISTERE")
            .setLabel("espace.activite.normative.application.ordonnances")
            .setRight(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_ORDONNANCE_MINISTERE)
            .setMenuIndex(ActiviteNormativeConstants.MENU_5)
            .setTypeActiviteNormative(ActiviteNormativeConstants.TYPE_APPLICATION_ORDONNANCES)
            .setAttributSchema(ActiviteNormativeConstants.ATTRIBUT_APPLICATION_ORDONNANCES)
    );

    private final String id;
    private final String label;
    private final String right;
    private final String menuIndex;
    private final String typeActiviteNormative;
    private final String attributSchema;
    private final MAJ_TARGET majTarget;
    private final TypeNorAutoComplete norType;
    private final Map<ActiviteNormativeProfileEnum, String> rightsForProfile;
    private final ActiviteNormativeConstants.DEFAULT_TEXM_SORT_COLUMN defaultSortColumn;

    ActiviteNormativeEnum(Builder builder) {
        this.id = builder.id;
        this.label = builder.label;
        this.right = builder.right;
        this.menuIndex = builder.menuIndex;
        this.typeActiviteNormative = builder.typeActiviteNormative;
        this.attributSchema = builder.attributSchema;
        this.majTarget = builder.majTarget;
        this.norType = builder.norType;
        this.defaultSortColumn = builder.sortColumn;
        rightsForProfile = builder.rightsForProfile;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getRight() {
        return right;
    }

    public String getMenuIndex() {
        return menuIndex;
    }

    public static ActiviteNormativeEnum getById(String id) {
        return Stream.of(values()).filter(an -> an.getId().equals(id)).findFirst().orElse(null);
    }

    public String getTypeActiviteNormative() {
        return typeActiviteNormative;
    }

    public static ActiviteNormativeEnum getByType(String type) {
        return Stream.of(values()).filter(an -> an.getTypeActiviteNormative().equals(type)).findFirst().orElse(null);
    }

    public String getAttributSchema() {
        return attributSchema;
    }

    public MAJ_TARGET getMajTarget() {
        return majTarget;
    }

    public TypeNorAutoComplete getNorType() {
        return norType;
    }

    public ActiviteNormativeConstants.DEFAULT_TEXM_SORT_COLUMN getDefaultSortColumn() {
        return defaultSortColumn;
    }

    public List<String> getRightsForProfile(boolean isReader, boolean isUpdater, boolean isMinistere) {
        List<String> rights = new ArrayList<>();
        addRightForProfile(rights, ActiviteNormativeProfileEnum.READER, isReader);
        addRightForProfile(rights, ActiviteNormativeProfileEnum.UPDATER, isUpdater);
        addRightForProfile(rights, ActiviteNormativeProfileEnum.MINISTERE, isMinistere);

        return rights;
    }

    private void addRightForProfile(List<String> rights, ActiviteNormativeProfileEnum profile, boolean hasProfile) {
        if (hasProfile) {
            CollectionUtils.addIgnoreNull(rights, rightsForProfile.get(profile));
        }
    }

    private static class Builder {
        private String id;
        private String label;
        private String right;
        private String menuIndex;
        private String typeActiviteNormative;
        private String attributSchema;
        private MAJ_TARGET majTarget;
        private TypeNorAutoComplete norType;
        private Map<ActiviteNormativeProfileEnum, String> rightsForProfile;
        private ActiviteNormativeConstants.DEFAULT_TEXM_SORT_COLUMN sortColumn;

        private Builder(String id) {
            this.id = id;
        }

        private Builder setLabel(String label) {
            this.label = label;
            return this;
        }

        private Builder setRight(String right) {
            this.right = right;
            return this;
        }

        private Builder setMenuIndex(String menuIndex) {
            this.menuIndex = menuIndex;
            return this;
        }

        private Builder setTypeActiviteNormative(String typeActiviteNormative) {
            this.typeActiviteNormative = typeActiviteNormative;
            return this;
        }

        private Builder setAttributSchema(String attributSchema) {
            this.attributSchema = attributSchema;
            return this;
        }

        private Builder setMajTarget(MAJ_TARGET majTarget) {
            this.majTarget = majTarget;
            return this;
        }

        private Builder setNorType(TypeNorAutoComplete norType) {
            this.norType = norType;
            return this;
        }

        private Builder setRightsForProfile(Map<ActiviteNormativeProfileEnum, String> rightsForProfile) {
            this.rightsForProfile = rightsForProfile;
            return this;
        }

        public Builder setDefaultSortColumn(ActiviteNormativeConstants.DEFAULT_TEXM_SORT_COLUMN sortColumn) {
            this.sortColumn = sortColumn;
            return this;
        }
    }
}
