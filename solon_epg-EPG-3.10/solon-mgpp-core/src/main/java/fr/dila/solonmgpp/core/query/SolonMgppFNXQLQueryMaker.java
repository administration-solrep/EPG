package fr.dila.solonmgpp.core.query;

import fr.dila.solonepg.core.query.SolonEpgFNXQLQueryMaker;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.FichePresentation341;
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.domain.Navette;
import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.solonmgpp.api.domain.RepresentantAUD;
import fr.dila.solonmgpp.api.domain.RepresentantAVI;
import fr.dila.solonmgpp.api.domain.RepresentantOEP;

public class SolonMgppFNXQLQueryMaker extends SolonEpgFNXQLQueryMaker {

    static {
        // fiche présentation
        mapTypeSchema.put(FicheLoi.DOC_TYPE, FicheLoi.SCHEMA);
        mapTypeSchema.put(FichePresentationAVI.DOC_TYPE, FichePresentationAVI.SCHEMA);
        mapTypeSchema.put(FichePresentation341.DOC_TYPE, FichePresentation341.SCHEMA);
        mapTypeSchema.put(FichePresentationDR.DOC_TYPE, FichePresentationDR.SCHEMA);
        mapTypeSchema.put(FichePresentationIE.DOC_TYPE, FichePresentationIE.SCHEMA);
        mapTypeSchema.put(FichePresentationOEP.DOC_TYPE, FichePresentationOEP.SCHEMA);
        mapTypeSchema.put(FichePresentationAUD.DOC_TYPE, FichePresentationAUD.SCHEMA);
        // representants
        mapTypeSchema.put(RepresentantAVI.DOC_TYPE, RepresentantAVI.SCHEMA);
        mapTypeSchema.put(RepresentantOEP.DOC_TYPE, RepresentantOEP.SCHEMA);
        mapTypeSchema.put(RepresentantAUD.DOC_TYPE, RepresentantAUD.SCHEMA);
        // Navette
        mapTypeSchema.put(Navette.DOC_TYPE, Navette.SCHEMA);
        // parametre
        mapTypeSchema.put(ParametrageMgpp.DOC_TYPE, ParametrageMgpp.SCHEMA);

    };

    public SolonMgppFNXQLQueryMaker() {
        super();
    }
}
