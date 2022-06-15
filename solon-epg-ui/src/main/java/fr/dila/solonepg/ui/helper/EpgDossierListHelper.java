package fr.dila.solonepg.ui.helper;

import fr.dila.solonepg.core.recherche.EpgDossierListingDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.contentview.DossierPageProvider;
import fr.dila.solonepg.ui.enums.EpgColumnEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.utils.FiltreUtils;
import fr.dila.st.api.vocabulary.VocabularyEntry;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.bean.ColonneInfo;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.enums.SortOrder;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.core.api.CoreSession;

public class EpgDossierListHelper {

    private EpgDossierListHelper() {
        // Default constructor
    }

    /**
     * Construit un objet DossierList à partir de la liste de DTO docList.
     *
     * @return un objet DossierList
     */
    public static EpgDossierList buildDossierList(
        CoreSession session,
        List<Map<String, Serializable>> docList,
        String titre,
        DossierListForm form,
        List<String> lstUserColumn,
        int total,
        String pageProviderName
    ) {
        EpgDossierList lstResults = new EpgDossierList();
        if (form != null) {
            DossierPageProvider provider = form.getPageProvider(session, pageProviderName, null, new ArrayList<>());
            lstUserColumn.addAll(
                (Collection<String>) provider.getProperties().get(DossierPageProvider.USER_COLUMN_PROPERTY)
            );

            lstResults.buildColonnes(form, lstUserColumn);
        } else {
            lstResults.buildColonnesWithoutTri(lstUserColumn);
        }
        lstResults.setNbTotal(total);
        lstResults.setTitre(titre);

        // On fait le mapping des documents vers notre DTO
        for (Map<String, Serializable> doc : docList) {
            if (doc instanceof EpgDossierListingDTOImpl) {
                EpgDossierListingDTOImpl dto = (EpgDossierListingDTOImpl) doc;

                lstResults.getListe().add(dto);
            }
        }
        return lstResults;
    }

    /**
     * Construit un objet DossierList à partir d'un provider implémentant DossierPageProvider
     */
    public static EpgDossierList getListFromDossierPageProvider(
        SpecificContext context,
        DossierListForm form,
        String providerName,
        String titre
    ) {
        return getListFromDossierPageProvider(context, form, providerName, titre, new ArrayList<>());
    }

    /**
     * Construit un objet DossierList à partir d'un provider implémentant DossierPageProvider, paramètres fournis
     */
    public static EpgDossierList getListFromDossierPageProvider(
        SpecificContext context,
        DossierListForm form,
        String providerName,
        String titre,
        List<Object> params,
        List<String> colonnes
    ) {
        form = ObjectHelper.requireNonNullElseGet(form, DossierListForm::new);
        DossierPageProvider provider = form.getPageProvider(context.getSession(), providerName, null, params);
        List<Map<String, Serializable>> docList = provider.getCurrentPage();
        FiltreUtils.putProviderInfosInContextData(context, providerName, params, titre, provider);

        return buildDossierList(
            context.getSession(),
            docList,
            titre,
            form,
            colonnes,
            (int) provider.getResultsCount(),
            providerName
        );
    }

    /**
     * Construit un objet DossierList à partir d'un provider implémentant DossierPageProvider, paramètres fournis
     */
    public static EpgDossierList getListFromDossierPageProvider(
        SpecificContext context,
        DossierListForm form,
        String providerName,
        String titre,
        List<Object> params
    ) {
        return getListFromDossierPageProvider(context, form, providerName, titre, params, null);
    }

    public static void setUserColumns(
        CoreSession session,
        DossierListForm dossierlistForm,
        EpgDossierList list,
        List<String> lstUserColonnes
    ) {
        setUserColumns(session, dossierlistForm, list, lstUserColonnes, true);
    }

    public static void setUserColumns(
        CoreSession session,
        DossierListForm dossierlistForm,
        EpgDossierList list,
        List<String> lstUserColonnes,
        boolean sortable
    ) {
        List<String> preferenceColumns = SolonEpgServiceLocator
            .getProfilUtilisateurService()
            .getUserEspaceTraitementColumn(session);
        if (CollectionUtils.isNotEmpty(lstUserColonnes)) {
            preferenceColumns.addAll(lstUserColonnes);
        }
        list.buildColonnes(dossierlistForm, preferenceColumns, sortable, true);
        dossierlistForm.setColumnVisibility(list.getListeColonnes());
    }

    public static ColonneInfo buildColonne(
        String label,
        String nxPropName,
        List<String> lstUserVisibleColumns,
        boolean sortable,
        String name,
        SortOrder value,
        Boolean isLabelVisible,
        Integer order
    ) {
        boolean visible = isColumnDisplayed(label, nxPropName, lstUserVisibleColumns);

        return new ColonneInfo(label, sortable, visible, name, value, false, isLabelVisible, order);
    }

    protected static boolean isColumnDisplayed(String label, String nxPropName, List<String> lstUserVisibleColumns) {
        EpgColumnEnum column = EpgColumnEnum.getColumnByLabel(label);
        boolean isColumAlwaysDisplayed = column != null && column.isAlwaysDisplayed();
        boolean isColumnInUserVisibleColumns =
            lstUserVisibleColumns != null && nxPropName != null && lstUserVisibleColumns.contains(nxPropName);
        return isColumAlwaysDisplayed || isColumnInUserVisibleColumns;
    }

    public static List<String> getProviderAdditionalColumns(SpecificContext context) {
        String providerName = context.getFromContextData(EpgContextDataKey.PROVIDER_NAME);
        return SolonEpgProviderHelper.getAdditionalColumns(providerName);
    }

    public static boolean filterColumnsUserCanOptToDisplay(SelectValueDTO selectValue) {
        return filterColumnsUserCanOptToDisplay(selectValue.getId());
    }

    public static boolean filterColumnsUserCanOptToDisplay(VocabularyEntry vocEntry) {
        return filterColumnsUserCanOptToDisplay(vocEntry.getId());
    }

    private static boolean filterColumnsUserCanOptToDisplay(String colId) {
        EpgColumnEnum col = EpgColumnEnum.getColumnByNxPropName(colId);

        return !col.isHiddenInColumnSelectors() && !col.isAlwaysDisplayed();
    }
}
