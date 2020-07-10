package fr.dila.solonmgpp.core.descriptor;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

import fr.dila.solonmgpp.api.descriptor.DistributionDescriptor;
import fr.dila.solonmgpp.api.descriptor.DistributionElementDescriptor;

/**
 * Implementation de {@link DistributionDescriptor}
 * 
 * @author asatre
 */
@XObject("distribution")
public class DistributionDescriptorImpl implements DistributionDescriptor{

    /**
     * Emetteurs possibles de ce type d'événement.
     */
    @XNode(value = "emetteur")
    private DistributionElementDescriptorImpl emetteur;

    /**
     * Destinataires possibles de ce type d'événement.
     */
    @XNode(value = "destinataire")
    private DistributionElementDescriptorImpl destinataire;

    /**
     * Copies possibles de ce type d'événement.
     */
    @XNode(value = "copie")
    private DistributionElementDescriptorImpl copie;

    @Override
    public DistributionElementDescriptor getEmetteur() {
        return emetteur;
    }

    @Override
    public void setEmetteur(DistributionElementDescriptor emetteur) {
        this.emetteur = (DistributionElementDescriptorImpl) emetteur;
    }

    @Override
    public DistributionElementDescriptor getDestinataire() {
        return destinataire;
    }

    @Override
    public void setDestinataire(DistributionElementDescriptor destinataire) {
        this.destinataire = (DistributionElementDescriptorImpl) destinataire;
    }

    @Override
    public DistributionElementDescriptor getCopie() {
        return copie;
    }

    @Override
    public void setCopie(DistributionElementDescriptor copie) {
        this.copie = (DistributionElementDescriptorImpl) copie;
    }
}
