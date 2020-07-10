 CREATE TABLE "VOC_BASE_LEGALE_NATURE_TEXTE" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
   
insert into VOC_BASE_LEGALE_NATURE_TEXTE ("id","obsolete","ordering","label") values ('LOI',0,10000000,'Loi');
insert into VOC_BASE_LEGALE_NATURE_TEXTE ("id","obsolete","ordering","label") values ('LOI_ORGANIQUE',0,10000000,'Loi organique');
insert into VOC_BASE_LEGALE_NATURE_TEXTE ("id","obsolete","ordering","label") values ('DECRET',0,10000000,'Décret ');
insert into VOC_BASE_LEGALE_NATURE_TEXTE ("id","obsolete","ordering","label") values ('AUTRES',0,10000000,'Autres');

commit;