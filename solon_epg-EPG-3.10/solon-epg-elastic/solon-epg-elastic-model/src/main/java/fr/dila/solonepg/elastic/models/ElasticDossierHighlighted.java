package fr.dila.solonepg.elastic.models;

/**
 * {@link Dossier} avec des champs supplémentaires pour le highlight
 * 
 * @author mmaury
 * 
 */
public class ElasticDossierHighlighted extends ElasticDossier {

	private static final long	serialVersionUID	= 1L;

	private String				titleHighlighted;

	public String getTitleHighlighted() {
		return titleHighlighted;
	}

	public void setTitleHighlighted(String titleHighlighted) {
		this.titleHighlighted = titleHighlighted;
	}
}
