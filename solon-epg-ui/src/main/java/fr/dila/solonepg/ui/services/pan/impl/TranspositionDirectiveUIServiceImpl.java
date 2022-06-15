package fr.dila.solonepg.ui.services.pan.impl;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TexteTransposition;
import fr.dila.solonepg.api.cases.TranspositionDirective;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.dto.TexteTranspositionDTO;
import fr.dila.solonepg.api.dto.TranspositionDirectiveDTO;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.core.dto.activitenormative.TexteTranspositionDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.TranspositionDirectiveDTOImpl;
import fr.dila.solonepg.core.exception.ActiviteNormativeException;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.pan.TextesTranspositionsPanUnsortedList;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.services.pan.TranspositionDirectiveUIService;
import fr.dila.solonepg.ui.th.model.bean.pan.TexteTranspositionForm;
import fr.dila.solonepg.ui.th.model.bean.pan.TranspositionDirectiveForm;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

/**
 * Bean Seam de gestion des transposition de directive européenne de l'espace
 * d'activite normative
 *
 * @author asatre
 */
public class TranspositionDirectiveUIServiceImpl implements TranspositionDirectiveUIService {

    @Override
    public TextesTranspositionsPanUnsortedList getTextesTransposition(SpecificContext context) {
        TranspositionDirective transpositionDirective = context
            .getCurrentDocument()
            .getAdapter(TranspositionDirective.class);

        List<TexteTransposition> listTexteTransposition = SolonEpgServiceLocator
            .getTranspositionDirectiveService()
            .fetchTexteTransposition(transpositionDirective, context.getSession());

        List<TexteTranspositionDTOImpl> dtoList = listTexteTransposition
            .stream()
            .map(TexteTranspositionDTOImpl::new)
            .collect(Collectors.toList());

        return new TextesTranspositionsPanUnsortedList(context, dtoList);
    }

    public TranspositionDirectiveDTO updateTexteMaitreDTO(SpecificContext context) {
        TranspositionDirectiveDTOImpl texteMaitreDTO = context.getFromContextData(PanContextDataKey.TEXTE_MAITRE_DTO);
        TranspositionDirectiveForm texteMaitreForm = context.getFromContextData(PanContextDataKey.TEXTE_MAITRE_FORM);
        texteMaitreDTO.setNumero(texteMaitreForm.getNumero());
        texteMaitreDTO.setTitreOfficiel(texteMaitreForm.getTitre());
        texteMaitreDTO.setDateDirective(
            texteMaitreForm.getDateAdoption() == null ? null : texteMaitreForm.getDateAdoption().getTime()
        );
        texteMaitreDTO.setDirectionResponsable(texteMaitreForm.getDirection());
        texteMaitreDTO.setTabAffichageMarcheInt(texteMaitreForm.isDroitConforme());
        texteMaitreDTO.setAchevee(texteMaitreForm.isAchevee());
        texteMaitreDTO.setDateEcheance(
            texteMaitreForm.getDateEcheance() == null ? null : texteMaitreForm.getDateEcheance().getTime()
        );
        texteMaitreDTO.setDateTranspositionEffective(
            texteMaitreForm.getDateTranspositionEffective() == null
                ? null
                : texteMaitreForm.getDateTranspositionEffective().getTime()
        );
        texteMaitreDTO.setDateProchainTabAffichage(
            texteMaitreForm.getDateProchainTabAffichage() == null
                ? null
                : texteMaitreForm.getDateProchainTabAffichage().getTime()
        );
        texteMaitreDTO.setObservation(texteMaitreForm.getObservation());
        texteMaitreDTO.setMinisterePilote(texteMaitreForm.getMinisterePilote());
        texteMaitreDTO.setMinisterePiloteLabel(texteMaitreForm.getMinisterePiloteLabel());
        texteMaitreDTO.setChampLibre(texteMaitreForm.getChampLibre());
        texteMaitreDTO.setEtat(texteMaitreForm.getEtat());
        return texteMaitreDTO;
    }

    @Override
    public String saveTransposition(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        TranspositionDirectiveDTO currentTranspositionDTO = updateTexteMaitreDTO(context);
        DocumentModel currentDocument = context.getCurrentDocument();
        TranspositionDirective transpositionDirective = currentTranspositionDTO.remapField(
            currentDocument.getAdapter(TranspositionDirective.class)
        );
        SolonEpgServiceLocator
            .getTranspositionDirectiveService()
            .updateTranspositionDirectiveWithCheckUnique(documentManager, transpositionDirective);
        // Ajout dans le journal du PAN
        STServiceLocator
            .getJournalService()
            .journaliserActionPAN(
                documentManager,
                null,
                SolonEpgEventConstant.MODIF_TM_EVENT,
                SolonEpgEventConstant.MODIF_TM_COMMENT + " [" + transpositionDirective.getNumero() + "]",
                SolonEpgEventConstant.CATEGORY_LOG_PAN_TRANSPO_DIRECTIVES
            );

        return null;
    }

