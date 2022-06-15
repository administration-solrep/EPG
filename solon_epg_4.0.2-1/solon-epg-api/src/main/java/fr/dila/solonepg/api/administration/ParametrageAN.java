package fr.dila.solonepg.api.administration;

import fr.dila.solonepg.api.activitenormative.ParamAnTypeEnum;
import fr.dila.st.api.domain.STDomainObject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Interface du document du paramétrage des statistiques de l'activité normative.
 * Présente les paramètre pour les législatures courante et précédente.
 *
 */
public interface ParametrageAN extends STDomainObject {
    /* #############################################################
	 * 						GESTION DES LEGISLATURES
	 ###############################################################*/

    String getLegislatureEnCours();

    void setLegislatureEnCours(String legislatureEnCours);

    List<String> getLegislatures();

    void setLegislatures(List<String> legislatures);

    void setLegislaturePublication(String legislaturePublication);

    String getLegislaturePublication();

    void setLegislaturePrecPublication(String legislaturePrecPublication);

    String getLegislaturePrecPublication();

    /* #############################################################
	 * 						LEGISLATURES
	 * 				DATE DEBUT EN COURS ET PRECEDENTE
	 ###############################################################*/

    Date getLegislatureEnCoursDateDebut();

    void setLegislatureEnCoursDateDebut(Date legislatureEnCoursDateDebut);

    Date getLegislaturePrecedenteDateDebut();

    void setLegislaturePrecedenteDateDebut(Date legislaturePrecedenteDateDebut);

    /* ##############################################################################################################################################
	 * 															APPLICATION DES LOIS
	 ################################################################################################################################################*/

    /* #############################################################
	 * 						APPLICATION DES LOIS
	 * 				BILANS SEMESTRIELS LEGISLATURE COURANTE
	 ###############################################################*/

    /**
     * Getter date promulgation lois borne inférieure legislature en cours pour les bilans semestriels
     * @return
     */
    Date getLECBSDatePromulBorneInf();

    /**
     * setter date promulgation lois borne inférieure législature en cours pour les bilans semestriels
     * @param borneInf
     */
    void setLECBSDatePromulBorneInf(Date borneInf);

    /**
     * Getter date promulgation lois borne supérieure legislature en cours pour les bilans semestriels
     * @return
     */
    Date getLECBSDatePromulBorneSup();

    /**
     * Setter date promulgation lois borne supérieure legislature en cours pour les bilans semestriels
     * @param borneSup
     */
    void setLECBSDatePromulBorneSup(Date borneSup);

    /**
     * Getter date publication décrets d'application des lois borne inférieure legislature en cours pour les bilans semestriels
     * @return
     */
    Date getLECBSDatePubliBorneInf();

    /**
     * Setter date publication décrets d'application des lois borne inférieure legislature en cours pour les bilans semestriels
     * @param borneInf
     */
    void setLECBSDatePubliBorneInf(Date borneInf);

    /**
     * Getter date publication décrets d'application des lois borne supérieure legislature en cours pour les bilans semestriels
     * @return
     */
    Date getLECBSDatePubliBorneSup();

    /**
     * Setter date publication décrets d'application des lois borne Supérieure legislature en cours pour les bilans semestriels
     * @param borneSup
     */
    void setLECBSDatePubliBorneSup(Date borneSup);

    /* #############################################################
	 * 						APPLICATION DES LOIS
	 * 				TAUX D'EXECUTION SESSION PARLEMENTAIRE
	 * 						LEGISLATURE COURANTE
	 ###############################################################*/

    /**
     * Getter date promulgation lois borne inférieure legislature en cours pour les taux d'exécution (session parlementaire)
     * @return
     */
    Date getLECTauxSPDatePromulBorneInf();

    /**
     * Setter date promulgation lois borne inférieure legislature en cours pour les taux d'exécution (session parlementaire)
     * @param borneInf
     */
    void setLECTauxSPDatePromulBorneInf(Date borneInf);

    /**
     * Getter date promulgation lois borne supérieure legislature en cours pour les taux d'exécution (session parlementaire)
     * @return
     */
    Date getLECTauxSPDatePromulBorneSup();

    /**
     * Setter date promulgation lois borne supérieure legislature en cours pour les taux d'exécution (session parlementaire)
     * @param borneSup
     */
    void setLECTauxSPDatePromulBorneSup(Date borneSup);

