/**
 * 
 */
package fr.dila.solonepg.core.dto.activitenormative;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.TexteTransposition;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.dto.TexteTranspositionDTO;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.util.StringUtil;

/**
 * 
 * implementation de {@link TexteTranspositionDTO}
 * 
 * @see TexteTransposition
 * @author asatre
 * 
 */
public class TexteTranspositionDTOImpl extends AbstractMapDTO implements Serializable, TexteTranspositionDTO {

    private static final String VALIDATE = "validate";
    private static final String ID = "id";
    private static final long serialVersionUID = -3759537132915019472L;

    private static final Log log = LogFactory.getLog(TexteTranspositionDTOImpl.class);

    public TexteTranspositionDTOImpl(String numeroNor) {
        // constructeur vide pour créer une ligne vide
        setNumeroNor(numeroNor);
        setValidation(Boolean.TRUE);
        setValidate(Boolean.FALSE);
    }

    public TexteTranspositionDTOImpl(TexteTransposition texteTransposition) {

        this(texteTransposition.getNumeroNor());
        setId(texteTransposition.getId());
        setNature(texteTransposition.getNature());
        setTypeActe(texteTransposition.getTypeActe());

        setIntitule(texteTransposition.getIntitule());
        setMinisterePilote(texteTransposition.getMinisterePilote());
        setEtapeSolon(texteTransposition.getEtapeSolon());
        setDatePublication(texteTransposition.getDatePublication() != null ? texteTransposition.getDatePublication().getTime() : null);
        setNumeroTextePublie(texteTransposition.getNumeroTextePublie());
        setTitreTextePublie(texteTransposition.getTitreTextePublie());
        setValidation(texteTransposition.hasValidation());
        setValidate(Boolean.FALSE);
    }

    @Override
    public TexteTransposition remapField(TexteTransposition texteTransposition) {

        if (getDatePublication() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(getDatePublication());
            texteTransposition.setDatePublication(cal);
        } else {
            texteTransposition.setDatePublication(null);
        }

        texteTransposition.setEtapeSolon(getEtapeSolon());
        texteTransposition.setIntitule(getIntitule());
        texteTransposition.setMinisterePilote(getMinisterePilote());
        texteTransposition.setNature(getNature());
        texteTransposition.setTypeActe(getTypeActe());
        texteTransposition.setNumeroNor(getNumeroNor());
        texteTransposition.setNumeroTextePublie(getNumeroTextePublie());
        texteTransposition.setTitreTextePublie(getTitreTextePublie());

        if (getValidate()) {
            texteTransposition.setValidation(Boolean.TRUE);
        }

        return texteTransposition;
    }

    @Override
    public void remapField(Dossier dossier, CoreSession session) {
        if (dossier != null) {
            setNature(dossier.getTypeActe());
            setTypeActe(dossier.getTypeActe());
            setNumeroNor(dossier.getNumeroNor());
            setIntitule(dossier.getTitreActe());
            setMinisterePilote(dossier.getMinistereResp());
            setEtapeSolon(setEtapeSolon(dossier, session));
            RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
            setDatePublication(retourDila.getDateParutionJorf() != null ? retourDila.getDateParutionJorf().getTime() : null);
            setNumeroTextePublie(retourDila.getNumeroTexteParutionJorf());
            setTitreTextePublie(retourDila.getTitreOfficiel());
        }
    }

    private String setEtapeSolon(Dossier dossier, CoreSession session) {

        try {
            List<String> listStepLabel = SolonEpgServiceLocator.getCorbeilleService().findCurrentStepsLabel(session, dossier.getDocument().getId());
            return StringUtil.join(listStepLabel, ", ", "");
        } catch (Exception e) {
            log.error("Erreur lors de la construction de l'etapeSolon pour le dossier " + dossier.getDocument().getId());
        }

        return null;
    }

    @Override
    public Boolean hasValidation() {
        return getBoolean(TexteMaitreConstants.HAS_VALIDATION);
    }

    @Override
    public void setValidation(Boolean hasValidation) {
        put(TexteMaitreConstants.HAS_VALIDATION, hasValidation);
    }

    @Override
    public String getNature() {
        return getString(TexteMaitreConstants.NATURE_TEXTE);
    }
    
    @Override
    public String getTypeActe() {
      return getString(TexteMaitreConstants.TYPE_ACTE);
    }

    @Override
    public void setNature(String nature) {
        put(TexteMaitreConstants.NATURE_TEXTE, nature);
    }
    
    @Override
    public void setTypeActe(String typeActe) {
      put(TexteMaitreConstants.TYPE_ACTE, typeActe);
    }

    @Override
    public String getNumeroNor() {
        return getString(TexteMaitreConstants.NUMERO_NOR);
    }

    @Override
    public void setNumeroNor(String numeroNor) {
        put(TexteMaitreConstants.NUMERO_NOR, numeroNor);
    }

    @Override
    public String getIntitule() {
        return getString(TexteMaitreConstants.INTITULE);
    }

    @Override
    public void setIntitule(String intitule) {
        put(TexteMaitreConstants.INTITULE, intitule);
    }

    @Override
    public String getMinisterePilote() {
        return getString(TexteMaitreConstants.MINISTERE_PILOTE);
    }

    @Override
    public void setMinisterePilote(String ministerePilote) {
        put(TexteMaitreConstants.MINISTERE_PILOTE, ministerePilote);
    }

    @Override
    public String getEtapeSolon() {
        return getString(TexteMaitreConstants.ETAPE_SOLON);
    }

    @Override
    public void setEtapeSolon(String etapeSolon) {
        put(TexteMaitreConstants.ETAPE_SOLON, etapeSolon);
    }

    @Override
    public Date getDatePublication() {
        return getDate(TexteMaitreConstants.DATE_PUBLICATION);
    }

    @Override
    public void setDatePublication(Date datePublication) {
        put(TexteMaitreConstants.DATE_PUBLICATION, datePublication);
    }

    @Override
    public String getNumeroTextePublie() {
        return getString(TexteMaitreConstants.NUMERO_TEXTE_PUBLIE);
    }

    @Override
    public void setNumeroTextePublie(String numeroTextePublie) {
        put(TexteMaitreConstants.NUMERO_TEXTE_PUBLIE, numeroTextePublie);
    }

    @Override
    public String getTitreTextePublie() {
        return getString(TexteMaitreConstants.TITRE_OFFICIEL);
    }

    @Override
    public void setTitreTextePublie(String titreTextePublie) {
        put(TexteMaitreConstants.TITRE_OFFICIEL, titreTextePublie);
    }

    @Override
    public String getId() {
        return getString(ID);
    }

    @Override
    public void setId(String id) {
        put(ID, id);
    }

    @Override
    public String getType() {
        return TexteTransposition.class.getSimpleName();
    }

    @Override
    public String getDocIdForSelection() {
        return getString(ID);
    }

    @Override
    public Boolean getValidate() {
        return getBoolean(VALIDATE);
    }

    @Override
    public void setValidate(Boolean validation) {
        put(VALIDATE, validation);
    }

}
