package fr.dila.solonepg.ui.bean;

import fr.dila.solonepg.ui.enums.SourceFichierEnum;
import fr.dila.st.ui.bean.DocumentDTO;

public class EpgDocumentDTO extends DocumentDTO {
    private boolean modifEnCours = Boolean.FALSE;
    private boolean nonTransmis = false;
    private SourceFichierEnum sourceFichier;

    public EpgDocumentDTO() {
        super();
    }

    public boolean getModifEnCours() {
        return modifEnCours;
    }

    public void setModifEnCours(boolean modifEnCours) {
        this.modifEnCours = modifEnCours;
    }

    public boolean getNonTransmis() {
        return nonTransmis;
    }

    public void setNonTransmis(boolean nonTransmis) {
        this.nonTransmis = nonTransmis;
    }

    public boolean getFdd() {
        return sourceFichier == SourceFichierEnum.FOND_DOSSIER;
    }

    public boolean getParapheur() {
        return sourceFichier == SourceFichierEnum.PARAPHEUR;
    }

    public void setSourceFichier(SourceFichierEnum source) {
        this.sourceFichier = source;
    }
}
