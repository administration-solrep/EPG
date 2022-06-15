package fr.dila.solonepg.ui.services.requete.impl;

import com.google.common.collect.ImmutableMap;
import fr.dila.solonepg.ui.services.SolonEpgUIServiceLocator;
import fr.dila.ss.api.constant.SSRechercheChampConstants;
import fr.dila.st.core.requete.recherchechamp.ChampParameter;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.SelectValueDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class RequeteChampStatutValidationParameterServiceImpl implements ChampParameter {

    @Override
    public Map<String, Serializable> getAdditionalParameters() {
        return ImmutableMap.of(
            SSRechercheChampConstants.OPTIONS_PARAMETER_NAME,
            SolonEpgUIServiceLocator
                .getEpgSelectValueUIService()
                .getStatutValidation()
                .stream()
                .map(v -> new SelectValueDTO(v.getId(), ResourceHelper.getString(v.getLabel())))
                .collect(Collectors.toCollection(ArrayList::new))
        );
    }
}
