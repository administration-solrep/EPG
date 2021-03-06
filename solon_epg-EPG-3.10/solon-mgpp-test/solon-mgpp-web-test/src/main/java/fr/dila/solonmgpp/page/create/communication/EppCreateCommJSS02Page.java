package fr.dila.solonmgpp.page.create.communication;

import java.util.Calendar;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.EppDetailCommJSS02Page;
import fr.dila.solonmgpp.utils.DateUtils;
import fr.sword.xsd.solon.epp.PieceJointeType;

public class EppCreateCommJSS02Page extends AbstractEppCreateComm {

    public static final String TYPE_COMM = "JSS-02 : Demande de jours supplémentaires de séance à l'initiative d'une assemblée";
    public static final String DATE_VOTE_INPUT_DATE = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_dateVoteInputDate";
    public static final String OBJET_ID = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_objet";
    public static final String HORODATAGE = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_horodatage";

    public static final String URL = "http://PJPMVANPourAVIAN.solon-epg.fr";
    
    public static final String PJ = "src/main/attachments/piece-jointe.doc";
    

    public EppDetailCommJSS02Page createCommJSS02(final String idDossier, final String objet) {
        checkValue(COMMUNICATION, TYPE_COMM);
        checkValueContain(HORODATAGE, DateUtils.format(Calendar.getInstance()));
        setObjet(objet);
        setIdentifiantDossier(idDossier);
        addPieceJointe(PieceJointeType.LETTRE, URL, PJ);
        return publier();
    }

    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return EppDetailCommJSS02Page.class;
    }

}
