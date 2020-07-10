package fr.dila.solonepg.core.administration;

import java.security.spec.KeySpec;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.nuxeo.common.utils.Base64;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.administration.ParametrageApplication;
import fr.dila.solonepg.api.constant.SolonEpgParametrageApplicationConstants;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.domain.STDomainObjectImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.PropertyUtil;

/**
 * Parametrage Application Implémentation.
 * 
 * @author arolin
 */
public class ParametrageApplicationImpl extends STDomainObjectImpl implements ParametrageApplication {

    private static final long serialVersionUID = 1L;

	private static final STLogger	LOGGER				= STLogFactory.getLog(ParametrageApplicationImpl.class);

    /**
     * @param doc
     */
    public ParametrageApplicationImpl(DocumentModel doc) {
        super(doc);
    }

    //*************************************************************
    // Profil Utilisateur favoris de l'utilisateurs property
    //*************************************************************
    
    @Override
    public Long getNbDossierPage() {
        return PropertyUtil.getLongProperty(document, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_NB_DOSSIER_PAGE_PROPERTY);
    }

    @Override
    public void setNbDossierPage(Long nbDossierPage) {
        setProperty(SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_NB_DOSSIER_PAGE_PROPERTY, nbDossierPage);
    }

    @Override
    public Long getDateRafraichissementCorbeille() {
        return PropertyUtil.getLongProperty(document, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DELAI_RAFRAICHISSEMENT_CORBEILLE_PROPERTY);
    }

    @Override
    public void setDateRafraichissementCorbeille(Long dateRafraichissementCorbeille) {
        setProperty(SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DELAI_RAFRAICHISSEMENT_CORBEILLE_PROPERTY, dateRafraichissementCorbeille);
    }

    @Override
    public List<String> getMetadonneDisponibleColonneCorbeille() {
        return PropertyUtil.getStringListProperty(document, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_META_DISPO_CORBEILLE_PROPERTY);
    }

    @Override
    public void setMetadonneDisponibleColonneCorbeille(List<String> metadonneDisponibleColonneCorbeille) {
        setProperty(SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_META_DISPO_CORBEILLE_PROPERTY, metadonneDisponibleColonneCorbeille);
    }

    @Override
    public Long getNbDerniersResultatsConsultes() {
        return PropertyUtil.getLongProperty(document, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_NB_DERNIERS_RESULTATS_PROPERTY);
    }

    @Override
    public void setNbDerniersResultatsConsultes(Long nbDerniersResultatsConsultes) {
        setProperty(SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_NB_DERNIERS_RESULTATS_PROPERTY, nbDerniersResultatsConsultes);
    }

    @Override
    public Long getNbFavorisConsultation() {
        return PropertyUtil.getLongProperty(document, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_NB_FAVORIS_CONSULTATION_PROPERTY);
    }

    @Override
    public void setNbFavorisConsultation(Long nbFavorisConsultation) {
        setProperty(SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_NB_FAVORIS_CONSULTATION_PROPERTY, nbFavorisConsultation);
    }

    @Override
    public Long getNbFavorisRecherche() {
        return PropertyUtil.getLongProperty(document, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_NB_FAVORIS_RECHERCHE_PROPERTY);
    }

    @Override
    public void setNbFavorisRecherche(Long nbFavorisRecherche) {
        setProperty(SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_NB_FAVORIS_RECHERCHE_PROPERTY, nbFavorisRecherche);
    }

    @Override
    public Long getNbDossiersSignales() {
        return PropertyUtil.getLongProperty(document, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_NB_DOSSIERS_SIGNALES_PROPERTY);
    }

    @Override
    public void setNbDossiersSignales(Long nbDossiersSignales) {
        setProperty(SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_NB_DOSSIERS_SIGNALES_PROPERTY, nbDossiersSignales);
    }

    @Override
    public Long getNbTableauxDynamiques() {
        return PropertyUtil.getLongProperty(document, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_NB_TABLEAUX_DYNAMIQUES_PROPERTY);
    }

    @Override
    public void setNbTableauxDynamiques(Long nbTableauxDynamiques) {
        setProperty(SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_NB_TABLEAUX_DYNAMIQUES_PROPERTY, nbTableauxDynamiques);
    }

