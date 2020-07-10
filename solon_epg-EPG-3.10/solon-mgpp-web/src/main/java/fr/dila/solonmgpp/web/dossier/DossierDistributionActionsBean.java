package fr.dila.solonmgpp.web.dossier;

import static org.jboss.seam.ScopeType.CONVERSATION;

import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.solonmgpp.web.corbeille.CorbeilleActionsBean;

@Name("dossierDistributionActions")
@Scope(CONVERSATION)
@Install(precedence = Install.APPLICATION + 1)
public class DossierDistributionActionsBean extends fr.dila.solonepg.web.dossier.DossierDistributionActionsBean {


    /**
     * 
     */
    private static final long serialVersionUID = -4228709021563325851L;

    @Override
    public String validerEtape() throws ClientException {
        super.validerEtape();
        // refresh de la corbeille courante
        Events.instance().raiseEvent(CorbeilleActionsBean.REFRESH_CORBEILLE);
        return null;
    }
    
    @Override
    public String validerEtapeCorrection() throws ClientException {
        super.validerEtapeCorrection();
        // refresh de la corbeille courante
        Events.instance().raiseEvent(CorbeilleActionsBean.REFRESH_CORBEILLE);
        return null;
    }
    
    @Override
    public String validerEtapeRefus() throws ClientException {
        super.validerEtapeRefus();
        // refresh de la corbeille courante
        Events.instance().raiseEvent(CorbeilleActionsBean.REFRESH_CORBEILLE);
        return null;
    }
    
    @Override
    public String validerEtapeNonConcerne(final boolean ajoutEtape) throws ClientException {
        super.validerEtapeNonConcerne(ajoutEtape);
        // refresh de la corbeille courante
        Events.instance().raiseEvent(CorbeilleActionsBean.REFRESH_CORBEILLE);
        return null;
    }
    
    @Override
    public String validerEtapeRetourModification(final boolean ajoutEtape) throws ClientException {
        super.validerEtapeRetourModification(ajoutEtape);
        // refresh de la corbeille courante
        Events.instance().raiseEvent(CorbeilleActionsBean.REFRESH_CORBEILLE);
        return null;
    }
    
    
}
