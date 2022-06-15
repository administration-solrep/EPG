package fr.dila.solonepg.ui.bean;

import fr.dila.st.ui.bean.IColonneInfo;
import java.util.List;

public interface IFilterableList {
    /** Renvoie la liste de colonnes filtrables. */
    List<IColonneInfo> getFilterableColonnes();
}
