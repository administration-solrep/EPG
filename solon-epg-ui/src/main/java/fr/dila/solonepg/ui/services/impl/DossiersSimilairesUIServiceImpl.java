package fr.dila.solonepg.ui.services.impl;

import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_TYPE_ACTE_XPATH;
import static fr.dila.solonepg.ui.enums.EpgUserSessionKey.DOSSIER_SIMILAIRE_PP;
import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getBordereauUIService;
import static fr.dila.st.ui.helper.STUIPageProviderHelper.getProviderFromContext;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.core.recherche.EpgDossierListingDTOImpl;
import fr.dila.solonepg.ui.bean.dossier.bordereau.EpgDossierSimilaireBordereauDTO;
import fr.dila.solonepg.ui.bean.dossier.similaire.DossierSimilaireDTO;
import fr.dila.solonepg.ui.bean.dossier.similaire.DossierSimilaireList;
import fr.dila.solonepg.ui.bean.dossier.similaire.DossierSimilaireListForm;
import fr.dila.solonepg.ui.contentview.DossierPageProvider;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgPageProviders;
import fr.dila.solonepg.ui.services.DossiersSimilairesUIService;
import fr.dila.st.core.query.QueryHelper;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public class DossiersSimilairesUIServiceImpl implements DossiersSimilairesUIService {

    @Override
    public DossierSimilaireList getDossiersSimilaires(SpecificContext context) {
        DossierPageProvider provider = getPageProvider(context);
        DossierSimilaireList list = getList(provider);
        UserSessionHelper.putUserSessionParameter(context, DOSSIER_SIMILAIRE_PP, provider);
        return list;
    }

    private DossierPageProvider getPageProvider(SpecificContext context) {
        DossierSimilaireListForm form = context.getFromContextData(EpgContextDataKey.DOSSIER_SIMILAIRE_FORM);
        CoreSession session = context.getSession();

        DocumentModel dos = QueryHelper.getDocument(session, form.getDossierId(), DOSSIER_TYPE_ACTE_XPATH);
        String typeActe = dos.getAdapter(Dossier.class).getTypeActe();

        DossierPageProvider provider = form.getPageProvider(
            session,
            EpgPageProviders.DOSSIER_SIMILAIRE_PP.getCvName(),
            "d.",
            Arrays.asList(typeActe, getUFNXQLPart(dos))
        );

        UserSessionHelper.putUserSessionParameter(context, DOSSIER_SIMILAIRE_PP, provider);

        return provider;
    }

    private DossierSimilaireList getList(DossierPageProvider provider) {
        List<Map<String, Serializable>> page = provider.getCurrentPage();

        DossierSimilaireList list = new DossierSimilaireList();
        list.setNbTotal((int) provider.getResultsCount());

        List<DossierSimilaireDTO> dtos = new ArrayList<>();
        for (int i = 0; i < page.size(); i++) {
            dtos.add(toDto(page.get(i), i));
        }

        list.setListe(dtos);

        return list;
    }

    private DossierSimilaireDTO toDto(Map<String, Serializable> doc, int index) {
        EpgDossierListingDTOImpl dossierListingDto = (EpgDossierListingDTOImpl) doc;
        DossierSimilaireDTO dto = new DossierSimilaireDTO();
        dto.setIdDossier(dossierListingDto.getDossierId());
        dto.setAuteur(STServiceLocator.getSTUserService().getUserFullName(dossierListingDto.getAuthor()));
        dto.setIndexCourant(index);
        dto.setNor(dossierListingDto.getNumeroNor());
        dto.setStatut(dossierListingDto.getStatut());
        dto.setTitre(dossierListingDto.getTitreActe());
        return dto;
    }

    private String getUFNXQLPart(DocumentModel baseDossierSimilaire) {
        Dossier dossier = baseDossierSimilaire.getAdapter(Dossier.class);

        List<String> conditions = new ArrayList<>();

        conditions.add(" d.ecm:uuid != '" + baseDossierSimilaire.getId() + "'");

        List<String> rubriques = dossier.getIndexationRubrique();
        if (CollectionUtils.isNotEmpty(rubriques)) {
            StringBuilder builder = new StringBuilder(" d.dos:rubriques IN (\"")
                .append(StringUtils.join(rubriques, "\", \""))
                .append("\")");
            conditions.add(builder.toString());
        }

        List<String> motscles = dossier.getIndexationMotsCles();
        if (CollectionUtils.isNotEmpty(motscles)) {
            StringBuilder builder = new StringBuilder(" d.dos:motscles IN (\"")
                .append(StringUtils.join(motscles, "\", \""))
                .append("\")");
            conditions.add(builder.toString());
        }

        List<String> libre = dossier.getIndexationChampLibre();
        if (CollectionUtils.isNotEmpty(libre)) {
            StringBuilder builder = new StringBuilder(" d.dos:libre IN (\"")
                .append(StringUtils.join(libre, "\", \""))
                .append("\")");
            conditions.add(builder.toString());
        }

        return StringUtils.join(conditions, " AND ");
    }

    @Override
    public EpgDossierSimilaireBordereauDTO getCurrentBordereau(SpecificContext context) {
        DossierPageProvider provider = getProviderFromContext(context, DOSSIER_SIMILAIRE_PP);
        Long currentIndex = context.getFromContextData(EpgContextDataKey.CURRENT_INDEX);
        provider.setCurrentEntryIndex(currentIndex);
        UserSessionHelper.putUserSessionParameter(context, DOSSIER_SIMILAIRE_PP, provider);

        EpgDossierSimilaireBordereauDTO bordereauDTO = getBordereauUIService().getBordereauSimilaire(context);
        bordereauDTO.setHasNextDossier(provider.isNextEntryAvailable());
        bordereauDTO.setHasPreviousDossier(provider.isPreviousEntryAvailable());

        return bordereauDTO;
    }

    @Override
    public DossierSimilaireDTO getNextEntry(SpecificContext context) {
        DossierPageProvider provider = getProviderFromContext(context, DOSSIER_SIMILAIRE_PP);
        return getEntry(context, provider, provider::nextEntry);
    }

    @Override
    public DossierSimilaireDTO getPreviousEntry(SpecificContext context) {
        DossierPageProvider provider = getProviderFromContext(context, DOSSIER_SIMILAIRE_PP);
        return getEntry(context, provider, provider::previousEntry);
    }

    private DossierSimilaireDTO getEntry(
        SpecificContext context,
        DossierPageProvider provider,
        Runnable providerMethod
    ) {
        Long currentIndex = context.getFromContextData(EpgContextDataKey.CURRENT_INDEX);
        provider.setCurrentEntryIndex(currentIndex);
        providerMethod.run();
        UserSessionHelper.putUserSessionParameter(context, DOSSIER_SIMILAIRE_PP, provider);

        EpgDossierListingDTOImpl entry = (EpgDossierListingDTOImpl) provider.getCurrentEntry();
        int newCurrentIndex = provider.getCurrentEntryIndex();
        context.putInContextData(EpgContextDataKey.CURRENT_INDEX, (long) newCurrentIndex);
        return toDto(entry, newCurrentIndex);
    }
}
