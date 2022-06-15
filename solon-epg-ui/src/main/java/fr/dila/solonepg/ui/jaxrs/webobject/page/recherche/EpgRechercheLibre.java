package fr.dila.solonepg.ui.jaxrs.webobject.page.recherche;

import static fr.dila.solonepg.ui.enums.EpgUserSessionKey.CRITERE_RECHERCHE_KEY;

import fr.dila.solonepg.elastic.models.search.enums.FacetEnum;
import fr.dila.solonepg.ui.bean.recherchelibre.CategorieActeDTO;
import fr.dila.solonepg.ui.bean.recherchelibre.CritereRechercheForm;
import fr.dila.solonepg.ui.bean.recherchelibre.DirectionAttacheDTO;
import fr.dila.solonepg.ui.bean.recherchelibre.FiltreItemDTO;
import fr.dila.solonepg.ui.bean.recherchelibre.MinistereAttacheDTO;
import fr.dila.solonepg.ui.bean.recherchelibre.RechercheDossierList;
import fr.dila.solonepg.ui.bean.recherchelibre.StatutArchivageDTO;
import fr.dila.solonepg.ui.bean.recherchelibre.StatutDTO;
import fr.dila.solonepg.ui.bean.recherchelibre.TypeActeDTO;
import fr.dila.solonepg.ui.bean.recherchelibre.VecteurPublicationDTO;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgRechercheUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.bean.PaginationForm;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.GET;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliRechercheLibre")
public class EpgRechercheLibre extends SolonWebObject {
    // Critères
    private static final String VECTEUR_PUBLICATION = "vecteurPublication";
    private static final String TYPE_ACTE = "typeActe";
    private static final String CATEGORIE_ACTE = "categorieActe";
    private static final String STATUT = "statut";
    private static final String MINISTERE_ATTACHE = "ministereAttache";
    private static final String DIRECTION_ATTACHE = "directionAttache";
    private static final String STATUT_ARCHIVAGE = "statutArchivage";

    public static final String OPTIONS_TRI = "optionsTri";
    public static final String OPTIONS_TRI_PERTINENCE = "Pertinence";
    public static final String OPTIONS_TRI_DATE_CREATION_ASC = "Date de création (croissant)";
    public static final String OPTIONS_TRI_DATE_CREATION_DESC = "Date de création (décroissant)";
    public static final String OPTIONS_TRI_DATE_PUBLICATION_ASC = "Date de publication (croissant)";
    public static final String OPTIONS_TRI_DATE_PUBLICATION_DESC = "Date de publication (décroissant)";

    public static final String OPTIONS_RESULTAT_PAGE = "optionsResultatPage";

    public EpgRechercheLibre() {}

