/**
 * 
 * Surcharge le beam seam SmartNXQLQueryActions du package smartSearch de Nuxeo,
 * afin d'apporter d'avantages de fonctionnalité.
 * Correction de problème d'échappement dans la requête et traduction d'une requête en language
 * utilisateur.
 *
 */
package fr.dila.solonepg.web.requeteur;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.core.util.FullTextUtil;
import fr.dila.solonepg.core.util.RequeteurUtils;
import fr.dila.st.api.recherche.QueryAssembler;
import fr.dila.st.api.service.RequeteurService;
import fr.dila.st.core.requeteur.STIncrementalSmartNXQLQuery;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.web.requeteur.STSmartNXQLQueryActions;


/**
 * Surcharge du bean seam SmartNXQLQueryActions,
 * initialement pour écraser la méthode initCurrentSmartQuery(String existingQueryPart)
 * et mettre une nouvelle implementation de IncrementalSmartNXQLQuery.
 *
 * @since 5.4
 * @author jgomez
 **/

@Name("smartNXQLQueryActions")
@Scope(ScopeType.CONVERSATION)
@Install(precedence = Install.APPLICATION + 2)
public class EPGSmartNXQLQueryActions extends STSmartNXQLQueryActions implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String completeQuery;
    
    @In(create = true)
    protected transient ResourcesAccessor resourcesAccessor;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;
    
    @In(create = true, required = true)
    protected transient CoreSession documentManager;
    
    private static final Log LOGGER = LogFactory.getLog(EPGSmartNXQLQueryActions.class);
    
    
    public void initCurrentSmartQuery(String existingQueryPart,boolean resetHistory) {
    	LOGGER.debug("Initialisation requêteur d'EPG ");
        super.initCurrentSmartQuery(existingQueryPart,resetHistory);
        currentSmartQuery = new STIncrementalSmartNXQLQuery(existingQueryPart);
        requeteur.updateTranslation();
    }
    
    public String getFullQuery(){
    	QueryAssembler assembler = RequeteurUtils.getDossierAssembler();
    	RequeteurService requeteurService = STServiceLocator.getRequeteurService();
        String fullQuery = requeteurService.getPattern(documentManager, assembler, getQueryPart());
        //la recherche NOR ne doit pas commencer par '*' : on ne veut pas que l'utilisateur puisse rechercher tous les dossiers.
        if(FullTextUtil.beginWithStarQuery(fullQuery)){
            LOGGER.debug("Query before star error :" + fullQuery);
            fullQuery = DossierSolonEpgConstants.QUERY_BEGIN_STAR_ERROR;
        }         
		// pour toutes les sous-requêtes sur le NOR on convertit les ';' en 'OR'
		Pattern norPattern = Pattern.compile("d.dos:numeroNor [^'\"]* ['\"][^'\"]*['\"]");
		Matcher norMatcher = norPattern.matcher(fullQuery);
		while (norMatcher.find()) {
			String norQueryOld = norMatcher.group();
			String[] norQueryParts = norQueryOld.split("'|\"", 2);
			if (norQueryParts[1].contains(";")) {
				String[] nors = norQueryParts[1].toUpperCase().substring(0, norQueryParts[1].length() - 1).split(";");
				String norQueryNew = "";
				for (String nor : nors) {
					if (!nor.trim().isEmpty()) {
						norQueryNew += norQueryParts[0] + "\"" + nor.trim() + "\"";
						if(norQueryParts[0].contains("NOT") || norQueryParts[0].contains("!="))
							norQueryNew += " AND ";
						else
							norQueryNew += " OR ";
					}
				}
				if (!norQueryNew.isEmpty()) {
					norQueryNew = "(" + norQueryNew.substring(0, norQueryNew.length() - 4) + ")";
					fullQuery = fullQuery.replace(norQueryOld, norQueryNew);
				}
			}
		}
        LOGGER.debug("Requête du requeteur de dossier expert");
        LOGGER.debug(fullQuery);
        return fullQuery;
    }
    
}
