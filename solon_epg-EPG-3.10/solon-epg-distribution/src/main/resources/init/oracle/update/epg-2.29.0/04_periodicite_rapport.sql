 CREATE TABLE "VOC_PERIODICITE_RAPPORT" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
   
Insert into VOC_PERIODICITE_RAPPORT ("id","obsolete","ordering","label") values ('RAPPORT_UNIQUE',0,10000000,'Rapport unique');
Insert into VOC_PERIODICITE_RAPPORT ("id","obsolete","ordering","label") values ('RAPPORT_RELATIF_ARTICLE_67_LOI_N_2004-1343',0,10000000,'Rapport d’application de la loi');
Insert into VOC_PERIODICITE_RAPPORT ("id","obsolete","ordering","label") values ('RAPPORT_PERIODIQUE',0,10000000,'Rapport périodique ');

commit;