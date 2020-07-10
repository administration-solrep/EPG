DROP TABLE "VOC_COURRIER";

CREATE TABLE "VOC_COURRIER_DECRETS_PRESIDENT" 
(	"id" NVARCHAR2(2000), 
"obsolete" NUMBER(19,0) DEFAULT 0, 
"ordering" NUMBER(19,0) DEFAULT 10000000, 
"label" NVARCHAR2(2000)
) ;
Insert into VOC_COURRIER_DECRETS_PRESIDENT ("id","obsolete","ordering","label") values ('1',0,10000000,'ouverture session extraordinaire-décret');
Insert into VOC_COURRIER_DECRETS_PRESIDENT ("id","obsolete","ordering","label") values ('2',0,10000000,'clôture session extraordianaire-décret');
Insert into VOC_COURRIER_DECRETS_PRESIDENT ("id","obsolete","ordering","label") values ('3',0,10000000,'ouverture session extraordinaire-ampliation');
Insert into VOC_COURRIER_DECRETS_PRESIDENT ("id","obsolete","ordering","label") values ('4',0,10000000,'clôture session extraordinaire-ampliation');
Insert into VOC_COURRIER_DECRETS_PRESIDENT ("id","obsolete","ordering","label") values ('5',0,10000000,'ouverture session extraordinaire-lettre');
Insert into VOC_COURRIER_DECRETS_PRESIDENT ("id","obsolete","ordering","label") values ('6',0,10000000,'clôture session extraordinaire-lettre');
Insert into VOC_COURRIER_DECRETS_PRESIDENT ("id","obsolete","ordering","label") values ('7',0,10000000,'Lettres + décrets');
Insert into VOC_COURRIER_DECRETS_PRESIDENT ("id","obsolete","ordering","label") values ('8',0,10000000,'Décret congrès article 18C');
Insert into VOC_COURRIER_DECRETS_PRESIDENT ("id","obsolete","ordering","label") values ('9',0,10000000,'Lettres + décrets congrès article 18C');
Insert into VOC_COURRIER_DECRETS_PRESIDENT ("id","obsolete","ordering","label") values ('10',0,10000000,'Décret congrès');
Insert into VOC_COURRIER_DECRETS_PRESIDENT ("id","obsolete","ordering","label") values ('11',0,10000000,'Ampliation congrès');


CREATE TABLE "VOC_COURRIER_LOI" 
(	"id" NVARCHAR2(2000), 
"obsolete" NUMBER(19,0) DEFAULT 0, 
"ordering" NUMBER(19,0) DEFAULT 10000000, 
"label" NVARCHAR2(2000)
) ;
Insert into VOC_COURRIER_LOI ("id","obsolete","ordering","label") values ('0',0,10000000,'LEX-14 - Procedure accélérée');
Insert into VOC_COURRIER_LOI ("id","obsolete","ordering","label") values ('1',0,10000000,'LEX-14 - Procedure accélérée lettre rectificative');
Insert into VOC_COURRIER_LOI ("id","obsolete","ordering","label") values ('2',0,10000000,'LEX-17 - Première lecture');
Insert into VOC_COURRIER_LOI ("id","obsolete","ordering","label") values ('3',0,10000000,'LEX-17 - Première lecture modifiée');
Insert into VOC_COURRIER_LOI ("id","obsolete","ordering","label") values ('4',0,10000000,'LEX-17 - Première lecture rejetée');
Insert into VOC_COURRIER_LOI ("id","obsolete","ordering","label") values ('5',0,10000000,'LEX-17 - Première lecture procédure accélerée');
Insert into VOC_COURRIER_LOI ("id","obsolete","ordering","label") values ('6',0,10000000,'LEX-17 - Première lecture procédure accélerée modifiée');
Insert into VOC_COURRIER_LOI ("id","obsolete","ordering","label") values ('7',0,10000000,'LEX-17 - Première lecture procédure accélerée rejetée');
Insert into VOC_COURRIER_LOI ("id","obsolete","ordering","label") values ('8',0,10000000,'LEX-17 - Deuxième lecture');
Insert into VOC_COURRIER_LOI ("id","obsolete","ordering","label") values ('9',0,10000000,'LEX-17 - Deuxième lecture modifiée');
Insert into VOC_COURRIER_LOI ("id","obsolete","ordering","label") values ('10',0,10000000,'LEX-17 - Nouvelle lecture');
Insert into VOC_COURRIER_LOI ("id","obsolete","ordering","label") values ('11',0,10000000,'LEX-22 - CMP (45-2)');
Insert into VOC_COURRIER_LOI ("id","obsolete","ordering","label") values ('12',0,10000000,'LEX-22 - CMP (45-2) texte commun');
Insert into VOC_COURRIER_LOI ("id","obsolete","ordering","label") values ('13',0,10000000,'LEX-26 - CMP (45-3)');
Insert into VOC_COURRIER_LOI ("id","obsolete","ordering","label") values ('14',0,10000000,'LEX-27 - Echec CMP');
Insert into VOC_COURRIER_LOI ("id","obsolete","ordering","label") values ('15',0,10000000,'LEX-27 - Echec CMP texte commun');
Insert into VOC_COURRIER_LOI ("id","obsolete","ordering","label") values ('16',0,10000000,'LEX-27 - Rejet par l''Assemblée nationale');
Insert into VOC_COURRIER_LOI ("id","obsolete","ordering","label") values ('17',0,10000000,'LEX-27 - Rejet par le Sénat');
Insert into VOC_COURRIER_LOI ("id","obsolete","ordering","label") values ('18',0,10000000,'LEX-27 - Rejet par le Sénat des amendements du Gouvernement');
Insert into VOC_COURRIER_LOI ("id","obsolete","ordering","label") values ('19',0,10000000,'LEX-26 - Dernière lecture');
Insert into VOC_COURRIER_LOI ("id","obsolete","ordering","label") values ('20',0,10000000,'LEX-26 - Dernière lecture rejet');


