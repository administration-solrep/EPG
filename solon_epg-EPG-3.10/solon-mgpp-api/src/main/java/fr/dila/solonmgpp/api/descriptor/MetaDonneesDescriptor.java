package fr.dila.solonmgpp.api.descriptor;

/**
 * Interface de description des métadonnées.
 * 
 * @author asatre
 */
public interface MetaDonneesDescriptor {

	String getName();

	void setName(String name);

	void setEvenement(EvenementMetaDonneesDescriptor evenement);

	EvenementMetaDonneesDescriptor getEvenement();

	void setVersion(VersionMetaDonneesDescriptor version);

	VersionMetaDonneesDescriptor getVersion();
}
