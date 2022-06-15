package fr.dila.solonepg.ui.bean.dossier.bordereau;

import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.SwBean;
import java.io.Serializable;
import java.util.Calendar;

@SwBean
public class ParutionBoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NxProp(docType = "", xpath = RetourDilaConstants.RETOUR_DILA_DATE_PARUTION_BO_PROPERTY)
    private Calendar dateBO;

    @NxProp(docType = "", xpath = RetourDilaConstants.RETOUR_DILA_NUMERO_TEXTE_PARUTION_BO_PROPERTY)
    protected String numeroTextBO;

    @NxProp(docType = "", xpath = RetourDilaConstants.RETOUR_DILA_PAGE_BO_PROPERTY)
    protected Long pageBO;

    public ParutionBoDTO() {}

    public Calendar getDateBO() {
        return dateBO;
    }

    public void setDateBO(Calendar dateBO) {
        this.dateBO = dateBO;
    }

    public String getNumeroTextBO() {
        return numeroTextBO;
    }

    public void setNumeroTextBO(String numeroTextBO) {
        this.numeroTextBO = numeroTextBO;
    }

    public Long getPageBO() {
        return pageBO;
    }

    public void setPageBO(Long pageBO) {
        this.pageBO = pageBO;
    }
}