    /**
     * Getter date publication décrets d'application des lois borne inférieure legislature en cours pour les taux d'exécution (session parlementaire)
     * @return
     */
    Date getLECTauxSPDatePubliBorneInf();

    /**
     * Setter date publication décrets d'application des lois borne inférieure legislature en cours pour les taux d'exécution (session parlementaire)
     * @param borneInf
     */
    void setLECTauxSPDatePubliBorneInf(Date borneInf);

    /**
     * Getter date publication décrets d'application des lois borne supérieure legislature en cours pour les taux d'exécution (session parlementaire)
     * @return
     */
    Date getLECTauxSPDatePubliBorneSup();

    /**
     * Setter date publication décrets d'application des lois borne supérieure legislature en cours pour les taux d'exécution (session parlementaire)
     * @param borneSup
     */
    void setLECTauxSPDatePubliBorneSup(Date borneSup);

    /* #############################################################
	 * 						APPLICATION DES LOIS
	 * 			TAUX D'EXECUTION DEPUIS DEBUT LEGISLATURE
	 * 						LEGISLATURE COURANTE
	 ###############################################################*/

    /**
     * Getter date promulgation lois borne inférieure legislature en cours pour les taux d'exécution (depuis début legislature)
     * @return
     */
    Date getLECTauxDebutLegisDatePromulBorneInf();

    /**
     * Setter date promulgation lois borne inférieure legislature en cours pour les taux d'exécution (depuis début legislature)
     * @param borneInf
     */
    void setLECTauxDebutLegisDatePromulBorneInf(Date borneInf);

    /**
     * Getter date promulgation lois borne supérieure legislature en cours pour les taux d'exécution (depuis début legislature)
     * @return
     */
    Date getLECTauxDebutLegisDatePromulBorneSup();

    /**
     * Setter date promulgation lois borne supérieure legislature en cours pour les taux d'exécution (depuis début legislature)
     * @param borneSup
     */
    void setLECTauxDebutLegisDatePromulBorneSup(Date borneSup);

    /**
     * Getter date publication décret d'application lois borne inférieure legislature en cours pour les taux d'exécution (depuis début legislature)
     * @return
     */
    Date getLECTauxDebutLegisDatePubliBorneInf();

    /**
     * Setter date publication décret d'application lois borne inférieure legislature en cours pour les taux d'exécution (depuis début legislature)
     * @param borneInf
     */
    void setLECTauxDebutLegisDatePubliBorneInf(Date borneInf);

    /**
     * Getter date publication décret d'application lois borne supérieure legislature en cours pour les taux d'exécution (depuis début legislature)
     * @return
     */
    Date getLECTauxDebutLegisDatePubliBorneSup();

    /**
     * Setter date publication décret d'application lois borne supérieure legislature en cours pour les taux d'exécution (depuis début legislature)
     * @param borneSup
     */
    void setLECTauxDebutLegisDatePubliBorneSup(Date borneSup);

    /* #############################################################
	 * 						APPLICATION DES LOIS
	 * 				BILANS SEMESTRIELS LEGISLATURE PRECEDENTE
	 ###############################################################*/

    /**
     * Getter date promulgation lois borne inférieure legislature précédente pour les bilans semestriels
     * @return
     */
    Date getLPBSDatePromulBorneInf();

    /**
     * Setter date promulgation lois borne inférieure legislature précédente pour les bilans semestriels
     * @param borneInf
     */
    void setLPBSDatePromulBorneInf(Date borneInf);

    /**
     * Getter date promulgation lois borne supéreieure legislature précédente pour les bilans semestriels
     * @return
     */
    Date getLPBSDatePromulBorneSup();

    /**
     * Setter date promulgation lois borne supérieure legislature précédente pour les bilans semestriels
     * @param borneSup
     */
    void setLPBSDatePromulBorneSup(Date borneSup);

    /**
     * Getter date publication décret application des lois borne inférieure legislature précédente pour les bilans semestriels
     * @return
     */
    Date getLPBSDatePubliBorneInf();

    /**
     * Setter date publication décret application des lois borne inférieure legislature précédente pour les bilans semestriels
     * @param borneInf
     */
    void setLPBSDatePubliBorneInf(Date borneInf);

