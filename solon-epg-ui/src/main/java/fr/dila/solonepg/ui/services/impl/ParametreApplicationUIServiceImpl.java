package fr.dila.solonepg.ui.services.impl;

import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.COMMENTAIRE_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.DATE_ARRIVEE_NAME;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.api.administration.ParametrageApplication;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.service.ParametreApplicationService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.helper.EpgDossierListHelper;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.ParametreApplicationUIService;
import fr.dila.solonepg.ui.th.bean.ParametreApplicationForm;
import fr.dila.st.core.exception.STValidationException;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.mapper.MapDoc2Bean;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.math.NumberUtils;

public class ParametreApplicationUIServiceImpl implements ParametreApplicationUIService {
    private static final List<String> UNAVAILABLE_METADATAS = ImmutableList.of(
        COMMENTAIRE_NAME,
        DATE_ARRIVEE_NAME,
        DossierSolonEpgConstants.DOSSIER_MINISTERE_RESPONSABLE_PROPERTY,
        DossierSolonEpgConstants.DOSSIER_DIRECTION_RESPONSABLE_PROPERTY
    );

    @Override
    public ParametreApplicationForm getParametreApplication(SpecificContext context) {
        ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator.getParametreApplicationService();
        ParametrageApplication parametrageApplication = parametreApplicationService.getParametreApplicationDocument(
            context.getSession()
        );

        ParametreApplicationForm form = MapDoc2Bean.docToBean(
            parametrageApplication.getDocument(),
            ParametreApplicationForm.class
        );
        form.setPassEppInfosParl(null);

        form.setMetadonneDispos(
            EpgUIServiceLocator
                .getEpgSelectValueUIService()
                .getBordereauLabel()
                .stream()
                .filter(
                    selectValue ->
                        !UNAVAILABLE_METADATAS.contains(selectValue.getId()) &&
                        EpgDossierListHelper.filterColumnsUserCanOptToDisplay(selectValue)
                )
                .collect(Collectors.toList())
        );
        List<String> lstUnselectedData = form
            .getMetadonneDispos()
            .stream()
            .map(SelectValueDTO::getId)
            .filter(id -> !form.getMetadonneDisponibleColonneCorbeille().contains(id))
            .collect(Collectors.toList());
        form.setMetadonneDisponibleColonneCorbeille(lstUnselectedData);

        return form;
    }

    @Override
    public void save(SpecificContext context) {
        ParametreApplicationForm parametreApplicationForm = context.getFromContextData(
            EpgContextDataKey.PARAMETRE_APPLICATION_FORM
        );

        if (
            ObjectHelper.requireNonNullElse(
                parametreApplicationForm.getDateRafraichissementCorbeille(),
                NumberUtils.LONG_ZERO
            ) <
            NumberUtils.LONG_ONE
        ) {
            throw new STValidationException("parametresAppli.error.delai.rafraichissement");
        }

        ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator.getParametreApplicationService();
        ParametrageApplication parametrageApplication = parametreApplicationService.getParametreApplicationDocument(
            context.getSession()
        );

        MapDoc2Bean.beanToDoc(parametreApplicationForm, parametrageApplication.getDocument());
        parametrageApplication.setPassEppInfosParl(parametreApplicationForm.getPassEppInfosParl());

        context.getSession().saveDocument(parametrageApplication.getDocument());
    }
}
