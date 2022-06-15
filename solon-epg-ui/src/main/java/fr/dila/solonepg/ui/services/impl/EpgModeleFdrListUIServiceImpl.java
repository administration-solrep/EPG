package fr.dila.solonepg.ui.services.impl;

import static java.util.Arrays.asList;

import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.helper.SolonEpgProviderHelper;
import fr.dila.solonepg.ui.services.EpgModeleFdrListUIService;
import fr.dila.ss.api.logging.enumerationCodes.SSLogEnumImpl;
import fr.dila.ss.ui.bean.fdr.FeuilleRouteDTO;
import fr.dila.ss.ui.bean.fdr.ModeleFDRList;
import fr.dila.ss.ui.contentview.ModeleFDRPageProvider;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.services.impl.SSModeleFdrListUIServiceImpl;
import fr.dila.ss.ui.th.bean.ModeleFDRListForm;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.helper.PaginationHelper;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.STCsvUtils;
import fr.dila.st.core.util.StringHelper;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.th.bean.PaginationForm;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EpgModeleFdrListUIServiceImpl extends SSModeleFdrListUIServiceImpl implements EpgModeleFdrListUIService {
    private static final STLogger LOG = STLogFactory.getLog(EpgModeleFdrListUIServiceImpl.class);

    @Override
    public ModeleFDRList getModelesFDRByIds(SpecificContext context) {
        ModeleFDRList modeleFDRList = new ModeleFDRList();

        try {
            ModeleFDRListForm form = ObjectHelper.requireNonNullElseGet(
                context.getFromContextData(SSContextDataKey.LIST_MODELE_FDR),
                ModeleFDRListForm::new
            );

            ModeleFDRPageProvider provider = buildModeleFDRProvider("modeleFDRByIdPageProvider", context, form);

            modeleFDRList.buildColonnesSubstitution(form);
            modeleFDRList.setIsSubstitutionTable(false);
            // On fait le mapping des documents vers notre DTO
            modeleFDRList.setListe(
                provider
                    .getCurrentPage()
                    .stream()
                    .filter(FeuilleRouteDTO.class::isInstance)
                    .map(FeuilleRouteDTO.class::cast)
                    .collect(Collectors.toList())
            );
            modeleFDRList.setNbTotal((int) provider.getResultsCount());
            form.setPage(PaginationHelper.getPageFromCurrentIndex(provider.getCurrentPageIndex()));
        } catch (Exception e) {
            LOG.error(
                context.getSession(),
                SSLogEnumImpl.FAIL_GET_MOD_FDR_TEC,
                "Une erreur est survenue lors de la récupération des modèles de feuille de route",
                e
            );
            throw e;
        }
        return modeleFDRList;
    }

    public ModeleFDRPageProvider buildModeleFDRProvider(
        String pageProviderName,
        SpecificContext context,
        ModeleFDRListForm form
    ) {
        List<String> idDossiers = context.getFromContextData(EpgContextDataKey.ID_DOSSIERS);
        String ids = StringHelper.join(idDossiers, "','", "");
        return form.getPageProvider(context.getSession(), pageProviderName, "fdr.", Collections.singletonList(ids));
    }

    @Override
    public ModeleFDRList getDernierModelFDRConsulte(SpecificContext context) {
        PaginationForm form = context.getFromContextData(STContextDataKey.PAGINATION_FORM);
        String username = context.getSession().getPrincipal().getName();
        ModeleFDRPageProvider provider = form.getPageProvider(
            context.getSession(),
            "model_fdr_consult",
            asList(username)
        );

        ModeleFDRList modeleList = SolonEpgProviderHelper.builModelFdrList(provider);
        form.setPage(PaginationHelper.getPageFromCurrentIndex(provider.getCurrentPageIndex()));

        return modeleList;
    }

    @Override
    public File listeModeleToCsv(SpecificContext context) {
        ModeleFDRList lstResults = context.getFromContextData(SSContextDataKey.MODELE_FDR_LIST);
        List<String[]> dataLines = lstResults
            .getListe()
            .stream()
            .map(
                e ->
                    new String[] {
                        e.getEtat(),
                        e.getIntitule(),
                        e.getId(),
                        e.getMinistere(),
                        e.getAuteur(),
                        e.getDerniereModif()
                    }
            )
            .collect(Collectors.toList());

        return STCsvUtils.writeAllLines("modeles", dataLines);
    }
}
