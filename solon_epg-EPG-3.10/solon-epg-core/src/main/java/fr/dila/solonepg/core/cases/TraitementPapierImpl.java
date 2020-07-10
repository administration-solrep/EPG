package fr.dila.solonepg.core.cases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.cases.typescomplexe.DestinataireCommunication;
import fr.dila.solonepg.api.cases.typescomplexe.DonneesSignataire;
import fr.dila.solonepg.api.cases.typescomplexe.InfoEpreuve;
import fr.dila.solonepg.api.cases.typescomplexe.InfoHistoriqueAmpliation;
import fr.dila.solonepg.api.cases.typescomplexe.InfoNumeroListe;
import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.solonepg.core.cases.typescomplexe.DestinataireCommunicationImpl;
import fr.dila.solonepg.core.cases.typescomplexe.DonneesSignataireImpl;
import fr.dila.solonepg.core.cases.typescomplexe.InfoEpreuveImpl;
import fr.dila.solonepg.core.cases.typescomplexe.InfoHistoriqueAmpliationImpl;
import fr.dila.solonepg.core.cases.typescomplexe.InfoNumeroListeImpl;
import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.st.core.domain.STDomainObjectImpl;

public class TraitementPapierImpl extends STDomainObjectImpl implements TraitementPapier {

	/**
     * 
     */
	private static final long	serialVersionUID	= -2821452736228643835L;

	public TraitementPapierImpl(DocumentModel doc) {
		super(doc);
	}

