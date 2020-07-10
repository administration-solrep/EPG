package fr.dila.solonepg.core.cases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.ActiviteNormativeProgrammation;
import fr.dila.solonepg.api.cases.LigneProgrammation;
import fr.dila.solonepg.api.cases.typescomplexe.LigneProgrammationHabilitation;
import fr.dila.solonepg.api.constant.ActiviteNormativeProgrammationConstants;
import fr.dila.solonepg.api.constant.LigneProgrammationConstants;
import fr.dila.solonepg.core.cases.typescomplexe.LigneProgrammationHabilitationImpl;
import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.domain.STDomainObjectImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;

/**
 * Implementation de {@link ActiviteNormative} .
 * 
 * @author asatre
 * 
 */
public class ActiviteNormativeProgrammationImpl extends STDomainObjectImpl implements ActiviteNormativeProgrammation {

	private static final long		serialVersionUID			= -2648569632965678122L;
	private static final STLogger	LOGGER						= STLogFactory
																		.getLog(ActiviteNormativeProgrammationImpl.class);

	private static final String		QUERY_LIGNE_PROGRAMMATION	= "SELECT l.ecm:uuid AS id FROM "
																		+ LigneProgrammationConstants.LIGNE_PROGRAMMATION_DOC_TYPE
																		+ " as l WHERE l.ecm:parentId"
																		+ " = ? "
																		+ "AND l."
																		+ LigneProgrammationConstants.LIGNE_PROGRAMMATION_PREFIX
																		+ LigneProgrammationConstants.TYPE_TABLEAU
																		+ " = ? ";

	public ActiviteNormativeProgrammationImpl(DocumentModel doc) {
		super(doc);
	}

	@Override
	public List<LigneProgrammation> getLigneProgrammation(CoreSession session) {
		return getLigneProgrammationProperty(session, LigneProgrammationConstants.TYPE_TABLEAU_VALUE_PROGRAMMATION);
	}

