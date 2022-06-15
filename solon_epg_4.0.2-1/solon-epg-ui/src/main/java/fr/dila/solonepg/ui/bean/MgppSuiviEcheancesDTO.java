package fr.dila.solonepg.ui.bean;

import fr.dila.st.core.client.AbstractMapDTO;
import java.util.Calendar;

public class MgppSuiviEcheancesDTO extends AbstractMapDTO {
    private static final long serialVersionUID = 1L;

    private static final String FICHE_ID = "ficheId";
    private static final String NOR = "nor";
    private static final String OBJET = "objet";
    private static final String DATE_DEPOT_EFFECTIF = "dateDepotEffectif";
    private static final String DESTINATAIRE_1_RAPPORT = "destinataire1Rapport";

    public MgppSuiviEcheancesDTO() {
        super();
    }

    public String getFicheId() {
        return getString(FICHE_ID);
    }

    public void setFicheId(String ficheId) {
        put(FICHE_ID, ficheId);
    }

    public String getNor() {
        return getString(NOR);
    }

    public void setNor(String numeroOrdre) {
        put(NOR, numeroOrdre);
    }

    public String getObjet() {
        return getString(OBJET);
    }

    public void setObjet(String objet) {
        put(OBJET, objet);
    }

    public Calendar getDateDepotEffectif() {
        return getCalendar(DATE_DEPOT_EFFECTIF);
    }

    public void setDateDepotEffectif(Calendar dateDepotEffectif) {
        put(DATE_DEPOT_EFFECTIF, dateDepotEffectif);
    }

    public String getDestinataire1Rapport() {
        return getString(DESTINATAIRE_1_RAPPORT);
    }

    public void setDestinataire1Rapport(String destinataire1Rapport) {
        put(DESTINATAIRE_1_RAPPORT, destinataire1Rapport);
    }

    @Override
    public String getType() {
        return "SuiviEcheances";
    }

    @Override
    public String getDocIdForSelection() {
        return null;
    }
}
