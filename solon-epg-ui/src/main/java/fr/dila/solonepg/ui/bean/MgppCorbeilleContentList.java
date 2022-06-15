package fr.dila.solonepg.ui.bean;

import fr.dila.solonepg.ui.enums.mgpp.MgppCorbeilleFiltreEnum;
import fr.dila.solonepg.ui.th.bean.MgppCorbeilleContentListForm;
import fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName;
import fr.dila.st.ui.bean.ColonneInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MgppCorbeilleContentList implements MgppExportable {
    private final List<ColonneInfo> listeColonnes = new ArrayList<>();

    private List<Map<String, String>> liste = new ArrayList<>();

    private Integer nbTotal;

    private String titre;

    private String sousTitre;

    public MgppCorbeilleContentList() {
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

    @Override
    public List<ColonneInfo> getListeColonnes() {
        return listeColonnes;
    }

    public List<ColonneInfo> getListeColonnes(MgppCorbeilleContentListForm form, MgppCorbeilleName corbeilleId) {
        buildColonnes(form, corbeilleId);
        return listeColonnes;
    }

    public List<ColonneInfo> getListeColonnes(MgppCorbeilleContentListForm form, MgppCorbeilleFiltreEnum filtres) {
        buildColonnes(form, filtres);
        return listeColonnes;
    }

    public void buildColonnes(MgppCorbeilleContentListForm form, MgppCorbeilleName corbeilleId) {
        buildColonnes(form, MgppCorbeilleFiltreEnum.fromCorbeille(corbeilleId));
    }

    private void buildColonnes(MgppCorbeilleContentListForm form, MgppCorbeilleFiltreEnum filtres) {
        listeColonnes.clear();

        if (form != null) {
            listeColonnes.addAll(filtres.getLstCorbeillesColonnes(form));
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

    @Override
    public String getExportName() {
        return titre;
    }

    @Override
    public List<List<String>> getDataForExport() {
        return liste
            .stream()
            // La liste contient l'uid en premier élément mais il ne le faut pas dans l'export; on le skip donc
            .map(stringStringMap -> stringStringMap.values().stream().skip(1).collect(Collectors.toList()))
            .collect(Collectors.toList());
    }
}
