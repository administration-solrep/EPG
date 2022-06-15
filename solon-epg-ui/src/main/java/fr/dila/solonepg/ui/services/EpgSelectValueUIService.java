package fr.dila.solonepg.ui.services;

import fr.dila.ss.ui.services.SSSelectValueUIService;
import fr.dila.st.ui.bean.SelectValueDTO;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;

public interface EpgSelectValueUIService extends SSSelectValueUIService {
    List<SelectValueDTO> getTypeActe();

    List<SelectValueDTO> getUnaryTypeActe();

    List<SelectValueDTO> getBordereauLabel();

    List<SelectValueDTO> getCategorieActe();

    List<SelectValueDTO> getUnaryCategorieActe();

    List<SelectValueDTO> getPrioriteCE();

    List<SelectValueDTO> getDelaiPublication();

    List<SelectValueDTO> getDelaiPublicationFiltered();

    List<SelectValueDTO> getUnaryDelaiPublication();

    List<SelectValueDTO> getTypeMesures();

    List<SelectValueDTO> getStatutDossier();

    List<SelectValueDTO> getStatutArchivageForStatistiques();

    List<SelectValueDTO> getUnaryStatutDossier();

    List<SelectValueDTO> getStatutValidation();

    List<SelectValueDTO> getRoutingTaskTypesFiltered();

    List<SelectValueDTO> getRoutingTaskTypesFilteredByIdFdr(CoreSession session, String idFdr);

    List<SelectValueDTO> getResponsableAmendement();

    List<SelectValueDTO> getPoleChargeMission();

    List<SelectValueDTO> getNatureTexte();

    List<SelectValueDTO> getProcedureVote();

    List<SelectValueDTO> getNatureTexteBaseLegale();

    List<SelectValueDTO> getTypePublication();

    List<SelectValueDTO> getStatutArchivageFacet();

    List<SelectValueDTO> getVecteurPublication();

    List<SelectValueDTO> getActifVecteurPublication();

    List<SelectValueDTO> getUnaryVecteurPublication();

    List<SelectValueDTO> getVecteurPublicationTS();

    List<SelectValueDTO> getModeParution();

    List<SelectValueDTO> getActifModeParution();

    List<SelectValueDTO> getEtatsDirectives();

    List<SelectValueDTO> getTypeHabilitation();

    List<SelectValueDTO> getTraitementPapierTypeAvis();

    List<SelectValueDTO> getTypeSignataireTraitementPapier();

    List<SelectValueDTO> getPeriodiciteRapport();

    List<SelectValueDTO> getSqueletteTypeDestinataire();

    List<SelectValueDTO> getDispositionHabilitation();
}
