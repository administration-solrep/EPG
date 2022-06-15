package fr.dila.solonepg.core.cases;

import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.typescomplexe.ParutionBo;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.solonepg.core.cases.typescomplexe.ParutionBoImpl;
import fr.dila.solonepg.core.util.PropertyHelper;
import fr.dila.st.api.domain.ComplexeType;
import fr.dila.st.core.domain.STDomainObjectImpl;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Implementation de {@link RetourDila} .
 *
 * @author asatre
 *
 */
public class RetourDilaImpl extends STDomainObjectImpl implements RetourDila {
    private static final long serialVersionUID = 4376293619619241293L;

    public RetourDilaImpl(DocumentModel doc) {
        super(doc);
    }

    protected List<ParutionBo> getParutionBoProperty(String schema, String value) {
        List<ParutionBo> parutionBoList = new ArrayList<>();
        List<Map<String, Serializable>> parutionBOListSerializable = PropertyUtil.getMapStringSerializableListProperty(
            document,
            schema,
            value
        );
        if (parutionBOListSerializable != null) {
            for (Map<String, Serializable> parutionBo : parutionBOListSerializable) {
                parutionBoList.add(new ParutionBoImpl(parutionBo));
            }
        }
        return parutionBoList;
    }

    @Override
    public Boolean getIsPublicationEpreuvageDemandeSuivante() {
        return PropertyUtil.getBooleanProperty(
            document,
            RetourDilaConstants.RETOUR_DILA_SCHEMA,
            RetourDilaConstants.RETOUR_DILA_IS_PUBLICATION_EPREUVAGE_DEMANDE_SUIVANTE_PROPERTY
        );
    }

    @Override
    public void setIsPublicationEpreuvageDemandeSuivante(Boolean isPublicationEpreuvageDemandeSuivante) {
        setProperty(
            RetourDilaConstants.RETOUR_DILA_SCHEMA,
            RetourDilaConstants.RETOUR_DILA_IS_PUBLICATION_EPREUVAGE_DEMANDE_SUIVANTE_PROPERTY,
            isPublicationEpreuvageDemandeSuivante
        );
    }

    @Override
    public Calendar getDateParutionJorf() {
        return PropertyUtil.getCalendarProperty(
            document,
            RetourDilaConstants.RETOUR_DILA_SCHEMA,
            RetourDilaConstants.RETOUR_DILA_DATE_PARUTION_JORF_PROPERTY
        );
    }

    @Override
    public void setDateParutionJorf(Calendar dateParutionJorf) {
        setProperty(
            RetourDilaConstants.RETOUR_DILA_SCHEMA,
            RetourDilaConstants.RETOUR_DILA_DATE_PARUTION_JORF_PROPERTY,
            dateParutionJorf
        );
    }

    @Override
    public String getNumeroTexteParutionJorf() {
        return PropertyUtil.getStringProperty(
            document,
            RetourDilaConstants.RETOUR_DILA_SCHEMA,
            RetourDilaConstants.RETOUR_DILA_NUMERO_TEXTE_PARUTION_JORF_PROPERTY
        );
    }

    @Override
    public void setNumeroTexteParutionJorf(String numeroTexteParutionJorf) {
        setProperty(
            RetourDilaConstants.RETOUR_DILA_SCHEMA,
            RetourDilaConstants.RETOUR_DILA_NUMERO_TEXTE_PARUTION_JORF_PROPERTY,
            numeroTexteParutionJorf
        );
    }

    @Override
    public Long getPageParutionJorf() {
        return PropertyUtil.getLongProperty(
            document,
            RetourDilaConstants.RETOUR_DILA_SCHEMA,
            RetourDilaConstants.RETOUR_DILA_PAGE_PARUTION_JORF_PROPERTY
        );
    }

    @Override
    public void setPageParutionJorf(Long pageParutionJorf) {
        setProperty(
            RetourDilaConstants.RETOUR_DILA_SCHEMA,
            RetourDilaConstants.RETOUR_DILA_PAGE_PARUTION_JORF_PROPERTY,
            pageParutionJorf
        );
    }

    @Override
    public String getModeParution() {
        return PropertyUtil.getStringProperty(
            document,
            RetourDilaConstants.RETOUR_DILA_SCHEMA,
            RetourDilaConstants.RETOUR_DILA_MODE_PARUTION_PROPERTY
        );
    }

