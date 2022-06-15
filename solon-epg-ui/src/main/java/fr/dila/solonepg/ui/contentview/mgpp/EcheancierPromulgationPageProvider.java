package fr.dila.solonepg.ui.contentview.mgpp;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.cases.typescomplexe.DonneesSignataire;
import fr.dila.solonepg.api.cases.typescomplexe.InfoEpreuve;
import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.MgppEcheancierPromulgationDTO;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.core.query.QueryHelper;
import fr.dila.st.ui.contentview.AbstractDTOPageProvider;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.schema.PrefetchInfo;

public class EcheancierPromulgationPageProvider extends AbstractDTOPageProvider {
    private static final long serialVersionUID = 1L;

    @Override
    protected void fillCurrentPageMapList(CoreSession coreSession) {
        currentItems = new ArrayList<>();

        resultsCount = QueryUtils.doCountQuery(coreSession, query);
        // recupere la liste des ids triés
        // se baser sur ce tri pour retourner une liste de map triée dans le bon
        // ordre
        if (resultsCount > 0) {
            List<String> ids = QueryUtils.doQueryForIds(coreSession, query, getPageSize(), offset);
            populateFromIds(coreSession, ids);
        }
    }

    private void populateFromIds(CoreSession session, List<String> ids) {
        if (!ids.isEmpty()) {
            PrefetchInfo prefetchInfo = new PrefetchInfo(
                STSchemaConstant.DUBLINCORE_SCHEMA +
                "," +
                FicheLoi.SCHEMA +
                "," +
                TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA
            );
            List<DocumentModel> dml = QueryHelper.retrieveDocuments(session, ids, prefetchInfo);

            List<String> idsDossier = dml
                .stream()
                .map(doc -> doc.getAdapter(FicheLoi.class))
                .map(FicheLoi::getIdDossier)
                .collect(Collectors.toList());

            List<DocumentModel> dossierList = SolonEpgServiceLocator
                .getDossierService()
                .findDossiersFromIdsDossier(session, idsDossier);
            HashMap<String, DocumentModel> dossierMap = new HashMap<>();
            dossierList
                .stream()
                .map(doc -> doc.getAdapter(Dossier.class))
                .forEach(dos -> dossierMap.put(dos.getIdDossier(), dos.getDocument()));

            currentItems.addAll(
                dml
                    .stream()
                    .filter(Objects::nonNull)
                    .map(dm -> dmToEcheancierDto(dossierMap, dm))
                    .collect(Collectors.toList())
            );
        }
    }

    private MgppEcheancierPromulgationDTO dmToEcheancierDto(
        HashMap<String, DocumentModel> dossierMap,
        DocumentModel dm
    ) {
        FicheLoi ficheLoi = dm.getAdapter(FicheLoi.class);

        MgppEcheancierPromulgationDTO echeDto = new MgppEcheancierPromulgationDTO();
        echeDto.setId(ficheLoi.getDocument().getId());
        echeDto.setIntitule(ficheLoi.getIntitule());
        echeDto.setConseilConstitutionnelSaisine(ficheLoi.getDateSaisieCC());
        echeDto.setConseilConstitutionnelDecision(ficheLoi.getDateDecision());
        echeDto.setDateReception(ficheLoi.getDateReception());
        echeDto.setDepartElysee(ficheLoi.getDepartElysee());
        echeDto.setRetourElysee(ficheLoi.getRetourElysee());
        echeDto.setDateLimitePromulgation(ficheLoi.getDateLimitePromulgation());
        echeDto.setPublicationJO(ficheLoi.getDateJO());
        if (dossierMap.containsKey(ficheLoi.getIdDossier())) {
            TraitementPapier traitementPapier = dossierMap
                .get(ficheLoi.getIdDossier())
                .getAdapter(TraitementPapier.class);
            echeDto.setRetourRelecture(traitementPapier.getRetourDuBonaTitrerLe());
            InfoEpreuve epreuve = traitementPapier.getEpreuve();
            if (epreuve != null) {
                echeDto.setDemandeEpreuvesJO(epreuve.getEpreuveDemandeLe());
                echeDto.setEnvoiRelecture(epreuve.getEnvoiRelectureLe());
                echeDto.setRetourJO(epreuve.getEpreuvePourLe());
            }
            DonneesSignataire signature = traitementPapier.getSignaturePr();
            if (signature != null) {
                echeDto.setMisAuContreseing(signature.getDateEnvoiSignature());
                echeDto.setContreseingPM(signature.getDateRetourSignature());
            }
        }
        return echeDto;
    }
}
