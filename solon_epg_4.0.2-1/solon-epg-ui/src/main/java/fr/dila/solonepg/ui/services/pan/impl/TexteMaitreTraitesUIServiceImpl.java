package fr.dila.solonepg.ui.services.pan.impl;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.dto.TexteMaitreTraiteDTO;
import fr.dila.solonepg.core.dto.activitenormative.TexteMaitreTraiteDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.services.pan.TexteMaitreTraitesUIService;
import fr.dila.solonepg.ui.th.model.bean.pan.TexteMaitreTraiteForm;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.th.model.SpecificContext;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Bean Seam de gestion du texte maitre (Traite et Accords) de l'activite normative
 *
 * @author asatre
 */
public class TexteMaitreTraitesUIServiceImpl implements TexteMaitreTraitesUIService {

    @Override
    public String saveTexteMaître(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        DocumentModel currentDocument = context.getCurrentDocument();
        TexteMaitre texteMaitre = currentDocument.getAdapter(TexteMaitre.class);
        TexteMaitreTraiteDTO currentTexteMaitre = updateTexteMaitreTraiteDTO(context);
        DocumentModel doc = null;
        doc = currentTexteMaitre.remapField(texteMaitre, documentManager);
        doc =
            SolonEpgServiceLocator
                .getActiviteNormativeService()
                .saveTexteMaitre(doc.getAdapter(TexteMaitre.class), documentManager);

        // sauvegarde du decret de publication
        DocumentModel decretDoc = currentTexteMaitre.remapDecret(documentManager);
        if (decretDoc != null) {
            SolonEpgServiceLocator
                .getActiviteNormativeService()
                .saveTexteMaitre(decretDoc.getAdapter(TexteMaitre.class), documentManager);
            STServiceLocator
                .getJournalService()
                .journaliserActionPAN(
                    documentManager,
                    null,
                    SolonEpgEventConstant.MODIF_DECRET_PUB_EVENT,
                    SolonEpgEventConstant.MODIF_DECRET_PUB_COMMENT + " [" + currentTexteMaitre.getTitreActe() + "]",
                    SolonEpgEventConstant.CATEGORY_LOG_PAN_TRAITES_ACCORD
                );
        }

        // sauvegarde de la loi de ratification
        DocumentModel ratificationDoc = currentTexteMaitre.remapRatification(documentManager);
        if (ratificationDoc != null) {
            SolonEpgServiceLocator
                .getActiviteNormativeService()
                .saveTexteMaitre(ratificationDoc.getAdapter(TexteMaitre.class), documentManager);
            STServiceLocator
                .getJournalService()
                .journaliserActionPAN(
                    documentManager,
                    null,
                    SolonEpgEventConstant.MODIF_RATIF_EVENT,
                    SolonEpgEventConstant.MODIF_RATIF_COMMENT + " [" + currentTexteMaitre.getTitreActe() + "]",
                    SolonEpgEventConstant.CATEGORY_LOG_PAN_TRAITES_ACCORD
                );
        }

        currentTexteMaitre = new TexteMaitreTraiteDTOImpl(doc.getAdapter(ActiviteNormative.class), documentManager);

        STServiceLocator
            .getJournalService()
            .journaliserActionPAN(
                documentManager,
                null,
                SolonEpgEventConstant.MODIF_TM_EVENT,
                SolonEpgEventConstant.MODIF_TM_COMMENT + " [" + currentTexteMaitre.getTitreActe() + "]",
                SolonEpgEventConstant.CATEGORY_LOG_PAN_TRAITES_ACCORD
            );

        return null;
    }

