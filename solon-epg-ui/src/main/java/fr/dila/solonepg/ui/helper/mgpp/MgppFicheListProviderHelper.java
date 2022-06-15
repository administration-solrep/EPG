package fr.dila.solonepg.ui.helper.mgpp;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.ui.contentview.DocumentModelFiltrablePaginatedPageDocumentProvider;
import fr.dila.solonepg.ui.enums.mgpp.MgppCorbeilleFiltreEnum;
import fr.dila.solonepg.ui.enums.mgpp.MgppFiltreEnum;
import fr.dila.solonepg.ui.enums.mgpp.MgppSelectValueType;
import fr.dila.solonepg.ui.th.bean.MgppCorbeilleContentListForm;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.FichePresentation341;
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.domain.FichePresentationDPG;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.solonmgpp.api.domain.FichePresentationDecret;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.api.domain.FichePresentationJSS;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.domain.FichePresentationSD;
import fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName;
import fr.dila.solonmgpp.core.domain.FichePresentationDRImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationDecretImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationOEPImpl;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.enums.SortOrder;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.platform.query.api.PageProvider;

public class MgppFicheListProviderHelper {
    private static final String FICHE_ID = "ficheId";

    private MgppFicheListProviderHelper() {}

    public static void setProviderData(
        MgppCorbeilleContentListForm form,
        MgppCorbeilleName corbeille,
        PageProvider<?> provider,
        Map<String, Serializable> filters
    ) {
        setProviderData(form, MgppCorbeilleFiltreEnum.fromCorbeille(corbeille), provider, filters);
    }

    public static void setProviderData(
        MgppCorbeilleContentListForm form,
        MgppCorbeilleFiltreEnum filtres,
        PageProvider<?> provider,
        Map<String, Serializable> filters
    ) {
        List<SortInfo> sortInfos = buildSortInfos(form, filtres);
        if (!sortInfos.isEmpty()) {
            provider.setSortInfos(sortInfos);
        }

        if (provider instanceof DocumentModelFiltrablePaginatedPageDocumentProvider) {
            ((DocumentModelFiltrablePaginatedPageDocumentProvider) provider).setFiltrableMap(filters);
            filtres
                .getLstFiltresNuxeoFields()
                .forEach(
                    key ->
                        ((DocumentModelFiltrablePaginatedPageDocumentProvider) provider).addFiltrableProperty(
                                key,
                                filters.get(key)
                            )
                );
        }
    }

    private static List<SortInfo> buildSortInfos(
        MgppCorbeilleContentListForm form,
        MgppCorbeilleFiltreEnum corbeilleFiltre
    ) {
        String pathPrefix = corbeilleFiltre.getXpathPrefix();

        final MgppCorbeilleContentListForm finalForm = (form == null ? new MgppCorbeilleContentListForm() : form);

        return corbeilleFiltre
            .getLstFiltres()
            .stream()
            .filter(filtre -> StringUtils.isNotBlank(filtre.getFieldName()))
            .filter(filtre -> getFormValue(finalForm, filtre.getSortName()) != null)
            .map(
                filtre ->
                    new SortInfo(
                        pathPrefix + filtre.getFieldName(),
                        SortOrder.isAscending(getFormValue(finalForm, filtre.getSortName()))
                    )
            )
            .collect(Collectors.toList());
    }

    private static SortOrder getFormValue(MgppCorbeilleContentListForm form, String fieldName) {
        return form.getFormData() != null && form.getFormData().get(fieldName) != null
            ? SortOrder.fromValue(form.getFormData().get(fieldName).toString())
            : null;
    }

