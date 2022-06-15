package fr.dila.solonepg.ui.bean;

import fr.dila.solonepg.ui.th.bean.EpgCorbeilleContentListForm;
import fr.dila.st.ui.bean.ColonneInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EpgCorbeilleContentList {
    private final List<ColonneInfo> listeColonnes = new ArrayList<>();

    private List<Map<String, String>> liste = new ArrayList<>();

    private Integer nbTotal;

    private String titre;

    private String sousTitre;

    public EpgCorbeilleContentList() {
        this.nbTotal = 0;
    }

    public List<Map<String, String>> getListe() {
        return liste;
    }

    public void setListe(List<Map<String, String>> liste) {
        this.liste = liste;
    }

    public Integer getNbTotal() {
        return nbTotal;
    }

    public void setNbTotal(Integer nbTotal) {
        this.nbTotal = nbTotal;
    }

    public List<ColonneInfo> getListeColonnes() {
        return listeColonnes;
    }

    public List<ColonneInfo> getListeColonnes(EpgCorbeilleContentListForm form) {
        buildColonnes(form);
        return listeColonnes;
    }

    private void buildColonnes(EpgCorbeilleContentListForm form) {
        listeColonnes.clear();

        if (form != null) {
            listeColonnes.add(
                new ColonneInfo(
                    "label.content.header.numeroNor",
                    true,
                    EpgCorbeilleContentListForm.NOR_TRI,
                    form.getNor()
                )
            );
            listeColonnes.add(
                new ColonneInfo(
                    "label.content.header.titreActe",
                    true,
                    EpgCorbeilleContentListForm.TITRE_ACTE_TRI,
                    form.getTitreActe()
                )
            );
            listeColonnes.add(
                new ColonneInfo(
                    "label.content.header.dateArrivee",
                    true,
                    EpgCorbeilleContentListForm.DATE_ARRIVEE_TRI,
                    form.getDateArrivee()
                )
            );
            listeColonnes.add(
                new ColonneInfo(
                    "label.content.header.datePublicationSouhaitee",
                    true,
                    EpgCorbeilleContentListForm.DATE_PUB_TRI,
                    form.getDatePub()
                )
            );
            listeColonnes.add(
                new ColonneInfo(
                    "label.content.header.typeActe",
                    true,
                    EpgCorbeilleContentListForm.TYPE_ACTE_TRI,
                    form.getTypeActe()
                )
            );
        }
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getSousTitre() {
        return sousTitre;
    }

    public void setSousTitre(String sousTitre) {
        this.sousTitre = sousTitre;
    }
}
