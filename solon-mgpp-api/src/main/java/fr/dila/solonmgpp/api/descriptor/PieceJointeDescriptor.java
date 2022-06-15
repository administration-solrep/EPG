package fr.dila.solonmgpp.api.descriptor;

/**
 * Interface du descripteur des pièces jointes.
 *
 * @author asatre
 */
public interface PieceJointeDescriptor {
    String getType();

    void setType(String type);

    boolean isObligatoire();

    void setObligatoire(boolean obligatoire);

    boolean isMultivalue();

    void setMultivalue(boolean multivalue);

    void setOrder(Integer order);

    Integer getOrder();
}
