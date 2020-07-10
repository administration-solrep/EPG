--- Correctifs sur les différentes tables ACL ---

---- Droits sur des éléments manqants
UPDATE sw_aclr_user_aclid sw set sw.usergroup='Rauchene' 
WHERE sw.usergroup='rauchene' AND sw.acl_id NOT IN 
(SELECT sw1.acl_id FROM sw_aclr_user_aclid sw1 WHERE sw1.usergroup='Rauchene');

---- Correctif sur les droits de visibilité
UPDATE aclr acl set acl.acl=REPLACE(acl.acl,'rauchene|','Rauchene|') 
WHERE acl.acl LIKE 'rauchene%';

UPDATE aclr acl set acl.acl=REPLACE(acl.acl,'|Rauchene|','|') 
WHERE acl.acl LIKE 'Rauchene%';

UPDATE aclr acl set acl.acl=REPLACE(acl.acl,'|-Rauchene|','|') 
WHERE acl.acl LIKE 'Rauchene%';

---- Correctif items
UPDATE acls acl SET acl."USER"='Rauchene' 
WHERE acl."USER"='rauchene' ;

commit;