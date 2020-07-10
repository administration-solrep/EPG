package fr.dila.solonepg.page.recherche.favoris.consultation;

import fr.dila.solonepg.page.commun.EPGWebPage;

public abstract class AbstractFavorisConsultation extends EPGWebPage {

    /**
     * First Row => Index = 1
     * 
     * @param index start by 1
     */
    public void selectRow(int index) {

        String replaceBy = index == 1 ? "" : new StringBuilder("_").append(Integer.toString(index - 1)).toString();
        String checkBoxId = index == 1 ? this.getCheckBoxId().replace("@INDEX@", replaceBy) : this.getCheckBoxId().replace("@INDEX@", replaceBy);
        this.getElementById(checkBoxId).click();
        sleep(1);
    }

    /**
     * Clic sur le boutton retirer des favoris de consultations
     */
    public void retirerDesFavorisDeConsultation() {
        this.getElementById(this.getRetirerDesFavorisDeConsultation()).click();
        sleep(1);
    }

    protected abstract String getCheckBoxId();

    protected abstract String getRetirerDesFavorisDeConsultation();
}
