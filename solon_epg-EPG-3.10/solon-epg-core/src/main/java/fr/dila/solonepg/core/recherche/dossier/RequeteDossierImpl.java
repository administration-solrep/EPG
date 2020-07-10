package fr.dila.solonepg.core.recherche.dossier;

import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierConstants.DANS_BASE_ARCHIVAGE_INTERMEDIAIRE;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierConstants.DANS_BASE_PRODUCTION;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierConstants.REQUETE_DOSSIER_SCHEMA;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierConstants.SENSIBILITE_CASSE;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.recherche.dossier.RequeteDossier;
import fr.dila.st.core.domain.STDomainObjectImpl;


/**
 * 
 * L'implementation d'une requete de dossier, c'est-à-dire l'objet qui regroupe les propriétés communes
 * aux requêtes de dossier simple et requête experte
 * @author jgomez
 *
 */
public class RequeteDossierImpl extends STDomainObjectImpl implements RequeteDossier{



    private static final long serialVersionUID = 6160682333116646611L;

    public RequeteDossierImpl(DocumentModel doc) {
        super(doc);
    }

    private void setProperty(String propertyName, Object value){
        super.setProperty(REQUETE_DOSSIER_SCHEMA, propertyName, value);
    }
    
    private Boolean getBooleanProperty(String propertyName){
        return super.getBooleanProperty(REQUETE_DOSSIER_SCHEMA, propertyName);
    }
    
    
    @Override
    public void setDansBaseProduction(Boolean dansBaseProduction) {
        setProperty(DANS_BASE_PRODUCTION, dansBaseProduction);
    }

    @Override
    public Boolean getDansBaseProduction() {
        return getBooleanProperty(DANS_BASE_PRODUCTION);
    }

    @Override
    public void setDansBaseArchivageIntermediaire(Boolean dansBaseArchivageIntermediaire) {
        setProperty(DANS_BASE_ARCHIVAGE_INTERMEDIAIRE, dansBaseArchivageIntermediaire);
    }

    @Override
    public Boolean getDansBaseArchivageIntermediaire() {
        return getBooleanProperty(DANS_BASE_ARCHIVAGE_INTERMEDIAIRE);
    }

    @Override
    public void setSensibiliteCasse(Boolean sensibiliteCasse) {
        setProperty(SENSIBILITE_CASSE, sensibiliteCasse);
    }

    @Override
    public Boolean getSensibiliteCasse() {
        return getBooleanProperty(SENSIBILITE_CASSE);
    }

    @Override
    public boolean hasBasesSelected() {
        return getDansBaseArchivageIntermediaire() || getDansBaseProduction();
    }
}