    /**
     * Getter date publication décret application des lois borne supérieure legislature précédente pour les bilans semestriels
     * @return
     */
    Date getLPBSDatePubliBorneSup();

    /**
     * Setter date publication décret application des lois borne supérieure legislature précédente pour les bilans semestriels
     * @param borneSup
     */
    void setLPBSDatePubliBorneSup(Date borneSup);

    /* #############################################################
	 * 						APPLICATION DES LOIS
	 * 				TAUX D'EXECUTION SESSION PARLEMENTAIRE
	 * 						LEGISLATURE PRECEDENTE
	 ###############################################################*/

    /**
     * Getter date promulgation lois borne inférieure legislature précédente pour les taux d'exécution (session parlementaire)
     * @return
     */
    Date getLPTauxSPDatePromulBorneInf();

    /**
     * Setter date promulgation lois borne inférieure legislature précédente pour les taux d'exécution (session parlementaire)
     * @param borneInf
     */
    void setLPTauxSPDatePromulBorneInf(Date borneInf);

    /**
     * Getter date promulgation lois borne supérieure legislature précédente pour les taux d'exécution (session parlementaire)
     * @return
     */
    Date getLPTauxSPDatePromulBorneSup();

    /**
     * Setter date promulgation lois borne supérieure legislature précédente pour les taux d'exécution (session parlementaire)
     * @param borneSup
     */
    void setLPTauxSPDatePromulBorneSup(Date borneSup);

    /**
     * Getter date publication décret application des lois borne inférieure legislature précédente pour les taux d'exécution (session parlementaire)
     * @return
     */
    Date getLPTauxSPDatePubliBorneInf();

    /**
     * Setter date publication décret application des lois borne inférieure legislature précédente pour les taux d'exécution (session parlementaire)
     * @param borneInf
     */
    void setLPTauxSPDatePubliBorneInf(Date borneInf);

    /**
     * Getter date publication décret application des lois borne supérieure legislature précédente pour les taux d'exécution (session parlementaire)
     * @return
     */
    Date getLPTauxSPDatePubliBorneSup();

    /**
     * Setter date publication décret application des lois borne supérieure legislature précédente pour les taux d'exécution (session parlementaire)
     * @param borneSup
     */
    void setLPTauxSPDatePubliBorneSup(Date borneSup);

    /* #############################################################
	 * 						APPLICATION DES LOIS
	 * 			TAUX D'EXECUTION DEPUIS DEBUT LEGISLATURE
	 * 						LEGISLATURE PRECEDENTE
	 ###############################################################*/

    /**
     * Getter date promulgation lois borne inférieure legislature précédente pour les taux d'exécution (depuis début législature)
     * @return
     */
    Date getLPTauxDebutLegisDatePromulBorneInf();

    /**
     * Setter date promulgation lois borne inférieure legislature précédente pour les taux d'exécution (depuis début législature)
     * @param borneInf
     */
    void setLPTauxDebutLegisDatePromulBorneInf(Date borneInf);

    /**
     * Getter date promulgation lois borne supérieure legislature précédente pour les taux d'exécution (depuis début législature)
     * @return
     */
    Date getLPTauxDebutLegisDatePromulBorneSup();

    /**
     * Setter date promulgation lois borne supérieure legislature précédente pour les taux d'exécution (depuis début législature)
     * @param borneSup
     */
    void setLPTauxDebutLegisDatePromulBorneSup(Date borneSup);

    /**
     * Getter date publication décret application des lois borne inférieure legislature précédente pour les taux d'exécution (depuis début législature)
     * @return
     */
    Date getLPTauxDebutLegisDatePubliBorneInf();

    /**
     * Setter date publication décret application des lois borne inférieure legislature précédente pour les taux d'exécution (depuis début législature)
     * @param borneInf
     */
    void setLPTauxDebutLegisDatePubliBorneInf(Date borneInf);

    /**
     * Getter date publication décret application des lois borne supérieure legislature précédente pour les taux d'exécution (depuis début législature)
     * @return
     */
    Date getLPTauxDebutLegisDatePubliBorneSup();

    /**
     * Setter date publication décret application des lois borne supérieure legislature précédente pour les taux d'exécution (depuis début législature)
     * @param borneSup
     */
    void setLPTauxDebutLegisDatePubliBorneSup(Date borneSup);

