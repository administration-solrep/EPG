--------------------------------------------------------
--  DDL for Table FICHE_PRESENTATION_AN
--------------------------------------------------------

  CREATE TABLE "FICHE_PRESENTATION_AN" 
   (	"ID" VARCHAR2(36 BYTE), 
	"DATEAUDITIONSE" TIMESTAMP (6), 
	"NOMINE" NVARCHAR2(2000), 
	"DATEAUDITIONAN" TIMESTAMP (6), 
	"IDORGANISMEEPP" NVARCHAR2(2000), 
	"DATEFINMANDAT" TIMESTAMP (6), 
	"NOMORGANISME" NVARCHAR2(2000), 
	"DATE" TIMESTAMP (6), 
	"IDDOSSIER" NVARCHAR2(2000), 
	"BASELEGALE" NVARCHAR2(2000)
   ) ;
 

--------------------------------------------------------
--  Constraints for Table FICHE_PRESENTATION_AN
--------------------------------------------------------

  ALTER TABLE "FICHE_PRESENTATION_AN" ADD CONSTRAINT "FICHE_PRESENTATION_AN_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "FICHE_PRESENTATION_AN" MODIFY ("ID" NOT NULL ENABLE);
 

--------------------------------------------------------
--  Ref Constraints for Table FICHE_PRESENTATION_AN
--------------------------------------------------------

  ALTER TABLE "FICHE_PRESENTATION_AN" ADD CONSTRAINT "FICH_713449CA_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
	  

--------------------------------------------------------
--  DDL for Table FICHE_PRESENTATION_DECRET
--------------------------------------------------------

  CREATE TABLE "FICHE_PRESENTATION_DECRET" 
   (	"ID" VARCHAR2(36 BYTE), 
	"NORPUBLICATION" NVARCHAR2(2000), 
	"URLPUBLICATION" NVARCHAR2(2000), 
	"NOR" NVARCHAR2(2000), 
	"NUMEROACTE" NVARCHAR2(2000), 
	"PAGEJO" NVARCHAR2(2000), 
	"RUBRIQUE" NVARCHAR2(2000), 
	"INTITULE" NVARCHAR2(2000), 
	"DATE" TIMESTAMP (6), 
	"OBJET" NVARCHAR2(2000), 
	"NUMJO" NVARCHAR2(2000), 
	"DATEJO" TIMESTAMP (6)
   ) ;
 

--------------------------------------------------------
--  Constraints for Table FICHE_PRESENTATION_DECRET
--------------------------------------------------------

  ALTER TABLE "FICHE_PRESENTATION_DECRET" ADD CONSTRAINT "FICHE_PRESENTATION_DECRET_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "FICHE_PRESENTATION_DECRET" MODIFY ("ID" NOT NULL ENABLE);
 

--------------------------------------------------------
--  Ref Constraints for Table FICHE_PRESENTATION_DECRET
--------------------------------------------------------

  ALTER TABLE "FICHE_PRESENTATION_DECRET" ADD CONSTRAINT "FICH_7685C8A0_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
 

 alter table NXP_LOGS_MAPEXTINFOS drop constraint FKF96F609C4EA9779;
  ALTER TABLE "NXP_LOGS_MAPEXTINFOS" ADD CONSTRAINT "FKF96F609C4EA9779" FOREIGN KEY ("INFO_FK")
	  REFERENCES "NXP_LOGS_EXTINFO" ("LOG_EXTINFO_ID") ENABLE;
 
alter table NXP_LOGS_MAPEXTINFOS drop constraint FKF96F609E7AC49AA;
  ALTER TABLE "NXP_LOGS_MAPEXTINFOS" ADD CONSTRAINT "FKF96F609E7AC49AA" FOREIGN KEY ("LOG_FK")
	  REFERENCES "NXP_LOGS" ("LOG_ID") ENABLE;
	  
	  
	  --------------------------------------------------------
--  DDL for Index FICHE_PRESENTATION_AN_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "FICHE_PRESENTATION_AN_PK" ON "FICHE_PRESENTATION_AN" ("ID") 
  ;
 
  --------------------------------------------------------
--  DDL for Index FICHE_PRESENTATION_DECRET_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "FICHE_PRESENTATION_DECRET_PK" ON "FICHE_PRESENTATION_DECRET" ("ID") 
  ;
 
	  