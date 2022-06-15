package fr.dila.solonepg.ui.enums.mgpp;

import fr.dila.solonepg.ui.helper.mgpp.MgppWidgetFactory;
import fr.dila.st.ui.bean.WidgetDTO;
import fr.dila.st.ui.enums.WidgetTypeEnum;
import java.util.Arrays;
import java.util.function.Function;

public enum MgppWidgetTypeEnum {
    TEXT(WidgetTypeEnum.TEXT),
    URL(WidgetTypeEnum.URL),
    FILE_MULTI(WidgetTypeEnum.FILE_MULTI, MgppWidgetFactory::createFileMultiWidget),
    INPUT_TEXT(WidgetTypeEnum.INPUT_TEXT),
    MULTIPLE_INPUT_TEXT(WidgetTypeEnum.MULTIPLE_INPUT_TEXT),
    TEXT_AREA(WidgetTypeEnum.TEXT_AREA),
    DATE(WidgetTypeEnum.DATE, MgppWidgetFactory::createDateTextWidget),
    MULTIPLE_DATE(WidgetTypeEnum.MULTIPLE_DATE, MgppWidgetFactory::createMultipleDateTextWidget),
    RADIO(WidgetTypeEnum.RADIO, MgppWidgetFactory::createBooleanTextWidget),
    NIVEAU_LECTURE(WidgetTypeEnum.NIVEAU_LECTURE, MgppWidgetFactory::createNiveauLectureTextWidget),
    SELECT(WidgetTypeEnum.SELECT, MgppWidgetFactory::createPropertyTextWidget),
    PIECE_JOINTE(WidgetTypeEnum.PIECE_JOINTE),
    MULTIPLE_SELECT(WidgetTypeEnum.MULTIPLE_SELECT, MgppWidgetFactory::createMultiplePropertyTextWidget);

    private final WidgetTypeEnum parent;

    /**
     * Méthode de la factory de widget à utiliser pour générer un widget de ce type à partir des données réelles
     */
    private Function<Object, WidgetDTO> factoryFunction;

    /**
     * Type de champ dans le format serializé json. Le type de champ correspond à un
     * attribut texte (typeChamp) et un ensemble de paramètres
     */
    private final MgppTypeChamp typeChamp;

    MgppWidgetTypeEnum(WidgetTypeEnum parent) {
        this(parent, MgppTypeChamp.TEXT, MgppWidgetFactory::createTextWidget);
    }

    MgppWidgetTypeEnum(WidgetTypeEnum parent, Function<Object, WidgetDTO> factoryFunction) {
        this(parent, MgppTypeChamp.TEXT, factoryFunction);
    }

    MgppWidgetTypeEnum(WidgetTypeEnum parent, MgppTypeChamp typeChamp, Function<Object, WidgetDTO> factoryFunction) {
        this.parent = parent;
        this.typeChamp = typeChamp;
        this.factoryFunction = factoryFunction;
    }

    @Override
    public String toString() {
        return parent.toString();
    }

    public MgppTypeChamp getTypeChamp() {
        return typeChamp;
    }

    public Function<Object, WidgetDTO> getFactoryFunction() {
        return factoryFunction;
    }

    public static MgppWidgetTypeEnum fromWidgetTypeEnum(WidgetTypeEnum wte) {
        return Arrays.asList(values()).stream().filter(val -> val.parent == wte).findFirst().orElse(null);
    }
}
