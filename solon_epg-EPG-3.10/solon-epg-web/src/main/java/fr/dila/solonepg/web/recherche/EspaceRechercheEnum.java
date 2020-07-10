package fr.dila.solonepg.web.recherche;

import java.util.ArrayList;
import java.util.List;

import fr.dila.solonepg.api.constant.SolonEpgViewConstant;

public enum EspaceRechercheEnum {
	
	RECHERCHE("RECHERCHE", "label.recherche.requeteur", "BASE", "/img/icons/search.png", "view_espace_recherche"),
	RECHERCHE_DOSSIER("RECHERCHE", "espace.recherche.dossiers", "OTHER", "/img/icons/search.png", "_dossier"),
	RECHERCHE_FDR("RECHERCHE", "espace.recherche.fdr", "OTHER", "/img/icons/search.png", "_fdr"),
	RECHERCHE_USER("RECHERCHE", "espace.recherche.user", "OTHER", "/img/icons/search.png", "_user"),
	DERNIERS_RESULTATS_CONSULTES("DERNIERS_RESULTATS_CONSULTES", "espace.recherche.consultation.derniers.resultats.consultes", "BASE", "/img/icons/folder.png", SolonEpgViewConstant.ESPACE_RECHERCHE_RESULTATS_CONSULTES_VIEW),
	FAVORIS_CONSULTATION("FAVORIS_CONSULTATION", "espace.recherche.consultation.favoris.consultation", "BASE", "/img/icons/folder.png", SolonEpgViewConstant.ESPACE_RECHERCHE_FAVORIS_CONSULTATIONS_VIEW),
	FAVORIS_RECHERCHE("FAVORIS_RECHERCHE", "espace.recherche.consultation.favoris.recherche", "BASE_CLICK", "/img/icons/folder.png", SolonEpgViewConstant.ESPACE_RECHERCHE_FAVORIS_RECHERCHE_CONTENT_VIEW),
	ORGANNIGRAMME("ORGANNIGRAMME", "label.recherche.consultation.organigramme", "BASE_CLICK", "img/icons/element_find.png", "view_organigramme"),
	UTILISATEURS("UTILISATEURS", "label.recherche.consultation.utilisateurs", "BASE_CLICK", "img/icons/element_find.png", "view_many_users"),
	DOSSIERS("DOSSIERS", "espace.recherche.dossiers", "OTHER", "/img/icons/folder.png", "_dossier"),
	FDR("FDR", "espace.recherche.fdr", "OTHER", "/img/icons/folder.png", "_fdr"),
	USER("USER", "espace.recherche.user", "OTHER", "/img/icons/folder.png", "_user");
	
	private String id;
	private String label;
	private String type;
	private String img;
	private String link;
	
	private static List<EspaceRechercheEnum> listRoot = new ArrayList<EspaceRechercheEnum>();
	static{
	    listRoot.add(RECHERCHE);
	    listRoot.add(DERNIERS_RESULTATS_CONSULTES);
	    listRoot.add(FAVORIS_CONSULTATION);
        listRoot.add(FAVORIS_RECHERCHE);
        listRoot.add(ORGANNIGRAMME);
        listRoot.add(UTILISATEURS);
	}

	EspaceRechercheEnum(String id, String label, String type, String img, String link){
		this.id = id;
		this.label = label;
		this.type = type;
		this.img = img;
		this.link = link;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	public void setLink(String link) {
		this.link = link;
	}

	public String getLink() {
		return link;
	}
	
	public static List<EspaceRechercheEnum> getRoots(){
		return listRoot;
	}
	
	public List<EspaceRechercheEnum> getChildren(){
		List<EspaceRechercheEnum> list = new ArrayList<EspaceRechercheEnum>();
		switch (this) {
		case DERNIERS_RESULTATS_CONSULTES:
		case FAVORIS_CONSULTATION:
			list.add(DOSSIERS);
			list.add(FDR);
			list.add(USER);
			break;
		case RECHERCHE:
			list.add(RECHERCHE_DOSSIER);
			list.add(RECHERCHE_FDR);
			list.add(RECHERCHE_USER);
			break;
		default:
			break;
		}
		
		return list;
	}

}
