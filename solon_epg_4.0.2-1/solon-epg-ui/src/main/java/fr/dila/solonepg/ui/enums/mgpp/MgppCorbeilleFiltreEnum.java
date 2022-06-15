package fr.dila.solonepg.ui.enums.mgpp;

import com.google.common.collect.Lists;
import fr.dila.solonepg.ui.th.bean.MgppCorbeilleContentListForm;
import fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName;
import fr.dila.st.ui.bean.ColonneInfo;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

public enum MgppCorbeilleFiltreEnum {
    COMMUNICATIONS(
        Arrays.asList(),
        Lists.newArrayList(
            MgppFiltreEnum.ETAT_MESSAGE,
            MgppFiltreEnum.ETAT_EVENEMENT,
            MgppFiltreEnum.PRESENCE_PIECE_JOINTE,
            MgppFiltreEnum.OBJET,
            MgppFiltreEnum.ID_DOSSIER,
            MgppFiltreEnum.CODE_LECTURE,
            MgppFiltreEnum.EMETTEUR_EVENEMENT,
            MgppFiltreEnum.DESTINATAIRE_EVENEMENT,
            MgppFiltreEnum.COPIE_EVENEMENT,
            MgppFiltreEnum.TYPE_EVENEMENT,
            MgppFiltreEnum.DATE_EVENEMENT,
            MgppFiltreEnum.ID_EVENEMENT
        ),
        "mgppMessageListPageProvider",
        "",
        "",
        ""
    ),
    PROC_LEC(
        Arrays.asList(MgppCorbeilleName.CONSULTATION_FICHE_LOIS_EXISTANT),
        Lists.newArrayList(
            MgppFiltreEnum.NOR,
            MgppFiltreEnum.INTITULE,
            MgppFiltreEnum.ID_DOSSIER,
            MgppFiltreEnum.DATE_CREATION
        ),
        "ficheLoiPageProvider",
        "fl.floi:",
        "fiche.loi",
        "ufnxql: SELECT fl.ecm:uuid as id FROM FicheLoi as fl "
    ),
    PUBLICATION(
        Lists.newArrayList(
            MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_PUBLICATION_ATTRIB,
            MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_PUBLICATION_FOURN_EPRV,
            MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_PUBLICATION_BON_TIRER,
            MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_PUBLICATION_PUBLI_JO,
            MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_PUBLICATION_ATTRIB_PARLEMENTAIRE
        ),
        Lists.newArrayList(
            MgppFiltreEnum.NOR,
            MgppFiltreEnum.TITRE_ACTE,
            MgppFiltreEnum.DATE_ARRIVEE,
            MgppFiltreEnum.STATUT,
            MgppFiltreEnum.TYPE_ACTE
        ),
        "mgppDossierPageProvider",
        "d.dos:",
        "",
        ""
    ),
    RAPPORT(
        Lists.newArrayList(
            MgppCorbeilleName.CONSULTATION_FICHE_DR_EXISTANT,
            MgppCorbeilleName.CONSULTATION_FICHE_DR_67_EXISTANT
        ),
        Lists.newArrayList(
            MgppFiltreEnum.ID_DOSSIER_FICHE,
            MgppFiltreEnum.TYPE_RAPPORT,
            MgppFiltreEnum.OBJET,
            MgppFiltreEnum.DATE_DEPOT_DR
        ),
        "ficheDRPageProvider",
        "fpd.fpdr:",
        "fiche.presentation.dr",
        "ufnxql: SELECT fpd.ecm:uuid as id FROM FichePresentationDR as fpd "
    ),
    DOCUMENT(
        Arrays.asList(MgppCorbeilleName.CONSULTATION_FICHE_DOC_EXISTANT),
        Lists.newArrayList(MgppFiltreEnum.ID_DOSSIER_FICHE, MgppFiltreEnum.OBJET),
        "ficheDOCPageProvider",
        "fpdo.fpdoc:",
        "fiche.presentation.doc",
        "ufnxql: SELECT fpdo.ecm:uuid as id FROM FichePresentationDOC as fpdo "
    ),
    NOMINATION(
        Arrays.asList(MgppCorbeilleName.CONSULTATION_AVI_EXISTANT),
        Lists.newArrayList(
            MgppFiltreEnum.ID_DOSSIER,
            MgppFiltreEnum.ORG_NOM,
            MgppFiltreEnum.BASE_LEGALE,
            MgppFiltreEnum.DATE_DEBUT,
            MgppFiltreEnum.DATE_FIN
        ),
        "ficheAVIPageProvider",
        "fpa.fpavi:",
        "fiche.presentation.avi",
        "ufnxql: SELECT fpa.ecm:uuid as id FROM FichePresentationAVI as fpa "
    ),
    OEP(
        Arrays.asList(MgppCorbeilleName.CONSULTATION_OEP_EXISTANT),
        Lists.newArrayList(
            MgppFiltreEnum.ID_DOSSIER,
            MgppFiltreEnum.OEP_NOM,
            MgppFiltreEnum.MIN_RATTACH,
            MgppFiltreEnum.TEXTE_REF,
            MgppFiltreEnum.DATE_DEBUT,
            MgppFiltreEnum.DATE_FIN
        ),
        "ficheOEPPageProvider",
        "fpo.fpoep:",
        "fiche.presentation.oep",
        "ufnxql: SELECT fpo.ecm:uuid as id FROM FichePresentationOEP as fpo "
    ),
    AUD(
        Arrays.asList(MgppCorbeilleName.CONSULTATION_FICHE_AUD_EXISTANT),
        Lists.newArrayList(MgppFiltreEnum.ID_DOSSIER_FICHE, MgppFiltreEnum.ORG_NOM),
        "ficheAUDPageProvider",
        "fpau.fpaud:",
        "fiche.presentation.aud",
        "ufnxql: SELECT fpau.ecm:uuid as id FROM FichePresentationAUD as fpau "
    ),
    DECRET(
        Arrays.asList(MgppCorbeilleName.CONSULTATION_FICHE_DECRET_EXISTANT),
        Lists.newArrayList(MgppFiltreEnum.ID_NOR, MgppFiltreEnum.OBJET, MgppFiltreEnum.DATE_PUB),
        "ficheDecretPageProvider",
        "fpde.fpdec:",
        "fiche.presentation.decret",
        "ufnxql: SELECT fpde.ecm:uuid as id FROM FichePresentationDecret as fpde "
    ),
    IE(
        Arrays.asList(MgppCorbeilleName.CONSULTATION_FICHE_IE_EXISTANT),
        Lists.newArrayList(MgppFiltreEnum.ID_PROPOSITION, MgppFiltreEnum.OBJET_IE, MgppFiltreEnum.DATE_TRANS),
        "ficheIEPageProvider",
        "fpi.fpie:",
        "fiche.presentation.ie",
        "ufnxql: SELECT fpi.ecm:uuid as id FROM FichePresentationIE as fpi "
    ),
    JSS(
        Lists.newArrayList(MgppCorbeilleName.CONSULTATION_FICHE_JSS_EXISTANT),
        Lists.newArrayList(MgppFiltreEnum.ID_DOSSIER_FICHE, MgppFiltreEnum.OBJET),
        "ficheJSSPageProvider",
        "fpjs.fpjss:",
        "fiche.presentation.jss",
        "ufnxql: SELECT fpjs.ecm:uuid as id FROM FichePresentationJSS as fpjs "
    ),
    DPG(
        Lists.newArrayList(MgppCorbeilleName.CONSULTATION_FICHE_DPG_EXISTANT),
        Lists.newArrayList(MgppFiltreEnum.ID_DOSSIER_FICHE, MgppFiltreEnum.OBJET),
        "ficheDPGPageProvider",
        "fpdp.fpdpg:",
        "fiche.presentation.dpg",
        "ufnxql: SELECT fpdp.ecm:uuid as id FROM FichePresentationDPG as fpdp "
    ),
    SD(
        Lists.newArrayList(MgppCorbeilleName.CONSULTATION_FICHE_SD_EXISTANT),
        Lists.newArrayList(MgppFiltreEnum.ID_DOSSIER_FICHE, MgppFiltreEnum.OBJET),
        "ficheSDPageProvider",
        "fps.fpsd:",
        "fiche.presentation.sd",
        "ufnxql: SELECT fps.ecm:uuid as id FROM FichePresentationSD as fps "
    ),
    RESOLUTION(
        Arrays.asList(MgppCorbeilleName.CONSULTATION_FICHE_341_EXISTANT),
        Lists.newArrayList(
            MgppFiltreEnum.ID_PROPOSITION,
            MgppFiltreEnum.EMETTEUR,
            MgppFiltreEnum.OBJET,
            MgppFiltreEnum.DATE_DEPOT
        ),
        "fiche341PageProvider",
        "fp3.fp341:",
        "fiche.presentation.341",
        "ufnxql: SELECT fp3.ecm:uuid as id FROM FichePresentation341 as fp3 "
    );

