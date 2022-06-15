package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.dto.BilanSemestrielDTO;
import fr.dila.solonepg.api.exception.WsBdjException;
import java.io.IOException;
import java.util.List;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface WsBdjService {
    /**
     * Indique si la fonctionnalité de publication de l'échéancier vers la BDJ est
     * activée.
     */
    boolean isPublicationEcheancierBdjActivated(CoreSession session);

    /**
     * Publication de l'échéancier du texte maître
     */
    String publierEcheancierBDJ(DocumentModel currentDocument, CoreSession documentManager)
        throws WsBdjException, IOException, FactoryConfigurationError, XMLStreamException;

    /**
     * Publication de l'échéancier des textes maîtres
     */
    String publierEcheancierBDJ(List<TexteMaitre> textesMaitres, CoreSession session)
        throws WsBdjException, IOException, FactoryConfigurationError, XMLStreamException;

    void publierBilanSemestrielBDJ(BilanSemestrielDTO bilanSemestriel, String legislatureEnCours, CoreSession session);

    void sendTransferErrorMailToAdmins(Exception exc, CoreSession session);
}
