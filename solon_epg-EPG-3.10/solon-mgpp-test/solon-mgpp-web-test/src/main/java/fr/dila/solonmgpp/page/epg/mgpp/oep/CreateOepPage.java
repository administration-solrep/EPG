package fr.dila.solonmgpp.page.epg.mgpp.oep;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import fr.dila.solonmgpp.page.commun.CommonWebPage;

public class CreateOepPage extends CommonWebPage {
    
    
    public static final String ID_COMMUN_INPUT = "create_fpoep:nxl_fiche_presentation_OEP_general:nxw_fiche_OEP_idCommun";
    public static final String NOM_ORGANISME_INPUT = "create_fpoep:nxl_fiche_presentation_OEP_general:nxw_fiche_OEP_nomOrganisme";
    public static final String MINISTERER_ATTACHEMENT_INPUT = "create_fpoep:nxl_fiche_presentation_OEP_general:nxw_fiche_OEP_ministereRattachement";
    

    public void setIdCommun(final String idCommun) {
        final WebElement elem = getDriver().findElement(By.id(ID_COMMUN_INPUT));
        fillField("Identifiant commun", elem, idCommun);
    }
    
    public void setNomOrganisme(final String nomOrganisme) {
        final WebElement elem = getDriver().findElement(By.id(NOM_ORGANISME_INPUT));
        fillField("Nom organisme extraparlementaire", elem, nomOrganisme);
    }
    
    
    public void setMinistereRattachement1(final String ministereRattachement) {
        final WebElement elem = getDriver().findElement(By.id(MINISTERER_ATTACHEMENT_INPUT));
        fillField("Ministère de rattachement", elem, ministereRattachement);
    }
    
    
    public void setMinistereRattachement2(final String ministereRattachement) {
        final WebElement elem = getDriver().findElement(By.id("create_fpoep:nxl_fiche_presentation_OEP_general:nxw_fiche_OEP_ministereRattachement2"));
        fillField("Ministère de rattachement bis", elem, ministereRattachement);
    }
    
    
    public void setMinistereRattachement3(final String ministereRattachement) {
        final WebElement elem = getDriver().findElement(By.id("create_fpoep:nxl_fiche_presentation_OEP_general:nxw_fiche_OEP_ministereRattachement3"));
        fillField("Ministère de rattachement ter", elem, ministereRattachement);
    }
    
    public void setTexteRef(final String texteRef) {
        final WebElement elem = getDriver().findElement(By.id("create_fpoep:nxl_fiche_presentation_OEP_general:nxw_fiche_OEP_texteRef"));
        fillField("Texte de référence", elem, texteRef);
    }
    
    public void setTexteDuree(final String texteDuree) {
        final WebElement elem = getDriver().findElement(By.id("create_fpoep:nxl_fiche_presentation_OEP_general:nxw_fiche_OEP_texteDuree"));
        fillField("Texte durée", elem, texteDuree);
    }
    
    public void setCommentaire(final String commentaire) {
        final WebElement elem = getDriver().findElement(By.id("create_fpoep:nxl_fiche_presentation_OEP_general:nxw_fiche_OEP_commentaire"));
        fillField("Commentaire", elem, commentaire);
    }
    
    public void setObservation(final String observation) {
        final WebElement elem = getDriver().findElement(By.id("create_fpoep:nxl_fiche_presentation_OEP_general:nxw_fiche_OEP_observation"));
        fillField("Observations", elem, observation);
    }
    
    public void setNbDeputes(final String nbDeputes) {
        final WebElement elem = getDriver().findElement(By.id("create_fpoep:nxl_fiche_presentation_OEP_general:nxw_fiche_OEP_nbDeputes"));
        fillField("Nombre de députés", elem, nbDeputes);
    }
    
    public void setNbSenateurs(final String nbSenateurs) {
        final WebElement elem = getDriver().findElement(By.id("create_fpoep:nxl_fiche_presentation_OEP_general:nxw_fiche_OEP_nbSenateurs"));
        fillField("Nombre de sénateurs", elem, nbSenateurs);
    }
      
    public void setAdresse(final String adresse) {
        final WebElement elem = getDriver().findElement(By.id("create_fpoep:nxl_fiche_presentation_OEP_coordonnees:nxw_fiche_OEP_adresse"));
        fillField("Adresse", elem, adresse);
    }
    
    public void setTelephone(final String telephone) {
        final WebElement elem = getDriver().findElement(By.id("create_fpoep:nxl_fiche_presentation_OEP_coordonnees:nxw_fiche_OEP_tel"));
        fillField("Téléphone", elem, telephone);
    }
    
    public void setFax(final String fax) {
        final WebElement elem = getDriver().findElement(By.id("create_fpoep:nxl_fiche_presentation_OEP_coordonnees:nxw_fiche_OEP_fax"));
        fillField("Fax", elem, fax);
    }
    
    public void setMail(final String mail) {
        final WebElement elem = getDriver().findElement(By.id("create_fpoep:nxl_fiche_presentation_OEP_coordonnees:nxw_fiche_OEP_mail"));
        fillField("Mél de contact", elem, mail);
    }
    
    public void setDureeMandatAN(final String dureeMandatAN) {
        final WebElement elem = getDriver().findElement(By.id("create_fpoep:nxl_fiche_presentation_OEP_mandatAN:nxw_fiche_OEP_dureeMandatAN"));
        fillField("Sénat", elem, dureeMandatAN);
    }
    
    public void setDureeMandatSE(final String dureeMandatSE) {
        final WebElement elem = getDriver().findElement(By.id("create_fpoep:nxl_fiche_presentation_OEP_mandatSE:nxw_fiche_OEP_dureeMandatSE"));
        fillField("Assemblée nationale", elem, dureeMandatSE);
    }
}
