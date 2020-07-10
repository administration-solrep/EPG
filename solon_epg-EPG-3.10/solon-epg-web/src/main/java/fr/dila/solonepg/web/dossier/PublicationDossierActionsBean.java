package fr.dila.solonepg.web.dossier;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.web.context.NavigationContextBean;
import fr.dila.st.web.lock.STLockActionsBean;

/**
 * Une classe qui permet de gérer les actions relatives au publiation des dossiers.
 * 
 * @author arammal
 */
@Name("publicationDossierActions")
@Scope(ScopeType.PAGE)
public class PublicationDossierActionsBean implements Serializable {
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 1L;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;
    
    @In(create = true, required = false)
    protected transient NavigationContextBean navigationContext;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;
    
    @In(create = true)
    protected transient ResourcesAccessor resourcesAccessor;
    
    @In(create = true, required = false)
    protected transient DossierDistributionActionsBean dossierDistributionActions;

    @In(create = true, required = false)
    protected transient BordereauActionsBean bordereauActions;
    
    @In(create = true, required = false)
    protected transient STLockActionsBean stLockActions;
    
    @In(create = true, required = false)
    protected transient DossierListingActionsBean dossierListingActions;
    
    @In(required = true, create = true)
    protected NuxeoPrincipal currentUser;
    

    
    private String dateJO = "";
    private String numTexteJO = "";
    private String pageJO = "";
    
    
    public String getDateJO() {
        return dateJO;
    }

    public void setDateJO(String dateJO) {
        this.dateJO = dateJO;
    }

    public String getNumTexteJO() {
        return numTexteJO;
    }

    public void setNumTexteJO(String numTexteJO) {
        this.numTexteJO = numTexteJO;
    }

    public String getPageJO() {
        return pageJO;
    }

    public void setPageJO(String pageJO) {
        this.pageJO = pageJO;
    }
    
    
    public void publierDossierStub(DocumentModel dossierDoc) throws ClientException {
        ActiviteNormativeService dossierBordereauService = SolonEpgServiceLocator.getActiviteNormativeService();
        
        RetourDila retourDila = dossierDoc.getAdapter(RetourDila.class);
        
        SimpleDateFormat formatter = DateUtil.simpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar calJO = Calendar.getInstance();
        Long pageJoLoc = 0L;
        try {
            calJO.setTime(formatter.parse(dateJO + " 00:00:00"));
            pageJoLoc = Long.valueOf(pageJO);
        } catch (Exception e) {
            throw new ClientException(e);
        }

        retourDila.setDateParutionJorf(calJO);
        retourDila.setNumeroTexteParutionJorf(numTexteJO);
        retourDila.setPageParutionJorf(pageJoLoc);
        
        dossierBordereauService.setPublicationInfo(retourDila, this.documentManager);

    }
    
    
    public boolean isAfficherBoutonPublicationDossier() throws ClientException
    {
        STParametreService paramService = STServiceLocator.getSTParametreService();
        String flagAffichagePublierDossier = paramService.getParametreValue(documentManager, "flag-affichage-publier-dossier");
        return "true".equalsIgnoreCase(flagAffichagePublierDossier) ;
    }

}
