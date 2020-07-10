package fr.dila.solonmgpp.core.domain;

import java.io.Serializable;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.nuxeo.common.utils.Base64;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.constant.SolonEpgParametrageApplicationConstants;
import fr.dila.solonepg.core.administration.ParametrageApplicationImpl;
import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;

/**
 * Implementation de {@link ParametrageMgpp}
 * 
 * @author asatre
 * 
 */
public class ParametrageMgppImpl implements ParametrageMgpp, Serializable {

    private static final long serialVersionUID = -5991496049977912872L;

	private static final STLogger	LOGGER				= STLogFactory.getLog(ParametrageMgppImpl.class);

    private final DocumentModel document;

    public ParametrageMgppImpl(DocumentModel doc) {
        this.document = doc;
    }

    @Override
    public DocumentModel getDocument() {
        return document;
    }

    @Override
    public String getUrlEpp() {
        return PropertyUtil.getStringProperty(document, SCHEMA, URL_EPP);
    }

    @Override
    public void setUrlEpp(String urlEpp) {
        PropertyUtil.setProperty(document, SCHEMA, URL_EPP, urlEpp);
    }

    @Override
    public String getLoginEpp() {
        return PropertyUtil.getStringProperty(document, SCHEMA, LOGIN_EPP);
    }

    @Override
    public void setLoginEpp(String loginEpp) {
        PropertyUtil.setProperty(document, SCHEMA, LOGIN_EPP, loginEpp);
    }

    @Override
    public String getPassEpp() {
		String passEpp = PropertyUtil.getStringProperty(document, SCHEMA, PASSWORD_EPP);
		if (passEpp != null) {
			try {
				// IV length: must be 16 bytes long
				IvParameterSpec iv = new IvParameterSpec("Randominit param".getBytes("UTF-8"));
				SecretKeySpec skeySpec = getSecretKey();

				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
				cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

				byte[] original = cipher.doFinal(Base64.decode(passEpp));

				return new String(original);
			} catch (Exception ex) {
				LOGGER.error(STLogEnumImpl.FAIL_GET_META_DONNEE_TEC, ex);
			}
		}
		return null;
    }

    @Override
    public void setPassEpp(String passEpp) throws ClientException {
		Cipher cipher;
		byte[] valueStored;
		try {
			valueStored = passEpp.getBytes("UTF-8");
			// IV length: must be 16 bytes long
			IvParameterSpec iv = new IvParameterSpec("Randominit param".getBytes("UTF-8"));
			SecretKeySpec skeySpec = getSecretKey();
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			valueStored = cipher.doFinal(passEpp.getBytes());
			PropertyUtil.setProperty(document, SCHEMA, PASSWORD_EPP, Base64.encodeBytes(valueStored));
		} catch (Exception e) {
			LOGGER.error(STLogEnumImpl.FAIL_SAVE_PWD_TEC, e);
			throw new ClientException("Erreur lors du cryptage du mot de passe");
		}
    }

	private SecretKeySpec getSecretKey() {
		SecretKeyFactory factory;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec("A random passphrase to help securing this".toCharArray(),
					"Salt is a movie with Angelina Jolie".getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			return new SecretKeySpec(tmp.getEncoded(), "AES");
		} catch (Exception e) {
			LOGGER.error(STLogEnumImpl.FAIL_GENERATE_KEY_TEC, e);
		}
		return null;
	}

    @Override
    public Long getNbJourAffichable() {
        return PropertyUtil.getLongProperty(document, SCHEMA, NB_JOUR_AFFICHABLE);
    }

    @Override
    public void setNbJourAffichable(Long nbJourAffichable) {
        PropertyUtil.setProperty(document, SCHEMA, NB_JOUR_AFFICHABLE, nbJourAffichable);
    }
    
    @Override
    public String getNomAN() {
        return PropertyUtil.getStringProperty(document, SCHEMA, NOM_AN);
    }
    
    @Override
    public void setNomAN(String nomAN) {
        PropertyUtil.setProperty(document, SCHEMA, NOM_AN, nomAN);
    }
    
    @Override
    public String getNomSenat() {
        return PropertyUtil.getStringProperty(document, SCHEMA, NOM_SENAT);
    }
    
    @Override
    public void setNomSenat(String nomSenat) {
        PropertyUtil.setProperty(document, SCHEMA, NOM_SENAT, nomSenat);
    }

    @Override
    public Long getDelai() {
        return PropertyUtil.getLongProperty(document, SCHEMA, DELAI);
    }

    @Override
    public void setDelai(Long delai) {
        PropertyUtil.setProperty(document, SCHEMA, DELAI, delai);
    }

    @Override
    public String getAuteurLex01() {
        return PropertyUtil.getStringProperty(document, SCHEMA, AUTEUR_LEX01);
    }

    @Override
    public void setAuteurLex01(String auteurLex01) {
        PropertyUtil.setProperty(document, SCHEMA, AUTEUR_LEX01, auteurLex01);
    }
    
    @Override
    public String getTexteLibreListeOep() {
        return PropertyUtil.getStringProperty(document, SCHEMA, TEXTE_LIBRE_LISTE_OEP);
    }

    @Override
    public void setTexteLibreListeOep(String texteLibreListeOep) {
        PropertyUtil.setProperty(document, SCHEMA, TEXTE_LIBRE_LISTE_OEP, texteLibreListeOep);
    }
    
    @Override
    public Long getDelaiPurgeCalendrier() {
        return PropertyUtil.getLongProperty(document, SCHEMA, DELAI_PURGE_CALENDRIER);
    }

    @Override
    public void setDelaiPurgeCalendrier(Long delaiPurgeCalendrier) {
        PropertyUtil.setProperty(document, SCHEMA, DELAI_PURGE_CALENDRIER, delaiPurgeCalendrier);
    }

	@Override
	public Boolean isMinistre() {
		return PropertyUtil.getBooleanProperty(document, SCHEMA, MINISTRE_OR_SECRETAIRE);
	}

	@Override
	public void setIsMinistre(Boolean isMinistre) {
		PropertyUtil.setProperty(document, SCHEMA, MINISTRE_OR_SECRETAIRE, isMinistre);
	}

	@Override
	public Long getFiltreDateCreationLoi() {
		return PropertyUtil.getLongProperty(document, SCHEMA, FILTRE_DATE_CREATION_LOI);
	}

	@Override
	public void setFiltreDateCreationLoi(Long filtreDateCreationLoi) {
		 PropertyUtil.setProperty(document, SCHEMA, FILTRE_DATE_CREATION_LOI,filtreDateCreationLoi);
		
	}

}
