package fr.dila.solonepg.ui.services.pan.impl;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.core.exception.ActiviteNormativeException;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.services.pan.ActiviteNormativeTraitesUIService;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Bean Seam de l'espace d'activite normative (Traites et Accords)
 *
 * @author asatre
 */
public class ActiviteNormativeTraitesUIServiceImpl implements ActiviteNormativeTraitesUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(ActiviteNormativeTraitesUIServiceImpl.class);

    @Override
    public String addToActiviteNormative(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        String titre = context.getFromContextData(PanContextDataKey.TITRE);
        Date dateSignature = context.getFromContextData(PanContextDataKey.DATE_SIGNATURE);
        boolean publication = context.getFromContextData(PanContextDataKey.PUBLICATION);

        String message = "";
        if (StringUtils.isNotEmpty(titre) && dateSignature != null) {
            try {
                SolonEpgServiceLocator
                    .getActiviteNormativeService()
                    .addTraiteToTableauMaitre(titre.trim(), dateSignature, publication, documentManager);
                STServiceLocator
                    .getJournalService()
                    .journaliserActionPAN(
                        documentManager,
                        null,
                        SolonEpgEventConstant.AJOUT_TM_EVENT,
                        SolonEpgEventConstant.AJOUT_TM_COMMENT + " [" + titre.trim() + "]",
                        SolonEpgEventConstant.CATEGORY_LOG_PAN_TRAITES_ACCORD
                    );
            } catch (ActiviteNormativeException e) {
                LOGGER.error(EpgLogEnumImpl.LOG_EXCEPTION_PAN_TEC, e);
                message = ResourceHelper.getString(e.getMessage());
            }
        }

        return message;
    }
}
