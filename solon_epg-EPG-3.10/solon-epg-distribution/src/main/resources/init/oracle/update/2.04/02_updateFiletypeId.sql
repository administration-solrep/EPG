-- Déplacement de la nature vers le type FileSolonEPG

ALTER TABLE FILE_SOLON_EPG ADD (FILETYPEID NUMBER(2,0));

UPDATE (SELECT  f_F_1.ID, f_D_3.nature,f_f_1.filetypeid AS filetypeid
FROM FILE_SOLON_EPG f_F_1
LEFT JOIN DUBLINCORE f_D_3 ON f_D_3.ID = f_F_1.ID
WHERE
 f_D_3."NATURE" = 'acte') f
 SET  f.filetypeid = 1;

COMMIT;
 
 UPDATE (SELECT  f_F_1.ID, f_D_3.nature,f_f_1.filetypeid AS filetypeid
FROM FILE_SOLON_EPG f_F_1
LEFT JOIN DUBLINCORE f_D_3 ON f_D_3.ID = f_F_1.ID
WHERE
 f_D_3."NATURE" = 'extrait') f
 SET  f.filetypeid = 2;

COMMIT;

UPDATE ( SELECT  f_F_1.ID, f_D_3.nature,f_f_1.filetypeid AS filetypeid
FROM FILE_SOLON_EPG f_F_1
LEFT JOIN DUBLINCORE f_D_3 ON f_D_3.ID = f_F_1.ID
WHERE
 f_D_3."NATURE" = 'autreparapheur') f
 SET  f.filetypeid = 3;
 
COMMIT;

UPDATE ( SELECT  f_F_1.ID, f_D_3.nature,f_f_1.filetypeid AS filetypeid
FROM FILE_SOLON_EPG f_F_1
LEFT JOIN DUBLINCORE f_D_3 ON f_D_3.ID = f_F_1.ID
WHERE
 f_D_3."NATURE" = 'fonddossier') f
 SET  f.filetypeid = 4;
 
 COMMIT;