    @Override
    public void setModeParution(String modeParution) {
        setProperty(
            RetourDilaConstants.RETOUR_DILA_SCHEMA,
            RetourDilaConstants.RETOUR_DILA_MODE_PARUTION_PROPERTY,
            modeParution
        );
    }

    @Override
    public String getModeParutionLabel() {
        return PropertyUtil.getStringProperty(
            document,
            RetourDilaConstants.RETOUR_DILA_SCHEMA,
            RetourDilaConstants.RETOUR_DILA_MODE_PARUTION_LABEL_PROPERTY
        );
    }

    @Override
    public void setModeParutionLabel(String modeParution) {
        setProperty(
            RetourDilaConstants.RETOUR_DILA_SCHEMA,
            RetourDilaConstants.RETOUR_DILA_MODE_PARUTION_LABEL_PROPERTY,
            modeParution
        );
    }

    @Override
    public Calendar getDatePromulgation() {
        return PropertyUtil.getCalendarProperty(
            document,
            RetourDilaConstants.RETOUR_DILA_SCHEMA,
            RetourDilaConstants.RETOUR_DILA_DATE_PROMULGATION
        );
    }

    @Override
    public void setDatePromulgation(Calendar datePromulgation) {
        PropertyUtil.setProperty(
            document,
            RetourDilaConstants.RETOUR_DILA_SCHEMA,
            RetourDilaConstants.RETOUR_DILA_DATE_PROMULGATION,
            datePromulgation
        );
    }

    @Override
    public String getTitreOfficiel() {
        return PropertyUtil.getStringProperty(
            document,
            RetourDilaConstants.RETOUR_DILA_SCHEMA,
            RetourDilaConstants.RETOUR_DILA_TITRE_OFFICIEL
        );
    }

    @Override
    public void setTitreOfficiel(String titreOfficiel) {
        PropertyUtil.setProperty(
            document,
            RetourDilaConstants.RETOUR_DILA_SCHEMA,
            RetourDilaConstants.RETOUR_DILA_TITRE_OFFICIEL,
            titreOfficiel
        );
    }

    @Override
    public String getLegislaturePublication() {
        return PropertyUtil.getStringProperty(
            document,
            RetourDilaConstants.RETOUR_DILA_SCHEMA,
            RetourDilaConstants.RETOUR_DILA_LEGISLATURE_PUBLICATION
        );
    }

    @Override
    public void setLegislaturePublication(String legislaturePublication) {
        PropertyUtil.setProperty(
            document,
            RetourDilaConstants.RETOUR_DILA_SCHEMA,
            RetourDilaConstants.RETOUR_DILA_LEGISLATURE_PUBLICATION,
            legislaturePublication
        );
    }

    @Override
    public List<ParutionBo> getParutionBo() {
        return getParutionBoProperty(
            RetourDilaConstants.RETOUR_DILA_SCHEMA,
            RetourDilaConstants.RETOUR_DILA_PARUTION_BO_PROPERTY
        );
    }

    @Override
    public void setParutionBo(List<ParutionBo> parutionBoList) {
        //note
        PropertyHelper.setListSerializableProperty(
            document,
            RetourDilaConstants.RETOUR_DILA_SCHEMA,
            RetourDilaConstants.RETOUR_DILA_PARUTION_BO_PROPERTY,
            initParutionBoList(parutionBoList)
        );
    }

    protected List<ComplexeType> initParutionBoList(List<ParutionBo> parutionBoList) {
        // init serializable List
        List<ComplexeType> listeComplexType = new ArrayList<>();
        for (ParutionBo parutionBOMap : parutionBoList) {
            if (parutionBOMap.getSerializableMap().isEmpty()) {
                if (parutionBOMap.getDateParutionBo() != null) {
                    parutionBOMap.setDateParutionBo(parutionBOMap.getDateParutionBo());
                }
                parutionBOMap.setNumeroTexteParutionBo(parutionBOMap.getNumeroTexteParutionBo());
                if (parutionBOMap.getPageParutionBo() != null) {
                    parutionBOMap.setPageParutionBo(parutionBOMap.getPageParutionBo());
                }
            }
            //ajout à la liste
            listeComplexType.add(parutionBOMap);
        }
        return listeComplexType;
    }
}
