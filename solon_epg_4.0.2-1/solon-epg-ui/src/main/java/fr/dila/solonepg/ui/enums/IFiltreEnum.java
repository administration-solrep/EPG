package fr.dila.solonepg.ui.enums;

import fr.dila.solonepg.ui.helper.mgpp.MgppEditWidgetFactory;
import fr.dila.st.core.requete.recherchechamp.Parametre;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.bean.WidgetDTO;
import fr.dila.st.ui.enums.WidgetTypeEnum;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.text.CaseUtils;

public interface IFiltreEnum {
    /** Renvoie le type de widget : texte/date/select/... */
    WidgetTypeEnum getType();

    String getName();

    String getWidgetLabel();

    /** Supplier de valeurs dans le cas d'un select */
    Supplier<List<SelectValueDTO>> getSupplier();

    /** Renvoie le xpath du champ Nuxeo sur lequel ce filtre opère. */
    String getXpath();

    /**
     * Renvoie la fonction de traitement à appliquer à la valeur asspcoée à ce
     * filtre avant de la transférer au provider. Exemple : rajouter * devant et
     * derrière.
     */
    Function<Serializable, Serializable> getValueTreatmentFct();

    default String getWidgetName() {
        return CaseUtils.toCamelCase(getName().toLowerCase(), false, '_');
    }

    default WidgetDTO initWidget() {
        String name = getWidgetName();
        String label = getWidgetLabel();
        WidgetDTO widget = new WidgetDTO(name, label, getType().toString());
        List<SelectValueDTO> lstSelectValues = null;

        if (getType() == WidgetTypeEnum.SELECT && getSupplier() != null) {
            lstSelectValues = getSupplier().get();
            lstSelectValues.sort(SelectValueDTO.getLabelComparator());
        }

        if (CollectionUtils.isNotEmpty(lstSelectValues)) {
            widget.getParametres().add(new Parametre(MgppEditWidgetFactory.LST_VALUES_PARAM_NAME, lstSelectValues));
        }
        return widget;
    }

    default Serializable computeTreatedValue(Serializable value) {
        if (getValueTreatmentFct() == null) {
            // Pas de modification
            return value;
        }
        return getValueTreatmentFct().apply(value);
    }
}
