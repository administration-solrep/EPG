package fr.dila.solonepg.ui.jaxrs.webobject.page.recherche;

import fr.dila.solonepg.ui.constants.EpgUIConstants;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.ss.ui.bean.RequeteExperteDTO;
import fr.dila.ss.ui.jaxrs.webobject.page.SSRequeteExperte;
import fr.dila.st.core.requete.recherchechamp.RechercheChampService;
import fr.dila.st.core.requete.recherchechamp.descriptor.ChampDescriptor;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliRechercheExperte")
public class EpgRechercheExperte extends SolonWebObject implements SSRequeteExperte {
    private static final String CATEGORIES = "categories";
    private static final String IS_FIRST_CHAMP = "isFirstChamp";
    private static final String REQUETE_EXPERTE_DTO = "requeteExperteDTO";

    public EpgRechercheExperte() {
        super();
    }

    public static final String RECHERCHE_EXPERTE_CHAMP_CONTRIB_NAME =
        "fr.dila.solonepg.ui.recherche-experte-champ-contrib";

    @SuppressWarnings("unchecked")
    @GET
    public ThTemplate getHome() {
        template.setName("pages/recherche-experte");
        template.setContext(context);
        context.removeNavigationContextTitle();

        RechercheChampService champService = STServiceLocator.getRechercheChampService();
        Map<String, Object> map = new HashMap<>();

        RequeteExperteDTO requeteExperteDTO = UserSessionHelper.getUserSessionParameter(
            context,
            getDtoSessionKey(context),
            RequeteExperteDTO.class
        );
        if (requeteExperteDTO == null) {
            requeteExperteDTO = new RequeteExperteDTO();
            List<ChampDescriptor> champs = champService.getChamps(RECHERCHE_EXPERTE_CHAMP_CONTRIB_NAME);
            requeteExperteDTO.setChamps(champs);
            UserSessionHelper.putUserSessionParameter(context, getDtoSessionKey(context), requeteExperteDTO);
        } else if (
            UserSessionHelper.getUserSessionParameter(context, getResultsSessionKey(context), Map.class) != null
        ) {
            map.putAll(UserSessionHelper.getUserSessionParameter(context, getResultsSessionKey(context), Map.class));
        }

        map.put(REQUETE_EXPERTE_DTO, requeteExperteDTO);
        map.put(IS_FIRST_CHAMP, requeteExperteDTO.getRequetes().isEmpty());
        map.put(
            CATEGORIES,
            requeteExperteDTO
                .getChamps()
                .stream()
                .map(ChampDescriptor::getCategorie)
                .filter(Objects::nonNull)
                .sorted()
                .distinct()
                .collect(Collectors.toList())
        );
        map.put(STTemplateConstants.ACTION, context.getActions(EpgActionCategory.RECHERCHE_EXPERTE_ACTIONS));
        map.put(EpgTemplateConstants.RECHERCHE_SESSION_KEY, getDtoSessionKey(context));
        template.setData(map);
        return template;
    }

    @Override
    public String getSuffixForSessionKeys(SpecificContext context) {
        return EpgUIConstants.EPG_SUFFIX;
    }
}
