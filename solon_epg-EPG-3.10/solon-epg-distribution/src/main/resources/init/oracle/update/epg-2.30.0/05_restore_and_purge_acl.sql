

CREATE OR REPLACE PROCEDURE "NX_VACUUM_READ_ACLS"
-- Remove unused read acls entries
IS
BEGIN
  -- nx_vacuum_read_acls vacuuming
  DELETE FROM aclr WHERE acl_id IN (SELECT r.acl_id FROM aclr r
    LEFT JOIN hierarchy_read_acl h ON r.acl_id=h.acl_id
    WHERE h.acl_id IS NULL);
  EXECUTE IMMEDIATE 'TRUNCATE TABLE aclr_modified';
  EXECUTE IMMEDIATE 'TRUNCATE TABLE SW_ACLR_USERID_USER';
  EXECUTE IMMEDIATE 'TRUNCATE TABLE SW_ACLR_USER_ACLID';
  EXECUTE IMMEDIATE 'TRUNCATE TABLE SW_ACLR_DUSER_ACLID';
END;
/


CREATE OR REPLACE PROCEDURE "NX_REBUILD_READ_ACLS"
-- Rebuild the read acls tables
IS
BEGIN
  EXECUTE IMMEDIATE 'TRUNCATE TABLE aclr';
  EXECUTE IMMEDIATE 'TRUNCATE TABLE hierarchy_read_acl';
  EXECUTE IMMEDIATE 'TRUNCATE TABLE aclr_modified';
  EXECUTE IMMEDIATE 'TRUNCATE TABLE SW_ACLR_USERID_USER';
  EXECUTE IMMEDIATE 'TRUNCATE TABLE SW_ACLR_USER_ACLID';
  EXECUTE IMMEDIATE 'TRUNCATE TABLE SW_ACLR_DUSER_ACLID';
  INSERT INTO hierarchy_read_acl
    SELECT id, nx_get_read_acl_id(id)
      FROM (SELECT id FROM hierarchy WHERE isproperty = 0);
END;
/



CREATE OR REPLACE TRIGGER "NX_TRIG_HIER_READ_ACL_MOD"
  AFTER INSERT OR UPDATE ON hierarchy_read_acl
  FOR EACH ROW
   WHEN (NEW.acl_id IS NOT NULL) BEGIN
  MERGE INTO aclr USING DUAL
    ON (aclr.acl_id = :NEW.acl_id)
    WHEN NOT MATCHED THEN
    INSERT (acl_id, acl) VALUES (:NEW.acl_id, nx_get_read_acl(:NEW.id));
END;
/


CREATE OR REPLACE TRIGGER "NX_TRIG_ACLR_MODIFIED"
  AFTER INSERT ON aclr
  FOR EACH ROW
   WHEN (NEW.acl_id IS NOT NULL) DECLARE
  acl NX_STRING_ARRAY;
  ace VARCHAR(4000);
  sep VARCHAR2(1) := '|';
  neg VARCHAR2(200);
BEGIN
                acl := split(:NEW.acl, sep);
        FOR r IN (SELECT DISTINCT USERGROUP FROM SW_ACLR_USER_ACLID) LOOP
                                neg := '-'||r.usergroup;
                FOR i IN acl.FIRST .. acl.LAST LOOP
                        ace := acl(i);
                        IF ace = r.usergroup THEN
                                -- GRANTED
                                INSERT INTO SW_ACLR_USER_ACLID SELECT r.usergroup, :NEW.acl_id FROM DUAL
                                WHERE NOT EXISTS (SELECT 1 FROM SW_ACLR_USER_ACLID WHERE usergroup = r.usergroup AND acl_id = :NEW.acl_id);
                                                ELSIF ace = neg THEN
                                                                --DENY
                                                                INSERT INTO SW_ACLR_DUSER_ACLID SELECT r.usergroup, :NEW.acl_id FROM DUAL
                                WHERE NOT EXISTS (SELECT 1 FROM SW_ACLR_DUSER_ACLID WHERE usergroup = r.usergroup AND acl_id = :NEW.acl_id);
                        END IF;
                END LOOP;
        END LOOP;
END;
/



create or replace PROCEDURE "SW_VACUUM_ACLIDS"
-- Remove unused read acls entries
IS
BEGIN
  DELETE FROM aclr a WHERE NOT EXISTS(SELECT 1 FROM hierarchy_read_acl r WHERE r.acl_id = a.acl_id);
  DELETE FROM SW_ACLR_USER_ACLID a WHERE NOT EXISTS(SELECT 1 FROM aclr r WHERE r.acl_id = a.acl_id);  
  DELETE FROM SW_ACLR_DUSER_ACLID a WHERE NOT EXISTS(SELECT 1 FROM aclr r WHERE r.acl_id = a.acl_id);  
END;
/

create or replace PROCEDURE "SW_UPDATE_USERGROUP_ACL"
IS
  usergroups NX_STRING_TABLE;
BEGIN
  SELECT distinct USERGROUP BULK COLLECT INTO usergroups from (SELECT USERGROUP FROM SW_ACLR_USER_ACLID UNION SELECT USERGROUP FROM SW_ACLR_DUSER_ACLID);
  EXECUTE IMMEDIATE 'TRUNCATE TABLE SW_ACLR_USER_ACLID';
  EXECUTE IMMEDIATE 'TRUNCATE TABLE SW_ACLR_DUSER_ACLID';
  FOR usergroup IN (select column_value from table(usergroups)) LOOP
    SW_FILL_ACLID(usergroup.column_value);
  END LOOP;
  EXECUTE IMMEDIATE 'TRUNCATE TABLE SW_ACLR_USERID_USER';
END;
/


-- PURGE OLD ACL

CALL SW_VACUUM_ACLIDS();

-- UPDATE USER - ACL association

CALL SW_UPDATE_USERGROUP_ACL();

-- RESTORE NX_GET_READ_ACLS_FOR

CREATE OR REPLACE FUNCTION "NX_GET_READ_ACLS_FOR" (users NX_STRING_TABLE)
RETURN NX_STRING_TABLE
-- List read acl ids for a list of user/groups, using the cache
IS
  user_md5 VARCHAR2(34) := sw_retrieve_user(users);
BEGIN
  RETURN SW_GET_READ_ACLS_FOR(user_md5);
END;
/


-- UNUSED ELEMENTS 

DROP FUNCTION "NX_LIST_READ_ACLS_FOR";

DROP TABLE ACLR_USER_MAP;

DROP TABLE ACLR_USER;



