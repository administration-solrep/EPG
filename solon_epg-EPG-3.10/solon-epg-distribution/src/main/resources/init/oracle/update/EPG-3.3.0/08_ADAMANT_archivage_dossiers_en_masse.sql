--Script d'archivage en masse des dossiers pour ADAMANT

--Renseigner des numéros NOR entre quotes dans les parenthèses, séparés par une virgule
--Par exemple : WHERE NUMERONOR IN ('PRMX1800003L','PRMX1800004V', 'ARTG1800001L');
--Il faut que ces dossiers possèdent un statut qui permette l'archivage. Par exemple : PUBLIE, TERMINE SANS PUBLICATION, CLOS

--La ligne "DATEPUBLICATION" permet de renseigner une plage de dates avec date de début et date de fin pour ajouter comme critère la date de publication
--Par exemple  DATEPUBLICATION between '01/05/12' AND '01/06/12'
--Ce critère de recherche est optionnel et il est donc possible de le supprimer de la requête

--La ligne 'STATUT' permet d'ajouter un critère de recherche sur le statut du dossier.
--L'ajout des statut se fait entre quotes, dans les parenthèses, séparés par une virgule.
--Par exemple : AND STATUT IN ('4', '6', '7') pour sélectionner uniquement des dossiers au statut PUBLIE, TERMINE SANS PUBLICATION et CLOS
--Rappel sur les différentes status :
--INITIE = '1'
--LANCE	 = '2'
--ABANDONN = '3'
--PUBLIE = '4'
--NOR_ATTRIBUE = '5'
--TERMINE_SANS_PUBLICATION = '6'
--CLOS = '7'
--Ce critère de recherche est optionnel et il est donc possible de le supprimer de la requête

--Il peut être nécessaire de se déconnecter puis de se reconnecter pour voir ces dossiers apparaître dans la liste des dossiers pour archivage définitif

UPDATE DOSSIER_SOLON_EPG
SET STATUTARCHIVAGE = '4'
WHERE NUMERONOR IN ('','')
AND DATEPUBLICATION between '' AND ''
AND STATUT IN ('', '');
commit;