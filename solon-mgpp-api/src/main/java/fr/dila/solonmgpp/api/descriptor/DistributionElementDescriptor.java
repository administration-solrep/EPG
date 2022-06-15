package fr.dila.solonmgpp.api.descriptor;

import java.util.Map;

/**
 * Interface du descripteur des institutions autorisés en tant qu'émetteur / institution / copie de la distribution.
 *
 * @author asatre
 */
public interface DistributionElementDescriptor {
    Map<String, String> getInstitution();

    void setInstitution(Map<String, String> institution);

    boolean isObligatoire();

    void setObligatoire(boolean obligatoire);
}
