import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.DocumentModelList;
import java.lang.String;
import org.apache.commons.lang.StringUtils;
import java.util.Collections;
import org.nuxeo.common.utils.*;
import org.nuxeo.ecm.core.api.*;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.common.utils.IdUtils;
import fr.dila.solonepg.api.cases.Dossier;

import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.query.FlexibleQueryMaker;
import fr.dila.st.core.util.UnrestrictedQueryRunner;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.DocumentRef;

import fr.dila.solonepg.api.service.DossierDistributionService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
      
// Script groovy pour valider automatique des etapes de type (ROUTING_TASK_TYPE_PUBLICATION_DILA_JO , ROUTING_TASK_TYPE_FOURNITURE_EPREUVE) pour les dossiers publies

        println("lancement du script de validation automatique des etapes de type (ROUTING_TASK_TYPE_PUBLICATION_DILA_JO , ROUTING_TASK_TYPE_FOURNITURE_EPREUVE) pour les dossiers publies");
        final DossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
        dossierDistributionService.validerAutomatiquementEtapePourPublication(Session);
		
		println("script ok");
		return "Fin du script de validation automatique des etapes";