    private final List<MgppFiltreEnum> lstFiltres;
    private final List<MgppCorbeilleName> lstCorbeilles;
    private final String providerName;
    private final String xpathPrefix;
    private final String searchCategory;
    private final String searchQuery;

    MgppCorbeilleFiltreEnum(
        List<MgppCorbeilleName> corbeilles,
        List<MgppFiltreEnum> allFiltres,
        String providerName,
        String xpathPrefix,
        String searchCategory,
        String searchQuery
    ) {
        this.lstFiltres = allFiltres;
        this.lstCorbeilles = corbeilles;
        this.providerName = providerName;
        this.xpathPrefix = xpathPrefix;
        this.searchCategory = searchCategory;
        this.searchQuery = searchQuery;
    }

    public List<MgppFiltreEnum> getLstFiltres() {
        return lstFiltres;
    }

    public List<String> getLstFiltresNuxeoFields() {
        return lstFiltres
            .stream()
            .filter(filtre -> StringUtils.isNotBlank(filtre.getFieldName()))
            .map(filtre -> xpathPrefix + filtre.getFieldName())
            .collect(Collectors.toList());
    }

    public List<MgppCorbeilleName> getLstCorbeilles() {
        return lstCorbeilles;
    }

    public List<ColonneInfo> getLstCorbeillesColonnes(MgppCorbeilleContentListForm form) {
        return lstFiltres
            .stream()
            .map(filtre -> filtre.buildColonne(form.getValue(filtre.getSortName())))
            .collect(Collectors.toList());
    }

    public static MgppCorbeilleFiltreEnum fromCorbeille(MgppCorbeilleName corbeille) {
        return Stream
            .of(values())
            .filter(filtre -> filtre.getLstCorbeilles().contains(corbeille))
            .findFirst()
            .orElse(COMMUNICATIONS);
    }

    public static MgppCorbeilleFiltreEnum fromSearchCategory(String searchCategory) {
        return Stream
            .of(values())
            .filter(filtre -> filtre.getSearchCategory().equals(searchCategory))
            .findFirst()
            .orElse(PROC_LEC);
    }

    public String getProviderName() {
        return providerName;
    }

    public String getXpathPrefix() {
        return xpathPrefix;
    }

    public String getSearchCategory() {
        return searchCategory;
    }

    public String getSearchQuery() {
        return searchQuery;
    }
}
