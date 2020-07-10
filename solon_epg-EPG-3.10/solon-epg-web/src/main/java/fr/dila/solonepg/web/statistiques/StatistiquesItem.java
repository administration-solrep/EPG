package fr.dila.solonepg.web.statistiques;

public class StatistiquesItem  {
    private Integer id;
	private String label;
	private String category;
	private String rapportBirt;

    public StatistiquesItem(Integer id, String label, String rapportBirt, String category) {
        super();
        this.setId(id);
        this.label = label;
        this.category = category;
        this.rapportBirt = rapportBirt;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String toString(){
		return "StatisticItem : [label = " + label + ", category = " + category ;
		
	}

    public void setRapportBirt(String rapportBirt) {
        this.rapportBirt = rapportBirt;
    }

    public String getRapportBirt() {
        return rapportBirt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

}
