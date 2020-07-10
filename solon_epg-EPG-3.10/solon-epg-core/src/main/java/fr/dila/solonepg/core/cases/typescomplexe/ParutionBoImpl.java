package fr.dila.solonepg.core.cases.typescomplexe;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;

import fr.dila.solonepg.api.cases.typescomplexe.ParutionBo;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.st.core.domain.ComplexeTypeImpl;

/**
 * Implementation du type complexe parution Bo;
 * 
 * @author arolin
 */
public class ParutionBoImpl extends ComplexeTypeImpl implements ParutionBo {

    private static final long serialVersionUID = 1L;

    protected String numeroTexteParutionBo;
    
    protected Calendar dateParutionBo;
    
    protected Long pageParutionBo;

    public ParutionBoImpl(){
        super();
    }
    
    public ParutionBoImpl(Map<String, Serializable> serializableMap){
        if (serializableMap.get(RetourDilaConstants.RETOUR_DILA_NUMERO_TEXTE_PARUTION_BO_PROPERTY) instanceof String) {
            this.numeroTexteParutionBo = (String) serializableMap.get(RetourDilaConstants.RETOUR_DILA_NUMERO_TEXTE_PARUTION_BO_PROPERTY);
        }
        if (serializableMap.get(RetourDilaConstants.RETOUR_DILA_DATE_PARUTION_BO_PROPERTY) instanceof Calendar) {
            this.dateParutionBo = (Calendar) serializableMap.get(RetourDilaConstants.RETOUR_DILA_DATE_PARUTION_BO_PROPERTY);
        }
        if (serializableMap.get(RetourDilaConstants.RETOUR_DILA_PAGE_BO_PROPERTY) instanceof Long) {
            this.pageParutionBo = (Long) serializableMap.get(RetourDilaConstants.RETOUR_DILA_PAGE_BO_PROPERTY);
        }
    }

    @Override
    public void setSerializableMap(Map<String, Serializable> serializableMap) {
        Serializable numeroTexteParutionBo = serializableMap.get(RetourDilaConstants.RETOUR_DILA_NUMERO_TEXTE_PARUTION_BO_PROPERTY);
        Serializable dateParutionBo = serializableMap.get(RetourDilaConstants.RETOUR_DILA_DATE_PARUTION_BO_PROPERTY);
        Serializable pageParutionBo = serializableMap.get(RetourDilaConstants.RETOUR_DILA_PAGE_BO_PROPERTY);
        //set the map
        super.setSerializableMap(serializableMap);
        //set all the field
        setNumeroTexteParutionBo((String)numeroTexteParutionBo);
        setDateParutionBo((Calendar)dateParutionBo);
        setPageParutionBo((Long)pageParutionBo);
    }

    @Override
    public String getNumeroTexteParutionBo() {
        return numeroTexteParutionBo;
    }

    @Override
    public void setNumeroTexteParutionBo(String numeroTexteParutionBo) {
        this.numeroTexteParutionBo = numeroTexteParutionBo;
        put(RetourDilaConstants.RETOUR_DILA_NUMERO_TEXTE_PARUTION_BO_PROPERTY,numeroTexteParutionBo);
    }

    @Override
    public Calendar getDateParutionBo() {
        return dateParutionBo;
    }

    @Override
    public void setDateParutionBo(Calendar dateParutionBo) {
        this.dateParutionBo = dateParutionBo;
        put(RetourDilaConstants.RETOUR_DILA_DATE_PARUTION_BO_PROPERTY,dateParutionBo);
    }

    @Override
    public Long getPageParutionBo() {
        return pageParutionBo;
    }

    @Override
    public void setPageParutionBo(Long pageParutionBo) {
        this.pageParutionBo = pageParutionBo;
        put(RetourDilaConstants.RETOUR_DILA_PAGE_BO_PROPERTY,pageParutionBo);
    }

}
