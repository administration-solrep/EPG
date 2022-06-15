package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TexteSignale;
import fr.dila.solonepg.api.constant.TexteSignaleConstants;
import fr.dila.solonepg.api.service.TexteSignaleService;
import fr.dila.st.core.service.STServiceLocator;
import fr.sword.naiad.nuxeo.ufnxql.core.query.FlexibleQueryMaker;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Implementation du service sur les textes signalés de l'espace de suivi.
 *
 * @author asatre
 */
public class TexteSignaleServiceImpl implements TexteSignaleService {
    private static final long serialVersionUID = 1L;

    @Override
    public void verser(final CoreSession session, final Dossier dossier, final String type) {
        if (dossier != null) {
            final TexteSignale texteSignale = findTexteSignale(session, dossier.getDocument().getId(), type);
            dossier.setDateVersementTS(Calendar.getInstance());
            dossier.save(session);
            texteSignale.setType(type);
            session.saveDocument(texteSignale.getDocument());
        }
    }

    @Override
    public void retirer(final CoreSession session, final Dossier dossier) {
        // force lock remove
        if (dossier != null) {
            STServiceLocator.getSTLockService().unlockDocUnrestricted(session, dossier.getDocument());
            dossier.setDateVersementTS(null);
            dossier.save(session);
        }
    }

    @Override
    public void save(
        final CoreSession session,
        final String idDossier,
        final Date datePublication,
        final String vecteurPublication,
        final String observation,
        final String type
    ) {
        final TexteSignale texteSignale = findTexteSignale(session, idDossier, type);
        if (datePublication != null) {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(datePublication);
            texteSignale.setDateDemandePublicationTS(cal);
        } else {
            texteSignale.setDateDemandePublicationTS(null);
        }

        texteSignale.setVecteurPublicationTS(vecteurPublication);
        texteSignale.setObservationTS(observation);
        session.saveDocument(texteSignale.getDocument());
    }

    @Override
    public TexteSignale findTexteSignale(final CoreSession session, final String idDossier, final String type) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT m.ecm:uuid as id FROM ");
        queryBuilder.append(TexteSignaleConstants.TEXTE_SIGNALE_DOCUMENT_TYPE);
        queryBuilder.append(" as m WHERE m.");
        queryBuilder.append(TexteSignaleConstants.TEXTE_SIGNALE_PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(TexteSignaleConstants.ID_DOSSIER_TS);
        queryBuilder.append(" = ? ");

        final List<DocumentModel> list = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            TexteSignaleConstants.TEXTE_SIGNALE_DOCUMENT_TYPE,
            FlexibleQueryMaker.KeyCode.UFXNQL.getKey() + queryBuilder.toString(),
            new Object[] { idDossier }
        );
        if (list != null && !list.isEmpty()) {
            final DocumentModel doc = list.get(0);
            return doc.getAdapter(TexteSignale.class);
        } else {
            // creation du texte signale
            DocumentModel modelDesired = session.createDocumentModel(TexteSignaleConstants.TEXTE_SIGNALE_DOCUMENT_TYPE);
            modelDesired.setPathInfo(TexteSignaleConstants.TEXTE_SIGNALE_PATH, idDossier);
            modelDesired.getAdapter(TexteSignale.class).setIdDossier(idDossier);
            modelDesired = session.createDocument(modelDesired);
            session.save();

            return modelDesired.getAdapter(TexteSignale.class);
        }
    }
}
