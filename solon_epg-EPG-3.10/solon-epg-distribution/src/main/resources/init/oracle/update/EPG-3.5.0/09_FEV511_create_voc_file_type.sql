CREATE TABLE "VOC_FILE_TYPE" 
   (	
    "id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   );


insert into VOC_FILE_TYPE ("id","obsolete","ordering","label") values ('1',0,10000000,'Acte');
insert into VOC_FILE_TYPE ("id","obsolete","ordering","label") values ('2',0,10000000,'Extrait');
insert into VOC_FILE_TYPE ("id","obsolete","ordering","label") values ('3',0,10000000,'Autres pièces du parapheur');
insert into VOC_FILE_TYPE ("id","obsolete","ordering","label") values ('4',0,10000000,'Fond de dossier');
