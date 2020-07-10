package fr.dila.solonepg.core.dto.activitenormative;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.dto.TexteMaitreLoiDTO;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.util.PropertyUtil;
import fr.sword.xsd.solon.epp.NatureLoi;

public class TexteMaitreLoiDTOImpl implements Serializable, TexteMaitreLoiDTO {

	private static final long	serialVersionUID	= 78416213510924898L;

	private String				id;
	private String				numeroNor;
	private String				titreActe;
	private String				ministerePilote;
	private String				numeroInterne;
	private String				motCle;
	private String				lienJORFLegifrance;
	private Date				dateEntreeEnVigueur;
	private String				observation;
	private String				champLibre;
	private Boolean				applicationDirecte;
	private Date				datePublication;
	private Date				datePromulgation;
	private String				titreOfficiel;
	private String				legislaturePublication;
	private String				natureTexte;
	private String				procedureVote;
	private String				commissionAssNationale;
	private String				commissionSenat;
	private String				numero;
	private Boolean				dispositionHabilitation;
	private Date				dateReunionProgrammation;
	private Date				dateCirculationCompteRendu;
	private Date dateModification;
	private Date dateInjection;

	private Boolean				titreActeLocked;
	private Boolean				ministerePiloteLocked;
	private Boolean				numeroInterneLocked;
	private Boolean				dateEntreeEnVigueurLocked;
	private Boolean				datePublicationLocked;
	private Boolean				titreOfficielLocked;
	private Boolean				legislaturePublicationLocked;
	private Boolean				natureTexteLocked;
	private Boolean				procedureVoteLocked;
	private Boolean				commissionAssNationaleLocked;
	private Boolean				commissionSenatLocked;
	private Boolean				numeroLocked;

	public TexteMaitreLoiDTOImpl() {
	}

	public TexteMaitreLoiDTOImpl(ActiviteNormative activiteNormative) {
		if (activiteNormative == null) {
			return;
		}
		setId(activiteNormative.getDocument().getId());

		TexteMaitre texteMaitre = activiteNormative.getDocument().getAdapter(TexteMaitre.class);

		this.numeroNor = texteMaitre.getNumeroNor();
		this.titreActeLocked = texteMaitre.isTitreActeLocked();
		this.ministerePiloteLocked = texteMaitre.isMinistereRespLocked();
		this.numeroInterneLocked = texteMaitre.isNumeroInterneLocked();
		this.dateEntreeEnVigueurLocked = texteMaitre.isDateEntreeEnVigueurLocked();
		this.datePublicationLocked = texteMaitre.isDatePublicationLocked();
		this.titreOfficielLocked = texteMaitre.isTitreOfficielLocked();
		this.legislaturePublicationLocked = texteMaitre.isLegislaturePublicationLocked();
		this.natureTexteLocked = texteMaitre.isNatureTexteLocked();
		this.procedureVoteLocked = texteMaitre.isProcedureVoteLocked();
		this.commissionAssNationaleLocked = texteMaitre.isCommissionAssNationaleLocked();
		this.commissionSenatLocked = texteMaitre.isCommissionSenatLocked();
		this.numeroLocked = texteMaitre.isNumeroLocked();

		this.titreActe = texteMaitre.getTitreActe();
		this.ministerePilote = texteMaitre.getMinisterePilote();
		this.lienJORFLegifrance = getLienLegifranceFromJORF(numeroNor);
		this.numeroInterne = texteMaitre.getNumeroInterne();
		this.motCle = texteMaitre.getMotCle();
		this.dateEntreeEnVigueur = texteMaitre.getDateEntreeEnVigueur() == null ? null : texteMaitre
				.getDateEntreeEnVigueur().getTime();
		this.observation = texteMaitre.getObservation();
		this.champLibre = texteMaitre.getChampLibre();
		this.applicationDirecte = texteMaitre.isApplicationDirecte();
		this.datePublication = texteMaitre.getDatePublication() == null ? null : texteMaitre.getDatePublication()
				.getTime();
		this.datePromulgation = texteMaitre.getDatePromulgation() == null ? null : texteMaitre.getDatePromulgation()
				.getTime();
		this.titreOfficiel = texteMaitre.getTitreOfficiel();
		this.legislaturePublication = texteMaitre.getLegislaturePublication();
		this.natureTexte = texteMaitre.getNatureTexte();
		this.procedureVote = texteMaitre.getProcedureVote();
		this.commissionAssNationale = texteMaitre.getCommissionAssNationale();
		this.commissionSenat = texteMaitre.getCommissionSenat();
		this.numero = texteMaitre.getNumero();
		this.dispositionHabilitation = texteMaitre.isDispositionHabilitation() == null ? Boolean.TRUE : texteMaitre
				.isDispositionHabilitation();

		this.dateCirculationCompteRendu = texteMaitre.getDateCirculationCompteRendu() == null ? null : texteMaitre
				.getDateCirculationCompteRendu().getTime();
		this.dateReunionProgrammation = texteMaitre.getDateReunionProgrammation() == null ? null : texteMaitre
				.getDateReunionProgrammation().getTime();

	}