    public static Map<String, String> convertDocToMap(DocumentModel doc) {
        Map<String, String> curDocData = new LinkedHashMap<>();
        if (doc.hasSchema(FichePresentationIE.SCHEMA)) {
            curDocData = mapFicheIE(doc.getAdapter(FichePresentationIE.class));
        } else if (doc.hasSchema(FichePresentationDecretImpl.SCHEMA)) {
            curDocData = mapFicheDecret(doc.getAdapter(FichePresentationDecret.class));
        } else if (doc.hasSchema(FichePresentationAVI.SCHEMA)) {
            curDocData = mapFicheAVI(doc.getAdapter(FichePresentationAVI.class));
        } else if (doc.hasSchema(FichePresentationOEPImpl.SCHEMA)) {
            curDocData = mapFicheOEP(doc.getAdapter(FichePresentationOEP.class));
        } else if (doc.hasSchema(FichePresentationAUD.SCHEMA)) {
            curDocData = mapFicheAUD(doc.getAdapter(FichePresentationAUD.class));
        } else if (doc.hasSchema(FichePresentation341.SCHEMA)) {
            curDocData = mapFiche341(doc.getAdapter(FichePresentation341.class));
        } else if (doc.hasSchema(FicheLoi.SCHEMA)) {
            curDocData = mapFicheLoi(doc.getAdapter(FicheLoi.class));
        } else if (doc.hasSchema(FichePresentationDRImpl.SCHEMA)) {
            curDocData = mapFicheDR(doc.getAdapter(FichePresentationDR.class));
        } else if (doc.hasSchema(FichePresentationDOC.SCHEMA)) {
            curDocData = mapFicheDOC(doc.getAdapter(FichePresentationDOC.class));
        } else if (doc.hasSchema(FichePresentationJSS.SCHEMA)) {
            curDocData = mapFicheJSS(doc.getAdapter(FichePresentationJSS.class));
        } else if (doc.hasSchema(FichePresentationDPG.SCHEMA)) {
            curDocData = mapFicheDPG(doc.getAdapter(FichePresentationDPG.class));
        } else if (doc.hasSchema(FichePresentationSD.SCHEMA)) {
            curDocData = mapFicheSD(doc.getAdapter(FichePresentationSD.class));
        } else if (doc.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
            curDocData = mapDossier(doc.getAdapter(Dossier.class));
        }
        return curDocData;
    }

    private static Map<String, String> mapFicheDOC(FichePresentationDOC ficheDoc) {
        Map<String, String> mapDoc = new LinkedHashMap<>();

        mapDoc.put(FICHE_ID, ficheDoc.getDocument().getId());
        mapDoc.put(MgppFiltreEnum.ID_DOSSIER_FICHE.getWidgetName(), ficheDoc.getIdDossier());
        mapDoc.put(MgppFiltreEnum.OBJET.getWidgetName(), ficheDoc.getObjet());

        return mapDoc;
    }

    private static Map<String, String> mapFicheJSS(FichePresentationJSS ficheJSS) {
        Map<String, String> mapDoc = new LinkedHashMap<>();

        mapDoc.put(FICHE_ID, ficheJSS.getDocument().getId());
        mapDoc.put(MgppFiltreEnum.ID_DOSSIER_FICHE.getWidgetName(), ficheJSS.getIdDossier());
        mapDoc.put(MgppFiltreEnum.OBJET.getWidgetName(), ficheJSS.getObjet());

        return mapDoc;
    }

    private static Map<String, String> mapFicheIE(FichePresentationIE ficheIE) {
        Map<String, String> mapDoc = new LinkedHashMap<>();

        mapDoc.put(FICHE_ID, ficheIE.getDocument().getId());
        mapDoc.put(MgppFiltreEnum.ID_PROPOSITION.getWidgetName(), ficheIE.getIdentifiantProposition());
        mapDoc.put(MgppFiltreEnum.OBJET.getWidgetName(), ficheIE.getIntitule());
        mapDoc.put(MgppFiltreEnum.DATE_TRANS.getWidgetName(), SolonDateConverter.DATE_SLASH.format(ficheIE.getDate()));

        return mapDoc;
    }

    private static Map<String, String> mapFicheDecret(FichePresentationDecret ficheDecret) {
        Map<String, String> mapDoc = new LinkedHashMap<>();

        mapDoc.put(FICHE_ID, ficheDecret.getDocument().getId());
        mapDoc.put(MgppFiltreEnum.ID_NOR.getWidgetName(), ficheDecret.getNor());
        mapDoc.put(MgppFiltreEnum.OBJET.getWidgetName(), ficheDecret.getObjet());
        mapDoc.put(
            MgppFiltreEnum.DATE_PUB.getWidgetName(),
            SolonDateConverter.DATE_SLASH.format(ficheDecret.getDateJO())
        );
        return mapDoc;
    }

