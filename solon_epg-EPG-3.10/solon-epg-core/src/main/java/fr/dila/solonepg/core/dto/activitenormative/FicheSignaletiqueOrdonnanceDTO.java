package fr.dila.solonepg.core.dto.activitenormative;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.Decret;
import fr.dila.solonepg.api.cases.LoiDeRatification;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;

/**
 * Representation de la fiche signaletique d'une ordonnance
 * 
 * @author asatre
 * 
 */
public class FicheSignaletiqueOrdonnanceDTO implements Serializable {

	private static final String	ARTICLE_74_1	= "Article 74-1";
	private static final String	ARTICLE_38_C	= "Article 38 C";

	/**
	 * public sinon pas d'acces dans le xhtml
	 */
	public class OrdonnanceDTO {

		private String	fondementConstitutionnel;
		private String	ministere;
		private String	titre;
		private String	numeroNor;
		private Date	date;
		private Date	datePublication;

		public String getFondementConstitutionnel() {
			return fondementConstitutionnel;
		}

		public void setFondementConstitutionnel(String fondementConstitutionnel) {
			this.fondementConstitutionnel = fondementConstitutionnel;
		}

		public String getMinistere() {
			return ministere;
		}

		public void setMinistere(String ministere) {
			this.ministere = ministere;
		}

		public String getTitre() {
			return titre;
		}

		public void setTitre(String titre) {
			this.titre = titre;
		}

		public String getNumeroNor() {
			return numeroNor;
		}

		public void setNumeroNor(String numeroNor) {
			this.numeroNor = numeroNor;
		}

		public Date getDate() {
			return DateUtil.copyDate(this.date);
		}

		public void setDate(Date date) {
			this.date = DateUtil.copyDate(date);
		}

		public Date getDatePublication() {
			return DateUtil.copyDate(this.datePublication);
		}

		public void setDatePublication(Date datePublication) {
			this.datePublication = DateUtil.copyDate(datePublication);
		}
	}

	/**
	 * public sinon pas d'acces dans le xhtml
	 */
	public class RatificationDTO {

		private String	termeDepot;
		private String	numeroNor;
		private String	titre;
		private String	numeroDepot;
		private String	assemblee;
		private Date	dateLimite;
		private Date	dateDepot;
		private Date	datePromulgation;
		private Date	datePublication;

		public String getTermeDepot() {
			return termeDepot;
		}

		public void setTermeDepot(String termeDepot) {
			this.termeDepot = termeDepot;
		}

		public String getNumeroNor() {
			return numeroNor;
		}

		public void setNumeroNor(String numeroNor) {
			this.numeroNor = numeroNor;
		}

		public String getTitre() {
			return titre;
		}

		public void setTitre(String titre) {
			this.titre = titre;
		}

		public String getNumeroDepot() {
			return numeroDepot;
		}

		public void setNumeroDepot(String numeroDepot) {
			this.numeroDepot = numeroDepot;
		}

		public String getAssemblee() {
			return assemblee;
		}

		public void setAssemblee(String assemblee) {
			this.assemblee = assemblee;
		}

		public Date getDateLimite() {
			return DateUtil.copyDate(this.dateLimite);
		}

		public void setDateLimite(Date dateLimite) {
			this.dateLimite = DateUtil.copyDate(dateLimite);
		}

		public Date getDateDepot() {
			return DateUtil.copyDate(this.dateDepot);
		}

		public void setDateDepot(Date dateDepot) {
			this.dateDepot = DateUtil.copyDate(dateDepot);
		}

		public Date getDatePromulgation() {
			return DateUtil.copyDate(this.datePromulgation);
		}

		public void setDatePromulgation(Date datePromulgation) {
			this.datePromulgation = DateUtil.copyDate(datePromulgation);
		}

		public Date getDatePublication() {
			return DateUtil.copyDate(this.datePublication);
		}

		public void setDatePublication(Date datePublication) {
			this.datePublication = DateUtil.copyDate(datePublication);
		}
	}

	/**
	 * public sinon pas d'acces dans le xhtml
	 */
	public class DecretApplicationDTO {

		private String	titre;
		private String	numeroNor;
		private Date	datePublication;

		public String getTitre() {
			return titre;
		}

		public void setTitre(String titre) {
			this.titre = titre;
		}

		public String getNumeroNor() {
			return numeroNor;
		}

		public void setNumeroNor(String numeroNor) {
			this.numeroNor = numeroNor;
		}

		public Date getDatePublication() {
			return DateUtil.copyDate(this.datePublication);
		}

		public void setDatePublication(Date datePublication) {
			this.datePublication = DateUtil.copyDate(datePublication);
		}

	}

	private static final long			serialVersionUID	= -472166326870307270L;

