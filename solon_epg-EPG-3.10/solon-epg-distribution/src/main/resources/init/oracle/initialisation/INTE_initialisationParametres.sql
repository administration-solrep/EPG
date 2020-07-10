-- Permet d'initialiser les valeurs par défaut de Solon EPG
-- Script pour l'intégration (INTE)

-- URL de l'application transmise par mail
UPDATE parametre p SET p.value='http://idlv-solrep-epg-inte-01.lyon-dev2.local:8180/solon-epg' WHERE p.id=
	(SELECT h.id FROM hierarchy h WHERE h.primarytype='Parametre' AND h.name='url-application-transmise-par-mel');
	
UPDATE parametre p SET p.value='999' WHERE p.id=
	(SELECT h.id from hierarchy h where h.primarytype='Parametre' AND h.name='delai-renouvellement-mots-de-passe');

-- Adresse mail administrateur
UPDATE parametre p SET p.value='Administration-SOLREP@solon-epg.com' WHERE p.id=
	(SELECT h.id FROM hierarchy h WHERE h.primarytype='Parametre' AND h.name='adresse-mail-administrateur-application');
	
-- PARAMETRAGE MGPP
UPDATE parametrage_mgpp pm SET pm.urlepp='http://idlv-solrep-epp-inte-01.lyon-dev2.local:8180/solon-epp/site/solonepp', pm.loginepp='ws-gouvernement', pm.passepp='ws-gouvernement';

-- Date de dernier changement de mot de passe
UPDATE profil_utilisateur_solon_epg SET dernierchangementmotdepasse = SYSDATE;

commit;
