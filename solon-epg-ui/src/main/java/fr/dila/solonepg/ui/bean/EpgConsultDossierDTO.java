package fr.dila.solonepg.ui.bean;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.ss.ui.bean.SSConsultDossierDTO;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.ui.annot.NxProp;

public class EpgConsultDossierDTO extends SSConsultDossierDTO {
    @NxProp(
        xpath = STSchemaConstant.DOSSIER_SCHEMA_PREFIX + ":" + DossierSolonEpgConstants.DOSSIER_TITRE_ACTE_PROPERTY,
        docType = STConstant.DOSSIER_DOCUMENT_TYPE
    )
    private String titreActe = "";

    @NxProp(
        xpath = STSchemaConstant.DOSSIER_SCHEMA_PREFIX + ":" + DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PROPERTY,
        docType = STConstant.DOSSIER_DOCUMENT_TYPE
    )
    private String numeroNor = "";

    public EpgConsultDossierDTO() {
        super();
    }

    public String getTitreActe() {
        return titreActe;
    }

    public void setTitreActe(String titreActe) {
        this.titreActe = titreActe;
    }

    public String getNumeroNor() {
        return numeroNor;
    }

    public void setNumeroNor(String numeroNor) {
        this.numeroNor = numeroNor;
    }
}