	/**
	 * refresh du DTO en fonction des locks
	 * 
	 * @param texteMaitre
	 * @param dossier
	 * @param ficheLoiDoc
	 * @throws ClientException
	 */
	public void refresh(Dossier dossier, DocumentModel ficheLoiDoc) throws ClientException {

		if (dossier != null) {
			if (!getTitreActeLocked()) {
				this.titreActe = dossier.getTitreActe();
			}

			if (!getMinisterePiloteLocked()) {
				this.ministerePilote = dossier.getMinistereResp();
			}

			RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);

			if (!getTitreOfficielLocked()) {
				this.titreOfficiel = retourDila.getTitreOfficiel();
			}

			if (!getDatePublicationLocked()) {
				Calendar cal = retourDila.getDateParutionJorf();
				if (cal != null) {
					this.datePublication = cal.getTime();
				} else {
					this.datePublication = null;
				}
			}

			if (ficheLoiDoc != null && "FicheLoi".equals(ficheLoiDoc.getType())) {
				if (!getProcedureVoteLocked()) {
					Boolean isProcAcceleree = (PropertyUtil.getCalendarProperty(ficheLoiDoc, "fiche_loi",
							"dateProcedureAcceleree") != null);
					if (isProcAcceleree != null) {
						if (isProcAcceleree) {
							this.procedureVote = VocabularyConstants.PROC_VOTE_VOCABULARY_ACCELEREE_ID;
						} else {
							this.procedureVote = VocabularyConstants.PROC_VOTE_VOCABULARY_NORMAL_ID;
						}
					}
				}
				if (!getNatureTexteLocked()) {
					String natureLoiStr = PropertyUtil.getStringProperty(ficheLoiDoc, "fiche_loi", "natureLoi");
					if (natureLoiStr != null) {
						NatureLoi natureLoi = NatureLoi.fromValue(natureLoiStr);
						if (NatureLoi.PROJET.equals(natureLoi)) {
							this.natureTexte = VocabularyConstants.NATURE_VOCABULARY_PROJ_LOI_ID;
						} else if (NatureLoi.PROPOSITION.equals(natureLoi)) {
							this.natureTexte = VocabularyConstants.NATURE_VOCABULARY_PROP_LOI_ID;
						}
					}
				}
			}
		}

