package fr.dila.solonmgpp.web.fichepresentation;

import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.Observer;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.FichePresentation341;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.solonmgpp.api.domain.FichePresentationDecret;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.solonmgpp.web.decoration.DecorationBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.web.lock.STLockActionsBean;

public abstract class FichePresentationBean extends DecorationBean {

	/**
	 * Logger surcouche socle de log4j
	 */
	private static final STLogger						LOGGER					= STLogFactory
																						.getLog(FichePresentationBean.class);

	private static final String[]						EMPTY_COMPONENTS_IDS	= new String[0];

    /**
     * retourne l'ID du dossier
     * 
     * @param contextBean The Mgpp context bean
     * @return the Dossier Id
     */
    protected String findDossierId(NavigationContextBean contextBean) {

        String idDossier = contextBean.getCurrentIdDossier();

        if (idDossier == null) {

            EvenementDTO evenementDTO = contextBean.getCurrentEvenement();
            DocumentModel dossierDoc = contextBean.getCurrentDocument();

            if (evenementDTO != null) {
                idDossier = evenementDTO.getIdDossier();
            }

            if (idDossier == null) {
                return this.getIdFromAdapter(dossierDoc);
            }
        }

        return idDossier;

    }

    private String getIdFromAdapter(DocumentModel dossierDoc) {
        if (dossierDoc != null) {

            if (dossierDoc.hasSchema(FichePresentationIE.SCHEMA)) {
                return dossierDoc.getAdapter(FichePresentationIE.class).getIdentifiantProposition();
            } else if (dossierDoc.hasSchema(FichePresentationDecret.SCHEMA)) {
                return dossierDoc.getAdapter(FichePresentationDecret.class).getNor();
            } else if (dossierDoc.hasSchema(FichePresentationAVI.SCHEMA)) {
                return dossierDoc.getAdapter(FichePresentationAVI.class).getIdDossier();
            } else if (dossierDoc.hasSchema(FichePresentation341.SCHEMA)) {
                return dossierDoc.getAdapter(FichePresentation341.class).getIdentifiantProposition();
            } else if (dossierDoc.hasSchema(FicheLoi.SCHEMA)) {
                return dossierDoc.getAdapter(FicheLoi.class).getIdDossier();
            } else if (dossierDoc.hasSchema(FichePresentationDR.SCHEMA)) {
                return dossierDoc.getAdapter(FichePresentationDR.class).getIdDossier();
            } else if (dossierDoc.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
                Dossier dossier = dossierDoc.getAdapter(Dossier.class);
                String idDossier = dossier.getIdDossier();
                if (idDossier == null) {
                    idDossier = dossier.getNumeroNor();
                }
                return idDossier;
            }
        }
        return null;
    }

    /**
     * Recherche un composant dans la représentation JSF de la page.
     * 
     * @param parent Composant parent
     * @param identifier Identifiant du composant à rechercher
     * @return Composant trouvé ou null
     */
    protected UIComponent findComponent(UIComponent parent, String identifier) {
        if (identifier.equals(parent.getId())) {
            return parent;
        }
        Iterator<UIComponent> kids = parent.getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent found = findComponent(kids.next(), identifier);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

	/**
	 * Vidage des arbres de composants qui doivent être reconstruits
	 * Nécessaire quand l'arbre de composant n'est pas le même en fonction d'une valeur du modèle
	 * (mis en place car selectOneDirectory possède un composant de visualisation et un composant de modification
	 * et que le rendu était incorrect lors du click sur le lock - on restait en visualisation)
	 */
	@Observer(create = false,
			value = { STLockActionsBean.EVENT_LOCK_DOCUMENTS, STLockActionsBean.EVENT_UNLOCK_DOCUMENTS })
	public void onLockUnlock() {
		String[] names = getLockUnlockComponentIdsToRebuild();
		if (names.length == 0) {
			return;
		}
		for (String name : getLockUnlockComponentIdsToRebuild()) {
			UIComponent component = findComponent(FacesContext.getCurrentInstance().getViewRoot(), name);
			if (component != null) {
				component.getChildren().clear();
			} else {
				if (LOGGER.isDebugEnable()) {
					// tous les beans concernés (oep, avi, aud) sont notifiés quel que soit la page concernée
					// il est donc normal de ne pas trouver le composant quand on n'est pas sur la bonne page.
					LOGGER.debug(STLogEnumImpl.LOG_INFO_TEC,
							String.format("Identifiant %s attendu et non trouvé (peut être normal)", name));
				}
			}
		}
	}

	/**
	 * Le verrouillage / déverrouillage peut nécessiter de recharger des listes d'élément ; un appel
	 * getChildren().clear() est effectué sur tous les composants dont le nom est contenu dans la liste transmise.
	 * 
	 * Implémentation par défaut : liste vide.
	 * 
	 * @return la liste des identifiants de composant à réinitialiser lors d'un lock/unlock
	 */
	protected String[] getLockUnlockComponentIdsToRebuild() {
		return EMPTY_COMPONENTS_IDS;
	}

}
