package fr.dila.solonepg.core.administration;

import java.util.Date;
import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.constant.SolonEpgParametresANConstants;
import fr.dila.st.core.domain.STDomainObjectImpl;

public class ParametrageANImpl extends STDomainObjectImpl implements ParametrageAN {

	/**
	 * Serial version UID
	 */
	private static final long	serialVersionUID	= -928319059833069636L;

	public ParametrageANImpl(DocumentModel document) {
		super(document);
	}

	@Override
	public Date getLegislatureEnCoursDateDebut() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE).getTime();
	}

	@Override
	public void setLegislatureEnCoursDateDebut(Date legislatureEnCoursDateDebut) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislatureEnCoursDateDebut);
	}

	@Override
	public Date getLECBSDatePromulBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_BS_PROMUL_LOIS_DEBUT).getTime();
	}

	@Override
	public void setLECBSDatePromulBorneInf(
			Date legislatureEnCoursBilanSemestrielDatePromulgationBorneInferieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislatureEnCoursBilanSemestrielDatePromulgationBorneInferieure);
	}

	@Override
	public Date getLECBSDatePromulBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_BS_PROMUL_LOIS_FIN).getTime();
	}

	@Override
	public void setLECBSDatePromulBorneSup(
			Date legislatureEnCoursBilanSemestrielDatePromulgationBorneSuperieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislatureEnCoursBilanSemestrielDatePromulgationBorneSuperieure);
	}

	@Override
	public Date getLECBSDatePubliBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_BS_PUBLI_DECRETS_DEBUT).getTime();
	}

	@Override
	public void setLECBSDatePubliBorneInf(
			Date legislatureEnCoursBilanSemestrielDatePublicationBorneInferieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislatureEnCoursBilanSemestrielDatePublicationBorneInferieure);
	}

	@Override
	public Date getLECBSDatePubliBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_BS_PUBLI_DECRETS_FIN).getTime();
	}

	@Override
	public void setLECBSDatePubliBorneSup(
			Date legislatureEnCoursBilanSemestrielDatePublicationBorneSuperieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislatureEnCoursBilanSemestrielDatePublicationBorneSuperieure);
	}

	@Override
	public Date getLECTauxSPDatePromulBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_TAUXPARL_PROMUL_LOIS_DEBUT).getTime();
	}

	@Override
	public void setLECTauxSPDatePromulBorneInf(
			Date legislatureEnCoursTauxParlementaireDatePromulgationBorneInferieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislatureEnCoursTauxParlementaireDatePromulgationBorneInferieure);
	}

	@Override
	public Date getLECTauxSPDatePromulBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_TAUXPARL_PROMUL_LOIS_FIN).getTime();
	}

	@Override
	public void setLECTauxSPDatePromulBorneSup(
			Date legislatureEnCoursTauxParlementaireDatePromulgationBorneSuperieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislatureEnCoursTauxParlementaireDatePromulgationBorneSuperieure);
	}

	@Override
	public Date getLECTauxSPDatePubliBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_TAUXPARL_PUBLI_DECRETS_DEBUT).getTime();
	}

	@Override
	public void setLECTauxSPDatePubliBorneInf(
			Date legislatureEnCoursTauxParlementaireDatePublicationBorneInferieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislatureEnCoursTauxParlementaireDatePublicationBorneInferieure);
	}

	@Override
	public Date getLECTauxSPDatePubliBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_TAUXPARL_PUBLI_DECRETS_FIN).getTime();
	}

	@Override
	public void setLECTauxSPDatePubliBorneSup(
			Date legislatureEnCoursTauxParlementaireDatePublicationBorneSuperieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislatureEnCoursTauxParlementaireDatePublicationBorneSuperieure);
	}

	@Override
	public Date getLECTauxDebutLegisDatePromulBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_TAUXLEGIS_PROMUL_LOIS_DEBUT).getTime();
	}

	@Override
	public void setLECTauxDebutLegisDatePromulBorneInf(
			Date legislatureEnCoursTauxMagistratureDatePromulgationBorneInferieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislatureEnCoursTauxMagistratureDatePromulgationBorneInferieure);
	}

	@Override
	public Date getLECTauxDebutLegisDatePromulBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_TAUXLEGIS_PROMUL_LOIS_FIN).getTime();
	}

	@Override
	public void setLECTauxDebutLegisDatePromulBorneSup(
			Date legislatureEnCoursTauxMagistratureDatePromulgationBorneSuperieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislatureEnCoursTauxMagistratureDatePromulgationBorneSuperieure);
	}

	@Override
	public Date getLECTauxDebutLegisDatePubliBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_TAUXLEGIS_PUBLI_DECRETS_DEBUT).getTime();
	}

	@Override
	public void setLECTauxDebutLegisDatePubliBorneInf(
			Date legislatureEnCoursTauxMagistratureDatePublicationBorneInferieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislatureEnCoursTauxMagistratureDatePublicationBorneInferieure);
	}

	@Override
	public Date getLECTauxDebutLegisDatePubliBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_TAUXLEGIS_PUBLI_DECRETS_FIN).getTime();
	}

	@Override
	public void setLECTauxDebutLegisDatePubliBorneSup(
			Date legislatureEnCoursTauxMagistratureDatePublicationBorneSuperieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislatureEnCoursTauxMagistratureDatePublicationBorneSuperieure);
	}

	@Override
	public Date getLegislaturePrecedenteDateDebut() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_DEBUT_LEGISLATURE).getTime();
	}

	@Override
	public void setLegislaturePrecedenteDateDebut(Date legislaturePrecedenteDateDebut) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislaturePrecedenteDateDebut);
	}

	@Override
	public Date getLPBSDatePromulBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_BS_PROMUL_LOIS_DEBUT).getTime();
	}

	@Override
	public void setLPBSDatePromulBorneInf(
			Date legislaturePrecedenteBilanSemestrielDatePromulgationBorneInferieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislaturePrecedenteBilanSemestrielDatePromulgationBorneInferieure);
	}

	@Override
	public Date getLPBSDatePromulBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_BS_PROMUL_LOIS_FIN).getTime();
	}

	@Override
	public void setLPBSDatePromulBorneSup(
			Date legislaturePrecedenteBilanSemestrielDatePromulgationBorneSuperieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislaturePrecedenteBilanSemestrielDatePromulgationBorneSuperieure);
	}

	@Override
	public Date getLPBSDatePubliBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_BS_PUBLI_DECRETS_DEBUT).getTime();
	}

	@Override
	public void setLPBSDatePubliBorneInf(
			Date legislaturePrecedenteBilanSemestrielDatePublicationBorneInferieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislaturePrecedenteBilanSemestrielDatePublicationBorneInferieure);
	}

	@Override
	public Date getLPBSDatePubliBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_BS_PUBLI_DECRETS_FIN).getTime();
	}

	@Override
	public void setLPBSDatePubliBorneSup(
			Date legislaturePrecedenteBilanSemestrielDatePublicationBorneSuperieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislaturePrecedenteBilanSemestrielDatePublicationBorneSuperieure);
	}

	@Override
	public Date getLPTauxSPDatePromulBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_TAUXPARL_PROMUL_LOIS_DEBUT).getTime();
	}

	@Override
	public void setLPTauxSPDatePromulBorneInf(
			Date legislaturePrecedenteTauxParlementaireDatePromulgationBorneInferieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislaturePrecedenteTauxParlementaireDatePromulgationBorneInferieure);
	}

	@Override
	public Date getLPTauxSPDatePromulBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_TAUXPARL_PROMUL_LOIS_FIN).getTime();
	}

	@Override
	public void setLPTauxSPDatePromulBorneSup(
			Date legislaturePrecedenteTauxParlementaireDatePromulgationBorneSuperieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislaturePrecedenteTauxParlementaireDatePromulgationBorneSuperieure);
	}

	@Override
	public Date getLPTauxSPDatePubliBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_TAUXPARL_PUBLI_DECRETS_DEBUT).getTime();
	}

	@Override
	public void setLPTauxSPDatePubliBorneInf(
			Date legislaturePrecedenteTauxParlementaireDatePublicationBorneInferieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislaturePrecedenteTauxParlementaireDatePublicationBorneInferieure);
	}

	@Override
	public Date getLPTauxSPDatePubliBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_TAUXPARL_PUBLI_DECRETS_FIN).getTime();
	}

	@Override
	public void setLPTauxSPDatePubliBorneSup(
			Date legislaturePrecedenteTauxParlementaireDatePublicationBorneSuperieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislaturePrecedenteTauxParlementaireDatePublicationBorneSuperieure);
	}

	@Override
	public Date getLPTauxDebutLegisDatePromulBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_TAUXLEGIS_PROMUL_LOIS_DEBUT).getTime();
	}

	@Override
	public void setLPTauxDebutLegisDatePromulBorneInf(
			Date legislaturePrecedenteTauxMagistratureDatePromulgationBorneInferieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislaturePrecedenteTauxMagistratureDatePromulgationBorneInferieure);
	}

	@Override
	public Date getLPTauxDebutLegisDatePromulBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_TAUXLEGIS_PROMUL_LOIS_FIN).getTime();
	}

	@Override
	public void setLPTauxDebutLegisDatePromulBorneSup(
			Date legislaturePrecedenteTauxMagistratureDatePromulgationBorneSuperieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislaturePrecedenteTauxMagistratureDatePromulgationBorneSuperieure);
	}

	@Override
	public Date getLPTauxDebutLegisDatePubliBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_TAUXLEGIS_PUBLI_DECRETS_DEBUT).getTime();
	}

	@Override
	public void setLPTauxDebutLegisDatePubliBorneInf(
			Date legislaturePrecedenteTauxMagistratureDatePublicationBorneInferieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislaturePrecedenteTauxMagistratureDatePublicationBorneInferieure);
	}

	@Override
	public Date getLPTauxDebutLegisDatePubliBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_TAUXLEGIS_PUBLI_DECRETS_FIN).getTime();
	}

	@Override
	public void setLPTauxDebutLegisDatePubliBorneSup(
			Date legislaturePrecedenteTauxMagistratureDatePublicationBorneSuperieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislaturePrecedenteTauxMagistratureDatePublicationBorneSuperieure);
	}

	@Override
	public String getLegislatureEnCours() {
		return getStringProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.LEGIS_ENCOURS);
	}

	@Override
	public void setLegislatureEnCours(String legislatureEnCours) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.LEGIS_ENCOURS,
				legislatureEnCours);
	}

	@Override
	public List<String> getLegislatures() {
		return getListStringProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.LEGISLATURES);
	}

	@Override
	public void setLegislatures(List<String> legislatures) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.LEGISLATURES, legislatures);
	}

	@Override
	public void setLegislaturePublication(String legislaturePublication) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.LEGISLATURE_PUBLICATION,
				legislaturePublication);
	}

	@Override
	public String getLegislaturePublication() {
		return getStringProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.LEGISLATURE_PUBLICATION);
	}

	@Override
	public void setLegislaturePrecPublication(String legislaturePrecPublication) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.LEGISLATURE_PREC_PUBLICATION,
				legislaturePrecPublication);
	}

	@Override
	public String getLegislaturePrecPublication() {
		return getStringProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.LEGISLATURE_PREC_PUBLICATION);
	}

	// ORDOONNANCES

	// Bilan semestriel - legislature en cours
	@Override
	public Date getLECBSDatePubliOrdoBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_BS_PUBLI_ORDO_DEBUT).getTime();
	}

	@Override
	public void setLECBSDatePubliOrdoBorneInf(
			Date legislatureEnCoursBilanSemestrielDatePublicationOrdoBorneInferieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislatureEnCoursBilanSemestrielDatePublicationOrdoBorneInferieure);
	}

	@Override
	public Date getLECBSDatePubliOrdoBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_BS_PUBLI_ORDO_FIN).getTime();
	}

	@Override
	public void setLECBSDatePubliOrdoBorneSup(
			Date legislatureEnCoursBilanSemestrielDatePublicationOrdoBorneSuperieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislatureEnCoursBilanSemestrielDatePublicationOrdoBorneSuperieure);
	}

	@Override
	public Date getLECBSDatePubliDecretOrdoBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_BS_PUBLI_DECRETS_ORDO_DEBUT).getTime();
	}

	@Override
	public void setLECBSDatePubliDecretOrdoBorneInf(
			Date legislatureEnCoursBilanSemestrielDatePublicationDecretOrdoBorneInferieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislatureEnCoursBilanSemestrielDatePublicationDecretOrdoBorneInferieure);
	}

	@Override
	public Date getLECBSDatePubliDecretOrdoBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_BS_PUBLI_DECRETS_ORDO_FIN).getTime();
	}

	@Override
	public void setLECBSDatePubliDecretOrdoBorneSup(
			Date legislatureEnCoursBilanSemestrielDatePublicationDecretOrdoBorneSuperieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_DEBUT_LEGISLATURE,
				legislatureEnCoursBilanSemestrielDatePublicationDecretOrdoBorneSuperieure);
	}

	// Bilan semestriel - legislature précédente
	@Override
	public Date getLPBSDatePubliOrdoBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_BS_PUBLI_ORDO_DEBUT).getTime();
	}

	@Override
	public void setLPBSDatePubliOrdoBorneInf(
			Date legislaturePrecedenteBilanSemestrielDatePublicationOrdoBorneInferieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.PRECED_DEBUT_LEGISLATURE,
				legislaturePrecedenteBilanSemestrielDatePublicationOrdoBorneInferieure);
	}

	@Override
	public Date getLPBSDatePubliOrdoBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_BS_PUBLI_ORDO_FIN).getTime();
	}

	@Override
	public void setLPBSDatePubliOrdoBorneSup(
			Date legislaturePrecedenteBilanSemestrielDatePublicationOrdoBorneSuperieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.PRECED_DEBUT_LEGISLATURE,
				legislaturePrecedenteBilanSemestrielDatePublicationOrdoBorneSuperieure);
	}

	@Override
	public Date getLPBSDatePubliDecretOrdoBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_BS_PUBLI_DECRETS_ORD_DEBUT).getTime();
	}

	@Override
	public void setLPBSDatePubliDecretOrdoBorneInf(
			Date legislaturePrecedenteBilanSemestrielDatePublicationDecretOrdoBorneInferieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.PRECED_DEBUT_LEGISLATURE,
				legislaturePrecedenteBilanSemestrielDatePublicationDecretOrdoBorneInferieure);
	}

	@Override
	public Date getLPBSDatePubliDecretOrdoBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_BS_PUBLI_DECRETS_ORDO_FIN).getTime();
	}

	@Override
	public void setLPBSDatePubliDecretOrdoBorneSup(
			Date legislaturePrecedenteBilanSemestrielDatePublicationDecretOrdoBorneSuperieure) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.PRECED_DEBUT_LEGISLATURE,
				legislaturePrecedenteBilanSemestrielDatePublicationDecretOrdoBorneSuperieure);
	}

	@Override
	public Date getLECTauxSPDatePubliOrdoBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_TAUXPARL_PROMUL_ORDO_DEBUT).getTime();
	}

	@Override
	public void setLECTauxSPDatePubliOrdoBorneInf(Date borneInf) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_TAUXPARL_PROMUL_ORDO_DEBUT,
				borneInf);
	}

	@Override
	public Date getLECTauxSPDatePubliOrdoBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_TAUXPARL_PROMUL_ORDO_FIN).getTime();
	}

	@Override
	public void setLECTauxSPDatePubliOrdoBorneSup(Date borneSup) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_TAUXPARL_PROMUL_ORDO_FIN,
				borneSup);
	}

	@Override
	public Date getLECTauxSPDatePubliDecretOrdoBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_TAUXPARL_PUBLI_DECRETS_ORDO_DEBUT).getTime();
	}

	@Override
	public void setLECTauxSPDatePubliDecretOrdoBorneInf(Date borneInf) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_TAUXPARL_PUBLI_DECRETS_ORDO_DEBUT,
				borneInf);
	}

	@Override
	public Date getLECTauxSPDatePubliDecretOrdoBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_TAUXPARL_PUBLI_DECRETS_ORDO_FIN).getTime();
	}

	@Override
	public void setLECTauxSPDatePubliDecretOrdoBorneSup(Date borneSup) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_TAUXPARL_PUBLI_DECRETS_ORDO_FIN,
				borneSup);
	}

	@Override
	public Date getLECTauxDebutLegisDatePubliOrdoBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_TAUXLEGIS_PROMUL_ORDO_DEBUT).getTime();
	}

	@Override
	public void setLECTauxDebutLegisDatePubliOrdoBorneInf(Date borneInf) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_TAUXLEGIS_PROMUL_ORDO_DEBUT,
				borneInf);
	}

	@Override
	public Date getLECTauxDebutLegisDatePubliOrdoBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_TAUXLEGIS_PROMUL_ORDO_FIN).getTime();
	}

	@Override
	public void setLECTauxDebutLegisDatePubliOrdoBorneSup(Date borneSup) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_TAUXLEGIS_PROMUL_ORDO_FIN,
				borneSup);
	}

	@Override
	public Date getLECTauxDebutLegisDatePubliDecretOrdoBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_TAUXLEGIS_PUBLI_DECRETS_ORDO_DEBUT).getTime();
	}

	@Override
	public void setLECTauxDebutLegisDatePubliDecretOrdoBorneInf(Date borneInf) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_TAUXLEGIS_PUBLI_DECRETS_ORDO_DEBUT,
				borneInf);
	}

	@Override
	public Date getLECTauxDebutLegisDatePubliDecretOrdoBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.ENCOURS_TAUXLEGIS_PUBLI_DECRETS_ORDO_FIN).getTime();
	}

	@Override
	public void setLECTauxDebutLegisDatePubliDecretOrdoBorneSup(Date borneSup) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.ENCOURS_TAUXLEGIS_PUBLI_DECRETS_ORDO_FIN,
				borneSup);
	}

	@Override
	public Date getLPTauxSPDatePubliOrdoBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_TAUXPARL_PROMUL_ORDO_DEBUT).getTime();
	}

	@Override
	public void setLPTauxSPDatePubliOrdoBorneInf(Date borneInf) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.PRECED_TAUXPARL_PROMUL_ORDO_DEBUT,
				borneInf);
	}

	@Override
	public Date getLPTauxSPDatePubliOrdoBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_TAUXPARL_PROMUL_ORDO_FIN).getTime();
	}

	@Override
	public void setLPTauxSPDatePubliOrdoBorneSup(Date borneSup) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.PRECED_TAUXPARL_PROMUL_ORDO_FIN,
				borneSup);
	}

	@Override
	public Date getLPTauxSPDatePubliDecretOrdoBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_TAUXPARL_PUBLI_DECRETS_ORDO_DEBUT).getTime();
	}

	@Override
	public void setLPTauxSPDatePubliDecretOrdoBorneInf(Date borneInf) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.PRECED_TAUXPARL_PUBLI_DECRETS_ORDO_DEBUT,
				borneInf);
	}

	@Override
	public Date getLPTauxSPDatePubliDecretOrdoBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_TAUXPARL_PUBLI_DECRETS_ORDO_FIN).getTime();
	}

	@Override
	public void setLPTauxSPDatePubliDecretOrdoBorneSup(Date borneSup) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.PRECED_TAUXPARL_PUBLI_DECRETS_ORDO_FIN,
				borneSup);
	}

	@Override
	public Date getLPTauxDebutLegisDatePubliOrdoBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_TAUXLEGIS_PROMUL_ORDO_DEBUT).getTime();
	}

	@Override
	public void setLPTauxDebutLegisDatePubliOrdoBorneInf(Date borneInf) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.PRECED_TAUXLEGIS_PROMUL_ORDO_DEBUT,
				borneInf);
	}

	@Override
	public Date getLPTauxDebutLegisDatePubliOrdoBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_TAUXLEGIS_PROMUL_ORDO_FIN).getTime();
	}

	@Override
	public void setLPTauxDebutLegisDatePubliOrdonnanceBorneSup(Date borneSup) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.PRECED_TAUXLEGIS_PROMUL_ORDO_FIN,
				borneSup);
	}

	@Override
	public Date getLPTauxDebutLegisDatePubliDecretOrdoBorneInf() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_TAUXLEGIS_PUBLI_DECRETS_ORDO_DEBUT).getTime();
	}

	@Override
	public void setLPTauxDebutLegisDatePubliDecretOrdoBorneInf(Date borneInf) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.PRECED_TAUXLEGIS_PUBLI_DECRETS_ORDO_DEBUT,
				borneInf);
	}

	@Override
	public Date getLPTauxDebutLegisDatePubliDecretOrdoBorneSup() {
		return getDateProperty(SolonEpgParametresANConstants.SCHEMA,
				SolonEpgParametresANConstants.PRECED_TAUXLEGIS_PUBLI_DECRETS_ORDO_FIN).getTime();
	}

	@Override
	public void setLPTauxDebutLegisDatePubliDecretOrdoBorneSup(Date borneSup) {
		setProperty(SolonEpgParametresANConstants.SCHEMA, SolonEpgParametresANConstants.PRECED_TAUXLEGIS_PUBLI_DECRETS_ORDO_DEBUT,
				borneSup);
	}
}
