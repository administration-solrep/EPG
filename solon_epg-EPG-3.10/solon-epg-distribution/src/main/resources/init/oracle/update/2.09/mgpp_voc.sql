
DROP TABLE "VOC_MOTIF_IRRECEVABILITE";
  CREATE TABLE "VOC_MOTIF_IRRECEVABILITE" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;

COMMIT;


Insert into VOC_MOTIF_IRRECEVABILITE ("id","obsolete","ordering","label") values ('INJONCTION',0,10000000,'Injonction');
Insert into VOC_MOTIF_IRRECEVABILITE ("id","obsolete","ordering","label") values ('MISE_EN_CAUSE_RESPONSABILITE_GOUVERNEMENT',0,10000000,'Mise en cause de la responsabilité du Gouvernement');
Insert into VOC_MOTIF_IRRECEVABILITE ("id","obsolete","ordering","label") values ('REPETITION',0,10000000,'Répétition');
Insert into VOC_MOTIF_IRRECEVABILITE ("id","obsolete","ordering","label") values ('AUTRES',0,10000000,'"Autres');

commit;

