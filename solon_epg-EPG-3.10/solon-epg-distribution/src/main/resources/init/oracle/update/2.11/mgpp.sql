ALTER TABLE "REPRESENTANT_AVI" ADD "IDFPAVI" VARCHAR2(36);

alter table "REPRESENTANT_AVI" DROP COLUMN IDFPAN;

CREATE UNIQUE INDEX "REP_AVI_FICHE" ON "REPRESENTANT_AVI" ("IDFPAVI" , "ID");

ALTER TABLE DOSSIER_SOLON_EPG ADD ( "ADOPTION" NUMBER(1,0) DEFAULT 0 NOT NULL);

CREATE UNIQUE INDEX "DOSSIER_SOLON_EPG_ADOPTION" ON "DOSSIER_SOLON_EPG" ("ADOPTION" , "ID");
   
COMMIT;
