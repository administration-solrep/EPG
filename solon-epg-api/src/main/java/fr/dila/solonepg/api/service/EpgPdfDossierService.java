package fr.dila.solonepg.api.service;

import java.io.File;
import java.io.IOException;

import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.st.api.io.STByteArrayOutputStream;

/**
 * Service associé à la manipulation de fichiers PDF au niveau métier
 *
 * @author tlombard
 * @author ygbinu
 */
public interface EpgPdfDossierService {
    File generateDossierPdf(CoreSession session, Dossier dossier) throws IOException;

    STByteArrayOutputStream genererPdfBordereau(CoreSession session, Dossier dossier, boolean archivageDefinitif);

    STByteArrayOutputStream genererPdfFeuilleRoute(CoreSession session, Dossier dossier, boolean archivageDefinitif);

    STByteArrayOutputStream genererPdfJournal(Dossier dossier);

    STByteArrayOutputStream genererPdfTraitementPapier(CoreSession session, Dossier dossier);
}
