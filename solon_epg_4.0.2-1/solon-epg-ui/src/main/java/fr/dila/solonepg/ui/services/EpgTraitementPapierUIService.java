package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.api.documentmodel.FileSolonEpg;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.ampliation.AmpliationDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.ampliation.HistoriqueAmpliation;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.communication.CommunicationDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.communication.DestinataireCommunicationDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.epreuve.EpreuveDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.publication.PublicationDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.reference.ComplementDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.retour.RetourDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.signature.SignatureDTO;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import java.util.stream.Stream;
import javax.ws.rs.core.Response;

public interface EpgTraitementPapierUIService {
    boolean canEditTabs(SpecificContext context);

    ComplementDTO getComplementDTO(SpecificContext context);

    void saveComplementDTO(SpecificContext context);

    CommunicationDTO getCommunicationDTO(SpecificContext context);

    AmpliationDTO getAmpliationDTO(SpecificContext context);

    String getDefaultAmpliationMailText(SpecificContext context);

    String getDefaultAmpliationMailObjet(SpecificContext context);

    List<HistoriqueAmpliation> getInfoAmpilation(SpecificContext context);

    void saveAmpliationDTO(SpecificContext context);

    void saveAmpliationFile(SpecificContext context);

    void sendAmpliation(SpecificContext context);

    String getCommunicationDestinataireObjet(SpecificContext context);

    EpreuveDTO getEpreuveDTO(SpecificContext context);

    SignatureDTO getSignatureDTO(SpecificContext context);

    PublicationDTO getPublicationDTO(SpecificContext context);

    void saveCommunicationDTO(SpecificContext context);

    void savePublicationDTO(SpecificContext context);

    void saveEpreuveDTO(SpecificContext context);

    void saveSignatureDTO(SpecificContext context);

    RetourDTO getRetourDTO(SpecificContext context);

    void saveRetourDTO(SpecificContext context);

    Response editerBordereauEnvoi(SpecificContext context);

    Response editerBordereauRelance(SpecificContext context);

    Response editerBordereauCabinetSg(SpecificContext context);

    Response editerCheminDeCroix(SpecificContext context);

    boolean isReferencesPublicationVisible(SpecificContext context);

    boolean isReferencesComplementElementVisible(SpecificContext context);

    boolean isCommunicationSecretaireGeneralVisible(SpecificContext context);

    boolean isCommunicationComplementVisible(SpecificContext context);

    boolean isSignatureEditerCheminCroixVisible(SpecificContext context);

    List<SelectValueDTO> getReferencesCabinetSg(SpecificContext context);

    List<SelectValueDTO> getReferencesCabinetPremierMinistre(SpecificContext context);

    List<SelectValueDTO> getReferencesChargesMission(SpecificContext context);

    List<SelectValueDTO> getReferencesSecretariatSecretaireGeneral(SpecificContext context);

    List<SelectValueDTO> getReferencesSignataires(SpecificContext context);

    SelectValueDTO usrIdToSelectValueDTO(SpecificContext context);

    Response editerBordereauRetour(SpecificContext context);

    String getEpreuveDestinataire(SpecificContext context);

    Response editerDemandeEpreuve(SpecificContext context);

    Response editerNouvelleEpreuve(SpecificContext context);

    void setLabelValues(Stream<DestinataireCommunicationDTO> destinataires);

    FileSolonEpg getAmpliationFile(SpecificContext context);
}
