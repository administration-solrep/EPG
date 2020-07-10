ALTER TABLE "DOSSIER_SOLON_EPG" ADD ("TEXTEENTREPRISE"  NUMBER(1,0));


CREATE TABLE "DOS_DATEEFFET" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" TIMESTAMP(6)
   );


INSERT INTO "VOC_BORDEREAU_LABEL" ("id","obsolete","ordering","label") values ('texteEntreprise',0, 10000000, 'Textes relevant de la rubrique entreprise');
INSERT INTO "VOC_BORDEREAU_LABEL" ("id","obsolete","ordering","label") values ('dateEffet',0, 10000000,'Date(s) d''effet');
