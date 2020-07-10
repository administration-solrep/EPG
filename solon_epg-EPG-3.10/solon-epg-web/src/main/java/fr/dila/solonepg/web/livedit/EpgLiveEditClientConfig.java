package fr.dila.solonepg.web.livedit;

import java.util.ArrayList;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webapp.liveedit.LiveEditClientConfig;

import fr.dila.solonepg.api.administration.ProfilUtilisateur;
import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;

@Name("liveEditClientConfig")
@Scope(ScopeType.SESSION)
@Install(precedence = Install.APPLICATION)
public class EpgLiveEditClientConfig extends LiveEditClientConfig {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;

	private static final STLogger	LOGGER				= STLogFactory.getLog(EpgLiveEditClientConfig.class);

	@In(create = true, required = false)
	protected transient CoreSession	documentManager;

	/**
	 * ProfilUtilisateur contenant les informations sur le profil utilisateur
	 */
	protected ProfilUtilisateur		profilUtilisateur;

	@Override
	public boolean isMimeTypeLiveEditable(String mimetype) {
		this.clientHasLiveEditInstalled = isLiveEditInstalled();
		return super.isMimeTypeLiveEditable(mimetype);
	}

	@Override
	public boolean isLiveEditInstalled() {
		if (profilUtilisateur == null) {
			reloadParametrage();
		}
		if (profilUtilisateur != null && profilUtilisateur.getPresenceLiveEdit() != null
				&& profilUtilisateur.getPresenceLiveEdit()) {

			if (this.advertizedLiveEditableMimeTypes == null || this.advertizedLiveEditableMimeTypes.isEmpty()) {
				// On autorise les format microsoft office et open office (word/tableur et présentation)
				this.advertizedLiveEditableMimeTypes = new ArrayList<String>();
				this.advertizedLiveEditableMimeTypes.add("application/msword");
				this.advertizedLiveEditableMimeTypes.add("application/vnd.ms-excel");
				this.advertizedLiveEditableMimeTypes.add("application/vnd.ms-powerpoint");
				this.advertizedLiveEditableMimeTypes
						.add("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
				this.advertizedLiveEditableMimeTypes
						.add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				this.advertizedLiveEditableMimeTypes
						.add("application/vnd.openxmlformats-officedocument.presentationml.presentation");
				// formats open office
				this.advertizedLiveEditableMimeTypes
				.add("application/vnd.oasis.opendocument.text");//odt+
				this.advertizedLiveEditableMimeTypes
				.add("application/vnd.oasis.opendocument.spreadsheet");//ods+
				this.advertizedLiveEditableMimeTypes
				.add("application/vnd.oasis.opendocument.presentation");//odp+
				
				this.advertizedLiveEditableMimeTypes
				.add("application/vnd.oasis.opendocument.text-template");//ott
				this.advertizedLiveEditableMimeTypes
				.add("application/vnd.oasis.opendocument.presentation-template");//otp
				this.advertizedLiveEditableMimeTypes
				.add("application/vnd.oasis.opendocument.spreadsheet-template");//ots

			}

			this.clientHasLiveEditInstalled = true;
			return true;
		}

		return super.isLiveEditInstalled();

	}

	public void reloadParametrage() {
		ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator.getProfilUtilisateurService();
		try {
			profilUtilisateur = profilUtilisateurService.getOrCreateCurrentUserProfil(documentManager).getAdapter(
					ProfilUtilisateur.class);
		} catch (ClientException e) {
			LOGGER.error(STLogEnumImpl.FAIL_GET_DOCUMENT_TEC, e);
		}
	}
}
