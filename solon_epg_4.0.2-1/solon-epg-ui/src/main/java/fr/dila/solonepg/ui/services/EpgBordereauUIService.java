package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.bean.dossier.bordereau.EpgBordereauDTO;
import fr.dila.solonepg.ui.bean.dossier.bordereau.EpgDossierSimilaireBordereauDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.bordereau.BordereauCourrierForm;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;

public interface EpgBordereauUIService {
    EpgBordereauDTO getBordereau(SpecificContext context);

    void saveBordereau(SpecificContext context);

    boolean isVisibleCategorieActeConventionCollective(SpecificContext context);

    List<SelectValueDTO> getVecteurPublicationList(SpecificContext context, List<String> vecteurPublication);

    List<SelectValueDTO> getModesParution(CoreSession session);

    String getTitreDirectiveFromEurlex(SpecificContext context);

    EpgDossierSimilaireBordereauDTO getBordereauSimilaire(SpecificContext context);

    List<SelectValueDTO> getTypeActeOptions(SpecificContext context);

    BordereauCourrierForm getBordereauEtCourrier(SpecificContext context);
}
