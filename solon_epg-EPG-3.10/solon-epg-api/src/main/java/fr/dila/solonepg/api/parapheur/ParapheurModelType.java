package fr.dila.solonepg.api.parapheur;

import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;

/**
 * Types de document contenus dans l'arborescence du modèle de parapheur.
 * 
 * @author antoine Rolin
 */
public enum ParapheurModelType {
        PARAPHEUR_MODEL_FOLDER_UNDELETABLE (SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_UNDELETABLE  ), PARAPHEUR_MODEL_FOLDER (SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_TYPE );
	
	private String value;
	
	ParapheurModelType(String type)
	{
		this.setValue(type);
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public static ParapheurModelType getEnum(String enumValue)
	{
		ParapheurModelType type = null;
		if(PARAPHEUR_MODEL_FOLDER.value.equals(enumValue)){
			type = PARAPHEUR_MODEL_FOLDER;
		} else if(PARAPHEUR_MODEL_FOLDER_UNDELETABLE.value.equals(enumValue)){
		    type = PARAPHEUR_MODEL_FOLDER_UNDELETABLE;
		}
		
		return type;
	}
}