	// /////////////////
	// getter/setter Traitement papier
	// ////////////////
	// /////////////////
	// onglet Référence
	// ////////////////
	@Override
	public Calendar getDateArrivePapier() {
		return PropertyUtil.getCalendarProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ARRIVE_PAPIER_PROPERTY);
	}

	@Override
	public void setDateArrivePapier(Calendar dateArrivePapier) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ARRIVE_PAPIER_PROPERTY, dateArrivePapier);
	}

	@Override
	public String getCommentaireReference() {
		return PropertyUtil.getStringProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_COMMENTAIRE_REFERENCE_PROPERTY);
	}

	@Override
	public void setCommentaireReference(String commentaireReference) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_COMMENTAIRE_REFERENCE_PROPERTY, commentaireReference);
	}

	@Override
	public Boolean getTexteAPublier() {
		return PropertyUtil.getBooleanProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_TEXTE_A_PUBLIER_PROPERTY);
	}

	@Override
	public void setTexteAPublier(Boolean texteAPublier) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_TEXTE_A_PUBLIER_PROPERTY, texteAPublier);
	}

	@Override
	public Boolean getTexteSoumisAValidation() {
		return PropertyUtil.getBooleanProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_TEXTE_SOUMIS_A_VALIDATION);
	}

	@Override
	public void setTexteSoumisAValidation(Boolean texteSoumisAValidation) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_TEXTE_SOUMIS_A_VALIDATION, texteSoumisAValidation);
	}

	@Override
	public String getSignataire() {
		return PropertyUtil.getStringProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_SIGNATAIRE_PROPERTY);
	}

	@Override
	public void setSignataire(String signataire) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_SIGNATAIRE_PROPERTY, signataire);
	}

	// /////////////////
	// onglet Communication
	// ////////////////
	@Override
	public String getPriorite() {
		return PropertyUtil.getStringProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_PRIORITE_PROPERTY);
	}

	@Override
	public void setPriorite(String priorite) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_PRIORITE_PROPERTY, priorite);
	}

	@Override
	public String getPersonnesNommees() {
		return PropertyUtil.getStringProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_PERSONNES_NOMMEE_PROPERTY);
	}

	public void setPersonnesNommees(String personnesNommees) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_PERSONNES_NOMMEE_PROPERTY, personnesNommees);

	}

	@Override
	public List<DestinataireCommunication> getCabinetPmCommunication() {
		return getDestinataireCommunicationProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_CABINET_PM_COMMUNICATION_PROPERTY);
	}

	@Override
	public void setCabinetPmCommunication(List<DestinataireCommunication> cabinetPmCommunication) {
		PropertyUtil.setListSerializableProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_CABINET_PM_COMMUNICATION_PROPERTY, cabinetPmCommunication);
	}

	@Override
	public List<DestinataireCommunication> getChargeMissionCommunication() {
		return getDestinataireCommunicationProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_CHARGE_MISSION_COMMUNICATION_PROPERTY);
	}

	@Override
	public void setChargeMissionCommunication(List<DestinataireCommunication> chargeMissionCommunication) {
		PropertyUtil.setListSerializableProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_CHARGE_MISSION_COMMUNICATION_PROPERTY,
				chargeMissionCommunication);
	}

	@Override
	public List<DestinataireCommunication> getAutresDestinatairesCommunication() {
		return getDestinataireCommunicationProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_AUTRES_DESTINATAIRES_COMMUNICATION_PROPERTY);
	}

	@Override
	public void setCabinetSgCommunication(List<DestinataireCommunication> cabinetSgCommunication) {
		PropertyUtil.setListSerializableProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_CABINET_SG_COMMUNICATION_PROPERTY, cabinetSgCommunication);
	}

	@Override
	public List<DestinataireCommunication> getCabinetSgCommunication() {
		return getDestinataireCommunicationProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_CABINET_SG_COMMUNICATION_PROPERTY);
	}

	@Override
	public void setAutresDestinatairesCommunication(List<DestinataireCommunication> autresDestinatairesCommunication) {
		PropertyUtil.setListSerializableProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_AUTRES_DESTINATAIRES_COMMUNICATION_PROPERTY,
				autresDestinatairesCommunication);
	}

	@Override
	public String getNomsSignatairesCommunication() {
		return PropertyUtil.getStringProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_NOMS_SIGNATAIRES_COMMUNICATION_PROPERTY);
	}

	@Override
	public void setNomsSignatairesCommunication(String nomsSignatairesCommunication) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_NOMS_SIGNATAIRES_COMMUNICATION_PROPERTY,
				nomsSignatairesCommunication);
	}

	// /////////////////
	// onglet Signature
	// ////////////////
	@Override
	public DonneesSignataire getSignaturePm() {
		return getDonneesSignataireProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_SIGNATURE_PM_PROPERTY);
	}

	@Override
	public void setSignaturePm(DonneesSignataire signaturePm) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_SIGNATURE_PM_PROPERTY, signaturePm.getSerializableMap());
	}

	@Override
	public DonneesSignataire getSignatureSgg() {
		return getDonneesSignataireProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_SIGNATURE_SGG_PROPERTY);
	}

	@Override
	public void setSignatureSgg(DonneesSignataire signatureSgg) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_SIGNATURE_SGG_PROPERTY, signatureSgg.getSerializableMap());
	}

	@Override
	public DonneesSignataire getSignaturePr() {
		return getDonneesSignataireProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_SIGNATURE_PR_PROPERTY);
	}

	@Override
	public void setSignaturePr(DonneesSignataire signaturePr) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_SIGNATURE_PR_PROPERTY, signaturePr.getSerializableMap());
	}

	@Override
	public Boolean getCheminCroix() {
		return PropertyUtil.getBooleanProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_CHEMIN_CROIX_PROPERTY);
	}

	@Override
	public void setCheminCroix(Boolean cheminCroix) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_CHEMIN_CROIX_PROPERTY, cheminCroix);
	}

	@Override
	public String getSignatureDestinataireSGG() {
		return PropertyUtil.getStringProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_SIGNATURE_DESTINATAIRE_SGG_PROPERTY);
	}

	@Override
	public void setSignatureDestinataireSGG(String signatureDestinataireSGG) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_SIGNATURE_DESTINATAIRE_SGG_PROPERTY,
				signatureDestinataireSGG);
	}

	@Override
	public String getSignatureDestinataireCPM() {
		return PropertyUtil.getStringProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_SIGNATURE_DESTINATAIRE_CPM_PROPERTY);
	}

	@Override
	public void setSignatureDestinataireCPM(String signatureDestinataireCPM) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_SIGNATURE_DESTINATAIRE_CPM_PROPERTY,
				signatureDestinataireCPM);
	}

	@Override
	public List<InfoNumeroListe> getNumerosListeSignature() {
		return getInfoNumeroListeProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_NUMEROS_LISTE_SIGNATURE_FIELD_PROPERTY);
	}

	@Override
	public void setNumerosListeSignature(List<InfoNumeroListe> numerosListeSignature) {
		PropertyUtil.setListSerializableProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_NUMEROS_LISTE_SIGNATURE_FIELD_PROPERTY,
				numerosListeSignature);
	}

	// /////////////////
	// onglet Retour
	// ////////////////
	@Override
	public String getRetourA() {
		return PropertyUtil.getStringProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_RETOUR_A_PROPERTY);
	}

	@Override
	public void setRetourA(String retourA) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_RETOUR_A_PROPERTY, retourA);
	}

	@Override
	public Calendar getDateRetour() {
		return PropertyUtil.getCalendarProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RETOUR_PROPERTY);
	}

	@Override
	public void setDateRetour(Calendar dateRetour) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RETOUR_PROPERTY, dateRetour);
	}

	@Override
	public String getMotifRetour() {
		return PropertyUtil.getStringProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_MOTIF_RETOUR_PROPERTY);
	}

	@Override
	public void setMotifRetour(String motifRetour) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_MOTIF_RETOUR_PROPERTY, motifRetour);
	}

	@Override
	public String getNomsSignatairesRetour() {
		return PropertyUtil.getStringProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_NOMS_SIGNATAIRES_RETOUR_PROPERTY);
	}

	@Override
	public void setNomsSignatairesRetour(String nomsSignatairesRetour) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_NOMS_SIGNATAIRES_RETOUR_PROPERTY, nomsSignatairesRetour);
	}

	// /////////////////
	// onglet Epreuves
	// ////////////////
	@Override
	public InfoEpreuve getEpreuve() {
		return getInfoEpreuveProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_PROPERTY);
	}

	@Override
	public void setEpreuve(InfoEpreuve epreuve) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_PROPERTY, epreuve.getSerializableMap());
	}

	@Override
	public InfoEpreuve getNouvelleDemandeEpreuves() {
		return getInfoEpreuveProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_NOUVELLE_DEMANDE_EPREUVES_PROPERTY);
	}

	@Override
	public void setNouvelleDemandeEpreuves(InfoEpreuve nouvelleDemandeEpreuves) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_NOUVELLE_DEMANDE_EPREUVES_PROPERTY,
				nouvelleDemandeEpreuves.getSerializableMap());
	}

	@Override
	public Calendar getRetourDuBonaTitrerLe() {
		return PropertyUtil.getCalendarProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_RETOUR_DU_BON_A_TITRE_LE_PROPERTY);
	}

	@Override
	public void setRetourDuBonaTitrerLe(Calendar retourDuBonaTitrerLe) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_RETOUR_DU_BON_A_TITRE_LE_PROPERTY, retourDuBonaTitrerLe);
	}

	// /////////////////
	// onglet Publication
	// ////////////////
	@Override
	public Calendar getPublicationDateEnvoiJo() {
		return PropertyUtil.getCalendarProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_DATE_ENVOI_JO_PROPERTY);
	}

	@Override
	public void setPublicationDateEnvoiJo(Calendar publicationDateEnvoiJo) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_DATE_ENVOI_JO_PROPERTY, publicationDateEnvoiJo);
	}

	@Override
	public String getPublicationNomPublication() {
		return PropertyUtil.getStringProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_NOM_PUBLICATION_PROPERTY);
	}

	@Override
	public void setPublicationNomPublication(String publicationNomPublication) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_NOM_PUBLICATION_PROPERTY,
				publicationNomPublication);
	}

	@Override
	public String getPublicationModePublication() {
		return PropertyUtil.getStringProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_MODE_PUBLICATION_PROPERTY);
	}

	@Override
	public void setPublicationModePublication(String publicationModePublication) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_MODE_PUBLICATION_PROPERTY,
				publicationModePublication);
	}

	@Override
	public Boolean getPublicationEpreuveEnRetour() {
		return PropertyUtil.getBooleanProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_EPREUVE_EN_RETOUR_PROPERTY);
	}

	@Override
	public void setPublicationEpreuveEnRetour(Boolean publicationEpreuveEnRetour) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_EPREUVE_EN_RETOUR_PROPERTY,
				publicationEpreuveEnRetour);
	}

	@Override
	public String getPublicationDelai() {
		return PropertyUtil.getStringProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_DELAI_PROPERTY);
	}

	@Override
	public void setPublicationDelai(String publicationDelai) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_DELAI_PROPERTY, publicationDelai);
	}

	@Override
	public Calendar getPublicationDateDemande() {
		return PropertyUtil.getCalendarProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_DATE_DEMANDE_PROPERTY);
	}

	@Override
	public void setPublicationDateDemande(Calendar publicationDateDemande) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_DATE_DEMANDE_PROPERTY, publicationDateDemande);
	}

	@Override
	public String getPublicationNumeroListe() {
		return PropertyUtil.getStringProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_NUMERO_LISTE_PROPERTY);
	}

	@Override
	public void setPublicationNumeroListe(String publicationNumeroListe) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_NUMERO_LISTE_PROPERTY, publicationNumeroListe);
	}

	@Override
	public Calendar getPublicationDate() {
		return PropertyUtil.getCalendarProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_DATE_PROPERTY);
	}

	@Override
	public void setPublicationDate(Calendar publicationDate) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_DATE_PROPERTY, publicationDate);
	}

	// /////////////////
	// onglet Ampliation
	// ////////////////
	@Override
	public List<String> getAmpliationDestinataireMails() {
		return getListStringProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_AMPLIATION_DESTINATAIRE_MAILS_PROPERTY);
	}

	@Override
	public void setAmpliationDestinataireMails(List<String> ampliationDestinataireMails) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_AMPLIATION_DESTINATAIRE_MAILS_PROPERTY,
				ampliationDestinataireMails);
	}

	@Override
	public List<InfoHistoriqueAmpliation> getAmpliationHistorique() {
		return getInfoHistoriqueAmpliationProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_AMPLIATION_HISTORIQUE_PROPERTY);
	}

	@Override
	public void setAmpliationHistorique(List<InfoHistoriqueAmpliation> ampliationHistorique) {
		PropertyUtil.setListSerializableProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_AMPLIATION_HISTORIQUE_PROPERTY, ampliationHistorique);
	}

	@SuppressWarnings("unchecked")
	protected List<DestinataireCommunication> getDestinataireCommunicationProperty(String schema, String value) {
		try {
			ArrayList<DestinataireCommunication> destinataireCommunicationList = new ArrayList<DestinataireCommunication>();
			List<Map<String, Serializable>> destinataireCommunications = (List<Map<String, Serializable>>) document
					.getProperty(schema, value);
			if (destinataireCommunications != null) {
				for (Map<String, Serializable> destinataireCommunication : destinataireCommunications) {
					destinataireCommunicationList.add(new DestinataireCommunicationImpl(destinataireCommunication));
				}
			}
			return destinataireCommunicationList;
		} catch (ClientException e) {
			throw new CaseManagementRuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	protected DonneesSignataire getDonneesSignataireProperty(String schema, String value) {
		try {
			return new DonneesSignataireImpl((Map<String, Serializable>) document.getProperty(schema, value));
		} catch (ClientException e) {
			throw new CaseManagementRuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	protected List<InfoHistoriqueAmpliation> getInfoHistoriqueAmpliationProperty(String schema, String value) {
		try {
			ArrayList<InfoHistoriqueAmpliation> infoHistoriqueAmpliationList = new ArrayList<InfoHistoriqueAmpliation>();
			List<Map<String, Serializable>> infoHistoriqueAmpliations = (List<Map<String, Serializable>>) document
					.getProperty(schema, value);
			if (infoHistoriqueAmpliations != null) {
				for (Map<String, Serializable> infoHistoriqueAmpliation : infoHistoriqueAmpliations) {
					infoHistoriqueAmpliationList.add(new InfoHistoriqueAmpliationImpl(infoHistoriqueAmpliation));
				}
			}
			return infoHistoriqueAmpliationList;
		} catch (ClientException e) {
			throw new CaseManagementRuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	protected List<InfoNumeroListe> getInfoNumeroListeProperty(String schema, String value) {
		try {
			ArrayList<InfoNumeroListe> infoNumeroListeList = new ArrayList<InfoNumeroListe>();
			List<Map<String, Serializable>> infoNumeroListes = (List<Map<String, Serializable>>) document.getProperty(
					schema, value);
			if (infoNumeroListes != null) {
				for (Map<String, Serializable> infoNumeroListe : infoNumeroListes) {
					infoNumeroListeList.add(new InfoNumeroListeImpl(infoNumeroListe));
				}
			}
			return infoNumeroListeList;
		} catch (ClientException e) {
			throw new CaseManagementRuntimeException(e);
		}
	}

	@Override
	public Boolean getPapierArchive() {
		return PropertyUtil.getBooleanProperty(document, TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_AMPLIATION_PAPIER_ARCHIVE_PROPERTY);
	}

	@Override
	public void setPapierArchive(Boolean papierArchive) {
		setProperty(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA,
				TraitementPapierConstants.TRAITEMENT_PAPIER_AMPLIATION_PAPIER_ARCHIVE_PROPERTY, papierArchive);
	}

	@SuppressWarnings("unchecked")
	protected InfoEpreuve getInfoEpreuveProperty(String schema, String value) {
		try {
			return new InfoEpreuveImpl((Map<String, Serializable>) document.getProperty(schema, value));
		} catch (ClientException e) {
			throw new CaseManagementRuntimeException(e);
		}
	}

}
