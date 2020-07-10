package fr.dila.solonepg.core.cases.typescomplexe;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;

import fr.dila.solonepg.api.cases.typescomplexe.LigneProgrammationHabilitation;
import fr.dila.solonepg.api.constant.LigneProgrammationHabilitationConstants;
import fr.dila.st.core.domain.ComplexeTypeImpl;

/**
 * {@link LigneProgrammationHabilitation} Implementation.
 * 
 * @author asatre
 * 
 */
public class LigneProgrammationHabilitationImpl extends ComplexeTypeImpl implements LigneProgrammationHabilitation {

    private static final long serialVersionUID = -7026027771349205057L;

    private String numeroOrdre;
    private String ministerePilote;
    private String observation;
    private Boolean codification;
    private String article;
    private String objetRIM;
    private String typeHabilitation;
    private String convention;
    private Calendar dateTerme;
    private String conventionDepot;
    private Calendar datePrevisionnelleCM;
    private String legislature;
    private Calendar datePrevisionnelleSaisineCE;

    public LigneProgrammationHabilitationImpl() {
        super();
    }

    public LigneProgrammationHabilitationImpl(Map<String, Serializable> serializableMap) {
        if (serializableMap.get(LigneProgrammationHabilitationConstants.P_DATE_PREV_SAISINE_CE) instanceof Calendar) {
            this.datePrevisionnelleSaisineCE = (Calendar) serializableMap.get(LigneProgrammationHabilitationConstants.P_DATE_PREV_SAISINE_CE);
        }
        if (serializableMap.get(LigneProgrammationHabilitationConstants.P_MINISTERE_PILOTE) instanceof String) {
            this.ministerePilote = (String) serializableMap.get(LigneProgrammationHabilitationConstants.P_MINISTERE_PILOTE);
        }
        if (serializableMap.get(LigneProgrammationHabilitationConstants.P_NUMERO_ORDRE) instanceof String) {
            this.numeroOrdre = (String) serializableMap.get(LigneProgrammationHabilitationConstants.P_NUMERO_ORDRE);
        }
        if (serializableMap.get(LigneProgrammationHabilitationConstants.P_OBSERVATION) instanceof String) {
            this.observation = (String) serializableMap.get(LigneProgrammationHabilitationConstants.P_OBSERVATION);
        }
        if (serializableMap.get(LigneProgrammationHabilitationConstants.P_CODIFICATION) instanceof Boolean) {
            this.codification = (Boolean) serializableMap.get(LigneProgrammationHabilitationConstants.P_CODIFICATION);
        }
        if (serializableMap.get(LigneProgrammationHabilitationConstants.P_ARTICLE) instanceof String) {
            this.article = (String) serializableMap.get(LigneProgrammationHabilitationConstants.P_ARTICLE);
        }
        if (serializableMap.get(LigneProgrammationHabilitationConstants.P_OBJET_RIM) instanceof String) {
            this.objetRIM = (String) serializableMap.get(LigneProgrammationHabilitationConstants.P_OBJET_RIM);
        }
        if (serializableMap.get(LigneProgrammationHabilitationConstants.P_TYPE_HABILITATION) instanceof String) {
            this.typeHabilitation = (String) serializableMap.get(LigneProgrammationHabilitationConstants.P_TYPE_HABILITATION);
        }
        if (serializableMap.get(LigneProgrammationHabilitationConstants.P_CONVENTION) instanceof String) {
            this.convention = (String) serializableMap.get(LigneProgrammationHabilitationConstants.P_CONVENTION);
        }
        if (serializableMap.get(LigneProgrammationHabilitationConstants.P_DATE_TERME) instanceof Calendar) {
            this.dateTerme = (Calendar) serializableMap.get(LigneProgrammationHabilitationConstants.P_DATE_TERME);
        }
        if (serializableMap.get(LigneProgrammationHabilitationConstants.P_CONVENTION_DEPOT) instanceof String) {
            this.conventionDepot = (String) serializableMap.get(LigneProgrammationHabilitationConstants.P_CONVENTION_DEPOT);
        }
        if (serializableMap.get(LigneProgrammationHabilitationConstants.P_DATE_PREV_CM) instanceof Calendar) {
            this.datePrevisionnelleCM = (Calendar) serializableMap.get(LigneProgrammationHabilitationConstants.P_DATE_PREV_CM);
        }
        if (serializableMap.get(LigneProgrammationHabilitationConstants.P_LEGISLATURE) instanceof String) {
            this.legislature = (String) serializableMap.get(LigneProgrammationHabilitationConstants.P_LEGISLATURE);
        }
    }

