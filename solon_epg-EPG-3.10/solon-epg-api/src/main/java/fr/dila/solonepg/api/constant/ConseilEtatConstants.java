package fr.dila.solonepg.api.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ConseilEtatConstants {

	public static final String	CONSEIL_ETAT_SCHEMA_PREFIX							= "consetat";

	public static final String	CONSEIL_ETAT_SCHEMA									= "conseil_etat";

	public static final String	CONSEIL_ETAT_SECTION_CE_PROPERTY					= "sectionCe";

	public static final String	CONSEIL_ETAT_RAPPORTEUR_CE_PROPERTY					= "rapporteurCe";

	public static final String	CONSEIL_ETAT_DATE_TRANSMISSION_SECTION_CE_PROPERTY	= "dateTransmissionSectionCe";

	public static final String	CONSEIL_ETAT_DATE_SECTION_CE_PROPERTY				= "dateSectionCe";

	public static final String	CONSEIL_ETAT_DATE_AG_CE_PROPERTY					= "dateAgCe";

	public static final String	CONSEIL_ETAT_NUMERO_ISA_PROPERTY					= "numeroISA";

	public static final String	CONSEIL_ETAT_DATE_SAISINE_CE_PROPERTY				= "dateSaisineCE";

	public static final String	CONSEIL_ETAT_DATE_SORTIE_CE_PROPERTY				= "dateSortieCE";

	public static final String	CONSEIL_ETAT_PRIORITE_PROPERTY						= "priorite";

	private ConseilEtatConstants() {
		// private empty constructor
	}

	// Nom des sections
	public static final String			CONSEIL_ETAT_SECTION_CE						= "Section CE";
	public static final String			CONSEIL_ETAT_SECTION_ADMINISTRATION			= "Section de l'administration";
	public static final String			CONSEIL_ETAT_SECTION_INTERIEUR				= "Section de l'intérieur";
	public static final String			CONSEIL_ETAT_SECTION_INTERIEUR_SANS_ACCENT	= "Section de l'interieur";
	public static final String			CONSEIL_ETAT_SECTION_FINANCE				= "Section des finances";
	public static final String			CONSEIL_ETAT_SECTION_TP						= "Section des travaux publics";
	public static final String			CONSEIL_ETAT_SECTION_SOCIALE				= "Section sociale";

	// Liste des sections du conseil d'état
	public static final List<String>	sectionConseilEtatList						= Collections
																							.unmodifiableList(Arrays
																									.asList(new String[] {
			CONSEIL_ETAT_SECTION_CE, CONSEIL_ETAT_SECTION_ADMINISTRATION, CONSEIL_ETAT_SECTION_INTERIEUR,
			CONSEIL_ETAT_SECTION_FINANCE, CONSEIL_ETAT_SECTION_TP, CONSEIL_ETAT_SECTION_SOCIALE,
			CONSEIL_ETAT_SECTION_INTERIEUR_SANS_ACCENT

																									}));
	// Nom des sections abrégés
	public static final String			CONSEIL_ETAT_SECTION_CE_SHORT				= "CE";
	public static final String			CONSEIL_ETAT_SECTION_ADMINISTRATION_SHORT	= "ADMINISTRATION";
	public static final String			CONSEIL_ETAT_SECTION_INTERIEUR_SHORT		= "INTERIEUR";
	public static final String			CONSEIL_ETAT_SECTION_FINANCE_SHORT			= "FINANCES";
	public static final String			CONSEIL_ETAT_SECTION_TP_SHORT				= "TRAVAUX PUBLICS";
	public static final String			CONSEIL_ETAT_SECTION_SOCIALE_SHORT			= "SOCIALE";

	// Liste des sections du conseil d'état abrégé
	public static final List<String>	sectionConseilEtatListShort					= Collections
																							.unmodifiableList(Arrays
																									.asList(new String[] {
			CONSEIL_ETAT_SECTION_CE_SHORT, CONSEIL_ETAT_SECTION_ADMINISTRATION_SHORT,
			CONSEIL_ETAT_SECTION_INTERIEUR_SHORT, CONSEIL_ETAT_SECTION_FINANCE_SHORT, CONSEIL_ETAT_SECTION_TP_SHORT,
			CONSEIL_ETAT_SECTION_SOCIALE_SHORT

																									}));

}
