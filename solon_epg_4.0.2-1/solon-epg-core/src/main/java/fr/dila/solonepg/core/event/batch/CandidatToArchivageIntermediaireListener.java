package fr.dila.solonepg.core.event.batch;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.st.api.constant.STParametreConstant;
import fr.dila.st.api.service.STParametreService;
import java.util.Calendar;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.query.sql.model.DateLiteral;

public class CandidatToArchivageIntermediaireListener extends AbstractCandidatToArchivageListener {

    public CandidatToArchivageIntermediaireListener() {
        super(
            SolonEpgEventConstant.BATCH_EVENT_DOSSIER_CANDIDAT_ARCHIVAGE_INTERMEDIAIRE,
            EpgLogEnumImpl.INIT_B_ARCHIV_INTER_TEC,
            EpgLogEnumImpl.END_B_ARCHIV_INTER_TEC,
            EpgLogEnumImpl.FAIL_PROCESS_B_ARCHIV_INTER_TEC,
            VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_INTERMEDIAIRE,
            "Dossiers candidats archivage intermédiaire",
            STParametreConstant.DELAI_VERSEMENT_BASE_INTERMEDIAIRE,
            STParametreConstant.OBJET_MAIL_DOSSIER_CANDIDAT_ARCHIVAGE_INTERMEDIAIRE,
            STParametreConstant.TEXT_MAIL_DOSSIER_CANDIDAT_ARCHIVAGE_INTERMEDIAIRE
        );
    }

    @Override
    protected String getQuery(CoreSession session, STParametreService paramService, String literalDate) {
        final String currentDate = DateLiteral.dateFormatter.print(Calendar.getInstance().getTime().getTime());

        return (
            "SELECT * FROM Dossier WHERE ecm:isProxy = 0 AND ( dos:" +
            DossierSolonEpgConstants.DOSSIER_STATUT_ARCHIVAGE +
            " is null OR dos:" +
            DossierSolonEpgConstants.DOSSIER_STATUT_ARCHIVAGE +
            " = '" +
            VocabularyConstants.STATUT_ARCHIVAGE_AUCUN +
            "') and (( dos:" +
            DossierSolonEpgConstants.DOSSIER_DATE_DE_MAINTIEN_EN_PRODCUTION +
            " is not null and dos:" +
            DossierSolonEpgConstants.DOSSIER_DATE_DE_MAINTIEN_EN_PRODCUTION +
            " < TIMESTAMP '" +
            currentDate +
            " ' ) OR dos:" +
            DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY +
            " = '" +
            VocabularyConstants.STATUT_NOR_ATTRIBUE +
            "' OR dos:" +
            DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY +
            " = '" +
            VocabularyConstants.STATUT_ABANDONNE +
            "' OR dos:" +
            DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY +
            " = '" +
            VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION +
            "' OR  ( dos:" +
            DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY +
            " = '" +
            VocabularyConstants.STATUT_PUBLIE +
            "' and  " +
            RetourDilaConstants.RETOUR_DILA_SCHEMA_PREFIX +
            ":" +
            RetourDilaConstants.RETOUR_DILA_DATE_PARUTION_JORF_PROPERTY +
            " < TIMESTAMP '" +
            literalDate +
            " ' ))"
        );
    }
}
