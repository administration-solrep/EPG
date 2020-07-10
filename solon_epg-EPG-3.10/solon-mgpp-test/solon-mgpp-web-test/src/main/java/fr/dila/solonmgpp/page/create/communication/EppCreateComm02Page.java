package fr.dila.solonmgpp.page.create.communication;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import fr.dila.solonmgpp.page.detail.communication.AbstractDetailComm;
import fr.dila.solonmgpp.page.detail.communication.MgppDetailComm02Page;
import fr.sword.xsd.solon.epp.PieceJointeType;
import fr.sword.xsd.solon.epp.TypeLoi;

public class EppCreateComm02Page extends AbstractEppCreateComm {

    public static final String TYPE_COMM = "LEX-02 : PPL - Information du Gouvernement du dépôt";
    
    public static final String TYPE_LOI_SELECT = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_typeLoi";
    public static final String NUM_DEPOT_TXT_INPUT = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_numeroDepotTexte";
    
    public static final String DATE_DEPOT_INPUT = "evenement_metadonnees:nxl_metadonnees_version:nxw_metadonnees_version_dateDepotTexteInputDate";
    
    public static final String URL = "http://PJPMVANPourAVIAN.solon-epg.fr";
    
    public static final String PJ = "src/main/attachments/piece-jointe.doc";
    
    
    public MgppDetailComm02Page createCommEVT02(String idDossier,String objet, TypeLoi typeLoi, String dateDepot, String numeroDepotTexte, String auteur) {
        checkValue(COMMUNICATION, TYPE_COMM);
        setIdentifiantDossier(idDossier);
        setObjet(objet);
        setTypeLoi(typeLoi);
        setDateDepotTexte(dateDepot);
        setNumeroDepotTexte(numeroDepotTexte);
        addAuteur(auteur);
        addPieceJointe(PieceJointeType.TEXTE, URL, PJ);
      
        return publier();
        
    }

    public void setDateDepotTexte(String date) {
        final WebElement elem = getDriver().findElement(By.id(DATE_DEPOT_INPUT));
        fillField("Date dépôt texte", elem, date);
    }
    
    public void setTypeLoi(final TypeLoi loi) {
        final WebElement elem = getDriver().findElement(By.id(TYPE_LOI_SELECT));
        getFlog().action("Selectionne \"" + loi + "\" dans le select \"Type loi\"");
        final Select select = new Select(elem);
        select.selectByValue(loi.name());
    }
    
    public void setNumeroDepotTexte(String numero) {
        final WebElement elem = getDriver().findElement(By.id(NUM_DEPOT_TXT_INPUT));
        fillField("Numero dépôt texte", elem, numero);
    }
        

    
    @Override
    protected Class<? extends AbstractDetailComm> getCreateResultPageClass() {
        return MgppDetailComm02Page.class;
    }


}
