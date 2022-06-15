package fr.dila.solonepg.ui.services.impl;

import static fr.dila.solonepg.core.service.SolonEpgServiceLocator.getTypeActeService;

import fr.dila.solonepg.adamant.SolonEpgAdamantServiceLocator;
import fr.dila.solonepg.api.administration.ParametrageAdamant;
import fr.dila.solonepg.api.administration.VecteurPublication;
import fr.dila.solonepg.api.constant.SolonEpgVecteurPublicationConstants;
import fr.dila.solonepg.api.service.ParametrageAdamantService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgParametrageAdamantUIService;
import fr.dila.solonepg.ui.th.bean.ParametreAdamantForm;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.mapper.MapDoc2Bean;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.nuxeo.ecm.core.api.DocumentModel;

public class EpgParametrageAdamantUIServiceImpl implements EpgParametrageAdamantUIService {
    public static final String GET_VECTORS_QUERY_FROM_LABEL =
        "SELECT v.ecm:uuid as id FROM VecteurPublication as v where v.vp:vpIntitule LIKE ? ";

    @Override
    public void save(SpecificContext context) {
        ParametreAdamantForm parametreAdamantForm = context.getFromContextData(
            EpgContextDataKey.PARAMETRE_ADAMANT_FORM
        );

        ParametrageAdamantService parametrageAdamantService = SolonEpgServiceLocator.getParametrageAdamantService();
        ParametrageAdamant parametrageAdamant = parametrageAdamantService.getParametrageAdamantDocument(
            context.getSession()
        );

        MapDoc2Bean.beanToDoc(parametreAdamantForm, parametrageAdamant.getDocument());

        context.getSession().saveDocument(parametrageAdamant.getDocument());
    }

    @Override
    public ParametreAdamantForm getParametreAdamantDocument(SpecificContext context) {
        ParametrageAdamantService parametrageAdamantService = SolonEpgServiceLocator.getParametrageAdamantService();

        ParametrageAdamant parametrageAdamant = parametrageAdamantService.getParametrageAdamantDocument(
            context.getSession()
        );
        return MapDoc2Bean.docToBean(parametrageAdamant.getDocument(), ParametreAdamantForm.class);
    }

    @Override
    public List<String> getVecteurPublicationEligibilite(SpecificContext context) {
        return SolonEpgAdamantServiceLocator
            .getDossierExtractionService()
            .getVecteurPublicationEligibilite(context.getSession());
    }

    @Override
    public List<String> getSuggestion(SpecificContext context) {
        List<String> result = new ArrayList<>();
        String suggestion = context.getFromContextData(STContextDataKey.INPUT);
        String typeSelection = context.getFromContextData(STContextDataKey.TYPE_SELECTION);
        if ("TypeActe".equals(typeSelection)) {
            result =
                getTypeActeService()
                    .getEntries()
                    .stream()
                    .filter(e -> e.getValue().contains(suggestion))
                    .map(Pair::getValue)
                    .collect(Collectors.toList());
        } else if ("VecteurPublication".equals(typeSelection)) {
            List<DocumentModel> list = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
                context.getSession(),
                SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DOCUMENT_TYPE,
                GET_VECTORS_QUERY_FROM_LABEL,
                new Object[] { suggestion + "%" }
            );
            result =
                list
                    .stream()
                    .map(dm -> dm.getAdapter(VecteurPublication.class))
                    .map(VecteurPublication::getIntitule)
                    .collect(Collectors.toList());
        }
        return result;
    }
}
