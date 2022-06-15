package fr.dila.solonepg.ui.services.requete.impl;

import fr.dila.solonepg.ui.services.SolonEpgUIServiceLocator;
import java.io.Serializable;
import java.util.Map;

public class RequeteChampCategorieActeParameterServiceImpl extends AbstractRequeteChampSelectValueService {

    @Override
    public Map<String, Serializable> getAdditionalParameters() {
        return getAdditionalParameters(SolonEpgUIServiceLocator.getEpgSelectValueUIService()::getUnaryCategorieActe);
    }
}
