CREATE TABLE "TBREF_TYPEACTE" 
   (	"ID" VARCHAR2(36 BYTE) NOT NULL, 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
   CREATE INDEX "TBREF_TYPEACTE_ID_IDX" ON "TBREF_TYPEACTE" ("ID") ;
   
   INSERT INTO VOC_CM_ROUTING_TASK_TYPE ("id","label","obsolete") VALUES (16,'label.epg.feuilleRoute.etape.pour.publicationBO',0);
   
   --------------------------------------------------------
--  DDL for Table VECTEUR_PUBLICATION
--------------------------------------------------------
   
   CREATE TABLE "VECTEUR_PUBLICATION" 
   (	"ID" VARCHAR2(36 BYTE), 
	"VPINTITULE" NVARCHAR2(2000), 
	"VPDEBUT"  TIMESTAMP (6), 
	"VPFIN"  TIMESTAMP (6),
	"VPPOS" NUMBER(10,0)
   ) ;
   --------------------------------------------------------
--  Ref Constraints for Table VECTEUR_PUBLICATION
--------------------------------------------------------

  ALTER TABLE "VECTEUR_PUBLICATION" ADD CONSTRAINT "VECTEUR_PUBLICATION_PK" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "VECTEUR_PUBLICATION" ADD CONSTRAINT "VECTPUB_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
  
   commit;
   
   --------------------------------------------------------
--  DDL for Table MODE_PARUTION
--------------------------------------------------------
   
   CREATE TABLE "MODE_PARUTION" 
   (	"ID" VARCHAR2(36 BYTE), 
	"MODE" NVARCHAR2(2000), 
	"DATEDEBUT"  TIMESTAMP (6), 
	"DATEFIN"  TIMESTAMP (6)
   ) ;
   --------------------------------------------------------
--  Ref Constraints for Table MODE_PARUTION
--------------------------------------------------------

  ALTER TABLE "MODE_PARUTION" ADD CONSTRAINT "MODE_PARUTION_PK" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "MODE_PARUTION" ADD CONSTRAINT "MODE_PARUTION_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
	  
DROP table "VOC_VECTEUR_PUBLICATION_TP";
DROP table "VOC_MODE_PARUTION";

commit;