    @GET
    public ThTemplate getRechercheLibre() {
        template.setName("pages/recherche");
        context.removeNavigationContextTitle();
        template.setContext(context);

        // Map pour mon contenu spécifique
        Map<String, Object> map = new HashMap<>();

        // On récupère les criteres sélectionnés en session (s'il y en a)
        CritereRechercheForm critereRechercheForm = UserSessionHelper.getUserSessionParameter(
            context,
            CRITERE_RECHERCHE_KEY
        );
        critereRechercheForm = ObjectHelper.requireNonNullElseGet(critereRechercheForm, CritereRechercheForm::new);
        context.putInContextData(EpgContextDataKey.CRITERE_RECHERCHE_FORM, critereRechercheForm);

        EpgRechercheUIService epgRechercheUIService = EpgUIServiceLocator.getEpgRechercheUIService();
        RechercheDossierList rechercheDossierList = epgRechercheUIService.doRechercheLibre(context);

        // Récupération des DTO
        VecteurPublicationDTO vecteurPublication = getVecteurPublicationDTO(rechercheDossierList);
        TypeActeDTO typeActe = getTypeActeDTO(rechercheDossierList);
        CategorieActeDTO categorieActe = getCategorieActeDTO(rechercheDossierList);
        StatutDTO statut = getStatutDTO(rechercheDossierList);
        MinistereAttacheDTO ministereAttache = getMinistereAttacheDTO(rechercheDossierList);
        DirectionAttacheDTO directionAttache = getDirectionAttacheDTO(rechercheDossierList);
        StatutArchivageDTO statutArchivage = getStatutArchivageDTO(rechercheDossierList);

        vecteurPublication.setItems(
            noEmptyFacet(critereRechercheForm.getVecteurPublication(), vecteurPublication.getItems())
        );
        typeActe.setItems(noEmptyFacet(critereRechercheForm.getTypeActe(), typeActe.getItems()));
        categorieActe.setItems(noEmptyFacet(critereRechercheForm.getCategorieActe(), categorieActe.getItems()));
        statut.setItems(noEmptyFacet(critereRechercheForm.getStatut(), statut.getItems()));
        ministereAttache.setItems(
            noEmptyFacet(critereRechercheForm.getMinistereAttache(), ministereAttache.getItems())
        );
        directionAttache.setItems(
            noEmptyFacet(critereRechercheForm.getDirectionAttache(), directionAttache.getItems())
        );
        statutArchivage.setItems(noEmptyFacet(critereRechercheForm.getStatutArchivage(), statutArchivage.getItems()));

        // Facettes
        map.put(VECTEUR_PUBLICATION, vecteurPublication); // Vecteur de publication
        map.put(TYPE_ACTE, typeActe); // Type d'acte
        map.put(CATEGORIE_ACTE, categorieActe); // Catégorie d'acte
        map.put(STATUT, statut); // Statut
        map.put(MINISTERE_ATTACHE, ministereAttache); // Ministère de rattachement
        map.put(DIRECTION_ATTACHE, directionAttache); // Direction de rattachement
        map.put(STATUT_ARCHIVAGE, statutArchivage); // Statut d'archivage

        // Poste
        if (StringUtils.isNotBlank(critereRechercheForm.getPosteId())) {
            critereRechercheForm.setMapPoste(
                Collections.singletonMap(
                    critereRechercheForm.getPosteId(),
                    STServiceLocator
                        .getOrganigrammeService()
                        .getOrganigrammeNodeById(critereRechercheForm.getPosteId(), OrganigrammeType.POSTE)
                        .getLabel()
                )
            );
        } else {
            critereRechercheForm.setMapPoste(new HashMap<>());
        }

        if (null == critereRechercheForm.getBaseProduction() && null == critereRechercheForm.getBaseArchivage()) {
            critereRechercheForm.setBaseProduction(true);
        }

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
     * Les options de tri
     * @return
     */
    public static List<String> getOptionsTri() {
        return Stream
            .of(
                OPTIONS_TRI_PERTINENCE,
                OPTIONS_TRI_DATE_CREATION_ASC,
                OPTIONS_TRI_DATE_CREATION_DESC,
                OPTIONS_TRI_DATE_PUBLICATION_ASC,
                OPTIONS_TRI_DATE_PUBLICATION_DESC
            )
            .collect(Collectors.toList());
    }

    /**
     * Les options de résultats par page
     * @return
     */
    public static List<String> getOptionsResultatPage() {
        return PaginationForm.getAllSizes();
    }

    /**
     * Get all VecteurPublicationDTO
     * @return VecteurPublicationDTO
     */
    private VecteurPublicationDTO getVecteurPublicationDTO(RechercheDossierList rechercheDossierList) {
        return new VecteurPublicationDTO(rechercheDossierList.getFacets().get(FacetEnum.VECTEUR_PUBLICATION));
    }

    /**
     * Get all TypeActeDTO
     * @return TypeActeDTO
     */
    private TypeActeDTO getTypeActeDTO(RechercheDossierList rechercheDossierList) {
        return new TypeActeDTO(rechercheDossierList.getFacets().get(FacetEnum.TYPE_ACTE));
    }

    /**
     * Get all CategorieActeDTO
     * @return CategorieActeDTO
     */
    private CategorieActeDTO getCategorieActeDTO(RechercheDossierList rechercheDossierList) {
        return new CategorieActeDTO(rechercheDossierList.getFacets().get(FacetEnum.CATEGORIE_ACTE));
    }

    /**
     * Get all StatutDTO
     * @return StatutDTO
     */
    private StatutDTO getStatutDTO(RechercheDossierList rechercheDossierList) {
        return new StatutDTO(rechercheDossierList.getFacets().get(FacetEnum.STATUT));
    }

    private MinistereAttacheDTO getMinistereAttacheDTO(RechercheDossierList rechercheDossierList) {
        return new MinistereAttacheDTO(rechercheDossierList.getFacets().get(FacetEnum.MINISTERE_ATTACHE));
    }

    private DirectionAttacheDTO getDirectionAttacheDTO(RechercheDossierList rechercheDossierList) {
        return new DirectionAttacheDTO(rechercheDossierList.getFacets().get(FacetEnum.DIRECTION_ATTACHE));
    }

    /**
     * Get all StatutArchivageDTO
     * @return StatutArchivageDTO
     */
    private StatutArchivageDTO getStatutArchivageDTO(RechercheDossierList rechercheDossierList) {
        return new StatutArchivageDTO(rechercheDossierList.getFacets().get(FacetEnum.STATUT_ARCHIVAGE));
    }

    /**
     * Returns the original DTO or a DTO with an empty item for selected facets with empty results
     * @param criteres
     * @param dto
     * @return List<FiltreItemDTO>
     */
    private List<FiltreItemDTO> noEmptyFacet(List<SelectValueDTO> criteres, List<FiltreItemDTO> dto) {
        if (CollectionUtils.isNotEmpty(criteres) && CollectionUtils.isEmpty(dto)) {
            return criteres
                .stream()
                .map(critere -> new FiltreItemDTO(critere.getId(), critere.getLabel(), 0))
                .collect(Collectors.toList());
        } else {
            return dto;
        }
    }
}
