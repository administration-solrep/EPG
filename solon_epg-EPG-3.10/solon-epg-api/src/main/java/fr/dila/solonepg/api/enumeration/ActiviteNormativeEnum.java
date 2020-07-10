package fr.dila.solonepg.api.enumeration;

import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;

public enum ActiviteNormativeEnum {
	APPICATION_DES_LOIS("APPICATION_DES_LOIS", "espace.activite.normative.application.lois",
			SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_READER,
			ActiviteNormativeConstants.MENU_0, ActiviteNormativeConstants.TYPE_APPLICATION_LOIS,
			ActiviteNormativeConstants.ATTRIBUT_APPLICATION_LOIS), APPLICATION_DES_ORDONNANCES(
			"APPLICATION_DES_ORDONNANCES", "espace.activite.normative.application.ordonnances",
			SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_READER,
			ActiviteNormativeConstants.MENU_5, ActiviteNormativeConstants.TYPE_APPLICATION_ORDONNANCES,
			ActiviteNormativeConstants.ATTRIBUT_APPLICATION_ORDONNANCES), TRANSPOSITION("TRANSPOSITION",
			"espace.activite.normative.transposition",
			SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRANSPOSITION_READER,
			ActiviteNormativeConstants.MENU_3, ActiviteNormativeConstants.TYPE_TRANSPOSITIONS,
			ActiviteNormativeConstants.ATTRIBUT_TRANSPOSITION), ORDONNANCES_38C("ORDONNANCES_38C",
			"espace.activite.normative.ordonnances.38C",
			SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_SUIVIS_READER, ActiviteNormativeConstants.MENU_4,
			ActiviteNormativeConstants.TYPE_ORDONNANCES_38C, ActiviteNormativeConstants.ATTRIBUT_ORDONNANCE_38C), ORDONNANCES(
			"ORDONNANCES", "espace.activite.normative.ordonnances",
			SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_READER,
			ActiviteNormativeConstants.MENU_1, ActiviteNormativeConstants.TYPE_ORDONNANCES,
			ActiviteNormativeConstants.ATTRIBUT_ORDONNANCE), TRAITES_ET_ACCORDS("TRAITES_ET_ACCORDS",
			"espace.activite.normative.traites.accords",
			SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRAITE_READER, ActiviteNormativeConstants.MENU_2,
			ActiviteNormativeConstants.TYPE_TRAITES_ACCORDS, ActiviteNormativeConstants.ATTRIBUT_TRAITE_ACCORD), APPLICATION_DES_LOIS_MINISTERE(
			"APPLICATION_DES_LOIS_MINISTERE", "espace.activite.normative.application.lois",
			SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_LOI_MINISTERE,
			ActiviteNormativeConstants.MENU_0, ActiviteNormativeConstants.TYPE_APPLICATION_LOIS,
			ActiviteNormativeConstants.ATTRIBUT_APPLICATION_LOIS), APPLICATION_DES_ORDONNANCES_MINISTERE(
			"APPLICATION_DES_ORDONNANCES_MINISTERE", "espace.activite.normative.application.ordonnances",
			SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_ORDONNANCE_MINISTERE,
			ActiviteNormativeConstants.MENU_5, ActiviteNormativeConstants.TYPE_APPLICATION_ORDONNANCES,
			ActiviteNormativeConstants.ATTRIBUT_APPLICATION_ORDONNANCES);

	private String	id;
	private String	label;
	private String	right;
	private String	menuIndex;
	private String	typeActiviteNormative;
	private String	attributSchema;

	ActiviteNormativeEnum(String id, String label, String right, String menuIndex, String typeActiviteNormative,
			String attributSchema) {
		this.id = id;
		this.label = label;
		this.right = right;
		this.menuIndex = menuIndex;
		this.typeActiviteNormative = typeActiviteNormative;
		this.attributSchema = attributSchema;
	}

	protected void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	protected void setLabel(String label) {
		this.label = label;
	}

	protected void setRight(String right) {
		this.right = right;
	}

	public String getRight() {
		return right;
	}

	protected void setMenuIndex(String menuIndex) {
		this.menuIndex = menuIndex;
	}

	public String getMenuIndex() {
		return menuIndex;
	}

	public static ActiviteNormativeEnum getById(String id) {
		for (ActiviteNormativeEnum elem : ActiviteNormativeEnum.values()) {
			if (elem.getId().equals(id)) {
				return elem;
			}
		}
		return null;
	}

	protected void setTypeActiviteNormative(String typeActiviteNormative) {
		this.typeActiviteNormative = typeActiviteNormative;
	}

	public String getTypeActiviteNormative() {
		return typeActiviteNormative;
	}

	public static ActiviteNormativeEnum getByType(String type) {
		for (ActiviteNormativeEnum elem : ActiviteNormativeEnum.values()) {
			if (elem.getTypeActiviteNormative().equals(type)) {
				return elem;
			}
		}
		return null;
	}

	public String getAttributSchema() {
		return attributSchema;
	}

	protected void setAttributSchema(String attributSchema) {
		this.attributSchema = attributSchema;
	}

}
