import java.util.Collections;
import org.nuxeo.common.utils.IdUtils;

import java.util.Collections;
import org.nuxeo.common.utils.*;
import org.nuxeo.ecm.core.api.*;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.common.utils.IdUtils;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.query.FlexibleQueryMaker;
import fr.dila.st.api.service.SecurityService;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgAclConstant;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import org.nuxeo.ecm.core.api.security.ACP;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import fr.dila.st.core.service.STServiceLocator;
import org.apache.commons.lang.StringUtils;

// Script groovy pour affecter les permissions de rattachement après le script des directions manquantes
public static final String INPUT_FILE = "/tmp/rattachementDossier/migration_groovy.txt";


void rattacherDossier(dossier,securityService, directionRattachementId, ministereRattachementId){
	if (dossier !=null){
		DocumentModel dossierDoc = dossier.getDocument();
	    ACP acp = dossierDoc.getACP();
	    // Les administrateurs du ministère de rattachement voient le dossier
        if (!StringUtils.isEmpty(ministereRattachementId)) {
            acp.removeACL(SolonEpgAclConstant.RATTACHEMENT_ACL);
            securityService.addAceToAcp(acp, SolonEpgAclConstant.RATTACHEMENT_ACL, SolonEpgAclConstant.DOSSIER_RATTACH_MIN_ACE_PREFIX
                    + ministereRattachementId, SecurityConstants.READ_WRITE);
        }
	
        // Les administrateurs de la direction de rattachement voient le dossier
        if (!StringUtils.isEmpty(directionRattachementId)) {
            securityService.addAceToAcp(acp, SolonEpgAclConstant.RATTACHEMENT_ACL, SolonEpgAclConstant.DOSSIER_RATTACH_DIR_ACE_PREFIX
                    + directionRattachementId, SecurityConstants.READ_WRITE);
        }

        Session.setACP(dossierDoc.getRef(), acp, true);
	}
	else{
		System.out.println("Erreur, le dossier est nul");
	}
}

final SecurityService securityService = STServiceLocator.getSecurityService();


new File(INPUT_FILE).splitEachLine(" ") { fields ->

	String selectDossierPourRattachement = "sql:[id]SELECT D.ID AS ID FROM DOSSIER_SOLON_EPG D WHERE D.DIRECTIONRESP = " + fields[0] + " AND D.MINISTERERESP = " + fields[1];
	def dossiers = Session.queryAndFetch(selectDossierPourRattachement, FlexibleQueryMaker.QUERY_TYPE, null);
	dossiers.each{
		def doc = Session.getDocument(new IdRef(it["id"]));
		def dossier = doc.getAdapter(Dossier.class);
		System.out.println("Rattachement : " + it["id"] + " - " + dossier.getNumeroNor()  + " - resp_dir " + fields[0] + " - resp_min "+ fields[1] + " - future_ratt_dir " + fields[2] +  " - " + " - future_ratt_min "  + fields[3]);
		rattacherDossier(dossier,securityService,fields[2],fields[3]);
				//System.out.println(dossier.getNumeroNor() + " vers " + fields[2] + " - " + fields[3]);
	}
	dossiers.close();
}


return "Fin de script"
