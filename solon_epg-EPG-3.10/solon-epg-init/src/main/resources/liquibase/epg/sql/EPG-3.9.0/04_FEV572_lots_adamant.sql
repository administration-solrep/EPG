CREATE TABLE "DOSSIER_EXTRACTION_LOT" (
	ID NUMBER(*,0) NOT NULL ENABLE, 
	NAME VARCHAR2(36),
	CREATION_DATE TIMESTAMP,
	STATUS VARCHAR2(25),
	CONSTRAINT PK_DOSSIER_EXTRACTION_LOT PRIMARY KEY (ID)
);

ALTER TABLE "DOSSIER_SOLON_EPG" ADD ("ID_EXTRACTION_LOT" NUMBER(*,0));

ALTER TABLE "DOSSIER_SOLON_EPG" ADD CONSTRAINT "DOSSIER_ID_EXTRACTION_LOT_FK" FOREIGN KEY ("ID_EXTRACTION_LOT") 
REFERENCES "DOSSIER_EXTRACTION_LOT" ("ID");

CREATE TABLE "DOSSIER_EXTRACTION_BORDEREAU" (
	ID_DOSSIER VARCHAR2(36) NOT NULL ENABLE,
	ID_EXTRACTION_LOT NUMBER(*,0),
	EXTRACTION_DATE TIMESTAMP,
	MESSAGE VARCHAR2(3500),
	CONSTRAINT PK_DOS_EXTRACTION_BORDEREAU PRIMARY KEY (ID_DOSSIER, ID_EXTRACTION_LOT)
);