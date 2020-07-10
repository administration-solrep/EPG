/**
 * 
 */
package fr.dila.solonepg.core.administration;

import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.constant.SolonEpgTableReferenceConstants;
import fr.dila.st.core.domain.STDomainObjectImpl;
import fr.dila.st.core.util.CollectionUtil;
import fr.dila.st.core.util.PropertyUtil;

/**
 * Table de reference Implementation.
 * 
 * @author asatre
 * 
 */
public class TableReferenceImpl extends STDomainObjectImpl implements TableReference {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8966265291670853971L;

	public TableReferenceImpl(DocumentModel document) {
		super(document);
	}

	@Override
	public List<String> getCabinetPM() {
		return getListStringProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_CABINET_PM);
	}

	@Override
	public void setCabinetPM(List<String> cabinetPM) {
		setProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_CABINET_PM, cabinetPM);
	}

	@Override
	public List<String> getChargesMission() {
		return getListStringProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_CHARGES_MISSION);
	}

	@Override
	public void setChargesMission(List<String> chargesMission) {
		setProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_CHARGES_MISSION, chargesMission);
	}

	@Override
	public List<String> getSignataires() {
		return getListStringProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_SIGNATAIRES);
	}

	@Override
	public void setSignataires(List<String> signataires) {
		setProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_SIGNATAIRES, signataires);
	}

	@Override
	public String getMinisterePM() {
		return getStringProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_MINISTERE_PM);
	}

	@Override
	public void setMinisterePM(String ministerePM) {
		setProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_MINISTERE_PM, ministerePM);
	}

	@Override
	public String getMinistereCE() {
		return getStringProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_MINISTERE_CE);
	}

	@Override
	public void setMinistereCE(String ministereCE) {
		setProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_MINISTERE_CE, ministereCE);
	}

	@Override
	public List<String> getVecteurPublicationMultiples() {
		return PropertyUtil.getStringListProperty(document, SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_VECTEURS_PUBLICATIONS_PROPERTY);
	}

	@Override
	public void setVecteurPublicationMultiples(List<String> vecteurPublicationMultiples) {
		setProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_VECTEURS_PUBLICATIONS_PROPERTY,
				vecteurPublicationMultiples);
	}

	@Override
	public List<String> getDirectionsPM() {
		return PropertyUtil.getStringListProperty(document, SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_DIRECTION_PM_PROPERTY);
	}

	@Override
	public void setRetourDAN(List<String> retoursDAN) {
		setProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_RETOUR_DAN_PROPERTY, retoursDAN);
	}

	@Override
	public List<String> getRetourDAN() {
		return PropertyUtil.getStringListProperty(document, SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_RETOUR_DAN_PROPERTY);
	}

	@Override
	public void setDirectionsPM(List<String> directionsPM) {
		setProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_DIRECTION_PM_PROPERTY, directionsPM);
	}
	
	@Override
	public List<String> getSignatureCPM() {
		return PropertyUtil.getStringListProperty(document, SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_SIGNATURE_CPM_PROPERTY);
	}

	@Override
	public void setSignatureCPM(List<String> signatureCPM) {
		setProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_SIGNATURE_CPM_PROPERTY, signatureCPM);

	}

	@Override
	public List<String> getSignatureSGG() {
		return PropertyUtil.getStringListProperty(document, SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_SIGNATURE_SGG_PROPERTY);

	}

	@Override
	public void setSignatureSGG(List<String> signatureSGG) {
		setProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_SIGNATURE_SGG_PROPERTY, signatureSGG);
	}

	@Override
	public List<String> getTypesActe() {

		return CollectionUtil.asSortedList(PropertyUtil.getStringListProperty(document,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_TYPE_ACTE_PROPERTY));
	}

	@Override
	public void setTypesActe(List<String> typesActe) {
		setProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_TYPE_ACTE_PROPERTY, typesActe);

	}

	@Override
	public String getPostePublicationId() {
		return getStringProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_PST_PUBLI_ID);
	}

	@Override
	public void setPostePublicationId(String postePublicationId) {
		setProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_PST_PUBLI_ID, postePublicationId);
	}

	@Override
	public String getPosteDanId() {
		return getStringProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_PST_DAN_ID);
	}

	@Override
	public void setPosteDanId(String posteDanId) {
		setProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_PST_DAN_ID, posteDanId);
	}
	

	@Override
	public String getPosteDanIdAvisRectificatifCE() {
		return getStringProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_PST_DAN_ID_AVIS_RECTIFICATIF_CE);
	}

	@Override
	public void setPosteDanIdAvisRectificatifCE(String posteDanIdAvisRectificatifCE) {
		setProperty(SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_PST_DAN_ID_AVIS_RECTIFICATIF_CE, posteDanIdAvisRectificatifCE);
	}
}
