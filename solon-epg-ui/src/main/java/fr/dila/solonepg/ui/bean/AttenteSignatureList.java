package fr.dila.solonepg.ui.bean;

import fr.dila.solonepg.ui.enums.EpgOngletAttenteSignatureEnum;
import fr.dila.st.ui.bean.ColonneInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AttenteSignatureList {
    protected final List<ColonneInfo> listeColonnes = new ArrayList<>();
    protected List<AttenteSignatureDTO> liste = new ArrayList<>();
    protected Integer nbTotal = 0;

    private static final String HEADER_PREFIX = "attente.signature.label.header.";
    private static final List<String> COMMON_HEADERS = Arrays.asList(
        "titre",
        "nor",
        "publication.demandee",
        "arrivee.solon",
        "accord.pm",
        "accord.sgg",
        "date.jo",
        "delai.publication",
        "observation"
    );
    private static final List<String> LOIS_HEADERS = Arrays.asList(
        "arrivee.originale",
        "mise.en.signature",
        "retour.signature",
        "decret.pr",
        "envoi.pr",
        "retour.pr"
    );

    public AttenteSignatureList() {
        super();
    }

    public List<AttenteSignatureDTO> getListe() {
        return liste;
    }

    public void setListe(List<AttenteSignatureDTO> liste) {
        this.liste = liste;
    }

    public Integer getNbTotal() {
        return nbTotal;
    }

    public void setNbTotal(Integer nbTotal) {
        this.nbTotal = nbTotal;
    }

    public List<ColonneInfo> getListeColonnes(EpgOngletAttenteSignatureEnum onglet) {
        buildColonnes(onglet);
        return listeColonnes;
    }

    public void buildColonnes(EpgOngletAttenteSignatureEnum onglet) {
        listeColonnes.clear();
        listeColonnes.addAll(
            COMMON_HEADERS
                .stream()
                .map(header -> new ColonneInfo(HEADER_PREFIX + header, false, true, false, true))
                .collect(Collectors.toList())
        );
        if (onglet == EpgOngletAttenteSignatureEnum.LOIS) {
            listeColonnes.addAll(
                6,
                LOIS_HEADERS
                    .stream()
                    .map(header -> new ColonneInfo(HEADER_PREFIX + header, false, true, false, true))
                    .collect(Collectors.toList())
            );
        }
    }
}
