package fr.dila.solonmgpp.core.descriptor;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

import fr.dila.solonmgpp.api.descriptor.PieceJointeDescriptor;

/**
 * Descripteur des pièces jointes.
 * 
 * @author asatre
 */
@XObject("pieceJointe")
public class PieceJointeDescriptorImpl implements PieceJointeDescriptor {

    /**
     * Type de pièce jointe : TEXTE, TRAITE, AUTRE...
     */
    @XNode("@type")
    private String type;

    /**
     * Type de pièce jointe obligatoire.
     */
    @XNode("@obligatoire")
    private boolean obligatoire;

    /**
     * Pièce jointe multivaluée.
     */
    @XNode("@multivalue")
    private boolean multivalue;

    /**
     * Ordre d'affichage
     */
    @XNode("@order")
    private Integer order;

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean isObligatoire() {
        return obligatoire;
    }

    @Override
    public void setObligatoire(boolean obligatoire) {
        this.obligatoire = obligatoire;
    }

    @Override
    public boolean isMultivalue() {
        return multivalue;
    }

    @Override
    public void setMultivalue(boolean multivalue) {
        this.multivalue = multivalue;
    }

    @Override
    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public Integer getOrder() {
        return order;
    }
}
