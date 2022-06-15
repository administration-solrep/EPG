package fr.dila.solonepg.ui.bean;

import fr.dila.st.ui.bean.ColonneInfo;
import java.util.ArrayList;
import java.util.List;

public class MgppEcheancierPromulgationList {
    private List<ColonneInfo> listeColonnes = new ArrayList<>();
    private List<MgppEcheancierPromulgationDTO> liste = new ArrayList<>();
    private Integer nbTotal = 0;

    public List<ColonneInfo> getListeColonnes() {
        buildColonnes();
        return listeColonnes;
    }

    public void setListeColonnes(List<ColonneInfo> listeColonnes) {
        this.listeColonnes = listeColonnes;
    }

    public List<MgppEcheancierPromulgationDTO> getListe() {
        return liste;
    }

    public void setListe(List<MgppEcheancierPromulgationDTO> liste) {
        this.liste = liste;
    }

    public Integer getNbTotal() {
        return nbTotal;
    }

    public void setNbTotal(Integer nbTotal) {
        this.nbTotal = nbTotal;
    }

    private void buildColonnes() {
        listeColonnes.clear();

        listeColonnes.add(new ColonneInfo("echeancier.promulgation.lois.label", false, true, false, true));
        listeColonnes.add(new ColonneInfo("echeancier.promulgation.cc.saisine.label", false, true, false, true));
        listeColonnes.add(new ColonneInfo("echeancier.promulgation.cc.decision.label", false, true, false, true));
        listeColonnes.add(new ColonneInfo("echeancier.promulgation.dateReception.label", false, true, false, true));
        listeColonnes.add(
            new ColonneInfo("echeancier.promulgation.demande.epreuve.jo.label", false, true, false, true)
        );
        listeColonnes.add(new ColonneInfo("echeancier.promulgation.retour.jo.label", false, true, false, true));
        listeColonnes.add(new ColonneInfo("echeancier.promulgation.envoi.relecture.label", false, true, false, true));
        listeColonnes.add(new ColonneInfo("echeancier.promulgation.retour.relecture.label", false, true, false, true));
        listeColonnes.add(new ColonneInfo("echeancier.promulgation.misaucontreseing.label", false, true, false, true));
        listeColonnes.add(new ColonneInfo("echeancier.promulgation.contreseing.pm.label", false, true, false, true));
        listeColonnes.add(new ColonneInfo("echeancier.promulgation.depart.elysee.label", false, true, false, true));
        listeColonnes.add(new ColonneInfo("echeancier.promulgation.retour.elysse.label", false, true, false, true));
        listeColonnes.add(new ColonneInfo("echeancier.promulgation.datelimite.label", false, true, false, true));
        listeColonnes.add(new ColonneInfo("echeancier.promulgation.publication.jo.label", false, true, false, true));
    }
}
