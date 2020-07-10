  CREATE TABLE "VOC_NATURE_FDR" 
   (	
    "id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;

COMMIT;

delete from VOC_NATURE_FDR;
insert into VOC_NATURE_FDR ("id","obsolete","ordering","label") values ('RAPPORT',0,10000000,'Rapport');
insert into VOC_NATURE_FDR ("id","obsolete","ordering","label") values ('LOI',0,10000000,'Loi');
insert into VOC_NATURE_FDR ("id","obsolete","ordering","label") values ('LOI_ORGANIQUE',0,10000000,'Loi organique');
insert into VOC_NATURE_FDR ("id","obsolete","ordering","label") values ('DECRET',0,10000000,'Décret');
insert into VOC_NATURE_FDR ("id","obsolete","ordering","label") values ('AUTRES',0,10000000,'Autres');
insert into VOC_NATURE_FDR ("id","obsolete","ordering","label") values ('A_DETERMINER',0,10000000,'A déterminer');
insert into VOC_NATURE_FDR ("id","obsolete","ordering","label") values ('SANS_OBJET',0,10000000,'Sans objet');
COMMIT;