package fr.dila.solonepg.ui.services.mgpp.requete.impl;

import com.google.common.collect.ImmutableMap;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.ss.api.constant.SSRechercheChampConstants;
import fr.dila.st.core.requete.recherchechamp.ChampParameter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class MgppRequeteChampSelectableInstitutionsImpl implements ChampParameter {

    @Override
    public Map<String, Serializable> getAdditionalParameters() {
        return ImmutableMap.of(
            SSRechercheChampConstants.OPTIONS_PARAMETER_NAME,
            new ArrayList<>(MgppUIServiceLocator.getMgppSelectValueUIService().getSelectableInstitutions())
        );
    }
}