    private static Map<String, String> mapFicheAVI(FichePresentationAVI ficheAVI) {
        Map<String, String> mapDoc = new LinkedHashMap<>();
        mapDoc.put(FICHE_ID, ficheAVI.getDocument().getId());
        mapDoc.put(MgppFiltreEnum.ID_DOSSIER.getWidgetName(), ficheAVI.getIdDossier());
        mapDoc.put(MgppFiltreEnum.ORG_NOM.getWidgetName(), ficheAVI.getNomOrganisme());
        mapDoc.put(MgppFiltreEnum.BASE_LEGALE.getWidgetName(), ficheAVI.getBaseLegale());
        mapDoc.put(MgppFiltreEnum.DATE_DEBUT.getWidgetName(), SolonDateConverter.DATE_SLASH.format(ficheAVI.getDate()));
        mapDoc.put(
            MgppFiltreEnum.DATE_FIN.getWidgetName(),
            SolonDateConverter.DATE_SLASH.format(ficheAVI.getDateFin())
        );
        return mapDoc;
    }

    private static Map<String, String> mapFicheOEP(FichePresentationOEP ficheOEP) {
        Map<String, String> mapDoc = new LinkedHashMap<>();
        mapDoc.put(FICHE_ID, ficheOEP.getDocument().getId());
        mapDoc.put(MgppFiltreEnum.ID_DOSSIER.getWidgetName(), ficheOEP.getIdDossier());
        mapDoc.put(MgppFiltreEnum.OEP_NOM.getWidgetName(), ficheOEP.getNomOrganisme());
        mapDoc.put(MgppFiltreEnum.MIN_RATTACH.getWidgetName(), ficheOEP.getMinistereRattachement());
        mapDoc.put(MgppFiltreEnum.TEXTE_REF.getWidgetName(), ficheOEP.getTexteRef());
        mapDoc.put(MgppFiltreEnum.DATE_DEBUT.getWidgetName(), SolonDateConverter.DATE_SLASH.format(ficheOEP.getDate()));
        mapDoc.put(
            MgppFiltreEnum.DATE_FIN.getWidgetName(),
            SolonDateConverter.DATE_SLASH.format(ficheOEP.getDateFin())
        );
        return mapDoc;
    }

    private static Map<String, String> mapFicheAUD(FichePresentationAUD ficheAUD) {
        Map<String, String> mapDoc = new LinkedHashMap<>();
        mapDoc.put(FICHE_ID, ficheAUD.getDocument().getId());
        mapDoc.put(MgppFiltreEnum.ID_DOSSIER_FICHE.getWidgetName(), ficheAUD.getIdDossier());
        mapDoc.put(MgppFiltreEnum.ORG_NOM.getWidgetName(), ficheAUD.getNomOrganisme());
        return mapDoc;
    }

    private static Map<String, String> mapFicheLoi(FicheLoi ficheLoi) {
        Map<String, String> mapDoc = new LinkedHashMap<>();

        mapDoc.put(FICHE_ID, ficheLoi.getDocument().getId());
        mapDoc.put(MgppFiltreEnum.NOR.getWidgetName(), ficheLoi.getNumeroNor());
        mapDoc.put(MgppFiltreEnum.INTITULE.getWidgetName(), ficheLoi.getIntitule());
        mapDoc.put(MgppFiltreEnum.ID_DOSSIER.getWidgetName(), ficheLoi.getIdDossier());
        mapDoc.put(
            MgppFiltreEnum.DATE_CREATION.getWidgetName(),
            SolonDateConverter.DATE_SLASH.format(ficheLoi.getDateCreation())
        );

        return mapDoc;
    }

    private static String getLabelFromVoc(String id, MgppSelectValueType selecteur) {
        return selecteur
            .getSelectValues()
            .stream()
            .filter(voc -> voc.getId().equals(id))
            .map(SelectValueDTO::getLabel)
            .findFirst()
            .orElse(null);
    }