	private OrdonnanceDTO				ordonnanceDTO;

	private List<RatificationDTO>		ratificationDTO;
	private List<DecretApplicationDTO>	decretApplicationDTO;

	public FicheSignaletiqueOrdonnanceDTO(ActiviteNormative activiteNormative, CoreSession session)
			throws ClientException {

		final ActiviteNormativeService activiteNormativeService = SolonEpgServiceLocator.getActiviteNormativeService();

		TexteMaitre texteMaitre = activiteNormative.getDocument().getAdapter(TexteMaitre.class);

		// ordonnance
		ordonnanceDTO = new OrdonnanceDTO();
		if (texteMaitre.isDispositionHabilitation() == null) {
			ordonnanceDTO.setFondementConstitutionnel(ARTICLE_38_C);
		} else {
			ordonnanceDTO.setFondementConstitutionnel(texteMaitre.isDispositionHabilitation() ? ARTICLE_38_C
					: ARTICLE_74_1);
		}
		EntiteNode node = STServiceLocator.getSTMinisteresService().getEntiteNode(texteMaitre.getMinisterePilote());

		ordonnanceDTO.setMinistere(node.getNorMinistere());

		ordonnanceDTO.setTitre(texteMaitre.getTitreOfficiel());
		ordonnanceDTO.setDate(texteMaitre.getDateCreation() != null ? texteMaitre.getDateCreation().getTime() : null);
		ordonnanceDTO.setDatePublication(texteMaitre.getDatePublication() != null ? texteMaitre.getDatePublication()
				.getTime() : null);
		ordonnanceDTO.setNumeroNor(texteMaitre.getNumeroNor());

		// loi de ratification
		ratificationDTO = new ArrayList<RatificationDTO>();
		List<LoiDeRatification> listLoiRatification = activiteNormativeService.fetchLoiDeRatification(
				texteMaitre.getLoiRatificationIds(), session);

		for (LoiDeRatification loiDeRatification : listLoiRatification) {
			RatificationDTO rDTO = new RatificationDTO();
			rDTO.setAssemblee(loiDeRatification.getChambreDepot());
			rDTO.setDateDepot(loiDeRatification.getDateDepot() != null ? loiDeRatification.getDateDepot().getTime()
					: null);
			rDTO.setDateLimite(loiDeRatification.getDateLimiteDepot() != null ? loiDeRatification.getDateLimiteDepot()
					.getTime() : null);
			rDTO.setDatePromulgation(extractDatePromulgation(loiDeRatification.getTitreOfficiel()));
			rDTO.setDatePublication(loiDeRatification.getDatePublication() != null ? loiDeRatification
					.getDatePublication().getTime() : null);
			rDTO.setNumeroDepot(loiDeRatification.getNumeroDepot());
			rDTO.setNumeroNor(loiDeRatification.getNumeroNor());
			rDTO.setTermeDepot(loiDeRatification.getTermeDepot());
			rDTO.setTitre(loiDeRatification.getTitreOfficiel());
			ratificationDTO.add(rDTO);
		}

		// decret
		decretApplicationDTO = new ArrayList<DecretApplicationDTO>();
		List<Decret> listDecret = activiteNormativeService.fetchDecrets(texteMaitre.getDecretIds(), session);

		for (Decret decret : listDecret) {
			DecretApplicationDTO dDTO = new DecretApplicationDTO();
			dDTO.setDatePublication(decret.getDatePublication() != null ? decret.getDatePublication().getTime() : null);
			dDTO.setNumeroNor(decret.getNumeroNor());
			dDTO.setTitre(decret.getTitreOfficiel());
			decretApplicationDTO.add(dDTO);
		}

	}

	private Date extractDatePromulgation(String titre) {
		Calendar cal = SolonEpgServiceLocator.getActiviteNormativeService().extractDateFromTitre(titre);
		if (cal != null) {
			return cal.getTime();
		}
		return null;
	}

	public OrdonnanceDTO getOrdonnanceDTO() {
		return ordonnanceDTO;
	}

	public void setOrdonnanceDTO(OrdonnanceDTO ordonnanceDTO) {
		this.ordonnanceDTO = ordonnanceDTO;
	}

	public List<RatificationDTO> getRatificationDTO() {
		return ratificationDTO;
	}

	public void setRatificationDTO(List<RatificationDTO> ratificationDTO) {
		this.ratificationDTO = ratificationDTO;
	}

	public List<DecretApplicationDTO> getDecretApplicationDTO() {
		return decretApplicationDTO;
	}

	public void setDecretApplicationDTO(List<DecretApplicationDTO> decretApplicationDTO) {
		this.decretApplicationDTO = decretApplicationDTO;
	}

}
