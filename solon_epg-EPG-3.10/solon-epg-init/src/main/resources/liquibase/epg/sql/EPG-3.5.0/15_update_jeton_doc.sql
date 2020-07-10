CREATE TABLE "JTD_IDS_COMPLEMENTAIRES" 
   (	"ID" VARCHAR2(36 BYTE) NOT NULL ENABLE, 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   );
   
delete from "JETON_DOC" where "TYPE_WEBSERVICE"='WsEpgChercherModificationDossier';
commit;
alter table "JETON_DOC" drop column "ID_COMPLEMENTAIRE";