    /* ##############################################################################################################################################
	 * 															APPLICATION DES ORDONNANCES
	 ################################################################################################################################################*/

    /* #############################################################
	 * 						APPLICATION DES ORDONNANCES
	 * 				BILANS SEMESTRIELS LEGISLATURE EN COURS
	 ###############################################################*/

    /**
     * Getter date publication ordonnance borne inférieure legislature en cours pour les bilans semestriels
     * @return
     */
    Date getLECBSDatePubliOrdoBorneInf();

    /**
     * Setter date publication ordonnance borne inférieure legislature en cours pour les bilans semestriels
     * @param borneInf
     */
    void setLECBSDatePubliOrdoBorneInf(Date borneInf);

    /**
     * Getter date publication ordonnance borne supérieure legislature en cours pour les bilans semestriels
     * @return
     */
    Date getLECBSDatePubliOrdoBorneSup();

    /**
     * Setter date publication ordonnance borne supérieure legislature en cours pour les bilans semestriels
     * @param borneSup
     */
    void setLECBSDatePubliOrdoBorneSup(Date borneSup);

    /**
     * Getter date publication décret application des ordonnances borne inférieure legislature en cours pour les bilans semestriels
     * @return
     */
    Date getLECBSDatePubliDecretOrdoBorneInf();

    /**
     * Setter date publication décret application des ordonnances borne inférieure legislature en cours pour les bilans semestriels
     * @param borneInf
     */
    void setLECBSDatePubliDecretOrdoBorneInf(Date borneInf);

    /**
     * Getter date publication décret application des ordonnances borne supérieure legislature en cours pour les bilans semestriels
     * @return
     */
    Date getLECBSDatePubliDecretOrdoBorneSup();

    /**
     * Setter date publication décret application des ordonnances borne supérieure legislature en cours pour les bilans semestriels
     * @param borneSup
     */
    void setLECBSDatePubliDecretOrdoBorneSup(Date borneSup);

    /* #############################################################
	 * 						APPLICATION DES ORDONNANCES
	 * 				TAUX D'EXECUTION SESSION PARLEMENTAIRE
	 * 						LEGISLATURE COURANTE
	 ###############################################################*/

    /**
     * Getter date publication ordonnance borne inférieure legislature en cours pour les taux d'exécution (session parlementaire)
     * @return
     */
    Date getLECTauxSPDatePubliOrdoBorneInf();

    /**
     * Setter date publication ordonnance borne inférieure legislature en cours pour les taux d'exécution (session parlementaire)
     * @param borneInf
     */
    void setLECTauxSPDatePubliOrdoBorneInf(Date borneInf);

    /**
     * Getter date publication ordonnance borne supérieure legislature en cours pour les taux d'exécution (session parlementaire)
     * @return
     */
    Date getLECTauxSPDatePubliOrdoBorneSup();

    /**
     * Setter date publication ordonnance borne supérieure legislature en cours pour les taux d'exécution (session parlementaire)
     * @param borneSup
     */
    void setLECTauxSPDatePubliOrdoBorneSup(Date borneSup);

    /**
     * Getter date publication décrets d'application des ordonnances borne inférieure legislature en cours pour les taux d'exécution (session parlementaire)
     * @return
     */
    Date getLECTauxSPDatePubliDecretOrdoBorneInf();

    /**
     * Setter date publication décrets d'application des ordonnances borne inférieure legislature en cours pour les taux d'exécution (session parlementaire)
     * @param borneInf
     */
    void setLECTauxSPDatePubliDecretOrdoBorneInf(Date borneInf);

    /**
     * Getter date publication décrets d'application des ordonnances borne supérieure legislature en cours pour les taux d'exécution (session parlementaire)
     * @return
     */
    Date getLECTauxSPDatePubliDecretOrdoBorneSup();

    /**
     * Setter date publication décrets d'application des ordonnances borne supérieure legislature en cours pour les taux d'exécution (session parlementaire)
     * @param borneSup
     */
    void setLECTauxSPDatePubliDecretOrdoBorneSup(Date borneSup);

