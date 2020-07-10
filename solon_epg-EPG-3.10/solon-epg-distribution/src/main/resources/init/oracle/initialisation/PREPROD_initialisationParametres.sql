-- Permet d'initialiser les valeurs par défaut de Solon EPG
-- Script pour la plateforme de pré-production (PREPROD)

-- URL de l'application transmise par mail
UPDATE parametre p SET p.value='https://solon-epg-preprod.ader.gouv.fr/solon-epg' WHERE p.id=
	(SELECT h.id FROM hierarchy h WHERE h.primarytype='Parametre' AND h.name='url-application-transmise-par-mel');

-- Adresse mail administrateur
UPDATE parametre p SET p.value='Administration-SOLREP@dila.gouv.fr' WHERE p.id=
	(SELECT h.id FROM hierarchy h WHERE h.primarytype='Parametre' AND h.name='adresse-mail-administrateur-application');
	
-- PARAMETRAGE MGPP
UPDATE parametrage_mgpp pm SET pm.urlepp='http://vmwpda-tepp-v01.interne.local:8180/solon-epp/site/solonepp', pm.loginepp='ws-gouvernement', pm.passepp='ws_gouvernement_access';

commit;
