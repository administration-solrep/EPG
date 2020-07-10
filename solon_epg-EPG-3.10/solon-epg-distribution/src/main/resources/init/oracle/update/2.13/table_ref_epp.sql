  CREATE TABLE "VOC_TYPE_MANDAT_EPP" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;

COMMIT;

delete from VOC_TYPE_MANDAT_EPP;
insert into VOC_TYPE_MANDAT_EPP ("id","obsolete","ordering","label") values ('HAUT_COMMISSAIRE',0,10000000,'Haut Commissaire');
insert into VOC_TYPE_MANDAT_EPP ("id","obsolete","ordering","label") values ('SECRETARIAT_ETAT',0,10000000,'Secrétaire d''état');
insert into VOC_TYPE_MANDAT_EPP ("id","obsolete","ordering","label") values ('PRESIDENCE_REPUBLIQUE',0,10000000,'Présidence de la République');
insert into VOC_TYPE_MANDAT_EPP ("id","obsolete","ordering","label") values ('MINISTERE',0,10000000,'Ministre');

COMMIT;