    private TexteMaitreTraiteDTO updateTexteMaitreTraiteDTO(SpecificContext context) {
        TexteMaitreTraiteDTOImpl texteMaitreTraiteDTO = context.getFromContextData(PanContextDataKey.TEXTE_MAITRE_DTO);
        TexteMaitreTraiteForm texteMaitreForm = context.getFromContextData(PanContextDataKey.TEXTE_MAITRE_FORM);

        texteMaitreTraiteDTO.setCategorie(texteMaitreForm.getCategorie());
        texteMaitreTraiteDTO.setOrganisation(texteMaitreForm.getPays());
        texteMaitreTraiteDTO.setThematique(texteMaitreForm.getThematique());
        texteMaitreTraiteDTO.setTitreActe(texteMaitreForm.getIntitule());
        if (texteMaitreForm.getDateSignature() != null) {
            texteMaitreTraiteDTO.setDateSignature(texteMaitreForm.getDateSignature().getTime());
        }
        if (texteMaitreForm.getDateEntreeEnVigueur() != null) {
            texteMaitreTraiteDTO.setDateEntreeEnVigueur(texteMaitreForm.getDateEntreeEnVigueur().getTime());
        }
        texteMaitreTraiteDTO.setAutorisationRatification(texteMaitreForm.getAutorisation());
        texteMaitreTraiteDTO.setPublication(texteMaitreForm.getPublication());
        texteMaitreTraiteDTO.setObservation(texteMaitreForm.getObservation());
        texteMaitreTraiteDTO.setMinisterePilote(texteMaitreForm.getMinisterePilote());
        texteMaitreTraiteDTO.setDegrePriorite(texteMaitreForm.getDegrePriorite());
        if (texteMaitreForm.getDatePJL() != null) {
            texteMaitreTraiteDTO.setDatePJL(texteMaitreForm.getDatePJL().getTime());
        }
        texteMaitreTraiteDTO.setEtudeImpact(texteMaitreForm.getEtudeImpact());
        texteMaitreTraiteDTO.setDispoEtudeImpact(texteMaitreForm.getDispoEtudeImpact());
        if (texteMaitreTraiteDTO.getDateDepotRatification() != null) {
            texteMaitreTraiteDTO.setDateDepotRatification(texteMaitreForm.getDateDepotRatification().getTime());
        }

        TexteMaitreTraiteDTOImpl.RatificationTraite ratification = texteMaitreTraiteDTO.getRatificationTraite();
        ratification.setNumeroNor(texteMaitreForm.getRatificationNumeroNor());
        ratification.setNumero(texteMaitreForm.getRatificationNumero());
        if (texteMaitreForm.getRatificationDateCreation() != null) {
            ratification.setDateCreation(texteMaitreForm.getRatificationDateCreation().getTime());
        }
        ratification.setTitreOfficiel(texteMaitreForm.getRatificationTitreOfficiel());
        if (texteMaitreForm.getRatificationDateSaisineCE() != null) {
            ratification.setDateSaisineCE(texteMaitreForm.getRatificationDateSaisineCE().getTime());
        }
        if (texteMaitreForm.getRatificationDatePublication() != null) {
            ratification.setDatePublication(texteMaitreForm.getRatificationDatePublication().getTime());
        }

        TexteMaitreTraiteDTOImpl.DecretTraite decret = texteMaitreTraiteDTO.getDecretTraite();
        decret.setNumeroNor(texteMaitreForm.getDecretNumeroNor());
        decret.setNumero(texteMaitreForm.getDecretNumero());
        if (texteMaitreForm.getDecretDatePublication() != null) {
            decret.setDatePublication(texteMaitreForm.getDecretDatePublication().getTime());
        }
        decret.setTitreOfficiel(texteMaitreForm.getDecretTitreOfficiel());

        return texteMaitreTraiteDTO;
    }

    @Override
    public TexteMaitreTraiteDTO getCurrentTexteMaitre(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        TexteMaitreTraiteDTO currentTexteMaitre = context.getFromContextData(PanContextDataKey.TEXTE_MAITRE_TRAITE_DTO);
        DocumentModel currentDocument = context.getCurrentDocument();
        if (currentTexteMaitre == null || !currentTexteMaitre.getId().equals(currentDocument.getId())) {
            ActiviteNormative activiteNormative = currentDocument.getAdapter(ActiviteNormative.class);
            currentTexteMaitre = new TexteMaitreTraiteDTOImpl(activiteNormative, documentManager);
        }
        return currentTexteMaitre;
    }

    @Override
    public TexteMaitreTraiteDTOImpl.RatificationTraite getCurrentRatification(SpecificContext context) {
        TexteMaitreTraiteDTOImpl currentTexteMaitre = context.getFromContextData(
            PanContextDataKey.TEXTE_MAITRE_TRAITE_DTO
        );
        DocumentModel currentDocument = context.getCurrentDocument();
        if (currentTexteMaitre == null || !currentTexteMaitre.getId().equals(currentDocument.getId())) {
            currentTexteMaitre = (TexteMaitreTraiteDTOImpl) getCurrentTexteMaitre(context);
        }
        return currentTexteMaitre.getRatificationTraite();
    }

    @Override
    public TexteMaitreTraiteDTOImpl.DecretTraite getCurrentDecret(SpecificContext context) {
        TexteMaitreTraiteDTOImpl currentTexteMaitre = context.getFromContextData(
            PanContextDataKey.TEXTE_MAITRE_TRAITE_DTO
        );
        DocumentModel currentDocument = context.getCurrentDocument();
        if (currentTexteMaitre == null || !currentTexteMaitre.getId().equals(currentDocument.getId())) {
            currentTexteMaitre = (TexteMaitreTraiteDTOImpl) getCurrentTexteMaitre(context);
        }
        return currentTexteMaitre.getDecretTraite();
    }
}
