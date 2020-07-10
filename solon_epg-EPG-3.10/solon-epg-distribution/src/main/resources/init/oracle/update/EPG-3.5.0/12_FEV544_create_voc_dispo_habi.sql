CREATE TABLE "VOC_DISPOSITION_HABILITATION" 
   (	
    "id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;

insert into VOC_DISPOSITION_HABILITATION ("id","obsolete","ordering","label") values ('0',0,10000000,'Article 74-1');
insert into VOC_DISPOSITION_HABILITATION ("id","obsolete","ordering","label") values ('1',0,10000000,'Article 38 C');