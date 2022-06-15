package fr.dila.solonepg.ui.services.impl;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.ui.bean.RechercheItemDTO;
import fr.dila.solonepg.ui.services.EpgRechercheMenuService;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.HashMap;
import java.util.Map;

public class EpgRechercheMenuServiceImpl implements EpgRechercheMenuService {
    private static final String ICONE_RECHERCHE = "icon--search";
    private static final String ICONE_DOSSIER = "icon--folder";
    private static final String ICONE_ORGANIGRAMME = "icon--tree";
    private static final String ICONE_UTILISATEUR = "icon--user";
    private static final String LABEL_RECHERCHE = "recherche.menu.recherche";
    private static final String LABEL_DERNIERS_RESULTATS = "recherche.menu.derniers.resultats";
    private static final String LABEL_FAVORIS_CONSULTATIONS = "recherche.menu.favoris.consultation";
    private static final String LABEL_FAVORIS_RECHERCHE = "recherche.menu.favoris.recherche";
    private static final String LABEL_CONSULTER_ORGANIGRAMME = "recherche.menu.consulter.organigramme";
    private static final String LABEL_CONSULTER_ANNUAIRE = "recherche.menu.consulter.annuaire";
    private static final String LABEL_DOSSIER = "recherche.menu.dossiers";
    private static final String LABEL_FDR = "recherche.menu.modeles.fdr";
    private static final String LABEL_UTILISATEURS = "recherche.menu.utilisateurs";

    private static final RechercheItemDTO RECHERCHE = new RechercheItemDTO(
        LABEL_RECHERCHE,
        ICONE_RECHERCHE,
        "#",
        true,
        ImmutableList.of(
            new RechercheItemDTO(LABEL_FDR, ICONE_RECHERCHE, "/recherche/fdr/rechercher#main_content", false, null),
            new RechercheItemDTO(
                LABEL_UTILISATEURS,
                ICONE_RECHERCHE,
                EpgURLConstants.URL_SEARCH_USER + "#main_content",
                false,
                null
            )
        )
    );

    private static final RechercheItemDTO DERNIERS_RESULTATS = new RechercheItemDTO(
        LABEL_DERNIERS_RESULTATS,
        ICONE_DOSSIER,
        "#",
        true,
        ImmutableList.of(
            new RechercheItemDTO(
                LABEL_DOSSIER,
                ICONE_DOSSIER,
                String.format(EpgURLConstants.URL_PAGE_DERNIERS_RESULTATS, "dossier#main_content"),
                false,
                null
            ),
            new RechercheItemDTO(
                LABEL_FDR,
                ICONE_DOSSIER,
                String.format(EpgURLConstants.URL_PAGE_DERNIERS_RESULTATS, "mfdr#main_content"),
                false,
                null
            ),
            new RechercheItemDTO(
                LABEL_UTILISATEURS,
                ICONE_DOSSIER,
                String.format(EpgURLConstants.URL_PAGE_DERNIERS_RESULTATS, "utilisateur#main_content"),
                false,
                null
            )
        )
    );

    private static final RechercheItemDTO FAVORIS_CONSULTATION = new RechercheItemDTO(
        LABEL_FAVORIS_CONSULTATIONS,
        ICONE_DOSSIER,
        "#",
        true,
        ImmutableList.of(
            new RechercheItemDTO(LABEL_DOSSIER, ICONE_DOSSIER, "/recherche/favoris/dossiers#main_content", false, null),
            new RechercheItemDTO(LABEL_FDR, ICONE_DOSSIER, "/recherche/favoris/fdr#main_content", false, null),
            new RechercheItemDTO(
                LABEL_UTILISATEURS,
                ICONE_DOSSIER,
                "/recherche/favoris/utilisateurs#main_content",
                false,
                null
            )
        )
    );

    private static final RechercheItemDTO FAVORIS_RECHERCHE = new RechercheItemDTO(
        LABEL_FAVORIS_RECHERCHE,
        ICONE_DOSSIER,
        "/recherche/favoris/liste#main_content",
        true,
        null
    );

    private static final RechercheItemDTO CONSULTER_ORGANIGRAMME = new RechercheItemDTO(
        LABEL_CONSULTER_ORGANIGRAMME,
        ICONE_ORGANIGRAMME,
        "/admin/organigramme/consult#main_content",
        true,
        null
    );

    private static final RechercheItemDTO CONSULTER_ANNUAIRE = new RechercheItemDTO(
        LABEL_CONSULTER_ANNUAIRE,
        ICONE_UTILISATEUR,
        EpgURLConstants.URL_ANNUAIRE,
        true,
        null
    );

    @Override
    public Map<String, Object> getData(SpecificContext context) {
        Map<String, Object> mapData = new HashMap<>();

        mapData.put(
            "categoryDto",
            ImmutableList.of(
                RECHERCHE,
                DERNIERS_RESULTATS,
                FAVORIS_CONSULTATION,
                FAVORIS_RECHERCHE,
                CONSULTER_ORGANIGRAMME,
                CONSULTER_ANNUAIRE
            )
        );
        return mapData;
    }
}
