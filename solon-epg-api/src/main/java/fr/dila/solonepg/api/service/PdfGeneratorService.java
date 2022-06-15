package fr.dila.solonepg.api.service;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.audit.api.LogEntry;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.st.api.io.STByteArrayOutputStream;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.model.RouteTableElement;

/**
 * Interface pour le service permettant de générer des documents PDF à partir de Document Model.
 *
 * @author arolin
 */
public interface PdfGeneratorService {
    /**
     * Ajoute le bordereau pdf dans le ZipOutputStream.
     *
     * @param zip
     * @param dossier
     * @throws Exception
     */
	STByteArrayOutputStream addBordereauToZip(
        CoreSession session,
        ZipOutputStream zip,
        Dossier dossier,
        String idArchivageDefinitif
    )
        throws IOException;

    /**
     * Ajoute le journal pdf dans le ZipOutputStream.
     *
     * @param zip
     * @param dossier
     * @param logEntries
     * @param messages
     * @throws Exception
     */
	STByteArrayOutputStream addJournalToZip(
        CoreSession session,
        ZipOutputStream zip,
        Dossier dossier,
        List<LogEntry> logEntries,
        String idArchivageDefinitif
    )
        throws IOException;

    /**
     * Ajoute la feuille de route pdf dans le ZipOutputStream.
     *
     * @param zip
     * @param dossier
     * @param logDocRouteTableElement
     * @param session
     * @param messages
     * @throws Exception
     */
	STByteArrayOutputStream addFeuilleRouteToZip(
        ZipOutputStream zip,
        Dossier dossier,
        List<RouteTableElement> logRouteTableElement,
        CoreSession session,
        String idArchivageDefinitif
    )
        throws IOException;

    /**
     * Ajoute le traitement papier pdf au zip
     *
     * @param session
     * @param zip
     * @param dossier
     * @throws Exception
     */
	STByteArrayOutputStream addTraitementPapierToZip(
        CoreSession session,
        ZipOutputStream zip,
        Dossier dossier,
        String idArchivageDefinitif
    )
        throws IOException;
}
