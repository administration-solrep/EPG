ALTER TABLE "ACTIVITE_NORMATIVE_PARAMETRAGE"
ADD (
		"EC_BS_PUBLI_ORDO_DEBUT" TIMESTAMP (6),
		"EC_BS_PUBLI_ORDO_FIN" TIMESTAMP (6),
		"EC_BS_PUBLI_DECRETS_ORDO_DEBUT" TIMESTAMP (6),
		"EC_BS_PUBLI_DECRETS_ORDO_FIN" TIMESTAMP (6),
        "PRE_BS_PUBLI_ORDO_DEBUT" TIMESTAMP (6),
        "PRE_BS_PUBLI_ORDO_FIN" TIMESTAMP (6),
		"PRE_BS_PUBLI_DECRETS_ORD_DEBUT" TIMESTAMP (6),
		"PRE_BS_PUBLI_DECRETS_ORDO_FIN" TIMESTAMP (6),
		"EC_TP_PUBLI_ORDOS_DEB" TIMESTAMP (6),
		"EC_TP_PUBLI_ORDOS_FIN" TIMESTAMP (6), 
    	"EC_TP_PUBLI_DECRETS_ORDOS_DEB" TIMESTAMP (6),
    	"EC_TP_PUBLI_DECRETS_ORDOS_FIN" TIMESTAMP (6),
		"EC_TL_PUBLI_ORDOS_DEB" TIMESTAMP (6),
		"EC_TL_PUBLI_ORDOS_FIN" TIMESTAMP (6),
		"EC_TL_PUBLI_DECRETS_ORDOS_DEB" TIMESTAMP (6),
		"EC_TL_PUBLI_DECRETS_ORDOS_FIN" TIMESTAMP (6),		
		"PRE_TP_PUBLI_ORDOS_DEB" TIMESTAMP (6),
		"PRE_TP_PUBLI_ORDOS_FIN" TIMESTAMP (6),
		"PRE_TP_PUBLI_DECRETS_ORDOS_DEB" TIMESTAMP (6),
		"PRE_TP_PUBLI_DECRETS_ORDOS_FIN" TIMESTAMP (6),
		"PRE_TL_PUBLI_ORDOS_DEB" TIMESTAMP (6),
		"PRE_TL_PUBLI_ORDOS_FIN" TIMESTAMP (6),
		"PRE_TL_PUBLI_DECRETS_ORDOS_DEB" TIMESTAMP (6),
		"PRE_TL_PUBLI_DECRETS_ORDOS_FIN" TIMESTAMP (6)
);


update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "EC_BS_PUBLI_ORDO_DEBUT" = "EC_BS_PROMUL_LOIS_DEBUT";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "EC_BS_PUBLI_ORDO_FIN" = "EC_BS_PROMUL_LOIS_FIN";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "EC_BS_PUBLI_DECRETS_ORDO_DEBUT" = "EC_BS_PUBLI_DECRETS_DEBUT";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "EC_BS_PUBLI_DECRETS_ORDO_FIN" = "EC_BS_PUBLI_DECRETS_FIN";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "PRE_BS_PUBLI_ORDO_DEBUT" = "PRE_BS_PROMUL_LOIS_DEBUT";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "PRE_BS_PUBLI_ORDO_FIN" = "PRE_BS_PROMUL_LOIS_FIN";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "PRE_BS_PUBLI_DECRETS_ORD_DEBUT" = "PRE_BS_PUBLI_DECRETS_DEBUT";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "PRE_BS_PUBLI_DECRETS_ORDO_FIN" = "PRE_BS_PUBLI_DECRETS_FIN";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "EC_TP_PUBLI_ORDOS_DEB" = "EC_TP_PROMUL_LOIS_DEBUT";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "EC_TP_PUBLI_ORDOS_FIN" = "EC_TP_PROMUL_LOIS_FIN";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "EC_TP_PUBLI_DECRETS_ORDOS_DEB" = "EC_TP_PUBLI_DECRETS_DEBUT";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "EC_TP_PUBLI_DECRETS_ORDOS_FIN" = "EC_TP_PUBLI_DECRETS_FIN";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "EC_TL_PUBLI_ORDOS_DEB" = "EC_TL_PROMUL_LOIS_DEBUT";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "EC_TL_PUBLI_ORDOS_FIN" = "EC_TL_PROMUL_LOIS_FIN";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "EC_TL_PUBLI_DECRETS_ORDOS_DEB" = "EC_TL_PUBLI_DECRETS_DEBUT";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "EC_TL_PUBLI_DECRETS_ORDOS_FIN" = "EC_TL_PUBLI_DECRETS_FIN";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "PRE_TP_PUBLI_ORDOS_DEB" = "PRE_TP_PROMUL_LOIS_DEBUT";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "PRE_TP_PUBLI_ORDOS_FIN" = "PRE_TP_PROMUL_LOIS_FIN";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "PRE_TP_PUBLI_DECRETS_ORDOS_DEB" = "PRE_TP_PUBLI_DECRETS_DEBUT";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "PRE_TP_PUBLI_DECRETS_ORDOS_FIN" = "PRE_TP_PUBLI_DECRETS_FIN";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "PRE_TL_PUBLI_ORDOS_DEB" = "PRE_TL_PROMUL_LOIS_DEBUT";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "PRE_TL_PUBLI_ORDOS_FIN" = "PRE_TL_PROMUL_LOIS_FIN";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "PRE_TL_PUBLI_DECRETS_ORDOS_DEB" = "PRE_TL_PUBLI_DECRETS_DEBUT";
update "ACTIVITE_NORMATIVE_PARAMETRAGE" set "PRE_TL_PUBLI_DECRETS_ORDOS_FIN" = "PRE_TL_PUBLI_DECRETS_FIN";