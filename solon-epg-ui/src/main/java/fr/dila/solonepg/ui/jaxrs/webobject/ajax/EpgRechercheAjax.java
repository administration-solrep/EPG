package fr.dila.solonepg.ui.jaxrs.webobject.ajax;

import static fr.dila.solonepg.ui.enums.EpgUserSessionKey.CRITERE_RECHERCHE_KEY;
import static fr.dila.solonepg.ui.jaxrs.webobject.page.recherche.EpgRechercheLibre.OPTIONS_RESULTAT_PAGE;
import static fr.dila.solonepg.ui.jaxrs.webobject.page.recherche.EpgRechercheLibre.OPTIONS_TRI;
import static fr.dila.solonepg.ui.jaxrs.webobject.page.recherche.EpgRechercheLibre.getOptionsResultatPage;
import static fr.dila.solonepg.ui.jaxrs.webobject.page.recherche.EpgRechercheLibre.getOptionsTri;
import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getEpgRechercheUIService;

import com.google.common.collect.ImmutableMap;
import fr.dila.solonepg.ui.bean.recherchelibre.CritereRechercheForm;
import fr.dila.solonepg.ui.bean.recherchelibre.RechercheDossierList;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgRechercheUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.SolonEpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.ss.ui.jaxrs.webobject.ajax.SSRechercheAjax;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "RechercheAjax")
public class EpgRechercheAjax extends SSRechercheAjax {
    // Critères
    private static final String VECTEUR_PUBLICATION = "vecteur-publication";
    private static final String TYPE_ACTE = "type-acte";
    private static final String CATEGORIE_ACTE = "categorie-acte";
    private static final String STATUT = "statut";
    private static final String MINISTERE_ATTACHE = "ministere-attache";
    private static final String DIRECTION_ATTACHE = "direction-attache";
    private static final String STATUT_ARCHIVAGE = "statut-archivage";

    private static final String NOR_KEY = "NOR";

    public EpgRechercheAjax() {
        suggestionFunctions.put(
            NOR_KEY,
            input -> EpgUIServiceLocator.getNumeroNorSuggestionProviderService().getSuggestions(input, context)
        );
    }

    private static final ImmutableMap<String, Function<CritereRechercheForm, List<SelectValueDTO>>> TYPE_FUNCTION = ImmutableMap
        .<String, Function<CritereRechercheForm, List<SelectValueDTO>>>builder()
        .put(VECTEUR_PUBLICATION, CritereRechercheForm::getVecteurPublication)
        .put(TYPE_ACTE, CritereRechercheForm::getTypeActe)
        .put(CATEGORIE_ACTE, CritereRechercheForm::getCategorieActe)
        .put(STATUT, CritereRechercheForm::getStatut)
        .put(MINISTERE_ATTACHE, CritereRechercheForm::getMinistereAttache)
        .put(DIRECTION_ATTACHE, CritereRechercheForm::getDirectionAttache)
        .put(STATUT_ARCHIVAGE, CritereRechercheForm::getStatutArchivage)
        .build();

