package fr.dila.solonmgpp.api.dto;

import java.util.Comparator;

/**
 * interface de representation d'une Identite
 * 
 * @author asatre
 * 
 */
public interface IdentiteDTO extends TableReferenceDTO {

	String getPrenom();

	void setPrenom(String prenom);

	String getCivilite();

	void setCivilite(String civilite);

	String getTitleModifie();

	public static Comparator<IdentiteDTO>	COMPARE	= new Comparator<IdentiteDTO>() {
														public int compare(IdentiteDTO one, IdentiteDTO two) {
															// Comparaison effectuée pour trier les données
															// Comparaison d'abord sur le nom, puis sur le prénom, puis
															// sur les dates de mandat
															if (two.getNom().toUpperCase()
																	.compareTo(one.getNom().toUpperCase()) == 0) {
																if (two.getPrenom().toUpperCase()
																		.compareTo(one.getPrenom().toUpperCase()) == 0
																		&& two.getDateDebutMandat() != null
																		&& one.getDateDebutMandat() != null) {
																	if (two.getDateDebutMandat().compareTo(
																			one.getDateDebutMandat()) == 0
																			&& two.getDateFinMandat() != null
																			&& one.getDateFinMandat() != null) {
																		return two.getDateFinMandat().compareTo(
																				one.getDateFinMandat());
																	} else {
																		return two.getDateDebutMandat().compareTo(
																				one.getDateDebutMandat());
																	}
																} else {
																	return two.getPrenom().toUpperCase()
																			.compareTo(one.getPrenom().toUpperCase());
																}
															} else {
																return one.getNom().toUpperCase()
																		.compareTo(two.getNom().toUpperCase());
															}

														}
													};

}
