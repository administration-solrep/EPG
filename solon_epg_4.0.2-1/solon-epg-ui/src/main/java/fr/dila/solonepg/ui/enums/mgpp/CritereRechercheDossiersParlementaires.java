package fr.dila.solonepg.ui.enums.mgpp;

import static fr.dila.solonmgpp.api.constant.SolonMgppActionConstant.*;

import com.google.common.collect.ImmutableSet;
import fr.dila.solonepg.ui.bean.RechercheDossiersParlementairesDTO;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum CritereRechercheDossiersParlementaires {
    NOR(
        new RechercheDossiersParlementairesDTO("idDossier", "label.mgpp.recherche.identifiant", true),
        ImmutableSet.of(
            AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES,
            DEMANDE_AUDITION,
            DEMANDE_DE_MISE_EN_OEUVRE_ARTICLE_283C,
            DECLARATION_SUR_UN_SUJET_DETERMINE,
            DECLARATION_DE_POLITIQUE_GENERALE
        )
    ),
    IDENTIFIANT(
        new RechercheDossiersParlementairesDTO("idDossier", "label.mgpp.recherche.identifiant", true),
        ImmutableSet.of(INTERVENTION_EXTERIEURE, DESIGNATION_OEP, AVIS_NOMINATION)
    ),
    NUMERO_NOR(
        new RechercheDossiersParlementairesDTO("numeroNor", "label.mgpp.recherche.nor", true),
        ImmutableSet.of(PUBLICATION, DEPOT_DE_RAPPORT, PROCEDURE_LEGISLATIVE, RESOLUTION_ARTICLE_341, DECRET)
    ),
    OBJET(
        new RechercheDossiersParlementairesDTO("objet", "label.mgpp.recherche.objet", true),
        ImmutableSet.of(
            PROCEDURE_LEGISLATIVE,
            INTERVENTION_EXTERIEURE,
            RESOLUTION_ARTICLE_341,
            DESIGNATION_OEP,
            AVIS_NOMINATION,
            AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES,
            DEMANDE_DE_MISE_EN_OEUVRE_ARTICLE_283C,
            DECLARATION_SUR_UN_SUJET_DETERMINE,
            DECLARATION_DE_POLITIQUE_GENERALE
        )
    ),
    TITRE_ACTE(
        new RechercheDossiersParlementairesDTO("titreActe", "label.mgpp.recherche.objet", true),
        ImmutableSet.of(PUBLICATION)
    ),
    NOM_ORGANISME(
        new RechercheDossiersParlementairesDTO("nomOrganisme", "label.mgpp.recherche.nomOrganisme", true),
        ImmutableSet.of(DEMANDE_AUDITION)
    );

    private final RechercheDossiersParlementairesDTO rechercheDossiersParlementairesDTO;
    private final Set<String> dossiersParlementaires;

    CritereRechercheDossiersParlementaires(
        RechercheDossiersParlementairesDTO rechercheDossiersParlementairesDTO,
        Set<String> dossiersParlementaires
    ) {
        this.rechercheDossiersParlementairesDTO = rechercheDossiersParlementairesDTO;
        this.dossiersParlementaires = dossiersParlementaires;
    }

    public RechercheDossiersParlementairesDTO getRechercheDossiersParlementairesDTO() {
        return rechercheDossiersParlementairesDTO;
    }

    public Set<String> getDossiersParlementaires() {
        return dossiersParlementaires;
    }

    public static List<RechercheDossiersParlementairesDTO> getCriteresRechercheDossiersParlementaires(
        String selectedDossierParlementaire
    ) {
        return Arrays
            .stream(values())
            .filter(critere -> critere.getDossiersParlementaires().contains(selectedDossierParlementaire))
            .map(CritereRechercheDossiersParlementaires::getRechercheDossiersParlementairesDTO)
            .collect(Collectors.toList());
    }
}