CREATE TABLE "VOC_COURRIER_OEP" 
(	"id" NVARCHAR2(2000), 
"obsolete" NUMBER(19,0) DEFAULT 0, 
"ordering" NUMBER(19,0) DEFAULT 10000000, 
"label" NVARCHAR2(2000)
) ;
Insert into VOC_COURRIER_OEP ("id","obsolete","ordering","label") values ('1',0,10000000,'Lettre choix multiples - décès');
Insert into VOC_COURRIER_OEP ("id","obsolete","ordering","label") values ('2',0,10000000,'Lettre choix multiples - démission');
Insert into VOC_COURRIER_OEP ("id","obsolete","ordering","label") values ('3',0,10000000,'Lettre choix multiples - expiration');
Insert into VOC_COURRIER_OEP ("id","obsolete","ordering","label") values ('4',0,10000000,'Lettre création OEP');
Insert into VOC_COURRIER_OEP ("id","obsolete","ordering","label") values ('5',0,10000000,'Lettres re-création OEP');
Insert into VOC_COURRIER_OEP ("id","obsolete","ordering","label") values ('6',0,10000000,'Lettres suite entrée au Gouvernement');
Insert into VOC_COURRIER_OEP ("id","obsolete","ordering","label") values ('7',0,10000000,'Lettres suite nomination CCI');


CREATE TABLE "VOC_COURRIER_DEPOT_RAPPORT" 
(	"id" NVARCHAR2(2000), 
"obsolete" NUMBER(19,0) DEFAULT 0, 
"ordering" NUMBER(19,0) DEFAULT 10000000, 
"label" NVARCHAR2(2000)
) ;
Insert into VOC_COURRIER_DEPOT_RAPPORT ("id","obsolete","ordering","label") values ('1',0,10000000,'dépôt rapport art 67');
Insert into VOC_COURRIER_DEPOT_RAPPORT ("id","obsolete","ordering","label") values ('2',0,10000000,'dépôt rapport');
Insert into VOC_COURRIER_DEPOT_RAPPORT ("id","obsolete","ordering","label") values ('3',0,10000000,'dépôt rapport pour information');
Insert into VOC_COURRIER_DEPOT_RAPPORT ("id","obsolete","ordering","label") values ('4',0,10000000,'dépôt rapport- info ministre');


