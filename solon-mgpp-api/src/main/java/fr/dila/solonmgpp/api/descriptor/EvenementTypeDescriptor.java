package fr.dila.solonmgpp.api.descriptor;

import java.util.Map;

/**
 * Interface des types d'evenements
 *
 * @author asatre
 *
 */
public interface EvenementTypeDescriptor {
    String getName();

    void setName(String name);

    String getLabel();

    void setLabel(String label);

    boolean isCreateur();

    void setCreateur(boolean createur);

    boolean isSuccessif();

    void setSuccessif(boolean successif);

    boolean isDemandeAr();

    void setDemandeAr(boolean demandeAr);

    boolean isCreerBrouillon();

    void setCreerBrouillon(boolean creerBrouillon);

    boolean isCompleter();

    void setCompleter(boolean completer);

    boolean isRectifier();

    void setRectifier(boolean rectifier);

    boolean isAnnuler();

    void setAnnuler(boolean annuler);

    DistributionDescriptor getDistribution();

    void setDistribution(DistributionDescriptor distribution);

    Map<String, PieceJointeDescriptor> getPieceJointe();

    void setPieceJointe(Map<String, PieceJointeDescriptor> pieceJointe);

    void setProcedure(String procedure);

    String getProcedure();
}
