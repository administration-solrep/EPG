package fr.dila.solonepg.api.mailbox;

import fr.dila.cm.mailbox.Mailbox;

/**
 * Interface des documents de type SOLON EPG.
 *
 * @author jtremeaux
 */
public interface SolonEpgMailbox extends Mailbox {
    /**
     * Retourne le nombre de documents contenus dans la mailbox à l'état "Pour rédaction".
     *
     * @return Nombre de documents
     */
    Integer getCountPourRedaction();

    /**
     * Renseigne le nombre de documents contenus dans la mailbox à l'état "Pour rédaction".
     *
     * @param count
     *            Nombre de documents
     */
    void setCountPourRedaction(Integer count);

    /**
     * Retourne le nombre de documents contenus dans la mailbox à l'état "Pour attribution".
     *
     * @return Nombre de documents
     */
    Integer getCountPourAttribution();

    /**
     * Renseigne le nombre de documents contenus dans la mailbox à l'état "Pour attribution".
     *
     * @param count
     *            Nombre de documents
     */
    void setCountPourAttribution(Integer count);

    /**
     * Retourne le nombre de documents contenus dans la mailbox à l'état "Pour signature".
     *
     * @return Nombre de documents
     */
    Integer getCountPourSignature();

    /**
     * Renseigne le nombre de documents contenus dans la mailbox à l'état "Pour signature".
     *
     * @param count
     *            Nombre de documents
     */
    void setCountPourSignature(Integer count);

    /**
     * Retourne le nombre de documents contenus dans la mailbox à l'état "Pour information".
     *
     * @return Nombre de documents
     */
    Integer getCountPourInformation();

    /**
     * Renseigne le nombre de documents contenus dans la mailbox à l'état "Pour information".
     *
     * @param count
     *            Nombre de documents
     */
    void setCountPourInformation(Integer count);

    /**
     * Retourne le nombre de documents contenus dans la mailbox à l'état "Pour avis".
     *
     * @return Nombre de documents
     */
    Integer getCountPourAvis();

    /**
     * Renseigne le nombre de documents contenus dans la mailbox à l'état "Pour avis".
     *
     * @param count
     *            Nombre de documents
     */
    void setCountPourAvis(Integer count);

    /**
     * Retourne le nombre de documents contenus dans la mailbox à l'état "Pour validation PM".
     *
     * @return Nombre de documents
     */
    Integer getCountPourValidationPM();

    /**
     * Renseigne le nombre de documents contenus dans la mailbox à l'état "Pour validation PM".
     *
     * @param count
     *            Nombre de documents
     */
    void setCountPourValidationPM(Integer count);

    /**
     * Retourne le nombre de documents contenus dans la mailbox à l'état "Pour réattribution".
     *
     * @return Nombre de documents
     */
    Integer getCountPourReattribution();

    /**
     * Renseigne le nombre de documents contenus dans la mailbox à l'état "Pour réattribution".
     *
     * @param count
     *            Nombre de documents
     */
    void setCountPourReattribution(Integer count);

    /**
     * Retourne le nombre de documents contenus dans la mailbox à l'état "Pour réorientation".
     *
     * @return Nombre de documents
     */
    Integer getCountPourReorientation();

    /**
     * Renseigne le nombre de documents contenus dans la mailbox à l'état "Pour réorientation".
     *
     * @param count
     *            Nombre de documents
     */
    void setCountPourReorientation(Integer count);

    /**
     * Retourne le nombre de documents contenus dans la mailbox à l'état "Pour impression".
     *
     * @return Nombre de documents
     */
    Integer getCountPourImpression();

    /**
     * Renseigne le nombre de documents contenus dans la mailbox à l'état "Pour impression".
     *
     * @param count
     *            Nombre de documents
     */
    void setCountPourImpression(Integer count);

    /**
     * Retourne le nombre de documents contenus dans la mailbox à l'état "Pour transmission aux assemblées".
     *
     * @return Nombre de documents
     */
    Integer getCountPourTransmissionAuxAssemblees();

    /**
     * Renseigne le nombre de documents contenus dans la mailbox à l'état "Pour transmission aux assemblées".
     *
     * @param count
     *            Nombre de documents
     */
    void setCountPourTransmissionAuxAssemblees(Integer count);

    /**
     * Retourne le nombre de documents contenus dans la mailbox à l'état "Pour arbitrage".
     *
     * @return Nombre de documents
     */
    Integer getCountPourArbitrage();

    /**
     * Renseigne le nombre de documents contenus dans la mailbox à l'état "Pour arbitrage".
     *
     * @param count
     *            Nombre de documents
     */
    void setCountPourArbitrage(Integer count);

    /**
     * Retourne le nombre de documents contenus dans la mailbox à l'état "Pour rédaction interfacée".
     *
     * @return Nombre de documents
     */
    Integer getCountPourRedactionInterfacee();

    /**
     * Renseigne le nombre de documents contenus dans la mailbox à l'état "Pour rédaction interfacée".
     *
     * @param count
     *            Nombre de documents
     */
    void setCountPourRedactionInterfacee(Integer count);
}