    /**
     * Mettre en session les facettes
     *
     * @param type
     * @param value
     * @param isChecked
     * @return
     */
    @POST
    @Path("facette")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addOrRemoveItemFacet(
        @FormParam("type") String type,
        @FormParam("id") String id,
        @FormParam("value") String value,
        @FormParam("isChecked") Boolean isChecked
    ) {
        // je récupère les critères s'ils existent
        CritereRechercheForm critereRechercheForm = ObjectHelper.requireNonNullElseGet(
            UserSessionHelper.getUserSessionParameter(context, CRITERE_RECHERCHE_KEY),
            CritereRechercheForm::new
        );

        // j'ajoute ou je retire l'élément
        SelectValueDTO selectValueDTO = new SelectValueDTO(id, value);
        addOrRemoveIfExists(TYPE_FUNCTION.get(type), critereRechercheForm, selectValueDTO, isChecked);

        critereRechercheForm.setPage(1); // reset la pagination

        // Comme je recharge la page si pas d'erreur je met en session les critères pour les récupérer au rechargement
        UserSessionHelper.putUserSessionParameter(context, CRITERE_RECHERCHE_KEY, critereRechercheForm);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    /**
     * Mettre en session les filtres
     *
     * @param form
     * @return
     */
    @POST
    @Path("filtre")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addOrRemoveFilters(@SwBeanParam CritereRechercheForm form) {
        // je récupère les critères s'ils existent
        CritereRechercheForm critereRechercheForm = ObjectHelper.requireNonNullElseGet(
            UserSessionHelper.getUserSessionParameter(context, CRITERE_RECHERCHE_KEY),
            CritereRechercheForm::new
        );

        critereRechercheForm.setPosteId(form.getPosteId());
        critereRechercheForm.setPosteEnCours(form.getPosteEnCours());
        critereRechercheForm.setDateDebutCreation(form.getDateDebutCreation());
        critereRechercheForm.setDateFinCreation(form.getDateFinCreation());
        critereRechercheForm.setDateDebutPublication(form.getDateDebutPublication());
        critereRechercheForm.setDateFinPublication(form.getDateFinPublication());
        critereRechercheForm.setDateDebutSignature(form.getDateDebutSignature());
        critereRechercheForm.setDateFinSignature(form.getDateFinSignature());
        critereRechercheForm.setDateDebutPublicationJO(form.getDateDebutPublicationJO());
        critereRechercheForm.setDateFinPublicationJO(form.getDateFinPublicationJO());

        critereRechercheForm.setPage(1); // reset la pagination

        // Comme je recharge la page si pas d'erreur je met en session les critères pour les récupérer au rechargement
        UserSessionHelper.putUserSessionParameter(context, CRITERE_RECHERCHE_KEY, critereRechercheForm);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    /**
     * Mettre en session les autres critères
     *
     * @param form
     * @return
     */
    @POST
    @Path("rechercher")
    @Produces(MediaType.APPLICATION_JSON)
    public Response doRecherche(@SwBeanParam CritereRechercheForm form) {
        context.putInContextData(EpgContextDataKey.CRITERE_RECHERCHE_FORM, form);
        getEpgRechercheUIService().getUpdatedCritereRechercheForm(context);
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    /**
     * Mettre en session la page, le nombre de résultats par page et le tri
     *
     * @param page
     * @param size
     * @param sort
     * @return
     */
    @POST
    @Path("resultat")
    public ThTemplate getResults(
        @FormParam("page") Integer page,
        @FormParam("size") int size,
        @FormParam("sort") String sort
    ) {
        // je récupère les critères s'ils existent
        CritereRechercheForm critereRechercheForm = ObjectHelper.requireNonNullElseGet(
            UserSessionHelper.getUserSessionParameter(context, CRITERE_RECHERCHE_KEY),
            CritereRechercheForm::new
        );

        critereRechercheForm.setPage(page);
        critereRechercheForm.setSize(size);
        critereRechercheForm.setSort(sort);

        // Je met en session les critères pour les récupérer plus tard
        UserSessionHelper.putUserSessionParameter(context, CRITERE_RECHERCHE_KEY, critereRechercheForm);

        ThTemplate template = new AjaxLayoutThTemplate(
            "fragments/components/recherchelibre/recherche-libre-zone-recherche-resultats",
            context
        );

        context.putInContextData(EpgContextDataKey.CRITERE_RECHERCHE_FORM, critereRechercheForm);

        EpgRechercheUIService epgRechercheUIService = EpgUIServiceLocator.getEpgRechercheUIService();
        RechercheDossierList rechercheDossierList = epgRechercheUIService.doRechercheLibre(context);

        // Map pour mon contenu spécifique
        Map<String, Object> map = new HashMap<>();
        // Résultats
        map.put(STTemplateConstants.RESULT_LIST, rechercheDossierList);
        UserSessionHelper.putUserSessionParameter(
            context,
            STTemplateConstants.RESULT_LIST,
            rechercheDossierList.getListe()
        );
        map.put(STTemplateConstants.RESULT_FORM, critereRechercheForm);
        UserSessionHelper.putUserSessionParameter(context, STTemplateConstants.RESULT_FORM, critereRechercheForm);

        // options de tri
        map.put(OPTIONS_TRI, getOptionsTri());
        // options de résultats par page
        map.put(OPTIONS_RESULTAT_PAGE, getOptionsResultatPage());

        template.setData(map);

        return template;
    }

    /**
     * Ajouter ou retirer un élément dans une liste
     *
     * @param list
     * @param value
     * @param isChecked
     */
    public void addOrRemoveIfExists(List<String> list, String value, Boolean isChecked) {
        if (Boolean.TRUE.equals(isChecked)) {
            if (!list.contains(value)) {
                list.add(value);
            }
        } else {
            list.remove(value);
        }
    }

    /**
     * Ajouter ou retirer un élément dans une liste
     *
     * @param list
     * @param value
     * @param isChecked
     */
    public void addOrRemoveIfExists(
        Function<CritereRechercheForm, List<SelectValueDTO>> listFunc,
        CritereRechercheForm form,
        SelectValueDTO value,
        Boolean isChecked
    ) {
        if (listFunc == null) {
            return;
        }

        List<SelectValueDTO> list = listFunc.apply(form);
        if (Boolean.TRUE.equals(isChecked)) {
            if (!list.contains(value)) {
                list.add(value);
            }
        } else {
            list.remove(value);
        }
    }

    /**
     * Supprimer les Id en session
     * @return JsonResponse
     */
    @POST
    @Path("reinit")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reinit() {
        // Supprimer les critères en session
        UserSessionHelper.putUserSessionParameter(context, CRITERE_RECHERCHE_KEY, null);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @GET
    @Path("libre")
    public ThTemplate doListe(@QueryParam("page") Integer page) {
        // Je déclare mon template et j'instancie mon context
        ThTemplate template = new AjaxLayoutThTemplate();
        template.setName("fragments/components/recherchelibre/recherche-libre-zone-recherche-resultats");
        template.setContext(context);

        // Map pour mon contenu spécifique
        Map<String, Object> map = new HashMap<>();

        CritereRechercheForm critereRechercheForm = UserSessionHelper.getUserSessionParameter(
            context,
            STTemplateConstants.RESULT_FORM,
            CritereRechercheForm.class
        );
        context.putInContextData(EpgContextDataKey.CRITERE_RECHERCHE_FORM, critereRechercheForm);
        critereRechercheForm.setPage(page);

        EpgRechercheUIService epgRechercheUIService = EpgUIServiceLocator.getEpgRechercheUIService();
        RechercheDossierList rechercheDossierList = epgRechercheUIService.doRechercheLibre(context);

        map.put(STTemplateConstants.RESULT_LIST, rechercheDossierList);
        map.put(STTemplateConstants.RESULT_FORM, critereRechercheForm);

        template.setData(map);

        return template;
    }

    @Path("rapide")
    public Object getRapideResults(@SwBeanParam DossierListForm form) {
        form = ObjectHelper.requireNonNullElseGet(form, DossierListForm::newForm);

        ThTemplate template = buildTemplateRapideSearch(form);
        return newObject("AppliEpgRapideResultats", context, template);
    }

    @Path("derniers/resultats")
    public Object doDerniersResultatsConsultes() {
        return newObject("DerniersResultatsConsultesAjax", context, template);
    }

    @Path("exporter")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response export() {
        SolonEpgUIServiceLocator.getSolonEpgDossierListUIService().exportDossiersRechercheRapide(context);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Path("users")
    public Object getUsers() {
        return newObject("EpgRechercheUsersAjax", context, template);
    }
}
