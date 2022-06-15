package fr.dila.solonepg.ui.services.requete.impl;

import com.google.common.collect.ImmutableMap;
import fr.dila.ss.api.constant.SSRechercheChampConstants;
import fr.dila.st.core.requete.recherchechamp.ChampParameter;
import fr.dila.st.ui.bean.SelectValueDTO;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractRequeteChampSelectValueService implements ChampParameter {

    protected Map<String, Serializable> getAdditionalParameters(Supplier<List<SelectValueDTO>> supplier) {
        return ImmutableMap.of(SSRechercheChampConstants.OPTIONS_PARAMETER_NAME, (Serializable) sorted(supplier.get()));
    }

    private List<SelectValueDTO> sorted(List<SelectValueDTO> values) {
        values.sort(Comparator.comparing(SelectValueDTO::getValue));
        return values;
    }
}
