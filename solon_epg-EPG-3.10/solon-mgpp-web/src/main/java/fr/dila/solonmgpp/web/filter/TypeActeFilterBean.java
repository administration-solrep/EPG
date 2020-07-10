package fr.dila.solonmgpp.web.filter;

import static org.jboss.seam.annotations.Install.FRAMEWORK;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonmgpp.web.dossier.DossierCreationActionsBean;

/**
 * @see fr.dila.solonepg.web.filter.TypeActeFilterBean
 * 
 * @author asatre
 */
@Name("typeActeFilter")
@Scope(ScopeType.EVENT)
@Install(precedence = FRAMEWORK + 1)
public class TypeActeFilterBean extends fr.dila.solonepg.web.filter.TypeActeFilterBean {

    private static final long serialVersionUID = -6748502557921813272L;

    @In(required = true, create = true)
    protected NuxeoPrincipal currentUser;

    @In(required = true, create = true)
    protected DossierCreationActionsBean dossierCreationActions;

    final ActeService acteService = SolonEpgServiceLocator.getActeService();

    @Override
    public boolean accept(DocumentModel doc) {
        if (dossierCreationActions.isCaseTraiter()) {
            // pour l'action "Traiter", on peut créer que des lois
            return acteService.getLoiList().contains(doc.getId());
        } else if (dossierCreationActions.isCasePublier()) {
            // pour l'action "Publier", on peut créer que des depots rapports
            return TypeActeConstants.TYPE_ACTE_RAPPORT_AU_PARLEMENT.equals(doc.getId());
        } else {
            if (acteService.hasTypeActeMesureNominative(doc.getId())) {
                final List<String> groups = currentUser.getGroups();
                return groups.contains(SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVE_READER);
            }
			try {
				String displayed = (String) doc.getProperty(VocabularyConstants.VOCABULARY_TYPE_ACTE_SCHEMA,
						"displayed");
				return displayed.equals("1");
			} catch (ClientException e) {
			}
        }

        return Boolean.TRUE;
    }
}