		Calendar cal = SolonEpgServiceLocator.getActiviteNormativeService().extractDateFromTitre(titreOfficiel);
		if (cal != null) {
			this.datePromulgation = cal.getTime();
		} else {
			this.datePromulgation = null;
		}

	}

	public DocumentModel remapField(TexteMaitre texteMaitre, CoreSession session) throws ClientException {
		texteMaitre.setTitreActe(titreActe);
		texteMaitre.setMinisterePilote(ministerePilote);

		ActiviteNormativeService activiteNormativeService = SolonEpgServiceLocator.getActiviteNormativeService();
		activiteNormativeService.checkNumeroInterne(texteMaitre, numeroInterne, session);

		texteMaitre.setNumeroInterne(numeroInterne);
		texteMaitre.setNumero(numero);
		texteMaitre.setMotCle(motCle);

		if (dateEntreeEnVigueur != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateEntreeEnVigueur);
			texteMaitre.setDateEntreeEnVigueur(cal);
		} else {
			texteMaitre.setDateEntreeEnVigueur(null);
		}

		texteMaitre.setObservation(observation);
		texteMaitre.setChampLibre(champLibre);
		texteMaitre.setApplicationDirecte(applicationDirecte);

		if (datePublication == null) {
			texteMaitre.setDatePublication(null);
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(datePublication);
			texteMaitre.setDatePublication(cal);
		}

		if (datePromulgation == null) {
			texteMaitre.setDatePromulgation(null);
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(datePromulgation);
			texteMaitre.setDatePromulgation(cal);
		}

		if (dateReunionProgrammation == null) {
			texteMaitre.setDateReunionProgrammation(null);
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateReunionProgrammation);
			texteMaitre.setDateReunionProgrammation(cal);
		}

		if (dateCirculationCompteRendu == null) {
			texteMaitre.setDateCirculationCompteRendu(null);
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateCirculationCompteRendu);
			texteMaitre.setDateCirculationCompteRendu(cal);
		}

		texteMaitre.setTitreOfficiel(titreOfficiel);
		texteMaitre.setLegislaturePublication(legislaturePublication);
		texteMaitre.setNatureTexte(natureTexte);
		texteMaitre.setProcedureVote(procedureVote);
		texteMaitre.setCommissionAssNationale(commissionAssNationale);
		texteMaitre.setCommissionSenat(commissionSenat);

		Calendar cal = activiteNormativeService.extractDateFromTitre(texteMaitre.getTitreOfficiel());
		texteMaitre.setDatePromulgation(cal);

		texteMaitre.setTitreActeLocked(titreActeLocked);
		texteMaitre.setMinistereRespLocked(ministerePiloteLocked);
		texteMaitre.setNumeroInterneLocked(numeroInterneLocked);
		texteMaitre.setDateEntreeEnVigueurLocked(dateEntreeEnVigueurLocked);
		texteMaitre.setDatePublicationLocked(datePublicationLocked);
		texteMaitre.setTitreOfficielLocked(titreOfficielLocked);
		texteMaitre.setLegislaturePublicationLocked(legislaturePublicationLocked);
		texteMaitre.setNatureTexteLocked(natureTexteLocked);
		texteMaitre.setProcedureVoteLocked(procedureVoteLocked);
		texteMaitre.setCommissionAssNationaleLocked(commissionAssNationaleLocked);
		texteMaitre.setCommissionSenatLocked(commissionSenatLocked);
		texteMaitre.setNumeroLocked(numeroLocked);

		texteMaitre.setDispositionHabilitation(dispositionHabilitation);

		return texteMaitre.getDocument();

	}

	private String getLienLegifranceFromJORF(String jorfLegifrance) {
		return SolonEpgServiceLocator.getActiviteNormativeService().createLienJORFLegifrance(jorfLegifrance);
	}

	@Override
	public String getNumeroInterne() {
		return numeroInterne;
	}

	@Override
	public void setNumeroInterne(String numeroInterne) {
		this.numeroInterne = numeroInterne;
	}

	@Override
	public String getMotCle() {
		return motCle;
	}

	@Override
	public void setMotCle(String motCle) {
		this.motCle = motCle;
	}

	@Override
	public Date getDateEntreeEnVigueur() {
		return dateEntreeEnVigueur;
	}

	@Override
	public void setDateEntreeEnVigueur(Date dateEntreeEnVigueur) {
		this.dateEntreeEnVigueur = dateEntreeEnVigueur;
	}

	@Override
	public String getObservation() {
		return observation;
	}

	@Override
	public void setObservation(String observation) {
		this.observation = observation;
	}

	@Override
	public String getChampLibre() {
		return champLibre;
	}

	@Override
	public void setChampLibre(String champLibre) {
		this.champLibre = champLibre;
	}

	@Override
	public Boolean getApplicationDirecte() {
		return applicationDirecte;
	}

	@Override
	public void setApplicationDirecte(Boolean applicationDirecte) {
		this.applicationDirecte = applicationDirecte;
	}

	@Override
	public Date getDatePublication() {
		return datePublication;
	}

	@Override
	public void setDatePublication(Date datePublication) {
		this.datePublication = datePublication;
	}

	@Override
	public Date getDatePromulgation() {
		return datePromulgation;
	}

	@Override
	public void setDatePromulgation(Date datePromulgation) {
		this.datePromulgation = datePromulgation;
	}

	@Override
	public String getTitreOfficiel() {
		return titreOfficiel;
	}

	@Override
	public void setTitreOfficiel(String titreOfficiel) {
		this.titreOfficiel = titreOfficiel;
	}

	@Override
	public String getLegislaturePublication() {
		return legislaturePublication;
	}

	@Override
	public void setLegislaturePublication(String legislaturePublication) {
		this.legislaturePublication = legislaturePublication;
	}

	@Override
	public String getNatureTexte() {
		return natureTexte;
	}

	@Override
	public void setNatureTexte(String natureTexte) {
		this.natureTexte = natureTexte;
	}

	@Override
	public String getProcedureVote() {
		return procedureVote;
	}

	@Override
	public void setProcedureVote(String procedureVote) {
		this.procedureVote = procedureVote;
	}

	@Override
	public String getCommissionAssNationale() {
		return commissionAssNationale;
	}

	@Override
	public void setCommissionAssNationale(String commissionAssNationale) {
		this.commissionAssNationale = commissionAssNationale;
	}

	@Override
	public String getCommissionSenat() {
		return commissionSenat;
	}

	@Override
	public void setCommissionSenat(String commissionSenat) {
		this.commissionSenat = commissionSenat;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getNumeroNor() {
		return numeroNor;
	}

	@Override
	public void setNumeroNor(String numeroNor) {
		this.numeroNor = numeroNor;
	}

	@Override
	public String getTitreActe() {
		return titreActe;
	}

	@Override
	public void setTitreActe(String titreActe) {
		this.titreActe = titreActe;
	}

	@Override
	public String getMinisterePilote() {
		return ministerePilote;
	}

	@Override
	public void setMinisterePilote(String ministerePilote) {
		this.ministerePilote = ministerePilote;
	}

	@Override
	public void setLienJORFLegifrance(String lienJORFLegifrance) {
		this.lienJORFLegifrance = lienJORFLegifrance;
	}

	@Override
	public String getLienJORFLegifrance() {
		return lienJORFLegifrance;
	}

	@Override
	public Boolean getTitreActeLocked() {
		return titreActeLocked;
	}

	@Override
	public void setTitreActeLocked(Boolean titreActeLocked) {
		this.titreActeLocked = titreActeLocked;
	}

	@Override
	public Boolean getMinisterePiloteLocked() {
		return ministerePiloteLocked;
	}

	@Override
	public void setMinisterePiloteLocked(Boolean ministerePiloteLocked) {
		this.ministerePiloteLocked = ministerePiloteLocked;
	}

	@Override
	public Boolean getNumeroInterneLocked() {
		return numeroInterneLocked;
	}

	@Override
	public void setNumeroInterneLocked(Boolean numeroInterneLocked) {
		this.numeroInterneLocked = numeroInterneLocked;
	}

	@Override
	public Boolean getDateEntreeEnVigueurLocked() {
		return dateEntreeEnVigueurLocked;
	}

	@Override
	public void setDateEntreeEnVigueurLocked(Boolean dateEntreeEnVigueurLocked) {
		this.dateEntreeEnVigueurLocked = dateEntreeEnVigueurLocked;
	}

	@Override
	public Boolean getDatePublicationLocked() {
		return datePublicationLocked;
	}

	@Override
	public void setDatePublicationLocked(Boolean datePublicationLocked) {
		this.datePublicationLocked = datePublicationLocked;
	}

	@Override
	public Boolean getTitreOfficielLocked() {
		return titreOfficielLocked;
	}

	@Override
	public void setTitreOfficielLocked(Boolean titreOfficielLocked) {
		this.titreOfficielLocked = titreOfficielLocked;
	}

	@Override
	public Boolean getLegislaturePublicationLocked() {
		return legislaturePublicationLocked;
	}

	@Override
	public void setLegislaturePublicationLocked(Boolean legislaturePublicationLocked) {
		this.legislaturePublicationLocked = legislaturePublicationLocked;
	}

	@Override
	public Boolean getNatureTexteLocked() {
		return natureTexteLocked;
	}

	@Override
	public void setNatureTexteLocked(Boolean natureTexteLocked) {
		this.natureTexteLocked = natureTexteLocked;
	}

	@Override
	public Boolean getProcedureVoteLocked() {
		return procedureVoteLocked;
	}

	@Override
	public void setProcedureVoteLocked(Boolean procedureVoteLocked) {
		this.procedureVoteLocked = procedureVoteLocked;
	}

	@Override
	public Boolean getCommissionAssNationaleLocked() {
		return commissionAssNationaleLocked;
	}

	@Override
	public void setCommissionAssNationaleLocked(Boolean commissionAssNationaleLocked) {
		this.commissionAssNationaleLocked = commissionAssNationaleLocked;
	}

	@Override
	public Boolean getCommissionSenatLocked() {
		return commissionSenatLocked;
	}

	@Override
	public void setCommissionSenatLocked(Boolean commissionSenatLocked) {
		this.commissionSenatLocked = commissionSenatLocked;
	}

	@Override
	public void setNumeroLocked(Boolean numeroLocked) {
		this.numeroLocked = numeroLocked;
	}

	@Override
	public Boolean getNumeroLocked() {
		return numeroLocked;
	}

	@Override
	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Override
	public String getNumero() {
		return numero;
	}

	@Override
	public void setDispositionHabilitation(Boolean dispositionHabilitation) {
		this.dispositionHabilitation = dispositionHabilitation;
	}

	@Override
	public Boolean getDispositionHabilitation() {
		return dispositionHabilitation;
	}

	@Override
	public Date getDateReunionProgrammation() {
		return dateReunionProgrammation;
	}

	@Override
	public void setDateReunionProgrammation(Date dateReunionProgrammation) {
		this.dateReunionProgrammation = dateReunionProgrammation;
	}

	@Override
	public Date getDateCirculationCompteRendu() {
		return dateCirculationCompteRendu;
	}

	@Override
	public void setDateCirculationCompteRendu(Date dateCirculationCompteRendu) {
		this.dateCirculationCompteRendu = dateCirculationCompteRendu;
	}

	@Override
	public Date getDateModification() {
		return dateModification;
	}

	@Override
	public void setDateModification(Date dateModification) {
		this.dateModification = dateModification;
	}
	
	@Override
	public Date getDateInjection() {
		return dateInjection;
	}

	@Override
	public void setDateInjection(Date dateInjection) {
		this.dateInjection = dateInjection;
	}

}
