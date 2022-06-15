package fr.dila.solonepg.core.service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.audit.api.LogEntry;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.service.PdfGeneratorService;
import fr.dila.st.api.io.STByteArrayOutputStream;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.model.RouteTableElement;

/**
 * Implémentation du service de gnération du bordereau.
 *
 * @author admin
 */
public class PdfGeneratorServiceImpl implements PdfGeneratorService {
    /**
     * Nombre de colonne affiché dans la feuille de route.
     */
    private static final String BORDEREAU_PDF = "bordereau.pdf";

    private static final String JOURNAL_PDF = "journal.pdf";

    private static final String FEUILLE_ROUTE_PDF = "feuilleRoute.pdf";

    private static final String TRAITEMENT_PAPIER_PDF = "traitementPapier.pdf";

    /**
     * Debut du Nom du repertoire contenant un dossier dans le zip.
     */
    private static final String DOSSIER_FOLDER_NAME_IN_ZIP = "Dossier_";

    @Override
    public STByteArrayOutputStream addBordereauToZip(
        CoreSession session,
        ZipOutputStream zip,
        Dossier dossier,
        String idArchivageDefinitif
    )
        throws IOException {
    	return this.addPdfToZip(zip, dossier, BORDEREAU_PDF, session, idArchivageDefinitif);
    }

    @Override
    public STByteArrayOutputStream addJournalToZip(
        CoreSession session,
        ZipOutputStream zip,
        Dossier dossier,
        List<LogEntry> logEntries,
        String idArchivageDefinitif
    )
        throws IOException {
    	return this.addPdfToZip(zip, dossier, JOURNAL_PDF, session, idArchivageDefinitif);
    }

    @Override
    public STByteArrayOutputStream addFeuilleRouteToZip(
        ZipOutputStream zip,
        Dossier dossier,
        List<RouteTableElement> documentRouteTableElement,
        CoreSession session,
        String idArchivageDefinitif
    )
        throws IOException {
    	return this.addPdfToZip(zip, dossier, FEUILLE_ROUTE_PDF, session, idArchivageDefinitif);
    }

    @Override
    public STByteArrayOutputStream addTraitementPapierToZip(
        CoreSession session,
        ZipOutputStream zip,
        Dossier dossier,
        String idArchivageDefinitif
    )
        throws IOException {
        return this.addPdfToZip(zip, dossier, TRAITEMENT_PAPIER_PDF, session, idArchivageDefinitif);
    }

    private STByteArrayOutputStream addPdfToZip(
        ZipOutputStream zip,
        Dossier dossier,
        String fileName,
        CoreSession session,
        String idArchivageDefinitif
    )
        throws IOException {
        String pathToFile = getPdfPath(dossier, idArchivageDefinitif, fileName);
        boolean archivageDefinitif = StringUtils.isNotBlank(idArchivageDefinitif);
        // ajout du fichier
        ZipEntry entry = new ZipEntry(pathToFile);
        zip.putNextEntry(entry);

        STByteArrayOutputStream pdf = null;

        if (fileName.equals(BORDEREAU_PDF)) {
            pdf = SolonEpgServiceLocator.getPdfService().genererPdfBordereau(session, dossier, archivageDefinitif);
        } else if (fileName.equals(JOURNAL_PDF)) {
            pdf = SolonEpgServiceLocator.getPdfService().genererPdfJournal(dossier);
        } else if (fileName.equals(FEUILLE_ROUTE_PDF)) {
            pdf = SolonEpgServiceLocator.getPdfService().genererPdfFeuilleRoute(session, dossier, archivageDefinitif);
        } else if (fileName.equals(TRAITEMENT_PAPIER_PDF)) {
            pdf = SolonEpgServiceLocator.getPdfService().genererPdfTraitementPapier(session, dossier);
        }
        if (pdf != null) {
            zip.write(pdf.toByteArray());
        }
        zip.closeEntry();

        return pdf;
    }

    private String getPdfPath(Dossier dossier, String idArchivageDefinitif, String fileName) {
        String pathToFile;
        if (StringUtils.isBlank(idArchivageDefinitif)) {
            pathToFile = DOSSIER_FOLDER_NAME_IN_ZIP + dossier.getNumeroNor() + File.separator + fileName;
        } else {
            pathToFile = "Content" + File.separator + idArchivageDefinitif + ".pdf";
        }
        return pathToFile;
    }

    /**
     * Fonction utilitaire pour convertir un serializable en String
     *
     * @param serializable
     *            le serializable à convertir
     * @return la chaîne vide si le Serializable est null, la valeur de son toString sinon
     */
    public static String convertToString(Serializable serializable) {
        if (serializable == null) {
            return StringUtils.EMPTY;
        } else {
            return serializable.toString();
        }
    }
}