	@Override
	public void emptyLigneProgrammation(CoreSession session, String value) {
		try {
			DocumentRef[] listLigneProgDocIds = QueryUtils.doUFNXQLQueryForIds(session, QUERY_LIGNE_PROGRAMMATION,
					new Object[] { this.getId(), value });

			session.removeDocuments(listLigneProgDocIds);
			session.save();

		} catch (Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOCUMENT_TEC, e);
		}
	}

	protected List<LigneProgrammation> getLigneProgrammationProperty(CoreSession session, String value) {

		List<LigneProgrammation> ligneProgrammationList = new ArrayList<LigneProgrammation>();
		try {
			List<DocumentModel> listLigneProgDoc = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
					LigneProgrammationConstants.LIGNE_PROGRAMMATION_DOC_TYPE, QUERY_LIGNE_PROGRAMMATION, new Object[] {
							this.getId(), value });

			for (DocumentModel etatDoc : listLigneProgDoc) {
				ligneProgrammationList.add(etatDoc.getAdapter(LigneProgrammation.class));
			}
		} catch (Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOCUMENT_TEC, e);
		}

		return ligneProgrammationList;
	}

	protected List<String> extractIdsFromLignesProgrammation(List<LigneProgrammation> lstLignes) {
		List<String> lstIds = new ArrayList<String>();
		if (lstLignes != null) {
			for (LigneProgrammation ligne : lstLignes) {
				lstIds.add(ligne.getId());
			}
		}
		return lstIds;
	}

	@Override
	public String getLockUser() {
		return PropertyUtil.getStringProperty(document,
				ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA,
				ActiviteNormativeProgrammationConstants.LOCK_USER);
	}

	@Override
	public void setLockUser(String lockUser) {
		PropertyUtil.setProperty(document,
				ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA,
				ActiviteNormativeProgrammationConstants.LOCK_USER, lockUser);
	}

	@Override
	public Calendar getLockDate() {
		return PropertyUtil.getCalendarProperty(document,
				ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA,
				ActiviteNormativeProgrammationConstants.LOCK_DATE);
	}

	@Override
	public void setLockDate(Calendar lockDate) {
		PropertyUtil.setProperty(document,
				ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA,
				ActiviteNormativeProgrammationConstants.LOCK_DATE, lockDate);
	}

	@Override
	public List<LigneProgrammation> getTableauSuivi(CoreSession session) {
		return getLigneProgrammationProperty(session, LigneProgrammationConstants.TYPE_TABLEAU_VALUE_PROGRAMMATION);
	}

	@Override
	public void setTableauSuivi(List<LigneProgrammation> tableauSuivi) {

		List<String> lstLignesIds = extractIdsFromLignesProgrammation(tableauSuivi);
		setProperty(ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA,
				ActiviteNormativeProgrammationConstants.TABLEAU_SUIVI, lstLignesIds);
	}

	@Override
	public Calendar getTableauSuiviPublicationDate() {
		return PropertyUtil.getCalendarProperty(document,
				ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA,
				ActiviteNormativeProgrammationConstants.TABLEAU_SUIVI_PUBLICATION_DATE);
	}

	@Override
	public void setTableauSuiviPublicationDate(Calendar tableauSuiviPublicationDate) {
		PropertyUtil.setProperty(document,
				ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA,
				ActiviteNormativeProgrammationConstants.TABLEAU_SUIVI_PUBLICATION_DATE, tableauSuiviPublicationDate);
	}

	@Override
	public String getTableauSuiviPublicationUser() {
		return PropertyUtil.getStringProperty(document,
				ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA,
				ActiviteNormativeProgrammationConstants.TABLEAU_SUIVI_PUBLICATION_USER);
	}

	@Override
	public void setTableauSuiviPublicationUser(String tableauSuiviPublicationUser) {
		PropertyUtil.setProperty(document,
				ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA,
				ActiviteNormativeProgrammationConstants.TABLEAU_SUIVI_PUBLICATION_USER, tableauSuiviPublicationUser);
	}

	@Override
	public List<LigneProgrammationHabilitation> getLigneProgrammationHabilitation() {
		return getLigneProgrammationHabProperty(
				ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA,
				ActiviteNormativeProgrammationConstants.LIGNE_PROGRAMMATION_HAB);
	}

	@Override
	public void setLigneProgrammationHabilitation(List<LigneProgrammationHabilitation> ligneProgrammationHabilitations) {
		PropertyUtil.setListSerializableProperty(document,
				ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA,
				ActiviteNormativeProgrammationConstants.LIGNE_PROGRAMMATION_HAB, ligneProgrammationHabilitations);
	}

	protected List<LigneProgrammationHabilitation> getLigneProgrammationHabProperty(String schema, String value) {
		ArrayList<LigneProgrammationHabilitation> ligneProgrammationList = new ArrayList<LigneProgrammationHabilitation>();
		List<Map<String, Serializable>> ligneProgrammations = PropertyUtil.getMapStringSerializableListProperty(
				document, schema, value);
		if (ligneProgrammations != null) {
			for (Map<String, Serializable> ligneProgrammation : ligneProgrammations) {
				ligneProgrammationList.add(new LigneProgrammationHabilitationImpl(ligneProgrammation));
			}
		}
		return ligneProgrammationList;
	}

	@Override
	public List<LigneProgrammationHabilitation> getTableauSuiviHab() {
		return getLigneProgrammationHabProperty(
				ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA,
				ActiviteNormativeProgrammationConstants.TABLEAU_SUIVI_HAB);
	}

	@Override
	public void setTableauSuiviHab(List<LigneProgrammationHabilitation> tableauSuiviHab) {
		PropertyUtil.setListSerializableProperty(document,
				ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA,
				ActiviteNormativeProgrammationConstants.TABLEAU_SUIVI_HAB, tableauSuiviHab);
	}

	@Override
	public Calendar getTableauSuiviHabPublicationDate() {
		return PropertyUtil.getCalendarProperty(document,
				ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA,
				ActiviteNormativeProgrammationConstants.TABLEAU_SUIVI_HAB_PUBLICATION_DATE);
	}

	@Override
	public void setTableauSuiviHabPublicationDate(Calendar tableauSuiviHabPublicationDate) {
		PropertyUtil.setProperty(document,
				ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA,
				ActiviteNormativeProgrammationConstants.TABLEAU_SUIVI_HAB_PUBLICATION_DATE,
				tableauSuiviHabPublicationDate);
	}

	@Override
	public String getTableauSuiviHabPublicationUser() {
		return PropertyUtil.getStringProperty(document,
				ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA,
				ActiviteNormativeProgrammationConstants.TABLEAU_SUIVI_HAB_PUBLICATION_USER);
	}

	@Override
	public void setTableauSuiviHabPublicationUser(String tableauSuiviHabPublicationUser) {
		PropertyUtil.setProperty(document,
				ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA,
				ActiviteNormativeProgrammationConstants.TABLEAU_SUIVI_HAB_PUBLICATION_USER,
				tableauSuiviHabPublicationUser);
	}

	@Override
	public String getLockHabUser() {
		return PropertyUtil.getStringProperty(document,
				ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA,
				ActiviteNormativeProgrammationConstants.LOCK_USER_HAB);
	}

	@Override
	public void setLockHabUser(String lockHabUser) {
		PropertyUtil.setProperty(document,
				ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA,
				ActiviteNormativeProgrammationConstants.LOCK_USER_HAB, lockHabUser);
	}

	@Override
	public Calendar getLockHabDate() {
		return PropertyUtil.getCalendarProperty(document,
				ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA,
				ActiviteNormativeProgrammationConstants.LOCK_DATE_HAB);
	}

	@Override
	public void setLockHabDate(Calendar lockHabDate) {
		PropertyUtil.setProperty(document,
				ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA,
				ActiviteNormativeProgrammationConstants.LOCK_DATE_HAB, lockHabDate);
	}

}
