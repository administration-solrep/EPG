package fr.dila.solonepg.ui.bean.fdr;

import fr.dila.ss.ui.bean.fdr.ModeleFDRList;
import fr.dila.ss.ui.th.bean.ModeleFDRListForm;
import fr.dila.st.ui.bean.ColonneInfo;

public class EpgModeleFDRList extends ModeleFDRList {
    private static final String COLUMN_NUMERO = "numero";

    public EpgModeleFDRList() {
        super();
    }

    @Override
    public void buildColonnes(ModeleFDRListForm form) {
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
                    "modeleFDR.content.header.numero",
                    true,
                    true,
                    COLUMN_NUMERO,
                    form.getNumero(),
                    false,
                    true,
                    form.getNumeroOrder()
                )
            );
            listeColonnes.add(
                new ColonneInfo(
                    "modeleFDR.content.header.ministere",
                    false,
                    COLUMN_MINISTERE,
                    form.getMinistere(),
                    true,
                    true
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
            // Colonne lock
            listeColonnes.add(new ColonneInfo("header.label.lock", false, true, false, false));
            // Colonne actions
            listeColonnes.add(new ColonneInfo("header.label.actions", false, true, false, false));
        }
    }
}
