-- Suppression et recréation de deux vocabulaires de recherche
-- Voc état recherche

  CREATE TABLE "VOC_ETAPE_ETAT_RECHERCHE" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;

COMMIT;

Insert into VOC_ETAPE_ETAT_RECHERCHE ("id","obsolete","ordering","label") values ('running',0,100000,'label.epg.feuilleRoute.etape.running');
Insert into VOC_ETAPE_ETAT_RECHERCHE ("id","obsolete","ordering","label") values ('ready',0,100000,'label.epg.feuilleRoute.etape.ready');
Insert into VOC_ETAPE_ETAT_RECHERCHE ("id","obsolete","ordering","label") values ('done',0,100000,'label.epg.feuilleRoute.etape.done');
Insert into VOC_ETAPE_ETAT_RECHERCHE ("id","obsolete","ordering","label") values ('validated',0,100000,'label.epg.feuilleRoute.etape.validated');

-- Voc statut recherche

  CREATE TABLE "VOC_STATUT_VALIDATION" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;

COMMIT;

Insert into VOC_STATUT_VALIDATION ("id","obsolete","ordering","label") values ('1',0,100000,'label.epg.feuilleRoute.etape.valide.manuellement');
Insert into VOC_STATUT_VALIDATION ("id","obsolete","ordering","label") values ('2',0,100000,'label.epg.feuilleRoute.etape.valide.refusValidation');
Insert into VOC_STATUT_VALIDATION ("id","obsolete","ordering","label") values ('3',0,100000,'label.epg.feuilleRoute.etape.valide.automatiquement');
Insert into VOC_STATUT_VALIDATION ("id","obsolete","ordering","label") values ('4',0,100000,'label.epg.feuilleRoute.etape.valide.nonConcerne');
Insert into VOC_STATUT_VALIDATION ("id","obsolete","ordering","label") values ('10',0,100000,'label.epg.feuilleRoute.etape.valide.avisFavorableCorrection');
Insert into VOC_STATUT_VALIDATION ("id","obsolete","ordering","label") values ('15',0,100000,'label.epg.feuilleRoute.etape.valide.retourModification');
