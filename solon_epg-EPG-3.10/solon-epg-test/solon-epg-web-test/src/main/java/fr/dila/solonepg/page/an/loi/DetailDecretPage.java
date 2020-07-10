package fr.dila.solonepg.page.an.loi;

import junit.framework.Assert;

import org.openqa.selenium.WebElement;

import fr.dila.solonepg.page.commun.EPGWebPage;

public class DetailDecretPage extends EPGWebPage {

    private static final String NOR_ID = "texte_maitre_decret:decret_input";
    private static final String ADD_BTN_ID = "texte_maitre_decret:addActiviteNormativedecret";
    private static final String REMOVE_BTN_ID = "texte_maitre_decret:nxl_texte_maitre_decret_listing:nxw_activite_normative_widget_decret_remove:nxw_activite_normative_widget_decret_removedelete_img";
    private static final String SAUVEGARDER_BTN_PATH = "//span[@id='texte_maitre_decret:decret_actions']/a[1]/img";
    private static final String REFRESH_BTN_PATH = "//span[@id='texte_maitre_decret:decret_actions']/a[2]/img";

    public static final String TITLE_ID = "texte_maitre_decret:nxl_texte_maitre_decret_listing:nxw_activite_normative_widget_decret_titreDecret:nxw_activite_normative_widget_decret_titreDecret_text";
    public static final String SECTIONDUCE_ID = "texte_maitre_decret:nxl_texte_maitre_decret_listing:nxw_activite_normative_widget_sectionCE_lock:nxw_activite_normative_widget_sectionCE_lock_text";
    public static final String DATE_SAISIE_CE_ID = "texte_maitre_decret:nxl_texte_maitre_decret_listing:nxw_activite_normative_widget_dateSaisineCE_lock:nxw_activite_normative_widget_dateSaisineCE_lock_edittimeInputDate";
    public static final String DATE_EXAMEN_CE_ID = "texte_maitre_decret:nxl_texte_maitre_decret_listing:nxw_activite_normative_widget_decret_dateExamenCE:nxw_activite_normative_widget_decret_dateExamenCE_edittimeInputDate";
    public static final String RAPPORTEUR_CE_ID = "texte_maitre_decret:nxl_texte_maitre_decret_listing:nxw_activite_normative_widget_decret_rapporteurCE:nxw_activite_normative_widget_decret_rapporteurCE_text";
    public static final String REFERENCE_AVIS_CE_ID = "texte_maitre_decret:nxl_texte_maitre_decret_listing:nxw_activite_normative_widget_decret_referenceAvisCE";
    public static final String NUMERO_DU_TEXT_ID = "texte_maitre_decret:nxl_texte_maitre_decret_listing:nxw_activite_normative_widget_decret_numerosTextes";
    public static final String DATE_SIGNATURE_ID = "texte_maitre_decret:nxl_texte_maitre_decret_listing:nxw_activite_normative_widget_decret_dateSignatureInputDate";
    public static final String DATE_PUBLICATION_ID = "texte_maitre_decret:nxl_texte_maitre_decret_listing:nxw_activite_normative_widget_data_datePublication_and_lock:nxw_activite_normative_widget_data_datePublication_and_lock_edittimeInputDate";
    public static final String NUMERO_JO_ID = "texte_maitre_decret:nxl_texte_maitre_decret_listing:nxw_activite_normative_widget_decret_numeroJOPublication:nxw_activite_normative_widget_decret_numeroJOPublication_text";
    public static final String NUMERO_PAGE_ID = "texte_maitre_decret:nxl_texte_maitre_decret_listing:nxw_activite_normative_widget_decret_numeroPage";
    public static final String DECRET_NOR_INPUT_ID = "texte_maitre_decret:nxl_texte_maitre_decret_listing:nxw_activite_normative_widget_numeroNor_lock:nxw_activite_normative_widget_numeroNor_lock_text";

    private void lockElement(String elementId) {
        getElementById(elementId).click();
    }

    private String adjustPath(String mainPath, String toBeReplace, String replaceby) {
        int index = mainPath.lastIndexOf(toBeReplace);
        String substring = mainPath.substring(0, index);
        return substring + replaceby;
    }

    public void setNumeroPage(String numeroPageId, boolean lock) {
        this.fillElement(NUMERO_PAGE_ID, "N° de page", numeroPageId);
        if (lock) {
            lockElement(adjustPath(TITLE_ID, "text", "check"));
        }
    }

    public void setNumeroJo(String numeroJo) {
        this.fillElement(NUMERO_JO_ID, "N° du JO", numeroJo);
    }

