package fr.dila.solonmgpp.core.descriptor;

import fr.dila.solonmgpp.api.descriptor.DistributionDescriptor;
import fr.dila.solonmgpp.api.descriptor.EvenementTypeDescriptor;
import fr.dila.solonmgpp.api.descriptor.PieceJointeDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeMap;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * Implementation de {@link EvenementTypeDescriptorImpl}
 *
 * @author asatre
 */
@XObject("evenementType")
public class EvenementTypeDescriptorImpl implements EvenementTypeDescriptor, Serializable {
    private static final long serialVersionUID = 6958760930928546120L;

    /**
     * Nom du type d'événement (ex. "EVT01").
     */
    @XNode("@name")
    private String name;

    /**
     * Label du type d'événement (ex. "1 : ...").
     */
    @XNode("@label")
    private String label;

    /**
     * Événement de type créateur : crée un dossier.
     */
    @XNode("@createur")
    private boolean createur;

    /**
     * Événement de type successif : succède un autre événement dans un dossier.
     */
    @XNode("@successif")
    private boolean successif;

    /**
     * Si vrai, chaque version de ce type d'événement nécessite un accusé de réception.
     */
    @XNode("@demandeAr")
    private boolean demandeAr;

    /**
     * Autorise la création d'une version à l'état brouillon (pour initialisation) de ce type d'événement. Valeur par défaut : vrai.
     */
    @XNode("@creerBrouillon")
    private boolean creerBrouillon;

    /**
     * Autorise la création d'une version complétée de ce type d'événement. Valeur par défaut : vrai.
     */
    @XNode("@completer")
    private boolean completer;

    /**
     * Autorise la création d'une version rectifiée de ce type d'événement. Valeur par défaut : vrai.
     */
    @XNode("@rectifier")
    private boolean rectifier;

    /**
     * Autorise l'annulation de ce type d'événement. Valeur par défaut : vrai.
     */
    @XNode("@annuler")
    private boolean annuler;

    /**
     * Paramètres de distribution du type d'événement.
     */
    @XNode(value = "distribution")
    private DistributionDescriptorImpl distribution;

    /**
     * Pièces jointes triés par ordre
     */
    private Map<String, PieceJointeDescriptor> orderedPieceJointe;

    /**
     * Identifiant de la procédure.
     */
    @XNode("@procedure")
    private String procedure;

    /**
     * Pièces jointes.
     */
    @XNodeMap(
        value = "pieceJointe",
        key = "@type",
        type = HashMap.class,
        componentType = PieceJointeDescriptorImpl.class
    )
    private Map<String, PieceJointeDescriptor> pieceJointe;

    /**
     * Constructeur par défaut de EvenementTypeDescriptor.
     */
    public EvenementTypeDescriptorImpl() {
        creerBrouillon = true;
        completer = true;
        rectifier = true;
        annuler = true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean isCreateur() {
        return createur;
    }

    @Override
    public void setCreateur(boolean createur) {
        this.createur = createur;
    }

    @Override
    public boolean isSuccessif() {
        return successif;
    }

    @Override
    public void setSuccessif(boolean successif) {
        this.successif = successif;
    }

    @Override
    public boolean isDemandeAr() {
        return demandeAr;
    }

    @Override
    public void setDemandeAr(boolean demandeAr) {
        this.demandeAr = demandeAr;
    }

    @Override
    public boolean isCreerBrouillon() {
        return creerBrouillon;
    }

    @Override
    public void setCreerBrouillon(boolean creerBrouillon) {
        this.creerBrouillon = creerBrouillon;
    }

    @Override
    public boolean isCompleter() {
        return completer;
    }

    @Override
    public void setCompleter(boolean completer) {
        this.completer = completer;
    }

    @Override
    public boolean isRectifier() {
        return rectifier;
    }

    @Override
    public void setRectifier(boolean rectifier) {
        this.rectifier = rectifier;
    }

    @Override
    public boolean isAnnuler() {
        return annuler;
    }

    @Override
    public void setAnnuler(boolean annuler) {
        this.annuler = annuler;
    }

    @Override
    public DistributionDescriptor getDistribution() {
        return distribution;
    }

    @Override
    public void setDistribution(DistributionDescriptor distribution) {
        this.distribution = (DistributionDescriptorImpl) distribution;
    }

    @Override
    public Map<String, PieceJointeDescriptor> getPieceJointe() {
        if (orderedPieceJointe == null) {
            getOrderedPieceJointe();
        }
        return orderedPieceJointe;
    }

    @Override
    public void setPieceJointe(Map<String, PieceJointeDescriptor> pieceJointe) {
        this.pieceJointe = pieceJointe;
    }

    private void getOrderedPieceJointe() {
        orderedPieceJointe = new LinkedHashMap<String, PieceJointeDescriptor>();

        if (pieceJointe != null) {
            List<PieceJointeDescriptor> list = new ArrayList<PieceJointeDescriptor>(pieceJointe.values());

            Collections.sort(
                list,
                new Comparator<PieceJointeDescriptor>() {

                    @Override
                    public int compare(PieceJointeDescriptor pj1, PieceJointeDescriptor pj2) {
                        if (pj1.getOrder() != null && pj2.getOrder() != null) {
                            return pj1.getOrder().compareTo(pj2.getOrder());
                        }
                        return 0;
                    }
                }
            );

            for (PieceJointeDescriptor descriptor : list) {
                orderedPieceJointe.put(descriptor.getType(), descriptor);
            }
        }
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getProcedure() {
        return procedure;
    }
}
