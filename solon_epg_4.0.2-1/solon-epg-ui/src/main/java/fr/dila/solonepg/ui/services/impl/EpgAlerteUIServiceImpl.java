package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.ui.bean.recherchelibre.CritereRechercheForm;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgUserSessionKey;
import fr.dila.solonepg.ui.services.EpgAlerteUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.ss.ui.bean.AlerteForm;
import fr.dila.ss.ui.bean.RequeteExperteDTO;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.services.actions.impl.SSAlertActionServiceImpl;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import org.apache.commons.lang3.StringUtils;

public class EpgAlerteUIServiceImpl extends SSAlertActionServiceImpl implements EpgAlerteUIService {

    @Override
    public String saveAlert(SpecificContext context) {
        AlerteForm alerteForm = context.getFromContextData(SSContextDataKey.ALERTE_FORM);

        if (StringUtils.isBlank(alerteForm.getIdRequete())) {
            // création de l'alerte -> il faut enregistrer la requete ES
            saveRequeteES(context);
        }

        return super.saveAlert(context);
    }

    private static void saveRequeteES(SpecificContext context) {
        String idRequete;

        String rechercheExperteKey = context.getFromContextData(EpgContextDataKey.RECHERCHE_EXPERTE_KEY);
        RequeteExperteDTO requeteExperteDto = UserSessionHelper.getUserSessionParameter(
            context,
            rechercheExperteKey,
            RequeteExperteDTO.class
        );

        if (requeteExperteDto != null) {
            // cas d'une recherche experte
            DossierListForm dossierListForm = ObjectHelper.requireNonNullElseGet(
                context.getFromContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM),
                DossierListForm::newForm
            );
            context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierListForm);

            context.putInContextData(SSContextDataKey.REQUETE_EXPERTE_DTO, requeteExperteDto);

            idRequete = EpgUIServiceLocator.getEpgRequeteUIService().saveRequeteExperteES(context);
        } else {
            // cas d'une recherche libre
            CritereRechercheForm critereRechercheForm = context.getFromContextData(
                EpgContextDataKey.CRITERE_RECHERCHE_FORM
            );

            CritereRechercheForm sessionForm = ObjectHelper.requireNonNullElseGet(
                UserSessionHelper.getUserSessionParameter(context, EpgUserSessionKey.CRITERE_RECHERCHE_KEY),
                CritereRechercheForm::new
            );
            if (critereRechercheForm.getBaseArchivage() != null) {
                sessionForm.setBaseArchivage(critereRechercheForm.getBaseArchivage());
            }

            if (critereRechercheForm.getBaseProduction() != null) {
                sessionForm.setBaseProduction(critereRechercheForm.getBaseProduction());
            }

            if (critereRechercheForm.getDerniereVersion() != null) {
                sessionForm.setDerniereVersion(critereRechercheForm.getDerniereVersion());
            }

            if (critereRechercheForm.getRecherche() != null) {
                sessionForm.setRecherche(critereRechercheForm.getRecherche());
            }

            if (critereRechercheForm.getExpressionExacte() != null) {
                sessionForm.setExpressionExacte(critereRechercheForm.getExpressionExacte());
            }

            idRequete = EpgUIServiceLocator.getEpgRequeteUIService().saveRequeteLibreES(context);
        }

        AlerteForm alerteForm = context.getFromContextData(SSContextDataKey.ALERTE_FORM);
        alerteForm.setIdRequete(idRequete);
    }
}