    public void setDatePublication(String datePublication, boolean lock) {
        this.fillElement(DATE_PUBLICATION_ID, "Date publication", datePublication);
        if (lock) {
            lockElement(adjustPath(DATE_EXAMEN_CE_ID, "edittimeInputDate", "check"));
        }
    }

    public void setDateSignature(String dateSignature) {
        fillElement(DATE_SIGNATURE_ID, "Date signature", dateSignature);
    }

    public void setNumeroDuText(String numeroDuText) {
        fillElement(NUMERO_DU_TEXT_ID, "N° texte", numeroDuText);
    }

    public void setReferenceAvisCe(String referenceAvisCe) {
        fillElement(REFERENCE_AVIS_CE_ID, "Réference Avis CE", referenceAvisCe);
    }

    public void setRaporteurCe(String raporteurCe, boolean lock) {
        this.fillElement(RAPPORTEUR_CE_ID, "Rapporteur CE", raporteurCe);
        if (lock) {
            lockElement(adjustPath(RAPPORTEUR_CE_ID, "text", "check"));
        }
    }

    public void setDateExamenCe(String dateExamenCe, boolean lock) {
        this.fillElement(DATE_EXAMEN_CE_ID, "Date d'examen CE", dateExamenCe);
        if (lock) {
            lockElement(adjustPath(DATE_EXAMEN_CE_ID, "edittimeInputDate", "check"));
        }
    }

    public void setDateSaisieCe(String dateSaisieCe, boolean lock) {
        this.fillElement(DATE_SAISIE_CE_ID, "Date de saisine CE", dateSaisieCe);
        if (lock) {
            lockElement(adjustPath(DATE_SAISIE_CE_ID, "edittimeInputDate", "check"));
        }
    }

    public void setSectionDuCe(String section, boolean lock) {
        this.fillElement(SECTIONDUCE_ID, "Section du CE", section);
        if (lock) {
            lockElement(adjustPath(SECTIONDUCE_ID, "text", "check"));
        }
    }

    public void setTitre(String titre) {
        fillElement(TITLE_ID, "Titre", titre);
    }

    public void ajouterNor(String nor) {
        String waitForPath = "//a[@href=\"http://www.legifrance.org/WAspad/UnTexteDeJorf?numjo=" + nor + "\"]";
        WebElement el = getElementById(NOR_ID);
        fillField("Nor", el, nor);
        getElementById(ADD_BTN_ID).click();
        waitElementVisibilityByXpath(waitForPath);
    }

    public void verifyValue(String id, String value) {
    	//On fait en deux étapes pour éviter une NPE (ce qui est moins visible dans les logs)
    	WebElement element = getElementById(id);
    	Assert.assertNotNull(element);
        String text = element.getAttribute("value");
        Assert.assertEquals(value, text);
    }

    public void remove() {
        getElementById(REMOVE_BTN_ID).click();

        // Wait for popup to Open
        waitElementVisibilityById("popup_container");

        // Confirmer
        getElementById("popup_ok").click();

        sleep(2);
    }

    public void assignDecretNor(String nor) { 
    	waitElementVisibilityById(DECRET_NOR_INPUT_ID);
        fillElement(DECRET_NOR_INPUT_ID, "Nor", nor);
        sauvegarder();
    }

    public void selectNorCheckBox() {
    	waitElementVisibilityById("texte_maitre_decret:nxl_texte_maitre_decret_listing:nxw_activite_normative_widget_validation:nxw_activite_normative_widget_validation_check");
        getElementById("texte_maitre_decret:nxl_texte_maitre_decret_listing:nxw_activite_normative_widget_validation:nxw_activite_normative_widget_validation_check").click();
    }

    public void verifylegifranceLink(String nor) {

        String path = "//a[@href=\"http://www.legifrance.org/WAspad/UnTexteDeJorf?numjo=" + nor + "\"]";
        WebElement element = this.getElementByXpath(path);
        Assert.assertNotNull(element);
    }

    public void sauvegarder() {
    	sleep(2);
        getElementByXpath(SAUVEGARDER_BTN_PATH).click();
        sleep(2);
    }

    public void refresh() {
        getElementByXpath(REFRESH_BTN_PATH).click();
        sleep(2);
    }

    public void assertElementText(String id, String value) {
        super.assertElementValue(By_enum.ID, id, value);
    }

    public void assertElementValue(String identifier, String value) {
        WebElement elementById = this.getElementById(identifier);
        String attribute = elementById.getAttribute("value");
        Assert.assertEquals(value, attribute);
    }

}
