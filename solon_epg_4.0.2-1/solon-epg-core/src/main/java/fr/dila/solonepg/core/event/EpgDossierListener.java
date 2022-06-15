package fr.dila.solonepg.core.event;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.tablereference.ModeParution;
import fr.dila.st.core.event.RollbackEventListener;
import fr.dila.st.core.service.STServiceLocator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

public class EpgDossierListener extends RollbackEventListener {

    @Override
    public void handleDocumentEvent(Event event, DocumentEventContext ctx) {
        DocumentModel doc = ctx.getSourceDocument();
        if (!DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE.equals(doc.getType())) {
            return;
        }

        Dossier dos = doc.getAdapter(Dossier.class);
        setLabelToPropertyFromVocabulary(
            dos.getTypeActe(),
            VocabularyConstants.TYPE_ACTE_VOCABULARY,
            dos,
            (dossier, label) -> dossier.setTypeActeLabel(label)
        );
        setLabelToPropertyFromVocabulary(
            dos.getStatut(),
            VocabularyConstants.STATUT_DOSSIER_DIRECTORY_NAME,
            dos,
            (dossier, label) -> dossier.setStatutLabel(label)
        );
        setLabelToPropertyFromVocabulary(
            dos.getCategorieActe(),
            VocabularyConstants.CATEGORY_ACTE_CONVENTION_COLLECTIVE,
            dos,
            (dossier, label) -> dossier.setCategorieActeLabel(label)
        );
        setLabelToPropertyFromVocabulary(
            dos.getPublicationIntegraleOuExtrait(),
            VocabularyConstants.TYPE_PUBLICATION,
            dos,
            (dossier, label) -> dossier.setPublicationIntegraleOuExtraitLabel(label)
        );
        setLabelToPropertyFromVocabulary(
            dos.getDelaiPublication(),
            VocabularyConstants.DELAI_PUBLICATION,
            dos,
            (dossier, label) -> dossier.setDelaiPublicationLabel(label)
        );

        CoreSession session = ctx.getCoreSession();
        RetourDila rd = doc.getAdapter(RetourDila.class);
        Optional
            .of(rd)
            .map(RetourDila::getModeParution)
            .filter(Objects::nonNull)
            .map(IdRef::new)
            .filter(session::exists)
            .map(session::getDocument)
            .map(ModeParution::getAdapter)
            .map(ModeParution::getMode)
            .ifPresent(rd::setModeParutionLabel);
    }

    private void setLabelToPropertyFromVocabulary(
        String property,
        String voc,
        Dossier dos,
        BiConsumer<Dossier, String> function
    ) {
        if (property != null) {
            String label = STServiceLocator.getVocabularyService().getEntryLabel(voc, property);
            function.accept(dos, label);
        }
    }
}
