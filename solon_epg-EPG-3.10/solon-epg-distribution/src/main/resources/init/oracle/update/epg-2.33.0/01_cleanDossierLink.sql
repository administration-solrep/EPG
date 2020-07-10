DELETE FROM actionnable_case_link 
where id in (select id from misc where lifecyclestate = 'todo') 
and caseDocumentId in (select id from dossier_solon_epg where lastdocumentroute in (select id from misc where lifecyclestate ='done'));

commit;
