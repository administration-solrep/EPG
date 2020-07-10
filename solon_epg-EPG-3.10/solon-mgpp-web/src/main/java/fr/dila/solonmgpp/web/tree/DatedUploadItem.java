package fr.dila.solonmgpp.web.tree;

import java.util.Date;
import org.richfaces.model.UploadItem;

/**
 * 
 * Objet qui surcharge UploadItem pour y ajouter une date de lancement.
 * Cette Date de lancement sera utiliser vérifier qu'un traitement d'upload n'est pas lancé 
 * plusieur fois dans la même seconde (Mantis 155426).
 * @author Benjamin Beaunée
 *
 */


public class DatedUploadItem extends UploadItem{

	/** constructor **/
	public DatedUploadItem(String fileName, int fileSize, String contentType, Object file) {
		super(fileName, fileSize, contentType, file);
	}
	
	/** Strat date */
    private Date startDate;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}
