package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.ProfilUtilisateurAdministrationUIService;
import fr.dila.solonepg.ui.services.impl.ProfilUtilisateurAdministrationUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class ProfilUtilisateurAdministrationUIComponent
    extends ServiceEncapsulateComponent<ProfilUtilisateurAdministrationUIService, ProfilUtilisateurAdministrationUIServiceImpl> {

    public ProfilUtilisateurAdministrationUIComponent() {
        super(ProfilUtilisateurAdministrationUIService.class, new ProfilUtilisateurAdministrationUIServiceImpl());
    }
}
