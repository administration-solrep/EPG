/**
 * 
 */
package fr.dila.solonepg.core.administration;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.administration.BulletinOfficiel;
import fr.dila.solonepg.api.constant.SolonEpgBulletinOfficielConstants;
import fr.dila.st.core.domain.STDomainObjectImpl;

/**
 * BulletinOfficiel Implementation.
 * 
 * @author asatre
 * 
 */
public class BulletinOfficielImpl extends STDomainObjectImpl implements BulletinOfficiel {

	private static final long serialVersionUID = 7673491105496472833L;

	public BulletinOfficielImpl(DocumentModel document) {
		super(document);
	}

	@Override
	public String getNor() {
		return getStringProperty(SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_SCHEMA, SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_NOR);
	}

	@Override
	public void setNor(String nor) {
		//on force en MAJ
		setProperty(SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_SCHEMA, SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_NOR, nor.toUpperCase());
	}

	@Override
	public String getIntitule() {
		return getStringProperty(SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_SCHEMA, SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_INTITULE);
	}

	@Override
	public void setIntitule(String intitule) {
		setProperty(SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_SCHEMA, SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_INTITULE, intitule);
	}

	@Override
	public String getEtat() {
		return getStringProperty(SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_SCHEMA, SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_ETAT);
	}

	@Override
	public void setEtat(String etat) {
		setProperty(SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_SCHEMA, SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_ETAT, etat);
	}

}
