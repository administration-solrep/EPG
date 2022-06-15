package fr.dila.solonepg.ui.enums.mgpp;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonmgpp.api.constant.SolonMgppActionConstant;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.FichePresentation341;
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.domain.FichePresentationDPG;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.api.domain.FichePresentationJSS;
import fr.dila.solonmgpp.api.domain.FichePresentationSD;
import fr.dila.solonmgpp.core.domain.FichePresentationDRImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationDecretImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationOEPImpl;
import java.util.stream.Stream;

public enum MgppDossiersParlementairesEnum {
    PROCEDURE_LEGISLATIVE(SolonMgppActionConstant.PROCEDURE_LEGISLATIVE, FicheLoi.DOC_TYPE),
    PUBLICATION(SolonMgppActionConstant.PUBLICATION, DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE),
    DEPOT_RAPPORT(SolonMgppActionConstant.DEPOT_DE_RAPPORT, FichePresentationDRImpl.DOC_TYPE),
    DOCUMENTS(SolonMgppActionConstant.AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES, FichePresentationDOC.DOC_TYPE),
    AVIS_NOMINATION(SolonMgppActionConstant.AVIS_NOMINATION, FichePresentationAVI.DOC_TYPE),
    OEP(SolonMgppActionConstant.DESIGNATION_OEP, FichePresentationOEPImpl.DOC_TYPE),
    DEMANDES_AUDITION(SolonMgppActionConstant.DEMANDE_AUDITION, FichePresentationAUD.DOC_TYPE),
    DECRET_PRESIDENT(SolonMgppActionConstant.DECRET, FichePresentationDecretImpl.DOC_TYPE),
    JOURS_SUPPLEMENTAIRES_SEANCE(
        SolonMgppActionConstant.DEMANDE_DE_MISE_EN_OEUVRE_ARTICLE_283C,
        FichePresentationJSS.DOC_TYPE
    ),
    INTERVENTION_EXTERIEURE(SolonMgppActionConstant.INTERVENTION_EXTERIEURE, FichePresentationIE.DOC_TYPE),
    POLITIQUE_GENERALE(SolonMgppActionConstant.DECLARATION_DE_POLITIQUE_GENERALE, FichePresentationDPG.DOC_TYPE),
    SUJET_DETERMINE(SolonMgppActionConstant.DECLARATION_SUR_UN_SUJET_DETERMINE, FichePresentationSD.DOC_TYPE),
    RESOLUTIONS(SolonMgppActionConstant.RESOLUTION_ARTICLE_341, FichePresentation341.DOC_TYPE);

    private final String dossierParlementaire;
    private final String typeFiche;

    MgppDossiersParlementairesEnum(String dossierParlementaire, String typeFiche) {
        this.dossierParlementaire = dossierParlementaire;
        this.typeFiche = typeFiche;
    }

    public String getDossierParlementaire() {
        return dossierParlementaire;
    }

    public String getTypeFiche() {
        return typeFiche;
    }

    public static MgppDossiersParlementairesEnum fromTypeFiche(String typeFiche) {
        return Stream
            .of(values())
            .filter(dossierParl -> dossierParl.getTypeFiche().equals(typeFiche))
            .findFirst()
            .orElse(null);
    }
}
