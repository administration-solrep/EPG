-- Ajout de la table pour le schema EXPORT_PAN_STAT  
CREATE TABLE "EXPORT_PAN_STAT" 
   (	"ID" VARCHAR2(36 BYTE), 
		"OWNER" NVARCHAR2(2000),
		"DATEREQUEST" TIMESTAMP (6)
   ) ;
commit;