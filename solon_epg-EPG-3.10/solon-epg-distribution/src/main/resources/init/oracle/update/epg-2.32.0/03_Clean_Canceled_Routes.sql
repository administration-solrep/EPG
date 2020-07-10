delete from HIERARCHY WHERE (id IN (select id from MISC where LIFECYCLESTATE = 'canceled')) and  (HIERARCHY.PRIMARYTYPE='FeuilleRoute' or HIERARCHY.PRIMARYTYPE='RouteStep') ;
commit;