    @Override
    public Long getDelaiEnvoiMailMaintienAlerte() {
        return PropertyUtil.getLongProperty(document, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DELAI_ENVOI_MAIL_ALERTE_PROPERTY);
    }

    @Override
    public void setDelaiEnvoiMailMaintienAlerte(Long delaiEnvoiMailMaintienAlerte) {
        setProperty(SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DELAI_ENVOI_MAIL_ALERTE_PROPERTY, delaiEnvoiMailMaintienAlerte);
    }
    
    @Override
    public Long getDelaiAffichageMessage() {
      return PropertyUtil.getLongProperty(document, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DELAI_AFFICHAGE_MESSAGE_PROPERTY);
    }
    
    @Override
    public void setDelaiAffichageMessage(Long delaiAffichageMessage) {
      setProperty(SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DELAI_AFFICHAGE_MESSAGE_PROPERTY, delaiAffichageMessage);
    }

	@Override
	public List<String> getSuffixesMailsAutorises() {
		return PropertyUtil.getStringListProperty(document, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SUFF_MAILS_AUTORISES_PROPERTY);
	}

	@Override
	public void setSuffixesMailsAutorises(List<String> suffixesMailsAutorises) {
		setProperty(SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA, SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SUFF_MAILS_AUTORISES_PROPERTY, suffixesMailsAutorises);
	}

	@Override
	public String getUrlEppInfosParl() {
		return PropertyUtil.getStringProperty(document,
				SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA,
				SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_URL_EPP_INFOS_PARL_PROPERTY);
	}

	@Override
	public void setUrlEppInfosParl(String urlEppInfosParl) {
		setProperty(SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA,
				SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_URL_EPP_INFOS_PARL_PROPERTY,
				urlEppInfosParl);
	}

	@Override
	public String getLoginEppInfosParl() {
		return PropertyUtil.getStringProperty(document,
				SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA,
				SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_LOGIN_EPP_INFOS_PARL_PROPERTY);
	}

	@Override
	public void setLoginEppInfosParl(String loginEppInfosParl) {
		setProperty(SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA,
				SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_LOGIN_EPP_INFOS_PARL_PROPERTY,
				loginEppInfosParl);
	}

	@Override
	public String getPassEppInfosParl() {
		String passEppInfosParl = PropertyUtil.getStringProperty(document,
				SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA,
				SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_PASS_EPP_INFOS_PARL_PROPERTY);
		if (passEppInfosParl != null) {
			try {
				// IV length: must be 16 bytes long
				IvParameterSpec iv = new IvParameterSpec("Randominit param".getBytes("UTF-8"));
				SecretKeySpec skeySpec = getSecretKey();

				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
				cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

				byte[] original = cipher.doFinal(Base64.decode(passEppInfosParl));

				return new String(original);
			} catch (Exception ex) {
				LOGGER.error(STLogEnumImpl.FAIL_GET_META_DONNEE_TEC, ex);
			}
		}
		return null;
	}

	@Override
	public void setPassEppInfosParl(String passEppInfosParl) throws ClientException {
		Cipher cipher;
		byte[] valueStored;
		try {
			valueStored = passEppInfosParl.getBytes("UTF-8");
			// IV length: must be 16 bytes long
			IvParameterSpec iv = new IvParameterSpec("Randominit param".getBytes("UTF-8"));
			SecretKeySpec skeySpec = getSecretKey();
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			valueStored = cipher.doFinal(passEppInfosParl.getBytes());
			setProperty(SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA,
					SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_PASS_EPP_INFOS_PARL_PROPERTY,
					Base64.encodeBytes(valueStored));
		} catch (Exception e) {
			LOGGER.error(STLogEnumImpl.FAIL_SAVE_PWD_TEC, e);
			throw new ClientException("Erreur lors du cryptage du mot de passe");
		}
	}
    
	private SecretKeySpec getSecretKey() {
		SecretKeyFactory factory;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec("A random passphrase to help securing this".toCharArray(), "Salt is a movie with Angelina Jolie".getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			return new SecretKeySpec(tmp.getEncoded(), "AES");
		} catch (Exception e) {
			LOGGER.error(STLogEnumImpl.FAIL_GENERATE_KEY_TEC, e);
		}
		return null;
	}

}
