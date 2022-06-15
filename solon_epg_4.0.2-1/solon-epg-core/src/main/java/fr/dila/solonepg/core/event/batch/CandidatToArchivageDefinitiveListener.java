package fr.dila.solonepg.core.event.batch;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.EpgCorbeilleService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.constant.STParametreConstant;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.core.service.STServiceLocator;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.query.sql.model.DateLiteral;

public class CandidatToArchivageDefinitiveListener extends AbstractCandidatToArchivageListener {

    public CandidatToArchivageDefinitiveListener() {
        super(
            SolonEpgEventConstant.BATCH_EVENT_DOSSIER_CANDIDAT_ARCHIVAGE_DEFINITIF,
            EpgLogEnumImpl.INIT_B_ARCHIV_DEF_TEC,
            EpgLogEnumImpl.END_B_ARCHIV_DEF_TEC,
            EpgLogEnumImpl.FAIL_PROCESS_B_ARCHIV_DEF_TEC,
            VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_DEFINITIVE,
            "Dossiers candidats archivage définitif",
            STParametreConstant.DUREE_UTILITE_ADMINISTRATIVE,
            STParametreConstant.OBJET_MAIL_DOSSIER_CANDIDAT_ARCHIVAGE_DEFINITIVE,
            STParametreConstant.TEXT_MAIL_DOSSIER_CANDIDAT_ARCHIVAGE_DEFINITIVE
        );
    }

    @Override
    protected String getQuery(CoreSession session, STParametreService paramService, String literalDate) {
        final DocumentModelList documentModelList = STServiceLocator
            .getVocabularyService()
            .getAllEntry(VocabularyConstants.TYPE_ACTE_VOCABULARY);

        final HashMap<String, String> params = new HashMap<>();
        String paramVal;
        for (final DocumentModel documentModel : documentModelList) {
            final String ident = (String) documentModel.getProperty(STVocabularyConstants.COLUMN_ID).getValue();
            paramVal =
                paramService.getParametreValue(session, STParametreConstant.DUREE_UTILITE_ADMINISTRATIVE + "-" + ident);
            if (StringUtils.isBlank(paramVal)) {
                paramVal = literalDate;
            } else {
                Calendar dua = Calendar.getInstance();
                dua.add(Calendar.MONTH, -Integer.parseInt(paramVal));
                paramVal = DateLiteral.dateFormatter.print(dua.getTimeInMillis());
            }
            params.put(ident, paramVal);
        }
        StringBuilder query = new StringBuilder("SELECT * FROM Dossier WHERE dos:");
        query.append(DossierSolonEpgConstants.DOSSIER_STATUT_ARCHIVAGE);
        query.append(" = '");
        query.append(VocabularyConstants.STATUT_ARCHIVAGE_BASE_INTERMEDIAIRE);
        query.append("' and ecm:isProxy = 0 ");
        query.append("and (");

        for (Entry<String, String> entry : params.entrySet()) {
            query.append("( dos:");
            query.append(DossierSolonEpgConstants.DOSSIER_TYPE_ACTE_PROPERTY);
            query.append(" = ");
            query.append(entry.getKey());
            query.append(" and dos:");
            query.append(DossierSolonEpgConstants.DOSSIER_DATE_VERSEMENT_ARCHIVAGE_INTERMEDIAIRE);
            query.append(" < TIMESTAMP '");
            query.append(entry.getValue());
            query.append(" ' ) OR ");
        }
        String queryStr = query.substring(0, query.length() - 3);
        queryStr += ")";

        return queryStr;
    }

    @Override
    protected void additionalProcessToDossier(CoreSession session, DocumentModel dossierDoc) {
        // récupère les dossiersLink lié au dossier
        final EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
        final List<DocumentModel> dossierLinkDocList = corbeilleService.findDossierLink(session, dossierDoc.getId());
        // modifie les dossiersLinks du dossier
        for (final DocumentModel dossierLinkDoc : dossierLinkDocList) {
            final DossierLink dossierLink = dossierLinkDoc.getAdapter(DossierLink.class);
            dossierLink.setStatutArchivage(VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_DEFINITIVE);
            dossierLink.save(session);
        }
    }
}
