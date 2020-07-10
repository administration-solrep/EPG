package fr.dila.solonepg.web.recherche;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * 
 * Bean utilisé pour gérer la sélection des bases de données de l'utilisateur
 * @author admin
 *
 */

@Name("rechercheArchivageActions")
@Scope(ScopeType.CONVERSATION)
public class RechercheArchivageActionsBean {

    private static final Log LOGGER = LogFactory.getLog(RecherchePanneauxActionsBean.class);
    
    private Boolean dansBaseProduction;
    
    private Boolean dansBaseArchivageIntermediaire;

    @PostConstruct
    public void init(){
        this.dansBaseProduction = true;
    }
    
    public void setDansBaseProduction(Boolean dansBaseProduction) {
        LOGGER.info("Base Prod " + dansBaseProduction);
        this.dansBaseProduction = dansBaseProduction;
    }

    public Boolean getDansBaseProduction() {
        return dansBaseProduction;
    }
    
    public void setDansBaseArchivageIntermediaire(Boolean dansBaseArchivageIntermediaire) {
        LOGGER.info("Base Archi " + dansBaseArchivageIntermediaire);
        this.dansBaseArchivageIntermediaire = dansBaseArchivageIntermediaire;
    }

    public Boolean getDansBaseArchivageIntermediaire() {
        return dansBaseArchivageIntermediaire;
    }
    
}