    /* #############################################################
	 * 						APPLICATION DES ORDONNANCES
	 * 			TAUX D'EXECUTION DEPUIS DEBUT LEGISLATURE
	 * 						LEGISLATURE COURANTE
	 ###############################################################*/

    /**
     * Getter date publication ordonnance borne inférieure legislature en cours pour les taux d'exécution (depuis début legislature)
     * @return
     */
    Date getLECTauxDebutLegisDatePubliOrdoBorneInf();

    /**
     * Setter date publication ordonnance borne inférieure legislature en cours pour les taux d'exécution (depuis début legislature)
     * @param borneInf
     */
    void setLECTauxDebutLegisDatePubliOrdoBorneInf(Date borneInf);

    /**
     * Getter date publication ordonnance borne supérieure legislature en cours pour les taux d'exécution (depuis début legislature)
     * @return
     */
    Date getLECTauxDebutLegisDatePubliOrdoBorneSup();

    /**
     * Setter date publication ordonnance borne supérieure legislature en cours pour les taux d'exécution (depuis début legislature)
     * @param borneSup
     */
    void setLECTauxDebutLegisDatePubliOrdoBorneSup(Date borneSup);

    /**
     * Getter date publication décret d'application des ordonnances borne inférieure legislature en cours pour les taux d'exécution (depuis début legislature)
     * @return
     */
    Date getLECTauxDebutLegisDatePubliDecretOrdoBorneInf();

    /**
     * Setter date publication décret d'application des ordonnances borne inférieure legislature en cours pour les taux d'exécution (depuis début legislature)
     * @param borneInf
     */
    void setLECTauxDebutLegisDatePubliDecretOrdoBorneInf(Date borneInf);

    /**
     * Getter date publication décret d'application des ordonnances borne supérieure legislature en cours pour les taux d'exécution (depuis début legislature)
     * @return
     */
    Date getLECTauxDebutLegisDatePubliDecretOrdoBorneSup();

    /**
     * Setter date publication décret d'application des ordonnances borne supérieure legislature en cours pour les taux d'exécution (depuis début legislature)
     * @param borneSup
     */
    void setLECTauxDebutLegisDatePubliDecretOrdoBorneSup(Date borneSup);

    /* #############################################################
	 * 						APPLICATION DES ORDONNANCES
	 * 				BILANS SEMESTRIELS LEGISLATURE PRECEDENTE
	 ###############################################################*/

    /**
     * Getter date publication ordonnance borne inférieure legislature précédente pour les bilans semestriels
     * @return
     */
    Date getLPBSDatePubliOrdoBorneInf();

    /**
     * Setter date publication ordonnance borne inférieure legislature précédente pour les bilans semestriels
     * @param borneInf
     */
    void setLPBSDatePubliOrdoBorneInf(Date borneInf);

    /**
     * Getter date publication ordonnance borne supérieure legislature précédente pour les bilans semestriels
     * @return
     */
    Date getLPBSDatePubliOrdoBorneSup();

    /**
     * Setter date publication ordonnance borne supérieure legislature précédente pour les bilans semestriels
     * @param borneSup
     */
    void setLPBSDatePubliOrdoBorneSup(Date borneSup);

    /**
     * Getter date publication décret application des ordonnances borne inférieure legislature précédente pour les bilans semestriels
     * @return
     */
    Date getLPBSDatePubliDecretOrdoBorneInf();

    /**
     * Setter date publication décret application des ordonnances borne inférieure legislature précédente pour les bilans semestriels
     * @param borneInf
     */
    void setLPBSDatePubliDecretOrdoBorneInf(Date borneInf);

    /**
     * Getter date publication décret application des ordonnances borne supérieure legislature précédente pour les bilans semestriels
     * @return
     */
    Date getLPBSDatePubliDecretOrdoBorneSup();

    /**
     * Setter date publication décret application des ordonnances borne supérieure legislature précédente pour les bilans semestriels
     * @param borneSup
     */
    void setLPBSDatePubliDecretOrdoBorneSup(Date borneSup);

    /* #############################################################
	 * 						APPLICATION DES ORDONNANCES
	 * 				TAUX D'EXECUTION SESSION PARLEMENTAIRE
	 * 						LEGISLATURE PRECEDENTE
	 ###############################################################*/

