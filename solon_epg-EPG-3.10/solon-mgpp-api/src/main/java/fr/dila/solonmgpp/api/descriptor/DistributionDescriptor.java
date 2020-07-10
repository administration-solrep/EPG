package fr.dila.solonmgpp.api.descriptor;


/**
 * Interface du descripteur de la distribution d'un type d'événement.
 * 
 * @author asatre
 */
public interface DistributionDescriptor {


     DistributionElementDescriptor getEmetteur() ;

     void setEmetteur(DistributionElementDescriptor emetteur) ;

     DistributionElementDescriptor getDestinataire() ;

     void setDestinataire(DistributionElementDescriptor destinataire) ;

     DistributionElementDescriptor getCopie() ;

     void setCopie(DistributionElementDescriptor copie) ;
}
