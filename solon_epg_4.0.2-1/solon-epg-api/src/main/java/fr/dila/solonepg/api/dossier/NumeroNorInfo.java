package fr.dila.solonepg.api.dossier;

public interface NumeroNorInfo {
    /**
     * @return le numéro du NOR
     */
    String getNumeroNor();

    /**
     * @return les lettres du ministères
     */
    String getMinistere();

    /**
     * @return la lettre du direction
     */
    String getDirection();

    /**
     * @return L'année
     */
    int getAnnee();

    /**
     * @return le compteur
     */
    int getCompteur();

    /**
     * @return la lettre de l'Acte
     */
    String getActe();

    /**
     * Cette fonction change le compteur dans le numéro du NOR
     *
     * @param compteur
     * @return nouveau NumeroNorInfo
     */
    NumeroNorInfo reglerCompteur(int compteur);
}
