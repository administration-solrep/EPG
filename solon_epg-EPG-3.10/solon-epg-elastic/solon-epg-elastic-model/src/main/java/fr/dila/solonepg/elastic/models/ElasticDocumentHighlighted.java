package fr.dila.solonepg.elastic.models;

/**
 * {@link Document} avec des champs supplémentaires pour le highlight
 * 
 * @author mmaury
 * 
 */
public class ElasticDocumentHighlighted extends ElasticDocument {

	private static final long	serialVersionUID	= 1L;

	private String				titleHighlighted;
	private String				texteHighlighted;

	public String getTitleHighlighted() {
		return titleHighlighted;
	}

	public void setTitleHighlighted(String titleHighlighted) {
		this.titleHighlighted = titleHighlighted;
	}

	public String getTexteHighlighted() {
		return texteHighlighted;
	}

	public void setTexteHighlighted(String texteHighlighted) {
		this.texteHighlighted = texteHighlighted;
	}
}