    @Override
    public TranspositionDirectiveDTO getCurrentTranspositionDTO(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        DocumentModel currentDocument = context.getCurrentDocument();
        List<TexteTranspositionDTO> listTexteTranspositionDTO = context.getFromContextData(
            PanContextDataKey.TRANSPOSITION_DIRECTIVE_LIST
        );
        TranspositionDirectiveDTO currentTranspositionDTO = context.getFromContextData(
            PanContextDataKey.TRANSPOSITION_DIRECTIVE_DTO
        );
        if (currentTranspositionDTO == null || !currentTranspositionDTO.getId().equals(currentDocument.getId())) {
            TranspositionDirective transpositionDirective = currentDocument.getAdapter(TranspositionDirective.class);
            currentTranspositionDTO = new TranspositionDirectiveDTOImpl(transpositionDirective);

            List<TexteTransposition> listTexteTransposition = SolonEpgServiceLocator
                .getTranspositionDirectiveService()
                .fetchTexteTransposition(transpositionDirective, documentManager);
            for (TexteTransposition texteTransposition : listTexteTransposition) {
                listTexteTranspositionDTO.add(new TexteTranspositionDTOImpl(texteTransposition));
            }

            if (listTexteTranspositionDTO.isEmpty()) {
                listTexteTranspositionDTO.add(new TexteTranspositionDTOImpl(""));
            }
        }

        return currentTranspositionDTO;
    }

    @Override
    public String addNewText(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        String nor = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT_NOR);
        TexteTransposition texteTransposition = context
            .getSession()
            .getDocument(new IdRef(context.getFromContextData(PanContextDataKey.FIRST_TABLE_ID)))
            .getAdapter(TexteTransposition.class);
        List<TexteTranspositionDTO> listTexteTranspositionDTO = context.getFromContextData(
            PanContextDataKey.FIRST_TABLE_LIST
        );

        Dossier dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(documentManager, nor);
        if (dossier == null) {
            throw new ActiviteNormativeException("activite.normative.dossier.not.found");
        }
        TexteTranspositionDTO tmp = new TexteTranspositionDTOImpl(nor);
        tmp.setValidate(Boolean.TRUE);

        listTexteTranspositionDTO.add(tmp);
        // Ajout dans le journal du PAN
        STServiceLocator
            .getJournalService()
            .journaliserActionPAN(
                documentManager,
                null,
                SolonEpgEventConstant.AJOUT_TXT_TRANSPO_EVENT,
                SolonEpgEventConstant.AJOUT_TXT_TRANSPO_COMMENT +
                " [" +
                nor +
                "] transposant la directive [" +
                texteTransposition.getNumero() +
                "]",
                SolonEpgEventConstant.CATEGORY_LOG_PAN_TRANSPO_DIRECTIVES
            );
        return null;
    }

    @Override
    public String removeTexteTransposition(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        TexteTransposition texteTransposition = context
            .getSession()
            .getDocument(new IdRef(context.getFromContextData(PanContextDataKey.FIRST_TABLE_ID)))
            .getAdapter(TexteTransposition.class);
        TranspositionDirective transposition = context.getCurrentDocument().getAdapter(TranspositionDirective.class);

        List<String> textesNors = transposition.getDossiersNor();
        textesNors.remove(texteTransposition.getNumeroNor());
        transposition.setDossiersNor(textesNors);
        context.putInContextData(PanContextDataKey.RELOAD_FROM_DOSSIER, false);

        final ActiviteNormativeService panService = SolonEpgServiceLocator.getActiviteNormativeService();

        panService.saveTexteMaitre(transposition, context.getSession());
        // Ajout dans le journal du PAN
        STServiceLocator
            .getJournalService()
            .journaliserActionPAN(
                documentManager,
                null,
                SolonEpgEventConstant.SUPPR_TXT_TRANSPO_EVENT,
                SolonEpgEventConstant.SUPPR_TXT_TRANSPO_COMMENT +
                " [" +
                texteTransposition.getNumeroNor() +
                "] transposant la directive [" +
                transposition.getNumero() +
                "]",
                SolonEpgEventConstant.CATEGORY_LOG_PAN_TRANSPO_DIRECTIVES
            );
        return null;
    }

    @Override
    public String updateTexteTransposition(SpecificContext context) {
        TexteTranspositionForm form = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT_FORM);
        TexteTranspositionDTO dto = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT_DTO);

        dto.setNature(form.getNature());
        dto.setNumeroNor(form.getNumeroNor());
        dto.setIntitule(form.getIntitule());
        dto.setMinisterePilote(form.getMinisterePilote());
        dto.setNumeroTextePublie(form.getNumeroTextePublie());
        dto.setTitreTextePublie(form.getTitreOfficiel());
        dto.setValidate(true);

        if (form.getDatePublication() != null) {
            dto.setDatePublication(form.getDatePublication().getTime());
        }

        return saveTexteTransposition(context);
    }

    @Override
    public String saveTexteTransposition(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        DocumentModel currentDocument = context.getCurrentDocument();
        List<TexteTranspositionDTO> listTexteTranspositionDTO = context.getFromContextData(
            PanContextDataKey.FIRST_TABLE_LIST
        );

        TranspositionDirective transpositionDirective = currentDocument.getAdapter(TranspositionDirective.class);
        SolonEpgServiceLocator
            .getTranspositionDirectiveService()
            .saveTexteTranspositionDTO(transpositionDirective, listTexteTranspositionDTO, documentManager);

        listTexteTranspositionDTO = new ArrayList<>();

        List<TexteTransposition> listTexteTransposition = SolonEpgServiceLocator
            .getTranspositionDirectiveService()
            .fetchTexteTransposition(transpositionDirective, documentManager);
        for (TexteTransposition texteTransposition : listTexteTransposition) {
            listTexteTranspositionDTO.add(new TexteTranspositionDTOImpl(texteTransposition));
        }
        return null;
    }

    @Override
    public List<TexteTranspositionDTO> refreshTexteTransposition(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        List<TexteTranspositionDTO> listTexteTranspositionDTO = context.getFromContextData(
            PanContextDataKey.FIRST_TABLE_LIST
        );
        SolonEpgServiceLocator
            .getTranspositionDirectiveService()
            .refreshTexteTransposition(listTexteTranspositionDTO, documentManager);
        return listTexteTranspositionDTO;
    }
}