    /**
     * Getter date publication ordonnance borne inférieure legislature précédente pour les taux d'exécution (session parlementaire)
     * @return
     */
    Date getLPTauxSPDatePubliOrdoBorneInf();

    /**
     * Setter date publication ordonnance borne inférieure legislature précédente pour les taux d'exécution (session parlementaire)
     * @param borneInf
     */
    void setLPTauxSPDatePubliOrdoBorneInf(Date borneInf);

    /**
     * Getter date publication ordonnance borne supérieure legislature précédente pour les taux d'exécution (session parlementaire)
     * @return
     */
    Date getLPTauxSPDatePubliOrdoBorneSup();

    /**
     * Setter date publication ordonnance borne supérieure legislature précédente pour les taux d'exécution (session parlementaire)
     * @param borneSup
     */
    void setLPTauxSPDatePubliOrdoBorneSup(Date borneSup);

    /**
     * Getter date publication décret application des ordonnances borne inférieure legislature précédente pour les taux d'exécution (session parlementaire)
     * @return
     */
    Date getLPTauxSPDatePubliDecretOrdoBorneInf();

    /**
     * Setter date publication décret application des ordonnances borne inférieure legislature précédente pour les taux d'exécution (session parlementaire)
     * @param borneInf
     */
    void setLPTauxSPDatePubliDecretOrdoBorneInf(Date borneInf);

    /**
     * Getter date publication décret application des ordonnances borne supérieure legislature précédente pour les taux d'exécution (session parlementaire)
     * @return
     */
    Date getLPTauxSPDatePubliDecretOrdoBorneSup();

    /**
     * Setter date publication décret application des ordonnances borne supérieure legislature précédente pour les taux d'exécution (session parlementaire)
     * @param borneSup
     */
    void setLPTauxSPDatePubliDecretOrdoBorneSup(Date borneSup);

    /* #############################################################
	 * 						APPLICATION DES LOIS
	 * 			TAUX D'EXECUTION DEPUIS DEBUT LEGISLATURE
	 * 						LEGISLATURE PRECEDENTE
	 ###############################################################*/

    /**
     * Getter date publication ordonnance borne inférieure legislature précédente pour les taux d'exécution (depuis début législature)
     * @return
     */
    Date getLPTauxDebutLegisDatePubliOrdoBorneInf();

    /**
     * Setter date publication ordonnance borne inférieure legislature précédente pour les taux d'exécution (depuis début législature)
     * @param borneInf
     */
    void setLPTauxDebutLegisDatePubliOrdoBorneInf(Date borneInf);

    /**
     * Getter date publication ordonnance borne supérieure legislature précédente pour les taux d'exécution (depuis début législature)
     * @return
     */
    Date getLPTauxDebutLegisDatePubliOrdoBorneSup();

    /**
     * Setter date publication ordonnance borne supérieure legislature précédente pour les taux d'exécution (depuis début législature)
     * @param borneSup
     */
    void setLPTauxDebutLegisDatePubliOrdonnanceBorneSup(Date borneSup);

    /**
     * Getter date publication décret application des ordonnances borne inférieure legislature précédente pour les taux d'exécution (depuis début législature)
     * @return
     */
    Date getLPTauxDebutLegisDatePubliDecretOrdoBorneInf();

    /**
     * Setter date publication décret application des ordonnances borne inférieure legislature précédente pour les taux d'exécution (depuis début législature)
     * @param borneInf
     */
    void setLPTauxDebutLegisDatePubliDecretOrdoBorneInf(Date borneInf);

    /**
     * Getter date publication décret application des ordonnances borne supérieure legislature précédente pour les taux d'exécution (depuis début législature)
     * @return
     */
    Date getLPTauxDebutLegisDatePubliDecretOrdoBorneSup();

    /**
     * Setter date publication décret application des ordonnances borne supérieure legislature précédente pour les taux d'exécution (depuis début législature)
     * @param borneSup
     */
    void setLPTauxDebutLegisDatePubliDecretOrdoBorneSup(Date borneSup);

    /**
     * Affectation des entrées de date dans la map à transférer à Birt, avec les
     * bons paramètres en fonction du ParamAnTypeEnum et de l'information
     * législature courante ou précédente.
     */
    void assignParameters(ParamAnTypeEnum paramAnTypeEnum, Map<String, Serializable> inputValues, boolean curLegis);
}
