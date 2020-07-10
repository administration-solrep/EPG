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
      
      
// Script groovy pour supprimer les dossiers signales qui existe deja dans l'application

        println("lancement du script de suppression des anciens dossiers signales");
       

        String query = "SELECT dsr.ecm:uuid as id from DossiersSignalesRoot as dsr";
        DocumentRef[] docRefs = QueryUtils.doUFNXQLQueryForIds(Session, query, null);
        Session.removeDocuments(docRefs);
        Session.save();
		println("script ok");
		return "Fin du script de suppression des dossiers signales";