CREATE TABLE "VOC_COURRIER_INTERVENTION_EXT" 
(	"id" NVARCHAR2(2000), 
"obsolete" NUMBER(19,0) DEFAULT 0, 
"ordering" NUMBER(19,0) DEFAULT 10000000, 
"label" NVARCHAR2(2000)
) ;
Insert into VOC_COURRIER_INTERVENTION_EXT ("id","obsolete","ordering","label") values ('1',0,10000000,'Lettre intervention extérieure');
Insert into VOC_COURRIER_INTERVENTION_EXT ("id","obsolete","ordering","label") values ('2',0,10000000,'Lettre intervention ext en session extra');


CREATE TABLE "VOC_COURRIER_DPG" 
(	"id" NVARCHAR2(2000), 
"obsolete" NUMBER(19,0) DEFAULT 0, 
"ordering" NUMBER(19,0) DEFAULT 10000000, 
"label" NVARCHAR2(2000)
) ;
Insert into VOC_COURRIER_DPG ("id","obsolete","ordering","label") values ('1',0,10000000,'Délcaration de politique générale');
Insert into VOC_COURRIER_DPG ("id","obsolete","ordering","label") values ('2',0,10000000,'Déclaration de politique générale approbation');

CREATE TABLE "VOC_COURRIER_RESOLUTION_341" 
(	"id" NVARCHAR2(2000), 
"obsolete" NUMBER(19,0) DEFAULT 0, 
"ordering" NUMBER(19,0) DEFAULT 10000000, 
"label" NVARCHAR2(2000)
) ;
Insert into VOC_COURRIER_RESOLUTION_341 ("id","obsolete","ordering","label") values ('1',0,10000000,'34-1C : injonction');
Insert into VOC_COURRIER_RESOLUTION_341 ("id","obsolete","ordering","label") values ('2',0,10000000,'34-1C : mise en cause de la responsabilité');

CREATE TABLE "VOC_COURRIER_SD" 
(	"id" NVARCHAR2(2000), 
"obsolete" NUMBER(19,0) DEFAULT 0, 
"ordering" NUMBER(19,0) DEFAULT 10000000, 
"label" NVARCHAR2(2000)
) ;
Insert into VOC_COURRIER_SD ("id","obsolete","ordering","label") values ('1',0,10000000,'Déclaration en application de l''article 50-1 de la Constitution');


CREATE TABLE "VOC_COURRIER_JSS" 
(	"id" NVARCHAR2(2000), 
"obsolete" NUMBER(19,0) DEFAULT 0, 
"ordering" NUMBER(19,0) DEFAULT 10000000, 
"label" NVARCHAR2(2000)
) ;
Insert into VOC_COURRIER_JSS ("id","obsolete","ordering","label") values ('1',0,10000000,'Correspondance en application du troisième alinéa de l''article 28 de la Constitution');


CREATE TABLE "VOC_COURRIER_AUDIT" 
(	"id" NVARCHAR2(2000), 
"obsolete" NUMBER(19,0) DEFAULT 0, 
"ordering" NUMBER(19,0) DEFAULT 10000000, 
"label" NVARCHAR2(2000)
) ;
Insert into VOC_COURRIER_AUDIT ("id","obsolete","ordering","label") values ('1',0,10000000,'Audition code de la santé publique');
Insert into VOC_COURRIER_AUDIT ("id","obsolete","ordering","label") values ('2',0,10000000,'Audition autres');


CREATE TABLE "VOC_COURRIER_DOC" 
(	"id" NVARCHAR2(2000), 
"obsolete" NUMBER(19,0) DEFAULT 0, 
"ordering" NUMBER(19,0) DEFAULT 10000000, 
"label" NVARCHAR2(2000)
) ;
Insert into VOC_COURRIER_DOC ("id","obsolete","ordering","label") values ('1',0,10000000,'Autre transmission');
Insert into VOC_COURRIER_DOC ("id","obsolete","ordering","label") values ('2',0,10000000,'Transmission invest avenir');
Insert into VOC_COURRIER_DOC ("id","obsolete","ordering","label") values ('3',0,10000000,'Transmission COM');
Insert into VOC_COURRIER_DOC ("id","obsolete","ordering","label") values ('4',0,10000000,'Transmission contre-expertise invest publics');

commit;
