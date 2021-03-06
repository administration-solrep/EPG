ALTER TABLE FICHE_PRESENTATION_OEP 
ADD
(
    "INFORMERCHARGEMISSION" NUMBER(1,0),
    "NATUREJURIDIQUEORGANISME" NVARCHAR2(2000),
    "COMMISSIONDUDECRET2006" NUMBER(1,0),
    "ISSUIVIEDQD" NUMBER(1,0),
    "ISSUIVIEAPPSUIVIMANDATS" NUMBER(1,0),
    "MOTIFFINOEP" NVARCHAR2(2000),
    "RENOUVELABLEAN" NUMBER(1,0),
    "NBMANDATSAN" NUMBER(19,0),
    "RENOUVELABLESE" NUMBER(1,0),
    "NBMANDATSSE" NUMBER(19,0)
);

ALTER TABLE REPRESENTANT_OEP
ADD
(
    "NUMEROMANDAT" NUMBER(19,0),
    "AUTORITEDESIGNATION" NVARCHAR2(2000),
    "COMMISSIONSAISIE" NVARCHAR2(2000)
);

CREATE TABLE "FPOEP_CHARGEMISSION" 
(	
    "ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
);
