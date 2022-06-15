package fr.dila.solonmgpp.core.event;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.cases.typescomplexe.DonneesSignataire;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.solonmgpp.api.service.DossierService;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.core.util.SolonDateConverter;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.PostCommitEventListener;

public class UpdateFichePresentationAfterUpdateBordereauListener implements PostCommitEventListener {

    @Override
    public void handleEvent(EventBundle events) {
        for (Event event : events) {
            if (SolonEpgEventConstant.BATCH_EVENT_UPDATE_FICHE_AFTER_UPDATE_BORDEREAU.equals(event.getName())) {
                processEvent(event);
            }
        }
    }

    private void processEvent(Event event) {
        final EventContext eventCtx = event.getContext();
        final CoreSession session = eventCtx.getCoreSession();

        DocumentModel dossierDoc = (DocumentModel) eventCtx.getProperty(SolonEpgEventConstant.DOSSIER_EVENT_PARAM);
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        String typeActe = (String) eventCtx.getProperty(SolonEpgEventConstant.TYPE_ACTE_EVENT_PARAM);
        String norPreReattribution = (String) eventCtx.getProperty(SolonEpgEventConstant.NOR_PRE_REATTRIBUTION_VALUE);

        if (dossier != null) {
            if (SolonEpgEventConstant.TYPE_ACTE_DR_EVENT_VALUE.equals(typeActe)) {
                updateFicheDR(dossier, session, norPreReattribution);
            } else if (SolonEpgEventConstant.TYPE_ACTE_FL_EVENT_VALUE.equals(typeActe)) {
                updateFicheLoi(dossier, session, norPreReattribution);
            }
        }
    }

    private void updateFicheDR(Dossier dossier, CoreSession session, String norPreReattribution) {
        final DossierService dossierService = SolonMgppServiceLocator.getDossierService();
        FichePresentationDR fichePresentationDR = dossierService.findOrCreateFicheDR(
            session,
            norPreReattribution == null ? dossier.getNumeroNor() : norPreReattribution
        );

        fichePresentationDR.setIdDossier(dossier.getNumeroNor());
        fichePresentationDR.setNature(dossier.getBaseLegaleNatureTexte());
        fichePresentationDR.setTexteRef(dossier.getBaseLegaleNumeroTexte());
        fichePresentationDR.setObjet(dossier.getTitreActe());
        fichePresentationDR.setResponsabiliteElaboration(dossier.getNomRespDossier());
        fichePresentationDR.setRapportParlement(dossier.getPeriodiciteRapport());
        fichePresentationDR.setMinisteres(dossier.getMinistereResp());
        fichePresentationDR.setPeriodicite(dossier.getPeriodicite());
        fichePresentationDR.setPole(dossier.getNomCompletChargesMissions());

        dossierService.saveFicheDR(session, fichePresentationDR.getDocument());
    }

    private void updateFicheLoi(Dossier dossier, CoreSession session, String norPreReattribution) {
        final DossierService dossierService = SolonMgppServiceLocator.getDossierService();
        final ActeService acteService = SolonEpgServiceLocator.getActeService();
        Set<String> lstTypeLois = acteService.getLoiList();

        if (lstTypeLois.contains(dossier.getTypeActe())) {
            FicheLoi ficheLoi = null;
            DocumentModel doc = dossierService.findFicheLoiDocumentFromNor(
                session,
                norPreReattribution == null ? dossier.getNumeroNor() : norPreReattribution
            );
            if (doc != null) {
                ficheLoi = doc.getAdapter(FicheLoi.class);
            }

            if (ficheLoi != null) {
                ficheLoi.setIntitule(dossier.getTitreActe());
                ficheLoi.setNumeroNor(dossier.getNumeroNor());

                // On met à jour les données de type traitement papier et retourDila dès qu'on met à jour le bordereau
                // ou
                // l'onglet traitement papier
                TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
                RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
                if (traitementPapier != null && traitementPapier.getSignaturePr() != null) {
                    DonneesSignataire signaturePr = traitementPapier.getSignaturePr();

                    if (signaturePr != null) {
                        ficheLoi.setDepartElysee(signaturePr.getDateEnvoiSignature());
                        ficheLoi.setRetourElysee(signaturePr.getDateRetourSignature());

                        if (retourDila != null && StringUtils.isNotBlank(retourDila.getNumeroTexteParutionJorf())) {
                            ficheLoi.setTitreOfficiel(
                                "Loi n° " +
                                StringUtils.defaultString(retourDila.getNumeroTexteParutionJorf()) +
                                " du " +
                                SolonDateConverter.getClientConverter().format(signaturePr.getDateRetourSignature())
                            );
                        }
                    }
                }

                if (retourDila != null) {
                    ficheLoi.setDateJO(retourDila.getDateParutionJorf());
                }

                dossierService.saveFicheLoi(session, ficheLoi.getDocument());
            }
        }
    }
}
