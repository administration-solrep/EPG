-- Ajout de la table pour le schema birt_refresh_fichier  
  CREATE TABLE "BIRT_REFRESH_FICHIER" 
   (	"ID" VARCHAR2(36 BYTE), 
		"FILENAME" NVARCHAR2(2000),
		"OWNER" NVARCHAR2(2000),
		"DATEREQUEST" TIMESTAMP (6)
   ) ;
commit;