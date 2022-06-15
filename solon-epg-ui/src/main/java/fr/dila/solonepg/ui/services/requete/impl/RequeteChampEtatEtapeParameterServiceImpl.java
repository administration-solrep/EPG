package fr.dila.solonepg.ui.services.requete.impl;

import com.google.common.collect.ImmutableMap;
import fr.dila.ss.api.constant.SSRechercheChampConstants;
import fr.dila.ss.ui.enums.EtapeLifeCycle;
import fr.dila.st.core.requete.recherchechamp.ChampParameter;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.SelectValueDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class RequeteChampEtatEtapeParameterServiceImpl implements ChampParameter {

    @Override
    public Map<String, Serializable> getAdditionalParameters() {
        return ImmutableMap.of(
            SSRechercheChampConstants.OPTIONS_PARAMETER_NAME,
            Arrays
                .stream(EtapeLifeCycle.values())
                .map(c -> new SelectValueDTO(c.getValue(), ResourceHelper.getString(c.getLabelKey())))
                .collect(Collectors.toCollection(ArrayList::new))
        );
    }
}
