package fr.dila.solonepg.ui.bean;

import fr.dila.solonepg.ui.th.bean.EpgSqueletteListForm;
import fr.dila.ss.ui.bean.fdr.ModeleFDRList;
import fr.dila.st.ui.bean.ColonneInfo;
import java.util.ArrayList;
import java.util.List;

public class EpgSqueletteList extends ModeleFDRList {
    private static final String COLUMN_TYPE_ACTE = "typeActe";

    private List<SqueletteDTO> listeSquelettes = new ArrayList<>();

    public EpgSqueletteList() {
        super();
    }

    public List<SqueletteDTO> getListeSquelettes() {
        return listeSquelettes;
    }

    public void setListeSquelettes(List<SqueletteDTO> listeSquelettes) {
        this.listeSquelettes = listeSquelettes;
    }

    public void buildColonnes(EpgSqueletteListForm form) {
        listeColonnes.clear();

        if (form != null) {
            listeColonnes.add(
                new ColonneInfo(
                    "modeleFDR.content.header.etat",
                    true,
                    true,
                    COLUMN_ETAT,
                    form.getEtat(),
                    false,
                    true,
                    form.getEtatOrder()
                )
            );
            listeColonnes.add(
                new ColonneInfo(
                    "modeleFDR.content.header.intitule",
                    true,
                    true,
                    COLUMN_INTITULE,
                    form.getIntitule(),
                    false,
                    true,
                    form.getIntituleOrder()
                )
            );
            listeColonnes.add(
                new ColonneInfo(
                    "modeleFDR.content.header.typeActe",
                    true,
                    true,
                    COLUMN_TYPE_ACTE,
                    form.getTypeActe(),
                    true,
                    true,
                    form.getTypeActeOrder()
                )
            );
            listeColonnes.add(
                new ColonneInfo(
                    "modeleFDR.content.header.auteur",
                    true,
                    true,
                    COLUMN_AUTEUR,
                    form.getAuteur(),
                    false,
                    true,
                    form.getAuteurOrder()
                )
            );
            listeColonnes.add(
                new ColonneInfo(
                    "modeleFDR.content.header.derniereModif",
                    true,
                    true,
                    COLUMN_DATE_MODIF,
                    form.getDerniereModif(),
                    true,
                    true,
                    form.getDerniereModifOrder()
                )
            );
            // Colonne actions
            listeColonnes.add(new ColonneInfo("header.label.actions", false, true, false, false));
        }
    }
}