    @Override
    public void setSerializableMap(Map<String, Serializable> serializableMap) {
        Serializable ministerePilote = serializableMap.get(LigneProgrammationHabilitationConstants.P_MINISTERE_PILOTE);
        Serializable numeroOrdre = serializableMap.get(LigneProgrammationHabilitationConstants.P_NUMERO_ORDRE);
        Serializable observation = serializableMap.get(LigneProgrammationHabilitationConstants.P_OBSERVATION);
        Serializable codification = serializableMap.get(LigneProgrammationHabilitationConstants.P_CODIFICATION);
        Serializable article = serializableMap.get(LigneProgrammationHabilitationConstants.P_ARTICLE);
        Serializable objetRIM = serializableMap.get(LigneProgrammationHabilitationConstants.P_OBJET_RIM);
        Serializable typeHabilitation = serializableMap.get(LigneProgrammationHabilitationConstants.P_TYPE_HABILITATION);
        Serializable convention = serializableMap.get(LigneProgrammationHabilitationConstants.P_CONVENTION);
        Serializable dateTerme = serializableMap.get(LigneProgrammationHabilitationConstants.P_DATE_TERME);
        Serializable conventionDepot = serializableMap.get(LigneProgrammationHabilitationConstants.P_CONVENTION_DEPOT);
        Serializable datePrevisionnelleCM = serializableMap.get(LigneProgrammationHabilitationConstants.P_DATE_PREV_CM);
        Serializable datePrevisionnelleSaisineCE = serializableMap.get(LigneProgrammationHabilitationConstants.P_DATE_PREV_SAISINE_CE);
        Serializable legislature = serializableMap.get(LigneProgrammationHabilitationConstants.P_LEGISLATURE);
        
        // set the map
        super.setSerializableMap(serializableMap);
        // set all the field
        setMinisterePilote((String) ministerePilote);
        setNumeroOrdre((String) numeroOrdre);
        setObservation((String) observation);
        setCodification((Boolean) codification);
        setArticle((String) article);
        setObjetRIM((String) objetRIM);
        setTypeHabilitation((String) typeHabilitation);
        setConvention((String) convention);
        setDateTerme((Calendar) dateTerme);
        setConventionDepot((String) conventionDepot);
        setDatePrevisionnelleCM((Calendar) datePrevisionnelleCM);
        setLegislature((String) legislature);
        setDatePrevisionnelleSaisineCE((Calendar) datePrevisionnelleSaisineCE);
        
    }

    @Override
    public String getNumeroOrdre() {
        return numeroOrdre;
    }

    @Override
    public void setNumeroOrdre(String numeroOrdre) {
        this.numeroOrdre = numeroOrdre;
        put(LigneProgrammationHabilitationConstants.P_NUMERO_ORDRE, numeroOrdre);
    }

    @Override
    public String getMinisterePilote() {
        return ministerePilote;
    }

    @Override
    public void setMinisterePilote(String ministerePilote) {
        this.ministerePilote = ministerePilote;
        put(LigneProgrammationHabilitationConstants.P_MINISTERE_PILOTE, ministerePilote);
    }

    @Override
    public Calendar getDatePrevisionnelleSaisineCE() {
        return datePrevisionnelleSaisineCE;
    }

    @Override
    public void setDatePrevisionnelleSaisineCE(Calendar datePrevisionnelleSaisineCE) {
        this.datePrevisionnelleSaisineCE = datePrevisionnelleSaisineCE;
        put(LigneProgrammationHabilitationConstants.P_DATE_PREV_SAISINE_CE, datePrevisionnelleSaisineCE);
    }


    @Override
    public String getObservation() {
        return observation;
    }

    @Override
    public void setObservation(String observation) {
        this.observation = observation;
        put(LigneProgrammationHabilitationConstants.P_OBSERVATION, observation);
    }

    @Override
    public Boolean getCodification() {
        return codification;
    }

    @Override
    public void setCodification(Boolean codification) {
        this.codification = codification;
        put(LigneProgrammationHabilitationConstants.P_CODIFICATION, codification);
        
    }

    @Override
    public String getArticle() {
        return article;
    }

    @Override
    public void setArticle(String article) {
        this.article = article;
        put(LigneProgrammationHabilitationConstants.P_ARTICLE, article);
    }

    @Override
    public String getObjetRIM() {
        return objetRIM;
    }

    @Override
    public void setObjetRIM(String objetRIM) {
        this.objetRIM = objetRIM;
        put(LigneProgrammationHabilitationConstants.P_OBJET_RIM, objetRIM);
    }

    @Override
    public String getTypeHabilitation() {
        return this.typeHabilitation;
    }

    @Override
    public void setTypeHabilitation(String typeHabilitation) {
        this.typeHabilitation = typeHabilitation;
        put(LigneProgrammationHabilitationConstants.P_TYPE_HABILITATION, typeHabilitation);
    }

    @Override
    public String getConvention() {
        return this.convention;
    }

    @Override
    public void setConvention(String convention) {
        this.convention = convention;
        put(LigneProgrammationHabilitationConstants.P_CONVENTION, convention);
    }

    @Override
    public Calendar getDateTerme() {
        return this.dateTerme;
    }

    @Override
    public void setDateTerme(Calendar dateTerme) {
        this.dateTerme = dateTerme;
        put(LigneProgrammationHabilitationConstants.P_DATE_TERME, dateTerme);
    }

    @Override
    public String getConventionDepot() {
        return this.conventionDepot;
    }

    @Override
    public void setConventionDepot(String conventionDepot) {
        this.conventionDepot = conventionDepot;
        put(LigneProgrammationHabilitationConstants.P_CONVENTION_DEPOT, conventionDepot);
    }

    @Override
    public Calendar getDatePrevisionnelleCM() {
        return this.datePrevisionnelleCM;
    }

    @Override
    public void setDatePrevisionnelleCM(Calendar datePrevisionnelleCM) {
        this.datePrevisionnelleCM = datePrevisionnelleCM;
        put(LigneProgrammationHabilitationConstants.P_DATE_PREV_CM, datePrevisionnelleCM);
    }

    @Override
    public String getLegislature() {
        return this.legislature;
    }

    @Override
    public void setLegislature(String legislature) {
        this.legislature = legislature;
        put(LigneProgrammationHabilitationConstants.P_LEGISLATURE, legislature);
    }
}
