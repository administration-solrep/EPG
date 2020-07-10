package fr.dila.solonepg.api.parapheur;

import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;

/**
 * Types de document contenus dans l'arborescence du parapheur.
 * 
 * @author antoine Rolin
 */
public enum ParapheurType {
        PARAPHEUR_FOLDER_NON_VIDE_DOC_AJOUTABLE (SolonEpgParapheurConstants.PARAPHEUR_FOLDER_NON_VIDE_DOC_AJOUTABLE  ),
        PARAPHEUR_FOLDER_NON_VIDE_DOC_NON_AJOUTABLE (SolonEpgParapheurConstants.PARAPHEUR_FOLDER_NON_VIDE_DOC_NON_AJOUTABLE  ),
        PARAPHEUR_FOLDER_VIDE_DOC_NON_AJOUTABLE (SolonEpgParapheurConstants.PARAPHEUR_FOLDER_VIDE_DOC_NON_AJOUTABLE  ),
        PARAPHEUR_FOLDER_VIDE_DOC_AJOUTABLE (SolonEpgParapheurConstants.PARAPHEUR_FOLDER_VIDE_DOC_AJOUTABLE  ),
        PARAPHEUR_FILE (SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE);
	
	private String value;
	
	ParapheurType(String type)
	{
		this.setValue(type);
	}

	private void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public static ParapheurType getEnum(String enumValue)
	{
		ParapheurType type = null;
		if(PARAPHEUR_FOLDER_NON_VIDE_DOC_AJOUTABLE.value.equals(enumValue)){
			type = PARAPHEUR_FOLDER_NON_VIDE_DOC_AJOUTABLE;
		} else if(PARAPHEUR_FOLDER_NON_VIDE_DOC_NON_AJOUTABLE.value.equals(enumValue)){
		    type = PARAPHEUR_FOLDER_NON_VIDE_DOC_NON_AJOUTABLE;
                } else if(PARAPHEUR_FOLDER_VIDE_DOC_NON_AJOUTABLE.value.equals(enumValue)){
                    type = PARAPHEUR_FOLDER_VIDE_DOC_NON_AJOUTABLE;
                } else if(PARAPHEUR_FOLDER_VIDE_DOC_AJOUTABLE.value.equals(enumValue)){
                    type = PARAPHEUR_FOLDER_VIDE_DOC_AJOUTABLE;
                } else if(PARAPHEUR_FILE.value.equals(enumValue)){
                    type = PARAPHEUR_FILE;
                }
		
		return type;
	}
}