    private static Map<String, String> mapFicheDR(FichePresentationDR ficheDR) {
        Map<String, String> mapDoc = new LinkedHashMap<>();
        mapDoc.put(FICHE_ID, ficheDR.getDocument().getId());
        mapDoc.put(MgppFiltreEnum.ID_DOSSIER_FICHE.getWidgetName(), ficheDR.getIdDossier());

        mapDoc.put(
            MgppFiltreEnum.TYPE_RAPPORT.getWidgetName(),
            getLabelFromVoc(ficheDR.getRapportParlement(), MgppSelectValueType.TYPE_RAPPORT)
        );
        mapDoc.put(MgppFiltreEnum.OBJET.getWidgetName(), ficheDR.getObjet());
        mapDoc.put(
            MgppFiltreEnum.DATE_DEPOT_DR.getWidgetName(),
            SolonDateConverter.DATE_SLASH.format(ficheDR.getDateDepotEffectif())
        );

        return mapDoc;
    }

    private static Map<String, String> mapFiche341(FichePresentation341 fiche341) {
        Map<String, String> mapDoc = new LinkedHashMap<>();

        mapDoc.put(FICHE_ID, fiche341.getDocument().getId());
        mapDoc.put(MgppFiltreEnum.ID_PROPOSITION.getWidgetName(), fiche341.getIdentifiantProposition());
        mapDoc.put(MgppFiltreEnum.EMETTEUR.getWidgetName(), fiche341.getAuteur());
        mapDoc.put(MgppFiltreEnum.OBJET.getWidgetName(), fiche341.getObjet());
        mapDoc.put(
            MgppFiltreEnum.DATE_DEPOT.getWidgetName(),
            SolonDateConverter.DATE_SLASH.format(fiche341.getDateDepot())
        );
        return mapDoc;
    }

    private static Map<String, String> mapFicheDPG(FichePresentationDPG ficheDPG) {
        Map<String, String> mapDoc = new LinkedHashMap<>();

        mapDoc.put(FICHE_ID, ficheDPG.getDocument().getId());
        mapDoc.put(MgppFiltreEnum.ID_DOSSIER_FICHE.getWidgetName(), ficheDPG.getIdDossier());
        mapDoc.put(MgppFiltreEnum.OBJET.getWidgetName(), ficheDPG.getObjet());
        return mapDoc;
    }

    private static Map<String, String> mapFicheSD(FichePresentationSD ficheSD) {
        Map<String, String> mapDoc = new LinkedHashMap<>();

        mapDoc.put(FICHE_ID, ficheSD.getDocument().getId());
        mapDoc.put(MgppFiltreEnum.ID_DOSSIER_FICHE.getWidgetName(), ficheSD.getIdDossier());
        mapDoc.put(MgppFiltreEnum.OBJET.getWidgetName(), ficheSD.getObjet());
        return mapDoc;
    }

    private static Map<String, String> mapDossier(Dossier dossier) {
        Map<String, String> mapDoc = new LinkedHashMap<>();

        mapDoc.put(FICHE_ID, dossier.getDocument().getId());
        mapDoc.put(MgppFiltreEnum.NOR.getWidgetName(), dossier.getNumeroNor());
        mapDoc.put(MgppFiltreEnum.TITRE_ACTE.getWidgetName(), dossier.getTitreActe());
        mapDoc.put(
            MgppFiltreEnum.DATE_ARRIVEE.getWidgetName(),
            SolonDateConverter.DATE_SLASH.format(dossier.getDateCreation())
        );
        mapDoc.put(
            MgppFiltreEnum.STATUT.getWidgetName(),
            getLabelFromVoc(dossier.getStatut(), MgppSelectValueType.STATUT)
        );
        mapDoc.put(
            MgppFiltreEnum.TYPE_ACTE.getWidgetName(),
            getLabelFromVoc(dossier.getTypeActe(), MgppSelectValueType.TYPE_ACTE)
        );

        return mapDoc;
    }
}
