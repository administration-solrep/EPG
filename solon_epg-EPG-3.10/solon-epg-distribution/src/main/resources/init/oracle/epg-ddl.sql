--------------------------------------------------------
--  Fichier créé - jeudi-février-23-2012   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Type NX_STRING_ARRAY
--------------------------------------------------------
spool epg-ddl.sql.log

  CREATE OR REPLACE TYPE "NX_STRING_ARRAY" AS VARRAY(200) OF VARCHAR2(32767);
/

--------------------------------------------------------
--  DDL for Type NX_STRING_TABLE
--------------------------------------------------------

  CREATE OR REPLACE TYPE "NX_STRING_TABLE" AS TABLE OF VARCHAR2(4000);
/

--------------------------------------------------------
--  DDL for Sequence HIBERNATE_SEQUENCE
--------------------------------------------------------

   CREATE SEQUENCE  "HIBERNATE_SEQUENCE"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 41 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Table ACLR
--------------------------------------------------------

  CREATE TABLE "ACLR" 
   (	"ACL_ID" VARCHAR2(34 BYTE), 
	"ACL" VARCHAR2(4000 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table ACLR_MODIFIED
--------------------------------------------------------

  CREATE TABLE "ACLR_MODIFIED" 
   (	"HIERARCHY_ID" VARCHAR2(36 BYTE), 
	"IS_NEW" NUMBER(1,0)
   ) ;
--------------------------------------------------------
--  DDL for Table ACLR_PERMISSION
--------------------------------------------------------

  CREATE TABLE "ACLR_PERMISSION" 
   (	"PERMISSION" VARCHAR2(250 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table ACLR_USER
--------------------------------------------------------

  CREATE TABLE "ACLR_USER" 
   (	"USER_ID" VARCHAR2(34 BYTE), 
	"USERS" "NX_STRING_TABLE" 
   ) 
 NESTED TABLE "USERS" STORE AS "ACLR_USER_USERS"
 RETURN AS VALUE;
--------------------------------------------------------
--  DDL for Table ACLR_USER_MAP
--------------------------------------------------------

  CREATE TABLE "ACLR_USER_MAP" 
   (	"USER_ID" VARCHAR2(34 BYTE), 
	"ACL_ID" VARCHAR2(34 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table ACLS
--------------------------------------------------------

  CREATE TABLE "ACLS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"NAME" VARCHAR2(250 BYTE), 
	"GRANT" NUMBER(1,0), 
	"PERMISSION" VARCHAR2(250 BYTE), 
	"USER" VARCHAR2(250 BYTE), 
	"GROUP" VARCHAR2(250 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table ACTIONNABLE_CASE_LINK
--------------------------------------------------------

  CREATE TABLE "ACTIONNABLE_CASE_LINK" 
   (	"ID" VARCHAR2(36 BYTE), 
	"AUTOMATICVALIDATED" NUMBER(1,0), 
	"DUEDATE" TIMESTAMP (6), 
	"TITREACTE" NVARCHAR2(2000), 
	"ISMAILSEND" NUMBER(1,0), 
	"AUTOMATICVALIDATION" NUMBER(1,0), 
	"TASKTYPE" NVARCHAR2(2000), 
	"STEPDOCUMENTID" VARCHAR2(50 BYTE), 
	"ROUTINGTASKID" VARCHAR2(50 BYTE), 
	"CASELIFECYCLESTATE" NVARCHAR2(50), 
	"STATUTARCHIVAGE" NVARCHAR2(2), 
	"ROUTINGTASKLABEL" NVARCHAR2(150), 
	"ROUTINGTASKTYPE" NVARCHAR2(4), 
	"VALIDATIONOPERATIONCHAINID" NVARCHAR2(150), 
	"REFUSALOPERATIONCHAINID" NVARCHAR2(150), 
	"ROUTINGTASKMAILBOXLABEL" NVARCHAR2(150)
   ) ;
--------------------------------------------------------
--  DDL for Table ACTIVITE_NORMATIVE
--------------------------------------------------------

  CREATE TABLE "ACTIVITE_NORMATIVE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"TRAITE" NVARCHAR2(2000), 
	"APPLICATIONLOI" NVARCHAR2(2000), 
	"TRANSPOSITION" NVARCHAR2(2000), 
	"ORDONNANCE" NVARCHAR2(2000), 
	"ORDONNANCE38C" NVARCHAR2(2000),
	"APPLICATIONORDONNANCE" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table ACTIVITE_NORMATIVE_PR_DE9E019D
--------------------------------------------------------

  CREATE TABLE "ACTIVITE_NORMATIVE_PR_DE9E019D" 
   (	"ID" VARCHAR2(36 BYTE), 
	"LOCKHABUSERFULNAME" NVARCHAR2(2000), 
	"TABLEAUSUIVIHABPUBLICATIONUSER" NVARCHAR2(2000), 
	"TABLEAUSUIVIPUBLICATIONDATE" TIMESTAMP (6), 
	"LOCKHABDATE" TIMESTAMP (6), 
	"TABLEAUSUIVIPUBLICATIONUSER" NVARCHAR2(2000), 
	"LOCKUSERFULNAME" NVARCHAR2(2000), 
	"TABLEAUSUIVIHABPUBLICATIONDATE" TIMESTAMP (6), 
	"LOCKDATE" TIMESTAMP (6)
   ) ;
--------------------------------------------------------
--  DDL for Table ACTIVITE_PARLEMENTAIRE
--------------------------------------------------------

  CREATE TABLE "ACTIVITE_PARLEMENTAIRE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"INTITULELOI" NVARCHAR2(2000), 
	"ACTIVITE" NVARCHAR2(2000), 
	"DATE" TIMESTAMP (6), 
	"ASSEMBLEE" NVARCHAR2(2000), 
	"IDDOSSIER" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table ADVANCED_SEARCH
--------------------------------------------------------

  CREATE TABLE "ADVANCED_SEARCH" 
   (	"ID" VARCHAR2(36 BYTE), 
	"TITLE" NVARCHAR2(2000), 
	"VALID_MAX" TIMESTAMP (6), 
	"EXPIRED_MIN" TIMESTAMP (6), 
	"FULLTEXT_NONE" NVARCHAR2(2000), 
	"FORMAT" NVARCHAR2(2000), 
	"ISCHECKEDINVERSION" NUMBER(1,0), 
	"VALID_MIN" TIMESTAMP (6), 
	"ISSUED_MAX" TIMESTAMP (6), 
	"EXPIRED_MAX" TIMESTAMP (6), 
	"CREATED_MIN" TIMESTAMP (6), 
	"CREATED_MAX" TIMESTAMP (6), 
	"FULLTEXT_PHRASE" NVARCHAR2(2000), 
	"MODIFIED_MAX" TIMESTAMP (6), 
	"FULLTEXT_ALL" NVARCHAR2(2000), 
	"SORTCOLUMN" NVARCHAR2(2000), 
	"DESCRIPTION" NVARCHAR2(2000), 
	"CURRENTLIFECYCLESTATE" NVARCHAR2(2000), 
	"SOURCE" NVARCHAR2(2000), 
	"SORTASCENDING" NUMBER(1,0), 
	"RIGHTS" NVARCHAR2(2000), 
	"LANGUAGE" NVARCHAR2(2000), 
	"ISSUED_MIN" TIMESTAMP (6), 
	"MODIFIED_MIN" TIMESTAMP (6), 
	"FULLTEXT_ONE_OF" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table ALERT
--------------------------------------------------------

  CREATE TABLE "ALERT" 
   (	"ID" VARCHAR2(36 BYTE), 
	"DATEVALIDITYBEGIN" TIMESTAMP (6), 
	"ISACTIVATED" NUMBER(1,0), 
	"PERIODICITY" NVARCHAR2(2000), 
	"REQUETEID" NVARCHAR2(2000), 
	"DATEVALIDITYEND" TIMESTAMP (6), 
	"DATEDEMANDECONFIRMATION" TIMESTAMP (6), 
	"HASDEMANDECONFIRMATION" NUMBER(1,0)
   ) ;
--------------------------------------------------------
--  DDL for Table ALTR_RECIPIENTS
--------------------------------------------------------

  CREATE TABLE "ALTR_RECIPIENTS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table AMPLIATIONDESTINATAIREMAILS
--------------------------------------------------------

  CREATE TABLE "AMPLIATIONDESTINATAIREMAILS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table ANCESTORS
--------------------------------------------------------

  CREATE TABLE "ANCESTORS" 
   (	"HIERARCHY_ID" VARCHAR2(36 BYTE), 
	"ANCESTORS" "NX_STRING_TABLE" 
   ) 
 NESTED TABLE "ANCESTORS" STORE AS "ANCESTORS_ANCESTORS"
 RETURN AS VALUE;
--------------------------------------------------------
--  DDL for Table BIRTREPORT
--------------------------------------------------------

  CREATE TABLE "BIRTREPORT" 
   (	"ID" VARCHAR2(36 BYTE), 
	"REPORTKEY" NVARCHAR2(2000), 
	"OLDMODELREF" NVARCHAR2(2000), 
	"MODELREF" NVARCHAR2(2000)
   ) ;
   
   --------------------------------------------------------
--  DDL for Table HIERARCHY
--------------------------------------------------------

  CREATE TABLE "HIERARCHY" 
   (	"ID" VARCHAR2(36 BYTE), 
	"PARENTID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"NAME" NVARCHAR2(2000), 
	"ISPROPERTY" NUMBER(1,0), 
	"PRIMARYTYPE" VARCHAR2(250 BYTE), 
	"MIXINTYPES" VARCHAR2(250 BYTE), 
	"ISCHECKEDIN" NUMBER(1,0), 
	"BASEVERSIONID" VARCHAR2(36 BYTE), 
	"MAJORVERSION" NUMBER(19,0), 
	"MINORVERSION" NUMBER(19,0), 
	"ISVERSION" NUMBER(1,0),
	"ISDELETED" NUMBER(1,0),
	"DELETEDTIME" TIMESTAMP (6)
   ) ;
   
--------------------------------------------------------
--  DDL for Table BIRTREPORTMODEL
--------------------------------------------------------

  CREATE TABLE "BIRTREPORTMODEL" 
   (	"ID" VARCHAR2(36 BYTE), 
	"REPORTNAME" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table BIRT_RESULTAT_FICHIER
--------------------------------------------------------

  CREATE TABLE "BIRT_RESULTAT_FICHIER" 
   (	"ID" VARCHAR2(36 BYTE), 
	"FILENAME" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table BULLETIN_OFFICIEL
--------------------------------------------------------

  CREATE TABLE "BULLETIN_OFFICIEL" 
   (	"ID" VARCHAR2(36 BYTE), 
	"BOINTITULE" NVARCHAR2(2000), 
	"BONOR" NVARCHAR2(5), 
	"BOETAT" NVARCHAR2(15)
   ) ;

--------------------------------------------------------
--  DDL for Table VECTEUR_PUBLICATION
--------------------------------------------------------
   
   CREATE TABLE "VECTEUR_PUBLICATION" 
   (	"ID" VARCHAR2(36 BYTE) NOT NULL, 
	"VPINTITULE" NVARCHAR2(2000), 
	"VPDEBUT"  TIMESTAMP (6), 
	"VPFIN"  TIMESTAMP (6),
	"VPPOS" NUMBER(10,0)
   ) ;
--------------------------------------------------------
--  DDL for Table CASE_DOCUMENTSID
--------------------------------------------------------

  CREATE TABLE "CASE_DOCUMENTSID" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table CASE_ITEM
--------------------------------------------------------

  CREATE TABLE "CASE_ITEM" 
   (	"ID" VARCHAR2(36 BYTE), 
	"RECEIVE_DATE" TIMESTAMP (6), 
	"BODY" NVARCHAR2(2000), 
	"TYPE" NVARCHAR2(2000), 
	"REFERENCE" NVARCHAR2(2000), 
	"DEFAULTCASEID" NVARCHAR2(2000), 
	"DOCUMENT_DATE" TIMESTAMP (6), 
	"IMPORT_DATE" TIMESTAMP (6), 
	"SENDING_DATE" TIMESTAMP (6), 
	"CONFIDENTIALITY" NVARCHAR2(2000), 
	"ORIGIN" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table CASE_LINK
--------------------------------------------------------

  CREATE TABLE "CASE_LINK" 
   (	"ID" VARCHAR2(36 BYTE), 
	"COMMENT" NVARCHAR2(2000), 
	"TYPEINFO" NVARCHAR2(2000), 
	"DRAFT" NUMBER(1,0), 
	"CASEITEMID" NVARCHAR2(2000), 
	"DATE" TIMESTAMP (6), 
	"ISACTIONABLE" NUMBER(1,0), 
	"SENDERMAILBOXID" NVARCHAR2(2000), 
	"SENTDATE" TIMESTAMP (6), 
	"CASEREPOSITORYNAME" NVARCHAR2(2000), 
	"TYPE" NVARCHAR2(2000), 
	"ISSENT" NUMBER(1,0), 
	"SENDER" NVARCHAR2(2000), 
	"ISREAD" NUMBER(1,0), 
	"CASEDOCUMENTID" VARCHAR2(50 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table CLASSIFICATION_TARGETS
--------------------------------------------------------

  CREATE TABLE "CLASSIFICATION_TARGETS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table CLUSTER_INVALS
--------------------------------------------------------

  CREATE TABLE "CLUSTER_INVALS" 
   (	"NODEID" NUMBER(10,0), 
	"ID" VARCHAR2(36 BYTE), 
	"FRAGMENTS" VARCHAR2(4000 BYTE), 
	"KIND" NUMBER(3,0)
   ) ;
--------------------------------------------------------
--  DDL for Table CLUSTER_NODES
--------------------------------------------------------

  CREATE TABLE "CLUSTER_NODES" 
   (	"NODEID" NUMBER(10,0), 
	"CREATED" TIMESTAMP (6)
   ) ;
--------------------------------------------------------
--  DDL for Table CMDIST_ALL_ACTION_PAR_6B4BBED8
--------------------------------------------------------

  CREATE TABLE "CMDIST_ALL_ACTION_PAR_6B4BBED8" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(100)
   ) ;
--------------------------------------------------------
--  DDL for Table CMDIST_ALL_COPY_PARTI_21AB3C5B
--------------------------------------------------------

  CREATE TABLE "CMDIST_ALL_COPY_PARTI_21AB3C5B" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table CMDIST_INITIAL_ACTION_4CD43708
--------------------------------------------------------

  CREATE TABLE "CMDIST_INITIAL_ACTION_4CD43708" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(100)
   ) ;
--------------------------------------------------------
--  DDL for Table CMDIST_INITIAL_ACTION_88A481B7
--------------------------------------------------------

  CREATE TABLE "CMDIST_INITIAL_ACTION_88A481B7" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table CMDIST_INITIAL_COPY_E_B3610C04
--------------------------------------------------------

  CREATE TABLE "CMDIST_INITIAL_COPY_E_B3610C04" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table CMDIST_INITIAL_COPY_I_D6588F7E
--------------------------------------------------------

  CREATE TABLE "CMDIST_INITIAL_COPY_I_D6588F7E" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table CMDOC_SENDERS
--------------------------------------------------------

  CREATE TABLE "CMDOC_SENDERS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table COMMENT
--------------------------------------------------------

  CREATE TABLE "COMMENT" 
   (	"ID" VARCHAR2(36 BYTE), 
	"AUTHOR" NVARCHAR2(2000), 
	"CREATIONDATE" TIMESTAMP (6), 
	"TEXT" NVARCHAR2(2000), 
	"VISIBLETOMINISTEREID" NVARCHAR2(2000), 
	"VISIBLETOPOSTEID" NVARCHAR2(2000), 
	"VISIBLETOUNITESTRUCTURELLEID" NVARCHAR2(2000), 
	"COMMENTEDDOCID" VARCHAR2(50 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table COMMON
--------------------------------------------------------

  CREATE TABLE "COMMON" 
   (	"ID" VARCHAR2(36 BYTE), 
	"ICON" NVARCHAR2(2000), 
	"ICON-EXPANDED" NVARCHAR2(2000), 
	"SIZE" NUMBER(19,0)
   ) ;
--------------------------------------------------------
--  DDL for Table CONSEIL_ETAT
--------------------------------------------------------

  CREATE TABLE "CONSEIL_ETAT" 
   (	"ID" VARCHAR2(36 BYTE), 
	"DATESECTIONCE" TIMESTAMP (6), 
	"RAPPORTEURCE" NVARCHAR2(2000), 
	"DATESORTIECE" TIMESTAMP (6), 
	"RECEPTIONAVISCE" TIMESTAMP (6), 
	"DATETRANSMISSIONSECTIONCE" TIMESTAMP (6), 
	"DATESAISINECE" TIMESTAMP (6), 
	"DATEAGCE" TIMESTAMP (6), 
	"NUMEROISA" NVARCHAR2(2000), 
	"SECTIONCE" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table CONTENT
--------------------------------------------------------

  CREATE TABLE "CONTENT" 
   (	"ID" VARCHAR2(36 BYTE), 
	"NAME" NVARCHAR2(2000), 
	"LENGTH" NUMBER(19,0), 
	"DATA" VARCHAR2(40 BYTE), 
	"ENCODING" NVARCHAR2(2000), 
	"DIGEST" NVARCHAR2(2000), 
	"MIME-TYPE" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table CONTENT_VIEW_DISPLAY
--------------------------------------------------------

  CREATE TABLE "CONTENT_VIEW_DISPLAY" 
   (	"ID" VARCHAR2(36 BYTE), 
	"PAGESIZE" NUMBER(19,0)
   ) ;
--------------------------------------------------------
--  DDL for Table continent
--------------------------------------------------------

  CREATE TABLE "continent" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table country
--------------------------------------------------------

  CREATE TABLE "country" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"parent" NVARCHAR2(2000), 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table CVD_SELECTEDLAYOUTCOLUMNS
--------------------------------------------------------

  CREATE TABLE "CVD_SELECTEDLAYOUTCOLUMNS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
   
--------------------------------------------------------
--  DDL for Table DC_CONTRIBUTORS
--------------------------------------------------------

  CREATE TABLE "DC_CONTRIBUTORS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(100)
   ) ;
--------------------------------------------------------
--  DDL for Table DC_SUBJECTS
--------------------------------------------------------

  CREATE TABLE "DC_SUBJECTS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
   
--------------------------------------------------------
--  DDL for Table DEFAULTSETTINGS
--------------------------------------------------------

  CREATE TABLE "DEFAULTSETTINGS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"LANGUAGE" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table DESTINATAIRECOMMUNICATION
--------------------------------------------------------

  CREATE TABLE "DESTINATAIRECOMMUNICATION" 
   (	"ID" VARCHAR2(36 BYTE), 
	"DATERELANCECOMMUNICATION" TIMESTAMP (6), 
	"DATEENVOICOMMUNICATION" TIMESTAMP (6), 
	"DATERETOURCOMMUNICATION" TIMESTAMP (6), 
	"SENSAVISCOMMUNICATION" NVARCHAR2(2000), 
	"NOMDESTINATAIRECOMMUNICATION" NVARCHAR2(2000), 
	"OBJETCOMMUNICATION" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table DOCRI_PARTICIPATINGDOCUMENTS
--------------------------------------------------------

  CREATE TABLE "DOCRI_PARTICIPATINGDOCUMENTS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" VARCHAR2(50 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table DONNEESSIGNATAIRE
--------------------------------------------------------

  CREATE TABLE "DONNEESSIGNATAIRE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"DATERETOURSIGNATURE" TIMESTAMP (6), 
	"DATEENVOISIGNATURE" TIMESTAMP (6), 
	"COMMENTAIRESIGNATURE" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table DOS_LIBRE
--------------------------------------------------------

  CREATE TABLE "DOS_LIBRE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table DOS_MOTSCLES
--------------------------------------------------------

  CREATE TABLE "DOS_MOTSCLES" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table DOS_NOMCOMPLETCHARGESMISSIONS
--------------------------------------------------------

  CREATE TABLE "DOS_NOMCOMPLETCHARGESMISSIONS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table DOS_NOMCOMPLETCONSEILLERSPMS
--------------------------------------------------------

  CREATE TABLE "DOS_NOMCOMPLETCONSEILLERSPMS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table DOS_PUBLICATIONSCONJOINTES
--------------------------------------------------------

  CREATE TABLE "DOS_PUBLICATIONSCONJOINTES" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(100)
   ) ;
--------------------------------------------------------
--  DDL for Table DOS_RUBRIQUES
--------------------------------------------------------

  CREATE TABLE "DOS_RUBRIQUES" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table DOSSIER_SOLON_EPG
--------------------------------------------------------

  CREATE TABLE "DOSSIER_SOLON_EPG" 
   (	"ID" VARCHAR2(36 BYTE), 
	"DATESIGNATURE" TIMESTAMP (6), 
	"ISURGENT" NUMBER(1,0), 
	"POSTECREATOR" NVARCHAR2(2000), 
	"ISPARAPHEURTYPEACTEUPDATED" NUMBER(1,0), 
	"INDEXATIONMIN" NUMBER(1,0), 
	"INDEXATIONDIRPUB" NUMBER(1,0), 
	"DATEPUBLICATION" TIMESTAMP (6), 
	"DECRETNUMEROTE" NUMBER(1,0), 
	"HABILITATIONNUMEROARTICLES" NVARCHAR2(2000), 
	"NUMEROVERSIONACTEOUEXTRAIT" NUMBER(19,0), 
	"PUBLICATIONRAPPORTPRESENTATION" NUMBER(1,0), 
	"DISPOSITIONHABILITATION" NUMBER(1,0), 
	"DATEDEMAINTIENENPRODUCTION" TIMESTAMP (6), 
	"INDEXATIONDIR" NUMBER(1,0), 
	"ISPARAPHEURCOMPLET" NUMBER(1,0), 
	"TITREACTE" NVARCHAR2(2000), 
	"ZONECOMSGGDILA" NVARCHAR2(2000), 
	"IDDOSSIER" NVARCHAR2(2000), 
	"POURFOURNITUREEPREUVE" TIMESTAMP (6), 
	"INDEXATIONSGG" NUMBER(1,0), 
	"HABILITATIONCOMMENTAIRE" NVARCHAR2(2000), 
	"HABILITATIONTERME" NVARCHAR2(2000), 
	"DATEENVOIJOTS" TIMESTAMP (6), 
	"INDEXATIONSGGPUB" NUMBER(1,0), 
	"NBDOSSIERRECTIFIE" NUMBER(19,0), 
	"NOMCOMPLETRESPDOSSIER" NVARCHAR2(2000), 
	"DATECANDIDATURE" TIMESTAMP (6), 
	"ARRIVEESOLONTS" NUMBER(1,0), 
	"ISDOSSIERISSUINJECTION" NUMBER(1,0), 
	"ISACTEREFERENCEFORNUM_302A1DC6" NUMBER(1,0), 
	"DATEVERSEMENTTS" TIMESTAMP (6), 
	"DATEVERSEMENTARCHIVAG_E55E9FEF" TIMESTAMP (6), 
	"HABILITATIONNUMEROORDRE" NVARCHAR2(2000), 
	"HABILITATIONREFLOI" NVARCHAR2(2000), 
	"MESURENOMINATIVE" NUMBER(1,0), 
	"ISPARAPHEURFICHIERINF_54BB2DAB" NUMBER(1,0), 
	"DATEPRECISEEPUBLICATION" TIMESTAMP (6), 
	"HABILITATIONDATETERME" TIMESTAMP (6), 
	"INDEXATIONMINPUB" NUMBER(1,0), 
	"STATUT" NVARCHAR2(2), 
	"STATUTARCHIVAGE" NVARCHAR2(2), 
	"NUMERONOR" NVARCHAR2(12), 
	"CATEGORIEACTE" NVARCHAR2(50), 
	"IDDOCUMENTPARAPHEUR" VARCHAR2(50 BYTE), 
	"TYPEACTE" NVARCHAR2(10), 
	"PUBLICATIONINTEGRALEOUEXTRAIT" NVARCHAR2(1), 
	"MINISTEREATTACHE" NVARCHAR2(50), 
	"BASELEGALEACTE" NVARCHAR2(100), 
	"LASTDOCUMENTROUTE" VARCHAR2(50 BYTE), 
	"DIRECTIONRESP" NVARCHAR2(50), 
	"MINISTERERESP" NVARCHAR2(50), 
	"IDDOCUMENTFDD" VARCHAR2(50 BYTE), 
	"DIRECTIONATTACHE" NVARCHAR2(50), 
	"DELAIPUBLICATION" NVARCHAR2(2), 
	"NOMRESPDOSSIER" NVARCHAR2(100), 
	"PRENOMRESPDOSSIER" NVARCHAR2(100), 
	"QUALITERESPDOSSIER" NVARCHAR2(250), 
	"MAILRESPDOSSIER" NVARCHAR2(150), 
	"TELRESPDOSSIER" NVARCHAR2(150), 
	"IDCREATEURDOSSIER" NVARCHAR2(100), 
	"CANDIDAT" NVARCHAR2(2),
	"DELETED" NUMBER(1,0) DEFAULT 0
   ) ;
--------------------------------------------------------
--  DDL for Table DOS_TRANSPOSITIONDIRE_3E14AB2D
--------------------------------------------------------

  CREATE TABLE "DOS_TRANSPOSITIONDIRE_3E14AB2D" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table DOS_VECTEURPUBLICATION
--------------------------------------------------------

  CREATE TABLE "DOS_VECTEURPUBLICATION" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(100)
   ) ;
--------------------------------------------------------
--  DDL for Table DR$FULLTEXT_FULLTEXT_IDX$I
--------------------------------------------------------

  CREATE TABLE "DR$FULLTEXT_FULLTEXT_IDX$I" 
   (	"TOKEN_TEXT" VARCHAR2(64 BYTE), 
	"TOKEN_TYPE" NUMBER(3,0), 
	"TOKEN_FIRST" NUMBER(10,0), 
	"TOKEN_LAST" NUMBER(10,0), 
	"TOKEN_COUNT" NUMBER(10,0), 
	"TOKEN_INFO" BLOB
   ) ;
--------------------------------------------------------
--  DDL for Table DR$FULLTEXT_FULLTEXT_IDX$K
--------------------------------------------------------

  CREATE TABLE "DR$FULLTEXT_FULLTEXT_IDX$K" 
   (	"DOCID" NUMBER(38,0), 
	"TEXTKEY" ROWID, 
	 PRIMARY KEY ("TEXTKEY") ENABLE
   ) ORGANIZATION INDEX NOCOMPRESS ;
--------------------------------------------------------
--  DDL for Table DR$FULLTEXT_FULLTEXT_IDX$N
--------------------------------------------------------

  CREATE TABLE "DR$FULLTEXT_FULLTEXT_IDX$N" 
   (	"NLT_DOCID" NUMBER(38,0), 
	"NLT_MARK" CHAR(1 BYTE), 
	 PRIMARY KEY ("NLT_DOCID") ENABLE
   ) ORGANIZATION INDEX NOCOMPRESS ;
--------------------------------------------------------
--  DDL for Table DR$FULLTEXT_FULLTEXT_IDX$R
--------------------------------------------------------

  CREATE TABLE "DR$FULLTEXT_FULLTEXT_IDX$R" 
   (	"ROW_NO" NUMBER(3,0), 
	"DATA" BLOB
   ) ;
--------------------------------------------------------
--  DDL for Table DR$FULLTEXT_FULLTEXT_NOR_IDX$I
--------------------------------------------------------

  CREATE TABLE "DR$FULLTEXT_FULLTEXT_NOR_IDX$I" 
   (	"TOKEN_TEXT" VARCHAR2(64 BYTE), 
	"TOKEN_TYPE" NUMBER(3,0), 
	"TOKEN_FIRST" NUMBER(10,0), 
	"TOKEN_LAST" NUMBER(10,0), 
	"TOKEN_COUNT" NUMBER(10,0), 
	"TOKEN_INFO" BLOB
   ) ;
--------------------------------------------------------
--  DDL for Table DR$FULLTEXT_FULLTEXT_NOR_IDX$K
--------------------------------------------------------

  CREATE TABLE "DR$FULLTEXT_FULLTEXT_NOR_IDX$K" 
   (	"DOCID" NUMBER(38,0), 
	"TEXTKEY" ROWID, 
	 PRIMARY KEY ("TEXTKEY") ENABLE
   ) ORGANIZATION INDEX NOCOMPRESS ;
--------------------------------------------------------
--  DDL for Table DR$FULLTEXT_FULLTEXT_NOR_IDX$N
--------------------------------------------------------

  CREATE TABLE "DR$FULLTEXT_FULLTEXT_NOR_IDX$N" 
   (	"NLT_DOCID" NUMBER(38,0), 
	"NLT_MARK" CHAR(1 BYTE), 
	 PRIMARY KEY ("NLT_DOCID") ENABLE
   ) ORGANIZATION INDEX NOCOMPRESS ;
--------------------------------------------------------
--  DDL for Table DR$FULLTEXT_FULLTEXT_NOR_IDX$R
--------------------------------------------------------

  CREATE TABLE "DR$FULLTEXT_FULLTEXT_NOR_IDX$R" 
   (	"ROW_NO" NUMBER(3,0), 
	"DATA" BLOB
   ) ;
--------------------------------------------------------
--  DDL for Table DUBLINCORE
--------------------------------------------------------

  CREATE TABLE "DUBLINCORE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"NATURE" NVARCHAR2(2000), 
	"SOURCE" NVARCHAR2(2000), 
	"CREATED" TIMESTAMP (6), 
	"RIGHTS" NVARCHAR2(2000), 
	"VALID" TIMESTAMP (6), 
	"FORMAT" NVARCHAR2(2000), 
	"ISSUED" TIMESTAMP (6), 
	"MODIFIED" TIMESTAMP (6), 
	"LANGUAGE" NVARCHAR2(2000), 
	"COVERAGE" NVARCHAR2(2000), 
	"EXPIRED" TIMESTAMP (6), 
	"CREATOR" NVARCHAR2(50), 
	"LASTCONTRIBUTOR" NVARCHAR2(50), 
	"DESCRIPTION" NVARCHAR2(500), 
	"TITLE" NVARCHAR2(500)
   ) ;
--------------------------------------------------------
--  DDL for Table ECHEANCIER_PROMULGATION
--------------------------------------------------------

  CREATE TABLE "ECHEANCIER_PROMULGATION" 
   (	"ID" VARCHAR2(36 BYTE), 
	"RETOURELYSEE" TIMESTAMP (6), 
	"DEPARTELYSEE" TIMESTAMP (6), 
	"CONTRESEINGPM" NVARCHAR2(2000), 
	"DATERECEPTION" TIMESTAMP (6), 
	"MISAUCONTRESEING" TIMESTAMP (6), 
	"DATESAISIECC" TIMESTAMP (6), 
	"INTITULELOI" NVARCHAR2(2000), 
	"DATEDECISION" TIMESTAMP (6), 
	"IDDOSSIER" NVARCHAR2(2000), 
	"DEMANDEEPREUVEJO" TIMESTAMP (6), 
	"DATELIMITEPROMULGATION" TIMESTAMP (6), 
	"ENVOIRELECTURE" TIMESTAMP (6), 
	"RETOURJO" TIMESTAMP (6), 
	"PUBLICATIONJO" TIMESTAMP (6), 
	"RETOURRELECTURE" TIMESTAMP (6)
   ) ;
--------------------------------------------------------
--  DDL for Table ETAT_APPLICATION
--------------------------------------------------------

  CREATE TABLE "ETAT_APPLICATION" 
   (	"ID" VARCHAR2(36 BYTE), 
	"DESCRIPTION_RESTRICTION" NVARCHAR2(2000), 
	"RESTRICTION_ACCES" NUMBER(1,0)
   ) ;
--------------------------------------------------------
--  DDL for Table FACETED_SEARCH
--------------------------------------------------------

  CREATE TABLE "FACETED_SEARCH" 
   (	"ID" VARCHAR2(36 BYTE), 
	"CONTENT_VIEW_NAME" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table FACETED_SEARCH_DEFAULT
--------------------------------------------------------

  CREATE TABLE "FACETED_SEARCH_DEFAULT" 
   (	"ID" VARCHAR2(36 BYTE), 
	"ECM_FULLTEXT" NVARCHAR2(2000), 
	"DC_CREATED_MAX" TIMESTAMP (6), 
	"DC_CREATED_MIN" TIMESTAMP (6), 
	"DC_MODIFIED_MAX" TIMESTAMP (6), 
	"DC_MODIFIED_MIN" TIMESTAMP (6)
   ) ;
--------------------------------------------------------
--  DDL for Table FAVCON_DOSSIERSIDS
--------------------------------------------------------

  CREATE TABLE "FAVCON_DOSSIERSIDS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" VARCHAR2(50 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table FAVCON_FDRSIDS
--------------------------------------------------------

  CREATE TABLE "FAVCON_FDRSIDS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" VARCHAR2(50 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table FAVCON_USERSNAMES
--------------------------------------------------------

  CREATE TABLE "FAVCON_USERSNAMES" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(100)
   ) ;
--------------------------------------------------------
--  DDL for Table FAVORIS_RECHERCHE
--------------------------------------------------------

  CREATE TABLE "FAVORIS_RECHERCHE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"FEUILLEROUTECREATIONU_85952588" NVARCHAR2(2000), 
	"ROUTESTEPOBLIGATOIRESGG" NVARCHAR2(2000), 
	"FEUILLEROUTECREATIONDATEMIN" TIMESTAMP (6), 
	"TYPE" NVARCHAR2(2000), 
	"FEUILLEROUTECREATIONDATEMAX" TIMESTAMP (6), 
	"ROUTESTEPROUTINGTASKTYPE" NVARCHAR2(2000), 
	"ROUTESTEPECHEANCEINDICATIVE" NUMBER(19,0), 
	"FEUILLEROUTETYPEACTE" NVARCHAR2(2000), 
	"ROUTESTEPDISTRIBUTIONMAILBOXID" NVARCHAR2(2000), 
	"ROUTESTEPAUTOMATICVALIDATION" NVARCHAR2(2000), 
	"FEUILLEROUTEMINISTERE" NVARCHAR2(2000), 
	"ROUTESTEPOBLIGATOIREMINISTERE" NVARCHAR2(2000), 
	"FEUILLEROUTETITLE" NVARCHAR2(2000), 
	"FEUILLEROUTEDIRECTION" NVARCHAR2(2000), 
	"FULLQUERY" NVARCHAR2(2000), 
	"FEUILLEROUTENUMERO" NVARCHAR2(2000), 
	"FEUILLEROUTEVALIDEE" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table FDD_FORMATAUTORISE
--------------------------------------------------------

  CREATE TABLE "FDD_FORMATAUTORISE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(10)
   ) ;
--------------------------------------------------------
--  DDL for Table FEUILLE_ROUTE
--------------------------------------------------------

  CREATE TABLE "FEUILLE_ROUTE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"DEMANDEVALIDATION" NUMBER(1,0), 
	"FEUILLEROUTEDEFAUT" NUMBER(1,0), 
	"MINISTERE" NVARCHAR2(50), 
	"TYPECREATION" NVARCHAR2(50), 
	"TYPEACTE" NVARCHAR2(50), 
	"DIRECTION" NVARCHAR2(50), 
	"NUMERO" NVARCHAR2(10)
   ) ;
--------------------------------------------------------
--  DDL for Table FICHE_LOI
--------------------------------------------------------

  CREATE TABLE "FICHE_LOI" 
   (	"ID" VARCHAR2(36 BYTE), 
	"DATEDECISION" TIMESTAMP (6), 
	"DATERECEPTION" TIMESTAMP (6), 
	"PROCEDUREACCELEREE" NUMBER(1,0), 
	"NUMERODEPOT" VARCHAR2(2000), 
	"INTITULE" NVARCHAR2(2000), 
	"NUMERONOR" NVARCHAR2(2000), 
	"DATEADOPTION" TIMESTAMP (6), 
	"OBSERVATION" NVARCHAR2(2000), 
	"IDDOSSIER" NVARCHAR2(2000), 
	"ASSEMBLEEDEPOT" NVARCHAR2(2000),
	"ARTICLE493" NUMBER(1,0), 
	"DATEJO" TIMESTAMP (6), 
	"DATELIMITEPROMULGATION" TIMESTAMP (6), 
	"DATEDEPOT" TIMESTAMP (6), 
	"DATECM" TIMESTAMP (6), 
	"DATESAISIECC" TIMESTAMP (6),
	"ECHEANCIERPROMULGATION" NUMBER(1,0),
    "DEPARTELYSEE" TIMESTAMP (6),
    "RETOURELYSEE" TIMESTAMP (6),
    "MINISTERERESP" NVARCHAR2(2000),
	"DATEPROJET" TIMESTAMP(6),
	"DATESECTIONCE" TIMESTAMP(6),
	"NUMEROISA" NVARCHAR2(2000),
	"DIFFUSION" NVARCHAR2(2000),
	"DIFFUSIONGENERALE" NVARCHAR2(2000),
	"TITREOFFICIEL" NVARCHAR2(2000),
	"REFUSENGAGEMENTPROCASS1" NVARCHAR2(2000),
	"DATEREFUSENGPROCASS1" TIMESTAMP(6),
	"REFUSENGAGEMENTPROCASS2" NVARCHAR2(2000),
	"DECISIONENGAGEMENTASSEMBLEE2" NVARCHAR2(2000)
   ) ;
   
CREATE TABLE "FLOI_NOMCOMPLETCHARGESMISSIONS"
(
    "ID"  VARCHAR2(36 BYTE),
    "POS" NUMBER(10,0),
    "ITEM" NVARCHAR2(2000),
    CONSTRAINT "FLOI_CM_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
    	REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE
);
--------------------------------------------------------
--  DDL for Table FICHE_PRESENTATION_DR
--------------------------------------------------------

  CREATE TABLE "FICHE_PRESENTATION_DR" 
   (	"ID" VARCHAR2(36 BYTE), 
	"ARTICLETEXTEREF" NVARCHAR2(2000), 
	"DESTINATAIRE2RAPPORT" NVARCHAR2(2000), 
	"NUMEROORDRE" NUMBER(19,0), 
	"CONVENTIONCALCUL" NVARCHAR2(2000), 
	"OBSERVATION" NVARCHAR2(2000), 
	"RUBRIQUE" NVARCHAR2(2000), 
	"DATEPUBLICATIONTEXTEREF" TIMESTAMP (6), 
	"LEGISLATURE" NVARCHAR2(2000), 
	"POLE" NVARCHAR2(2000), 
	"NATURE" NVARCHAR2(2000), 
	"DIRECTEURCM" NVARCHAR2(2000), 
	"CREATEURDEPOT" NVARCHAR2(2000), 
	"DAJMINISTERE" NVARCHAR2(2000), 
	"PERIODICITE" NVARCHAR2(2000), 
	"MINISTERES" NVARCHAR2(2000), 
	"NUMERODEPOTJOSENAT" NUMBER(19,0), 
	"IDDOSSIER" NVARCHAR2(2000), 
	"RESPONSABILITEELABORATION" NVARCHAR2(2000), 
	"RAPPORTSENAT" NUMBER(1,0), 
	"SESSION" NVARCHAR2(2000), 
	"DESTINATAIRE1RAPPORT" NVARCHAR2(2000), 
	"ANNEEDUPLIQUEE" NUMBER(19,0), 
	"TEXTEREF" NVARCHAR2(2000), 
	"DATERELANCESGG" TIMESTAMP (6), 
	"DATEDEPOTEFFECTIF" TIMESTAMP (6), 
	"OBJET" NVARCHAR2(2000), 
	"ACTIF" NUMBER(1,0)
   ) ;
--------------------------------------------------------
--  DDL for Table FICHE_PRESENTATION_IE
--------------------------------------------------------

  CREATE TABLE "FICHE_PRESENTATION_IE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"INTITULE" NVARCHAR2(2000), 
	"AUTEUR" NVARCHAR2(2000), 
	"OBSERVATION" NVARCHAR2(2000), 
	"DATE" TIMESTAMP (6), 
	"IDENTIFIANTPROPOSITION" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table FICHE_PRESENTATION_OEP
--------------------------------------------------------

  CREATE TABLE "FICHE_PRESENTATION_OEP" 
   (	"ID" VARCHAR2(36 BYTE), 
	"DATE" TIMESTAMP (6), 
	"MAIL" NVARCHAR2(2000), 
	"FAX" NVARCHAR2(2000), 
	"NOMORGANISME" NVARCHAR2(2000), 
	"TEXTEREF" NVARCHAR2(2000), 
	"TEL" NVARCHAR2(2000), 
	"MINISTERERATTACHEMENT" NVARCHAR2(2000), 
	"IDORGANISMEEPP" NVARCHAR2(2000), 
	"DUREEMANDATSE" NVARCHAR2(2000), 
	"DUREEMANDATAN" NVARCHAR2(2000), 
	"IDDOSSIER" NVARCHAR2(2000), 
	"ADRESSE" NVARCHAR2(2000),
	"INFORMERCHARGEMISSION" NUMBER(1,0),
    "NATUREJURIDIQUEORGANISME" NVARCHAR2(2000),
    "COMMISSIONDUDECRET2006" NUMBER(1,0),
    "ISSUIVIEDQD" NUMBER(1,0),
    "ISSUIVIEAPPSUIVIMANDATS" NUMBER(1,0),
    "MOTIFFINOEP" NVARCHAR2(2000),
    "RENOUVELABLEAN" NUMBER(1,0),
    "NBMANDATSAN" NUMBER(19,0),
    "RENOUVELABLESE" NUMBER(1,0),
    "NBMANDATSSE" NUMBER(19,0)
   ) ;
--------------------------------------------------------
--  DDL for Table FICHE_PRESENTATION_341
--------------------------------------------------------

  CREATE TABLE "FICHE_PRESENTATION_341" 
   (	"ID" VARCHAR2(36 BYTE), 
	"DATEDEPOT" TIMESTAMP (6), 
	"INTITULE" NVARCHAR2(2000), 
	"IDENTIFIANTPROPOSITION" NVARCHAR2(2000), 
	"OBJET" NVARCHAR2(2000), 
	"NUMERODEPOT" NVARCHAR2(2000), 
	"AUTEUR" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table FILE
--------------------------------------------------------

  CREATE TABLE "FILE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"FILENAME" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table FILE_SOLON_EPG
--------------------------------------------------------

  CREATE TABLE "FILE_SOLON_EPG" 
   (	"ID" VARCHAR2(36 BYTE), 
	"RELATEDDOCUMENT" NVARCHAR2(2000), 
	"ENTITE" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table FILE_SOLON_MGPP
--------------------------------------------------------

  CREATE TABLE "FILE_SOLON_MGPP" 
   (	"ID" VARCHAR2(36 BYTE), 
	"IDFICHE" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table FOND_DOSSIER_FOLDER_SOLON_EPG
--------------------------------------------------------

  CREATE TABLE "FOND_DOSSIER_FOLDER_SOLON_EPG" 
   (	"ID" VARCHAR2(36 BYTE), 
	"ISSUPPRIMABLE" NUMBER(1,0)
   ) ;
--------------------------------------------------------
--  DDL for Table FP341_COAUTEUR
--------------------------------------------------------

  CREATE TABLE "FP341_COAUTEUR" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table FPOEP_CHARGEMISSION
--------------------------------------------------------

CREATE TABLE "FPOEP_CHARGEMISSION" 
(	
    "ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
);
--------------------------------------------------------
--  DDL for Table FSD_DC_COVERAGE
--------------------------------------------------------

  CREATE TABLE "FSD_DC_COVERAGE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table FSD_DC_CREATOR
--------------------------------------------------------

  CREATE TABLE "FSD_DC_CREATOR" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table FSD_DC_NATURE
--------------------------------------------------------

  CREATE TABLE "FSD_DC_NATURE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table FSD_DC_SUBJECTS
--------------------------------------------------------

  CREATE TABLE "FSD_DC_SUBJECTS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table FSD_ECM_PATH
--------------------------------------------------------

  CREATE TABLE "FSD_ECM_PATH" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table FULLTEXT
--------------------------------------------------------

  CREATE TABLE "FULLTEXT" 
   (	"ID" VARCHAR2(36 BYTE), 
	"JOBID" VARCHAR2(250 BYTE), 
	"FULLTEXT" CLOB, 
	"SIMPLETEXT" NCLOB, 
	"BINARYTEXT" NCLOB, 
	"FULLTEXT_NOR" CLOB, 
	"SIMPLETEXT_NOR" NCLOB, 
	"BINARYTEXT_NOR" NCLOB
   ) ;

--------------------------------------------------------
--  DDL for Table HIERARCHY_READ_ACL
--------------------------------------------------------

  CREATE TABLE "HIERARCHY_READ_ACL" 
   (	"ID" VARCHAR2(36 BYTE), 
	"ACL_ID" VARCHAR2(34 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table INDEXATION_MOT_CLE_SOLON_EPG
--------------------------------------------------------

  CREATE TABLE "INDEXATION_MOT_CLE_SOLON_EPG" 
   (	"ID" VARCHAR2(36 BYTE), 
	"AUTHOR" NVARCHAR2(2000), 
	"INTITULE" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table INDEXATION_RUBRIQUE_SOLON_EPG
--------------------------------------------------------

  CREATE TABLE "INDEXATION_RUBRIQUE_SOLON_EPG" 
   (	"ID" VARCHAR2(36 BYTE), 
	"INTITULE" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table INFO_COMMENTS
--------------------------------------------------------

  CREATE TABLE "INFO_COMMENTS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"NUMBEROFCOMMENTS" NUMBER(19,0)
   ) ;
--------------------------------------------------------
--  DDL for Table INFOEPREUVE
--------------------------------------------------------

  CREATE TABLE "INFOEPREUVE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"EPREUVEDESTINATAIREENTETE" NVARCHAR2(2000), 
	"EPREUVEPOURLE" TIMESTAMP (6), 
	"EPREUVEDEMANDELE" TIMESTAMP (6), 
	"EPREUVENUMEROLISTE" NVARCHAR2(2000), 
	"EPREUVEENVOIRELECTURELE" TIMESTAMP (6), 
	"EPREUVENOMSSIGNATAIRES" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table INFOHISTORIQUEAMPLIATION
--------------------------------------------------------

  CREATE TABLE "INFOHISTORIQUEAMPLIATION" 
   (	"ID" VARCHAR2(36 BYTE), 
	"DATEENVOIAMPLIATION" TIMESTAMP (6)
   ) ;
--------------------------------------------------------
--  DDL for Table INFONUMEROLISTESIGNATURE
--------------------------------------------------------

  CREATE TABLE "INFONUMEROLISTESIGNATURE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"DATEGENERATIONLISTESIGNATURE" TIMESTAMP (6), 
	"NUMEROLISTESIGNATURE" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table INFO_UTILISATEUR_CONNECTION
--------------------------------------------------------

  CREATE TABLE "INFO_UTILISATEUR_CONNECTION" 
   (	"ID" VARCHAR2(36 BYTE), 
	"USERNAME" NVARCHAR2(2000), 
	"ISLOGOUT" NUMBER(1,0), 
	"FIRSTNAME" NVARCHAR2(2000), 
	"DATECONNECTION" TIMESTAMP (6), 
	"LASTNAME" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table ITEM#ANONYMOUSTYPE
--------------------------------------------------------

  CREATE TABLE "ITEM#ANONYMOUSTYPE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"IDFEUILLESTYLE" NVARCHAR2(2000), 
	"FILENAME" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table JETON_DOC
--------------------------------------------------------

  CREATE TABLE "JETON_DOC" 
   (	"ID" VARCHAR2(36 BYTE), 
	"TYPE_WEBSERVICE" NVARCHAR2(2000), 
	"ID_JETON" NUMBER(19,0), 
	"ID_OWNER" NVARCHAR2(2000), 
	"ID_DOC" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table JETON_MAITRE
--------------------------------------------------------

  CREATE TABLE "JETON_MAITRE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"ID_PROPRIETAIRE" NVARCHAR2(2000), 
	"TYPE_WEBSERVICE" NVARCHAR2(2000), 
	"NUMERO_JETON" NUMBER(19,0)
   ) ;
   
--------------------------------------------------------
--  DDL for Table LOCK_JETON_MAITRE
--------------------------------------------------------

  CREATE TABLE "LOCK_JETON_MAITRE" 
   (	"ID" VARCHAR2(36 BYTE), 
   	"ID_JETON_MAITRE" VARCHAR2(36 BYTE),
	"ID_PROPRIETAIRE" NVARCHAR2(2000), 
	"TYPE_WEBSERVICE" NVARCHAR2(2000), 
	"NUMERO_JETON" NUMBER(19,0)
   ) ;
--------------------------------------------------------
--  DDL for Table LIGNEPROGRAMMATION
--------------------------------------------------------

  CREATE TABLE "LIGNEPROGRAMMATION" 
   (	"ID" VARCHAR2(36 BYTE), 
	"ARTICLELOI" NVARCHAR2(2000), 
	"CATEGORIETEXTE" NVARCHAR2(2000), 
	"OBSERVATION" NVARCHAR2(2000), 
	"NUMEROORDRE" NVARCHAR2(2000), 
	"CONSULTATIONOHCE" NVARCHAR2(2000), 
	"BASELEGALE" NVARCHAR2(2000), 
	"DATESAISINECE" TIMESTAMP (6), 
	"DATEPREVISIONNELLESAISINECE" TIMESTAMP (6), 
	"OBJECTIFPUBLICATION" TIMESTAMP (6), 
	"DIRECTIONRESPONSABLE" NVARCHAR2(2000), 
	"CALENDRIERCOHCE" NVARCHAR2(2000), 
	"OBJET" NVARCHAR2(2000), 
	"MINISTEREPILOTE" NVARCHAR2(2000), 
	"TYPEMESURE" NVARCHAR2(2000),
	"TYPETABLEAU" VARCHAR2(50 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table LIGNEPROGRAMMATIONHAB
--------------------------------------------------------

  CREATE TABLE "LIGNEPROGRAMMATIONHAB" 
   (	"ID" VARCHAR2(36 BYTE), 
	"OBJETORDO" NVARCHAR2(2000), 
	"LEGISLATUREORDO" NVARCHAR2(2000), 
	"OBSERVATION" NVARCHAR2(2000), 
	"NUMEROORDRE" NVARCHAR2(2000), 
	"DATEPREVISIONNELLESAISINECE" TIMESTAMP (6), 
	"CONVENTION" NVARCHAR2(2000), 
	"CODIFICATION" NUMBER(1,0), 
	"LEGISLATURE" NVARCHAR2(2000), 
	"DATETERME" TIMESTAMP (6), 
	"OBJETRIM" NVARCHAR2(2000), 
	"TITREOFFICIELORDO" NVARCHAR2(2000), 
	"ARTICLE" NVARCHAR2(2000), 
	"MINISTEREPILOTE" NVARCHAR2(2000), 
	"NUMEROORDO" NVARCHAR2(2000), 
	"MINISTEREPILOTEORDO" NVARCHAR2(2000), 
	"CONVENTIONDEPOTORDO" NVARCHAR2(2000), 
	"DATEPUBLICATIONORDO" TIMESTAMP (6), 
	"DATEPREVISIONNELLECM" TIMESTAMP (6), 
	"DATEPASSAGECMORDO" TIMESTAMP (6), 
	"DATESAISINECEORDO" TIMESTAMP (6), 
	"DATELIMITEDEPOTORDO" TIMESTAMP (6), 
	"NUMERONORORDO" NVARCHAR2(2000), 
	"CONVENTIONDEPOT" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table LIS_IDSDOSSIER
--------------------------------------------------------

  CREATE TABLE "LIS_IDSDOSSIER" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" VARCHAR2(50 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table LISTE_TRAITEMENT_PAPI_144E75F6
--------------------------------------------------------

  CREATE TABLE "LISTE_TRAITEMENT_PAPI_144E75F6" 
   (	"ID" VARCHAR2(36 BYTE), 
	"NUMEROLISTE" NUMBER(19,0), 
	"DATEGENERATIONLISTE" TIMESTAMP (6), 
	"TYPELISTE" NVARCHAR2(2), 
	"TYPESIGNATURE" NVARCHAR2(5)
   ) ;
--------------------------------------------------------
--  DDL for Table LOCALTHEMECONFIG
--------------------------------------------------------

  CREATE TABLE "LOCALTHEMECONFIG" 
   (	"ID" NUMBER(10,0), 
	"DOCID" VARCHAR2(255 CHAR), 
	"ENGINE" VARCHAR2(255 CHAR), 
	"THEMODE" VARCHAR2(255 CHAR), 
	"PAGE" VARCHAR2(255 CHAR), 
	"PERSPECTIVE" VARCHAR2(255 CHAR), 
	"THEME" VARCHAR2(255 CHAR)
   ) ;
--------------------------------------------------------
--  DDL for Table LOCKS
--------------------------------------------------------

  CREATE TABLE "LOCKS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"OWNER" VARCHAR2(250 BYTE), 
	"CREATED" TIMESTAMP (6)
   ) ;
--------------------------------------------------------
--  DDL for Table MAIL
--------------------------------------------------------

  CREATE TABLE "MAIL" 
   (	"ID" VARCHAR2(36 BYTE), 
	"HTMLTEXT" NVARCHAR2(2000), 
	"MESSAGEID" NVARCHAR2(2000), 
	"TEXT" NVARCHAR2(2000), 
	"SENDER" NVARCHAR2(2000), 
	"SENDING_DATE" TIMESTAMP (6)
   ) ;
--------------------------------------------------------
--  DDL for Table MAILBOX
--------------------------------------------------------

  CREATE TABLE "MAILBOX" 
   (	"ID" VARCHAR2(36 BYTE), 
	"ORIGIN" NVARCHAR2(2000), 
	"LASTSYNCUPDATE" TIMESTAMP (6), 
	"DEFAULTCONFIDENTIALITY" NUMBER(19,0), 
	"TYPE" NVARCHAR2(2000), 
	"SYNCHRONIZERID" NVARCHAR2(2000), 
	"AFFILIATED_MAILBOX_ID" NVARCHAR2(2000), 
	"OWNER" NVARCHAR2(2000), 
	"SYNCHRONIZEDSTATE" NVARCHAR2(2000), 
	"MAILBOX_ID" NVARCHAR2(100)
   ) ;
--------------------------------------------------------
--  DDL for Table MAILBOX_ID
--------------------------------------------------------

  CREATE TABLE "MAILBOX_ID" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table MAILBOX_SOLON_EPG
--------------------------------------------------------

  CREATE TABLE "MAILBOX_SOLON_EPG" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POURSIGNATURE" NUMBER(19,0), 
	"POURTRANSMISSIONAUXASSEMBLEES" NUMBER(19,0), 
	"POURREORIENTATION" NUMBER(19,0), 
	"POURATTRIBUTION" NUMBER(19,0), 
	"POURREDACTION" NUMBER(19,0), 
	"POURIMPRESSION" NUMBER(19,0), 
	"POURREDACTIONINTERFACEE" NUMBER(19,0), 
	"POURREATTRIBUTION" NUMBER(19,0), 
	"POURVALIDATIONPM" NUMBER(19,0), 
	"POURAVIS" NUMBER(19,0), 
	"POURINFORMATION" NUMBER(19,0), 
	"POURARBITRAGE" NUMBER(19,0)
   ) ;
--------------------------------------------------------
--  DDL for Table MAIL_CC_RECIPIENTS
--------------------------------------------------------

  CREATE TABLE "MAIL_CC_RECIPIENTS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table MAIL_RECIPIENTS
--------------------------------------------------------

  CREATE TABLE "MAIL_RECIPIENTS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table MGPP_INFO_CORBEILLE
--------------------------------------------------------

   CREATE TABLE MGPP_INFO_CORBEILLE 
   (	"CORBEILLE" VARCHAR2(2000) NOT NULL,
	"HASMESS" NUMBER(1,0),
	CONSTRAINT MGPP_INFO_CORB_PK PRIMARY KEY ( CORBEILLE ) ENABLE
	);
--------------------------------------------------------
--  DDL for Table MFDDR_FORMATAUTORISE
--------------------------------------------------------

  CREATE TABLE "MFDDR_FORMATAUTORISE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(50)
   ) ;
--------------------------------------------------------
--  DDL for Table MINISTEREIDS
--------------------------------------------------------

  CREATE TABLE "MINISTEREIDS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table MISC
--------------------------------------------------------

  CREATE TABLE "MISC" 
   (	"ID" VARCHAR2(36 BYTE), 
	"LIFECYCLEPOLICY" VARCHAR2(250 BYTE), 
	"LIFECYCLESTATE" VARCHAR2(250 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table MLBX_FAVORITES
--------------------------------------------------------

  CREATE TABLE "MLBX_FAVORITES" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table MLBX_GROUPS
--------------------------------------------------------

  CREATE TABLE "MLBX_GROUPS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(100)
   ) ;
--------------------------------------------------------
--  DDL for Table MLBX_NOTIFIED_USERS
--------------------------------------------------------

  CREATE TABLE "MLBX_NOTIFIED_USERS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table MLBX_PROFILES
--------------------------------------------------------

  CREATE TABLE "MLBX_PROFILES" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table MLBX_USERS
--------------------------------------------------------

  CREATE TABLE "MLBX_USERS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table MODELE_FOND_DOSSIER_SOLON_EPG
--------------------------------------------------------

  CREATE TABLE "MODELE_FOND_DOSSIER_SOLON_EPG" 
   (	"ID" VARCHAR2(36 BYTE), 
	"TYPEACTE" NVARCHAR2(10)
   ) ;
--------------------------------------------------------
--  DDL for Table MODELE_PARAPHEUR_SOLON_EPG
--------------------------------------------------------

  CREATE TABLE "MODELE_PARAPHEUR_SOLON_EPG" 
   (	"ID" VARCHAR2(36 BYTE), 
	"TYPEACTE" NVARCHAR2(10)
   ) ;
--------------------------------------------------------
--  DDL for Table MOTS_CLES
--------------------------------------------------------

  CREATE TABLE "MOTS_CLES" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table NAVETTE
--------------------------------------------------------

  CREATE TABLE "NAVETTE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"SORTADOPTION" NVARCHAR2(2000), 
	"DATETRANSMISSION" TIMESTAMP (6), 
	"NUMEROTEXTE" NVARCHAR2(2000), 
	"NIVEAULECTURE" NUMBER(19,0), 
	"CODELECTURE" NVARCHAR2(2000), 
	"DATEADOPTION" TIMESTAMP (6), 
	"DATEREUNION" TIMESTAMP (6),
	"RESULTATCMP" NVARCHAR2(2000),
	"SORTADOPTIONAN" NVARCHAR2(2000),
	"SORTADOPTIONSE" NVARCHAR2(2000),
	"DATEADOPTIONAN" TIMESTAMP(6),
	"DATEADOPTIONSE" TIMESTAMP(6)
   ) ;
   
   CREATE TABLE "NAV_DATECMP"
(
	"ID" VARCHAR2(36) NOT NULL ENABLE,
	"POS" NUMBER(10,0),
	"ITEM" TIMESTAMP (6),
 	CONSTRAINT "NAV_DATECMP_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
  		REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE
);
--------------------------------------------------------
--  DDL for Table NAVIGATIONSETTINGS
--------------------------------------------------------

  CREATE TABLE "NAVIGATIONSETTINGS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"HOMEPAGE" NVARCHAR2(2000), 
	"PAGINATION" NVARCHAR2(2000), 
	"FULLNAVIGATION" NUMBER(1,0)
   ) ;
--------------------------------------------------------
--  DDL for Table NOTE
--------------------------------------------------------

  CREATE TABLE "NOTE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"MIME_TYPE" NVARCHAR2(2000), 
	"NOTE" NCLOB
   ) ;
--------------------------------------------------------
--  DDL for Table NXP_LOGS
--------------------------------------------------------

  CREATE TABLE "NXP_LOGS" 
   (	"LOG_ID" NUMBER(*,0), 
	"LOG_EVENT_CATEGORY" VARCHAR2(255 CHAR), 
	"LOG_EVENT_COMMENT" VARCHAR2(1024 CHAR), 
	"LOG_DOC_LIFE_CYCLE" VARCHAR2(255 CHAR), 
	"LOG_DOC_PATH" VARCHAR2(1024 CHAR), 
	"LOG_DOC_TYPE" VARCHAR2(255 CHAR), 
	"LOG_DOC_UUID" VARCHAR2(255 CHAR), 
	"LOG_EVENT_DATE" TIMESTAMP (6), 
	"LOG_EVENT_ID" VARCHAR2(255 CHAR), 
	"LOG_PRINCIPAL_NAME" VARCHAR2(255 CHAR)
   ) ;
--------------------------------------------------------
--  DDL for Table NXP_LOGS_EXTINFO
--------------------------------------------------------

  CREATE TABLE "NXP_LOGS_EXTINFO" 
   (	"DISCRIMINATOR" VARCHAR2(31 CHAR), 
	"LOG_EXTINFO_ID" NUMBER(19,0), 
	"LOG_EXTINFO_STRING" VARCHAR2(255 CHAR), 
	"LOG_EXTINFO_BOOLEAN" NUMBER(1,0), 
	"LOG_EXTINFO_DOUBLE" FLOAT(126), 
	"LOG_EXTINFO_LONG" NUMBER(19,0), 
	"LOG_EXTINFO_DATE" TIMESTAMP (6), 
	"LOG_EXTINFO_BLOB" BLOB
   ) ;
--------------------------------------------------------
--  DDL for Table NXP_LOGS_MAPEXTINFOS
--------------------------------------------------------

  CREATE TABLE "NXP_LOGS_MAPEXTINFOS" 
   (	"LOG_FK" NUMBER(*,0), 
	"INFO_FK" NUMBER(19,0), 
	"MAPKEY" VARCHAR2(255 CHAR)
   ) ;
--------------------------------------------------------
--  DDL for Table NXP_UIDSEQ
--------------------------------------------------------

  CREATE TABLE "NXP_UIDSEQ" 
   (	"SEQ_ID" NUMBER(10,0), 
	"SEQ_INDEX" NUMBER(10,0), 
	"SEQ_KEY" VARCHAR2(255 CHAR)
   ) ;
--------------------------------------------------------
--  DDL for Table PA_METADISPOCORBEILLE
--------------------------------------------------------

  CREATE TABLE "PA_METADISPOCORBEILLE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table PARAMETER
--------------------------------------------------------

  CREATE TABLE "PARAMETER" 
   (	"ID" VARCHAR2(36 BYTE), 
	"PVALUE" NVARCHAR2(2000), 
	"PNAME" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table PARAMETRAGE_APPLICATION
--------------------------------------------------------

  CREATE TABLE "PARAMETRAGE_APPLICATION" 
   (	"ID" VARCHAR2(36 BYTE), 
	"NBDOSSIERSSIGNALES" NUMBER(19,0), 
	"NBTABLEAUXDYNAMIQUES" NUMBER(19,0), 
	"NBDERNIERSRESULTATS" NUMBER(19,0), 
	"NBFAVORISCONSULTATION" NUMBER(19,0), 
	"DELAIENVOIMAILALERTE" NUMBER(19,0), 
	"NBFAVORISRECHERCHE" NUMBER(19,0), 
	"NBDOSSIERPAGE" NUMBER(19,0), 
	"DELAIRAFRAICHISSEMENTCORBEILLE" NUMBER(19,0),
	"DELAIAFFICHAGEMESSAGE" NUMBER(19,0)
   ) ;
--------------------------------------------------------
--  DDL for Table PARAMETRE
--------------------------------------------------------

  CREATE TABLE "PARAMETRE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"UNIT" NVARCHAR2(2000), 
	"VALUE" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table PARAPHEUR_FOLDER_SOLON_EPG
--------------------------------------------------------

  CREATE TABLE "PARAPHEUR_FOLDER_SOLON_EPG" 
   (	"ID" VARCHAR2(36 BYTE), 
	"NBDOCACCEPTEMAX" NUMBER(19,0), 
	"ESTNONVIDE" NUMBER(1,0)
   ) ;
--------------------------------------------------------
--  DDL for Table PARTICIPANTLIST
--------------------------------------------------------

  CREATE TABLE "PARTICIPANTLIST" 
   (	"ID" VARCHAR2(36 BYTE), 
	"TITLE" NVARCHAR2(2000), 
	"DESCRIPTION" NVARCHAR2(2000), 
	"PLID" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table PARUTIONBO
--------------------------------------------------------

  CREATE TABLE "PARUTIONBO" 
   (	"ID" VARCHAR2(36 BYTE), 
	"NUMEROTEXTEPARUTIONBO" NVARCHAR2(2000), 
	"PAGEPARUTIONBO" NUMBER(19,0), 
	"DATEPARUTIONBO" TIMESTAMP (6)
   ) ;
--------------------------------------------------------
--  DDL for Table PF_FORMATAUTORISE
--------------------------------------------------------

  CREATE TABLE "PF_FORMATAUTORISE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(10)
   ) ;
--------------------------------------------------------
--  DDL for Table PROFIL_UTILISATEUR_SOLON_EPG
--------------------------------------------------------

  CREATE TABLE "PROFIL_UTILISATEUR_SOLON_EPG" 
   (	"ID" VARCHAR2(36 BYTE),
	"NOTIFICATIONDOSSIERURGENT" NUMBER(1,0),
	"NOTIFICATIONRETOURSGG" NUMBER(1,0),
	"NBDOSSIERSVISIBLESMAX" NUMBER(19,0),
	"NOTIFICATIONMAILSIFRA_CCA7CDBC" NUMBER(1,0),
	"NOTIFICATIONMESURENOMINATIVE" NUMBER(1,0),
	"POSTEDEFAUT" NUMBER(38,0),
	"PRESENCELIVEEDIT" NUMBER(1,0);
   );
--------------------------------------------------------
--  DDL for Table PROTOCOL
--------------------------------------------------------

  CREATE TABLE "PROTOCOL" 
   (	"ID" VARCHAR2(36 BYTE), 
	"EMAIL" NVARCHAR2(2000), 
	"PORT" NVARCHAR2(2000), 
	"HOST" NVARCHAR2(2000), 
	"EMAILS_LIMIT" NUMBER(19,0), 
	"PASSWORD" NVARCHAR2(2000), 
	"SOCKET_FACTORY_FALLBACK" NUMBER(1,0), 
	"SOCKET_FACTORY_PORT" NVARCHAR2(2000), 
	"SSL_PROTOCOLS" NVARCHAR2(2000), 
	"STARTTLS_ENABLE" NUMBER(1,0), 
	"PROTOCOL_TYPE" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table PROXIES
--------------------------------------------------------

  CREATE TABLE "PROXIES" 
   (	"ID" VARCHAR2(36 BYTE), 
	"TARGETID" VARCHAR2(36 BYTE), 
	"VERSIONABLEID" VARCHAR2(36 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table PUBLISH_SECTIONS
--------------------------------------------------------

  CREATE TABLE "PUBLISH_SECTIONS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table PUSR_IDCOLONNEESPACET_934A8665
--------------------------------------------------------

  CREATE TABLE "PUSR_IDCOLONNEESPACET_934A8665" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(100)
   ) ;
--------------------------------------------------------
--  DDL for Table PUSR_IDCOLONNEINSTANC_3FED9D1F
--------------------------------------------------------

  CREATE TABLE "PUSR_IDCOLONNEINSTANC_3FED9D1F" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(100)
   ) ;
--------------------------------------------------------
--  DDL for Table PUSR_NOTIFICATIONTYPEACTES
--------------------------------------------------------

  CREATE TABLE "PUSR_NOTIFICATIONTYPEACTES" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(10)
   ) ;
   
-------------------------------------------------------------------------
------- DDL pour la table de paramétrage de l'activité normative
-------------------------------------------------------------------------
   CREATE TABLE "ACTIVITE_NORMATIVE_PARAMETRAGE" (
"ID" VARCHAR(36) NOT NULL, 
"EC_TP_PROMUL_LOIS_DEBUT" TIMESTAMP (6), 
"PRE_TP_PUBLI_DECRETS_FIN" TIMESTAMP (6),
"EC_BS_PUBLI_DECRETS_DEBUT" TIMESTAMP (6),
"PRE_TL_PROMUL_LOIS_FIN" TIMESTAMP (6),
"PRE_BS_PUBLI_DECRETS_DEBUT" TIMESTAMP (6),
"EC_BS_PROMUL_LOIS_FIN" TIMESTAMP (6),
"EC_TL_PROMUL_LOIS_FIN" TIMESTAMP (6),
"PRE_BS_PROMUL_LOIS_DEBUT" TIMESTAMP (6),
"EC_TL_PROMUL_LOIS_DEBUT" TIMESTAMP (6),
"PRE_TP_PUBLI_DECRETS_DEBUT" TIMESTAMP (6),
"EC_TP_PROMUL_LOIS_FIN" TIMESTAMP (6),
"EC_DEBUT_LEGISLATURE" TIMESTAMP (6),
"PRE_DEBUT_LEGISLATURE" TIMESTAMP (6),
"LEGIS_EC" VARCHAR(512), 
"LEGISLATURE_PUBLICATION" VARCHAR(512),
"PRE_TL_PROMUL_LOIS_DEBUT" TIMESTAMP (6), 
"EC_BS_PROMUL_LOIS_DEBUT" TIMESTAMP (6),
"PRE_TL_PUBLI_DECRETS_DEBUT" TIMESTAMP (6),
"PRE_BS_PROMUL_LOIS_FIN" TIMESTAMP (6), 
"EC_TL_PUBLI_DECRETS_FIN" TIMESTAMP (6), 
"PRE_TP_PROMUL_LOIS_DEBUT" TIMESTAMP (6), 
"PRE_TL_PUBLI_DECRETS_FIN" TIMESTAMP (6), 
"PRE_BS_PUBLI_DECRETS_FIN" TIMESTAMP (6), 
"EC_TL_PUBLI_DECRETS_DEBUT" TIMESTAMP (6), 
"EC_BS_PUBLI_DECRETS_FIN" TIMESTAMP (6),
"EC_TP_PUBLI_DECRETS_DEBUT" TIMESTAMP (6), 
"EC_TP_PUBLI_DECRETS_FIN" TIMESTAMP (6),
"PRE_TP_PROMUL_LOIS_FIN" TIMESTAMP (6));
CREATE TABLE "PARAN_LEGISLATURES" ("ID" VARCHAR(36) NOT NULL, "POS" INTEGER, "ITEM" VARCHAR(512));
ALTER TABLE "PARAN_LEGISLATURES" ADD CONSTRAINT "PARAN_LEGIS_ID_HIERARCHY_FK" FOREIGN KEY ("ID") REFERENCES "HIERARCHY" ON DELETE CASCADE;
CREATE INDEX "PARAN_LEGISLATURES_ID_IDX" ON "PARAN_LEGISLATURES" ("ID");

ALTER TABLE "ACTIVITE_NORMATIVE_PARAMETRAGE" ADD CONSTRAINT "AN_PARAMETRAGE_PK" PRIMARY KEY ("ID");
ALTER TABLE "ACTIVITE_NORMATIVE_PARAMETRAGE" ADD CONSTRAINT "AN_PARAMETRAGE_ID_HIERARCHY_FK" FOREIGN KEY ("ID") REFERENCES "HIERARCHY" ON DELETE CASCADE;
 
   
--------------------------------------------------------
-- QUARTZ
--------------------------------------------------------
	CREATE TABLE qrtz_job_details
	  (
	    SCHED_NAME VARCHAR(120) NOT NULL,
	    JOB_NAME  VARCHAR2(200) NOT NULL,
	    JOB_GROUP VARCHAR2(200) NOT NULL,
	    DESCRIPTION VARCHAR2(250) NULL,
	    JOB_CLASS_NAME   VARCHAR2(250) NOT NULL, 
	    IS_DURABLE VARCHAR2(1) NOT NULL,
	    IS_NONCONCURRENT VARCHAR2(1) NOT NULL,
	    IS_UPDATE_DATA VARCHAR2(1) NOT NULL,
	    REQUESTS_RECOVERY VARCHAR2(1) NOT NULL,
	    JOB_DATA BLOB NULL,
	    PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
	);
	
	CREATE TABLE qrtz_triggers
	  (
	    SCHED_NAME VARCHAR(120) NOT NULL,
	    TRIGGER_NAME VARCHAR2(200) NOT NULL,
	    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
	    JOB_NAME  VARCHAR2(200) NOT NULL, 
	    JOB_GROUP VARCHAR2(200) NOT NULL,
	    DESCRIPTION VARCHAR2(250) NULL,
	    NEXT_FIRE_TIME NUMBER(13) NULL,
	    PREV_FIRE_TIME NUMBER(13) NULL,
	    PRIORITY NUMBER(13) NULL,
	    TRIGGER_STATE VARCHAR2(16) NOT NULL,
	    TRIGGER_TYPE VARCHAR2(8) NOT NULL,
	    START_TIME NUMBER(13) NOT NULL,
	    END_TIME NUMBER(13) NULL,
	    CALENDAR_NAME VARCHAR2(200) NULL,
	    MISFIRE_INSTR NUMBER(2) NULL,
	    JOB_DATA BLOB NULL,
	    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
	    FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP) 
		REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP) 
	);
	
	CREATE TABLE qrtz_simple_triggers
	  (
	    SCHED_NAME VARCHAR(120) NOT NULL,
	    TRIGGER_NAME VARCHAR2(200) NOT NULL,
	    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
	    REPEAT_COUNT NUMBER(7) NOT NULL,
	    REPEAT_INTERVAL NUMBER(12) NOT NULL,
	    TIMES_TRIGGERED NUMBER(10) NOT NULL,
	    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
	    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
		REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
	);
	
	CREATE TABLE qrtz_cron_triggers
	  (
	    SCHED_NAME VARCHAR(120) NOT NULL,
	    TRIGGER_NAME VARCHAR2(200) NOT NULL,
	    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
	    CRON_EXPRESSION VARCHAR2(120) NOT NULL,
	    TIME_ZONE_ID VARCHAR2(80),
	    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
	    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
		REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
	);
	
	CREATE TABLE qrtz_simprop_triggers
	  (          
	    SCHED_NAME VARCHAR(120) NOT NULL,
	    TRIGGER_NAME VARCHAR(200) NOT NULL,
	    TRIGGER_GROUP VARCHAR(200) NOT NULL,
	    STR_PROP_1 VARCHAR(512) NULL,
	    STR_PROP_2 VARCHAR(512) NULL,
	    STR_PROP_3 VARCHAR(512) NULL,
	    INT_PROP_1 NUMBER(10) NULL,
	    INT_PROP_2 NUMBER(10) NULL,
	    LONG_PROP_1 NUMBER(13) NULL,
	    LONG_PROP_2 NUMBER(13) NULL,
	    DEC_PROP_1 NUMERIC(13,4) NULL,
	    DEC_PROP_2 NUMERIC(13,4) NULL,
	    BOOL_PROP_1 VARCHAR(1) NULL,
	    BOOL_PROP_2 VARCHAR(1) NULL,
	    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
	    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
	    REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
	);
	
	CREATE TABLE qrtz_blob_triggers
	  (
	    SCHED_NAME VARCHAR(120) NOT NULL,
	    TRIGGER_NAME VARCHAR2(200) NOT NULL,
	    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
	    BLOB_DATA BLOB NULL,
	    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
	    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
	        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
	);
	
	CREATE TABLE qrtz_calendars
	  (
	    SCHED_NAME VARCHAR(120) NOT NULL,
	    CALENDAR_NAME  VARCHAR2(200) NOT NULL, 
	    CALENDAR BLOB NOT NULL,
	    PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
	);
	
	CREATE TABLE qrtz_paused_trigger_grps
	  (
	    SCHED_NAME VARCHAR(120) NOT NULL,
	    TRIGGER_GROUP  VARCHAR2(200) NOT NULL, 
	    PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
	);
	
	CREATE TABLE qrtz_fired_triggers 
	  (
	    SCHED_NAME VARCHAR(120) NOT NULL,
	    ENTRY_ID VARCHAR2(95) NOT NULL,
	    TRIGGER_NAME VARCHAR2(200) NOT NULL,
	    TRIGGER_GROUP VARCHAR2(200) NOT NULL,
	    INSTANCE_NAME VARCHAR2(200) NOT NULL,
	    FIRED_TIME NUMBER(13) NOT NULL,
	    PRIORITY NUMBER(13) NOT NULL,
	    STATE VARCHAR2(16) NOT NULL,
	    JOB_NAME VARCHAR2(200) NULL,
	    JOB_GROUP VARCHAR2(200) NULL,
	    IS_NONCONCURRENT VARCHAR2(1) NULL,
	    REQUESTS_RECOVERY VARCHAR2(1) NULL,
	    PRIMARY KEY (SCHED_NAME,ENTRY_ID)
	);
	
	CREATE TABLE qrtz_scheduler_state 
	  (
	    SCHED_NAME VARCHAR(120) NOT NULL,
	    INSTANCE_NAME VARCHAR2(200) NOT NULL,
	    LAST_CHECKIN_TIME NUMBER(13) NOT NULL,
	    CHECKIN_INTERVAL NUMBER(13) NOT NULL,
	    PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
	);
	
	CREATE TABLE qrtz_locks
	  (
	    SCHED_NAME VARCHAR(120) NOT NULL,
	    LOCK_NAME  VARCHAR2(40) NOT NULL, 
	    PRIMARY KEY (SCHED_NAME,LOCK_NAME)
	);

--------------------------------------------------------
--  DDL for Table QUERYNAV
--------------------------------------------------------

  CREATE TABLE "QUERYNAV" 
   (	"ID" VARCHAR2(36 BYTE), 
	"SUBJECTS" NVARCHAR2(2000), 
	"COVERAGE" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table RELATEDTEXTRESOURCE
--------------------------------------------------------

  CREATE TABLE "RELATEDTEXTRESOURCE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"RELATEDTEXTID" NVARCHAR2(2000), 
	"RELATEDTEXT" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table RELATION
--------------------------------------------------------

  CREATE TABLE "RELATION" 
   (	"ID" VARCHAR2(36 BYTE), 
	"SOURCE" NVARCHAR2(2000), 
	"TARGET" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table RELATION_SEARCH
--------------------------------------------------------

  CREATE TABLE "RELATION_SEARCH" 
   (	"ID" VARCHAR2(36 BYTE), 
	"DC_TITLE" NVARCHAR2(2000), 
	"ECM_FULLTEXT" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table REL_SRCH_ECM_PATH
--------------------------------------------------------

  CREATE TABLE "REL_SRCH_ECM_PATH" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table REPOSITORIES
--------------------------------------------------------

  CREATE TABLE "REPOSITORIES" 
   (	"ID" VARCHAR2(36 BYTE), 
	"NAME" VARCHAR2(250 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table REPRESENTANT_OEP
--------------------------------------------------------

  CREATE TABLE "REPRESENTANT_OEP" 
   (	"ID" VARCHAR2(36 BYTE), 
	"TYPE" NVARCHAR2(2000), 
	"IDFROEP" NVARCHAR2(2000), 
	"DATEDEBUT" TIMESTAMP (6), 
	"DATEFIN" TIMESTAMP (6), 
	"REPRESENTANT" NVARCHAR2(2000), 
	"FONCTION" NVARCHAR2(2000),
	"NUMEROMANDAT" NUMBER(19,0),
    "AUTORITEDESIGNATION" NVARCHAR2(2000),
    "COMMISSIONSAISIE" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table REQUETE_DOSSIER
--------------------------------------------------------

  CREATE TABLE "REQUETE_DOSSIER" 
   (	"ID" VARCHAR2(36 BYTE), 
	"DANSBASEPRODUCTION" NUMBER(1,0), 
	"DANSBASEARCHIVAGEINTERMEDIAIRE" NUMBER(1,0), 
	"SENSIBILITECASSE" NUMBER(1,0)
   ) ;
--------------------------------------------------------
--  DDL for Table REQUETE_DOSSIER_SIMPL_A3A85FF2
--------------------------------------------------------

  CREATE TABLE "REQUETE_DOSSIER_SIMPL_A3A85FF2" 
   (	"ID" VARCHAR2(36 BYTE), 
	"INDEXATIONRUBRIQUE" NVARCHAR2(2000), 
	"DATEPUBLICATION_2" TIMESTAMP (6), 
	"DATESIGNATURE_2" TIMESTAMP (6), 
	"INDEXATIONMOTSCLEFS" NVARCHAR2(2000), 
	"DATEPUBLICATION_1" TIMESTAMP (6), 
	"IDCATEGORIEACTE" NVARCHAR2(2000), 
	"INDEXATIONCHAMPLIBRE" NVARCHAR2(2000), 
	"DATESIGNATURE_1" TIMESTAMP (6), 
	"APPLICATIONLOI" NVARCHAR2(2000), 
	"TRANSPOSITIONORDONNANCE" NVARCHAR2(2000), 
	"NUMEROTEXTE" NUMBER(19,0), 
	"TRANSPOSITIONDIRECTIVE" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table REQUETE_DOSSIER_SIMPL_C483C4B2
--------------------------------------------------------

  CREATE TABLE "REQUETE_DOSSIER_SIMPL_C483C4B2" 
   (	"ID" VARCHAR2(36 BYTE), 
	"NOTEMODIFIED" NVARCHAR2(2000), 
	"DATEACTIVATION_2" NVARCHAR2(2000), 
	"IDAUTEUR" NVARCHAR2(2000), 
	"DATEACTIVATION_1" TIMESTAMP (6), 
	"IDACTION" NVARCHAR2(2000), 
	"IDSTATUT" NVARCHAR2(2000), 
	"DISTRIBUTIONMAILBOXID" NVARCHAR2(2000), 
	"CURRENTLIFECYCLESTATE" NVARCHAR2(2000), 
	"NOTE" NVARCHAR2(2000), 
	"IDPOSTE" NVARCHAR2(2000), 
	"VALIDATIONSTATUS" NVARCHAR2(2000), 
	"DATEVALIDATION_2" NVARCHAR2(2000), 
	"DATEVALIDATION_1" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table REQUETE_DOSSIER_SIMPL_7B2FB691
--------------------------------------------------------

  CREATE TABLE "REQUETE_DOSSIER_SIMPL_7B2FB691" 
   (	"ID" VARCHAR2(36 BYTE), 
	"OBJET" NVARCHAR2(2000), 
	"IDSTATUTDOSSIER" NVARCHAR2(2000), 
	"NUMERONOR" NVARCHAR2(2000), 
	"NUMERONORMODIFIED" NVARCHAR2(2000), 
	"IDTYPEACTE" NVARCHAR2(2000), 
	"IDMINISTERERESPONSABLE" NVARCHAR2(2000), 
	"OBJETMODIFIE" NVARCHAR2(2000), 
	"IDDIRECTIONRESPONSABLE" NVARCHAR2(2000), 
	"IDAUTEUR" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table REQUETE_DOSSIER_SIMPL_743D2E6F
--------------------------------------------------------

  CREATE TABLE "REQUETE_DOSSIER_SIMPL_743D2E6F" 
   (	"ID" VARCHAR2(36 BYTE), 
	"TEXTEINTEGRAL" NVARCHAR2(2000), 
	"DANSEXTRAIT" NUMBER(1,0), 
	"DANSFONDDOSSIER" NUMBER(1,0), 
	"DANSACTE" NUMBER(1,0), 
	"SOUSCLAUSETEXTEINTEGRAL" NVARCHAR2(2000), 
	"DANSAUTREPIECEPARAPHEUR" NUMBER(1,0)
   ) ;
--------------------------------------------------------
--  DDL for Table RESCON_DOSSIERSIDS
--------------------------------------------------------

  CREATE TABLE "RESCON_DOSSIERSIDS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" VARCHAR2(50 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table RESCON_FDRSIDS
--------------------------------------------------------

  CREATE TABLE "RESCON_FDRSIDS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" VARCHAR2(50 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table RESCON_USERSNAMES
--------------------------------------------------------

  CREATE TABLE "RESCON_USERSNAMES" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" VARCHAR2(100 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table RETOUR_DILA
--------------------------------------------------------

  CREATE TABLE "RETOUR_DILA" 
   (	"ID" VARCHAR2(36 BYTE), 
	"DATEPARUTIONJORF" TIMESTAMP (6), 
	"NUMEROTEXTEPARUTIONJORF" NVARCHAR2(2000), 
	"LEGISLATUREPUBLICATION" NVARCHAR2(2000), 
	"TITREOFFICIEL" NVARCHAR2(2000), 
	"PAGEPARUTIONJORF" NUMBER(19,0), 
	"ISPUBLICATIONEPREUVAG_8742CC78" NUMBER(1,0), 
	"DATEPROMULGATION" TIMESTAMP (6), 
	"MODEPARUTION" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table ROUTING_TASK
--------------------------------------------------------

  CREATE TABLE "ROUTING_TASK" 
   (	"ID" VARCHAR2(36 BYTE), 
	"AUTOMATICVALIDATION" NUMBER(1,0), 
	"NUMEROVERSION" NUMBER(19,0), 
	"POSTELABEL" NVARCHAR2(2000), 
	"VALIDATIONSTATUS" NVARCHAR2(2000), 
	"MINISTERELABEL" NVARCHAR2(2000),
	"DIRECTIONLABEL" NVARCHAR2(2000),
	"ALREADYDUPLICATED" NUMBER(1,0), 
	"DISTRIBUTIONMAILBOXID" NVARCHAR2(2000), 
	"TYPE" NVARCHAR2(2000), 
	"DATEFINETAPE" TIMESTAMP (6), 
	"DUEDATE" TIMESTAMP (6), 
	"ISMAILSEND" NUMBER(1,0), 
	"ACTIONNABLE" NUMBER(1,0), 
	"DEADLINE" NUMBER(19,0), 
	"VALIDATIONUSERLABEL" NVARCHAR2(2000), 
	"VALIDATIONUSERID" NVARCHAR2(2000), 
	"OBLIGATOIREMINISTERE" NUMBER(1,0), 
	"AUTOMATICVALIDATED" NUMBER(1,0), 
	"DATEDEBUTETAPE" TIMESTAMP (6), 
	"OBLIGATOIRESGG" NUMBER(1,0), 
	"DOCUMENTROUTEID" VARCHAR2(50 BYTE)
   ) ;
--------------------------------------------------------
--  DDL for Table RQDCP_IDSTATUTSARCHIV_82ED246B
--------------------------------------------------------

  CREATE TABLE "RQDCP_IDSTATUTSARCHIV_82ED246B" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table SEARCH_COVERAGE
--------------------------------------------------------

  CREATE TABLE "SEARCH_COVERAGE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table SEARCH_CURRENTLIFECYCLESTATES
--------------------------------------------------------

  CREATE TABLE "SEARCH_CURRENTLIFECYCLESTATES" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table SEARCH_NATURE
--------------------------------------------------------

  CREATE TABLE "SEARCH_NATURE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table SEARCH_SEARCHPATH
--------------------------------------------------------

  CREATE TABLE "SEARCH_SEARCHPATH" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table SEARCH_SUBJECTS
--------------------------------------------------------

  CREATE TABLE "SEARCH_SUBJECTS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table SEMAINE_PARLEMENTAIRE
--------------------------------------------------------

  CREATE TABLE "SEMAINE_PARLEMENTAIRE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"IDDOSSIER" NVARCHAR2(2000), 
	"DATEDEBUT" TIMESTAMP (6), 
	"INTITULELOI" NVARCHAR2(2000), 
	"DATEFIN" TIMESTAMP (6), 
	"ASSEMBLEE" NVARCHAR2(2000), 
	"ORIENTATION" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table SMART_FOLDER
--------------------------------------------------------

  CREATE TABLE "SMART_FOLDER" 
   (	"ID" VARCHAR2(36 BYTE), 
	"QUERYPART" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table SORTINFOTYPE
--------------------------------------------------------

  CREATE TABLE "SORTINFOTYPE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"SORTCOLUMN" NVARCHAR2(2000), 
	"SORTASCENDING" NUMBER(1,0)
   ) ;
--------------------------------------------------------
--  DDL for Table STATUS
--------------------------------------------------------

  CREATE TABLE "STATUS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"SERVICEID" NVARCHAR2(2000), 
	"ADMINISTRATIVE_STATUS" NVARCHAR2(2000), 
	"INSTANCEID" NVARCHAR2(2000), 
	"USERLOGIN" NVARCHAR2(2000), 
	"STATUSMESSAGE" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table STEP_FOLDER
--------------------------------------------------------

  CREATE TABLE "STEP_FOLDER" 
   (	"ID" VARCHAR2(36 BYTE), 
	"EXECUTION" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table subtopic
--------------------------------------------------------

  CREATE TABLE "subtopic" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"parent" NVARCHAR2(2000), 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TABDYN_DESTINATAIRESID
--------------------------------------------------------

  CREATE TABLE "TABDYN_DESTINATAIRESID" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TABDYN_USERSNAMES
--------------------------------------------------------

  CREATE TABLE "TABDYN_USERSNAMES" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TABLEAU_DYNAMIQUE
--------------------------------------------------------

  CREATE TABLE "TABLEAU_DYNAMIQUE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"TYPE" NVARCHAR2(2000), 
	"QUERY" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TABLE_REFERENCE
--------------------------------------------------------

  CREATE TABLE "TABLE_REFERENCE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"MINISTEREPM" NVARCHAR2(2000), 
	"MINISTERECE" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TAG
--------------------------------------------------------

  CREATE TABLE "TAG" 
   (	"ID" VARCHAR2(36 BYTE), 
	"LABEL" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TBREF_CABINETPM
--------------------------------------------------------

  CREATE TABLE "TBREF_CABINETPM" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TBREF_CHARGESMISSION
--------------------------------------------------------

  CREATE TABLE "TBREF_CHARGESMISSION" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TBREF_DIRECTIONPM
--------------------------------------------------------

  CREATE TABLE "TBREF_DIRECTIONPM" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
   
--------------------------------------------------------
--  DDL for Table TBREF_RETOURDAN
--------------------------------------------------------

  CREATE TABLE "TBREF_RETOURDAN" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
   
--------------------------------------------------------
--  DDL for Table TBREF_SIGNATAIRES
--------------------------------------------------------

  CREATE TABLE "TBREF_SIGNATAIRES" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TBREF_SIGNATURECPM
--------------------------------------------------------

  CREATE TABLE "TBREF_SIGNATURECPM" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TBREF_SIGNATURESGG
--------------------------------------------------------

  CREATE TABLE "TBREF_SIGNATURESGG" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
   
--------------------------------------------------------
--  DDL for Table TBREF_VECTEURSPUBLICATIONS
--------------------------------------------------------

  CREATE TABLE "TBREF_VECTEURSPUBLICATIONS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
   
   --------------------------------------------------------
	--  DDL for Table TBREF_TYPEACTE
	--------------------------------------------------------
   CREATE TABLE "TBREF_TYPEACTE" 
   (	"ID" VARCHAR2(36 BYTE) NOT NULL, 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
   CREATE INDEX "TBREF_TYPEACTE_ID_IDX" ON "TBREF_TYPEACTE" ("ID") ;
--------------------------------------------------------
--  DDL for Table TEXM_DECRETIDS
--------------------------------------------------------

  CREATE TABLE "TEXM_DECRETIDS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TEXM_DOSSIERSNOR
--------------------------------------------------------

  CREATE TABLE "TEXM_DOSSIERSNOR" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TEXM_HABILITATIONIDS
--------------------------------------------------------

  CREATE TABLE "TEXM_HABILITATIONIDS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TEXM_LOIRATIFICATIONIDS
--------------------------------------------------------

  CREATE TABLE "TEXM_LOIRATIFICATIONIDS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TEXM_MESUREIDS
--------------------------------------------------------

  CREATE TABLE "TEXM_MESUREIDS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TEXM_MESURESAPPLICATIVESIDS
--------------------------------------------------------

  CREATE TABLE "TEXM_MESURESAPPLICATIVESIDS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TEXM_ORDONNANCEIDS
--------------------------------------------------------

  CREATE TABLE "TEXM_ORDONNANCEIDS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TEXM_TRANSPOSITIONIDS
--------------------------------------------------------

  CREATE TABLE "TEXM_TRANSPOSITIONIDS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TEXTE_MAITRE
--------------------------------------------------------

  CREATE TABLE "TEXTE_MAITRE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"DATEPREVISIONNELLECM" TIMESTAMP (6), 
	"LEGISLATURE" NVARCHAR2(2000), 
	"TERMEDEPOT" NVARCHAR2(2000), 
	"PROCEDUREVOTE" NVARCHAR2(2000), 
	"NUMEROJOPUBLICATION" NVARCHAR2(2000), 
	"DIRECTIONRESPONSABLE" NVARCHAR2(2000), 
	"AUTORISATIONRATIFICATION" NUMBER(1,0), 
	"INTITULE" NVARCHAR2(2000), 
	"IDDOSSIER" NVARCHAR2(2000), 
	"OBJETRIM" NCLOB, 
	"TITREACTE" NVARCHAR2(2000), 
	"CHAMBREDEPOTLOCKED" NUMBER(1,0), 
	"SECTIONCE" NVARCHAR2(2000), 
	"DOCLOCKUSER" NVARCHAR2(2000), 
	"NUMERONOR" NVARCHAR2(2000), 
	"NUMEROINTERNE" NVARCHAR2(2000), 
	"CHAMPLIBRE" NCLOB, 
	"DATEPUBLICATIONLOCKED" NUMBER(1,0), 
	"TYPEMESURE" NVARCHAR2(2000), 
	"OBJET" NCLOB, 
	"NATURETEXTE" NVARCHAR2(2000), 
	"CALENDRIERCONSULTATIONSHCE" NVARCHAR2(2000), 
	"TYPEACTE" NVARCHAR2(2000), 
	"DATEDIRECTIVE" TIMESTAMP (6), 
	"TYPEACTELOCKED" NUMBER(1,0), 
	"NUMEROSTEXTES" NVARCHAR2(2000), 
	"DIFFERE" NUMBER(1,0), 
	"DATEPJL" TIMESTAMP (6), 
	"RECEPTIONAVISCE" NVARCHAR2(2000), 
	"SECTIONCELOCKED" NUMBER(1,0), 
	"CHAMBREDEPOT" NVARCHAR2(2000), 
	"DATELIMITEPUBLICATION" TIMESTAMP (6), 
	"OBSERVATION" NCLOB, 
	"NUMERODEPOT" NVARCHAR2(2000), 
	"APPLICATIONLOI" NUMBER(1,0), 
	"APPLICATIONORDONNANCE" NUMBER(1,0), 
	"NUMEROINTERNELOCKED" NUMBER(1,0), 
	"DATECREATION" TIMESTAMP (6), 
	"NUMEROPAGE" NUMBER(19,0), 
	"MOTCLE" NVARCHAR2(2000), 
	"THEMATIQUE" NVARCHAR2(2000), 
	"CODEMODIFIE" NVARCHAR2(2000), 
	"AMENDEMENT" NUMBER(1,0), 
	"BASELEGALE" NVARCHAR2(2000), 
	"DATEDISPONIBILITEAVANTPROJET" TIMESTAMP (6), 
	"DATESORTIECE" TIMESTAMP (6), 
	"TYPEHABILITATION" NVARCHAR2(2000), 
	"ORGANISATION" NVARCHAR2(2000), 
	"NATURETEXTELOCKED" NUMBER(1,0), 
	"DISPOSITIONHABILITATION" NUMBER(1,0), 
	"DEGREPRIORITE" NVARCHAR2(2000), 
	"DATEECHEANCE" TIMESTAMP (6), 
	"DATEENTREEENVIGUEURLOCKED" NUMBER(1,0), 
	"NUMERO" NVARCHAR2(2000), 
	"LEGISLATUREPUBLICATION" NVARCHAR2(2000), 
	"DOCLOCKDATE" TIMESTAMP (6), 
	"DATESAISINECELOCKED" NUMBER(1,0), 
	"DATEPASSAGECMLOCKED" NUMBER(1,0), 
	"APPLICATIONDIRECTELOCKED" NUMBER(1,0), 
	"DATESORTIECELOCKED" NUMBER(1,0), 
	"PUBLICATION" NUMBER(1,0), 
	"ETUDEIMPACT" NUMBER(1,0), 
	"DATEEXAMENCM" TIMESTAMP (6), 
	"COMMISSIONASSNATIONALE" NVARCHAR2(2000), 
	"RAPPORTEURCE" NVARCHAR2(2000), 
	"ETAPESOLON" NVARCHAR2(2000), 
	"CATEGORIE" NVARCHAR2(2000), 
	"COMMISSIONASSNATIONALELOCKED" NUMBER(1,0), 
	"PROCEDUREVOTELOCKED" NUMBER(1,0), 
	"CONVENTION" NVARCHAR2(2000), 
	"DATEOBJECTIFPUBLICATION" TIMESTAMP (6), 
	"DATEDEPOTLOCKED" NUMBER(1,0), 
	"DATEENTREEENVIGUEUR" TIMESTAMP (6), 
	"DATEPROCHAINTABAFFICHAGE" TIMESTAMP (6), 
	"NUMEROPAGELOCKED" NUMBER(1,0), 
	"DATEEXAMENCE" TIMESTAMP (6), 
	"TITREOFFICIEL" NVARCHAR2(2000), 
	"ARTICLE" NVARCHAR2(2000), 
	"NUMEROTEXTEPUBLIE" NVARCHAR2(2000), 
	"CHAMPLIBRELOCKED" NUMBER(1,0), 
	"COMMISSIONSENAT" NVARCHAR2(2000), 
	"APPLICATIONRECU" NUMBER(1,0), 
	"CONVENTIONDEPOT" NVARCHAR2(2000), 
	"DISPOETUDEIMPACT" NUMBER(1,0), 
	"DATEDEPOT" TIMESTAMP (6), 
	"DATEPUBLICATION" TIMESTAMP (6), 
	"LEGISLATUREPUBLICATIONLOCKED" NUMBER(1,0), 
	"NUMERODEPOTLOCKED" NUMBER(1,0), 
	"NUMEROQUESTION" NVARCHAR2(2000), 
	"NORDECRET" NVARCHAR2(2000), 
	"DATEEXAMENCELOCKED" NUMBER(1,0), 
	"RATIFIE" NUMBER(1,0), 
	"CONSULTATIONSHCE" NVARCHAR2(2000), 
	"NUMEROORDRE" NVARCHAR2(2000), 
	"MINISTEREPILOTE" NVARCHAR2(2000), 
	"OBJETLOCKED" NUMBER(1,0), 
	"DATESECTIONCELOCKED" NUMBER(1,0), 
	"NUMEROLOCKED" NUMBER(1,0), 
	"DATESECTIONCE" TIMESTAMP (6), 
	"DATEREUNIONPROGRAMMATION" TIMESTAMP (6), 
	"TABAFFICHAGEMARCHEINT" NUMBER(1,0), 
	"HASVALIDATION" NUMBER(1,0), 
	"DATECIRCULATIONCOMPTERENDU" TIMESTAMP (6), 
	"CODIFICATION" NUMBER(1,0), 
	"RENVOIDECRET" NUMBER(1,0), 
	"TITREACTELOCKED" NUMBER(1,0), 
	"DATESIGNATURE" TIMESTAMP (6), 
	"ACHEVEE" NUMBER(1,0), 
	"RESPONSABLEAMENDEMENT" NVARCHAR2(2000), 
	"REFERENCEDISPOSITIONR_370BE108" NVARCHAR2(2000), 
	"DATEDEPOTRATIFICATION" TIMESTAMP (6), 
	"COMMISSIONSENATLOCKED" NUMBER(1,0), 
	"DATELIMITEDEPOT" TIMESTAMP (6), 
	"TITRE" NVARCHAR2(2000), 
	"MESUREAPPLICATION" NUMBER(1,0), 
	"DATEPROMULGATIONLOCKED" NUMBER(1,0), 
	"DATESAISINECE" TIMESTAMP (6), 
	"TITREOFFICIELLOCKED" NUMBER(1,0), 
	"DATEPREVISIONNELLESAISINECE" TIMESTAMP (6), 
	"NUMEROJOPUBLICATIONLOCKED" NUMBER(1,0), 
	"FROMAMENDEMENT" NUMBER(1,0), 
	"DATEPASSAGECM" TIMESTAMP (6), 
	"DATEPROMULGATION" TIMESTAMP (6), 
	"DATETERME" TIMESTAMP (6), 
	"REFERENCEAVISCE" NVARCHAR2(2000), 
	"APPLICATIONDIRECTE" NUMBER(1,0), 
	"MINISTEREPILOTELOCKED" NUMBER(1,0), 
	"RAPPORTEURCELOCKED" NUMBER(1,0), 
	"POLECHARGEMISSION" NVARCHAR2(2000), 
	"DELAIPUBLICATION" NVARCHAR2(2000), 
	"NORLOIRATIFICATION" NVARCHAR2(2000), 
	"TITRETEXTEPUBLIE" NVARCHAR2(2000),
	"DIRECTIVEETAT" NVARCHAR2(2000),
	"DATEMISEAPPLICATION" TIMESTAMP (6),
	"COMMUNICATIONMINISTERIELLE" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TEXTE_SIGNALE
--------------------------------------------------------

  CREATE TABLE "TEXTE_SIGNALE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"DATEDEMANDEPUBLICATIONTS" TIMESTAMP (6), 
	"VECTEURPUBLICATIONTS" NVARCHAR2(2000), 
	"IDDOSSIER" NVARCHAR2(2000), 
	"OBSERVATIONTS" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table topic
--------------------------------------------------------

  CREATE TABLE "topic" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TP_AMPLIATIONDESTINATAIREMAILS
--------------------------------------------------------

  CREATE TABLE "TP_AMPLIATIONDESTINATAIREMAILS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TRAITEMENT_PAPIER
--------------------------------------------------------

  CREATE TABLE "TRAITEMENT_PAPIER" 
   (	"ID" VARCHAR2(36 BYTE), 
	"PUBLICATIONDATEENVOIJO" TIMESTAMP (6), 
	"DATEARRIVEPAPIER" TIMESTAMP (6), 
	"PERSONNESNOMMEES" NVARCHAR2(2000), 
	"CHEMINCROIX" NUMBER(1,0), 
	"PUBLICATIONNOMPUBLICATION" NVARCHAR2(2000), 
	"PUBLICATIONDELAI" NVARCHAR2(2000), 
	"PUBLICATIONEPREUVEENRETOUR" NUMBER(1,0), 
	"PUBLICATIONMODEPUBLICATION" NVARCHAR2(2000), 
	"SIGNATUREDESTINATAIRESGG" NVARCHAR2(2000), 
	"SIGNATUREDESTINATAIRECPM" NVARCHAR2(2000), 
	"PUBLICATIONDATEDEMANDE" TIMESTAMP (6), 
	"DATERETOUR" TIMESTAMP (6), 
	"RETOURA" NVARCHAR2(2000), 
	"COMMENTAIREREFERENCE" NVARCHAR2(2000), 
	"PRIORITE" NVARCHAR2(2000), 
	"TEXTESOUMISAVALIDATION" NUMBER(1,0), 
	"PUBLICATIONDATE" TIMESTAMP (6), 
	"TEXTEAPUBLIER" NUMBER(1,0), 
	"RETOURDUBONATITRERLE" TIMESTAMP (6), 
	"NOMSSIGNATAIRESRETOUR" NVARCHAR2(2000), 
	"PUBLICATIONNUMEROLISTE" NVARCHAR2(2000), 
	"MOTIFRETOUR" NVARCHAR2(2000), 
	"NOMSSIGNATAIRESCOMMUNICATION" NVARCHAR2(2000), 
	"SIGNATAIRE" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TRANPOSITION
--------------------------------------------------------

  CREATE TABLE "TRANPOSITION" 
   (	"ID" VARCHAR2(36 BYTE), 
	"REF" NVARCHAR2(2000), 
	"NUMEROARTICLES" NVARCHAR2(2000), 
	"COMMENTAIRE" NVARCHAR2(2000), 
	"REFMESURE" NVARCHAR2(2000), 
	"TITRE" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table TYPE_CONTACT
--------------------------------------------------------

  CREATE TABLE "TYPE_CONTACT" 
   (	"ID" VARCHAR2(36 BYTE), 
	"EMAIL" NVARCHAR2(2000), 
	"MAILBOXID" NVARCHAR2(2000), 
	"NAME" NVARCHAR2(2000), 
	"SERVICE" NVARCHAR2(2000), 
	"SURNAME" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table UID
--------------------------------------------------------

  CREATE TABLE "UID" 
   (	"ID" VARCHAR2(36 BYTE), 
	"UID" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table UITYPESCONF_ALLOWEDTYPES
--------------------------------------------------------

  CREATE TABLE "UITYPESCONF_ALLOWEDTYPES" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table UITYPESCONF_DENIEDTYPES
--------------------------------------------------------

  CREATE TABLE "UITYPESCONF_DENIEDTYPES" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table UI_TYPES_CONFIGURATION
--------------------------------------------------------

  CREATE TABLE "UI_TYPES_CONFIGURATION" 
   (	"ID" VARCHAR2(36 BYTE), 
	"DENYALLTYPES" NUMBER(1,0)
   ) ;
--------------------------------------------------------
--  DDL for Table USERSETTINGS
--------------------------------------------------------

  CREATE TABLE "USERSETTINGS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"USER" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table USERSUBSCRIPTION
--------------------------------------------------------

  CREATE TABLE "USERSUBSCRIPTION" 
   (	"ID" NUMBER(10,0), 
	"DOCID" VARCHAR2(255 CHAR), 
	"NOTIFICATION" VARCHAR2(255 CHAR), 
	"USERID" VARCHAR2(255 CHAR)
   ) ;
--------------------------------------------------------
--  DDL for Table VCARD
--------------------------------------------------------

  CREATE TABLE "VCARD" 
   (	"ID" VARCHAR2(36 BYTE), 
	"EMAIL" NVARCHAR2(2000), 
	"SOUND" NVARCHAR2(2000), 
	"GEO" NVARCHAR2(2000), 
	"TITLE" NVARCHAR2(2000), 
	"BDAY" NVARCHAR2(2000), 
	"NOTE" NVARCHAR2(2000), 
	"REV" NVARCHAR2(2000), 
	"NICKNAME" NVARCHAR2(2000), 
	"MAILER" NVARCHAR2(2000), 
	"LABEL" NVARCHAR2(2000), 
	"TEL" NVARCHAR2(2000), 
	"UID" NVARCHAR2(2000), 
	"FN" NVARCHAR2(2000), 
	"LOGO" NVARCHAR2(2000), 
	"N" NVARCHAR2(2000), 
	"ORG" NVARCHAR2(2000), 
	"AGENT" NVARCHAR2(2000), 
	"ADR" NVARCHAR2(2000), 
	"KEY" NVARCHAR2(2000), 
	"VERSION" NVARCHAR2(2000), 
	"URL" NVARCHAR2(2000), 
	"TZ" NVARCHAR2(2000), 
	"ROLE" NVARCHAR2(2000), 
	"PHOTO" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VERSIONS
--------------------------------------------------------

  CREATE TABLE "VERSIONS" 
   (	"ID" VARCHAR2(36 BYTE), 
	"VERSIONABLEID" VARCHAR2(36 BYTE), 
	"CREATED" TIMESTAMP (6), 
	"LABEL" VARCHAR2(250 BYTE), 
	"DESCRIPTION" NVARCHAR2(2000), 
	"ISLATEST" NUMBER(1,0), 
	"ISLATESTMAJOR" NUMBER(1,0)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_ACTE_CATEGORY
--------------------------------------------------------

  CREATE TABLE "VOC_ACTE_CATEGORY" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_ACTE_TYPE
--------------------------------------------------------

  CREATE TABLE "VOC_ACTE_TYPE" 
   (	"id" NVARCHAR2(2000), 
	"activiteNormative" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"classification" NVARCHAR2(2000), 
	"isDecret" NVARCHAR2(2000), 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_ATTRIBUTION_COMMISSION
--------------------------------------------------------

  CREATE TABLE "VOC_ATTRIBUTION_COMMISSION" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_BOOLEAN
--------------------------------------------------------

  CREATE TABLE "VOC_BOOLEAN" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_BORDEREAU_LABEL
--------------------------------------------------------

  CREATE TABLE "VOC_BORDEREAU_LABEL" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_CM_ROUTING_TASK_TYPE
--------------------------------------------------------

  CREATE TABLE "VOC_CM_ROUTING_TASK_TYPE" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_COURRIER
--------------------------------------------------------

  CREATE TABLE "VOC_COURRIER" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_SESSION
--------------------------------------------------------

  CREATE TABLE "VOC_SESSION" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_TYPE_ACTE_EPP
--------------------------------------------------------

  CREATE TABLE "VOC_TYPE_ACTE_EPP" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_DOS_METADATA
--------------------------------------------------------

  CREATE TABLE "VOC_DOS_METADATA" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_DOS_STATUS
--------------------------------------------------------

  CREATE TABLE "VOC_DOS_STATUS" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_ETAPE_STATUT_RECHERCHE
--------------------------------------------------------

  CREATE TABLE "VOC_ETAPE_STATUT_RECHERCHE" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_FDR_COLUMN
--------------------------------------------------------

  CREATE TABLE "VOC_FDR_COLUMN" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_FILE_FORMAT
--------------------------------------------------------

  CREATE TABLE "VOC_FILE_FORMAT" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_GROUPE_POLITIQUE
--------------------------------------------------------

  CREATE TABLE "VOC_GROUPE_POLITIQUE" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_MINISTERES
--------------------------------------------------------

  CREATE TABLE "VOC_MINISTERES" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_MODE_PARUTION
--------------------------------------------------------

  CREATE TABLE "VOC_MODE_PARUTION" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_NATURE_RAPPORT
--------------------------------------------------------

  CREATE TABLE "VOC_NATURE_RAPPORT" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_NATURE_TEXTE
--------------------------------------------------------

  CREATE TABLE "VOC_NATURE_TEXTE" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_NIVEAU_LECTURE
--------------------------------------------------------

  CREATE TABLE "VOC_NIVEAU_LECTURE" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_NIVEAU_VISIBILITE
--------------------------------------------------------

  CREATE TABLE "VOC_NIVEAU_VISIBILITE" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_ORGA_NODE_TYPE
--------------------------------------------------------

  CREATE TABLE "VOC_ORGA_NODE_TYPE" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_PAPIER_AVIS_TP
--------------------------------------------------------

  CREATE TABLE "VOC_PAPIER_AVIS_TP" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000), 
	"type" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_PAPIER_PRIORITE
--------------------------------------------------------

  CREATE TABLE "VOC_PAPIER_PRIORITE" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_PAPIER_TYPE_SIGN
--------------------------------------------------------

  CREATE TABLE "VOC_PAPIER_TYPE_SIGN" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_PROCEDURE_VOTE
--------------------------------------------------------

  CREATE TABLE "VOC_PROCEDURE_VOTE" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_PUBLICATION_DELAI
--------------------------------------------------------

  CREATE TABLE "VOC_PUBLICATION_DELAI" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_PUBLICATION_TYPE
--------------------------------------------------------

  CREATE TABLE "VOC_PUBLICATION_TYPE" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_SORT_ADOPTION
--------------------------------------------------------

  CREATE TABLE "VOC_SORT_ADOPTION" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_TITLE
--------------------------------------------------------

  CREATE TABLE "VOC_TITLE" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_TRAITEMENT_PAPIER_LABEL
--------------------------------------------------------

  CREATE TABLE "VOC_TRAITEMENT_PAPIER_LABEL" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_TYPE_HABILITATION
--------------------------------------------------------

  CREATE TABLE "VOC_TYPE_HABILITATION" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_TYPE_MESURE
--------------------------------------------------------

  CREATE TABLE "VOC_TYPE_MESURE" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_TYPE_PJ
--------------------------------------------------------

  CREATE TABLE "VOC_TYPE_PJ" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_VECTEUR_PUBLICATION_TP
--------------------------------------------------------

  CREATE TABLE "VOC_VECTEUR_PUBLICATION_TP" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_VECTEUR_PUBLICATION_TS
--------------------------------------------------------

  CREATE TABLE "VOC_VECTEUR_PUBLICATION_TS" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table VOC_VERROU
--------------------------------------------------------

  CREATE TABLE "VOC_VERROU" 
   (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table WEBCONTAINER
--------------------------------------------------------

  CREATE TABLE "WEBCONTAINER" 
   (	"ID" VARCHAR2(36 BYTE), 
	"USECAPTCHA" NUMBER(1,0), 
	"WELCOMETEXT" NCLOB, 
	"THEME" NVARCHAR2(2000), 
	"ISWEBCONTAINER" NUMBER(1,0), 
	"BASELINE" NVARCHAR2(2000), 
	"TEMPLATE" NVARCHAR2(2000), 
	"THEMEPERSPECTIVE" NVARCHAR2(2000), 
	"THEMEPAGE" NVARCHAR2(2000), 
	"NAME" NVARCHAR2(2000), 
	"MODERATIONTYPE" NVARCHAR2(2000), 
	"URL" NVARCHAR2(2000), 
	"EMAIL" NVARCHAR2(2000)
   ) ;
--------------------------------------------------------
--  DDL for Table WSSPE
--------------------------------------------------------

  CREATE TABLE "WSSPE" 
   (	"ID" VARCHAR2(36 BYTE), 
	"POSTEID" NVARCHAR2(2000), 
	"WEBSERVICE" NVARCHAR2(2000), 
	"NBESSAIS" NUMBER(19,0), 
	"IDDOSSIER" NVARCHAR2(2000), 
	"TYPEPUBLICATION" NVARCHAR2(2000)
   ) ;
   
--------------------------------------------------------
--  DDL for Table FPDR_DESTINATAIRE1RAPPORT
--------------------------------------------------------

CREATE TABLE "FPDR_DESTINATAIRE1RAPPORT" 
(	
	"ID" VARCHAR2(36 BYTE), 
	"POS" NUMBER(10,0), 
	"ITEM" NVARCHAR2(2000)
) ;
   
   
--------------------------------------------------------
--  DDL for Table NOTIFICATION_DOC
--------------------------------------------------------

CREATE TABLE "NOTIFICATION_DOC"
  (
    "ID" VARCHAR2(36 BYTE),
    "EVENEMENTID" NVARCHAR2(2000),
    "DATEARRIVE" TIMESTAMP (6)
  ) ;
  
 CREATE TABLE "HISTORIQUE_MDP"
   ("ID" NUMBER(19,0) NOT NULL ENABLE,
	"LOGIN" VARCHAR2(255 CHAR),
	"DERNIER_MDP" VARCHAR2(255 CHAR),
	"DATE_ENREGISTREMENT" TIMESTAMP(6),
	 PRIMARY KEY ("ID")
   );

   CREATE TABLE "EXPORT_PAN_STAT" 
   (	"ID" VARCHAR2(36 BYTE) NOT NULL ENABLE, 
	"OWNER" NVARCHAR2(2000), 
	"DATEREQUEST" TIMESTAMP (6), 
	"CURLEGISLATURE" NUMBER(1,0),
	PRIMARY KEY ("ID")
   );
   
--------------------------------------------------------
--  DDL for Table RLACL
--------------------------------------------------------

   CREATE TABLE "RLACL" (	
    "HIERARCHY_ID" VARCHAR2(36 BYTE)  NOT NULL ENABLE, 
    "ACL" VARCHAR2(4000),
    CONSTRAINT "RLACL_ID_HIERARCHY_FK" FOREIGN KEY ("HIERARCHY_ID") REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE
); 
   
   
   REM INSERTING into VOC_ACTE_CATEGORY
Insert into VOC_ACTE_CATEGORY ("id","obsolete","ordering","label") values ('1',0,10000000,'Réglementaire ');
Insert into VOC_ACTE_CATEGORY ("id","obsolete","ordering","label") values ('2',0,10000000,'Non réglementaire');
REM INSERTING into VOC_ACTE_TYPE
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('1','0',0,'0','0" ',10000000,'Amnistie');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('2','0',0,'0','0',10000000,'Arrêté ministériel');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('36','0',0,'0','0',10000000,'Arrêté ministériel (individuel)');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('3','0',0,'0','0',10000000,'Arrêté interministériel');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('37','0',0,'0','0',10000000,'Arrêté interministériel (individuel)');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('4','0',0,'1','0',10000000,'Arrêté du Premier ministre');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('38','0',0,'1','0',10000000,'Arrêté du Premier ministre (individuel)');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('5','0',0,'0','0',10000000,'Arrêté du Président de la République');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('39','0',0,'0','0',10000000,'Arrêté du Président de la République (individuel)');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('6','0',0,'0','0',10000000,'Avenant');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('7','0',0,'0','0',10000000,'Avis');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('8','0',0,'0','0',10000000,'Circulaire');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('9','0',0,'0','0',10000000,'Citation');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('10','0',0,'0','0',10000000,'Communiqué');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('11','0',0,'0','0',10000000,'Convention');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('12','0',0,'0','0',10000000,'Décision');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('13','0',0,'1','1',10000000,'Décret en Conseil d''Etat de l''article 37 second alinéa de la Constitution');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('14','0',0,'1','1',10000000,'Décret en Conseil d''Etat');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('40','0',0,'1','1',10000000,'Décret en Conseil d''Etat (individuel)');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('15','0',0,'1','1',10000000,'Décret en Conseil d''Etat et conseil des ministres');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('41','0',0,'1','1',10000000,'Décret en Conseil d''Etat et conseil des ministres (individuel)');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('16','0',0,'1','1',10000000,'Décret en conseil des ministres');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('42','0',0,'1','1',10000000,'Décret en conseil des ministres (individuel)');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('17','0',0,'1','1',10000000,'Décret du Président de la République');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('43','0',0,'1','1',10000000,'Décret du Président de la République (individuel)');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('18','0',0,'1','1',10000000,'Décret simple');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('44','0',0,'1','1',10000000,'Décret simple (individuel)');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('19','0',0,'1','1',10000000,'Décret de publication de traité ou accord');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('20','0',0,'0','0',10000000,'Délibération');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('21','0',0,'0','0',10000000,'Demande d''avis au Conseil d''Etat');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('22','0',0,'0','0',10000000,'Divers');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('23','0',0,'0','0',10000000,'Exequatur');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('24','0',0,'0','0',10000000,'Instruction');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('25','0',0,'0','0',10000000,'Liste');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('26','1',0,'1','0',10000000,'Loi');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('27','1',0,'1','0',10000000,'Loi de l''article 53 de la Constitution');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('28','1',0,'1','0',10000000,'Loi constitutionnelle');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('29','1',0,'1','0',10000000,'Loi organique');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('30','0',0,'0','0',10000000,'Note');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('31','2',0,'1','0',10000000,'Ordonnance');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('32','0',0,'0','0',10000000,'Rapport');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('33','0',0,'0','0',10000000,'Rectificatif');
Insert into VOC_ACTE_TYPE ("id","activiteNormative","obsolete","classification","isDecret","ordering","label") values ('34','0',0,'0','0',10000000,'Tableau');
REM INSERTING into VOC_ATTRIBUTION_COMMISSION
Insert into VOC_ATTRIBUTION_COMMISSION ("id","obsolete","ordering","label") values ('AU_FOND',0,10000000,'Au fond');
Insert into VOC_ATTRIBUTION_COMMISSION ("id","obsolete","ordering","label") values ('POUR_AVIS',0,10000000,'Pour avis');
REM INSERTING into VOC_BOOLEAN
Insert into VOC_BOOLEAN ("id","obsolete","ordering","label") values ('TRUE',0,10000000,'Oui');
Insert into VOC_BOOLEAN ("id","obsolete","ordering","label") values ('FALSE',0,10000000,'Non');
REM INSERTING into VOC_BORDEREAU_LABEL
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('dateArriveeDossierLink',0,10000000,'Date d''arrivée');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('dateCreationDossier',0,10000000,'Date de création');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('idCreateurDossier',0,10000000,'Créé par');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('dernierContributeur',0,10000000,'Dernier contributeur');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('numeroNor',0,10000000,'NOR');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('statut',0,10000000,'Statut');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('typeActe',0,10000000,'Type d''acte');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('titreActe',0,10000000,'Titre de l''acte');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('ministereResp',0,10000000,'Entité à l''origine du projet');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('directionResp',0,10000000,'Direction concernée');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('ministereRespBord',0,10000000,'Ministère resp.');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('directionRespBord',0,10000000,'Direction resp.');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('ministereAttache',0,10000000,'Ministère rattach.');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('directionAttache',0,10000000,'Direction rattach.');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('nomRespDossier',0,10000000,'Nom du responsable du dossier');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('prenomRespDossier',0,10000000,'Prénom resp. dossier');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('qualiteRespDossier',0,10000000,'Qualité resp. dossier');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('telRespDossier',0,10000000,'Tél. resp. dossier');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('mailRespDossier',0,10000000,'Mél resp. dossier');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('categorieActe',0,10000000,'Catégorie acte');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('baseLegaleActe',0,10000000,'Base légale');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('datePublication',0,10000000,'Date de publication souhaitée');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('publicationRapportPresentation',0,10000000,'Publication du rapport de présentation');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('sectionCe',0,10000000,'Section du CE');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('rapporteurCe',0,10000000,'Rapporteur');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('dateTransmissionSectionCe',0,10000000,'Date transm. section CE');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('dateSectionCe',0,10000000,'Date section CE');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('dateAgCe',0,10000000,'Date AG CE');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('numeroISA',0,10000000,'Numéro ISA');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('nomCompletChargesMissions',0,10000000,'Chargé de mission');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('nomCompletConseillersPms',0,10000000,'Conseiller PM');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('dateSignature',0,10000000,'Date signature');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('pourFournitureEpreuve',0,10000000,'Pour fourniture d''épreuve');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('vecteurPublication',0,10000000,'Vecteur de publication');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('publicationsConjointes',0,10000000,'Publication conjointe');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('publicationIntegraleOuExtrait',0,10000000,'Publication intégrale ou par extrait');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('decretNumerote',0,10000000,'Décret numéroté');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('modeParution',0,10000000,'Mode parution');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('delaiPublication',0,10000000,'Délai publication');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('datePreciseePublication',0,10000000,'Publication à date précisée');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('dateParutionJorf',0,10000000,'Date JO');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('numeroTexteParutionJorf',0,10000000,'N° du texte JO');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('pageParutionJorf',0,10000000,'Page JO');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('titreOfficiel',0,10000000,'Titre publié');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('parutionBo',0,10000000,'Parutions BO');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('zoneComSggDila',0,10000000,'Zone commentaire SGG-DILA');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('rubriques',0,10000000,'Rubrique');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('motscles',0,10000000,'Mots Cles');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('libre',0,10000000,'Champ Libre');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('applicationLoi',0,10000000,'Réf. Lois Appliquées');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('transpositionOrdonnance',0,10000000,'Réf. Ordonnances');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('transpositionDirective',0,10000000,'Réf. Directives Europeennes');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('habilitationRefLoi',0,10000000,'Réf. Habilitation');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('habilitationNumeroArticles',0,10000000,'N° Article Habilitation');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('habilitationCommentaire',0,10000000,'Commentaire Habilitation');
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('habilitationNumeroOrdre',0,10000000,'N°Ordre Habilitation');
REM INSERTING into VOC_CM_ROUTING_TASK_TYPE
Insert into VOC_CM_ROUTING_TASK_TYPE ("id","obsolete","ordering","label") values ('1',0,10000000,'label.epg.feuilleRoute.etape.pour.initialisation');
Insert into VOC_CM_ROUTING_TASK_TYPE ("id","obsolete","ordering","label") values ('2',0,10000000,'label.epg.feuilleRoute.etape.pour.avis');
Insert into VOC_CM_ROUTING_TASK_TYPE ("id","obsolete","ordering","label") values ('3',0,10000000,'label.epg.feuilleRoute.etape.pour.avisCE');
Insert into VOC_CM_ROUTING_TASK_TYPE ("id","obsolete","ordering","label") values ('4',0,10000000,'label.epg.feuilleRoute.etape.pour.attribution');
Insert into VOC_CM_ROUTING_TASK_TYPE ("id","obsolete","ordering","label") values ('5',0,10000000,'label.epg.feuilleRoute.etape.pour.attributionSGG');
Insert into VOC_CM_ROUTING_TASK_TYPE ("id","obsolete","ordering","label") values ('6',0,10000000,'label.epg.feuilleRoute.etape.pour.attributionSecteurParlementaire');
Insert into VOC_CM_ROUTING_TASK_TYPE ("id","obsolete","ordering","label") values ('7',0,10000000,'label.epg.feuilleRoute.etape.pour.signature');
Insert into VOC_CM_ROUTING_TASK_TYPE ("id","obsolete","ordering","label") values ('8',0,10000000,'label.epg.feuilleRoute.etape.pour.contreseing');
Insert into VOC_CM_ROUTING_TASK_TYPE ("id","obsolete","ordering","label") values ('9',0,10000000,'label.epg.feuilleRoute.etape.pour.information');
Insert into VOC_CM_ROUTING_TASK_TYPE ("id","obsolete","ordering","label") values ('10',0,10000000,'label.epg.feuilleRoute.etape.pour.impression');
Insert into VOC_CM_ROUTING_TASK_TYPE ("id","obsolete","ordering","label") values ('11',0,10000000,'label.epg.feuilleRoute.etape.pour.bonATirer');
Insert into VOC_CM_ROUTING_TASK_TYPE ("id","obsolete","ordering","label") values ('12',0,10000000,'label.epg.feuilleRoute.etape.pour.fournitureEpreuves');
Insert into VOC_CM_ROUTING_TASK_TYPE ("id","obsolete","ordering","label") values ('13',0,10000000,'label.epg.feuilleRoute.etape.pour.publicationDILAJO');
Insert into VOC_CM_ROUTING_TASK_TYPE ("id","obsolete","ordering","label") values ('14',0,10000000,'label.epg.feuilleRoute.etape.pour.publicationDILABO');
Insert into VOC_CM_ROUTING_TASK_TYPE ("id","obsolete","ordering","label") values ('15',0,10000000,'label.epg.feuilleRoute.etape.pour.modification');
REM INSERTING into VOC_COURRIER
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('0',0,10000000,'LEX-14 - Procedure accélérée');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('1',0,10000000,'LEX-14 - Procedure accélérée lettre rectificative');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('2',0,10000000,'LEX-17 - Première lecture');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('3',0,10000000,'LEX-17 - Première lecture modifiée');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('4',0,10000000,'LEX-17 - Première lecture rejetée');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('5',0,10000000,'LEX-17 - Première lecture procédure accélerée');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('6',0,10000000,'LEX-17 - Première lecture procédure accélerée modifiée');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('7',0,10000000,'LEX-17 - Première lecture procédure accélerée rejetée');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('8',0,10000000,'LEX-17 - Deuxième lecture');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('9',0,10000000,'LEX-17 - Deuxième lecture modifiée');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('10',0,10000000,'LEX-17 - Nouvelle lecture');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('11',0,10000000,'LEX-22 - CMP (45-2)');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('12',0,10000000,'LEX-22 - CMP (45-2) texte commun');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('13',0,10000000,'LEX-26 - CMP (45-3)');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('14',0,10000000,'LEX-27 - Echec CMP');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('15',0,10000000,'LEX-27 - Echec CMP texte commun');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('16',0,10000000,'LEX-27 - Rejet par l''Assemblée nationale');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('17',0,10000000,'LEX-27 - Rejet par le Sénat');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('18',0,10000000,'LEX-27 - Rejet par le Sénat des amendements du Gouvernement');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('19',0,10000000,'LEX-26 - Dernière lecture');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('20',0,10000000,'LEX-26 - Dernière lecture rejet');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('21',0,10000000,'Lettre choix multiples - décès');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('22',0,10000000,'Lettre choix multiples - démission');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('23',0,10000000,'Lettre choix multiples - expiration');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('24',0,10000000,'Lettre création OEP');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('25',0,10000000,'Lettres re-création OEP');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('26',0,10000000,'Lettres suite entrée au Gouvernement');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('27',0,10000000,'Lettres suite nomination CCI');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('28',0,10000000,'34-1C : injonction');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('29',0,10000000,'34-1C: mise en cause de la responsabilité');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('30',0,10000000,'avenant conventions');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('31',0,10000000,'ouverture session extraordinaire-décret');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('32',0,10000000,'clôture session extraordianaire-décret');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('33',0,10000000,'ouverture session extraordinaire-ampliation');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('34',0,10000000,'clôture session extraordinaire-ampliation');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('35',0,10000000,'ouverture session extraordinaire-lettre');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('36',0,10000000,'clôture session extraordinaire-lettre');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('37',0,10000000,'dépôt rapport art 67');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('38',0,10000000,'dépôt rapport');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('39',0,10000000,'dépôt rapport pour information');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('40',0,10000000,'Lettres + décrets');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('41',0,10000000,'Lettre intervention extérieure');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('42',0,10000000,'Lettre intervention ext en session extra');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('43',0,10000000,'Décret congrès article 18C');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('44',0,10000000,'Lettres + décrets congrès article 18C');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('45',0,10000000,'Décret congrès');
Insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('46',0,10000000,'Ampliation congrès');
REM INSERTING into VOC_SESSION
Insert into VOC_SESSION ("id","obsolete","ordering","label") values ('0',0,10000000,'ordinaire');
Insert into VOC_SESSION ("id","obsolete","ordering","label") values ('1',0,10000000,'extraordinaire');
REM INSERTING into VOC_DOS_METADATA
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('1',0,10000000,'Statut de l''acte');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('2',0,10000000,'Type d''acte');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('3',0,10000000,'NOR');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('4',0,10000000,'Titre de l''acte');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('5',0,10000000,'Ministère responsable');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('6',0,10000000,'Direction responsable');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('7',0,10000000,'Ministère rattachement');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('8',0,10000000,'Direction rattachement');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('9',0,10000000,'Nom et Prénom du responsable du dossier');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('10',0,10000000,'Qualité resp. dossier');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('11',0,10000000,'Tél. resp. dossier');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('12',0,10000000,'Mél resp. dossier');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('13',0,10000000,'Nom et Prénom du créateur du dossier');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('14',0,10000000,'Qualité  créateur du dossier');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('15',0,10000000,'Tél.  créateur du dossier');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('16',0,10000000,'Mél  créateur du dossier');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('17',0,10000000,'Catégorie Acte ');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('18',0,10000000,'Base légale');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('19',0,10000000,'Publication du rapport de présentation');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('20',0,10000000,'Date de publication souhaitée');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('21',0,10000000,'Section du CE');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('22',0,10000000,'Rapporteur ');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('23',0,10000000,'Date transmission section CE');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('24',0,10000000,'Date section CE');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('25',0,10000000,'Date AG CE');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('26',0,10000000,'Numéro ISA');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('27',0,10000000,'Chargé de mission (nom et prénom)');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('28',0,10000000,'Conseiller PM (nom et prénom)');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('29',0,10000000,'Date signature');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('32',0,10000000,'Pour fourniture d''épreuve');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('33',0,10000000,'Vecteur de publication');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('34',0,10000000,'Publication intégrale ou par extrait');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('35',0,10000000,'Décret numéroté');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('36',0,10000000,'Publication conjointe');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('37',0,10000000,'Mode parution');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('38',0,10000000,'Délai publication');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('39',0,10000000,'Publication à date précisée');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('40',0,10000000,'date JOFR');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('41',0,10000000,'Numéro du texte JOFR');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('42',0,10000000,'page JOFR');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('43',0,10000000,'Numéro du texte BO');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('44',0,10000000,'Parution BO');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('45',0,10000000,'Zone commentaire SGG - DILA ');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('46',0,10000000,'indexation : rubriques');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('47',0,10000000,'indexation : mots clés');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('48',0,10000000,'indexation : champ libre');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('49',0,10000000,'mesure application loi ');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('50',0,10000000,'applications des lois : Référence de la loi');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('51',0,10000000,'applications des lois : Titre de la loi');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('52',0,10000000,'applications des lois : Numéros articles');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('53',0,10000000,'applications des lois : Référence de la mesure');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('54',0,10000000,'applications des lois : Commentaire');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('55',0,10000000,'transposition ordonnance');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('56',0,10000000,'application des ordonnances : Référence ordonnance');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('57',0,10000000,'application des ordonnances : Titre ordonnance');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('58',0,10000000,'application des ordonnances : Numéros articles');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('59',0,10000000,'application des ordonnances : Commentaire');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('60',0,10000000,'transposition directive europeene');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('61',0,10000000,'Transposition des directives : référence directive');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('62',0,10000000,'Transposition des directives : titre directive');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('63',0,10000000,'Transposition des directives : numéros articles');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('64',0,10000000,'Transposition des directives : commentaire');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('65',0,10000000,'disposition habilitation');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('66',0,10000000,'disposition d''habilitation : Référence de la loi');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('67',0,10000000,'disposition d''habilitation : Numéro article');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('68',0,10000000,'disposition d''habilitation : Commentaire');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('69',0,10000000,'date arrivée dossier');
Insert into VOC_DOS_METADATA ("id","obsolete","ordering","label") values ('70',0,10000000,'urgence');
REM INSERTING into VOC_DOS_STATUS
Insert into VOC_DOS_STATUS ("id","obsolete","ordering","label") values ('1',0,10000000,'Initié');
Insert into VOC_DOS_STATUS ("id","obsolete","ordering","label") values ('2',0,10000000,'Lancé');
Insert into VOC_DOS_STATUS ("id","obsolete","ordering","label") values ('3',0,10000000,'Abandonné');
Insert into VOC_DOS_STATUS ("id","obsolete","ordering","label") values ('4',0,10000000,'Publié');
Insert into VOC_DOS_STATUS ("id","obsolete","ordering","label") values ('5',0,10000000,'Nor attribué');
Insert into VOC_DOS_STATUS ("id","obsolete","ordering","label") values ('6',0,10000000,'Terminé sans publication');
REM INSERTING into VOC_ETAPE_STATUT_RECHERCHE
Insert into VOC_ETAPE_STATUT_RECHERCHE ("id","obsolete","ordering","label") values ('running_all',0,10000000,'label.epg.feuilleRoute.etape.running');
Insert into VOC_ETAPE_STATUT_RECHERCHE ("id","obsolete","ordering","label") values ('ready_all',0,10000000,'label.epg.feuilleRoute.etape.ready');
Insert into VOC_ETAPE_STATUT_RECHERCHE ("id","obsolete","ordering","label") values ('validated_1',0,10000000,'label.epg.feuilleRoute.etape.valide.manuellement');
Insert into VOC_ETAPE_STATUT_RECHERCHE ("id","obsolete","ordering","label") values ('validated_2',0,10000000,'label.epg.feuilleRoute.etape.valide.refusValidation');
Insert into VOC_ETAPE_STATUT_RECHERCHE ("id","obsolete","ordering","label") values ('validated_3',0,10000000,'label.epg.feuilleRoute.etape.valide.automatiquement');
Insert into VOC_ETAPE_STATUT_RECHERCHE ("id","obsolete","ordering","label") values ('validated_4',0,10000000,'label.epg.feuilleRoute.etape.valide.nonConcerne');
Insert into VOC_ETAPE_STATUT_RECHERCHE ("id","obsolete","ordering","label") values ('validated_10',0,10000000,'label.epg.feuilleRoute.etape.valide.avisFavorableCorrection');
Insert into VOC_ETAPE_STATUT_RECHERCHE ("id","obsolete","ordering","label") values ('validated_15',0,10000000,'label.epg.feuilleRoute.etape.valide.retourModification');
REM INSERTING into VOC_FDR_COLUMN
Insert into VOC_FDR_COLUMN ("id","obsolete","ordering","label") values ('etape',0,10000000,'Etape');
Insert into VOC_FDR_COLUMN ("id","obsolete","ordering","label") values ('ministere',0,10000000,'Ministere resp.');
Insert into VOC_FDR_COLUMN ("id","obsolete","ordering","label") values ('poste',0,10000000,'Poste');
Insert into VOC_FDR_COLUMN ("id","obsolete","ordering","label") values ('numeroVersion',0,10000000,'N°Version');
Insert into VOC_FDR_COLUMN ("id","obsolete","ordering","label") values ('urgence',0,10000000,'Urgence');
Insert into VOC_FDR_COLUMN ("id","obsolete","ordering","label") values ('note',0,10000000,'Note');
Insert into VOC_FDR_COLUMN ("id","obsolete","ordering","label") values ('etat',0,10000000,'Etat');
Insert into VOC_FDR_COLUMN ("id","obsolete","ordering","label") values ('typeAction',0,10000000,'Type d''action');
Insert into VOC_FDR_COLUMN ("id","obsolete","ordering","label") values ('auteur',0,10000000,'Auteur');
Insert into VOC_FDR_COLUMN ("id","obsolete","ordering","label") values ('echeance',0,10000000,'Echéance');
Insert into VOC_FDR_COLUMN ("id","obsolete","ordering","label") values ('dateTraitement',0,10000000,'Date de traitement');
Insert into VOC_FDR_COLUMN ("id","obsolete","ordering","label") values ('obligatoire',0,10000000,'Obligatoire');
REM INSERTING into VOC_FILE_FORMAT
Insert into VOC_FILE_FORMAT ("id","obsolete","ordering","label") values ('odg',0,10000000,'odg');
Insert into VOC_FILE_FORMAT ("id","obsolete","ordering","label") values ('odi',0,10000000,'odi');
Insert into VOC_FILE_FORMAT ("id","obsolete","ordering","label") values ('jpg',0,10000000,'jpg');
Insert into VOC_FILE_FORMAT ("id","obsolete","ordering","label") values ('png',0,10000000,'png');
Insert into VOC_FILE_FORMAT ("id","obsolete","ordering","label") values ('pdf',0,10000000,'pdf');
Insert into VOC_FILE_FORMAT ("id","obsolete","ordering","label") values ('rtf',0,10000000,'rtf');
Insert into VOC_FILE_FORMAT ("id","obsolete","ordering","label") values ('odt',0,10000000,'odt');
Insert into VOC_FILE_FORMAT ("id","obsolete","ordering","label") values ('doc',0,10000000,'doc');
Insert into VOC_FILE_FORMAT ("id","obsolete","ordering","label") values ('docx',0,10000000,'docx');
Insert into VOC_FILE_FORMAT ("id","obsolete","ordering","label") values ('ods',0,10000000,'ods');
Insert into VOC_FILE_FORMAT ("id","obsolete","ordering","label") values ('xls',0,10000000,'xls');
Insert into VOC_FILE_FORMAT ("id","obsolete","ordering","label") values ('xlsx',0,10000000,'xlsx');
Insert into VOC_FILE_FORMAT ("id","obsolete","ordering","label") values ('odp',0,10000000,'odp');
Insert into VOC_FILE_FORMAT ("id","obsolete","ordering","label") values ('ppt',0,10000000,'ppt');
Insert into VOC_FILE_FORMAT ("id","obsolete","ordering","label") values ('pptx',0,10000000,'pptx');
Insert into VOC_FILE_FORMAT ("id","obsolete","ordering","label") values ('odc',0,10000000,'odc');
Insert into VOC_FILE_FORMAT ("id","obsolete","ordering","label") values ('vsd',0,10000000,'vsd');
Insert into VOC_FILE_FORMAT ("id","obsolete","ordering","label") values ('zip',0,10000000,'zip');
REM INSERTING into VOC_GROUPE_POLITIQUE
Insert into VOC_GROUPE_POLITIQUE ("id","obsolete","ordering","label") values ('1',0,10000000,'Nouveau Centre');
Insert into VOC_GROUPE_POLITIQUE ("id","obsolete","ordering","label") values ('2',0,10000000,'Union pour un Mouvement Populaire');
Insert into VOC_GROUPE_POLITIQUE ("id","obsolete","ordering","label") values ('3',0,10000000,'Députés n''appartenant à aucun groupe');
Insert into VOC_GROUPE_POLITIQUE ("id","obsolete","ordering","label") values ('4',0,10000000,'Gauche démocrate et républicaine');
Insert into VOC_GROUPE_POLITIQUE ("id","obsolete","ordering","label") values ('5',0,10000000,'Socialiste, radical, citoyen et divers gauche');
Insert into VOC_GROUPE_POLITIQUE ("id","obsolete","ordering","label") values ('6',0,10000000,'Non connu');
REM INSERTING into VOC_MINISTERES
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50002368',0,10000000,'ARCEP');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50004632',0,10000000,'Autorité contrôle prudentiel');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50003732',0,10000000,'Autorité de la concurrence');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50002618',0,10000000,'CCO');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50002382',0,10000000,'Collectivités territoriales République');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50001827',0,10000000,'Comité entreprises assurances (CEA)');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50002357',0,10000000,'Commission de régulation de l''énergie');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50002507',0,10000000,'Commission nat informatique libertés');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50002907',0,10000000,'Conseil constitutionnel');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000582',0,10000000,'Conseil d''État');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50002682',0,10000000,'Conseil supérieur de l''audiovisuel');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50002509',0,10000000,'Cour des comptes');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000977',0,10000000,'Min. Affaires européennes');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000937',0,10000000,'Min. Affaires étrangères et européennes');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000938',0,10000000,'Min. Agr, alim, pêche, rur, amén. territ');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50005042',0,10000000,'Min. Apprentissage, formation prof');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50013998',0,10000000,'Min. Budget, comptes pub, FP, réf. Etat');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50005041',0,10000000,'Min. Collectivités territoriales');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50001008',0,10000000,'Min. Coopérations');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000966',0,10000000,'Min. Culture et communication');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000967',0,10000000,'Min. Défense, anciens combattants');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000976',0,10000000,'Min. Ecologie, dév durable, transp, logt');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000507',0,10000000,'Min. Economie, finances et industrie');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000968',0,10000000,'Min. Education nat, jeunesse, vie assoc.');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000940',0,10000000,'Min. Enseignement sup. et recherche');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50011153',0,10000000,'Min. Industrie, énergie, éco. numérique');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000632',0,10000000,'Min. Intérieur, OM, collect terr, immigr');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000972',0,10000000,'Min. Justice et des libertés');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50004232',0,10000000,'Min. Outre-mer');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50005038',0,10000000,'Min. Solidarités et cohésion sociale');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50005040',0,10000000,'Min. Sports');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000969',0,10000000,'Min. Travail, emploi et santé');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50005039',0,10000000,'Min. Ville');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000635',0,10000000,'Ministère pilote (à préciser)');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000636',0,10000000,'Ministère(s) suiveur(s) (à préciser)');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50001888',0,10000000,'NOR BCF');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50001894',0,10000000,'NOR BUD');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000979',0,10000000,'NOR DAC');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000633',0,10000000,'NOR DEV');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000973',0,10000000,'NOR DOM');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50003983',0,10000000,'NOR EAT');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50005032',0,10000000,'NOR ECE');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50001498',0,10000000,'NOR ECO');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50001910',0,10000000,'NOR EQU');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50001007',0,10000000,'NOR EXT');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000970',0,10000000,'NOR FPP');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50001501',0,10000000,'NOR INT');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50004432',0,10000000,'NOR JSA');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50001886',0,10000000,'NOR LOG');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000980',0,10000000,'NOR MCP');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000981',0,10000000,'NOR MCT');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50001441',0,10000000,'NOR MET');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000971',0,10000000,'NOR MJS');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50003584',0,10000000,'NOR MLV');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50005036',0,10000000,'NOR MTS');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000783',0,10000000,'NOR SAN');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000975',0,10000000,'NOR SAS');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50003582',0,10000000,'NOR SJS');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50001900',0,10000000,'NOR SOC');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000945',0,10000000,'NOR TOU');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000607',0,10000000,'Premier Ministre');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50000764',0,10000000,'Présidence de la République');
Insert into VOC_MINISTERES ("id","obsolete","ordering","label") values ('50004632',0,10000000,'NOR MJS');
REM INSERTING into VOC_MODE_PARUTION
Insert into VOC_MODE_PARUTION ("id","obsolete","ordering","label") values ('1',0,10000000,'Electronique');
Insert into VOC_MODE_PARUTION ("id","obsolete","ordering","label") values ('2',0,10000000,'Mixte');
Insert into VOC_MODE_PARUTION ("id","obsolete","ordering","label") values ('3',0,10000000,'Papier');
REM INSERTING into VOC_NATURE_RAPPORT
Insert into VOC_NATURE_RAPPORT ("id","obsolete","ordering","label") values ('RAPPORT',0,10000000,'Rapport');
Insert into VOC_NATURE_RAPPORT ("id","obsolete","ordering","label") values ('AVIS',0,10000000,'Avis');
Insert into VOC_NATURE_RAPPORT ("id","obsolete","ordering","label") values ('RAPPORT_SUPPLEMENTAIRE',0,10000000,'Rapport supplémentaire');
Insert into VOC_NATURE_RAPPORT ("id","obsolete","ordering","label") values ('AVIS_SUPPLEMENTAIRE',0,10000000,'Avis supplémentaire');
REM INSERTING into VOC_NATURE_TEXTE
Insert into VOC_NATURE_TEXTE ("id","obsolete","ordering","label") values ('0',0,10000000,'Projet de loi');
Insert into VOC_NATURE_TEXTE ("id","obsolete","ordering","label") values ('1',0,10000000,'Proposition de loi');
REM INSERTING into VOC_NIVEAU_LECTURE
Insert into VOC_NIVEAU_LECTURE ("id","obsolete","ordering","label") values ('AN',0,10000000,'Assemblée Nationale');
Insert into VOC_NIVEAU_LECTURE ("id","obsolete","ordering","label") values ('SENAT',0,10000000,'Sénat');
Insert into VOC_NIVEAU_LECTURE ("id","obsolete","ordering","label") values ('CMP',0,10000000,'Commission mixte paritaire');
Insert into VOC_NIVEAU_LECTURE ("id","obsolete","ordering","label") values ('NOUVELLE_LECTURE_AN',0,10000000,'Nouvelle lecture AN');
Insert into VOC_NIVEAU_LECTURE ("id","obsolete","ordering","label") values ('NOUVELLE_LECTURE_SENAT',0,10000000,'Nouvelle lecture sénat');
Insert into VOC_NIVEAU_LECTURE ("id","obsolete","ordering","label") values ('LECTURE_DEFINITIVE',0,10000000,'Lecture définitive');
Insert into VOC_NIVEAU_LECTURE ("id","obsolete","ordering","label") values ('CONGRES',0,10000000,'Congrès');
REM INSERTING into VOC_NIVEAU_VISIBILITE
Insert into VOC_NIVEAU_VISIBILITE ("id","obsolete","ordering","label") values ('1',0,10000000,'Uniquement Ministère');
Insert into VOC_NIVEAU_VISIBILITE ("id","obsolete","ordering","label") values ('2',0,10000000,'Ministère et SGG');
Insert into VOC_NIVEAU_VISIBILITE ("id","obsolete","ordering","label") values ('3',0,10000000,'A diffuser vers le parlement');
REM INSERTING into VOC_ORGA_NODE_TYPE
Insert into VOC_ORGA_NODE_TYPE ("id","obsolete","ordering","label") values ('MIN_TYPE',0,10000000,'Ministère');
Insert into VOC_ORGA_NODE_TYPE ("id","obsolete","ordering","label") values ('DIR_TYPE',0,10000000,'Direction');
Insert into VOC_ORGA_NODE_TYPE ("id","obsolete","ordering","label") values ('UST_TYPE',0,10000000,'Autre unité structurelle');
Insert into VOC_ORGA_NODE_TYPE ("id","obsolete","ordering","label") values ('POSTE_TYPE',0,10000000,'Poste');
REM INSERTING into VOC_PAPIER_AVIS_TP
Insert into VOC_PAPIER_AVIS_TP ("id","obsolete","ordering","label","type") values ('1',0,10000000,'Favorable','FAV');
Insert into VOC_PAPIER_AVIS_TP ("id","obsolete","ordering","label","type") values ('2',0,10000000,'Favorable avec corrections','FAV');
Insert into VOC_PAPIER_AVIS_TP ("id","obsolete","ordering","label","type") values ('3',0,10000000,'Défavorable','NONFAV');
Insert into VOC_PAPIER_AVIS_TP ("id","obsolete","ordering","label","type") values ('4',0,10000000,'Non concerné','NONFAV');
REM INSERTING into VOC_PAPIER_PRIORITE
Insert into VOC_PAPIER_PRIORITE ("id","obsolete","ordering","label") values ('1',0,10000000,'Urgent');
Insert into VOC_PAPIER_PRIORITE ("id","obsolete","ordering","label") values ('2',0,10000000,'TTU');
Insert into VOC_PAPIER_PRIORITE ("id","obsolete","ordering","label") values ('3',0,10000000,'Normal');
REM INSERTING into VOC_PAPIER_TYPE_SIGN
Insert into VOC_PAPIER_TYPE_SIGN ("id","obsolete","ordering","label") values ('1',0,10000000,'PR');
Insert into VOC_PAPIER_TYPE_SIGN ("id","obsolete","ordering","label") values ('2',0,10000000,'PM');
Insert into VOC_PAPIER_TYPE_SIGN ("id","obsolete","ordering","label") values ('3',0,10000000,'SGG');
Insert into VOC_PAPIER_TYPE_SIGN ("id","obsolete","ordering","label") values ('4',0,10000000,'Autre');
REM INSERTING into VOC_PROCEDURE_VOTE
Insert into VOC_PROCEDURE_VOTE ("id","obsolete","ordering","label") values ('0',0,10000000,'Procédure accélérée');
Insert into VOC_PROCEDURE_VOTE ("id","obsolete","ordering","label") values ('1',0,10000000,'Procédure normale');
Insert into VOC_PROCEDURE_VOTE ("id","obsolete","ordering","label") values ('2',0,10000000,'Urgence');
REM INSERTING into VOC_PUBLICATION_DELAI
Insert into VOC_PUBLICATION_DELAI ("id","obsolete","ordering","label") values ('1',0,10000000,'A date précisée');
Insert into VOC_PUBLICATION_DELAI ("id","obsolete","ordering","label") values ('2',0,10000000,'Aucun');
Insert into VOC_PUBLICATION_DELAI ("id","obsolete","ordering","label") values ('3',0,10000000,'De rigueur');
Insert into VOC_PUBLICATION_DELAI ("id","obsolete","ordering","label") values ('4',0,10000000,'Urgent');
Insert into VOC_PUBLICATION_DELAI ("id","obsolete","ordering","label") values ('5',0,10000000,'Sans tarder');
Insert into VOC_PUBLICATION_DELAI ("id","obsolete","ordering","label") values ('6',0,10000000,'Sous embargo');
REM INSERTING into VOC_PUBLICATION_TYPE
Insert into VOC_PUBLICATION_TYPE ("id","obsolete","ordering","label") values ('1',0,10000000,'In extenso');
Insert into VOC_PUBLICATION_TYPE ("id","obsolete","ordering","label") values ('2',0,10000000,'Extrait');
REM INSERTING into VOC_SORT_ADOPTION
Insert into VOC_SORT_ADOPTION ("id","obsolete","ordering","label") values ('ADOPTE',0,10000000,'Adopté');
Insert into VOC_SORT_ADOPTION ("id","obsolete","ordering","label") values ('REJETE',0,10000000,'Rejeté');
Insert into VOC_SORT_ADOPTION ("id","obsolete","ordering","label") values ('RENVOI_EN_COMMISSION',0,10000000,'Renvoi en commission');
Insert into VOC_SORT_ADOPTION ("id","obsolete","ordering","label") values ('ACCORD_CMP',0,10000000,'Accord CMP');
Insert into VOC_SORT_ADOPTION ("id","obsolete","ordering","label") values ('DESACCORD_CMP',0,10000000,'Désaccord CMP');
REM INSERTING into VOC_TITLE
Insert into VOC_TITLE ("id","obsolete","ordering","label") values ('Madame',0,10000000,'Madame');
Insert into VOC_TITLE ("id","obsolete","ordering","label") values ('Monsieur',0,10000000,'Monsieur');
REM INSERTING into VOC_TRAITEMENT_PAPIER_LABEL
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('dateArrivePapier',0,10000000,'Références - Date d''arrivée papier');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('commentaireReference',0,10000000,'Références - Commentaire');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('texteAPublier',0,10000000,'Références - Texte à publier');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('signataire',0,10000000,'Références - Signataire');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('priorite',0,10000000,'Communication - Priorité');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('cabinetPmCommunication',0,10000000,'Communication - Cabinet du PM');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('chargeMissionCommunication',0,10000000,'Communication - Chargé de mission');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('autresDestinatairesCommunication',0,10000000,'Communication - Autres');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('nomsSignatairesCommunication',0,10000000,'Communication - Signataire');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('signaturePm',0,10000000,'Signature - PM');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('signatureSgg',0,10000000,'Signature - SGG');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('signaturePr',0,10000000,'Signature - PR');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('cheminCroix',0,10000000,'Signature - Chemin de croix');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('numerosListeSignatureField',0,10000000,'Signature - Numéro de liste');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('retourA',0,10000000,'Retour - Retour à');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('dateRetour',0,10000000,'Retour - Date');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('motifRetour',0,10000000,'Retour - Motif Retour "	');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('nomsSignatairesRetour',0,10000000,'Retour - Signataire');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('epreuve',0,10000000,'Epreuves - Epreuve demandee le');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('nouvelleDemandeEpreuves',0,10000000,'Epreuves - Nouvelle demande le');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('retourDuBonaTitrerLe',0,10000000,'Epreuves - Retour du bon à tirer le');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('publicationDateEnvoiJo',0,10000000,'Publication - Date d''envoi JO');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('publicationNomPublication',0,10000000,'Publication - Vecteur de publication');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('publicationModePublication',0,10000000,'Publication - Mode parution');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('publicationEpreuveEnRetour',0,10000000,'Publication - Epreuves en retour');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('publicationDelai',0,10000000,'Publication - Délai publication');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('publicationDateDemande',0,10000000,'Publication - Publication à date précisée');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('publicationNumeroListe',0,10000000,'Publication - Numéro de liste');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('publicationDate',0,10000000,'Publication - Parution JORF - Date JO');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('ampliationDestinataireMails',0,10000000,'Ampliation - Destinataire');
Insert into VOC_TRAITEMENT_PAPIER_LABEL ("id","obsolete","ordering","label") values ('ampliationHistorique',0,10000000,'ampliation Historique');
REM INSERTING into VOC_TYPE_HABILITATION
Insert into VOC_TYPE_HABILITATION ("id","obsolete","ordering","label") values ('0',0,10000000,'Active');
Insert into VOC_TYPE_HABILITATION ("id","obsolete","ordering","label") values ('1',0,10000000,'En cours');
Insert into VOC_TYPE_HABILITATION ("id","obsolete","ordering","label") values ('2',0,10000000,'Non utilisée');
Insert into VOC_TYPE_HABILITATION ("id","obsolete","ordering","label") values ('3',0,10000000,'Autre');
REM INSERTING into VOC_TYPE_MESURE
Insert into VOC_TYPE_MESURE ("id","obsolete","ordering","label") values ('1',0,10000000,'Active');
Insert into VOC_TYPE_MESURE ("id","obsolete","ordering","label") values ('2',0,10000000,'En cours');
Insert into VOC_TYPE_MESURE ("id","obsolete","ordering","label") values ('3',0,10000000,'Facultative');
Insert into VOC_TYPE_MESURE ("id","obsolete","ordering","label") values ('4',0,10000000,'Éventuelle');
Insert into VOC_TYPE_MESURE ("id","obsolete","ordering","label") values ('6',0,10000000,'Différée');
Insert into VOC_TYPE_MESURE ("id","obsolete","ordering","label") values ('8',0,10000000,'Hors LOLF');
Insert into VOC_TYPE_MESURE ("id","obsolete","ordering","label") values ('9',0,10000000,'Autre');
REM INSERTING into VOC_TYPE_PJ
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('TEXTE',0,10000000,'Texte(s)');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('TEXTES',0,10000000,'Texte(s)');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('EXPOSE_DES_MOTIFS',0,10000000,'Exposé des motifs');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('ETUDE_IMPACT',0,10000000,'Étude d''impact');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('DECRET_PRESENTATION',0,10000000,'Décret de présentation');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('LETTRE_PM',0,10000000,'Lettre Premier Ministre');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('TRAITE',0,10000000,'Traité');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('ACCORD_INTERNATIONAL',0,10000000,'Accord international');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('AUTRE',0,10000000,'Autre(s)');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('AUTRES',0,10000000,'Autre(s)');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('ANNEXE',0,10000000,'Annexe');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('ANNEXES',0,10000000,'Annexe');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('AVIS',0,10000000,'Avis');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('COHERENT',0,10000000,'Cohérent');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('COPIES_3_LETTRES_TRANSMISSION',0,10000000,'Copies 3 lettres transmission Vélins Petite loi (Congrès)');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('DECRET_PRESIDENT_REPUBLIQUE',0,10000000,'Décret du président de la république');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('LETTRE',0,10000000,'Lettre');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('LETTRE_CONJOINTE_PRESIDENTS',0,10000000,'Lettre conjointe des président');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('LETTRE_SAISINE_CC',0,10000000,'Lettre de saisine du Conseil constitutionnel');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('LETTRE_PM_VERS_AN',0,10000000,'Lettre Premier Ministre pour l''Assemblée Nationale');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('LETTRE_PM_VERS_SENAT',0,10000000,'Lettre Premier Ministre pour le Sénat');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('LETTRE_TRANSMISSION',0,10000000,'Lettre transmission');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('LISTE_ANNEXES',0,10000000,'Liste des annexes');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('PETITE_LOI',0,10000000,'Petite loi');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('RAPPORT',0,10000000,'Rapport');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('RAPPORTS',0,10000000,'Rapport');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('TEXTE_ADOPTE',0,10000000,'Texte adopté');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('TEXTE_MOTION',0,10000000,'Texte de la motion');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('TEXTE_TRANSMIS',0,10000000,'Texte transmis');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('TRAVAUX_PREPARATOIRES',0,10000000,'Travaux préparatoires');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('CURRICULUM_VITAE',0,10000000,'Curriculum vitae');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('AMPLIATION_DECRET',0,10000000,'Ampliation Décret Président de la République');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('ECHEANCIER',0,10000000,'Echéancier');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('INSERTION_JOLD',0,10000000,'Insertions au JO-LD');
Insert into VOC_TYPE_PJ ("id","obsolete","ordering","label") values ('AMPLIATION_DECRET_PRESIDENT_REPUBLIQUE',0,10000000,'Ampliation Décret Président de la République');
REM INSERTING into VOC_VECTEUR_PUBLICATION_TP
Insert into VOC_VECTEUR_PUBLICATION_TP ("id","obsolete","ordering","label") values ('1',0,10000000,'JO');
Insert into VOC_VECTEUR_PUBLICATION_TP ("id","obsolete","ordering","label") values ('2',0,10000000,'BODMR');
Insert into VOC_VECTEUR_PUBLICATION_TP ("id","obsolete","ordering","label") values ('3',0,10000000,'Documents administratifs');
Insert into VOC_VECTEUR_PUBLICATION_TP ("id","obsolete","ordering","label") values ('4',0,10000000,'JO + Documents administratifs');
REM INSERTING into VOC_VECTEUR_PUBLICATION_TS
Insert into VOC_VECTEUR_PUBLICATION_TS ("id","obsolete","ordering","label") values ('1',0,10000000,'A date précisée');
Insert into VOC_VECTEUR_PUBLICATION_TS ("id","obsolete","ordering","label") values ('2',0,10000000,'Aucun');
Insert into VOC_VECTEUR_PUBLICATION_TS ("id","obsolete","ordering","label") values ('3',0,10000000,'De rigueur');
Insert into VOC_VECTEUR_PUBLICATION_TS ("id","obsolete","ordering","label") values ('4',0,10000000,'Urgent');
Insert into VOC_VECTEUR_PUBLICATION_TS ("id","obsolete","ordering","label") values ('5',0,10000000,'Sans tarder');
Insert into VOC_VECTEUR_PUBLICATION_TS ("id","obsolete","ordering","label") values ('6',0,10000000,'Sous embargo');
REM INSERTING into VOC_VERROU
Insert into VOC_VERROU ("id","obsolete","ordering","label") values ('1',0,10000000,' Oui');
Insert into VOC_VERROU ("id","obsolete","ordering","label") values ('2',0,10000000,' Non');
REM INSERTING into ACLR_PERMISSION
Insert into ACLR_PERMISSION (PERMISSION) values ('Browse');
Insert into ACLR_PERMISSION (PERMISSION) values ('Classify');
Insert into ACLR_PERMISSION (PERMISSION) values ('Read');
Insert into ACLR_PERMISSION (PERMISSION) values ('ReadProperties');
Insert into ACLR_PERMISSION (PERMISSION) values ('ReadRemove');
Insert into ACLR_PERMISSION (PERMISSION) values ('ReadWrite');
Insert into ACLR_PERMISSION (PERMISSION) values ('Everything');
--------------------------------------------------------
--  DDL for View V_AN_HAB_ORD_LOI
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_AN_HAB_ORD_LOI" ("HAB_DATEPUBLICATION","RAC_NUMERONOR", "RAC_TITREACTE", "RAC_DATEPUBLICATION", "HAB_NUMEROORDRE", "HAB_ARTICLE", "HAB_OBJETRIM", "HAB_TYPEHABILITATION", "HAB_MINISTEREPILOTE", "HAB_DATETERME", "HAB_CONVENTIONDEPOT", "HAB_DATESAISINECE", "HAB_DATEPREVISIONNELLECM", "HAB_OBSERVATION", "HAB_CODIFICATION", "HAB_LEGISLATURE", "ORD_DISPOSITIONHABILITATION", "ORD_NUMERONOR", "ORD_MINISTEREPILOTE", "ORD_DATESAISINECE", "ORD_DATEPREVISIONNELLECM", "ORD_DATEPUBLICATION", "ORD_TITREOFFICIEL", "ORD_CONVENTIONDEPOT", "ORD_DATELIMITEDEPOT", "LRA_NUMERONOR", "LRA_TITREACTE", "LRA_DATESAISINECE", "LRA_DATEEXAMENCE", "LRA_SECTIONCE", "LRA_DATEEXAMENCM", "LRA_CHAMBREDEPOT", "LRA_DATEDEPOT", "LRA_NUMERODEPOT", "LRA_TITREOFFICIEL", "LRA_DATEPUBLICATION", "LRA_NUMEROJOPUBLICATION") AS 
  SELECT
TM_HAB.DATEPUBLICATION AS HAB_DATEPUBLICATION,
TM_RAC.NUMERONOR AS RAC_NUMERONOR,
TM_RAC.TITREACTE AS RAC_TITREACTE,
TM_RAC.DATEPUBLICATION AS RAC_DATEPUBLICATION,
TM_HAB.NUMEROORDRE AS HAB_NUMEROORDRE,
TM_HAB.ARTICLE AS HAB_ARTICLE,
TM_HAB.OBJETRIM AS HAB_OBJETRIM,
VOC_THAB."label" AS HAB_TYPEHABILITATION,
TM_HAB.MINISTEREPILOTE AS HAB_MINISTEREPILOTE,
TM_HAB.DATETERME AS HAB_DATETERME,
TM_HAB.CONVENTIONDEPOT AS HAB_CONVENTIONDEPOT,
TM_HAB.DATESAISINECE AS HAB_DATESAISINECE,
TM_HAB.DATEPREVISIONNELLECM AS HAB_DATEPREVISIONNELLECM,
TM_HAB.OBSERVATION AS HAB_OBSERVATION,
TM_HAB.CODIFICATION AS HAB_CODIFICATION,
TM_HAB.LEGISLATURE AS HAB_LEGISLATURE,
TM_ORD.DISPOSITIONHABILITATION AS ORD_DISPOSITIONHABILITATION,
TM_ORD.NUMERONOR AS ORD_NUMERONOR,
TM_ORD.MINISTEREPILOTE AS ORD_MINISTEREPILOTE,
TM_ORD.DATESAISINECE AS ORD_DATESAISINECE,
TM_ORD.DATEPREVISIONNELLECM AS ORD_DATEPREVISIONNELLECM,
TM_ORD.DATEPUBLICATION AS ORD_DATEPUBLICATION,
TM_ORD.TITREOFFICIEL AS ORD_TITREOFFICIEL,
TM_ORD.CONVENTIONDEPOT AS ORD_CONVENTIONDEPOT,
TM_ORD.DATELIMITEDEPOT AS ORD_DATELIMITEDEPOT,
TM_LRA.NUMERONOR AS LRA_NUMERONOR,
TM_LRA.TITREACTE AS LRA_TITREACTE,
TM_LRA.DATESAISINECE AS LRA_DATESAISINECE,
TM_LRA.DATEEXAMENCE AS LRA_DATEEXAMENCE,
TM_LRA.SECTIONCE AS LRA_SECTIONCE,
TM_LRA.DATEEXAMENCM AS LRA_DATEEXAMENCM,
TM_LRA.CHAMBREDEPOT AS LRA_CHAMBREDEPOT,
TM_LRA.DATEDEPOT AS LRA_DATEDEPOT,
TM_LRA.NUMERODEPOT AS LRA_NUMERODEPOT,
TM_LRA.TITREOFFICIEL AS LRA_TITREOFFICIEL,
TM_LRA.DATEPUBLICATION AS LRA_DATEPUBLICATION,
TM_LRA.NUMEROJOPUBLICATION AS LRA_NUMEROJOPUBLICATION
FROM
 TEXTE_MAITRE TM_RAC
 INNER JOIN      ACTIVITE_NORMATIVE AN        ON TM_RAC.ID  = AN.ID AND AN.ORDONNANCE38C = '1'
 LEFT OUTER JOIN TEXM_HABILITATIONIDS TEXH    ON TM_RAC.ID  = TEXH.ID
 LEFT OUTER JOIN TEXTE_MAITRE TM_HAB          ON TEXH.ITEM  = TM_HAB.ID
	LEFT OUTER JOIN VOC_TYPE_HABILITATION VOC_THAB ON TM_HAB.TYPEHABILITATION = VOC_THAB."id"
 LEFT OUTER JOIN TEXM_ORDONNANCEIDS TEXO      ON TM_HAB.ID  = TEXO.ID
 LEFT OUTER JOIN TEXTE_MAITRE TM_ORD          ON TEXO.ITEM  = TM_ORD.ID
 LEFT OUTER JOIN TEXM_LOIRATIFICATIONIDS TEXL ON TM_ORD.ID  = TEXL.ID
 LEFT OUTER JOIN TEXTE_MAITRE TM_LRA          ON TEXL.ITEM  = TM_LRA.ID
ORDER BY TM_RAC.NUMERO, TM_ORD.DATETERME;

--------------------------------------------------------
--  DDL for View V_AN_LOI_HAB_ORDONNANCE
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_AN_LOI_HAB_ORDONNANCE" ("RAC_NUMERO", "RAC_DATEPUBLICATION", "RAC_TITREACTE", "HAB_NUMEROORDRE", "HAB_ARTICLE", "HAB_OBJETRIM", "HAB_LBLTYPEHABILITATION", "HAB_TYPEHABILITATION", "HAB_MINISTEREPILOTE", "HAB_DATESAISINECE", "HAB_DATEPREVISIONNELLECM", "HAB_LEGISLATURE", "ORD_NUMERONOR", "ORD_TITREACTE", "ORD_NUMERO", "ORD_DATEPUBLICATION", "ORD_TERMEDEPOT", "ORD_DATETERME", "ORD_DATELIMITEDEPOT", "ORD_OBSERVATION") AS 
  SELECT
TM_RAC.NUMERO AS RAC_NUMERO,
TM_RAC.DATEPUBLICATION AS RAC_DATEPUBLICATION,
TM_RAC.TITREACTE AS RAC_TITREACTE,
TM_HAB.NUMEROORDRE AS HAB_NUMEROORDRE,
TM_HAB.ARTICLE AS HAB_ARTICLE,
TM_HAB.OBJETRIM AS HAB_OBJETRIM,
VOC_THAB."label" AS HAB_LBLTYPEHABILITATION,
TM_HAB.TYPEHABILITATION AS HAB_TYPEHABILITATION,
TM_HAB.MINISTEREPILOTE AS HAB_MINISTEREPILOTE,
TM_HAB.DATESAISINECE AS HAB_DATESAISINECE,
TM_HAB.DATEPREVISIONNELLECM AS HAB_DATEPREVISIONNELLECM,
TM_HAB.LEGISLATURE AS HAB_LEGISLATURE,
TM_ORD.NUMERONOR AS ORD_NUMERONOR,
TM_ORD.TITREACTE AS ORD_TITREACTE,
TM_ORD.NUMERO AS ORD_NUMERO,
TM_ORD.DATEPUBLICATION AS ORD_DATEPUBLICATION,
TM_ORD.TERMEDEPOT AS ORD_TERMEDEPOT,
TM_ORD.DATETERME AS ORD_DATETERME,
TM_ORD.DATELIMITEDEPOT AS ORD_DATELIMITEDEPOT,
TM_ORD.OBSERVATION  AS ORD_OBSERVATION
FROM
 TEXTE_MAITRE TM_RAC
 INNER JOIN      ACTIVITE_NORMATIVE AN      ON TM_RAC.ID  = AN.ID AND AN.ORDONNANCE38C = 1
 LEFT OUTER JOIN TEXM_HABILITATIONIDS TEXH  ON TM_RAC.ID  = TEXH.ID
 LEFT OUTER JOIN TEXTE_MAITRE TM_HAB        ON TEXH.ITEM  = TM_HAB.ID
 	LEFT OUTER JOIN VOC_TYPE_HABILITATION VOC_THAB ON TM_HAB.TYPEHABILITATION = VOC_THAB."id"
 LEFT OUTER JOIN TEXM_ORDONNANCEIDS TEXO    ON TM_HAB.ID  = TEXO.ID
 LEFT OUTER JOIN TEXTE_MAITRE TM_ORD        ON TEXO.ITEM  = TM_ORD.ID
ORDER BY TM_RAC.NUMERO, TM_ORD.DATETERME;
--------------------------------------------------------
--  DDL for View V_AN_LOIS_APPELANT_MESURE
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_AN_LOIS_APPELANT_MESURE" ("LOI_ID", "LOI_MINISTERERESP", "LOI_DATEPUBLICATION", "LOI_DATEPROMULGATION", "LOI_DATEENTREEENVIGUEUR") AS 
  SELECT
TM_LOI.ID AS LOI_ID,
TM_LOI.MINISTEREPILOTE AS LOI_MINISTERERESP,
TM_LOI.DATEPUBLICATION AS LOI_DATEPUBLICATION,
TM_LOI.DATEPROMULGATION AS LOI_DATEPROMULGATION,
TM_LOI.DATEENTREEENVIGUEUR AS LOI_DATEENTREEENVIGUEUR
FROM (TEXTE_MAITRE TM_LOI INNER JOIN ACTIVITE_NORMATIVE AN ON TM_LOI.ID = AN.ID AND AN.APPLICATIONLOI = 1 )
WHERE EXISTS
(SELECT *
 FROM (TEXTE_MAITRE TM_MES INNER JOIN TEXM_MESUREIDS TEXM ON TM_MES.ID = TEXM.ITEM)
 WHERE TM_MES.TYPEMESURE = '1'
 AND   TM_LOI.ID = TEXM.ID);
--------------------------------------------------------
--  DDL for View V_AN_MESURES_APPELANT_DECRET
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_AN_MESURES_APPELANT_DECRET" ("LOI_ID", "LOI_MINISTERERESP", "LOI_DATEPUBLICATION", "LOI_DATEPROMULGATION", "LOI_DATEENTREEENVIGUEUR", "LOI_NATURETEXTE", "LOI_PROCEDUREVOTE", "MESURE_ID", "MESURE_MINISTEREPILOTE") AS 
  SELECT
TM_LOI.ID AS LOI_ID,
TM_LOI.MINISTEREPILOTE AS LOI_MINISTERERESP,
TM_LOI.DATEPUBLICATION AS LOI_DATEPUBLICATION,
TM_LOI.DATEPROMULGATION AS LOI_DATEPROMULGATION,
TM_LOI.DATEENTREEENVIGUEUR AS LOI_DATEENTREEENVIGUEUR,
TM_LOI.NATURETEXTE AS LOI_NATURETEXTE,
TM_LOI.PROCEDUREVOTE AS LOI_PROCEDUREVOTE,
TM_MES.ID AS MESURE_ID,
TM_MES.MINISTEREPILOTE AS MESURE_MINISTEREPILOTE
FROM
(TEXTE_MAITRE TM_LOI INNER JOIN ACTIVITE_NORMATIVE AN ON TM_LOI.ID = AN.ID AND AN.APPLICATIONLOI = 1 ),
(TEXTE_MAITRE TM_MES INNER JOIN TEXM_MESUREIDS TEXM   ON TM_MES.ID = TEXM.ITEM)
WHERE TM_MES.TYPEMESURE = '1'
AND   TM_LOI.ID = TEXM.ID
AND EXISTS
(SELECT *
 FROM TEXM_DECRETIDS TEXD
 WHERE TM_MES.ID = TEXD.ID);
--------------------------------------------------------
--  DDL for View V_AN_MESURES_ATTENTE_DECRET
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_AN_MESURES_ATTENTE_DECRET" ("LOI_ID", "LOI_MINISTERERESP", "LOI_DATEPUBLICATION", "LOI_DATEPROMULGATION", "LOI_DATEENTREEENVIGUEUR", "LOI_NATURETEXTE", "LOI_PROCEDUREVOTE", "MESURE_ID", "MESURE_MINISTEREPILOTE") AS 
  SELECT
TM_LOI.ID AS LOI_ID,
TM_LOI.MINISTEREPILOTE AS LOI_MINISTERERESP,
TM_LOI.DATEPUBLICATION AS LOI_DATEPUBLICATION,
TM_LOI.DATEPROMULGATION AS LOI_DATEPROMULGATION,
TM_LOI.DATEENTREEENVIGUEUR AS LOI_DATEENTREEENVIGUEUR,
TM_LOI.NATURETEXTE AS LOI_NATURETEXTE,
TM_LOI.PROCEDUREVOTE AS LOI_PROCEDUREVOTE,
TM_MES.ID AS MESURE_ID,
TM_MES.MINISTEREPILOTE AS MESURE_MINISTEREPILOTE
FROM
(TEXTE_MAITRE TM_LOI INNER JOIN ACTIVITE_NORMATIVE AN ON TM_LOI.ID = AN.ID AND AN.APPLICATIONLOI = 1 ),
(TEXTE_MAITRE TM_MES INNER JOIN TEXM_MESUREIDS TEXM   ON TM_MES.ID = TEXM.ITEM)
WHERE TM_LOI.ID = TEXM.ID
AND   TM_MES.TYPEMESURE = '1'
AND   TM_MES.APPLICATIONRECU <> '1'
AND EXISTS
  (SELECT *
  FROM TEXM_DECRETIDS TEXD
  WHERE TM_MES.ID = TEXD.ID);
--------------------------------------------------------
--  DDL for View V_AN_MESURES_DELAIS_DERNIER
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_AN_MESURES_DELAIS_DERNIER" ("LOI_ID", "LOI_DATEPROMULGATION", "LOI_NATURETEXTE", "MESURE_ID", "MESURE_MINISTEREPILOTE", "DELAIS_EN_MOIS") AS 
  SELECT
TM_LOI.ID AS LOI_ID,
TM_LOI.DATEPROMULGATION AS LOI_DATEPROMULGATION,
TM_LOI.NATURETEXTE AS LOI_NATURETEXTE,
TM_MES.ID AS MESURE_ID,
TM_MES.MINISTEREPILOTE AS MESURE_MINISTEREPILOTE,
CASE
  WHEN TM_MES.DATEENTREEENVIGUEUR IS NULL
  THEN TRUNC(abs(extract(day FROM ((TM_DEC.DATEPUBLICATION - TM_LOI.DATEPUBLICATION) day TO second))) / 30)
  ELSE TRUNC(abs(extract(day FROM ((TM_DEC.DATEPUBLICATION - TM_MES.DATEENTREEENVIGUEUR) day TO second))) / 30)
END DELAIS_EN_MOIS
FROM
(TEXTE_MAITRE TM_LOI INNER JOIN ACTIVITE_NORMATIVE AN ON TM_LOI.ID = AN.ID AND AN.APPLICATIONLOI = 1 ),
(TEXTE_MAITRE TM_MES INNER JOIN TEXM_MESUREIDS TEXM   ON TM_MES.ID = TEXM.ITEM),
(TEXTE_MAITRE TM_DEC INNER JOIN TEXM_DECRETIDS TEXD   ON TM_DEC.ID = TEXD.ITEM)
WHERE TM_LOI.ID = TEXM.ID
AND   TM_MES.TYPEMESURE = '1'
AND   TM_MES.APPLICATIONRECU = '1'
AND   TM_MES.ID = TEXD.ID
AND   TM_DEC.DATEPUBLICATION = (SELECT MAX(MX_TM_DEC.DATEPUBLICATION)
								FROM TEXM_MESUREIDS MX_TEXM,
								(TEXTE_MAITRE MX_TM_DEC INNER JOIN TEXM_DECRETIDS MX_TEXD   ON MX_TM_DEC.ID = MX_TEXD.ITEM)
								WHERE TM_MES.ID = MX_TEXM.ITEM
								AND   MX_TEXM.ID = MX_TM_DEC.ID
                                );
--------------------------------------------------------
--  DDL for View V_AN_MESURES_DELAIS_PREMIER
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_AN_MESURES_DELAIS_PREMIER" ("LOI_ID", "LOI_DATEPROMULGATION", "LOI_NATURETEXTE", "MESURE_ID", "MESURE_MINISTEREPILOTE", "DELAIS_EN_MOIS") AS 
  SELECT
TM_LOI.ID AS LOI_ID,
TM_LOI.DATEPROMULGATION AS LOI_DATEPROMULGATION,
TM_LOI.NATURETEXTE AS LOI_NATURETEXTE,
TM_MES.ID AS MESURE_ID,
TM_MES.MINISTEREPILOTE AS MESURE_MINISTEREPILOTE,
CASE
  WHEN TM_MES.DATEENTREEENVIGUEUR IS NULL
  THEN TRUNC(abs(extract(day FROM ((TM_DEC.DATEPUBLICATION - TM_LOI.DATEPUBLICATION) day TO second))) / 30)
  ELSE TRUNC(abs(extract(day FROM ((TM_DEC.DATEPUBLICATION - TM_MES.DATEENTREEENVIGUEUR) day TO second))) / 30)
END DELAIS_EN_MOIS
FROM
(TEXTE_MAITRE TM_LOI INNER JOIN ACTIVITE_NORMATIVE AN ON TM_LOI.ID = AN.ID AND AN.APPLICATIONLOI = 1 ),
(TEXTE_MAITRE TM_MES INNER JOIN TEXM_MESUREIDS TEXM   ON TM_MES.ID = TEXM.ITEM),
(TEXTE_MAITRE TM_DEC INNER JOIN TEXM_DECRETIDS TEXD   ON TM_DEC.ID = TEXD.ITEM)
WHERE TM_LOI.ID = TEXM.ID
AND   TM_MES.TYPEMESURE = '1'
AND   TM_MES.APPLICATIONRECU = '1'
AND   TM_MES.ID = TEXD.ID
AND   TM_DEC.DATEPUBLICATION = (SELECT MIN(MN_TM_DEC.DATEPUBLICATION)
								FROM TEXM_MESUREIDS MN_TEXM,
								(TEXTE_MAITRE MN_TM_DEC INNER JOIN TEXM_DECRETIDS MN_TEXD   ON MN_TM_DEC.ID = MN_TEXD.ITEM)
								WHERE TM_MES.ID = MN_TEXM.ITEM
								AND   MN_TEXM.ID = MN_TM_DEC.ID
                                );
--------------------------------------------------------
--  DDL for View V_AN_MESURES_PUBLIEES
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_AN_MESURES_PUBLIEES" ("TM_MESURE_ID", "JOUR_DE_PUBLICATION", "MOIS_DE_PUBLICATION", "ANNEE_DE_PUBLICATION") AS 
  SELECT
TM_MES.ID AS TM_MESURE_ID,
EXTRACT(DAY FROM TM_DEC.DATEPUBLICATION) AS JOUR_DE_PUBLICATION,
EXTRACT(MONTH FROM TM_DEC.DATEPUBLICATION) AS MOIS_DE_PUBLICATION,
EXTRACT(YEAR FROM TM_DEC.DATEPUBLICATION)  AS ANNEE_DE_PUBLICATION
FROM
(TEXTE_MAITRE TM_LOI INNER JOIN ACTIVITE_NORMATIVE AN ON TM_LOI.ID = AN.ID AND AN.APPLICATIONLOI = 1 ),
(TEXTE_MAITRE TM_MES INNER JOIN TEXM_MESUREIDS TEXM   ON TM_MES.ID = TEXM.ITEM),
(TEXTE_MAITRE TM_DEC INNER JOIN TEXM_DECRETIDS TEXD   ON TM_DEC.ID = TEXD.ITEM)
WHERE TM_LOI.ID = TEXM.ID
AND   TM_MES.TYPEMESURE = '1'
AND   TM_MES.APPLICATIONRECU = '1'
AND   TM_MES.ID = TEXD.ID
AND   TM_DEC.DATEPUBLICATION = (SELECT MAX(MX_TM_DEC.DATEPUBLICATION)
								FROM TEXM_MESUREIDS MX_TEXM,
								(TEXTE_MAITRE MX_TM_DEC INNER JOIN TEXM_DECRETIDS MX_TEXD   ON MX_TM_DEC.ID = MX_TEXD.ITEM)
								WHERE TM_MES.ID = MX_TEXM.ITEM
								AND   MX_TEXM.ID = MX_TM_DEC.ID
                                );
--------------------------------------------------------
--  DDL for View V_AN_MESURES_RECU_APPLICATION
--------------------------------------------------------

CREATE OR REPLACE VIEW V_AN_MESURES_RECU_APPLICATION  AS
SELECT
TM_LOI.ID AS LOI_ID,
TM_LOI.MINISTEREPILOTE AS LOI_MINISTERERESP,
TM_LOI.DATEPUBLICATION AS LOI_DATEPUBLICATION,
TM_LOI.DATEPROMULGATION AS LOI_DATEPROMULGATION,
TM_LOI.DATEENTREEENVIGUEUR AS LOI_DATEENTREEENVIGUEUR,
TM_LOI.NATURETEXTE AS LOI_NATURETEXTE,
TM_LOI.PROCEDUREVOTE AS LOI_PROCEDUREVOTE,
TM_MES.ID AS MESURE_ID,
TM_MES.MINISTEREPILOTE AS MESURE_MINISTEREPILOTE,
TM_DEC.ID AS DECRET_ID,
TM_DEC.DATEPUBLICATION AS DECRET_DATEPUBLICATION,
TM_MES.DATEMISEAPPLICATION AS MES_DATEMISEAPPLICATION,
TM_MES.DATEENTREEENVIGUEUR as MES_DATEENTREEENVIGUEUR,
TM_MES.DIFFERE as MES_DIFFERE
FROM
(TEXTE_MAITRE TM_LOI INNER JOIN ACTIVITE_NORMATIVE AN ON TM_LOI.ID = AN.ID AND AN.APPLICATIONLOI = '1' ),
(TEXTE_MAITRE TM_MES INNER JOIN TEXM_MESUREIDS TEXM   ON TM_MES.ID = TEXM.ITEM),
(TEXTE_MAITRE TM_DEC INNER JOIN TEXM_DECRETIDS TEXD   ON TM_DEC.ID = TEXD.ITEM)
WHERE TM_LOI.ID = TEXM.ID
AND   TM_MES.TYPEMESURE = '1'
AND   TM_MES.APPLICATIONRECU = '1'
AND   TM_MES.ID = TEXD.ID
AND   TM_DEC.DATEPUBLICATION = (SELECT MAX(MX_TM_DEC.DATEPUBLICATION)
        FROM TEXM_MESUREIDS MX_TEXM,
        (TEXTE_MAITRE MX_TM_DEC INNER JOIN TEXM_DECRETIDS MX_TEXD   ON MX_TM_DEC.ID = MX_TEXD.ITEM)
        WHERE TM_MES.ID = MX_TEXD.ID
        AND   MX_TEXM.ID = MX_TM_DEC.ID
                                );
--------------------------------------------------------
--  DDL for View V_AN_ORDONNANCE_38
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_AN_ORDONNANCE_38" ("RAC_NUMERO", "RAC_TITREACTE", "ORD_NUMERONOR", "ORD_MINISTEREPILOTE", "ORD_TITREACTE", "ORD_NUMERO", "ORD_DATEPUBLICATION", "LRA_DATELIMITEDEPOT", "LRA_CONVENTIONDEPOT", "LRA_NUMERONOR", "LRA_TITREACTE", "LRA_CHAMBREDEPOT", "LRA_DATEDEPOT", "LRA_NUMERODEPOT", "LRA_TITREOFFICIEL", "LRA_DATEPUBLICATION") AS 
  SELECT
TM_RAC.NUMERO AS RAC_NUMERO,
TM_RAC.TITREACTE AS RAC_TITREACTE,
TM_ORD.NUMERONOR AS ORD_NUMERONOR,
TM_ORD.MINISTEREPILOTE AS ORD_MINISTEREPILOTE,
TM_ORD.TITREACTE AS ORD_TITREACTE,
TM_ORD.NUMERO AS ORD_NUMERO,
TM_ORD.DATEPUBLICATION AS ORD_DATEPUBLICATION,
TM_LRA.DATELIMITEDEPOT AS LRA_DATELIMITEDEPOT,
TM_LRA.CONVENTIONDEPOT AS LRA_CONVENTIONDEPOT,
TM_LRA.NUMERONOR AS LRA_NUMERONOR,
TM_LRA.TITREACTE AS LRA_TITREACTE,
TM_LRA.CHAMBREDEPOT AS LRA_CHAMBREDEPOT,
TM_LRA.DATEDEPOT AS LRA_DATEDEPOT,
TM_LRA.NUMERODEPOT AS LRA_NUMERODEPOT,
TM_LRA.TITREOFFICIEL AS LRA_TITREOFFICIEL,
TM_LRA.DATEPUBLICATION AS LRA_DATEPUBLICATION
FROM
 (TEXTE_MAITRE TM_ORD
 INNER JOIN      ACTIVITE_NORMATIVE AN      ON TM_ORD.ID  = AN.ID AND AN.ORDONNANCE = 1 AND  TM_ORD.DISPOSITIONHABILITATION = 1)
 LEFT OUTER JOIN
 ( SELECT TEXO.ITEM, TM_RAC.* FROM
   TEXM_ORDONNANCEIDS TEXO
   INNER JOIN      TEXTE_MAITRE TM_HAB        ON TEXO.ID    = TM_HAB.ID
   INNER JOIN      TEXM_HABILITATIONIDS TEXH  ON TM_HAB.ID  = TEXH.ITEM
   INNER JOIN      TEXTE_MAITRE TM_RAC        ON TEXH.ID    = TM_RAC.ID ) TM_RAC ON TM_ORD.ID = TM_RAC.ITEM
 LEFT OUTER JOIN   TEXM_LOIRATIFICATIONIDS  TEXL   ON TM_ORD.ID = TEXL.ID
 LEFT OUTER JOIN   TEXTE_MAITRE TM_LRA             ON TEXL.ITEM = TM_LRA.ID
 ORDER BY TM_LRA.DATELIMITEDEPOT;
--------------------------------------------------------
--  DDL for View V_AN_ORDONNANCE_74
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_AN_ORDONNANCE_74" ("ORD_MINISTEREPILOTE", "ORD_NUMERONOR", "ORD_NUMERO", "ORD_TITREACTE", "ORD_DATEPUBLICATION", "LRA_DATELIMITEDEPOT", "LRA_NUMERONOR", "LRA_TITREACTE", "LRA_DATEPUBLICATION") AS 
  SELECT
TM_ORD.MINISTEREPILOTE AS ORD_MINISTEREPILOTE,
TM_ORD.NUMERONOR AS ORD_NUMERONOR,
TM_ORD.NUMERO AS ORD_NUMERO,
TM_ORD.TITREACTE AS ORD_TITREACTE,
TM_ORD.DATEPUBLICATION AS ORD_DATEPUBLICATION,
TM_LRA.DATELIMITEDEPOT AS LRA_DATELIMITEDEPOT,
TM_LRA.NUMERONOR AS LRA_NUMERONOR,
TM_LRA.TITREACTE AS LRA_TITREACTE,
TM_LRA.DATEPUBLICATION AS LRA_DATEPUBLICATION
FROM
 (TEXTE_MAITRE TM_ORD
 INNER JOIN      ACTIVITE_NORMATIVE AN         ON TM_ORD.ID  = AN.ID AND AN.ORDONNANCE = 1 AND  TM_ORD.DISPOSITIONHABILITATION = 0)
 LEFT OUTER JOIN TEXM_LOIRATIFICATIONIDS  TEXL ON TM_ORD.ID  = TEXL.ID
 LEFT OUTER JOIN TEXTE_MAITRE TM_LRA           ON TEXL.ITEM  = TM_LRA.ID
 ORDER BY TM_LRA.DATELIMITEDEPOT;
--------------------------------------------------------
--  DDL for View V_AN_TRAITE_ACCORD
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_AN_TRAITE_ACCORD" ("TRAIT_ID", "TRAIT_CATEGORIE", "TRAIT_ORGANISATION", "TRAIT_THEMATIQUE", "TRAIT_DEGREPRIORITE", "TRAIT_TITREACTE", "TRAIT_DATESIGNATURE", "TRAIT_DATEENTREEENVIGUEUR", "TRAIT_MINISTEREPILOTE", "TRAIT_DATEPJL", "TRAIT_DISPOETUDEIMPACT", "TRAIT_ETUDEIMPACT", "TRAIT_DATEDEPOTRATIFICATION", "TRAIT_AUTORISATIONRATIFICATION", "LRAT_ID", "LRAT_NUMERONOR", "LRAT_ETAPESOLON", "LRAT_DATEPUBLICATION", "LRAT_TITREACTE", "DECRET_NUMERONOR", "DECRET_ETAPESOLON", "DECRET_NUMERO", "DECRET_DATEPUBLICATION", "DECRET_TITREACTE", "DECRET_OBSERVATION") AS 
  SELECT
TM_TRAIT.ID AS TRAIT_ID,
TM_TRAIT.CATEGORIE AS TRAIT_CATEGORIE,
TM_TRAIT.ORGANISATION AS TRAIT_ORGANISATION,
TM_TRAIT.THEMATIQUE AS TRAIT_THEMATIQUE,
TM_TRAIT.DEGREPRIORITE AS TRAIT_DEGREPRIORITE,
TM_TRAIT.TITREACTE AS TRAIT_TITREACTE,
TM_TRAIT.DATESIGNATURE AS TRAIT_DATESIGNATURE,
TM_TRAIT.DATEENTREEENVIGUEUR AS TRAIT_DATEENTREEENVIGUEUR,
TM_TRAIT.MINISTEREPILOTE AS TRAIT_MINISTEREPILOTE,
TM_TRAIT.DATEPJL AS TRAIT_DATEPJL,
TM_TRAIT.DISPOETUDEIMPACT AS TRAIT_DISPOETUDEIMPACT,
TM_TRAIT.ETUDEIMPACT AS TRAIT_ETUDEIMPACT,
TM_TRAIT.DATEDEPOTRATIFICATION AS TRAIT_DATEDEPOTRATIFICATION,
TM_TRAIT.AUTORISATIONRATIFICATION AS TRAIT_AUTORISATIONRATIFICATION,
TM_LRAT.ID AS LRAT_ID,
TM_LRAT.NUMERONOR AS LRAT_NUMERONOR,
TM_LRAT.ETAPESOLON AS LRAT_ETAPESOLON,
TM_LRAT.DATEPUBLICATION AS LRAT_DATEPUBLICATION,
TM_LRAT.TITREACTE AS LRAT_TITREACTE,
TM_TRAIT.NUMERONOR AS DECRET_NUMERONOR,
TM_TRAIT.ETAPESOLON AS DECRET_ETAPESOLON,
TM_TRAIT.NUMERO AS DECRET_NUMERO,
TM_TRAIT.DATEPUBLICATION AS DECRET_DATEPUBLICATION,
TM_TRAIT.TITREACTE AS DECRET_TITREACTE,
TM_TRAIT.OBSERVATION AS DECRET_OBSERVATION
FROM
 TEXTE_MAITRE TM_TRAIT
 INNER JOIN      ACTIVITE_NORMATIVE AN ON TM_TRAIT.ID = AN.ID AND AN.TRAITE = 1
 LEFT OUTER JOIN TEXTE_MAITRE TM_LRAT  ON TM_TRAIT.NORLOIRATIFICATION = TM_LRAT.NUMERONOR;
--------------------------------------------------------
--  DDL for View V_AN_TRANSPOSITION_DIRECTIVE
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_AN_TRANSPOSITION_DIRECTIVE" ("NUMERO", "DATEDIRECTIVE", "TITRE", "DATEPROCHAINTABAFFICHAGE", "TD_MINISTEREPILOTE", "TABAFFICHAGEMARCHEINT", "ACHEVEE", "TD_OBSERVATION", "DATEECHEANCE", "NATURE", "NUMERONOR", "TT_MINISTERE_PILOTE", "ETAPESOLON", "NUMEROTEXTEPUBLIE", "TITRETEXTEPUBLIE", "DATEPUBLICATION") AS 
  SELECT
TM_DIR.NUMERO , TM_DIR.DATEDIRECTIVE, TM_DIR.TITREACTE AS TITRE, TM_DIR.DATEPROCHAINTABAFFICHAGE, TM_DIR.MINISTEREPILOTE AS TD_MINISTEREPILOTE,
TM_DIR.TABAFFICHAGEMARCHEINT, TM_DIR.ACHEVEE, TM_DIR.OBSERVATION AS TD_OBSERVATION, TM_DIR.DATEECHEANCE,
VOC."label" AS NATURE, TM_TT.NUMERONOR, TM_TT.MINISTEREPILOTE AS TT_MINISTERE_PILOTE, TM_TT.ETAPESOLON, TM_TT.NUMEROTEXTEPUBLIE,
TM_TT.TITRETEXTEPUBLIE, TM_TT.DATEPUBLICATION
FROM
((((
 TEXTE_MAITRE TM_DIR
 INNER JOIN      ACTIVITE_NORMATIVE AN ON TM_DIR.ID = AN.ID AND AN.TRANSPOSITION = 1 )
 LEFT OUTER JOIN TEXM_DOSSIERSNOR TEXN ON TM_DIR.ID = TEXN.ID)
 LEFT OUTER JOIN TEXTE_MAITRE TM_TT    ON TEXN.ITEM = TM_TT.NUMERONOR)
 LEFT OUTER JOIN VOC_ACTE_TYPE VOC     ON TM_TT.NATURETEXTE = VOC."id");
--------------------------------------------------------
--  DDL for View V_BORDEREAU_AUTRE_LISTE
--------------------------------------------------------

  
  CREATE OR REPLACE VIEW "V_BORDEREAU_AUTRE_LISTE" ("MINISTERERESP", "NUMERONOR", "TITREACTE", "PUBLICATIONEPREUVEENRETOUR", "PUBLICATIONDELAI", "PUBLICATIONDATEDEMANDE", "TYPELISTE", "NUMEROLISTE", "ID", "DOCID", "DATEGENERATIONLISTE", "TYPESIGNATURE", "PUBTPLABEL", "MODPARULABEL", "PUBDELAILABEL", "VECTEURPUBLICATION") AS 
  SELECT DOS.MINISTERERESP, DOS.NUMERONOR,
       DOS.TITREACTE,
       TP.PUBLICATIONEPREUVEENRETOUR,
       TP.PUBLICATIONDELAI,
       TP.PUBLICATIONDATEDEMANDE,
       LISTE.TYPELISTE, LISTE.NUMEROLISTE,
       LISTE.ID,
       TP.ID as DOCID,
       LISTE.DATEGENERATIONLISTE,
       LISTE.TYPESIGNATURE,
       NVL(VOCPUBTP."VPINTITULE",' ') AS PUBTPLABEL,
       NVL(VOCMODEPARU."MODE",'') AS MODPARULABEL,
       NVL(VOCPUBDEL."label",' ') AS PUBDELAILABEL,
       NVL(VOCVEC."VPINTITULE",'') AS VECTEURPUBLICATION
  FROM DOSSIER_SOLON_EPG DOS,
       TRAITEMENT_PAPIER TP,
       LIS_IDSDOSSIER LISDOS,
       LISTE_TRAITEMENT_PAPI_144E75F6 LISTE,
       VECTEUR_PUBLICATION VOCPUBTP,
       MODE_PARUTION VOCMODEPARU,
       VOC_PUBLICATION_DELAI VOCPUBDEL,
       VECTEUR_PUBLICATION VOCVEC
 WHERE DOS.ID = TP.ID
   AND DOS.ID = LISDOS.ITEM
   AND LISDOS.ID = LISTE.ID
   AND (LISTE.TYPELISTE = '2' OR LISTE.TYPELISTE = '3')
   AND TP.PUBLICATIONNOMPUBLICATION = VOCPUBTP."ID"(+)
   AND TP.PUBLICATIONMODEPUBLICATION = VOCMODEPARU."ID"(+)
   AND TP.PUBLICATIONDELAI = VOCPUBDEL."id"(+)
   AND TP.PUBLICATIONNOMPUBLICATION = VOCVEC."ID"(+);


--------------------------------------------------------
--  DDL for View V_BORDEREAU_COMMUNICATION
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_BORDEREAU_COMMUNICATION" ("JOIN_ID", "ID", "SIGNATAIRE", "NUMERONOR", "TITREACTE", "PRIORITE", "PRIORITELABEL", "RESPDOSSIER", "TELRESPDOSSIER", "PERSONNESNOMMEES", "TYPEACTE", "TEXTESOUMISAVALIDATION", "TEXTEAPUBLIER") AS 
  SELECT
    1 AS JOIN_ID,
    DOS.ID,
    TP.SIGNATAIRE,
    DOS.NUMERONOR,
    DOS.TITREACTE,
    TP.PRIORITE,
    NVL(VOCPRIORITE."label",' ') AS PRIORITELABEL,
    NVL(DOS.PRENOMRESPDOSSIER,' ') || ' ' || NVL(DOS.NOMRESPDOSSIER,' ') AS RESPDOSSIER,
    NVL(DOS.TELRESPDOSSIER,' ') AS TELRESPDOSSIER,
    TP.PERSONNESNOMMEES,
	  DOS.TYPEACTE,
	  TP.TEXTESOUMISAVALIDATION,
	  TP.TEXTEAPUBLIER
FROM
    DOSSIER_SOLON_EPG DOS,
    TRAITEMENT_PAPIER TP,
    VOC_PAPIER_PRIORITE  VOCPRIORITE
WHERE DOS.ID = TP.ID
AND	  TP.PRIORITE=VOCPRIORITE."id"(+);
--------------------------------------------------------
--  DDL for View V_BORDEREAU_ENVOI_EPR_RELC
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_BORDEREAU_ENVOI_EPR_RELC" ("ID", "SIGNATAIRE", "NUMERONOR", "TITREACTE", "PRIORITE", "PRIORITELABEL", "RESPDOSSIER", "TELRESPDOSSIER") AS 
  SELECT
	DOS.ID,
	TP.SIGNATAIRE,
	DOS.NUMERONOR,
	DOS.TITREACTE,
	TP.PRIORITE,
	NVL(VOCPRIORITE."label",' ') AS PRIORITELABEL,
	NVL(DOS.PRENOMRESPDOSSIER,' ') || ' ' || NVL(DOS.NOMRESPDOSSIER,' ') AS RESPDOSSIER,
	NVL(DOS.TELRESPDOSSIER,' ') AS TELRESPDOSSIER
FROM
	DOSSIER_SOLON_EPG DOS,
	TRAITEMENT_PAPIER TP,
	VOC_PAPIER_PRIORITE VOCPRIORITE
WHERE DOS.ID   = TP.ID
AND   TP.PRIORITE=VOCPRIORITE."id"(+);
--------------------------------------------------------
--  DDL for View V_BORDEREAU_SIGNATURE_LISTE
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_BORDEREAU_SIGNATURE_LISTE" ("MINISTERERESP", "NUMERONOR", "TYPELISTE", "NUMEROLISTE", "DATEGENERATIONLISTE", "TYPESIGNATURE", "TITREACTE", "ID") AS 
  SELECT DOS.MINISTERERESP,
        DOS.NUMERONOR,
        LISTE.TYPELISTE,
        LISTE.NUMEROLISTE,
        LISTE.DATEGENERATIONLISTE,
        LISTE.TYPESIGNATURE,
        DOS.TITREACTE,
        LISTE.ID
  FROM DOSSIER_SOLON_EPG DOS,LIS_IDSDOSSIER LISDOS,LISTE_TRAITEMENT_PAPI_144E75F6 LISTE
  WHERE DOS.ID=LISDOS.ITEM
	AND LISDOS.ID=LISTE.ID
	AND LISTE.TYPELISTE = '1' ;
--------------------------------------------------------
--  DDL for View V_BORDEREU_RETOUR
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_BORDEREU_RETOUR" ("ID", "NUMERONOR", "TITREACTE", "MOTIFRETOUR", "RETOURA", "RESPDOSSIER", "TELRESPDOSSIER") AS 
  SELECT  DOS.ID,
        DOS.NUMERONOR,
        NVL(DOS.TITREACTE,' ') AS TITREACTE,
        NVL(TP.MOTIFRETOUR,' ') AS MOTIFRETOUR,
        NVL(TP.RETOURA,' ') AS RETOURA,
        NVL(DOS.PRENOMRESPDOSSIER,' ') || ' ' || NVL(DOS.NOMRESPDOSSIER,' ') AS RESPDOSSIER,
        NVL(DOS.TELRESPDOSSIER,' ') AS TELRESPDOSSIER
FROM DOSSIER_SOLON_EPG DOS,
     TRAITEMENT_PAPIER TP
WHERE DOS.ID = TP.ID;
--------------------------------------------------------
--  DDL for View V_STATS_LISTE_ACTE_TYPE_15
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_STATS_LISTE_ACTE_TYPE_15" ("ID", "DATESIGNATURE", "ISURGENT", "POSTECREATOR", "ISPARAPHEURTYPEACTEUPDATED", "INDEXATIONMIN", "INDEXATIONDIRPUB", "DATEPUBLICATION", "DECRETNUMEROTE", "HABILITATIONNUMEROARTICLES", "NUMEROVERSIONACTEOUEXTRAIT", "PUBLICATIONRAPPORTPRESENTATION", "DISPOSITIONHABILITATION", "DATEDEMAINTIENENPRODUCTION", "INDEXATIONDIR", "ISPARAPHEURCOMPLET", "TITREACTE", "ZONECOMSGGDILA", "IDDOSSIER", "POURFOURNITUREEPREUVE", "INDEXATIONSGG", "HABILITATIONCOMMENTAIRE", "HABILITATIONTERME", "DATEENVOIJOTS", "INDEXATIONSGGPUB", "NBDOSSIERRECTIFIE", "NOMCOMPLETRESPDOSSIER", "DATECANDIDATURE", "ARRIVEESOLONTS", "ISDOSSIERISSUINJECTION", "ISACTEREFERENCEFORNUM_302A1DC6", "DATEVERSEMENTTS", "DATEVERSEMENTARCHIVAG_E55E9FEF", "HABILITATIONNUMEROORDRE", "HABILITATIONREFLOI", "MESURENOMINATIVE", "ISPARAPHEURFICHIERINF_54BB2DAB", "DATEPRECISEEPUBLICATION", "HABILITATIONDATETERME", "INDEXATIONMINPUB", "STATUT", "STATUTARCHIVAGE", "NUMERONOR", "CATEGORIEACTE", "IDDOCUMENTPARAPHEUR", "TYPEACTE", "PUBLICATIONINTEGRALEOUEXTRAIT", "MINISTEREATTACHE", "BASELEGALEACTE", "LASTDOCUMENTROUTE", "DIRECTIONRESP", "MINISTERERESP", "IDDOCUMENTFDD", "DIRECTIONATTACHE", "DELAIPUBLICATION", "NOMRESPDOSSIER", "PRENOMRESPDOSSIER", "QUALITERESPDOSSIER", "MAILRESPDOSSIER", "TELRESPDOSSIER", "IDCREATEURDOSSIER", "CANDIDAT", "DISTRIBUTIONMAILBOXID") AS 
  SELECT DOS."ID",DOS."DATESIGNATURE",DOS."ISURGENT",DOS."POSTECREATOR",DOS."ISPARAPHEURTYPEACTEUPDATED",DOS."INDEXATIONMIN",DOS."INDEXATIONDIRPUB",DOS."DATEPUBLICATION",DOS."DECRETNUMEROTE",DOS."HABILITATIONNUMEROARTICLES",DOS."NUMEROVERSIONACTEOUEXTRAIT",DOS."PUBLICATIONRAPPORTPRESENTATION",DOS."DISPOSITIONHABILITATION",DOS."DATEDEMAINTIENENPRODUCTION",DOS."INDEXATIONDIR",DOS."ISPARAPHEURCOMPLET",DOS."TITREACTE",DOS."ZONECOMSGGDILA",DOS."IDDOSSIER",DOS."POURFOURNITUREEPREUVE",DOS."INDEXATIONSGG",DOS."HABILITATIONCOMMENTAIRE",DOS."HABILITATIONTERME",DOS."DATEENVOIJOTS",DOS."INDEXATIONSGGPUB",DOS."NBDOSSIERRECTIFIE",DOS."NOMCOMPLETRESPDOSSIER",DOS."DATECANDIDATURE",DOS."ARRIVEESOLONTS",DOS."ISDOSSIERISSUINJECTION",DOS."ISACTEREFERENCEFORNUM_302A1DC6",DOS."DATEVERSEMENTTS",DOS."DATEVERSEMENTARCHIVAG_E55E9FEF",DOS."HABILITATIONNUMEROORDRE",DOS."HABILITATIONREFLOI",DOS."MESURENOMINATIVE",DOS."ISPARAPHEURFICHIERINF_54BB2DAB",DOS."DATEPRECISEEPUBLICATION",DOS."HABILITATIONDATETERME",DOS."INDEXATIONMINPUB",DOS."STATUT",DOS."STATUTARCHIVAGE",DOS."NUMERONOR",DOS."CATEGORIEACTE",DOS."IDDOCUMENTPARAPHEUR",DOS."TYPEACTE",DOS."PUBLICATIONINTEGRALEOUEXTRAIT",DOS."MINISTEREATTACHE",DOS."BASELEGALEACTE",DOS."LASTDOCUMENTROUTE",DOS."DIRECTIONRESP",DOS."MINISTERERESP",DOS."IDDOCUMENTFDD",DOS."DIRECTIONATTACHE",DOS."DELAIPUBLICATION",DOS."NOMRESPDOSSIER",DOS."PRENOMRESPDOSSIER",DOS."QUALITERESPDOSSIER",DOS."MAILRESPDOSSIER",DOS."TELRESPDOSSIER",DOS."IDCREATEURDOSSIER",DOS."CANDIDAT", ROU.DISTRIBUTIONMAILBOXID
FROM DOSSIER_SOLON_EPG DOS, HIERARCHY H, ROUTING_TASK ROU
WHERE DOS.LASTDOCUMENTROUTE = H.PARENTID
AND   DOS.STATUTARCHIVAGE IN ('1', '2')
AND   H.ID = ROU.ID
AND   ROU.TYPE = 15;
--------------------------------------------------------
--  DDL for View V_STATS_LISTE_ALL_ACTE
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_STATS_LISTE_ALL_ACTE" ("ID", "DATESIGNATURE", "ISURGENT", "POSTECREATOR", "ISPARAPHEURTYPEACTEUPDATED", "INDEXATIONMIN", "INDEXATIONDIRPUB", "DATEPUBLICATION", "DECRETNUMEROTE", "HABILITATIONNUMEROARTICLES", "NUMEROVERSIONACTEOUEXTRAIT", "PUBLICATIONRAPPORTPRESENTATION", "DISPOSITIONHABILITATION", "DATEDEMAINTIENENPRODUCTION", "INDEXATIONDIR", "ISPARAPHEURCOMPLET", "TITREACTE", "ZONECOMSGGDILA", "IDDOSSIER", "POURFOURNITUREEPREUVE", "INDEXATIONSGG", "HABILITATIONCOMMENTAIRE", "HABILITATIONTERME", "DATEENVOIJOTS", "INDEXATIONSGGPUB", "NBDOSSIERRECTIFIE", "NOMCOMPLETRESPDOSSIER", "DATECANDIDATURE", "ARRIVEESOLONTS", "ISDOSSIERISSUINJECTION", "ISACTEREFERENCEFORNUM_302A1DC6", "DATEVERSEMENTTS", "DATEVERSEMENTARCHIVAG_E55E9FEF", "HABILITATIONNUMEROORDRE", "HABILITATIONREFLOI", "MESURENOMINATIVE", "ISPARAPHEURFICHIERINF_54BB2DAB", "DATEPRECISEEPUBLICATION", "HABILITATIONDATETERME", "INDEXATIONMINPUB", "STATUT", "STATUTARCHIVAGE", "NUMERONOR", "CATEGORIEACTE", "IDDOCUMENTPARAPHEUR", "TYPEACTE", "PUBLICATIONINTEGRALEOUEXTRAIT", "MINISTEREATTACHE", "BASELEGALEACTE", "LASTDOCUMENTROUTE", "DIRECTIONRESP", "MINISTERERESP", "IDDOCUMENTFDD", "DIRECTIONATTACHE", "DELAIPUBLICATION", "NOMRESPDOSSIER", "PRENOMRESPDOSSIER", "QUALITERESPDOSSIER", "MAILRESPDOSSIER", "TELRESPDOSSIER", "IDCREATEURDOSSIER", "CANDIDAT") AS 
  SELECT DOS."ID",DOS."DATESIGNATURE",DOS."ISURGENT",DOS."POSTECREATOR",DOS."ISPARAPHEURTYPEACTEUPDATED",DOS."INDEXATIONMIN",DOS."INDEXATIONDIRPUB",DOS."DATEPUBLICATION",DOS."DECRETNUMEROTE",DOS."HABILITATIONNUMEROARTICLES",DOS."NUMEROVERSIONACTEOUEXTRAIT",DOS."PUBLICATIONRAPPORTPRESENTATION",DOS."DISPOSITIONHABILITATION",DOS."DATEDEMAINTIENENPRODUCTION",DOS."INDEXATIONDIR",DOS."ISPARAPHEURCOMPLET",DOS."TITREACTE",DOS."ZONECOMSGGDILA",DOS."IDDOSSIER",DOS."POURFOURNITUREEPREUVE",DOS."INDEXATIONSGG",DOS."HABILITATIONCOMMENTAIRE",DOS."HABILITATIONTERME",DOS."DATEENVOIJOTS",DOS."INDEXATIONSGGPUB",DOS."NBDOSSIERRECTIFIE",DOS."NOMCOMPLETRESPDOSSIER",DOS."DATECANDIDATURE",DOS."ARRIVEESOLONTS",DOS."ISDOSSIERISSUINJECTION",DOS."ISACTEREFERENCEFORNUM_302A1DC6",DOS."DATEVERSEMENTTS",DOS."DATEVERSEMENTARCHIVAG_E55E9FEF",DOS."HABILITATIONNUMEROORDRE",DOS."HABILITATIONREFLOI",DOS."MESURENOMINATIVE",DOS."ISPARAPHEURFICHIERINF_54BB2DAB",DOS."DATEPRECISEEPUBLICATION",DOS."HABILITATIONDATETERME",DOS."INDEXATIONMINPUB",DOS."STATUT",DOS."STATUTARCHIVAGE",DOS."NUMERONOR",DOS."CATEGORIEACTE",DOS."IDDOCUMENTPARAPHEUR",DOS."TYPEACTE",DOS."PUBLICATIONINTEGRALEOUEXTRAIT",DOS."MINISTEREATTACHE",DOS."BASELEGALEACTE",DOS."LASTDOCUMENTROUTE",DOS."DIRECTIONRESP",DOS."MINISTERERESP",DOS."IDDOCUMENTFDD",DOS."DIRECTIONATTACHE",DOS."DELAIPUBLICATION",DOS."NOMRESPDOSSIER",DOS."PRENOMRESPDOSSIER",DOS."QUALITERESPDOSSIER",DOS."MAILRESPDOSSIER",DOS."TELRESPDOSSIER",DOS."IDCREATEURDOSSIER",DOS."CANDIDAT"
FROM DOSSIER_SOLON_EPG DOS
WHERE DOS.STATUTARCHIVAGE IN ('1', '2');
--------------------------------------------------------
--  DDL for View V_STATS_LISTE_ALL_ACTE_DETAIL
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_STATS_LISTE_ALL_ACTE_DETAIL" ("ID", "MINISTEREATTACHE", "DATERETOUR", "POS", "DISTRIBUTIONMAILBOXID", "TYPE") AS 
  SELECT  DOS.ID ID,
		DOS.MINISTEREATTACHE MINISTEREATTACHE,
		TP.DATERETOUR DATERETOUR,
		H.POS POS,
		ROU.DISTRIBUTIONMAILBOXID DISTRIBUTIONMAILBOXID,
		ROU.TYPE TYPE
FROM DOSSIER_SOLON_EPG DOS, TRAITEMENT_PAPIER TP, HIERARCHY H, ROUTING_TASK ROU
WHERE DOS.ID = TP.ID
AND   DOS.LASTDOCUMENTROUTE = H.PARENTID
And   Dos.Statutarchivage In ('1', '2')
AND   H.ID = ROU.ID;
--------------------------------------------------------
--  DDL for View V_STATS_LISTE_EPREUVE_RELEC
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_STATS_LISTE_EPREUVE_RELEC" ("ID", "DATESIGNATURE", "ISURGENT", "POSTECREATOR", "ISPARAPHEURTYPEACTEUPDATED", "INDEXATIONMIN", "INDEXATIONDIRPUB", "DATEPUBLICATION", "DECRETNUMEROTE", "HABILITATIONNUMEROARTICLES", "NUMEROVERSIONACTEOUEXTRAIT", "PUBLICATIONRAPPORTPRESENTATION", "DISPOSITIONHABILITATION", "DATEDEMAINTIENENPRODUCTION", "INDEXATIONDIR", "ISPARAPHEURCOMPLET", "TITREACTE", "ZONECOMSGGDILA", "IDDOSSIER", "POURFOURNITUREEPREUVE", "INDEXATIONSGG", "HABILITATIONCOMMENTAIRE", "HABILITATIONTERME", "DATEENVOIJOTS", "INDEXATIONSGGPUB", "NBDOSSIERRECTIFIE", "NOMCOMPLETRESPDOSSIER", "DATECANDIDATURE", "ARRIVEESOLONTS", "ISDOSSIERISSUINJECTION", "ISACTEREFERENCEFORNUM_302A1DC6", "DATEVERSEMENTTS", "DATEVERSEMENTARCHIVAG_E55E9FEF", "HABILITATIONNUMEROORDRE", "HABILITATIONREFLOI", "MESURENOMINATIVE", "ISPARAPHEURFICHIERINF_54BB2DAB", "DATEPRECISEEPUBLICATION", "HABILITATIONDATETERME", "INDEXATIONMINPUB", "STATUT", "STATUTARCHIVAGE", "NUMERONOR", "CATEGORIEACTE", "IDDOCUMENTPARAPHEUR", "TYPEACTE", "PUBLICATIONINTEGRALEOUEXTRAIT", "MINISTEREATTACHE", "BASELEGALEACTE", "LASTDOCUMENTROUTE", "DIRECTIONRESP", "MINISTERERESP", "IDDOCUMENTFDD", "DIRECTIONATTACHE", "DELAIPUBLICATION", "NOMRESPDOSSIER", "PRENOMRESPDOSSIER", "QUALITERESPDOSSIER", "MAILRESPDOSSIER", "TELRESPDOSSIER", "IDCREATEURDOSSIER", "CANDIDAT") AS 
  SELECT DISTINCT DOS."ID",DOS."DATESIGNATURE",DOS."ISURGENT",DOS."POSTECREATOR",DOS."ISPARAPHEURTYPEACTEUPDATED",DOS."INDEXATIONMIN",DOS."INDEXATIONDIRPUB",DOS."DATEPUBLICATION",DOS."DECRETNUMEROTE",DOS."HABILITATIONNUMEROARTICLES",DOS."NUMEROVERSIONACTEOUEXTRAIT",DOS."PUBLICATIONRAPPORTPRESENTATION",DOS."DISPOSITIONHABILITATION",DOS."DATEDEMAINTIENENPRODUCTION",DOS."INDEXATIONDIR",DOS."ISPARAPHEURCOMPLET",DOS."TITREACTE",DOS."ZONECOMSGGDILA",DOS."IDDOSSIER",DOS."POURFOURNITUREEPREUVE",DOS."INDEXATIONSGG",DOS."HABILITATIONCOMMENTAIRE",DOS."HABILITATIONTERME",DOS."DATEENVOIJOTS",DOS."INDEXATIONSGGPUB",DOS."NBDOSSIERRECTIFIE",DOS."NOMCOMPLETRESPDOSSIER",DOS."DATECANDIDATURE",DOS."ARRIVEESOLONTS",DOS."ISDOSSIERISSUINJECTION",DOS."ISACTEREFERENCEFORNUM_302A1DC6",DOS."DATEVERSEMENTTS",DOS."DATEVERSEMENTARCHIVAG_E55E9FEF",DOS."HABILITATIONNUMEROORDRE",DOS."HABILITATIONREFLOI",DOS."MESURENOMINATIVE",DOS."ISPARAPHEURFICHIERINF_54BB2DAB",DOS."DATEPRECISEEPUBLICATION",DOS."HABILITATIONDATETERME",DOS."INDEXATIONMINPUB",DOS."STATUT",DOS."STATUTARCHIVAGE",DOS."NUMERONOR",DOS."CATEGORIEACTE",DOS."IDDOCUMENTPARAPHEUR",DOS."TYPEACTE",DOS."PUBLICATIONINTEGRALEOUEXTRAIT",DOS."MINISTEREATTACHE",DOS."BASELEGALEACTE",DOS."LASTDOCUMENTROUTE",DOS."DIRECTIONRESP",DOS."MINISTERERESP",DOS."IDDOCUMENTFDD",DOS."DIRECTIONATTACHE",DOS."DELAIPUBLICATION",DOS."NOMRESPDOSSIER",DOS."PRENOMRESPDOSSIER",DOS."QUALITERESPDOSSIER",DOS."MAILRESPDOSSIER",DOS."TELRESPDOSSIER",DOS."IDCREATEURDOSSIER",DOS."CANDIDAT"
FROM DOSSIER_SOLON_EPG DOS,
     TRAITEMENT_PAPIER TP
WHERE DOS.ID = TP.ID
AND   DOS.STATUTARCHIVAGE IN ('1', '2')
AND EXISTS (
				SELECT *
				FROM INFOEPREUVE EPR, HIERARCHY H
				WHERE DOS.ID = H.PARENTID
				AND   H.ID = EPR.ID
				AND   EPR.EPREUVEENVOIRELECTURELE IS NOT NULL
				AND   TP.RETOURDUBONATITRERLE IS NULL );
--------------------------------------------------------
--  DDL for View V_STATS_LISTE_TEXTE_AMPLIATION
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_STATS_LISTE_TEXTE_AMPLIATION" ("ID", "NUMERONOR", "TITREACTE", "ITEM") AS 
  SELECT  DOS.ID,
		DOS.NUMERONOR,
		DOS.TITREACTE,
		LISTAGG (AMP.ITEM, ', ') WITHIN GROUP (ORDER BY AMP.ITEM) ITEM
FROM (DOSSIER_SOLON_EPG DOS INNER JOIN TP_AMPLIATIONDESTINATAIREMAILS AMP ON DOS.ID = AMP.ID AND   DOS.STATUTARCHIVAGE IN ('1', '2'))
GROUP BY DOS.ID, DOS.NUMERONOR, DOS.TITREACTE;
--------------------------------------------------------
--  DDL for View V_STATS_LISTE_TEXTE_ARRIVE_SGG
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_STATS_LISTE_TEXTE_ARRIVE_SGG" ("ID", "DATESIGNATURE", "ISURGENT", "POSTECREATOR", "ISPARAPHEURTYPEACTEUPDATED", "INDEXATIONMIN", "INDEXATIONDIRPUB", "DATEPUBLICATION", "DECRETNUMEROTE", "HABILITATIONNUMEROARTICLES", "NUMEROVERSIONACTEOUEXTRAIT", "PUBLICATIONRAPPORTPRESENTATION", "DISPOSITIONHABILITATION", "DATEDEMAINTIENENPRODUCTION", "INDEXATIONDIR", "ISPARAPHEURCOMPLET", "TITREACTE", "ZONECOMSGGDILA", "IDDOSSIER", "POURFOURNITUREEPREUVE", "INDEXATIONSGG", "HABILITATIONCOMMENTAIRE", "HABILITATIONTERME", "DATEENVOIJOTS", "INDEXATIONSGGPUB", "NBDOSSIERRECTIFIE", "NOMCOMPLETRESPDOSSIER", "DATECANDIDATURE", "ARRIVEESOLONTS", "ISDOSSIERISSUINJECTION", "ISACTEREFERENCEFORNUM_302A1DC6", "DATEVERSEMENTTS", "DATEVERSEMENTARCHIVAG_E55E9FEF", "HABILITATIONNUMEROORDRE", "HABILITATIONREFLOI", "MESURENOMINATIVE", "ISPARAPHEURFICHIERINF_54BB2DAB", "DATEPRECISEEPUBLICATION", "HABILITATIONDATETERME", "INDEXATIONMINPUB", "STATUT", "STATUTARCHIVAGE", "NUMERONOR", "CATEGORIEACTE", "IDDOCUMENTPARAPHEUR", "TYPEACTE", "PUBLICATIONINTEGRALEOUEXTRAIT", "MINISTEREATTACHE", "BASELEGALEACTE", "LASTDOCUMENTROUTE", "DIRECTIONRESP", "MINISTERERESP", "IDDOCUMENTFDD", "DIRECTIONATTACHE", "DELAIPUBLICATION", "NOMRESPDOSSIER", "PRENOMRESPDOSSIER", "QUALITERESPDOSSIER", "MAILRESPDOSSIER", "TELRESPDOSSIER", "IDCREATEURDOSSIER", "CANDIDAT") AS 
  SELECT DISTINCT DOS."ID",DOS."DATESIGNATURE",DOS."ISURGENT",DOS."POSTECREATOR",DOS."ISPARAPHEURTYPEACTEUPDATED",DOS."INDEXATIONMIN",DOS."INDEXATIONDIRPUB",DOS."DATEPUBLICATION",DOS."DECRETNUMEROTE",DOS."HABILITATIONNUMEROARTICLES",DOS."NUMEROVERSIONACTEOUEXTRAIT",DOS."PUBLICATIONRAPPORTPRESENTATION",DOS."DISPOSITIONHABILITATION",DOS."DATEDEMAINTIENENPRODUCTION",DOS."INDEXATIONDIR",DOS."ISPARAPHEURCOMPLET",DOS."TITREACTE",DOS."ZONECOMSGGDILA",DOS."IDDOSSIER",DOS."POURFOURNITUREEPREUVE",DOS."INDEXATIONSGG",DOS."HABILITATIONCOMMENTAIRE",DOS."HABILITATIONTERME",DOS."DATEENVOIJOTS",DOS."INDEXATIONSGGPUB",DOS."NBDOSSIERRECTIFIE",DOS."NOMCOMPLETRESPDOSSIER",DOS."DATECANDIDATURE",DOS."ARRIVEESOLONTS",DOS."ISDOSSIERISSUINJECTION",DOS."ISACTEREFERENCEFORNUM_302A1DC6",DOS."DATEVERSEMENTTS",DOS."DATEVERSEMENTARCHIVAG_E55E9FEF",DOS."HABILITATIONNUMEROORDRE",DOS."HABILITATIONREFLOI",DOS."MESURENOMINATIVE",DOS."ISPARAPHEURFICHIERINF_54BB2DAB",DOS."DATEPRECISEEPUBLICATION",DOS."HABILITATIONDATETERME",DOS."INDEXATIONMINPUB",DOS."STATUT",DOS."STATUTARCHIVAGE",DOS."NUMERONOR",DOS."CATEGORIEACTE",DOS."IDDOCUMENTPARAPHEUR",DOS."TYPEACTE",DOS."PUBLICATIONINTEGRALEOUEXTRAIT",DOS."MINISTEREATTACHE",DOS."BASELEGALEACTE",DOS."LASTDOCUMENTROUTE",DOS."DIRECTIONRESP",DOS."MINISTERERESP",DOS."IDDOCUMENTFDD",DOS."DIRECTIONATTACHE",DOS."DELAIPUBLICATION",DOS."NOMRESPDOSSIER",DOS."PRENOMRESPDOSSIER",DOS."QUALITERESPDOSSIER",DOS."MAILRESPDOSSIER",DOS."TELRESPDOSSIER",DOS."IDCREATEURDOSSIER",DOS."CANDIDAT"
FROM DOSSIER_SOLON_EPG DOS
WHERE DOS.STATUTARCHIVAGE IN ('1', '2');
--------------------------------------------------------
--  DDL for View V_STATS_LISTE_TEXTE_COM
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_STATS_LISTE_TEXTE_COM" ("ID", "DATESIGNATURE", "ISURGENT", "POSTECREATOR", "ISPARAPHEURTYPEACTEUPDATED", "INDEXATIONMIN", "INDEXATIONDIRPUB", "DATEPUBLICATION", "DECRETNUMEROTE", "HABILITATIONNUMEROARTICLES", "NUMEROVERSIONACTEOUEXTRAIT", "PUBLICATIONRAPPORTPRESENTATION", "DISPOSITIONHABILITATION", "DATEDEMAINTIENENPRODUCTION", "INDEXATIONDIR", "ISPARAPHEURCOMPLET", "TITREACTE", "ZONECOMSGGDILA", "IDDOSSIER", "POURFOURNITUREEPREUVE", "INDEXATIONSGG", "HABILITATIONCOMMENTAIRE", "HABILITATIONTERME", "DATEENVOIJOTS", "INDEXATIONSGGPUB", "NBDOSSIERRECTIFIE", "NOMCOMPLETRESPDOSSIER", "DATECANDIDATURE", "ARRIVEESOLONTS", "ISDOSSIERISSUINJECTION", "ISACTEREFERENCEFORNUM_302A1DC6", "DATEVERSEMENTTS", "DATEVERSEMENTARCHIVAG_E55E9FEF", "HABILITATIONNUMEROORDRE", "HABILITATIONREFLOI", "MESURENOMINATIVE", "ISPARAPHEURFICHIERINF_54BB2DAB", "DATEPRECISEEPUBLICATION", "HABILITATIONDATETERME", "INDEXATIONMINPUB", "STATUT", "STATUTARCHIVAGE", "NUMERONOR", "CATEGORIEACTE", "IDDOCUMENTPARAPHEUR", "TYPEACTE", "PUBLICATIONINTEGRALEOUEXTRAIT", "MINISTEREATTACHE", "BASELEGALEACTE", "LASTDOCUMENTROUTE", "DIRECTIONRESP", "MINISTERERESP", "IDDOCUMENTFDD", "DIRECTIONATTACHE", "DELAIPUBLICATION", "NOMRESPDOSSIER", "PRENOMRESPDOSSIER", "QUALITERESPDOSSIER", "MAILRESPDOSSIER", "TELRESPDOSSIER", "IDCREATEURDOSSIER", "CANDIDAT") AS 
  SELECT DOS."ID",DOS."DATESIGNATURE",DOS."ISURGENT",DOS."POSTECREATOR",DOS."ISPARAPHEURTYPEACTEUPDATED",DOS."INDEXATIONMIN",DOS."INDEXATIONDIRPUB",DOS."DATEPUBLICATION",DOS."DECRETNUMEROTE",DOS."HABILITATIONNUMEROARTICLES",DOS."NUMEROVERSIONACTEOUEXTRAIT",DOS."PUBLICATIONRAPPORTPRESENTATION",DOS."DISPOSITIONHABILITATION",DOS."DATEDEMAINTIENENPRODUCTION",DOS."INDEXATIONDIR",DOS."ISPARAPHEURCOMPLET",DOS."TITREACTE",DOS."ZONECOMSGGDILA",DOS."IDDOSSIER",DOS."POURFOURNITUREEPREUVE",DOS."INDEXATIONSGG",DOS."HABILITATIONCOMMENTAIRE",DOS."HABILITATIONTERME",DOS."DATEENVOIJOTS",DOS."INDEXATIONSGGPUB",DOS."NBDOSSIERRECTIFIE",DOS."NOMCOMPLETRESPDOSSIER",DOS."DATECANDIDATURE",DOS."ARRIVEESOLONTS",DOS."ISDOSSIERISSUINJECTION",DOS."ISACTEREFERENCEFORNUM_302A1DC6",DOS."DATEVERSEMENTTS",DOS."DATEVERSEMENTARCHIVAG_E55E9FEF",DOS."HABILITATIONNUMEROORDRE",DOS."HABILITATIONREFLOI",DOS."MESURENOMINATIVE",DOS."ISPARAPHEURFICHIERINF_54BB2DAB",DOS."DATEPRECISEEPUBLICATION",DOS."HABILITATIONDATETERME",DOS."INDEXATIONMINPUB",DOS."STATUT",DOS."STATUTARCHIVAGE",DOS."NUMERONOR",DOS."CATEGORIEACTE",DOS."IDDOCUMENTPARAPHEUR",DOS."TYPEACTE",DOS."PUBLICATIONINTEGRALEOUEXTRAIT",DOS."MINISTEREATTACHE",DOS."BASELEGALEACTE",DOS."LASTDOCUMENTROUTE",DOS."DIRECTIONRESP",DOS."MINISTERERESP",DOS."IDDOCUMENTFDD",DOS."DIRECTIONATTACHE",DOS."DELAIPUBLICATION",DOS."NOMRESPDOSSIER",DOS."PRENOMRESPDOSSIER",DOS."QUALITERESPDOSSIER",DOS."MAILRESPDOSSIER",DOS."TELRESPDOSSIER",DOS."IDCREATEURDOSSIER",DOS."CANDIDAT"
FROM DOSSIER_SOLON_EPG DOS
WHERE DOS.STATUTARCHIVAGE IN ('1', '2')
AND   EXISTS ( 	SELECT *
				FROM HIERARCHY H, DESTINATAIRECOMMUNICATION DES
				WHERE DOS.ID = H.PARENTID
				AND   H.ID = DES.ID
				AND   DES.DATERETOURCOMMUNICATION IS NULL
				AND   DES.DATEENVOICOMMUNICATION IS NOT NULL );
--------------------------------------------------------
--  DDL for View V_STATS_LISTE_TEXTE_CORBEILLE
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_STATS_LISTE_TEXTE_CORBEILLE" ("ID", "DATESIGNATURE", "ISURGENT", "POSTECREATOR", "ISPARAPHEURTYPEACTEUPDATED", "INDEXATIONMIN", "INDEXATIONDIRPUB", "DATEPUBLICATION", "DECRETNUMEROTE", "HABILITATIONNUMEROARTICLES", "NUMEROVERSIONACTEOUEXTRAIT", "PUBLICATIONRAPPORTPRESENTATION", "DISPOSITIONHABILITATION", "DATEDEMAINTIENENPRODUCTION", "INDEXATIONDIR", "ISPARAPHEURCOMPLET", "TITREACTE", "ZONECOMSGGDILA", "IDDOSSIER", "POURFOURNITUREEPREUVE", "INDEXATIONSGG", "HABILITATIONCOMMENTAIRE", "HABILITATIONTERME", "DATEENVOIJOTS", "INDEXATIONSGGPUB", "NBDOSSIERRECTIFIE", "NOMCOMPLETRESPDOSSIER", "DATECANDIDATURE", "ARRIVEESOLONTS", "ISDOSSIERISSUINJECTION", "ISACTEREFERENCEFORNUM_302A1DC6", "DATEVERSEMENTTS", "DATEVERSEMENTARCHIVAG_E55E9FEF", "HABILITATIONNUMEROORDRE", "HABILITATIONREFLOI", "MESURENOMINATIVE", "ISPARAPHEURFICHIERINF_54BB2DAB", "DATEPRECISEEPUBLICATION", "HABILITATIONDATETERME", "INDEXATIONMINPUB", "STATUT", "STATUTARCHIVAGE", "NUMERONOR", "CATEGORIEACTE", "IDDOCUMENTPARAPHEUR", "TYPEACTE", "PUBLICATIONINTEGRALEOUEXTRAIT", "MINISTEREATTACHE", "BASELEGALEACTE", "LASTDOCUMENTROUTE", "DIRECTIONRESP", "MINISTERERESP", "IDDOCUMENTFDD", "DIRECTIONATTACHE", "DELAIPUBLICATION", "NOMRESPDOSSIER", "PRENOMRESPDOSSIER", "QUALITERESPDOSSIER", "MAILRESPDOSSIER", "TELRESPDOSSIER", "IDCREATEURDOSSIER", "CANDIDAT", "DISTRIBUTIONMAILBOXID") AS 
  SELECT DISTINCT DOS."ID",DOS."DATESIGNATURE",DOS."ISURGENT",DOS."POSTECREATOR",DOS."ISPARAPHEURTYPEACTEUPDATED",DOS."INDEXATIONMIN",DOS."INDEXATIONDIRPUB",DOS."DATEPUBLICATION",DOS."DECRETNUMEROTE",DOS."HABILITATIONNUMEROARTICLES",DOS."NUMEROVERSIONACTEOUEXTRAIT",DOS."PUBLICATIONRAPPORTPRESENTATION",DOS."DISPOSITIONHABILITATION",DOS."DATEDEMAINTIENENPRODUCTION",DOS."INDEXATIONDIR",DOS."ISPARAPHEURCOMPLET",DOS."TITREACTE",DOS."ZONECOMSGGDILA",DOS."IDDOSSIER",DOS."POURFOURNITUREEPREUVE",DOS."INDEXATIONSGG",DOS."HABILITATIONCOMMENTAIRE",DOS."HABILITATIONTERME",DOS."DATEENVOIJOTS",DOS."INDEXATIONSGGPUB",DOS."NBDOSSIERRECTIFIE",DOS."NOMCOMPLETRESPDOSSIER",DOS."DATECANDIDATURE",DOS."ARRIVEESOLONTS",DOS."ISDOSSIERISSUINJECTION",DOS."ISACTEREFERENCEFORNUM_302A1DC6",DOS."DATEVERSEMENTTS",DOS."DATEVERSEMENTARCHIVAG_E55E9FEF",DOS."HABILITATIONNUMEROORDRE",DOS."HABILITATIONREFLOI",DOS."MESURENOMINATIVE",DOS."ISPARAPHEURFICHIERINF_54BB2DAB",DOS."DATEPRECISEEPUBLICATION",DOS."HABILITATIONDATETERME",DOS."INDEXATIONMINPUB",DOS."STATUT",DOS."STATUTARCHIVAGE",DOS."NUMERONOR",DOS."CATEGORIEACTE",DOS."IDDOCUMENTPARAPHEUR",DOS."TYPEACTE",DOS."PUBLICATIONINTEGRALEOUEXTRAIT",DOS."MINISTEREATTACHE",DOS."BASELEGALEACTE",DOS."LASTDOCUMENTROUTE",DOS."DIRECTIONRESP",DOS."MINISTERERESP",DOS."IDDOCUMENTFDD",DOS."DIRECTIONATTACHE",DOS."DELAIPUBLICATION",DOS."NOMRESPDOSSIER",DOS."PRENOMRESPDOSSIER",DOS."QUALITERESPDOSSIER",DOS."MAILRESPDOSSIER",DOS."TELRESPDOSSIER",DOS."IDCREATEURDOSSIER",DOS."CANDIDAT", ROU.DISTRIBUTIONMAILBOXID
FROM DOSSIER_SOLON_EPG DOS, ACTIONNABLE_CASE_LINK ACT, CASE_LINK CAS, ROUTING_TASK ROU
WHERE DOS.ID = CAS.CASEDOCUMENTID
AND   DOS.STATUTARCHIVAGE IN ('1', '2')
AND CAS.ID = ACT.ID
AND ACT.ROUTINGTASKID = ROU.ID
AND ACT.CASELIFECYCLESTATE = 'running';
--------------------------------------------------------
--  DDL for View V_STATS_LISTE_TEXTE_DILA_PUB
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_STATS_LISTE_TEXTE_DILA_PUB" ("ID", "DATESIGNATURE", "ISURGENT", "POSTECREATOR", "ISPARAPHEURTYPEACTEUPDATED", "INDEXATIONMIN", "INDEXATIONDIRPUB", "DATEPUBLICATION", "DECRETNUMEROTE", "HABILITATIONNUMEROARTICLES", "NUMEROVERSIONACTEOUEXTRAIT", "PUBLICATIONRAPPORTPRESENTATION", "DISPOSITIONHABILITATION", "DATEDEMAINTIENENPRODUCTION", "INDEXATIONDIR", "ISPARAPHEURCOMPLET", "TITREACTE", "ZONECOMSGGDILA", "IDDOSSIER", "POURFOURNITUREEPREUVE", "INDEXATIONSGG", "HABILITATIONCOMMENTAIRE", "HABILITATIONTERME", "DATEENVOIJOTS", "INDEXATIONSGGPUB", "NBDOSSIERRECTIFIE", "NOMCOMPLETRESPDOSSIER", "DATECANDIDATURE", "ARRIVEESOLONTS", "ISDOSSIERISSUINJECTION", "ISACTEREFERENCEFORNUM_302A1DC6", "DATEVERSEMENTTS", "DATEVERSEMENTARCHIVAG_E55E9FEF", "HABILITATIONNUMEROORDRE", "HABILITATIONREFLOI", "MESURENOMINATIVE", "ISPARAPHEURFICHIERINF_54BB2DAB", "DATEPRECISEEPUBLICATION", "HABILITATIONDATETERME", "INDEXATIONMINPUB", "STATUT", "STATUTARCHIVAGE", "NUMERONOR", "CATEGORIEACTE", "IDDOCUMENTPARAPHEUR", "TYPEACTE", "PUBLICATIONINTEGRALEOUEXTRAIT", "MINISTEREATTACHE", "BASELEGALEACTE", "LASTDOCUMENTROUTE", "DIRECTIONRESP", "MINISTERERESP", "IDDOCUMENTFDD", "DIRECTIONATTACHE", "DELAIPUBLICATION", "NOMRESPDOSSIER", "PRENOMRESPDOSSIER", "QUALITERESPDOSSIER", "MAILRESPDOSSIER", "TELRESPDOSSIER", "IDCREATEURDOSSIER", "CANDIDAT") AS 
  SELECT DOS."ID",DOS."DATESIGNATURE",DOS."ISURGENT",DOS."POSTECREATOR",DOS."ISPARAPHEURTYPEACTEUPDATED",DOS."INDEXATIONMIN",DOS."INDEXATIONDIRPUB",DOS."DATEPUBLICATION",DOS."DECRETNUMEROTE",DOS."HABILITATIONNUMEROARTICLES",DOS."NUMEROVERSIONACTEOUEXTRAIT",DOS."PUBLICATIONRAPPORTPRESENTATION",DOS."DISPOSITIONHABILITATION",DOS."DATEDEMAINTIENENPRODUCTION",DOS."INDEXATIONDIR",DOS."ISPARAPHEURCOMPLET",DOS."TITREACTE",DOS."ZONECOMSGGDILA",DOS."IDDOSSIER",DOS."POURFOURNITUREEPREUVE",DOS."INDEXATIONSGG",DOS."HABILITATIONCOMMENTAIRE",DOS."HABILITATIONTERME",DOS."DATEENVOIJOTS",DOS."INDEXATIONSGGPUB",DOS."NBDOSSIERRECTIFIE",DOS."NOMCOMPLETRESPDOSSIER",DOS."DATECANDIDATURE",DOS."ARRIVEESOLONTS",DOS."ISDOSSIERISSUINJECTION",DOS."ISACTEREFERENCEFORNUM_302A1DC6",DOS."DATEVERSEMENTTS",DOS."DATEVERSEMENTARCHIVAG_E55E9FEF",DOS."HABILITATIONNUMEROORDRE",DOS."HABILITATIONREFLOI",DOS."MESURENOMINATIVE",DOS."ISPARAPHEURFICHIERINF_54BB2DAB",DOS."DATEPRECISEEPUBLICATION",DOS."HABILITATIONDATETERME",DOS."INDEXATIONMINPUB",DOS."STATUT",DOS."STATUTARCHIVAGE",DOS."NUMERONOR",DOS."CATEGORIEACTE",DOS."IDDOCUMENTPARAPHEUR",DOS."TYPEACTE",DOS."PUBLICATIONINTEGRALEOUEXTRAIT",DOS."MINISTEREATTACHE",DOS."BASELEGALEACTE",DOS."LASTDOCUMENTROUTE",DOS."DIRECTIONRESP",DOS."MINISTERERESP",DOS."IDDOCUMENTFDD",DOS."DIRECTIONATTACHE",DOS."DELAIPUBLICATION",DOS."NOMRESPDOSSIER",DOS."PRENOMRESPDOSSIER",DOS."QUALITERESPDOSSIER",DOS."MAILRESPDOSSIER",DOS."TELRESPDOSSIER",DOS."IDCREATEURDOSSIER",DOS."CANDIDAT"
FROM DOSSIER_SOLON_EPG DOS
WHERE DOS.STATUTARCHIVAGE IN ('1', '2')
AND EXISTS ( 	SELECT *
				FROM HIERARCHY H, ROUTING_TASK ROU
				WHERE DOS.LASTDOCUMENTROUTE = H.PARENTID
				AND   H.ID = ROU.ID
				AND   TO_CHAR (ROU.DATEDEBUTETAPE, 'MM/DD/YYYY') = TO_CHAR(SYSTIMESTAMP,'MM/DD/YYYY')
				AND   ROU.TYPE IN (13, 14) );
--------------------------------------------------------
--  DDL for View V_STATS_LISTE_TEXTE_DILA_RIG
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_STATS_LISTE_TEXTE_DILA_RIG" ("ID", "DATESIGNATURE", "ISURGENT", "POSTECREATOR", "ISPARAPHEURTYPEACTEUPDATED", "INDEXATIONMIN", "INDEXATIONDIRPUB", "DATEPUBLICATION", "DECRETNUMEROTE", "HABILITATIONNUMEROARTICLES", "NUMEROVERSIONACTEOUEXTRAIT", "PUBLICATIONRAPPORTPRESENTATION", "DISPOSITIONHABILITATION", "DATEDEMAINTIENENPRODUCTION", "INDEXATIONDIR", "ISPARAPHEURCOMPLET", "TITREACTE", "ZONECOMSGGDILA", "IDDOSSIER", "POURFOURNITUREEPREUVE", "INDEXATIONSGG", "HABILITATIONCOMMENTAIRE", "HABILITATIONTERME", "DATEENVOIJOTS", "INDEXATIONSGGPUB", "NBDOSSIERRECTIFIE", "NOMCOMPLETRESPDOSSIER", "DATECANDIDATURE", "ARRIVEESOLONTS", "ISDOSSIERISSUINJECTION", "ISACTEREFERENCEFORNUM_302A1DC6", "DATEVERSEMENTTS", "DATEVERSEMENTARCHIVAG_E55E9FEF", "HABILITATIONNUMEROORDRE", "HABILITATIONREFLOI", "MESURENOMINATIVE", "ISPARAPHEURFICHIERINF_54BB2DAB", "DATEPRECISEEPUBLICATION", "HABILITATIONDATETERME", "INDEXATIONMINPUB", "STATUT", "STATUTARCHIVAGE", "NUMERONOR", "CATEGORIEACTE", "IDDOCUMENTPARAPHEUR", "TYPEACTE", "PUBLICATIONINTEGRALEOUEXTRAIT", "MINISTEREATTACHE", "BASELEGALEACTE", "LASTDOCUMENTROUTE", "DIRECTIONRESP", "MINISTERERESP", "IDDOCUMENTFDD", "DIRECTIONATTACHE", "DELAIPUBLICATION", "NOMRESPDOSSIER", "PRENOMRESPDOSSIER", "QUALITERESPDOSSIER", "MAILRESPDOSSIER", "TELRESPDOSSIER", "IDCREATEURDOSSIER", "CANDIDAT") AS 
  SELECT DISTINCT DOS."ID",DOS."DATESIGNATURE",DOS."ISURGENT",DOS."POSTECREATOR",DOS."ISPARAPHEURTYPEACTEUPDATED",DOS."INDEXATIONMIN",DOS."INDEXATIONDIRPUB",DOS."DATEPUBLICATION",DOS."DECRETNUMEROTE",DOS."HABILITATIONNUMEROARTICLES",DOS."NUMEROVERSIONACTEOUEXTRAIT",DOS."PUBLICATIONRAPPORTPRESENTATION",DOS."DISPOSITIONHABILITATION",DOS."DATEDEMAINTIENENPRODUCTION",DOS."INDEXATIONDIR",DOS."ISPARAPHEURCOMPLET",DOS."TITREACTE",DOS."ZONECOMSGGDILA",DOS."IDDOSSIER",DOS."POURFOURNITUREEPREUVE",DOS."INDEXATIONSGG",DOS."HABILITATIONCOMMENTAIRE",DOS."HABILITATIONTERME",DOS."DATEENVOIJOTS",DOS."INDEXATIONSGGPUB",DOS."NBDOSSIERRECTIFIE",DOS."NOMCOMPLETRESPDOSSIER",DOS."DATECANDIDATURE",DOS."ARRIVEESOLONTS",DOS."ISDOSSIERISSUINJECTION",DOS."ISACTEREFERENCEFORNUM_302A1DC6",DOS."DATEVERSEMENTTS",DOS."DATEVERSEMENTARCHIVAG_E55E9FEF",DOS."HABILITATIONNUMEROORDRE",DOS."HABILITATIONREFLOI",DOS."MESURENOMINATIVE",DOS."ISPARAPHEURFICHIERINF_54BB2DAB",DOS."DATEPRECISEEPUBLICATION",DOS."HABILITATIONDATETERME",DOS."INDEXATIONMINPUB",DOS."STATUT",DOS."STATUTARCHIVAGE",DOS."NUMERONOR",DOS."CATEGORIEACTE",DOS."IDDOCUMENTPARAPHEUR",DOS."TYPEACTE",DOS."PUBLICATIONINTEGRALEOUEXTRAIT",DOS."MINISTEREATTACHE",DOS."BASELEGALEACTE",DOS."LASTDOCUMENTROUTE",DOS."DIRECTIONRESP",DOS."MINISTERERESP",DOS."IDDOCUMENTFDD",DOS."DIRECTIONATTACHE",DOS."DELAIPUBLICATION",DOS."NOMRESPDOSSIER",DOS."PRENOMRESPDOSSIER",DOS."QUALITERESPDOSSIER",DOS."MAILRESPDOSSIER",DOS."TELRESPDOSSIER",DOS."IDCREATEURDOSSIER",DOS."CANDIDAT"
FROM DOSSIER_SOLON_EPG DOS,
     TRAITEMENT_PAPIER TP
WHERE DOS.ID = TP.ID
AND   DOS.STATUTARCHIVAGE IN ('1', '2')
AND(
	EXISTS
	(
		SELECT *
		FROM ACTIONNABLE_CASE_LINK ACT, CASE_LINK CAS, ROUTING_TASK ROU
		WHERE DOS.ID = CAS.CASEDOCUMENTID
		AND   CAS.ID = ACT.ID
		AND   ACT.ROUTINGTASKID = ROU.ID
		AND   TO_CHAR (ROU.DATEDEBUTETAPE, 'MM/DD/YYYY') = TO_CHAR(SYSTIMESTAMP,'MM/DD/YYYY')
		AND   ROU.TYPE = 13
		AND   ACT.CASELIFECYCLESTATE = 'RUNNING'
		AND   DOS.DELAIPUBLICATION = 3
	)
	 OR
	(
		TO_CHAR (TP.PUBLICATIONDATEENVOIJO, 'MM/DD/YYYY') = TO_CHAR(SYSTIMESTAMP,'MM/DD/YYYY')
		AND   TP.PUBLICATIONDELAI = 3
	)
);
--------------------------------------------------------
--  DDL for View V_STATS_LISTE_TEXTE_SIG_PM_PR
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_STATS_LISTE_TEXTE_SIG_PM_PR" ("ID", "DATESIGNATURE", "ISURGENT", "POSTECREATOR", "ISPARAPHEURTYPEACTEUPDATED", "INDEXATIONMIN", "INDEXATIONDIRPUB", "DATEPUBLICATION", "DECRETNUMEROTE", "HABILITATIONNUMEROARTICLES", "NUMEROVERSIONACTEOUEXTRAIT", "PUBLICATIONRAPPORTPRESENTATION", "DISPOSITIONHABILITATION", "DATEDEMAINTIENENPRODUCTION", "INDEXATIONDIR", "ISPARAPHEURCOMPLET", "TITREACTE", "ZONECOMSGGDILA", "IDDOSSIER", "POURFOURNITUREEPREUVE", "INDEXATIONSGG", "HABILITATIONCOMMENTAIRE", "HABILITATIONTERME", "DATEENVOIJOTS", "INDEXATIONSGGPUB", "NBDOSSIERRECTIFIE", "NOMCOMPLETRESPDOSSIER", "DATECANDIDATURE", "ARRIVEESOLONTS", "ISDOSSIERISSUINJECTION", "ISACTEREFERENCEFORNUM_302A1DC6", "DATEVERSEMENTTS", "DATEVERSEMENTARCHIVAG_E55E9FEF", "HABILITATIONNUMEROORDRE", "HABILITATIONREFLOI", "MESURENOMINATIVE", "ISPARAPHEURFICHIERINF_54BB2DAB", "DATEPRECISEEPUBLICATION", "HABILITATIONDATETERME", "INDEXATIONMINPUB", "STATUT", "STATUTARCHIVAGE", "NUMERONOR", "CATEGORIEACTE", "IDDOCUMENTPARAPHEUR", "TYPEACTE", "PUBLICATIONINTEGRALEOUEXTRAIT", "MINISTEREATTACHE", "BASELEGALEACTE", "LASTDOCUMENTROUTE", "DIRECTIONRESP", "MINISTERERESP", "IDDOCUMENTFDD", "DIRECTIONATTACHE", "DELAIPUBLICATION", "NOMRESPDOSSIER", "PRENOMRESPDOSSIER", "QUALITERESPDOSSIER", "MAILRESPDOSSIER", "TELRESPDOSSIER", "IDCREATEURDOSSIER", "CANDIDAT") AS 
  SELECT DISTINCT DOS."ID",DOS."DATESIGNATURE",DOS."ISURGENT",DOS."POSTECREATOR",DOS."ISPARAPHEURTYPEACTEUPDATED",DOS."INDEXATIONMIN",DOS."INDEXATIONDIRPUB",DOS."DATEPUBLICATION",DOS."DECRETNUMEROTE",DOS."HABILITATIONNUMEROARTICLES",DOS."NUMEROVERSIONACTEOUEXTRAIT",DOS."PUBLICATIONRAPPORTPRESENTATION",DOS."DISPOSITIONHABILITATION",DOS."DATEDEMAINTIENENPRODUCTION",DOS."INDEXATIONDIR",DOS."ISPARAPHEURCOMPLET",DOS."TITREACTE",DOS."ZONECOMSGGDILA",DOS."IDDOSSIER",DOS."POURFOURNITUREEPREUVE",DOS."INDEXATIONSGG",DOS."HABILITATIONCOMMENTAIRE",DOS."HABILITATIONTERME",DOS."DATEENVOIJOTS",DOS."INDEXATIONSGGPUB",DOS."NBDOSSIERRECTIFIE",DOS."NOMCOMPLETRESPDOSSIER",DOS."DATECANDIDATURE",DOS."ARRIVEESOLONTS",DOS."ISDOSSIERISSUINJECTION",DOS."ISACTEREFERENCEFORNUM_302A1DC6",DOS."DATEVERSEMENTTS",DOS."DATEVERSEMENTARCHIVAG_E55E9FEF",DOS."HABILITATIONNUMEROORDRE",DOS."HABILITATIONREFLOI",DOS."MESURENOMINATIVE",DOS."ISPARAPHEURFICHIERINF_54BB2DAB",DOS."DATEPRECISEEPUBLICATION",DOS."HABILITATIONDATETERME",DOS."INDEXATIONMINPUB",DOS."STATUT",DOS."STATUTARCHIVAGE",DOS."NUMERONOR",DOS."CATEGORIEACTE",DOS."IDDOCUMENTPARAPHEUR",DOS."TYPEACTE",DOS."PUBLICATIONINTEGRALEOUEXTRAIT",DOS."MINISTEREATTACHE",DOS."BASELEGALEACTE",DOS."LASTDOCUMENTROUTE",DOS."DIRECTIONRESP",DOS."MINISTERERESP",DOS."IDDOCUMENTFDD",DOS."DIRECTIONATTACHE",DOS."DELAIPUBLICATION",DOS."NOMRESPDOSSIER",DOS."PRENOMRESPDOSSIER",DOS."QUALITERESPDOSSIER",DOS."MAILRESPDOSSIER",DOS."TELRESPDOSSIER",DOS."IDCREATEURDOSSIER",DOS."CANDIDAT"
FROM DOSSIER_SOLON_EPG DOS
WHERE DOS.STATUTARCHIVAGE IN ('1', '2')
AND EXISTS ( 	SELECT *
				FROM HIERARCHY H, DONNEESSIGNATAIRE SIG
				WHERE DOS.ID = H.PARENTID
				AND   H.NAME IN ('dos:signaturePm', 'dos:signaturePr')
				AND   H.ID = SIG.ID
				AND   SIG.DATEENVOISIGNATURE IS NOT NULL
				AND   SIG.DATERETOURSIGNATURE IS NULL);
--------------------------------------------------------
--  DDL for View V_STATS_NBR_ACTE_TYPE_PERIODE
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_STATS_NBR_ACTE_TYPE_PERIODE" ("ID", "MINISTEREATTACHE", "MINISTERERESP", "DIRECTIONRESP", "MAILRESPDOSSIER", "DATEDOSSIERCREE") AS 
  SELECT DOS.ID, DOS.MINISTEREATTACHE, DOS.MINISTERERESP, DOS.DIRECTIONRESP, DOS.MAILRESPDOSSIER, DUB.CREATED DATEDOSSIERCREE
FROM DOSSIER_SOLON_EPG DOS, DUBLINCORE DUB
WHERE DOS.ID = DUB.ID
AND   DOS.STATUTARCHIVAGE IN ('1', '2');
--------------------------------------------------------
--  DDL for View V_STATS_NOMBRE_ACTE_PAR_TYPE
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_STATS_NOMBRE_ACTE_PAR_TYPE" ("TYPEACTE", "ID", "DATEDOSSIERCREE") AS 
  SELECT DOS.TYPEACTE, DOS.ID, DUB.CREATED DATEDOSSIERCREE
FROM DOSSIER_SOLON_EPG DOS, DUBLINCORE DUB
WHERE DOS.ID = DUB.ID
AND   DOS.STATUTARCHIVAGE IN ('1', '2');

--------------------------------------------------------
--  DDL for View V_AN_MESURES_LEGISLATIVE
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_AN_MESURES_LEGISLATIVE" ("MOTCLE", "NUMEROLOI", "TITREOFFICIEL", "NATURETEXTE", "PROCEDUREVOTE", "DATEPUBLICATION", "LEGISLATUREPUBLICATION", "NUMEROORDRE", "ARTICLE", "CODEMODIFIE", "BASELEGALE", "OBJETRIM", "MINISTEREPILOTE", "DIRECTIONRESPONSABLE", "CONSULTATIONSHCE", "CALENDRIERCONSULTATIONSHCE", "DATEPREVISIONNELLESAISINECE", "DATEENTREEENVIGUEUR", "DATEOBJECTIFPUBLICATION", "TYPEMESUREID", "TYPEMESURE", "OBSERVATION", "NUMERONOR", "DECRET_TITREOFFICIEL", "TYPACTE", "DATESAISINECE", "DATESORTIECE", "DECRET_DATEPUBLICATION", "DATEMISEAPPLICATION", "MES_DATEENTREEENVIGUEUR", "APPLICATIONRECU", "LOI_DATEPROMULGATION", "MES_DIFFERE") AS 
  SELECT
TM_LOI.MOTCLE,
TM_LOI.NUMERO AS NUMEROLOI,
TM_LOI.TITREOFFICIEL,
TM_LOI.NATURETEXTE AS NATURETEXTE,
TM_LOI.PROCEDUREVOTE AS PROCEDUREVOTE,
TM_LOI.DATEPUBLICATION,
TM_LOI.LEGISLATUREPUBLICATION,
TM_MES.NUMEROORDRE,
TM_MES.ARTICLE,
TM_MES.CODEMODIFIE,
TM_MES.BASELEGALE,
TM_MES.OBJETRIM,
TM_MES.MINISTEREPILOTE,
TM_MES.DIRECTIONRESPONSABLE,
TM_MES.CONSULTATIONSHCE,
TM_MES.CALENDRIERCONSULTATIONSHCE,
TM_MES.DATEPREVISIONNELLESAISINECE,
TM_MES.DATEENTREEENVIGUEUR,
TM_MES.DATEOBJECTIFPUBLICATION DATEOBJECTIFPUBLICATION,
TM_MES.TYPEMESURE           AS TYPEMESUREID,
NVL(VOC_TM_MES."label", '') AS TYPEMESURE,
TM_MES.OBSERVATION,
TM_DEC.NUMERONOR,
TM_DEC.TITREOFFICIEL DECRET_TITREOFFICIEL,
NVL(VOC_TM_DEC."label",' ') AS TYPACTE,
TM_DEC.DATESAISINECE,
TM_DEC.DATESORTIECE,
TM_DEC.DATEPUBLICATION,
TM_MES.DATEMISEAPPLICATION,
TM_MES.DATEENTREEENVIGUEUR as MES_DATEENTREEENVIGUEUR,
TM_MES.APPLICATIONRECU,
TM_LOI.DATEPROMULGATION AS LOI_DATEPROMULGATION,
TM_MES.DIFFERE as MES_DIFFERE
FROM TEXTE_MAITRE TM_LOI, TEXM_MESUREIDS TME, ACTIVITE_NORMATIVE AN, TEXTE_MAITRE TM_MES
LEFT JOIN VOC_TYPE_MESURE VOC_TM_MES ON TM_MES.TYPEMESURE = VOC_TM_MES."id"
LEFT JOIN TEXM_DECRETIDS TD ON TD.ID = TM_MES.ID
LEFT JOIN TEXTE_MAITRE TM_DEC ON TM_DEC.ID = TD.ITEM
LEFT JOIN VOC_ACTE_TYPE VOC_TM_DEC ON TM_DEC.TYPEACTE = VOC_TM_DEC."id"
WHERE TM_LOI.ID = AN.ID
AND AN.APPLICATIONLOI = '1'
AND TM_MES.TYPEMESURE IN ('1','4' ,'6')
AND TME.ID = AN.ID
AND TM_MES.ID = TME.ITEM;

--------------------------------------------------------
--  DDL for View V_AN_MESURES_PREVISIONNELLE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_AN_MESURES_PREVISIONNELLE" ("NUMEROLOI", "TITREOFFICIEL", "NUMEROORDRE", "ARTICLE", "BASELEGALE", "OBJETRIM", "MINISTEREPILOTE", "DIRECTIONRESPONSABLE", "CONSULTATIONSHCE", "CALENDRIERCONSULTATIONSHCE", "DATEPREVISIONNELLESAISINECE", "DATEOBJECTIFPUBLICATION", "DATESAISINECE", "DATESORTIECE", "OBSERVATION", "NUMERONOR", "DECRET_TITREOFFICIEL", "TYPACTE", "DATEPUBLICATION", "DATEMISEAPPLICATION", "DATEPUBLICATIONLOI", "APPLICATIONRECU","LEGISLATUREPUBLICATION")
                       AS
  SELECT TM_LOI.NUMERO AS NUMEROLOI,
    TM_LOI.TITREOFFICIEL,
    TM_MES.NUMEROORDRE,
    TM_MES.ARTICLE,
    TM_MES.BASELEGALE,
    TM_MES.OBJETRIM,
    TM_MES.MINISTEREPILOTE,
    TM_MES.DIRECTIONRESPONSABLE,
    TM_MES.CONSULTATIONSHCE,
    TM_MES.CALENDRIERCONSULTATIONSHCE,
    TM_MES.DATEPREVISIONNELLESAISINECE,
    TM_MES.DATEOBJECTIFPUBLICATION,
    TM_DEC.DATESAISINECE,
    TM_DEC.DATESORTIECE,
    TM_MES.OBSERVATION,
    TM_DEC.NUMERONOR,
    TM_DEC.TITREOFFICIEL DECRET_TITREOFFICIEL,
    NVL(VOC_TM_DEC."label", '') AS TYPACTE,
    TM_DEC.DATEPUBLICATION,
    TM_MES.DATEMISEAPPLICATION,
    TM_LOI.DATEPUBLICATION DATEPUBLICATIONLOI,
    TM_MES.APPLICATIONRECU,    
    TM_LOI.LEGISLATUREPUBLICATION LEGISLATUREPUBLICATION
  FROM TEXTE_MAITRE TM_LOI,
    TEXM_MESUREIDS TME,
    ACTIVITE_NORMATIVE AN,
    TEXTE_MAITRE TM_MES
  LEFT JOIN TEXM_DECRETIDS TD
  ON TD.ID = TM_MES.ID
  LEFT JOIN TEXTE_MAITRE TM_DEC
  ON TM_DEC.ID = TD.ITEM
  LEFT JOIN VOC_ACTE_TYPE VOC_TM_DEC
  ON TM_DEC.TYPEACTE                 = VOC_TM_DEC."id"
  WHERE TM_LOI.ID                    = AN.ID
  AND AN.APPLICATIONLOI              = '1'
  AND TM_MES.TYPEMESURE              = '1'
  AND TME.ID                         = AN.ID
  AND TM_MES.ID                      = TME.ITEM
  AND TM_MES.DATEOBJECTIFPUBLICATION < SYSDATE;
  
--------------------------------------------------------
--  DDL for View V_AN_MES_PREV_APP_ORDO
--------------------------------------------------------
CREATE OR REPLACE VIEW "V_AN_MES_PREV_APP_ORDO" ("NUMEROORDO", "TITREOFFICIEL", "NUMEROORDRE", "ARTICLE", "BASELEGALE", "OBJETRIM", "MINISTEREPILOTE", "DIRECTIONRESPONSABLE", "CONSULTATIONSHCE", "CALENDRIERCONSULTATIONSHCE", "DATEPREVISIONNELLESAISINECE", "DATEOBJECTIFPUBLICATION", "DATESAISINECE", "DATESORTIECE", "OBSERVATION", "NUMERONOR", "DECRET_TITREOFFICIEL", "TYPACTE", "DATEPUBLICATION", "DATEMISEAPPLICATION", "DATEPUBLICATIONORDO", "APPLICATIONRECU", "LEGISLATUREPUBLICATION")
                       AS
  SELECT TM_LOI.NUMERO AS NUMEROORDO,
    TM_LOI.TITREOFFICIEL,
    TM_MES.NUMEROORDRE,
    TM_MES.ARTICLE,
    TM_MES.BASELEGALE,
    TM_MES.OBJETRIM,
    TM_MES.MINISTEREPILOTE,
    TM_MES.DIRECTIONRESPONSABLE,
    TM_MES.CONSULTATIONSHCE,
    TM_MES.CALENDRIERCONSULTATIONSHCE,
    TM_MES.DATEPREVISIONNELLESAISINECE,
    TM_MES.DATEOBJECTIFPUBLICATION,
    TM_DEC.DATESAISINECE,
    TM_DEC.DATESORTIECE,
    TM_MES.OBSERVATION,
    TM_DEC.NUMERONOR,
    TM_DEC.TITREOFFICIEL DECRET_TITREOFFICIEL,
    NVL(VOC_TM_DEC."label", '') AS TYPACTE,
    TM_DEC.DATEPUBLICATION,
    TM_MES.DATEMISEAPPLICATION,
    TM_LOI.DATEPUBLICATION AS DATEPUBLICATIONORDO,
    TM_MES.APPLICATIONRECU,
    TM_LOI.LEGISLATUREPUBLICATION AS LEGISLATUREPUBLICATION
  FROM TEXTE_MAITRE TM_LOI,
    TEXM_MESUREIDS TME,
    ACTIVITE_NORMATIVE AN,
    TEXTE_MAITRE TM_MES
  LEFT JOIN TEXM_DECRETIDS TD
  ON TD.ID = TM_MES.ID
  LEFT JOIN TEXTE_MAITRE TM_DEC
  ON TM_DEC.ID = TD.ITEM
  LEFT JOIN VOC_ACTE_TYPE VOC_TM_DEC
  ON TM_DEC.TYPEACTE                 = VOC_TM_DEC."id"
  WHERE TM_LOI.ID                    = AN.ID
  AND AN.APPLICATIONORDONNANCE       = '1'
  AND TM_MES.TYPEMESURE              = '1'
  AND TME.ID                         = AN.ID
  AND TM_MES.ID                      = TME.ITEM
  AND TM_MES.DATEOBJECTIFPUBLICATION < SYSDATE;
  
--------------------------------------------------------
--  DDL for View V_AN_MES_LEGIS_APP_ORDO
--------------------------------------------------------

  CREATE OR REPLACE VIEW "V_AN_MES_LEGIS_APP_ORDO" ("MOTCLE", "NUMEROLOI", "TITREOFFICIEL", "NATURETEXTE", "PROCEDUREVOTE", "DATEPUBLICATION", "LEGISLATUREPUBLICATION", "NUMEROORDRE", "ARTICLE", "CODEMODIFIE", "BASELEGALE", "OBJETRIM", "MINISTEREPILOTE", "DIRECTIONRESPONSABLE", "CONSULTATIONSHCE", "CALENDRIERCONSULTATIONSHCE", "DATEPREVISIONNELLESAISINECE", "DATEENTREEENVIGUEUR", "DATEOBJECTIFPUBLICATION", "TYPEMESUREID", "TYPEMESURE", "OBSERVATION", "NUMERONOR", "DECRET_TITREOFFICIEL", "TYPACTE", "DATESAISINECE", "DATESORTIECE", "DECRET_DATEPUBLICATION", "DATEMISEAPPLICATION", "MES_DATEENTREEENVIGUEUR", "APPLICATIONRECU", "LOI_DATEPROMULGATION", "MES_DIFFERE") AS 
  SELECT
TM_LOI.MOTCLE,
TM_LOI.NUMERO AS NUMEROLOI,
TM_LOI.TITREOFFICIEL,
TM_LOI.NATURETEXTE AS NATURETEXTE,
TM_LOI.PROCEDUREVOTE AS PROCEDUREVOTE,
TM_LOI.DATEPUBLICATION,
TM_LOI.LEGISLATUREPUBLICATION,
TM_MES.NUMEROORDRE,
TM_MES.ARTICLE,
TM_MES.CODEMODIFIE,
TM_MES.BASELEGALE,
TM_MES.OBJETRIM,
TM_MES.MINISTEREPILOTE,
TM_MES.DIRECTIONRESPONSABLE,
TM_MES.CONSULTATIONSHCE,
TM_MES.CALENDRIERCONSULTATIONSHCE,
TM_MES.DATEPREVISIONNELLESAISINECE,
TM_MES.DATEENTREEENVIGUEUR,
TM_MES.DATEOBJECTIFPUBLICATION DATEOBJECTIFPUBLICATION,
TM_MES.TYPEMESURE           AS TYPEMESUREID,
NVL(VOC_TM_MES."label", '') AS TYPEMESURE,
TM_MES.OBSERVATION,
TM_DEC.NUMERONOR,
TM_DEC.TITREOFFICIEL DECRET_TITREOFFICIEL,
NVL(VOC_TM_DEC."label",' ') AS TYPACTE,
TM_DEC.DATESAISINECE,
TM_DEC.DATESORTIECE,
TM_DEC.DATEPUBLICATION,
TM_MES.DATEMISEAPPLICATION,
TM_MES.DATEENTREEENVIGUEUR as MES_DATEENTREEENVIGUEUR,
TM_MES.APPLICATIONRECU,
TM_LOI.DATEPROMULGATION AS LOI_DATEPROMULGATION,
TM_MES.DIFFERE as MES_DIFFERE
FROM TEXTE_MAITRE TM_LOI, TEXM_MESUREIDS TME, ACTIVITE_NORMATIVE AN, TEXTE_MAITRE TM_MES
LEFT JOIN VOC_TYPE_MESURE VOC_TM_MES ON TM_MES.TYPEMESURE = VOC_TM_MES."id"
LEFT JOIN TEXM_DECRETIDS TD ON TD.ID = TM_MES.ID
LEFT JOIN TEXTE_MAITRE TM_DEC ON TM_DEC.ID = TD.ITEM
LEFT JOIN VOC_ACTE_TYPE VOC_TM_DEC ON TM_DEC.TYPEACTE = VOC_TM_DEC."id"
WHERE TM_LOI.ID = AN.ID
AND AN.APPLICATIONORDONNANCE = '1'
AND TM_MES.TYPEMESURE IN ('1','4' ,'6')
AND TME.ID = AN.ID
AND TM_MES.ID = TME.ITEM;

  
--------------------------------------------------------
--  DDL for View V_AN_MES_PREV_APP_ORDO
--------------------------------------------------------

CREATE OR REPLACE VIEW "V_AN_MES_PREV_APP_ORDO" ("NUMEROORDO", "TITREOFFICIEL", "NUMEROORDRE", "ARTICLE", "BASELEGALE", "OBJETRIM", "MINISTEREPILOTE", "DIRECTIONRESPONSABLE", "CONSULTATIONSHCE", "CALENDRIERCONSULTATIONSHCE", "DATEPREVISIONNELLESAISINECE", "DATEOBJECTIFPUBLICATION", "DATESAISINECE", "DATESORTIECE", "OBSERVATION", "NUMERONOR", "DECRET_TITREOFFICIEL", "TYPACTE", "DATEPUBLICATION", "DATEMISEAPPLICATION", "DATEPUBLICATIONORDO", "APPLICATIONRECU", "LEGISLATUREPUBLICATION")
                       AS
  SELECT TM_LOI.NUMERO AS NUMEROORDO,
    TM_LOI.TITREOFFICIEL,
    TM_MES.NUMEROORDRE,
    TM_MES.ARTICLE,
    TM_MES.BASELEGALE,
    TM_MES.OBJETRIM,
    TM_MES.MINISTEREPILOTE,
    TM_MES.DIRECTIONRESPONSABLE,
    TM_MES.CONSULTATIONSHCE,
    TM_MES.CALENDRIERCONSULTATIONSHCE,
    TM_MES.DATEPREVISIONNELLESAISINECE,
    TM_MES.DATEOBJECTIFPUBLICATION,
    TM_DEC.DATESAISINECE,
    TM_DEC.DATESORTIECE,
    TM_MES.OBSERVATION,
    TM_DEC.NUMERONOR,
    TM_DEC.TITREOFFICIEL AS DECRET_TITREOFFICIEL,
    NVL(VOC_TM_DEC."label", '') AS TYPACTE,
    TM_DEC.DATEPUBLICATION,
    TM_MES.DATEMISEAPPLICATION,
    TM_LOI.DATEPUBLICATION AS DATEPUBLICATIONORDO,
    TM_MES.APPLICATIONRECU,
    TM_LOI.LEGISLATUREPUBLICATION AS LEGISLATUREPUBLICATION
  FROM TEXTE_MAITRE TM_LOI,
    TEXM_MESUREIDS TME,
    ACTIVITE_NORMATIVE AN,
    TEXTE_MAITRE TM_MES
  LEFT JOIN TEXM_DECRETIDS TD
  ON TD.ID = TM_MES.ID
  LEFT JOIN TEXTE_MAITRE TM_DEC
  ON TM_DEC.ID = TD.ITEM
  LEFT JOIN VOC_ACTE_TYPE VOC_TM_DEC
  ON TM_DEC.TYPEACTE                 = VOC_TM_DEC."id"
  WHERE TM_LOI.ID                    = AN.ID
  AND AN.APPLICATIONORDONNANCE       = '1'
  AND TM_MES.TYPEMESURE              = '1'
  AND TME.ID                         = AN.ID
  AND TM_MES.ID                      = TME.ITEM
  AND TM_MES.DATEOBJECTIFPUBLICATION < SYSDATE;
  
--------------------------------------------------------
--  DDL for Index IDX_CMDISTALLACTION_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_CMDISTALLACTION_1" ON "CMDIST_ALL_ACTION_PAR_6B4BBED8" ("ID", "ITEM") 
  ;
--------------------------------------------------------
--  DDL for Index SEARCH_SEARCHPATH_ID_IDX
--------------------------------------------------------

  CREATE INDEX "SEARCH_SEARCHPATH_ID_IDX" ON "SEARCH_SEARCHPATH" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index HIERARCHY_READ_ACL_ACL_ID_IDX
--------------------------------------------------------

  CREATE INDEX "HIERARCHY_READ_ACL_ACL_ID_IDX" ON "HIERARCHY_READ_ACL" ("ACL_ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_PUBL_DELAI_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_VOC_PUBL_DELAI_1" ON "VOC_PUBLICATION_DELAI" ("id") 
  ;
--------------------------------------------------------
--  DDL for Index RESCON_FDRSIDS_ID_IDX
--------------------------------------------------------

  CREATE INDEX "RESCON_FDRSIDS_ID_IDX" ON "RESCON_FDRSIDS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index DOS__822CBD13_IDX
--------------------------------------------------------

  CREATE INDEX "DOS__822CBD13_IDX" ON "DOS_NOMCOMPLETCONSEILLERSPMS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index FICHE_PRESENTATION_OEP_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "FICHE_PRESENTATION_OEP_PK" ON "FICHE_PRESENTATION_OEP" ("ID") 
  ;
  
--------------------------------------------------------
--  DDL for Index DC_CONTRIBUTORS_ID_IDX
--------------------------------------------------------

  CREATE INDEX "DC_CONTRIBUTORS_ID_IDX" ON "DC_CONTRIBUTORS" ("ID") 
  ;

--------------------------------------------------------
--  DDL for Index IDX_HIER_6
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_HIER_6" ON "HIERARCHY" ("ID", "ISVERSION") 
  ;
--------------------------------------------------------
--  DDL for Index TEXM_0CA3AA3B_IDX
--------------------------------------------------------

  CREATE INDEX "TEXM_0CA3AA3B_IDX" ON "TEXM_MESURESAPPLICATIVESIDS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SEARCH_SUBJECTS_ID_IDX
--------------------------------------------------------

  CREATE INDEX "SEARCH_SUBJECTS_ID_IDX" ON "SEARCH_SUBJECTS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index NAVETTE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "NAVETTE_PK" ON "NAVETTE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_HIER_4
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_HIER_4" ON "HIERARCHY" ("ID", "PRIMARYTYPE") 
  ;
--------------------------------------------------------
--  DDL for Index LOCKS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "LOCKS_PK" ON "LOCKS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_VECT_PUBL_TP_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_VOC_VECT_PUBL_TP_1" ON "VOC_VECTEUR_PUBLICATION_TP" ("id") 
  ;
--------------------------------------------------------
--  DDL for Index JETON_DOC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "JETON_DOC_PK" ON "JETON_DOC" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SMART_FOLDER_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SMART_FOLDER_PK" ON "SMART_FOLDER" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index TRANPOSITION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRANPOSITION_PK" ON "TRANPOSITION" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_DOSSIER_EPG_3
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_DOSSIER_EPG_3" ON "DOSSIER_SOLON_EPG" ("ID", "NUMERONOR") 
  ;
--------------------------------------------------------
--  DDL for Index TAG_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TAG_PK" ON "TAG" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index REPRESENTANT_OEP_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "REPRESENTANT_OEP_PK" ON "REPRESENTANT_OEP" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_FULLTEXT_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_FULLTEXT_1" ON "FULLTEXT" ("JOBID", "ID") 
  ;
--------------------------------------------------------
--  DDL for Index PF_FORMATAUTORISE_ID_IDX
--------------------------------------------------------

  CREATE INDEX "PF_FORMATAUTORISE_ID_IDX" ON "PF_FORMATAUTORISE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index ACLS_ID_IDX
--------------------------------------------------------

  CREATE INDEX "ACLS_ID_IDX" ON "ACLS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SEMAINE_PARLEMENTAIRE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SEMAINE_PARLEMENTAIRE_PK" ON "SEMAINE_PARLEMENTAIRE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index TABDYN_USERSNAMES_ID_IDX
--------------------------------------------------------

  CREATE INDEX "TABDYN_USERSNAMES_ID_IDX" ON "TABDYN_USERSNAMES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index JETON_MAITRE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "JETON_MAITRE_PK" ON "JETON_MAITRE" ("ID") 
  ;  
--------------------------------------------------------
--  DDL for Index LOCK_JETON_MAITRE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "LOCK_JETON_MAITRE_PK" ON "LOCK_JETON_MAITRE" ("ID")
  ;
--------------------------------------------------------
--  DDL for Index IDX_RTASK_5
--------------------------------------------------------

  CREATE INDEX "IDX_RTASK_5" ON "ROUTING_TASK" ("DOCUMENTROUTEID") 
  ;
--------------------------------------------------------
--  DDL for Index HIER_B0ED9092_IDX
--------------------------------------------------------

  CREATE INDEX "HIER_B0ED9092_IDX" ON "HIERARCHY" ("PARENTID", "NAME") 
  ;
--------------------------------------------------------
--  DDL for Index FSD_ECM_PATH_ID_IDX
--------------------------------------------------------

  CREATE INDEX "FSD_ECM_PATH_ID_IDX" ON "FSD_ECM_PATH" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SEARCH_COVERAGE_ID_IDX
--------------------------------------------------------

  CREATE INDEX "SEARCH_COVERAGE_ID_IDX" ON "SEARCH_COVERAGE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index TEXM_45407F1E_IDX
--------------------------------------------------------

  CREATE INDEX "TEXM_45407F1E_IDX" ON "TEXM_TRANSPOSITIONIDS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SEARCH_NATURE_ID_IDX
--------------------------------------------------------

  CREATE INDEX "SEARCH_NATURE_ID_IDX" ON "SEARCH_NATURE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index CASE_ITEM_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CASE_ITEM_PK" ON "CASE_ITEM" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_ETAPE_STATUT_RECH_1
--------------------------------------------------------

  CREATE INDEX "IDX_VOC_ETAPE_STATUT_RECH_1" ON "VOC_ETAPE_STATUT_RECHERCHE" ("obsolete") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_PAPIER_AVIS_TP_2
--------------------------------------------------------

  CREATE INDEX "IDX_VOC_PAPIER_AVIS_TP_2" ON "VOC_PAPIER_AVIS_TP" ("obsolete") 
  ;

--------------------------------------------------------
--  DDL for Index IDX_LIST_TRAIT_PAP_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_LIST_TRAIT_PAP_1" ON "LISTE_TRAITEMENT_PAPI_144E75F6" ("DATEGENERATIONLISTE", "ID") 
  ;
--------------------------------------------------------
--  DDL for Index RELATEDTEXTRESOURCE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "RELATEDTEXTRESOURCE_PK" ON "RELATEDTEXTRESOURCE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index BULLETIN_OFFICIEL_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "BULLETIN_OFFICIEL_PK" ON "BULLETIN_OFFICIEL" ("ID") 
  ;
  
  CREATE UNIQUE INDEX "VECTEUR_PUBLICATION_PK" ON "VECTEUR_PUBLICATION" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index MINISTEREIDS_ID_IDX
--------------------------------------------------------

  CREATE INDEX "MINISTEREIDS_ID_IDX" ON "MINISTEREIDS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PARAMETRAGE_APPLICATION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PARAMETRAGE_APPLICATION_PK" ON "PARAMETRAGE_APPLICATION" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index DOS__B303B351_IDX
--------------------------------------------------------

  CREATE INDEX "DOS__B303B351_IDX" ON "DOS_VECTEURPUBLICATION" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index ALERT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "ALERT_PK" ON "ALERT" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index NOTE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "NOTE_PK" ON "NOTE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_DUBLINCORE_1
--------------------------------------------------------

  CREATE INDEX "IDX_DUBLINCORE_1" ON "DUBLINCORE" ("ID", "TITLE") 
  ;
--------------------------------------------------------
--  DDL for Index PROXIES_VERSIONABLEID_IDX
--------------------------------------------------------

  CREATE INDEX "PROXIES_VERSIONABLEID_IDX" ON "PROXIES" ("VERSIONABLEID") 
  ;
--------------------------------------------------------
--  DDL for Index REQUETE_DOSSIER_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "REQUETE_DOSSIER_PK" ON "REQUETE_DOSSIER" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index MAILBOX_ID_ID_IDX
--------------------------------------------------------

  CREATE INDEX "MAILBOX_ID_ID_IDX" ON "MAILBOX_ID" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index TBRE_D147E38E_IDX
--------------------------------------------------------

  CREATE INDEX "TBRE_D147E38E_IDX" ON "TBREF_VECTEURSPUBLICATIONS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index DEFAULTSETTINGS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "DEFAULTSETTINGS_PK" ON "DEFAULTSETTINGS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_PUBL_TYPE_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_VOC_PUBL_TYPE_1" ON "VOC_PUBLICATION_TYPE" ("id") 
  ;
--------------------------------------------------------
--  DDL for Index TYPE_CONTACT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TYPE_CONTACT_PK" ON "TYPE_CONTACT" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index INFO_UTILISATEUR_CONNECTION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "INFO_UTILISATEUR_CONNECTION_PK" ON "INFO_UTILISATEUR_CONNECTION" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index TABD_526DACF2_IDX
--------------------------------------------------------

  CREATE INDEX "TABD_526DACF2_IDX" ON "TABDYN_DESTINATAIRESID" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_MFDDR_FORMAT_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_MFDDR_FORMAT_1" ON "MFDDR_FORMATAUTORISE" ("ID", "POS", "ITEM") 
  ;
--------------------------------------------------------
--  DDL for Index TEXM_ORDONNANCEIDS_ID_IDX
--------------------------------------------------------

  CREATE INDEX "TEXM_ORDONNANCEIDS_ID_IDX" ON "TEXM_ORDONNANCEIDS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index UI_TYPES_CONFIGURATION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "UI_TYPES_CONFIGURATION_PK" ON "UI_TYPES_CONFIGURATION" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_HIER_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_HIER_1" ON "HIERARCHY" ("PRIMARYTYPE", "ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_RESCON_DOS_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_RESCON_DOS_1" ON "RESCON_DOSSIERSIDS" ("ID", "ITEM") 
  ;
--------------------------------------------------------
--  DDL for Index RESCON_USERSNAMES_ID_IDX
--------------------------------------------------------

  CREATE INDEX "RESCON_USERSNAMES_ID_IDX" ON "RESCON_USERSNAMES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index MLBX_A22042A4_IDX
--------------------------------------------------------

  CREATE INDEX "MLBX_A22042A4_IDX" ON "MLBX_NOTIFIED_USERS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_ACTE_CATEGORY_1
--------------------------------------------------------

  CREATE INDEX "IDX_VOC_ACTE_CATEGORY_1" ON "VOC_ACTE_CATEGORY" ("obsolete") 
  ;
--------------------------------------------------------
--  DDL for Index LIST_144E75F6_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "LIST_144E75F6_PK" ON "LISTE_TRAITEMENT_PAPI_144E75F6" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index INDE_4203B93F_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "INDE_4203B93F_PK" ON "INDEXATION_RUBRIQUE_SOLON_EPG" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_DOSSIER_EPG_5
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_DOSSIER_EPG_5" ON "DOSSIER_SOLON_EPG" ("ID", "TYPEACTE") 
  ;
--------------------------------------------------------
--  DDL for Index TBREF_SIGNATAIRES_ID_IDX
--------------------------------------------------------

  CREATE INDEX "TBREF_SIGNATAIRES_ID_IDX" ON "TBREF_SIGNATAIRES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index FACETED_SEARCH_DEFAULT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "FACETED_SEARCH_DEFAULT_PK" ON "FACETED_SEARCH_DEFAULT" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index CONSEIL_ETAT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONSEIL_ETAT_PK" ON "CONSEIL_ETAT" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_NXP_LOGS_1
--------------------------------------------------------

  CREATE INDEX "IDX_NXP_LOGS_1" ON "NXP_LOGS" ("LOG_DOC_UUID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_PUBL_DELAI_2
--------------------------------------------------------

  CREATE INDEX "IDX_VOC_PUBL_DELAI_2" ON "VOC_PUBLICATION_DELAI" ("obsolete") 
  ;
--------------------------------------------------------
--  DDL for Index VERS_E6513A3A_IDX
--------------------------------------------------------

  CREATE INDEX "VERS_E6513A3A_IDX" ON "VERSIONS" ("VERSIONABLEID") 
  ;
--------------------------------------------------------
--  DDL for Index MAILBOX_SOLON_EPG_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "MAILBOX_SOLON_EPG_PK" ON "MAILBOX_SOLON_EPG" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index MAIL_CC_RECIPIENTS_ID_IDX
--------------------------------------------------------

  CREATE INDEX "MAIL_CC_RECIPIENTS_ID_IDX" ON "MAIL_CC_RECIPIENTS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PUSR_A10F7C2D_IDX
--------------------------------------------------------

  CREATE INDEX "PUSR_A10F7C2D_IDX" ON "PUSR_IDCOLONNEINSTANC_3FED9D1F" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index COMMENT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "COMMENT_PK" ON "COMMENT" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_PF_FORMAT_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_PF_FORMAT_1" ON "PF_FORMATAUTORISE" ("ID", "POS", "ITEM") 
  ;
--------------------------------------------------------
--  DDL for Index INFOHISTORIQUEAMPLIATION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "INFOHISTORIQUEAMPLIATION_PK" ON "INFOHISTORIQUEAMPLIATION" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_FAVCON_USER_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_FAVCON_USER_1" ON "FAVCON_USERSNAMES" ("ID", "ITEM") 
  ;
--------------------------------------------------------
--  DDL for Index ITEM#ANONYMOUSTYPE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "ITEM#ANONYMOUSTYPE_PK" ON "ITEM#ANONYMOUSTYPE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index TRAITEMENT_PAPIER_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TRAITEMENT_PAPIER_PK" ON "TRAITEMENT_PAPIER" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index ALTR_RECIPIENTS_ID_IDX
--------------------------------------------------------

  CREATE INDEX "ALTR_RECIPIENTS_ID_IDX" ON "ALTR_RECIPIENTS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index TBRE_32503A91_IDX
--------------------------------------------------------

  CREATE INDEX "TBRE_32503A91_IDX" ON "TBREF_CHARGESMISSION" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_HIER_2
--------------------------------------------------------

  CREATE INDEX "IDX_HIER_2" ON "HIERARCHY" ("PARENTID", "ISPROPERTY") 
  ;
--------------------------------------------------------
--  DDL for Index ACLR_ACL_ID_IDX
--------------------------------------------------------

  CREATE INDEX "ACLR_ACL_ID_IDX" ON "ACLR" ("ACL_ID") 
  ;
--------------------------------------------------------
--  DDL for Index BIRTREPORT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "BIRTREPORT_PK" ON "BIRTREPORT" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index MODE_C2B36436_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "MODE_C2B36436_PK" ON "MODELE_FOND_DOSSIER_SOLON_EPG" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_FDR_COLUMN_2
--------------------------------------------------------

  CREATE INDEX "IDX_VOC_FDR_COLUMN_2" ON "VOC_FDR_COLUMN" ("obsolete") 
  ;
--------------------------------------------------------
--  DDL for Index RETOUR_DILA_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "RETOUR_DILA_PK" ON "RETOUR_DILA" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SORTINFOTYPE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SORTINFOTYPE_PK" ON "SORTINFOTYPE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index MODELE_PARAPHEUR_SOLON_EPG_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "MODELE_PARAPHEUR_SOLON_EPG_PK" ON "MODELE_PARAPHEUR_SOLON_EPG" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index TEXM_MESUREIDS_ID_IDX
--------------------------------------------------------

  CREATE INDEX "TEXM_MESUREIDS_ID_IDX" ON "TEXM_MESUREIDS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index HIERARCHY_PRIMARYTYPE_IDX
--------------------------------------------------------

  CREATE INDEX "HIERARCHY_PRIMARYTYPE_IDX" ON "HIERARCHY" ("PRIMARYTYPE") 
  ;
--------------------------------------------------------
--  DDL for Index ADVANCED_SEARCH_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "ADVANCED_SEARCH_PK" ON "ADVANCED_SEARCH" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index DR$FULLTEXT_FULLTEXT_NOR_IDX$X
--------------------------------------------------------

  CREATE INDEX "DR$FULLTEXT_FULLTEXT_NOR_IDX$X" ON "DR$FULLTEXT_FULLTEXT_NOR_IDX$I" ("TOKEN_TEXT", "TOKEN_TYPE", "TOKEN_FIRST", "TOKEN_LAST", "TOKEN_COUNT") 
  ;
--------------------------------------------------------
--  DDL for Index MISC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "MISC_PK" ON "MISC" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_ACTIONNABLE_CASE_LINK_6
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_ACTIONNABLE_CASE_LINK_6" ON "ACTIONNABLE_CASE_LINK" ("ID", "ROUTINGTASKTYPE") 
  ;
--------------------------------------------------------
--  DDL for Index PUSR_A7E0B68C_IDX
--------------------------------------------------------

  CREATE INDEX "PUSR_A7E0B68C_IDX" ON "PUSR_NOTIFICATIONTYPEACTES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index AMPL_3876A356_IDX
--------------------------------------------------------

  CREATE INDEX "AMPL_3876A356_IDX" ON "AMPLIATIONDESTINATAIREMAILS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_RTASK_6
--------------------------------------------------------

  CREATE INDEX "IDX_RTASK_6" ON "ROUTING_TASK" ("TYPE") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_MAILBOX_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_MAILBOX_1" ON "MAILBOX" ("MAILBOX_ID", "ID") 
  ;
--------------------------------------------------------
--  DDL for Index FP341_COAUTEUR_ID_IDX
--------------------------------------------------------

  CREATE INDEX "FP341_COAUTEUR_ID_IDX" ON "FP341_COAUTEUR" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PROTOCOL_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PROTOCOL_PK" ON "PROTOCOL" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_HIER_5
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_HIER_5" ON "HIERARCHY" ("ID", "MIXINTYPES") 
  ;
--------------------------------------------------------
--  DDL for Index ACTIONNABLE_CASE_LINK_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACTIONNABLE_CASE_LINK_PK" ON "ACTIONNABLE_CASE_LINK" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PARAPHEUR_FOLDER_SOLON_EPG_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PARAPHEUR_FOLDER_SOLON_EPG_PK" ON "PARAPHEUR_FOLDER_SOLON_EPG" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index MFDD_F97A4A71_IDX
--------------------------------------------------------

  CREATE INDEX "MFDD_F97A4A71_IDX" ON "MFDDR_FORMATAUTORISE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index FICHE_LOI_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "FICHE_LOI_PK" ON "FICHE_LOI" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index ROUTING_TASK_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "ROUTING_TASK_PK" ON "ROUTING_TASK" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_THEME_1
--------------------------------------------------------

  CREATE INDEX "IDX_THEME_1" ON "LOCALTHEMECONFIG" ("DOCID") 
  ;
--------------------------------------------------------
--  DDL for Index WEBCONTAINER_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "WEBCONTAINER_PK" ON "WEBCONTAINER" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_NXP_LOGS_2
--------------------------------------------------------

  CREATE INDEX "IDX_NXP_LOGS_2" ON "NXP_LOGS" ("LOG_EVENT_DATE") 
  ;
--------------------------------------------------------
--  DDL for Index LIGNEPROGRAMMATIONHAB_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "LIGNEPROGRAMMATIONHAB_PK" ON "LIGNEPROGRAMMATIONHAB" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_LIS_IDDOS_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_LIS_IDDOS_1" ON "LIS_IDSDOSSIER" ("ID", "ITEM") 
  ;
--------------------------------------------------------
--  DDL for Index FEUILLE_ROUTE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "FEUILLE_ROUTE_PK" ON "FEUILLE_ROUTE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_MODEL_FDD_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_MODEL_FDD_1" ON "MODELE_FOND_DOSSIER_SOLON_EPG" ("TYPEACTE", "ID") 
  ;
--------------------------------------------------------
--  DDL for Index BIRT_RESULTAT_FICHIER_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "BIRT_RESULTAT_FICHIER_PK" ON "BIRT_RESULTAT_FICHIER" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index MLBX_FAVORITES_ID_IDX
--------------------------------------------------------

  CREATE INDEX "MLBX_FAVORITES_ID_IDX" ON "MLBX_FAVORITES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index UITY_251B91EA_IDX
--------------------------------------------------------

  CREATE INDEX "UITY_251B91EA_IDX" ON "UITYPESCONF_ALLOWEDTYPES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index CONTENT_VIEW_DISPLAY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONTENT_VIEW_DISPLAY_PK" ON "CONTENT_VIEW_DISPLAY" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index REQU_743D2E6F_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "REQU_743D2E6F_PK" ON "REQUETE_DOSSIER_SIMPL_743D2E6F" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PARUTIONBO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PARUTIONBO_PK" ON "PARUTIONBO" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_DOSSIER_EPG_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_DOSSIER_EPG_1" ON "DOSSIER_SOLON_EPG" ("ID", "STATUTARCHIVAGE") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_DOCRIPARTDOC_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_DOCRIPARTDOC_1" ON "DOCRI_PARTICIPATINGDOCUMENTS" ("ID", "ITEM") 
  ;
--------------------------------------------------------
--  DDL for Index UITY_E32F2D57_IDX
--------------------------------------------------------

  CREATE INDEX "UITY_E32F2D57_IDX" ON "UITYPESCONF_DENIEDTYPES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_RESCON_USER_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_RESCON_USER_1" ON "RESCON_USERSNAMES" ("ID", "ITEM") 
  ;
--------------------------------------------------------
--  DDL for Index ANCESTORS_ANCESTORS_IDX
--------------------------------------------------------

  CREATE INDEX "ANCESTORS_ANCESTORS_IDX" ON "ANCESTORS_ANCESTORS" ("NESTED_TABLE_ID", "COLUMN_VALUE") 
  ;
--------------------------------------------------------
--  DDL for Index DOS_MOTSCLES_ID_IDX
--------------------------------------------------------

  CREATE INDEX "DOS_MOTSCLES_ID_IDX" ON "DOS_MOTSCLES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index MLBX_GROUPS_ID_IDX
--------------------------------------------------------

  CREATE INDEX "MLBX_GROUPS_ID_IDX" ON "MLBX_GROUPS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index TEXTE_MAITRE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TEXTE_MAITRE_PK" ON "TEXTE_MAITRE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index CMDI_E06BC7E3_IDX
--------------------------------------------------------

  CREATE INDEX "CMDI_E06BC7E3_IDX" ON "CMDIST_INITIAL_ACTION_4CD43708" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index DOS_LIBRE_ID_IDX
--------------------------------------------------------

  CREATE INDEX "DOS_LIBRE_ID_IDX" ON "DOS_LIBRE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PARTICIPANTLIST_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PARTICIPANTLIST_PK" ON "PARTICIPANTLIST" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index DOS_RUBRIQUES_ID_IDX
--------------------------------------------------------

  CREATE INDEX "DOS_RUBRIQUES_ID_IDX" ON "DOS_RUBRIQUES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_CASE_LINK_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_CASE_LINK_1" ON "CASE_LINK" ("ID", "DATE") 
  ;
--------------------------------------------------------
--  DDL for Index TEXM_DOSSIERSNOR_ID_IDX
--------------------------------------------------------

  CREATE INDEX "TEXM_DOSSIERSNOR_ID_IDX" ON "TEXM_DOSSIERSNOR" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_PAPIER_AVIS_TP_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_VOC_PAPIER_AVIS_TP_1" ON "VOC_PAPIER_AVIS_TP" ("id") 
  ;
--------------------------------------------------------
--  DDL for Index WSSPE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "WSSPE_PK" ON "WSSPE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index REQU_A3A85FF2_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "REQU_A3A85FF2_PK" ON "REQUETE_DOSSIER_SIMPL_A3A85FF2" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_MODEL_PARAPH_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_MODEL_PARAPH_1" ON "MODELE_PARAPHEUR_SOLON_EPG" ("TYPEACTE", "ID") 
  ;
--------------------------------------------------------
--  DDL for Index PUSR_D6676C8D_IDX
--------------------------------------------------------

  CREATE INDEX "PUSR_D6676C8D_IDX" ON "PUSR_IDCOLONNEESPACET_934A8665" ("ID") 
  ;
  
--------------------------------------------------------
--  DDL for Index IDX_DCCONTRIB_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_DCCONTRIB_1" ON "DC_CONTRIBUTORS" ("ID", "POS", "ITEM") 
  ;
  
--------------------------------------------------------
--  DDL for Index ACLR_USER_USER_ID_IDX
--------------------------------------------------------

  CREATE INDEX "ACLR_USER_USER_ID_IDX" ON "ACLR_USER" ("USER_ID") 
  ;
--------------------------------------------------------
--  DDL for Index UID_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "UID_PK" ON "UID" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_MLBX_GROUPS_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_MLBX_GROUPS_1" ON "MLBX_GROUPS" ("ID", "ITEM") 
  ;
--------------------------------------------------------
--  DDL for Index COMMON_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "COMMON_PK" ON "COMMON" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index FAVCON_FDRSIDS_ID_IDX
--------------------------------------------------------

  CREATE INDEX "FAVCON_FDRSIDS_ID_IDX" ON "FAVCON_FDRSIDS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index DOCR_C6FD7970_IDX
--------------------------------------------------------

  CREATE INDEX "DOCR_C6FD7970_IDX" ON "DOCRI_PARTICIPATINGDOCUMENTS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PROF_BA33E962_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PROF_BA33E962_PK" ON "PROFIL_UTILISATEUR_SOLON_EPG" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index DUBLINCORE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "DUBLINCORE_PK" ON "DUBLINCORE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_VECT_PUBL_TP_2
--------------------------------------------------------

  CREATE INDEX "IDX_VOC_VECT_PUBL_TP_2" ON "VOC_VECTEUR_PUBLICATION_TP" ("obsolete") 
  ;
--------------------------------------------------------
--  DDL for Index CASE_DOCUMENTSID_ID_IDX
--------------------------------------------------------

  CREATE INDEX "CASE_DOCUMENTSID_ID_IDX" ON "CASE_DOCUMENTSID" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index MOTS_CLES_ID_IDX
--------------------------------------------------------

  CREATE INDEX "MOTS_CLES_ID_IDX" ON "MOTS_CLES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PUBLISH_SECTIONS_ID_IDX
--------------------------------------------------------

  CREATE INDEX "PUBLISH_SECTIONS_ID_IDX" ON "PUBLISH_SECTIONS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index ETAT_APPLICATION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "ETAT_APPLICATION_PK" ON "ETAT_APPLICATION" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index REPOSITORIES_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "REPOSITORIES_PK" ON "REPOSITORIES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index FACETED_SEARCH_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "FACETED_SEARCH_PK" ON "FACETED_SEARCH" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index MAIL_RECIPIENTS_ID_IDX
--------------------------------------------------------

  CREATE INDEX "MAIL_RECIPIENTS_ID_IDX" ON "MAIL_RECIPIENTS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_BO_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_BO_1" ON "BULLETIN_OFFICIEL" ("ID", "BONOR") 
  ;
--------------------------------------------------------
--  DDL for Index FICHE_PRESENTATION_341_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "FICHE_PRESENTATION_341_PK" ON "FICHE_PRESENTATION_341" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_INDEX_MOT_CLE_1
--------------------------------------------------------

  CREATE INDEX "IDX_INDEX_MOT_CLE_1" ON "INDEXATION_MOT_CLE_SOLON_EPG" ("ID", "INTITULE") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_BORDEREAU_1
--------------------------------------------------------

  CREATE INDEX "IDX_VOC_BORDEREAU_1" ON "VOC_BORDEREAU_LABEL" ("id") 
  ;
--------------------------------------------------------
--  DDL for Index RQDC_63A1EEF4_IDX
--------------------------------------------------------

  CREATE INDEX "RQDC_63A1EEF4_IDX" ON "RQDCP_IDSTATUTSARCHIV_82ED246B" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_FDR_COLUMN_1
--------------------------------------------------------

  CREATE INDEX "IDX_VOC_FDR_COLUMN_1" ON "VOC_FDR_COLUMN" ("id") 
  ;
--------------------------------------------------------
--  DDL for Index TABLEAU_DYNAMIQUE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TABLEAU_DYNAMIQUE_PK" ON "TABLEAU_DYNAMIQUE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index TBREF_DIRECTIONPM_ID_IDX
--------------------------------------------------------

  CREATE INDEX "TBREF_DIRECTIONPM_ID_IDX" ON "TBREF_DIRECTIONPM" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_RTASK_2
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_RTASK_2" ON "ROUTING_TASK" ("ID", "DISTRIBUTIONMAILBOXID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_ACLRUSERMAP_1
--------------------------------------------------------

  CREATE INDEX "IDX_ACLRUSERMAP_1" ON "ACLR_USER_MAP" ("USER_ID", "ACL_ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_ACTIONNABLE_CASE_LINK_3
--------------------------------------------------------

  CREATE INDEX "IDX_ACTIONNABLE_CASE_LINK_3" ON "ACTIONNABLE_CASE_LINK" ("DUEDATE") 
  ;
--------------------------------------------------------
--  DDL for Index CMDI_FA613CD3_IDX
--------------------------------------------------------

  CREATE INDEX "CMDI_FA613CD3_IDX" ON "CMDIST_ALL_COPY_PARTI_21AB3C5B" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index CMDI_A3C38F3F_IDX
--------------------------------------------------------

  CREATE INDEX "CMDI_A3C38F3F_IDX" ON "CMDIST_INITIAL_COPY_E_B3610C04" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index ACLR_USER_MAP_ACL_ID_IDX
--------------------------------------------------------

  CREATE INDEX "ACLR_USER_MAP_ACL_ID_IDX" ON "ACLR_USER_MAP" ("ACL_ID", "USER_ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_DUBLINCORE_2
--------------------------------------------------------

  CREATE INDEX "IDX_DUBLINCORE_2" ON "DUBLINCORE" ("ID", "CREATOR") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_PROXIES_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_PROXIES_1" ON "PROXIES" ("ID", "TARGETID") 
  ;
--------------------------------------------------------
--  DDL for Index FAVCON_USERSNAMES_ID_IDX
--------------------------------------------------------

  CREATE INDEX "FAVCON_USERSNAMES_ID_IDX" ON "FAVCON_USERSNAMES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_HIER_3
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_HIER_3" ON "HIERARCHY" ("PARENTID", "PRIMARYTYPE", "ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_MISC_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_MISC_1" ON "MISC" ("ID", "LIFECYCLESTATE") 
  ;
--------------------------------------------------------
--  DDL for Index HIERARCHY_ISVERSION_IDX
--------------------------------------------------------

  CREATE INDEX "HIERARCHY_ISVERSION_IDX" ON "HIERARCHY" ("ISVERSION") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_PUSR_COL_INST_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_PUSR_COL_INST_1" ON "PUSR_IDCOLONNEINSTANC_3FED9D1F" ("ID", "POS", "ITEM") 
  ;
--------------------------------------------------------
--  DDL for Index DR$FULLTEXT_FULLTEXT_IDX$X
--------------------------------------------------------

  CREATE INDEX "DR$FULLTEXT_FULLTEXT_IDX$X" ON "DR$FULLTEXT_FULLTEXT_IDX$I" ("TOKEN_TEXT", "TOKEN_TYPE", "TOKEN_FIRST", "TOKEN_LAST", "TOKEN_COUNT") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_DOS_STATUS_1
--------------------------------------------------------

  CREATE INDEX "IDX_VOC_DOS_STATUS_1" ON "VOC_DOS_STATUS" ("obsolete") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_RTASK_4
--------------------------------------------------------

  CREATE INDEX "IDX_RTASK_4" ON "ROUTING_TASK" ("DISTRIBUTIONMAILBOXID") 
  ;
--------------------------------------------------------
--  DDL for Index PARAMETRE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PARAMETRE_PK" ON "PARAMETRE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_ACTIONNABLE_CASE_LINK_2
--------------------------------------------------------

  CREATE INDEX "IDX_ACTIONNABLE_CASE_LINK_2" ON "ACTIONNABLE_CASE_LINK" ("AUTOMATICVALIDATION") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_ACTE_CATEGORY_2
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_VOC_ACTE_CATEGORY_2" ON "VOC_ACTE_CATEGORY" ("id") 
  ;
--------------------------------------------------------
--  DDL for Index ECHEANCIER_PROMULGATION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "ECHEANCIER_PROMULGATION_PK" ON "ECHEANCIER_PROMULGATION" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_FAVCON_DOS_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_FAVCON_DOS_1" ON "FAVCON_DOSSIERSIDS" ("ID", "ITEM") 
  ;
--------------------------------------------------------
--  DDL for Index FSD_DC_SUBJECTS_ID_IDX
--------------------------------------------------------

  CREATE INDEX "FSD_DC_SUBJECTS_ID_IDX" ON "FSD_DC_SUBJECTS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index CMDI_5E2F4527_IDX
--------------------------------------------------------

  CREATE INDEX "CMDI_5E2F4527_IDX" ON "CMDIST_INITIAL_COPY_I_D6588F7E" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index RELATION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "RELATION_PK" ON "RELATION" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index MLBX_USERS_ID_IDX
--------------------------------------------------------

  CREATE INDEX "MLBX_USERS_ID_IDX" ON "MLBX_USERS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index FAVCON_DOSSIERSIDS_ID_IDX
--------------------------------------------------------

  CREATE INDEX "FAVCON_DOSSIERSIDS_ID_IDX" ON "FAVCON_DOSSIERSIDS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_ACTIONNABLE_CASE_LINK_5
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_ACTIONNABLE_CASE_LINK_5" ON "ACTIONNABLE_CASE_LINK" ("ID", "STATUTARCHIVAGE") 
  ;
--------------------------------------------------------
--  DDL for Index DOSSIER_ARCHIVAGE
--------------------------------------------------------

  CREATE INDEX "DOSSIER_ARCHIVAGE" ON "DOSSIER_SOLON_EPG" ("STATUTARCHIVAGE") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_CMDISTINITACTION_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_CMDISTINITACTION_1" ON "CMDIST_INITIAL_ACTION_4CD43708" ("ID", "ITEM") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_DOSSIER_EPG_6
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_DOSSIER_EPG_6" ON "DOSSIER_SOLON_EPG" ("LASTDOCUMENTROUTE") 
  ;
--------------------------------------------------------
--  DDL for Index MLBX_PROFILES_ID_IDX
--------------------------------------------------------

  CREATE INDEX "MLBX_PROFILES_ID_IDX" ON "MLBX_PROFILES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_ACTIONNABLE_CASE_LINK_4
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_ACTIONNABLE_CASE_LINK_4" ON "ACTIONNABLE_CASE_LINK" ("ID", "CASELIFECYCLESTATE") 
  ;
--------------------------------------------------------
--  DDL for Index TBREF_CABINETPM_ID_IDX
--------------------------------------------------------

  CREATE INDEX "TBREF_CABINETPM_ID_IDX" ON "TBREF_CABINETPM" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PARAMETER_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PARAMETER_PK" ON "PARAMETER" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_DOSSIER_EPG_2
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_DOSSIER_EPG_2" ON "DOSSIER_SOLON_EPG" ("ID", "CATEGORIEACTE") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_RESCON_MFDR_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_RESCON_MFDR_1" ON "RESCON_FDRSIDS" ("ID", "ITEM") 
  ;
--------------------------------------------------------
--  DDL for Index RELATION_SEARCH_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "RELATION_SEARCH_PK" ON "RELATION_SEARCH" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_FEUILLE_ROUTE_1
--------------------------------------------------------

  CREATE INDEX "IDX_FEUILLE_ROUTE_1" ON "FEUILLE_ROUTE" ("MINISTERE") 
  ;
--------------------------------------------------------
--  DDL for Index FILE_SOLON_EPG_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "FILE_SOLON_EPG_PK" ON "FILE_SOLON_EPG" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PA_M_D70C0FDB_IDX
--------------------------------------------------------

  CREATE INDEX "PA_M_D70C0FDB_IDX" ON "PA_METADISPOCORBEILLE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index TBREF_SIGNATURECPM_ID_IDX
--------------------------------------------------------

  CREATE INDEX "TBREF_SIGNATURECPM_ID_IDX" ON "TBREF_SIGNATURECPM" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index CVD__F112AB18_IDX
--------------------------------------------------------

  CREATE INDEX "CVD__F112AB18_IDX" ON "CVD_SELECTEDLAYOUTCOLUMNS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_PUSR_NOTIF_ACTE_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_PUSR_NOTIF_ACTE_1" ON "PUSR_NOTIFICATIONTYPEACTES" ("ID", "POS", "ITEM") 
  ;
--------------------------------------------------------
--  DDL for Index STEP_FOLDER_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "STEP_FOLDER_PK" ON "STEP_FOLDER" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index CLUSTER_INVALS_NODEID_IDX
--------------------------------------------------------

  CREATE INDEX "CLUSTER_INVALS_NODEID_IDX" ON "CLUSTER_INVALS" ("NODEID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_BO_2
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_BO_2" ON "BULLETIN_OFFICIEL" ("ID", "BOETAT") 
  ;
--------------------------------------------------------
--  DDL for Index DOS__43724302_IDX
--------------------------------------------------------

  CREATE INDEX "DOS__43724302_IDX" ON "DOS_NOMCOMPLETCHARGESMISSIONS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index REL_SRCH_ECM_PATH_ID_IDX
--------------------------------------------------------

  CREATE INDEX "REL_SRCH_ECM_PATH_ID_IDX" ON "REL_SRCH_ECM_PATH" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VECTEURPUBLIC_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_VECTEURPUBLIC_1" ON "DOS_VECTEURPUBLICATION" ("ID", "ITEM") 
  ;
--------------------------------------------------------
--  DDL for Index USERSETTINGS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "USERSETTINGS_PK" ON "USERSETTINGS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_FILE_FORMAT_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_VOC_FILE_FORMAT_1" ON "VOC_FILE_FORMAT" ("id") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_RTASK_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_RTASK_1" ON "ROUTING_TASK" ("ID", "DATEFINETAPE") 
  ;
--------------------------------------------------------
--  DDL for Index ACTIVITE_NORMATIVE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACTIVITE_NORMATIVE_PK" ON "ACTIVITE_NORMATIVE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index FILE_SOLON_MGPP_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "FILE_SOLON_MGPP_PK" ON "FILE_SOLON_MGPP" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_MODE_PARU_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_VOC_MODE_PARU_1" ON "VOC_MODE_PARUTION" ("id") 
  ;
--------------------------------------------------------
--  DDL for Index ACTIVITE_PARLEMENTAIRE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACTIVITE_PARLEMENTAIRE_PK" ON "ACTIVITE_PARLEMENTAIRE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index TEXM_49E495BB_IDX
--------------------------------------------------------

  CREATE INDEX "TEXM_49E495BB_IDX" ON "TEXM_HABILITATIONIDS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_PUSR_COL_ESP_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_PUSR_COL_ESP_1" ON "PUSR_IDCOLONNEESPACET_934A8665" ("ID", "POS", "ITEM") 
  ;
--------------------------------------------------------
--  DDL for Index CMDI_F03AFF82_IDX
--------------------------------------------------------

  CREATE INDEX "CMDI_F03AFF82_IDX" ON "CMDIST_ALL_ACTION_PAR_6B4BBED8" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index DOS__EC8D4458_IDX
--------------------------------------------------------

  CREATE INDEX "DOS__EC8D4458_IDX" ON "DOS_PUBLICATIONSCONJOINTES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_CASE_LINK_2
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_CASE_LINK_2" ON "CASE_LINK" ("CASEDOCUMENTID", "ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_PAPIER_PRIO_2
--------------------------------------------------------

  CREATE INDEX "IDX_VOC_PAPIER_PRIO_2" ON "VOC_PAPIER_PRIORITE" ("obsolete") 
  ;
--------------------------------------------------------
--  DDL for Index TEXM_DECRETIDS_ID_IDX
--------------------------------------------------------

  CREATE INDEX "TEXM_DECRETIDS_ID_IDX" ON "TEXM_DECRETIDS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_ACTE_TYPE_3
--------------------------------------------------------

  CREATE INDEX "IDX_VOC_ACTE_TYPE_3" ON "VOC_ACTE_TYPE" (LOWER("label")) 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_PAPIER_TYPE_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_VOC_PAPIER_TYPE_1" ON "VOC_PAPIER_TYPE_SIGN" ("id") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_MISC_2
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_MISC_2" ON "MISC" ("LIFECYCLESTATE", "ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_FAVCON_MFDR_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_FAVCON_MFDR_1" ON "FAVCON_FDRSIDS" ("ID", "ITEM") 
  ;
--------------------------------------------------------
--  DDL for Index INDE_4E2ADFD2_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "INDE_4E2ADFD2_PK" ON "INDEXATION_MOT_CLE_SOLON_EPG" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index CMDOC_SENDERS_ID_IDX
--------------------------------------------------------

  CREATE INDEX "CMDOC_SENDERS_ID_IDX" ON "CMDOC_SENDERS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index INFO_COMMENTS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "INFO_COMMENTS_PK" ON "INFO_COMMENTS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_MOT_CLE_1
--------------------------------------------------------

  CREATE INDEX "IDX_MOT_CLE_1" ON "MOTS_CLES" ("ID", "ITEM") 
  ;
--------------------------------------------------------
--  DDL for Index RESCON_DOSSIERSIDS_ID_IDX
--------------------------------------------------------

  CREATE INDEX "RESCON_DOSSIERSIDS_ID_IDX" ON "RESCON_DOSSIERSIDS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index VERSIONS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "VERSIONS_PK" ON "VERSIONS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index TEXM_18BCB504_IDX
--------------------------------------------------------

  CREATE INDEX "TEXM_18BCB504_IDX" ON "TEXM_LOIRATIFICATIONIDS" ("ID") 
  ;

--------------------------------------------------------
--  DDL for Index HIERARCHY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "HIERARCHY_PK" ON "HIERARCHY" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index VCARD_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "VCARD_PK" ON "VCARD" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_PUBLICCONJ_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_PUBLICCONJ_1" ON "DOS_PUBLICATIONSCONJOINTES" ("ID", "ITEM") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_MODE_PARU_2
--------------------------------------------------------

  CREATE INDEX "IDX_VOC_MODE_PARU_2" ON "VOC_MODE_PARUTION" ("obsolete") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_ACTE_TYPE_1
--------------------------------------------------------

  CREATE INDEX "IDX_VOC_ACTE_TYPE_1" ON "VOC_ACTE_TYPE" ("obsolete") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_RTASK_3
--------------------------------------------------------

  CREATE INDEX "IDX_RTASK_3" ON "ROUTING_TASK" ("DATEFINETAPE") 
  ;
--------------------------------------------------------
--  DDL for Index DOSSIER_SOLON_EPG_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "DOSSIER_SOLON_EPG_PK" ON "DOSSIER_SOLON_EPG" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_NXP_LOGS_3
--------------------------------------------------------

  CREATE INDEX "IDX_NXP_LOGS_3" ON "NXP_LOGS" ("LOG_EVENT_CATEGORY") 
  ;
--------------------------------------------------------
--  DDL for Index LIS_IDSDOSSIER_ID_IDX
--------------------------------------------------------

  CREATE INDEX "LIS_IDSDOSSIER_ID_IDX" ON "LIS_IDSDOSSIER" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index FICHE_PRESENTATION_IE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "FICHE_PRESENTATION_IE_PK" ON "FICHE_PRESENTATION_IE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index CLAS_14028D7C_IDX
--------------------------------------------------------

  CREATE INDEX "CLAS_14028D7C_IDX" ON "CLASSIFICATION_TARGETS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SEAR_BEE8E2A1_IDX
--------------------------------------------------------

  CREATE INDEX "SEAR_BEE8E2A1_IDX" ON "SEARCH_CURRENTLIFECYCLESTATES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_DOSSIER_EPG_4
--------------------------------------------------------

  CREATE INDEX "IDX_DOSSIER_EPG_4" ON "DOSSIER_SOLON_EPG" ("STATUT") 
  ;
--------------------------------------------------------
--  DDL for Index FDD_FORMATAUTORISE_ID_IDX
--------------------------------------------------------

  CREATE INDEX "FDD_FORMATAUTORISE_ID_IDX" ON "FDD_FORMATAUTORISE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index LIGNEPROGRAMMATION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "LIGNEPROGRAMMATION_PK" ON "LIGNEPROGRAMMATION" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index DESTINATAIRECOMMUNICATION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "DESTINATAIRECOMMUNICATION_PK" ON "DESTINATAIRECOMMUNICATION" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_CM_ROUT_TASK_TYPE_1
--------------------------------------------------------

  CREATE INDEX "IDX_VOC_CM_ROUT_TASK_TYPE_1" ON "VOC_CM_ROUTING_TASK_TYPE" ("obsolete") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_LIST_TRAIT_PAP_2
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_LIST_TRAIT_PAP_2" ON "LISTE_TRAITEMENT_PAPI_144E75F6" ("TYPELISTE", "ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_PAPIER_PRIO_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_VOC_PAPIER_PRIO_1" ON "VOC_PAPIER_PRIORITE" ("id") 
  ;
--------------------------------------------------------
--  DDL for Index TABLE_REFERENCE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TABLE_REFERENCE_PK" ON "TABLE_REFERENCE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index MAILBOX_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "MAILBOX_PK" ON "MAILBOX" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index MAIL_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "MAIL_PK" ON "MAIL" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_ACLS_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_ACLS_1" ON "ACLS" ("ID", "POS", "PERMISSION", "USER", "GRANT", "NAME", "GROUP") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_FDD_FORMAT_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_FDD_FORMAT_1" ON "FDD_FORMATAUTORISE" ("ID", "POS", "ITEM") 
  ;
--------------------------------------------------------
--  DDL for Index FSD_DC_CREATOR_ID_IDX
--------------------------------------------------------

  CREATE INDEX "FSD_DC_CREATOR_ID_IDX" ON "FSD_DC_CREATOR" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index FSD_DC_COVERAGE_ID_IDX
--------------------------------------------------------

  CREATE INDEX "FSD_DC_COVERAGE_ID_IDX" ON "FSD_DC_COVERAGE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index INFOEPREUVE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "INFOEPREUVE_PK" ON "INFOEPREUVE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_ACLRMODIFIED_1
--------------------------------------------------------

  CREATE INDEX "IDX_ACLRMODIFIED_1" ON "ACLR_MODIFIED" ("HIERARCHY_ID") 
  ;
--------------------------------------------------------
--  DDL for Index REQU_7B2FB691_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "REQU_7B2FB691_PK" ON "REQUETE_DOSSIER_SIMPL_7B2FB691" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index TBREF_SIGNATURESGG_ID_IDX
--------------------------------------------------------

  CREATE INDEX "TBREF_SIGNATURESGG_ID_IDX" ON "TBREF_SIGNATURESGG" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_COMMENT_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_COMMENT_1" ON "COMMENT" ("COMMENTEDDOCID", "ID") 
  ;
--------------------------------------------------------
--  DDL for Index FILE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "FILE_PK" ON "FILE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index FICHE_PRESENTATION_DR_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "FICHE_PRESENTATION_DR_PK" ON "FICHE_PRESENTATION_DR" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index PROXIES_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "PROXIES_PK" ON "PROXIES" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index FSD_DC_NATURE_ID_IDX
--------------------------------------------------------

  CREATE INDEX "FSD_DC_NATURE_ID_IDX" ON "FSD_DC_NATURE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index BIRTREPORTMODEL_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "BIRTREPORTMODEL_PK" ON "BIRTREPORTMODEL" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index FULLTEXT_FULLTEXT_NOR_IDX
--------------------------------------------------------
   
--------------------------------------------------------
--  DDL for Index TP_A_E828184C_IDX
--------------------------------------------------------

  CREATE INDEX "TP_A_E828184C_IDX" ON "TP_AMPLIATIONDESTINATAIREMAILS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index HIERARCHY_PARENTID_IDX
--------------------------------------------------------

  CREATE INDEX "HIERARCHY_PARENTID_IDX" ON "HIERARCHY" ("PARENTID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_BORDEREAU_2
--------------------------------------------------------

  CREATE INDEX "IDX_VOC_BORDEREAU_2" ON "VOC_BORDEREAU_LABEL" ("obsolete") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_FEUILLE_ROUTE_2
--------------------------------------------------------

  CREATE INDEX "IDX_FEUILLE_ROUTE_2" ON "FEUILLE_ROUTE" ("TYPEACTE") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_BOOL_1
--------------------------------------------------------

  CREATE INDEX "IDX_VOC_BOOL_1" ON "VOC_BOOLEAN" ("obsolete") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_PAPIER_TYPE_2
--------------------------------------------------------

  CREATE INDEX "IDX_VOC_PAPIER_TYPE_2" ON "VOC_PAPIER_TYPE_SIGN" ("obsolete") 
  ;
--------------------------------------------------------
--  DDL for Index QUERYNAV_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "QUERYNAV_PK" ON "QUERYNAV" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index CASE_LINK_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CASE_LINK_PK" ON "CASE_LINK" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index CMDI_1093196C_IDX
--------------------------------------------------------

  CREATE INDEX "CMDI_1093196C_IDX" ON "CMDIST_INITIAL_ACTION_88A481B7" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index FAVORIS_RECHERCHE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "FAVORIS_RECHERCHE_PK" ON "FAVORIS_RECHERCHE" ("ID") 
  ;
  
--------------------------------------------------------
--  DDL for Index IDX_DCSUBJECT_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_DCSUBJECT_1" ON "DC_SUBJECTS" ("ID", "POS", "ITEM") 
  ;
  
--------------------------------------------------------
--  DDL for Index DONNEESSIGNATAIRE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "DONNEESSIGNATAIRE_PK" ON "DONNEESSIGNATAIRE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index STATUS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "STATUS_PK" ON "STATUS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index FOND_67CE5EE1_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "FOND_67CE5EE1_PK" ON "FOND_DOSSIER_FOLDER_SOLON_EPG" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index REQU_C483C4B2_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "REQU_C483C4B2_PK" ON "REQUETE_DOSSIER_SIMPL_C483C4B2" ("ID") 
  ;
  
--------------------------------------------------------
--  DDL for Index DC_SUBJECTS_ID_IDX
--------------------------------------------------------

  CREATE INDEX "DC_SUBJECTS_ID_IDX" ON "DC_SUBJECTS" ("ID") 
  ;

--------------------------------------------------------
--  DDL for Index ACLR_USER_MAP_USER_ID_IDX
--------------------------------------------------------

  CREATE INDEX "ACLR_USER_MAP_USER_ID_IDX" ON "ACLR_USER_MAP" ("USER_ID") 
  ;
--------------------------------------------------------
--  DDL for Index CONTENT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CONTENT_PK" ON "CONTENT" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index NAVIGATIONSETTINGS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "NAVIGATIONSETTINGS_PK" ON "NAVIGATIONSETTINGS" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index TEXTE_SIGNALE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "TEXTE_SIGNALE_PK" ON "TEXTE_SIGNALE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index ACTI_DE9E019D_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "ACTI_DE9E019D_PK" ON "ACTIVITE_NORMATIVE_PR_DE9E019D" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_VOC_ACTE_TYPE_2
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_VOC_ACTE_TYPE_2" ON "VOC_ACTE_TYPE" ("id") 
  ;
--------------------------------------------------------
--  DDL for Index PROXIES_TARGETID_IDX
--------------------------------------------------------

  CREATE INDEX "PROXIES_TARGETID_IDX" ON "PROXIES" ("TARGETID") 
  ;
--------------------------------------------------------
--  DDL for Index DOS__73991B70_IDX
--------------------------------------------------------

  CREATE INDEX "DOS__73991B70_IDX" ON "DOS_TRANSPOSITIONDIRE_3E14AB2D" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index IDX_ACTIONNABLE_CASE_LINK_1
--------------------------------------------------------

  CREATE UNIQUE INDEX "IDX_ACTIONNABLE_CASE_LINK_1" ON "ACTIONNABLE_CASE_LINK" ("STEPDOCUMENTID", "ID") 
  ;
--------------------------------------------------------
--  DDL for Index FULLTEXT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "FULLTEXT_PK" ON "FULLTEXT" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index INFONUMEROLISTESIGNATURE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "INFONUMEROLISTESIGNATURE_PK" ON "INFONUMEROLISTESIGNATURE" ("ID") 
  ;
  
   --== DOSSIER_SOLON_EPG ==--
	CREATE UNIQUE INDEX IDX_DOSSIER_SOLON_EPG_1 ON DOSSIER_SOLON_EPG(NUMERONOR);

	
--------------------------------------------------------
--  INDEX POUR DATEMAINTIENPROD DOSSIER_SOLON_EPG
--------------------------------------------------------
	CREATE INDEX "DOSSIER_DATE_MAINTIEN_PROD" ON "DOSSIER_SOLON_EPG" ("DATEDEMAINTIENENPRODUCTION");
	
--------------------------------------------------------
--  INDEX POUR DATEPARUTIONJORF RETOUR_DILA
--------------------------------------------------------
CREATE INDEX "RETOUR_DILA_DATE_PARUTION_JORF" ON "RETOUR_DILA" ("DATEPARUTIONJORF");

--------------------------------------------------------
--  INDEX POUR QUARTZ
--------------------------------------------------------
CREATE INDEX "IDX_QRTZ_J_REQ_RECOVERY" ON "QRTZ_JOB_DETAILS"("SCHED_NAME","REQUESTS_RECOVERY");
CREATE INDEX "IDX_QRTZ_J_GRP" ON "QRTZ_JOB_DETAILS"("SCHED_NAME","JOB_GROUP");
CREATE INDEX "IDX_QRTZ_T_J" ON "QRTZ_TRIGGERS"("SCHED_NAME","JOB_NAME","JOB_GROUP");
CREATE INDEX "IDX_QRTZ_T_JG" ON "QRTZ_TRIGGERS"("SCHED_NAME","JOB_GROUP");
CREATE INDEX "IDX_QRTZ_T_C" ON "QRTZ_TRIGGERS"("SCHED_NAME","CALENDAR_NAME");
CREATE INDEX "IDX_QRTZ_T_G" ON "QRTZ_TRIGGERS"("SCHED_NAME","TRIGGER_GROUP");
CREATE INDEX "IDX_QRTZ_T_STATE" ON "QRTZ_TRIGGERS"("SCHED_NAME","TRIGGER_STATE");
CREATE INDEX "IDX_QRTZ_T_N_STATE" ON "QRTZ_TRIGGERS"("SCHED_NAME","TRIGGER_NAME","TRIGGER_GROUP","TRIGGER_STATE");
CREATE INDEX "IDX_QRTZ_T_N_G_STATE" ON "QRTZ_TRIGGERS"("SCHED_NAME","TRIGGER_GROUP","TRIGGER_STATE");
CREATE INDEX "IDX_QRTZ_T_NEXT_FIRE_TIME" ON "QRTZ_TRIGGERS"("SCHED_NAME","NEXT_FIRE_TIME");
CREATE INDEX "IDX_QRTZ_T_NFT_ST" ON "QRTZ_TRIGGERS"("SCHED_NAME","TRIGGER_STATE","NEXT_FIRE_TIM"E);
CREATE INDEX "IDX_QRTZ_T_nft_MISFIRE" ON "QRTZ_TRIGGERS"("SCHED_NAME","MISFIRE_INSTR","NEXT_FIRE_TIME");
CREATE INDEX "IDX_QRTZ_T_NFT_ST_MISFIRE" ON "QRTZ_TRIGGERS"("SCHED_NAME","MISFIRE_INSTR","NEXT_FIRE_TIME","TRIGGER_STATE");
CREATE INDEX "IDX_QRTZ_T_NFT_ST_MISFIRE_GRP" ON "QRTZ_TRIGGERS"("SCHED_NAME","MISFIRE_INSTR","NEXT_FIRE_TIME","TRIGGER_GROUP","TRIGGER_STATE");
CREATE INDEX "IDX_QRTZ_ft_TRIG_INST_NAME" ON "QRTZ_FIRED_TRIGGERS"("SCHED_NAME","INSTANCE_NAME");
CREATE INDEX "IDX_QRTZ_FT_INST_JOB_REQ_RCVRY" ON "QRTZ_FIRED_TRIGGERS"("SCHED_NAME","INSTANCE_NAME,REQUESTS_RECOVERY");
CREATE INDEX "IDX_QRTZ_FT_J_G" ON "QRTZ_FIRED_TRIGGERS"("SCHED_NAME","JOB_NAME","JOB_GROUP");
CREATE INDEX "IDX_QRTZ_FT_JG" ON "QRTZ_FIRED_TRIGGERS"("SCHED_NAME","JOB_GROUP");
CREATE INDEX "IDX_QRTZ_FT_T_G" ON "QRTZ_FIRED_TRIGGERS"("SCHED_NAME","TRIGGER_NAME","TRIGGER_GROUP");
CREATE INDEX "IDX_QRTZ_FT_TG" ON "QRTZ_FIRED_TRIGGERS"("SCHED_NAME","TRIGGER_GROUP");


--------------------------------------------------------
--  Constraints for Table MISC
--------------------------------------------------------

  ALTER TABLE "MISC" ADD CONSTRAINT "MISC_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "MISC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table MODELE_FOND_DOSSIER_SOLON_EPG
--------------------------------------------------------

  ALTER TABLE "MODELE_FOND_DOSSIER_SOLON_EPG" ADD CONSTRAINT "MODE_C2B36436_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "MODELE_FOND_DOSSIER_SOLON_EPG" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FILE_SOLON_MGPP
--------------------------------------------------------

  ALTER TABLE "FILE_SOLON_MGPP" ADD CONSTRAINT "FILE_SOLON_MGPP_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "FILE_SOLON_MGPP" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TRANPOSITION
--------------------------------------------------------

  ALTER TABLE "TRANPOSITION" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "TRANPOSITION" ADD CONSTRAINT "TRANPOSITION_PK" PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table ACTIVITE_NORMATIVE_PR_DE9E019D
--------------------------------------------------------

  ALTER TABLE "ACTIVITE_NORMATIVE_PR_DE9E019D" ADD CONSTRAINT "ACTI_DE9E019D_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "ACTIVITE_NORMATIVE_PR_DE9E019D" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TEXM_MESURESAPPLICATIVESIDS
--------------------------------------------------------

  ALTER TABLE "TEXM_MESURESAPPLICATIVESIDS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table STATUS
--------------------------------------------------------

  ALTER TABLE "STATUS" ADD CONSTRAINT "STATUS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "STATUS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FICHE_PRESENTATION_IE
--------------------------------------------------------

  ALTER TABLE "FICHE_PRESENTATION_IE" ADD CONSTRAINT "FICHE_PRESENTATION_IE_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "FICHE_PRESENTATION_IE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBREF_CHARGESMISSION
--------------------------------------------------------

  ALTER TABLE "TBREF_CHARGESMISSION" MODIFY ("ID" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table TBREF_SIGNATURESGG
--------------------------------------------------------

  ALTER TABLE "TBREF_SIGNATURESGG" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PROFIL_UTILISATEUR_SOLON_EPG
--------------------------------------------------------

  ALTER TABLE "PROFIL_UTILISATEUR_SOLON_EPG" ADD CONSTRAINT "PROF_BA33E962_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PROFIL_UTILISATEUR_SOLON_EPG" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table SEARCH_CURRENTLIFECYCLESTATES
--------------------------------------------------------

  ALTER TABLE "SEARCH_CURRENTLIFECYCLESTATES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ACLR_USER
--------------------------------------------------------

  ALTER TABLE "ACLR_USER" MODIFY ("USER_ID" NOT NULL ENABLE);
 
--------------------------------------------------------
--  Constraints for Table TABDYN_USERSNAMES
--------------------------------------------------------

  ALTER TABLE "TABDYN_USERSNAMES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PF_FORMATAUTORISE
--------------------------------------------------------

  ALTER TABLE "PF_FORMATAUTORISE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table RESCON_DOSSIERSIDS
--------------------------------------------------------

  ALTER TABLE "RESCON_DOSSIERSIDS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CASE_DOCUMENTSID
--------------------------------------------------------

  ALTER TABLE "CASE_DOCUMENTSID" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table MAIL
--------------------------------------------------------

  ALTER TABLE "MAIL" ADD CONSTRAINT "MAIL_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "MAIL" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table RELATION
--------------------------------------------------------

  ALTER TABLE "RELATION" ADD CONSTRAINT "RELATION_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "RELATION" MODIFY ("ID" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table DC_SUBJECTS
--------------------------------------------------------

  ALTER TABLE "DC_SUBJECTS" MODIFY ("ID" NOT NULL ENABLE);
  
--------------------------------------------------------
--  Constraints for Table DOS_RUBRIQUES
--------------------------------------------------------

  ALTER TABLE "DOS_RUBRIQUES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table SORTINFOTYPE
--------------------------------------------------------

  ALTER TABLE "SORTINFOTYPE" ADD CONSTRAINT "SORTINFOTYPE_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "SORTINFOTYPE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table REPOSITORIES
--------------------------------------------------------

  ALTER TABLE "REPOSITORIES" ADD CONSTRAINT "REPOSITORIES_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "REPOSITORIES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CMDIST_ALL_COPY_PARTI_21AB3C5B
--------------------------------------------------------

  ALTER TABLE "CMDIST_ALL_COPY_PARTI_21AB3C5B" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table DOS_PUBLICATIONSCONJOINTES
--------------------------------------------------------

  ALTER TABLE "DOS_PUBLICATIONSCONJOINTES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table SEARCH_SUBJECTS
--------------------------------------------------------

  ALTER TABLE "SEARCH_SUBJECTS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table MLBX_PROFILES
--------------------------------------------------------

  ALTER TABLE "MLBX_PROFILES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FILE
--------------------------------------------------------

  ALTER TABLE "FILE" ADD CONSTRAINT "FILE_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "FILE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FAVCON_FDRSIDS
--------------------------------------------------------

  ALTER TABLE "FAVCON_FDRSIDS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TEXTE_SIGNALE
--------------------------------------------------------

  ALTER TABLE "TEXTE_SIGNALE" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "TEXTE_SIGNALE" ADD CONSTRAINT "TEXTE_SIGNALE_PK" PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table MLBX_USERS
--------------------------------------------------------

  ALTER TABLE "MLBX_USERS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TAG
--------------------------------------------------------

  ALTER TABLE "TAG" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "TAG" ADD CONSTRAINT "TAG_PK" PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table DESTINATAIRECOMMUNICATION
--------------------------------------------------------

  ALTER TABLE "DESTINATAIRECOMMUNICATION" ADD CONSTRAINT "DESTINATAIRECOMMUNICATION_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "DESTINATAIRECOMMUNICATION" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table INDEXATION_RUBRIQUE_SOLON_EPG
--------------------------------------------------------

  ALTER TABLE "INDEXATION_RUBRIQUE_SOLON_EPG" ADD CONSTRAINT "INDE_4203B93F_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "INDEXATION_RUBRIQUE_SOLON_EPG" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PARAPHEUR_FOLDER_SOLON_EPG
--------------------------------------------------------

  ALTER TABLE "PARAPHEUR_FOLDER_SOLON_EPG" ADD CONSTRAINT "PARAPHEUR_FOLDER_SOLON_EPG_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PARAPHEUR_FOLDER_SOLON_EPG" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ITEM#ANONYMOUSTYPE
--------------------------------------------------------

  ALTER TABLE "ITEM#ANONYMOUSTYPE" ADD CONSTRAINT "ITEM#ANONYMOUSTYPE_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "ITEM#ANONYMOUSTYPE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PARTICIPANTLIST
--------------------------------------------------------

  ALTER TABLE "PARTICIPANTLIST" ADD CONSTRAINT "PARTICIPANTLIST_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PARTICIPANTLIST" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table DOCRI_PARTICIPATINGDOCUMENTS
--------------------------------------------------------

  ALTER TABLE "DOCRI_PARTICIPATINGDOCUMENTS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table RESCON_USERSNAMES
--------------------------------------------------------

  ALTER TABLE "RESCON_USERSNAMES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table AMPLIATIONDESTINATAIREMAILS
--------------------------------------------------------

  ALTER TABLE "AMPLIATIONDESTINATAIREMAILS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table REL_SRCH_ECM_PATH
--------------------------------------------------------

  ALTER TABLE "REL_SRCH_ECM_PATH" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PUSR_IDCOLONNEESPACET_934A8665
--------------------------------------------------------

  ALTER TABLE "PUSR_IDCOLONNEESPACET_934A8665" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ECHEANCIER_PROMULGATION
--------------------------------------------------------

  ALTER TABLE "ECHEANCIER_PROMULGATION" ADD CONSTRAINT "ECHEANCIER_PROMULGATION_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "ECHEANCIER_PROMULGATION" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FSD_ECM_PATH
--------------------------------------------------------

  ALTER TABLE "FSD_ECM_PATH" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FP341_COAUTEUR
--------------------------------------------------------

  ALTER TABLE "FP341_COAUTEUR" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PA_METADISPOCORBEILLE
--------------------------------------------------------

  ALTER TABLE "PA_METADISPOCORBEILLE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ACLS
--------------------------------------------------------

  ALTER TABLE "ACLS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table MAILBOX
--------------------------------------------------------

  ALTER TABLE "MAILBOX" ADD CONSTRAINT "MAILBOX_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "MAILBOX" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LOCKS
--------------------------------------------------------

  ALTER TABLE "LOCKS" ADD CONSTRAINT "LOCKS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "LOCKS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table BULLETIN_OFFICIEL
--------------------------------------------------------

  ALTER TABLE "BULLETIN_OFFICIEL" ADD CONSTRAINT "BULLETIN_OFFICIEL_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "BULLETIN_OFFICIEL" MODIFY ("ID" NOT NULL ENABLE);
  
--------------------------------------------------------
--  Constraints for Table VECTEUR_PUBLICATION
--------------------------------------------------------

  ALTER TABLE "VECTEUR_PUBLICATION" ADD CONSTRAINT "VECTEUR_PUBLICATION_PK" PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table FAVCON_DOSSIERSIDS
--------------------------------------------------------

  ALTER TABLE "FAVCON_DOSSIERSIDS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LOCALTHEMECONFIG
--------------------------------------------------------

  ALTER TABLE "LOCALTHEMECONFIG" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "LOCALTHEMECONFIG" ADD PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table REQUETE_DOSSIER_SIMPL_C483C4B2
--------------------------------------------------------

  ALTER TABLE "REQUETE_DOSSIER_SIMPL_C483C4B2" ADD CONSTRAINT "REQU_C483C4B2_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "REQUETE_DOSSIER_SIMPL_C483C4B2" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ROUTING_TASK
--------------------------------------------------------

  ALTER TABLE "ROUTING_TASK" ADD CONSTRAINT "ROUTING_TASK_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "ROUTING_TASK" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table NAVETTE
--------------------------------------------------------

  ALTER TABLE "NAVETTE" ADD CONSTRAINT "NAVETTE_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "NAVETTE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table UID
--------------------------------------------------------

  ALTER TABLE "UID" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "UID" ADD CONSTRAINT "UID_PK" PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table NOTE
--------------------------------------------------------

  ALTER TABLE "NOTE" ADD CONSTRAINT "NOTE_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "NOTE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TEXM_TRANSPOSITIONIDS
--------------------------------------------------------

  ALTER TABLE "TEXM_TRANSPOSITIONIDS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FAVORIS_RECHERCHE
--------------------------------------------------------

  ALTER TABLE "FAVORIS_RECHERCHE" ADD CONSTRAINT "FAVORIS_RECHERCHE_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "FAVORIS_RECHERCHE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TEXM_HABILITATIONIDS
--------------------------------------------------------

  ALTER TABLE "TEXM_HABILITATIONIDS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PARAMETRAGE_APPLICATION
--------------------------------------------------------

  ALTER TABLE "PARAMETRAGE_APPLICATION" ADD CONSTRAINT "PARAMETRAGE_APPLICATION_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PARAMETRAGE_APPLICATION" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TEXM_DECRETIDS
--------------------------------------------------------

  ALTER TABLE "TEXM_DECRETIDS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CLASSIFICATION_TARGETS
--------------------------------------------------------

  ALTER TABLE "CLASSIFICATION_TARGETS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBREF_VECTEURSPUBLICATIONS
--------------------------------------------------------

  ALTER TABLE "TBREF_VECTEURSPUBLICATIONS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FACETED_SEARCH_DEFAULT
--------------------------------------------------------

  ALTER TABLE "FACETED_SEARCH_DEFAULT" ADD CONSTRAINT "FACETED_SEARCH_DEFAULT_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "FACETED_SEARCH_DEFAULT" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ACLR
--------------------------------------------------------

  ALTER TABLE "ACLR" MODIFY ("ACL_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table MLBX_GROUPS
--------------------------------------------------------

  ALTER TABLE "MLBX_GROUPS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CMDIST_INITIAL_COPY_I_D6588F7E
--------------------------------------------------------

  ALTER TABLE "CMDIST_INITIAL_COPY_I_D6588F7E" MODIFY ("ID" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table CASE_LINK
--------------------------------------------------------

  ALTER TABLE "CASE_LINK" ADD CONSTRAINT "CASE_LINK_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "CASE_LINK" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table REQUETE_DOSSIER_SIMPL_7B2FB691
--------------------------------------------------------

  ALTER TABLE "REQUETE_DOSSIER_SIMPL_7B2FB691" ADD CONSTRAINT "REQU_7B2FB691_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "REQUETE_DOSSIER_SIMPL_7B2FB691" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table MAIL_RECIPIENTS
--------------------------------------------------------

  ALTER TABLE "MAIL_RECIPIENTS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table INFO_COMMENTS
--------------------------------------------------------

  ALTER TABLE "INFO_COMMENTS" ADD CONSTRAINT "INFO_COMMENTS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "INFO_COMMENTS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FDD_FORMATAUTORISE
--------------------------------------------------------

  ALTER TABLE "FDD_FORMATAUTORISE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ALTR_RECIPIENTS
--------------------------------------------------------

  ALTER TABLE "ALTR_RECIPIENTS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table INFO_UTILISATEUR_CONNECTION
--------------------------------------------------------

  ALTER TABLE "INFO_UTILISATEUR_CONNECTION" ADD CONSTRAINT "INFO_UTILISATEUR_CONNECTION_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "INFO_UTILISATEUR_CONNECTION" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LIS_IDSDOSSIER
--------------------------------------------------------

  ALTER TABLE "LIS_IDSDOSSIER" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CASE_ITEM
--------------------------------------------------------

  ALTER TABLE "CASE_ITEM" ADD CONSTRAINT "CASE_ITEM_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "CASE_ITEM" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table VCARD
--------------------------------------------------------

  ALTER TABLE "VCARD" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "VCARD" ADD CONSTRAINT "VCARD_PK" PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table FULLTEXT
--------------------------------------------------------

  ALTER TABLE "FULLTEXT" ADD CONSTRAINT "FULLTEXT_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "FULLTEXT" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table SEMAINE_PARLEMENTAIRE
--------------------------------------------------------

  ALTER TABLE "SEMAINE_PARLEMENTAIRE" ADD CONSTRAINT "SEMAINE_PARLEMENTAIRE_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "SEMAINE_PARLEMENTAIRE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table REPRESENTANT_OEP
--------------------------------------------------------

  ALTER TABLE "REPRESENTANT_OEP" ADD CONSTRAINT "REPRESENTANT_OEP_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "REPRESENTANT_OEP" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBREF_SIGNATAIRES
--------------------------------------------------------

  ALTER TABLE "TBREF_SIGNATAIRES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table DR$FULLTEXT_FULLTEXT_NOR_IDX$N
--------------------------------------------------------

  ALTER TABLE "DR$FULLTEXT_FULLTEXT_NOR_IDX$N" MODIFY ("NLT_MARK" NOT NULL ENABLE);
 
--------------------------------------------------------
--  Constraints for Table SMART_FOLDER
--------------------------------------------------------

  ALTER TABLE "SMART_FOLDER" ADD CONSTRAINT "SMART_FOLDER_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "SMART_FOLDER" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table REQUETE_DOSSIER
--------------------------------------------------------

  ALTER TABLE "REQUETE_DOSSIER" ADD CONSTRAINT "REQUETE_DOSSIER_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "REQUETE_DOSSIER" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table NAVIGATIONSETTINGS
--------------------------------------------------------

  ALTER TABLE "NAVIGATIONSETTINGS" ADD CONSTRAINT "NAVIGATIONSETTINGS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "NAVIGATIONSETTINGS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table DOS_NOMCOMPLETCONSEILLERSPMS
--------------------------------------------------------

  ALTER TABLE "DOS_NOMCOMPLETCONSEILLERSPMS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table DUBLINCORE
--------------------------------------------------------

  ALTER TABLE "DUBLINCORE" ADD CONSTRAINT "DUBLINCORE_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "DUBLINCORE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table MAIL_CC_RECIPIENTS
--------------------------------------------------------

  ALTER TABLE "MAIL_CC_RECIPIENTS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PROTOCOL
--------------------------------------------------------

  ALTER TABLE "PROTOCOL" ADD CONSTRAINT "PROTOCOL_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PROTOCOL" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PUSR_IDCOLONNEINSTANC_3FED9D1F
--------------------------------------------------------

  ALTER TABLE "PUSR_IDCOLONNEINSTANC_3FED9D1F" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table BIRTREPORT
--------------------------------------------------------

  ALTER TABLE "BIRTREPORT" ADD CONSTRAINT "BIRTREPORT_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "BIRTREPORT" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table RELATEDTEXTRESOURCE
--------------------------------------------------------

  ALTER TABLE "RELATEDTEXTRESOURCE" ADD CONSTRAINT "RELATEDTEXTRESOURCE_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "RELATEDTEXTRESOURCE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table WSSPE
--------------------------------------------------------

  ALTER TABLE "WSSPE" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "WSSPE" ADD CONSTRAINT "WSSPE_PK" PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table ACTIVITE_PARLEMENTAIRE
--------------------------------------------------------

  ALTER TABLE "ACTIVITE_PARLEMENTAIRE" ADD CONSTRAINT "ACTIVITE_PARLEMENTAIRE_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "ACTIVITE_PARLEMENTAIRE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBREF_SIGNATURECPM
--------------------------------------------------------

  ALTER TABLE "TBREF_SIGNATURECPM" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FICHE_PRESENTATION_341
--------------------------------------------------------

  ALTER TABLE "FICHE_PRESENTATION_341" ADD CONSTRAINT "FICHE_PRESENTATION_341_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "FICHE_PRESENTATION_341" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table WEBCONTAINER
--------------------------------------------------------

  ALTER TABLE "WEBCONTAINER" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "WEBCONTAINER" ADD CONSTRAINT "WEBCONTAINER_PK" PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table TEXM_DOSSIERSNOR
--------------------------------------------------------

  ALTER TABLE "TEXM_DOSSIERSNOR" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table MODELE_PARAPHEUR_SOLON_EPG
--------------------------------------------------------

  ALTER TABLE "MODELE_PARAPHEUR_SOLON_EPG" ADD CONSTRAINT "MODELE_PARAPHEUR_SOLON_EPG_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "MODELE_PARAPHEUR_SOLON_EPG" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table MAILBOX_ID
--------------------------------------------------------

  ALTER TABLE "MAILBOX_ID" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FSD_DC_COVERAGE
--------------------------------------------------------

  ALTER TABLE "FSD_DC_COVERAGE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LIGNEPROGRAMMATIONHAB
--------------------------------------------------------

  ALTER TABLE "LIGNEPROGRAMMATIONHAB" ADD CONSTRAINT "LIGNEPROGRAMMATIONHAB_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "LIGNEPROGRAMMATIONHAB" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table BIRT_RESULTAT_FICHIER
--------------------------------------------------------

  ALTER TABLE "BIRT_RESULTAT_FICHIER" ADD CONSTRAINT "BIRT_RESULTAT_FICHIER_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "BIRT_RESULTAT_FICHIER" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ANCESTORS
--------------------------------------------------------

  ALTER TABLE "ANCESTORS" MODIFY ("HIERARCHY_ID" NOT NULL ENABLE);
 
--------------------------------------------------------
--  Constraints for Table TABLE_REFERENCE
--------------------------------------------------------

  ALTER TABLE "TABLE_REFERENCE" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "TABLE_REFERENCE" ADD CONSTRAINT "TABLE_REFERENCE_PK" PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table NXP_LOGS_EXTINFO
--------------------------------------------------------

  ALTER TABLE "NXP_LOGS_EXTINFO" MODIFY ("DISCRIMINATOR" NOT NULL ENABLE);
 
  ALTER TABLE "NXP_LOGS_EXTINFO" MODIFY ("LOG_EXTINFO_ID" NOT NULL ENABLE);
 
  ALTER TABLE "NXP_LOGS_EXTINFO" ADD PRIMARY KEY ("LOG_EXTINFO_ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table RETOUR_DILA
--------------------------------------------------------

  ALTER TABLE "RETOUR_DILA" ADD CONSTRAINT "RETOUR_DILA_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "RETOUR_DILA" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table INFOHISTORIQUEAMPLIATION
--------------------------------------------------------

  ALTER TABLE "INFOHISTORIQUEAMPLIATION" ADD CONSTRAINT "INFOHISTORIQUEAMPLIATION_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "INFOHISTORIQUEAMPLIATION" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PUSR_NOTIFICATIONTYPEACTES
--------------------------------------------------------

  ALTER TABLE "PUSR_NOTIFICATIONTYPEACTES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table DOS_TRANSPOSITIONDIRE_3E14AB2D
--------------------------------------------------------

  ALTER TABLE "DOS_TRANSPOSITIONDIRE_3E14AB2D" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FACETED_SEARCH
--------------------------------------------------------

  ALTER TABLE "FACETED_SEARCH" ADD CONSTRAINT "FACETED_SEARCH_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "FACETED_SEARCH" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FSD_DC_SUBJECTS
--------------------------------------------------------

  ALTER TABLE "FSD_DC_SUBJECTS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CONTENT
--------------------------------------------------------

  ALTER TABLE "CONTENT" ADD CONSTRAINT "CONTENT_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "CONTENT" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table QUERYNAV
--------------------------------------------------------

  ALTER TABLE "QUERYNAV" ADD CONSTRAINT "QUERYNAV_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "QUERYNAV" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PARUTIONBO
--------------------------------------------------------

  ALTER TABLE "PARUTIONBO" ADD CONSTRAINT "PARUTIONBO_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PARUTIONBO" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table INFONUMEROLISTESIGNATURE
--------------------------------------------------------

  ALTER TABLE "INFONUMEROLISTESIGNATURE" ADD CONSTRAINT "INFONUMEROLISTESIGNATURE_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "INFONUMEROLISTESIGNATURE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TEXTE_MAITRE
--------------------------------------------------------

  ALTER TABLE "TEXTE_MAITRE" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "TEXTE_MAITRE" ADD CONSTRAINT "TEXTE_MAITRE_PK" PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table NXP_LOGS
--------------------------------------------------------

  ALTER TABLE "NXP_LOGS" MODIFY ("LOG_ID" NOT NULL ENABLE);
 
  ALTER TABLE "NXP_LOGS" MODIFY ("LOG_EVENT_ID" NOT NULL ENABLE);
 
  ALTER TABLE "NXP_LOGS" ADD PRIMARY KEY ("LOG_ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table USERSETTINGS
--------------------------------------------------------

  ALTER TABLE "USERSETTINGS" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "USERSETTINGS" ADD CONSTRAINT "USERSETTINGS_PK" PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table RELATION_SEARCH
--------------------------------------------------------

  ALTER TABLE "RELATION_SEARCH" ADD CONSTRAINT "RELATION_SEARCH_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "RELATION_SEARCH" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LISTE_TRAITEMENT_PAPI_144E75F6
--------------------------------------------------------

  ALTER TABLE "LISTE_TRAITEMENT_PAPI_144E75F6" ADD CONSTRAINT "LIST_144E75F6_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "LISTE_TRAITEMENT_PAPI_144E75F6" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table DOS_VECTEURPUBLICATION
--------------------------------------------------------

  ALTER TABLE "DOS_VECTEURPUBLICATION" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table REQUETE_DOSSIER_SIMPL_A3A85FF2
--------------------------------------------------------

  ALTER TABLE "REQUETE_DOSSIER_SIMPL_A3A85FF2" ADD CONSTRAINT "REQU_A3A85FF2_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "REQUETE_DOSSIER_SIMPL_A3A85FF2" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table JETON_MAITRE
--------------------------------------------------------

  ALTER TABLE "JETON_MAITRE" ADD CONSTRAINT "JETON_MAITRE_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "JETON_MAITRE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LOCK_JETON_MAITRE
--------------------------------------------------------

  ALTER TABLE "LOCK_JETON_MAITRE" ADD CONSTRAINT "LOCK_JETON_MAITRE_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "LOCK_JETON_MAITRE" MODIFY ("ID" NOT NULL ENABLE);  
  
--------------------------------------------------------
--  Constraints for Table PUBLISH_SECTIONS
--------------------------------------------------------

  ALTER TABLE "PUBLISH_SECTIONS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table HIERARCHY_READ_ACL
--------------------------------------------------------

  ALTER TABLE "HIERARCHY_READ_ACL" ADD PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table FSD_DC_NATURE
--------------------------------------------------------

  ALTER TABLE "FSD_DC_NATURE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FOND_DOSSIER_FOLDER_SOLON_EPG
--------------------------------------------------------

  ALTER TABLE "FOND_DOSSIER_FOLDER_SOLON_EPG" ADD CONSTRAINT "FOND_67CE5EE1_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "FOND_DOSSIER_FOLDER_SOLON_EPG" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CONSEIL_ETAT
--------------------------------------------------------

  ALTER TABLE "CONSEIL_ETAT" ADD CONSTRAINT "CONSEIL_ETAT_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "CONSEIL_ETAT" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table STEP_FOLDER
--------------------------------------------------------

  ALTER TABLE "STEP_FOLDER" ADD CONSTRAINT "STEP_FOLDER_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "STEP_FOLDER" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TABDYN_DESTINATAIRESID
--------------------------------------------------------

  ALTER TABLE "TABDYN_DESTINATAIRESID" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table DR$FULLTEXT_FULLTEXT_IDX$I
--------------------------------------------------------

  ALTER TABLE "DR$FULLTEXT_FULLTEXT_IDX$I" MODIFY ("TOKEN_TEXT" NOT NULL ENABLE);
 
  ALTER TABLE "DR$FULLTEXT_FULLTEXT_IDX$I" MODIFY ("TOKEN_TYPE" NOT NULL ENABLE);
 
  ALTER TABLE "DR$FULLTEXT_FULLTEXT_IDX$I" MODIFY ("TOKEN_FIRST" NOT NULL ENABLE);
 
  ALTER TABLE "DR$FULLTEXT_FULLTEXT_IDX$I" MODIFY ("TOKEN_LAST" NOT NULL ENABLE);
 
  ALTER TABLE "DR$FULLTEXT_FULLTEXT_IDX$I" MODIFY ("TOKEN_COUNT" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FEUILLE_ROUTE
--------------------------------------------------------

  ALTER TABLE "FEUILLE_ROUTE" ADD CONSTRAINT "FEUILLE_ROUTE_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "FEUILLE_ROUTE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CMDOC_SENDERS
--------------------------------------------------------

  ALTER TABLE "CMDOC_SENDERS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table COMMENT
--------------------------------------------------------

  ALTER TABLE "COMMENT" ADD CONSTRAINT "COMMENT_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "COMMENT" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FAVCON_USERSNAMES
--------------------------------------------------------

  ALTER TABLE "FAVCON_USERSNAMES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FICHE_PRESENTATION_DR
--------------------------------------------------------

  ALTER TABLE "FICHE_PRESENTATION_DR" ADD CONSTRAINT "FICHE_PRESENTATION_DR_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "FICHE_PRESENTATION_DR" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CONTENT_VIEW_DISPLAY
--------------------------------------------------------

  ALTER TABLE "CONTENT_VIEW_DISPLAY" ADD CONSTRAINT "CONTENT_VIEW_DISPLAY_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "CONTENT_VIEW_DISPLAY" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FSD_DC_CREATOR
--------------------------------------------------------

  ALTER TABLE "FSD_DC_CREATOR" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table DR$FULLTEXT_FULLTEXT_IDX$N
--------------------------------------------------------

  ALTER TABLE "DR$FULLTEXT_FULLTEXT_IDX$N" MODIFY ("NLT_MARK" NOT NULL ENABLE);
 
--------------------------------------------------------
--  Constraints for Table MFDDR_FORMATAUTORISE
--------------------------------------------------------

  ALTER TABLE "MFDDR_FORMATAUTORISE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table DOSSIER_SOLON_EPG
--------------------------------------------------------

  ALTER TABLE "DOSSIER_SOLON_EPG" ADD CONSTRAINT "DOSSIER_SOLON_EPG_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "DOSSIER_SOLON_EPG" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TRAITEMENT_PAPIER
--------------------------------------------------------

  ALTER TABLE "TRAITEMENT_PAPIER" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "TRAITEMENT_PAPIER" ADD CONSTRAINT "TRAITEMENT_PAPIER_PK" PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table TBREF_CABINETPM
--------------------------------------------------------

  ALTER TABLE "TBREF_CABINETPM" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table UITYPESCONF_DENIEDTYPES
--------------------------------------------------------

  ALTER TABLE "UITYPESCONF_DENIEDTYPES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ADVANCED_SEARCH
--------------------------------------------------------

  ALTER TABLE "ADVANCED_SEARCH" ADD CONSTRAINT "ADVANCED_SEARCH_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "ADVANCED_SEARCH" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table USERSUBSCRIPTION
--------------------------------------------------------

  ALTER TABLE "USERSUBSCRIPTION" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "USERSUBSCRIPTION" ADD PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table INDEXATION_MOT_CLE_SOLON_EPG
--------------------------------------------------------

  ALTER TABLE "INDEXATION_MOT_CLE_SOLON_EPG" ADD CONSTRAINT "INDE_4E2ADFD2_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "INDEXATION_MOT_CLE_SOLON_EPG" MODIFY ("ID" NOT NULL ENABLE);

--------------------------------------------------------
--  Constraints for Table DC_CONTRIBUTORS
--------------------------------------------------------

  ALTER TABLE "DC_CONTRIBUTORS" MODIFY ("ID" NOT NULL ENABLE);
  
--------------------------------------------------------
--  Constraints for Table CMDIST_INITIAL_COPY_E_B3610C04
--------------------------------------------------------

  ALTER TABLE "CMDIST_INITIAL_COPY_E_B3610C04" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table NXP_UIDSEQ
--------------------------------------------------------

  ALTER TABLE "NXP_UIDSEQ" MODIFY ("SEQ_ID" NOT NULL ENABLE);
 
  ALTER TABLE "NXP_UIDSEQ" MODIFY ("SEQ_INDEX" NOT NULL ENABLE);
 
  ALTER TABLE "NXP_UIDSEQ" MODIFY ("SEQ_KEY" NOT NULL ENABLE);
 
  ALTER TABLE "NXP_UIDSEQ" ADD PRIMARY KEY ("SEQ_ID") ENABLE;
 
  ALTER TABLE "NXP_UIDSEQ" ADD UNIQUE ("SEQ_KEY") ENABLE;
--------------------------------------------------------
--  Constraints for Table MAILBOX_SOLON_EPG
--------------------------------------------------------

  ALTER TABLE "MAILBOX_SOLON_EPG" ADD CONSTRAINT "MAILBOX_SOLON_EPG_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "MAILBOX_SOLON_EPG" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FILE_SOLON_EPG
--------------------------------------------------------

  ALTER TABLE "FILE_SOLON_EPG" ADD CONSTRAINT "FILE_SOLON_EPG_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "FILE_SOLON_EPG" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CMDIST_INITIAL_ACTION_4CD43708
--------------------------------------------------------

  ALTER TABLE "CMDIST_INITIAL_ACTION_4CD43708" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table INFOEPREUVE
--------------------------------------------------------

  ALTER TABLE "INFOEPREUVE" ADD CONSTRAINT "INFOEPREUVE_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "INFOEPREUVE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TBREF_DIRECTIONPM
--------------------------------------------------------

  ALTER TABLE "TBREF_DIRECTIONPM" MODIFY ("ID" NOT NULL ENABLE);
  
--------------------------------------------------------
--  Constraints for Table TBREF_RETOURDAN
--------------------------------------------------------

  ALTER TABLE "TBREF_RETOURDAN" MODIFY ("ID" NOT NULL ENABLE);
  
--------------------------------------------------------
--  Constraints for Table CMDIST_ALL_ACTION_PAR_6B4BBED8
--------------------------------------------------------

  ALTER TABLE "CMDIST_ALL_ACTION_PAR_6B4BBED8" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table SEARCH_COVERAGE
--------------------------------------------------------

  ALTER TABLE "SEARCH_COVERAGE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ETAT_APPLICATION
--------------------------------------------------------

  ALTER TABLE "ETAT_APPLICATION" ADD CONSTRAINT "ETAT_APPLICATION_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "ETAT_APPLICATION" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table COMMON
--------------------------------------------------------

  ALTER TABLE "COMMON" ADD CONSTRAINT "COMMON_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "COMMON" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table MOTS_CLES
--------------------------------------------------------

  ALTER TABLE "MOTS_CLES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FICHE_PRESENTATION_OEP
--------------------------------------------------------

  ALTER TABLE "FICHE_PRESENTATION_OEP" ADD CONSTRAINT "FICHE_PRESENTATION_OEP_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "FICHE_PRESENTATION_OEP" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PROXIES
--------------------------------------------------------

  ALTER TABLE "PROXIES" ADD CONSTRAINT "PROXIES_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PROXIES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CVD_SELECTEDLAYOUTCOLUMNS
--------------------------------------------------------

  ALTER TABLE "CVD_SELECTEDLAYOUTCOLUMNS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table SEARCH_SEARCHPATH
--------------------------------------------------------

  ALTER TABLE "SEARCH_SEARCHPATH" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PARAMETRE
--------------------------------------------------------

  ALTER TABLE "PARAMETRE" ADD CONSTRAINT "PARAMETRE_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PARAMETRE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table HIERARCHY
--------------------------------------------------------

  ALTER TABLE "HIERARCHY" ADD CONSTRAINT "HIERARCHY_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "HIERARCHY" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table MINISTEREIDS
--------------------------------------------------------

  ALTER TABLE "MINISTEREIDS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TP_AMPLIATIONDESTINATAIREMAILS
--------------------------------------------------------

  ALTER TABLE "TP_AMPLIATIONDESTINATAIREMAILS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table UITYPESCONF_ALLOWEDTYPES
--------------------------------------------------------

  ALTER TABLE "UITYPESCONF_ALLOWEDTYPES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table RESCON_FDRSIDS
--------------------------------------------------------

  ALTER TABLE "RESCON_FDRSIDS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FICHE_LOI
--------------------------------------------------------

  ALTER TABLE "FICHE_LOI" ADD CONSTRAINT "FICHE_LOI_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "FICHE_LOI" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table VERSIONS
--------------------------------------------------------

  ALTER TABLE "VERSIONS" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "VERSIONS" ADD CONSTRAINT "VERSIONS_PK" PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table ALERT
--------------------------------------------------------

  ALTER TABLE "ALERT" ADD CONSTRAINT "ALERT_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "ALERT" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TABLEAU_DYNAMIQUE
--------------------------------------------------------

  ALTER TABLE "TABLEAU_DYNAMIQUE" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "TABLEAU_DYNAMIQUE" ADD CONSTRAINT "TABLEAU_DYNAMIQUE_PK" PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table DOS_LIBRE
--------------------------------------------------------

  ALTER TABLE "DOS_LIBRE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table DOS_MOTSCLES
--------------------------------------------------------

  ALTER TABLE "DOS_MOTSCLES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table DR$FULLTEXT_FULLTEXT_NOR_IDX$I
--------------------------------------------------------

  ALTER TABLE "DR$FULLTEXT_FULLTEXT_NOR_IDX$I" MODIFY ("TOKEN_TEXT" NOT NULL ENABLE);
 
  ALTER TABLE "DR$FULLTEXT_FULLTEXT_NOR_IDX$I" MODIFY ("TOKEN_TYPE" NOT NULL ENABLE);
 
  ALTER TABLE "DR$FULLTEXT_FULLTEXT_NOR_IDX$I" MODIFY ("TOKEN_FIRST" NOT NULL ENABLE);
 
  ALTER TABLE "DR$FULLTEXT_FULLTEXT_NOR_IDX$I" MODIFY ("TOKEN_LAST" NOT NULL ENABLE);
 
  ALTER TABLE "DR$FULLTEXT_FULLTEXT_NOR_IDX$I" MODIFY ("TOKEN_COUNT" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table PARAMETER
--------------------------------------------------------

  ALTER TABLE "PARAMETER" ADD CONSTRAINT "PARAMETER_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "PARAMETER" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table CMDIST_INITIAL_ACTION_88A481B7
--------------------------------------------------------

  ALTER TABLE "CMDIST_INITIAL_ACTION_88A481B7" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ACTIONNABLE_CASE_LINK
--------------------------------------------------------

  ALTER TABLE "ACTIONNABLE_CASE_LINK" ADD CONSTRAINT "ACTIONNABLE_CASE_LINK_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "ACTIONNABLE_CASE_LINK" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TEXM_LOIRATIFICATIONIDS
--------------------------------------------------------

  ALTER TABLE "TEXM_LOIRATIFICATIONIDS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table BIRTREPORTMODEL
--------------------------------------------------------

  ALTER TABLE "BIRTREPORTMODEL" ADD CONSTRAINT "BIRTREPORTMODEL_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "BIRTREPORTMODEL" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table DONNEESSIGNATAIRE
--------------------------------------------------------

  ALTER TABLE "DONNEESSIGNATAIRE" ADD CONSTRAINT "DONNEESSIGNATAIRE_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "DONNEESSIGNATAIRE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table REQUETE_DOSSIER_SIMPL_743D2E6F
--------------------------------------------------------

  ALTER TABLE "REQUETE_DOSSIER_SIMPL_743D2E6F" ADD CONSTRAINT "REQU_743D2E6F_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "REQUETE_DOSSIER_SIMPL_743D2E6F" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table MLBX_FAVORITES
--------------------------------------------------------

  ALTER TABLE "MLBX_FAVORITES" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table TEXM_MESUREIDS
--------------------------------------------------------

  ALTER TABLE "TEXM_MESUREIDS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table RQDCP_IDSTATUTSARCHIV_82ED246B
--------------------------------------------------------

  ALTER TABLE "RQDCP_IDSTATUTSARCHIV_82ED246B" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table DEFAULTSETTINGS
--------------------------------------------------------

  ALTER TABLE "DEFAULTSETTINGS" ADD CONSTRAINT "DEFAULTSETTINGS_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "DEFAULTSETTINGS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ACLR_USER_MAP
--------------------------------------------------------

  ALTER TABLE "ACLR_USER_MAP" MODIFY ("USER_ID" NOT NULL ENABLE);
 
  ALTER TABLE "ACLR_USER_MAP" MODIFY ("ACL_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table UI_TYPES_CONFIGURATION
--------------------------------------------------------

  ALTER TABLE "UI_TYPES_CONFIGURATION" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "UI_TYPES_CONFIGURATION" ADD CONSTRAINT "UI_TYPES_CONFIGURATION_PK" PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table TEXM_ORDONNANCEIDS
--------------------------------------------------------

  ALTER TABLE "TEXM_ORDONNANCEIDS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table DOS_NOMCOMPLETCHARGESMISSIONS
--------------------------------------------------------

  ALTER TABLE "DOS_NOMCOMPLETCHARGESMISSIONS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table SEARCH_NATURE
--------------------------------------------------------

  ALTER TABLE "SEARCH_NATURE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table NXP_LOGS_MAPEXTINFOS
--------------------------------------------------------

  ALTER TABLE "NXP_LOGS_MAPEXTINFOS" MODIFY ("LOG_FK" NOT NULL ENABLE);
 
  ALTER TABLE "NXP_LOGS_MAPEXTINFOS" MODIFY ("INFO_FK" NOT NULL ENABLE);
 
  ALTER TABLE "NXP_LOGS_MAPEXTINFOS" MODIFY ("MAPKEY" NOT NULL ENABLE);
 
  ALTER TABLE "NXP_LOGS_MAPEXTINFOS" ADD PRIMARY KEY ("LOG_FK", "MAPKEY") ENABLE;
 
  ALTER TABLE "NXP_LOGS_MAPEXTINFOS" ADD UNIQUE ("INFO_FK") ENABLE;
--------------------------------------------------------
--  Constraints for Table TYPE_CONTACT
--------------------------------------------------------

  ALTER TABLE "TYPE_CONTACT" MODIFY ("ID" NOT NULL ENABLE);
 
  ALTER TABLE "TYPE_CONTACT" ADD CONSTRAINT "TYPE_CONTACT_PK" PRIMARY KEY ("ID") ENABLE;
--------------------------------------------------------
--  Constraints for Table MLBX_NOTIFIED_USERS
--------------------------------------------------------

  ALTER TABLE "MLBX_NOTIFIED_USERS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table LIGNEPROGRAMMATION
--------------------------------------------------------

  ALTER TABLE "LIGNEPROGRAMMATION" ADD CONSTRAINT "LIGNEPROGRAMMATION_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "LIGNEPROGRAMMATION" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table JETON_DOC
--------------------------------------------------------

  ALTER TABLE "JETON_DOC" ADD CONSTRAINT "JETON_DOC_PK" PRIMARY KEY ("ID") ENABLE;
 
  ALTER TABLE "JETON_DOC" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table ACTIVITE_NORMATIVE
--------------------------------------------------------

  ALTER TABLE "ACTIVITE_NORMATIVE" ADD CONSTRAINT "ACTIVITE_NORMATIVE_PK" PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "ACTIVITE_NORMATIVE" MODIFY ("ID" NOT NULL ENABLE);
  
--------------------------------------------------------
--  Constraints for Table RLACL
--------------------------------------------------------
  
  ALTER TABLE RLACL ADD CONSTRAINT RLACL_HIERARCHY_ID_PKEY PRIMARY KEY (HIERARCHY_ID);
 
--------------------------------------------------------
--  Ref Constraints for Table ACLS
--------------------------------------------------------

  ALTER TABLE "ACLS" ADD CONSTRAINT "ACLS_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ACTIONNABLE_CASE_LINK
--------------------------------------------------------

  ALTER TABLE "ACTIONNABLE_CASE_LINK" ADD CONSTRAINT "ACTI_D69A363C_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ACTIVITE_NORMATIVE
--------------------------------------------------------

  ALTER TABLE "ACTIVITE_NORMATIVE" ADD CONSTRAINT "ACTI_5342CA7F_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ACTIVITE_NORMATIVE_PR_DE9E019D
--------------------------------------------------------

  ALTER TABLE "ACTIVITE_NORMATIVE_PR_DE9E019D" ADD CONSTRAINT "ACTI_559BD1E2_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ACTIVITE_PARLEMENTAIRE
--------------------------------------------------------

  ALTER TABLE "ACTIVITE_PARLEMENTAIRE" ADD CONSTRAINT "ACTI_C9AA5871_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ADVANCED_SEARCH
--------------------------------------------------------

  ALTER TABLE "ADVANCED_SEARCH" ADD CONSTRAINT "ADVA_102157D6_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ALERT
--------------------------------------------------------

  ALTER TABLE "ALERT" ADD CONSTRAINT "ALERT_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ALTR_RECIPIENTS
--------------------------------------------------------

  ALTER TABLE "ALTR_RECIPIENTS" ADD CONSTRAINT "ALTR_C630CF28_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table AMPLIATIONDESTINATAIREMAILS
--------------------------------------------------------

  ALTER TABLE "AMPLIATIONDESTINATAIREMAILS" ADD CONSTRAINT "AMPL_EB69C656_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ANCESTORS
--------------------------------------------------------

  ALTER TABLE "ANCESTORS" ADD CONSTRAINT "ANCESTORS_HIERARCHY_ID_FK" FOREIGN KEY ("HIERARCHY_ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table BIRTREPORT
--------------------------------------------------------

  ALTER TABLE "BIRTREPORT" ADD CONSTRAINT "BIRTREPORT_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table BIRTREPORTMODEL
--------------------------------------------------------

  ALTER TABLE "BIRTREPORTMODEL" ADD CONSTRAINT "BIRT_CB0F83D0_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table BIRT_RESULTAT_FICHIER
--------------------------------------------------------

  ALTER TABLE "BIRT_RESULTAT_FICHIER" ADD CONSTRAINT "BIRT_F0C83B6C_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table BULLETIN_OFFICIEL
--------------------------------------------------------

  ALTER TABLE "BULLETIN_OFFICIEL" ADD CONSTRAINT "BULL_C3AA52B1_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
	  
--------------------------------------------------------
--  Ref Constraints for Table VECTEUR_PUBLICATION
--------------------------------------------------------

  ALTER TABLE "VECTEUR_PUBLICATION" ADD CONSTRAINT "VECTPUB_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CASE_DOCUMENTSID
--------------------------------------------------------

  ALTER TABLE "CASE_DOCUMENTSID" ADD CONSTRAINT "CASE_99AB76F4_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CASE_ITEM
--------------------------------------------------------

  ALTER TABLE "CASE_ITEM" ADD CONSTRAINT "CASE_ITEM_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CASE_LINK
--------------------------------------------------------

  ALTER TABLE "CASE_LINK" ADD CONSTRAINT "CASE_LINK_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CLASSIFICATION_TARGETS
--------------------------------------------------------

  ALTER TABLE "CLASSIFICATION_TARGETS" ADD CONSTRAINT "CLAS_4898D61D_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CMDIST_ALL_ACTION_PAR_6B4BBED8
--------------------------------------------------------

  ALTER TABLE "CMDIST_ALL_ACTION_PAR_6B4BBED8" ADD CONSTRAINT "CMDI_7B09D466_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CMDIST_ALL_COPY_PARTI_21AB3C5B
--------------------------------------------------------

  ALTER TABLE "CMDIST_ALL_COPY_PARTI_21AB3C5B" ADD CONSTRAINT "CMDI_516D49CB_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CMDIST_INITIAL_ACTION_4CD43708
--------------------------------------------------------

  ALTER TABLE "CMDIST_INITIAL_ACTION_4CD43708" ADD CONSTRAINT "CMDI_DBF5EC6D_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CMDIST_INITIAL_ACTION_88A481B7
--------------------------------------------------------

  ALTER TABLE "CMDIST_INITIAL_ACTION_88A481B7" ADD CONSTRAINT "CMDI_248596C2_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CMDIST_INITIAL_COPY_E_B3610C04
--------------------------------------------------------

  ALTER TABLE "CMDIST_INITIAL_COPY_E_B3610C04" ADD CONSTRAINT "CMDI_ED3FDFAC_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CMDIST_INITIAL_COPY_I_D6588F7E
--------------------------------------------------------

  ALTER TABLE "CMDIST_INITIAL_COPY_I_D6588F7E" ADD CONSTRAINT "CMDI_3EE0F322_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CMDOC_SENDERS
--------------------------------------------------------

  ALTER TABLE "CMDOC_SENDERS" ADD CONSTRAINT "CMDOC_SENDERS_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table COMMENT
--------------------------------------------------------

  ALTER TABLE "COMMENT" ADD CONSTRAINT "COMMENT_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table COMMON
--------------------------------------------------------

  ALTER TABLE "COMMON" ADD CONSTRAINT "COMMON_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CONSEIL_ETAT
--------------------------------------------------------

  ALTER TABLE "CONSEIL_ETAT" ADD CONSTRAINT "CONSEIL_ETAT_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CONTENT
--------------------------------------------------------

  ALTER TABLE "CONTENT" ADD CONSTRAINT "CONTENT_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CONTENT_VIEW_DISPLAY
--------------------------------------------------------

  ALTER TABLE "CONTENT_VIEW_DISPLAY" ADD CONSTRAINT "CONT_0FEB3678_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table CVD_SELECTEDLAYOUTCOLUMNS
--------------------------------------------------------

  ALTER TABLE "CVD_SELECTEDLAYOUTCOLUMNS" ADD CONSTRAINT "CVD__98E4BA2C_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;

--------------------------------------------------------
--  Ref Constraints for Table DC_CONTRIBUTORS
--------------------------------------------------------

  ALTER TABLE "DC_CONTRIBUTORS" ADD CONSTRAINT "DC_C_4015E4A8_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table DC_SUBJECTS
--------------------------------------------------------

  ALTER TABLE "DC_SUBJECTS" ADD CONSTRAINT "DC_SUBJECTS_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;

	  
--------------------------------------------------------
--  Ref Constraints for Table DEFAULTSETTINGS
--------------------------------------------------------

  ALTER TABLE "DEFAULTSETTINGS" ADD CONSTRAINT "DEFA_795A5897_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table DESTINATAIRECOMMUNICATION
--------------------------------------------------------

  ALTER TABLE "DESTINATAIRECOMMUNICATION" ADD CONSTRAINT "DEST_60F80E49_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table DOCRI_PARTICIPATINGDOCUMENTS
--------------------------------------------------------

  ALTER TABLE "DOCRI_PARTICIPATINGDOCUMENTS" ADD CONSTRAINT "DOCR_336D9931_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table DONNEESSIGNATAIRE
--------------------------------------------------------

  ALTER TABLE "DONNEESSIGNATAIRE" ADD CONSTRAINT "DONN_07AEAC6A_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table DOS_LIBRE
--------------------------------------------------------

  ALTER TABLE "DOS_LIBRE" ADD CONSTRAINT "DOS_LIBRE_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table DOS_MOTSCLES
--------------------------------------------------------

  ALTER TABLE "DOS_MOTSCLES" ADD CONSTRAINT "DOS_MOTSCLES_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table DOS_NOMCOMPLETCHARGESMISSIONS
--------------------------------------------------------

  ALTER TABLE "DOS_NOMCOMPLETCHARGESMISSIONS" ADD CONSTRAINT "DOS__21E46837_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table DOS_NOMCOMPLETCONSEILLERSPMS
--------------------------------------------------------

  ALTER TABLE "DOS_NOMCOMPLETCONSEILLERSPMS" ADD CONSTRAINT "DOS__97EAE8F3_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table DOS_PUBLICATIONSCONJOINTES
--------------------------------------------------------

  ALTER TABLE "DOS_PUBLICATIONSCONJOINTES" ADD CONSTRAINT "DOS__45E541BD_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table DOS_RUBRIQUES
--------------------------------------------------------

  ALTER TABLE "DOS_RUBRIQUES" ADD CONSTRAINT "DOS_RUBRIQUES_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table DOSSIER_SOLON_EPG
--------------------------------------------------------

  ALTER TABLE "DOSSIER_SOLON_EPG" ADD CONSTRAINT "DOSS_9D9380AE_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table DOS_TRANSPOSITIONDIRE_3E14AB2D
--------------------------------------------------------

  ALTER TABLE "DOS_TRANSPOSITIONDIRE_3E14AB2D" ADD CONSTRAINT "DOS__FEF7B6C9_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table DOS_VECTEURPUBLICATION
--------------------------------------------------------

  ALTER TABLE "DOS_VECTEURPUBLICATION" ADD CONSTRAINT "DOS__586ECAB7_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table DUBLINCORE
--------------------------------------------------------

  ALTER TABLE "DUBLINCORE" ADD CONSTRAINT "DUBLINCORE_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ECHEANCIER_PROMULGATION
--------------------------------------------------------

  ALTER TABLE "ECHEANCIER_PROMULGATION" ADD CONSTRAINT "ECHE_7D51698A_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ETAT_APPLICATION
--------------------------------------------------------

  ALTER TABLE "ETAT_APPLICATION" ADD CONSTRAINT "ETAT_E81FD654_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FACETED_SEARCH
--------------------------------------------------------

  ALTER TABLE "FACETED_SEARCH" ADD CONSTRAINT "FACETED_SEARCH_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FACETED_SEARCH_DEFAULT
--------------------------------------------------------

  ALTER TABLE "FACETED_SEARCH_DEFAULT" ADD CONSTRAINT "FACE_D7EAAFD8_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FAVCON_DOSSIERSIDS
--------------------------------------------------------

  ALTER TABLE "FAVCON_DOSSIERSIDS" ADD CONSTRAINT "FAVC_D233BBDB_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FAVCON_FDRSIDS
--------------------------------------------------------

  ALTER TABLE "FAVCON_FDRSIDS" ADD CONSTRAINT "FAVCON_FDRSIDS_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FAVCON_USERSNAMES
--------------------------------------------------------

  ALTER TABLE "FAVCON_USERSNAMES" ADD CONSTRAINT "FAVC_E6ECDD48_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FAVORIS_RECHERCHE
--------------------------------------------------------

  ALTER TABLE "FAVORIS_RECHERCHE" ADD CONSTRAINT "FAVO_718E9611_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FDD_FORMATAUTORISE
--------------------------------------------------------

  ALTER TABLE "FDD_FORMATAUTORISE" ADD CONSTRAINT "FDD__6727B73B_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FEUILLE_ROUTE
--------------------------------------------------------

  ALTER TABLE "FEUILLE_ROUTE" ADD CONSTRAINT "FEUILLE_ROUTE_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FICHE_LOI
--------------------------------------------------------

  ALTER TABLE "FICHE_LOI" ADD CONSTRAINT "FICHE_LOI_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FICHE_PRESENTATION_DR
--------------------------------------------------------

  ALTER TABLE "FICHE_PRESENTATION_DR" ADD CONSTRAINT "FICH_A99D7296_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FICHE_PRESENTATION_IE
--------------------------------------------------------

  ALTER TABLE "FICHE_PRESENTATION_IE" ADD CONSTRAINT "FICH_B8C1A0DB_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FICHE_PRESENTATION_OEP
--------------------------------------------------------

  ALTER TABLE "FICHE_PRESENTATION_OEP" ADD CONSTRAINT "FICH_E4718D3D_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FICHE_PRESENTATION_341
--------------------------------------------------------

  ALTER TABLE "FICHE_PRESENTATION_341" ADD CONSTRAINT "FICH_7E47EE23_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FILE
--------------------------------------------------------

  ALTER TABLE "FILE" ADD CONSTRAINT "FILE_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FILE_SOLON_EPG
--------------------------------------------------------

  ALTER TABLE "FILE_SOLON_EPG" ADD CONSTRAINT "FILE_SOLON_EPG_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FILE_SOLON_MGPP
--------------------------------------------------------

  ALTER TABLE "FILE_SOLON_MGPP" ADD CONSTRAINT "FILE_9891333B_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FOND_DOSSIER_FOLDER_SOLON_EPG
--------------------------------------------------------

  ALTER TABLE "FOND_DOSSIER_FOLDER_SOLON_EPG" ADD CONSTRAINT "FOND_1B1A9CBA_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FP341_COAUTEUR
--------------------------------------------------------

  ALTER TABLE "FP341_COAUTEUR" ADD CONSTRAINT "FP341_COAUTEUR_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FSD_DC_COVERAGE
--------------------------------------------------------

  ALTER TABLE "FSD_DC_COVERAGE" ADD CONSTRAINT "FSD__A5BA3E76_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FSD_DC_CREATOR
--------------------------------------------------------

  ALTER TABLE "FSD_DC_CREATOR" ADD CONSTRAINT "FSD_DC_CREATOR_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FSD_DC_NATURE
--------------------------------------------------------

  ALTER TABLE "FSD_DC_NATURE" ADD CONSTRAINT "FSD_DC_NATURE_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FSD_DC_SUBJECTS
--------------------------------------------------------

  ALTER TABLE "FSD_DC_SUBJECTS" ADD CONSTRAINT "FSD__56431C6C_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FSD_ECM_PATH
--------------------------------------------------------

  ALTER TABLE "FSD_ECM_PATH" ADD CONSTRAINT "FSD_ECM_PATH_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table FULLTEXT
--------------------------------------------------------

  ALTER TABLE "FULLTEXT" ADD CONSTRAINT "FULLTEXT_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table HIERARCHY
--------------------------------------------------------

  ALTER TABLE "HIERARCHY" ADD CONSTRAINT "HIER_7A50B200_FK" FOREIGN KEY ("PARENTID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table HIERARCHY_READ_ACL
--------------------------------------------------------

  ALTER TABLE "HIERARCHY_READ_ACL" ADD CONSTRAINT "HIERARCHY_READ_ACL_ID_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table INDEXATION_MOT_CLE_SOLON_EPG
--------------------------------------------------------

  ALTER TABLE "INDEXATION_MOT_CLE_SOLON_EPG" ADD CONSTRAINT "INDE_B30AC540_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table INDEXATION_RUBRIQUE_SOLON_EPG
--------------------------------------------------------

  ALTER TABLE "INDEXATION_RUBRIQUE_SOLON_EPG" ADD CONSTRAINT "INDE_B6D8DB17_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table INFO_COMMENTS
--------------------------------------------------------

  ALTER TABLE "INFO_COMMENTS" ADD CONSTRAINT "INFO_COMMENTS_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table INFOEPREUVE
--------------------------------------------------------

  ALTER TABLE "INFOEPREUVE" ADD CONSTRAINT "INFOEPREUVE_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table INFOHISTORIQUEAMPLIATION
--------------------------------------------------------

  ALTER TABLE "INFOHISTORIQUEAMPLIATION" ADD CONSTRAINT "INFO_EBB0E14F_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table INFONUMEROLISTESIGNATURE
--------------------------------------------------------

  ALTER TABLE "INFONUMEROLISTESIGNATURE" ADD CONSTRAINT "INFO_3434DD4E_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table INFO_UTILISATEUR_CONNECTION
--------------------------------------------------------

  ALTER TABLE "INFO_UTILISATEUR_CONNECTION" ADD CONSTRAINT "INFO_B4B11272_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ITEM#ANONYMOUSTYPE
--------------------------------------------------------

  ALTER TABLE "ITEM#ANONYMOUSTYPE" ADD CONSTRAINT "ITEM_BE41EC81_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table JETON_DOC
--------------------------------------------------------

  ALTER TABLE "JETON_DOC" ADD CONSTRAINT "JETON_DOC_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table JETON_MAITRE
--------------------------------------------------------

  ALTER TABLE "JETON_MAITRE" ADD CONSTRAINT "JETON_MAITRE_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table LOCK_JETON_MAITRE
--------------------------------------------------------

  ALTER TABLE "LOCK_JETON_MAITRE" ADD CONSTRAINT "LJM_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
	  
  ALTER TABLE "LOCK_JETON_MAITRE" ADD CONSTRAINT "LJM_ID_JETON_MAITRE_FK" FOREIGN KEY ("ID_JETON_MAITRE")
	  REFERENCES "JETON_MAITRE" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table LIGNEPROGRAMMATION
--------------------------------------------------------

  ALTER TABLE "LIGNEPROGRAMMATION" ADD CONSTRAINT "LIGN_B3C8DF50_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table LIGNEPROGRAMMATIONHAB
--------------------------------------------------------

  ALTER TABLE "LIGNEPROGRAMMATIONHAB" ADD CONSTRAINT "LIGN_021E1B4F_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table LIS_IDSDOSSIER
--------------------------------------------------------

  ALTER TABLE "LIS_IDSDOSSIER" ADD CONSTRAINT "LIS_IDSDOSSIER_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table LISTE_TRAITEMENT_PAPI_144E75F6
--------------------------------------------------------

  ALTER TABLE "LISTE_TRAITEMENT_PAPI_144E75F6" ADD CONSTRAINT "LIST_2E65B72C_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MAIL
--------------------------------------------------------

  ALTER TABLE "MAIL" ADD CONSTRAINT "MAIL_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MAILBOX
--------------------------------------------------------

  ALTER TABLE "MAILBOX" ADD CONSTRAINT "MAILBOX_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MAILBOX_ID
--------------------------------------------------------

  ALTER TABLE "MAILBOX_ID" ADD CONSTRAINT "MAILBOX_ID_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MAILBOX_SOLON_EPG
--------------------------------------------------------

  ALTER TABLE "MAILBOX_SOLON_EPG" ADD CONSTRAINT "MAIL_D7A70098_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MAIL_CC_RECIPIENTS
--------------------------------------------------------

  ALTER TABLE "MAIL_CC_RECIPIENTS" ADD CONSTRAINT "MAIL_0700D9E5_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MAIL_RECIPIENTS
--------------------------------------------------------

  ALTER TABLE "MAIL_RECIPIENTS" ADD CONSTRAINT "MAIL_926B433B_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MFDDR_FORMATAUTORISE
--------------------------------------------------------

  ALTER TABLE "MFDDR_FORMATAUTORISE" ADD CONSTRAINT "MFDD_66879A97_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MINISTEREIDS
--------------------------------------------------------

  ALTER TABLE "MINISTEREIDS" ADD CONSTRAINT "MINISTEREIDS_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MISC
--------------------------------------------------------

  ALTER TABLE "MISC" ADD CONSTRAINT "MISC_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MLBX_FAVORITES
--------------------------------------------------------

  ALTER TABLE "MLBX_FAVORITES" ADD CONSTRAINT "MLBX_FAVORITES_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MLBX_GROUPS
--------------------------------------------------------

  ALTER TABLE "MLBX_GROUPS" ADD CONSTRAINT "MLBX_GROUPS_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MLBX_NOTIFIED_USERS
--------------------------------------------------------

  ALTER TABLE "MLBX_NOTIFIED_USERS" ADD CONSTRAINT "MLBX_9887A51E_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MLBX_PROFILES
--------------------------------------------------------

  ALTER TABLE "MLBX_PROFILES" ADD CONSTRAINT "MLBX_PROFILES_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MLBX_USERS
--------------------------------------------------------

  ALTER TABLE "MLBX_USERS" ADD CONSTRAINT "MLBX_USERS_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MODELE_FOND_DOSSIER_SOLON_EPG
--------------------------------------------------------

  ALTER TABLE "MODELE_FOND_DOSSIER_SOLON_EPG" ADD CONSTRAINT "MODE_E59416C3_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MODELE_PARAPHEUR_SOLON_EPG
--------------------------------------------------------

  ALTER TABLE "MODELE_PARAPHEUR_SOLON_EPG" ADD CONSTRAINT "MODE_29948B25_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MOTS_CLES
--------------------------------------------------------

  ALTER TABLE "MOTS_CLES" ADD CONSTRAINT "MOTS_CLES_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table NAVETTE
--------------------------------------------------------

  ALTER TABLE "NAVETTE" ADD CONSTRAINT "NAVETTE_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table NAVIGATIONSETTINGS
--------------------------------------------------------

  ALTER TABLE "NAVIGATIONSETTINGS" ADD CONSTRAINT "NAVI_9BCB26B1_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table NOTE
--------------------------------------------------------

  ALTER TABLE "NOTE" ADD CONSTRAINT "NOTE_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table NXP_LOGS_MAPEXTINFOS
--------------------------------------------------------

  ALTER TABLE "NXP_LOGS_MAPEXTINFOS" ADD CONSTRAINT "FKF96F609C4EA9779" FOREIGN KEY ("INFO_FK")
	  REFERENCES "NXP_LOGS_EXTINFO" ("LOG_EXTINFO_ID") ENABLE;
 
  ALTER TABLE "NXP_LOGS_MAPEXTINFOS" ADD CONSTRAINT "FKF96F609E7AC49AA" FOREIGN KEY ("LOG_FK")
	  REFERENCES "NXP_LOGS" ("LOG_ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PA_METADISPOCORBEILLE
--------------------------------------------------------

  ALTER TABLE "PA_METADISPOCORBEILLE" ADD CONSTRAINT "PA_M_754D9FE5_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PARAMETER
--------------------------------------------------------

  ALTER TABLE "PARAMETER" ADD CONSTRAINT "PARAMETER_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PARAMETRAGE_APPLICATION
--------------------------------------------------------

  ALTER TABLE "PARAMETRAGE_APPLICATION" ADD CONSTRAINT "PARA_F89EE972_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PARAMETRE
--------------------------------------------------------

  ALTER TABLE "PARAMETRE" ADD CONSTRAINT "PARAMETRE_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PARAPHEUR_FOLDER_SOLON_EPG
--------------------------------------------------------

  ALTER TABLE "PARAPHEUR_FOLDER_SOLON_EPG" ADD CONSTRAINT "PARA_A8CF4306_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PARTICIPANTLIST
--------------------------------------------------------

  ALTER TABLE "PARTICIPANTLIST" ADD CONSTRAINT "PART_E4211619_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PARUTIONBO
--------------------------------------------------------

  ALTER TABLE "PARUTIONBO" ADD CONSTRAINT "PARUTIONBO_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PF_FORMATAUTORISE
--------------------------------------------------------

  ALTER TABLE "PF_FORMATAUTORISE" ADD CONSTRAINT "PF_F_6F210720_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PROFIL_UTILISATEUR_SOLON_EPG
--------------------------------------------------------

  ALTER TABLE "PROFIL_UTILISATEUR_SOLON_EPG" ADD CONSTRAINT "PROF_5548CB8D_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PROTOCOL
--------------------------------------------------------

  ALTER TABLE "PROTOCOL" ADD CONSTRAINT "PROTOCOL_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PROXIES
--------------------------------------------------------

  ALTER TABLE "PROXIES" ADD CONSTRAINT "PROXIES_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
 
  ALTER TABLE "PROXIES" ADD CONSTRAINT "PROXIES_TARGETID_HIERARCHY_FK" FOREIGN KEY ("TARGETID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PUBLISH_SECTIONS
--------------------------------------------------------

  ALTER TABLE "PUBLISH_SECTIONS" ADD CONSTRAINT "PUBL_D888C7AF_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PUSR_IDCOLONNEESPACET_934A8665
--------------------------------------------------------

  ALTER TABLE "PUSR_IDCOLONNEESPACET_934A8665" ADD CONSTRAINT "PUSR_CA0885DB_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PUSR_IDCOLONNEINSTANC_3FED9D1F
--------------------------------------------------------

  ALTER TABLE "PUSR_IDCOLONNEINSTANC_3FED9D1F" ADD CONSTRAINT "PUSR_742D4FA0_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table PUSR_NOTIFICATIONTYPEACTES
--------------------------------------------------------

  ALTER TABLE "PUSR_NOTIFICATIONTYPEACTES" ADD CONSTRAINT "PUSR_3E05611F_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table QUERYNAV
--------------------------------------------------------

  ALTER TABLE "QUERYNAV" ADD CONSTRAINT "QUERYNAV_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table RELATEDTEXTRESOURCE
--------------------------------------------------------

  ALTER TABLE "RELATEDTEXTRESOURCE" ADD CONSTRAINT "RELA_835C3B64_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table RELATION
--------------------------------------------------------

  ALTER TABLE "RELATION" ADD CONSTRAINT "RELATION_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table RELATION_SEARCH
--------------------------------------------------------

  ALTER TABLE "RELATION_SEARCH" ADD CONSTRAINT "RELA_30BD7D76_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table REL_SRCH_ECM_PATH
--------------------------------------------------------

  ALTER TABLE "REL_SRCH_ECM_PATH" ADD CONSTRAINT "REL__E3A6C2F4_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table REPOSITORIES
--------------------------------------------------------

  ALTER TABLE "REPOSITORIES" ADD CONSTRAINT "REPOSITORIES_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table REPRESENTANT_OEP
--------------------------------------------------------

  ALTER TABLE "REPRESENTANT_OEP" ADD CONSTRAINT "REPR_477EC373_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table REQUETE_DOSSIER
--------------------------------------------------------

  ALTER TABLE "REQUETE_DOSSIER" ADD CONSTRAINT "REQU_B8C8BC0D_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table REQUETE_DOSSIER_SIMPL_A3A85FF2
--------------------------------------------------------

  ALTER TABLE "REQUETE_DOSSIER_SIMPL_A3A85FF2" ADD CONSTRAINT "REQU_70025C9A_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table REQUETE_DOSSIER_SIMPL_C483C4B2
--------------------------------------------------------

  ALTER TABLE "REQUETE_DOSSIER_SIMPL_C483C4B2" ADD CONSTRAINT "REQU_658D8138_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table REQUETE_DOSSIER_SIMPL_7B2FB691
--------------------------------------------------------

  ALTER TABLE "REQUETE_DOSSIER_SIMPL_7B2FB691" ADD CONSTRAINT "REQU_D46D41B8_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table REQUETE_DOSSIER_SIMPL_743D2E6F
--------------------------------------------------------

  ALTER TABLE "REQUETE_DOSSIER_SIMPL_743D2E6F" ADD CONSTRAINT "REQU_0286816D_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table RESCON_DOSSIERSIDS
--------------------------------------------------------

  ALTER TABLE "RESCON_DOSSIERSIDS" ADD CONSTRAINT "RESC_7816223C_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table RESCON_FDRSIDS
--------------------------------------------------------

  ALTER TABLE "RESCON_FDRSIDS" ADD CONSTRAINT "RESCON_FDRSIDS_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table RESCON_USERSNAMES
--------------------------------------------------------

  ALTER TABLE "RESCON_USERSNAMES" ADD CONSTRAINT "RESC_100DF0CE_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table RETOUR_DILA
--------------------------------------------------------

  ALTER TABLE "RETOUR_DILA" ADD CONSTRAINT "RETOUR_DILA_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table ROUTING_TASK
--------------------------------------------------------

  ALTER TABLE "ROUTING_TASK" ADD CONSTRAINT "ROUTING_TASK_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table RQDCP_IDSTATUTSARCHIV_82ED246B
--------------------------------------------------------

  ALTER TABLE "RQDCP_IDSTATUTSARCHIV_82ED246B" ADD CONSTRAINT "RQDC_786770E0_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table SEARCH_COVERAGE
--------------------------------------------------------

  ALTER TABLE "SEARCH_COVERAGE" ADD CONSTRAINT "SEAR_34319F67_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table SEARCH_CURRENTLIFECYCLESTATES
--------------------------------------------------------

  ALTER TABLE "SEARCH_CURRENTLIFECYCLESTATES" ADD CONSTRAINT "SEAR_1B5AF6D2_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table SEARCH_NATURE
--------------------------------------------------------

  ALTER TABLE "SEARCH_NATURE" ADD CONSTRAINT "SEARCH_NATURE_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table SEARCH_SEARCHPATH
--------------------------------------------------------

  ALTER TABLE "SEARCH_SEARCHPATH" ADD CONSTRAINT "SEAR_8976FDB6_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table SEARCH_SUBJECTS
--------------------------------------------------------

  ALTER TABLE "SEARCH_SUBJECTS" ADD CONSTRAINT "SEAR_110E4D75_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table SEMAINE_PARLEMENTAIRE
--------------------------------------------------------

  ALTER TABLE "SEMAINE_PARLEMENTAIRE" ADD CONSTRAINT "SEMA_53A906C6_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table SMART_FOLDER
--------------------------------------------------------

  ALTER TABLE "SMART_FOLDER" ADD CONSTRAINT "SMART_FOLDER_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table SORTINFOTYPE
--------------------------------------------------------

  ALTER TABLE "SORTINFOTYPE" ADD CONSTRAINT "SORTINFOTYPE_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table STATUS
--------------------------------------------------------

  ALTER TABLE "STATUS" ADD CONSTRAINT "STATUS_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table STEP_FOLDER
--------------------------------------------------------

  ALTER TABLE "STEP_FOLDER" ADD CONSTRAINT "STEP_FOLDER_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TABDYN_DESTINATAIRESID
--------------------------------------------------------

  ALTER TABLE "TABDYN_DESTINATAIRESID" ADD CONSTRAINT "TABD_A6DA4A56_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TABDYN_USERSNAMES
--------------------------------------------------------

  ALTER TABLE "TABDYN_USERSNAMES" ADD CONSTRAINT "TABD_156114AB_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TABLEAU_DYNAMIQUE
--------------------------------------------------------

  ALTER TABLE "TABLEAU_DYNAMIQUE" ADD CONSTRAINT "TABL_2FDCB449_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TABLE_REFERENCE
--------------------------------------------------------

  ALTER TABLE "TABLE_REFERENCE" ADD CONSTRAINT "TABL_3AC6E87F_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TAG
--------------------------------------------------------

  ALTER TABLE "TAG" ADD CONSTRAINT "TAG_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBREF_CABINETPM
--------------------------------------------------------

  ALTER TABLE "TBREF_CABINETPM" ADD CONSTRAINT "TBRE_B268F3D2_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBREF_CHARGESMISSION
--------------------------------------------------------

  ALTER TABLE "TBREF_CHARGESMISSION" ADD CONSTRAINT "TBRE_0DE60FF7_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBREF_DIRECTIONPM
--------------------------------------------------------

  ALTER TABLE "TBREF_DIRECTIONPM" ADD CONSTRAINT "TBRE_5642BBE1_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBREF_SIGNATAIRES
--------------------------------------------------------

  ALTER TABLE "TBREF_SIGNATAIRES" ADD CONSTRAINT "TBRE_6047D8B9_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBREF_SIGNATURECPM
--------------------------------------------------------

  ALTER TABLE "TBREF_SIGNATURECPM" ADD CONSTRAINT "TBRE_C45D1FB8_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBREF_SIGNATURESGG
--------------------------------------------------------

  ALTER TABLE "TBREF_SIGNATURESGG" ADD CONSTRAINT "TBRE_B52F8393_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBREF_VECTEURSPUBLICATIONS
--------------------------------------------------------

  ALTER TABLE "TBREF_VECTEURSPUBLICATIONS" ADD CONSTRAINT "TBRE_1D918933_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TEXM_DECRETIDS
--------------------------------------------------------

  ALTER TABLE "TEXM_DECRETIDS" ADD CONSTRAINT "TEXM_DECRETIDS_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TEXM_DOSSIERSNOR
--------------------------------------------------------

  ALTER TABLE "TEXM_DOSSIERSNOR" ADD CONSTRAINT "TEXM_107CCAB8_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TEXM_HABILITATIONIDS
--------------------------------------------------------

  ALTER TABLE "TEXM_HABILITATIONIDS" ADD CONSTRAINT "TEXM_44F4231E_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TEXM_LOIRATIFICATIONIDS
--------------------------------------------------------

  ALTER TABLE "TEXM_LOIRATIFICATIONIDS" ADD CONSTRAINT "TEXM_CF898C8F_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TEXM_MESUREIDS
--------------------------------------------------------

  ALTER TABLE "TEXM_MESUREIDS" ADD CONSTRAINT "TEXM_MESUREIDS_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TEXM_MESURESAPPLICATIVESIDS
--------------------------------------------------------

  ALTER TABLE "TEXM_MESURESAPPLICATIVESIDS" ADD CONSTRAINT "TEXM_B2B50F76_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TEXM_ORDONNANCEIDS
--------------------------------------------------------

  ALTER TABLE "TEXM_ORDONNANCEIDS" ADD CONSTRAINT "TEXM_3410813C_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TEXM_TRANSPOSITIONIDS
--------------------------------------------------------

  ALTER TABLE "TEXM_TRANSPOSITIONIDS" ADD CONSTRAINT "TEXM_3F11443B_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TEXTE_MAITRE
--------------------------------------------------------

  ALTER TABLE "TEXTE_MAITRE" ADD CONSTRAINT "TEXTE_MAITRE_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TEXTE_SIGNALE
--------------------------------------------------------

  ALTER TABLE "TEXTE_SIGNALE" ADD CONSTRAINT "TEXTE_SIGNALE_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TP_AMPLIATIONDESTINATAIREMAILS
--------------------------------------------------------

  ALTER TABLE "TP_AMPLIATIONDESTINATAIREMAILS" ADD CONSTRAINT "TP_A_8488D0A4_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TRAITEMENT_PAPIER
--------------------------------------------------------

  ALTER TABLE "TRAITEMENT_PAPIER" ADD CONSTRAINT "TRAI_81B13EE8_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TRANPOSITION
--------------------------------------------------------

  ALTER TABLE "TRANPOSITION" ADD CONSTRAINT "TRANPOSITION_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TYPE_CONTACT
--------------------------------------------------------

  ALTER TABLE "TYPE_CONTACT" ADD CONSTRAINT "TYPE_CONTACT_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table UID
--------------------------------------------------------

  ALTER TABLE "UID" ADD CONSTRAINT "UID_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table UITYPESCONF_ALLOWEDTYPES
--------------------------------------------------------

  ALTER TABLE "UITYPESCONF_ALLOWEDTYPES" ADD CONSTRAINT "UITY_694B86C3_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table UITYPESCONF_DENIEDTYPES
--------------------------------------------------------

  ALTER TABLE "UITYPESCONF_DENIEDTYPES" ADD CONSTRAINT "UITY_6E2ADC3E_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table UI_TYPES_CONFIGURATION
--------------------------------------------------------

  ALTER TABLE "UI_TYPES_CONFIGURATION" ADD CONSTRAINT "UI_T_F6D8BC6A_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table USERSETTINGS
--------------------------------------------------------

  ALTER TABLE "USERSETTINGS" ADD CONSTRAINT "USERSETTINGS_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table VCARD
--------------------------------------------------------

  ALTER TABLE "VCARD" ADD CONSTRAINT "VCARD_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table VERSIONS
--------------------------------------------------------

  ALTER TABLE "VERSIONS" ADD CONSTRAINT "VERSIONS_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table WEBCONTAINER
--------------------------------------------------------

  ALTER TABLE "WEBCONTAINER" ADD CONSTRAINT "WEBCONTAINER_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table WSSPE
--------------------------------------------------------

  ALTER TABLE "WSSPE" ADD CONSTRAINT "WSSPE_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
	  REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE;
	  

	  
--------------------------------------------------------
--  DDL for Trigger NX_TRIG_ACLR_MODIFIED
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Function NX_ACCESS_ALLOWED
--------------------------------------------------------
  CREATE OR REPLACE FUNCTION "NX_ACCESS_ALLOWED" (id VARCHAR2, users NX_STRING_TABLE, permissions NX_STRING_TABLE)
RETURN NUMBER IS
  curid hierarchy.id%TYPE := id;
  newid hierarchy.id%TYPE;
  first BOOLEAN := TRUE;
BEGIN
  WHILE curid IS NOT NULL LOOP
    FOR r IN (SELECT * FROM acls WHERE acls.id = curid ORDER BY acls.pos) LOOP
      IF r.permission MEMBER OF permissions AND r.user MEMBER OF users THEN
        RETURN r."GRANT";
      END IF;
    END LOOP;
    SELECT parentid INTO newid FROM hierarchy WHERE hierarchy.id = curid;
    IF first AND newid IS NULL THEN
      SELECT versionableid INTO newid FROM versions WHERE versions.id = curid;
    END IF;
    first := FALSE;
    curid := newid;
  END LOOP;
  RETURN 0;
END;
/



  CREATE OR REPLACE FUNCTION "NX_GET_ANCESTORS" (id VARCHAR2)
RETURN NX_STRING_TABLE
IS
  curid hierarchy.id%TYPE := id;
  newid hierarchy.id%TYPE;
  ret NX_STRING_TABLE := NX_STRING_TABLE();
  first BOOLEAN := TRUE;
BEGIN
  WHILE curid IS NOT NULL LOOP
    BEGIN
      SELECT parentid INTO newid FROM hierarchy WHERE hierarchy.id = curid;
    EXCEPTION WHEN NO_DATA_FOUND THEN
      -- curid not in hierarchy at all
      newid := NULL;
    END;
    IF curid IS NOT NULL AND curid <> id THEN
      ret.EXTEND;
      ret(ret.COUNT) := curid;
    END IF;
    IF first AND newid IS NULL THEN
      BEGIN
        SELECT versionableid INTO newid FROM versions WHERE versions.id = curid;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN NULL;
      END;
    END IF;
    first := FALSE;
    curid := newid;
  END LOOP;
  RETURN ret;
END;
/



create or replace FUNCTION "NX_GET_READ_ACL"(id VARCHAR2)
RETURN VARCHAR2
-- Compute the merged read acl for a doc id
IS
  curid acls.id%TYPE := id;
  newid acls.id%TYPE;
  acl VARCHAR2(32767) := NULL;
  first BOOLEAN := TRUE;
  sep VARCHAR2(1) := '|';
BEGIN
  WHILE curid IS NOT NULL LOOP
    FOR r in (SELECT * FROM RLACL WHERE HIERARCHY_ID = curid) LOOP
      IF acl IS NOT NULL THEN
         acl := acl || sep;
      END IF;
      if r.ACL IS NOT NULL THEN
         acl := acl || r.acl;
      END IF;
    END LOOP;
    -- recurse into parent
    BEGIN
      SELECT parentid INTO newid FROM hierarchy WHERE hierarchy.id = curid;
    EXCEPTION WHEN NO_DATA_FOUND THEN
      -- curid not in hierarchy at all
      newid := NULL;
    END;
    IF first AND newid IS NULL THEN
      BEGIN
        SELECT versionableid INTO newid FROM versions WHERE versions.id = curid;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN NULL;
      END;
    END IF;
    first := FALSE;
    curid := newid;
  END LOOP;
  RETURN acl;
END;
/

 CREATE OR REPLACE FUNCTION "NX_HASH" (string VARCHAR2)
RETURN VARCHAR2
IS
BEGIN
  -- hash function 1 is MD4 (faster than 2 = MD5)
  RETURN DBMS_CRYPTO.HASH(UTL_I18N.STRING_TO_RAW(string, 'AL32UTF8'), 1);
END;
/

  CREATE OR REPLACE FUNCTION "SPLIT" (string VARCHAR2, sep VARCHAR2)
RETURN NX_STRING_ARRAY
-- splits a string, order matters
IS
  pos PLS_INTEGER := 1;
  len PLS_INTEGER := NVL(LENGTH(string), 0);
  i PLS_INTEGER;
  res NX_STRING_ARRAY := NX_STRING_ARRAY();
BEGIN
  WHILE pos <= len LOOP
    i := INSTR(string, sep, pos);
    IF i = 0 THEN i := len + 1; END IF;
    res.EXTEND;
    res(res.COUNT) := SUBSTR(string, pos, i - pos);
    pos := i + 1;
  END LOOP;
  RETURN res;
END;
/

  CREATE OR REPLACE FUNCTION "NX_HASH_USERS" (users NX_STRING_TABLE)
RETURN VARCHAR2
IS
  s VARCHAR2(32767) := NULL;
  sep VARCHAR2(1) := '|';
BEGIN
  -- TODO use canonical (sorted) order for users
  FOR i IN users.FIRST .. users.LAST LOOP
    IF s IS NOT NULL THEN
      s := s || sep;
    END IF;
    s := s || users(i);
  END LOOP;
  RETURN nx_hash(s);
END;
/

  CREATE OR REPLACE FUNCTION "NX_LIST_READ_ACLS_FOR" (users NX_STRING_TABLE)
RETURN NX_STRING_TABLE
-- List matching read acl ids for a list of user/groups
IS
  negusers NX_STRING_TABLE := NX_STRING_TABLE();
  aclusers NX_STRING_ARRAY;
  acluser VARCHAR2(32767);
  aclids NX_STRING_TABLE := NX_STRING_TABLE();
  sep VARCHAR2(1) := '|';
BEGIN
  -- Build a black list with negative users
  FOR n IN users.FIRST .. users.LAST LOOP
    negusers.EXTEND;
    negusers(n) := '-' || users(n);
  END LOOP;
  -- find match
  FOR r IN (SELECT acl_id, acl FROM aclr) LOOP
    aclusers := split(r.acl, sep);
    FOR i IN aclusers.FIRST .. aclusers.LAST LOOP
      acluser := aclusers(i);
      IF acluser MEMBER OF users THEN
        -- grant
        aclids.EXTEND;
        aclids(aclids.COUNT) := r.acl_id;
        GOTO next_acl;
      END IF;
      IF acluser MEMBER OF negusers THEN
        -- deny
        GOTO next_acl;
      END IF;
    END LOOP;
    <<next_acl>> NULL;
  END LOOP;
  RETURN aclids;
END;
/


  CREATE OR REPLACE FUNCTION "NX_GET_READ_ACLS_FOR" (users NX_STRING_TABLE)
RETURN NX_STRING_TABLE
-- List read acl ids for a list of user/groups, using the cache
IS
  PRAGMA AUTONOMOUS_TRANSACTION; -- needed for insert, ok since what we fill is a cache
  user_md5 VARCHAR2(34) := nx_hash_users(users);
  in_cache NUMBER;
  aclids NX_STRING_TABLE;
BEGIN
  SELECT acl_id BULK COLLECT INTO aclids FROM aclr_user_map WHERE user_id = user_md5;
  SELECT COUNT(*) INTO in_cache FROM TABLE(aclids);
  IF in_cache = 0 THEN
    -- dbms_output.put_line('no cache');
    aclids := nx_list_read_acls_for(users);
    -- below INSERT needs the PRAGMA AUTONOMOUS_TRANSACTION
    INSERT INTO aclr_user VALUES (user_md5, users);
    COMMIT;
    INSERT INTO aclr_user_map SELECT user_md5, COLUMN_VALUE FROM TABLE(aclids);
    COMMIT;
  END IF;
  RETURN aclids;
END;
/



  CREATE OR REPLACE FUNCTION "NX_GET_READ_ACL_ID" (id VARCHAR2)
RETURN VARCHAR2
IS
BEGIN
  RETURN nx_hash(nx_get_read_acl(id));
END;
/

   CREATE OR REPLACE FUNCTION "NX_IN_TREE" (id VARCHAR2, baseid VARCHAR2)
RETURN NUMBER IS
  curid hierarchy.id%TYPE := id;
BEGIN
  IF baseid IS NULL OR id IS NULL OR baseid = id THEN
    RETURN 0;
  END IF;
  LOOP
    SELECT parentid INTO curid FROM hierarchy WHERE hierarchy.id = curid;
    IF curid IS NULL THEN
      RETURN 0;
    ELSIF curid = baseid THEN
      RETURN 1;
    END IF;
  END LOOP;
END;
/



	  
-- Changed by NXP-8880
CREATE OR REPLACE PROCEDURE NX_CLUSTER_INVAL(i VARCHAR2, f VARCHAR2, k INTEGER, nid VARCHAR)
IS
BEGIN
  FOR c IN (SELECT nodeid FROM cluster_nodes WHERE nodeid <> nid) LOOP
    INSERT INTO cluster_invals (nodeid, id, fragments, kind) VALUES (c.nodeid, i, f, k);
  END LOOP;
END;
/



  CREATE OR REPLACE PROCEDURE "NX_INIT_ANCESTORS"
IS
BEGIN
  EXECUTE IMMEDIATE 'TRUNCATE TABLE ancestors';
  INSERT INTO ancestors
    SELECT id, nx_get_ancestors(id)
    FROM (SELECT id FROM hierarchy WHERE isproperty=0);
END;
/



  CREATE OR REPLACE PROCEDURE "NX_REBUILD_READ_ACLS"
-- Rebuild the read acls tables
IS
BEGIN
  EXECUTE IMMEDIATE 'TRUNCATE TABLE aclr';
  EXECUTE IMMEDIATE 'TRUNCATE TABLE aclr_user';
  EXECUTE IMMEDIATE 'TRUNCATE TABLE aclr_user_map';
  EXECUTE IMMEDIATE 'TRUNCATE TABLE hierarchy_read_acl';
  EXECUTE IMMEDIATE 'TRUNCATE TABLE aclr_modified';
  INSERT INTO hierarchy_read_acl
    SELECT id, nx_get_read_acl_id(id)
      FROM (SELECT id FROM hierarchy WHERE isproperty = 0);
END;
/



-- modif NX_UPDATE_READ_ACLS : mise à jour rlacl, utilisation de rlacl dans le recalcul des
-- acls sur les enfants 
create or replace PROCEDURE "NX_UPDATE_READ_ACLS"
-- Rebuild only necessary read acls
IS
  update_count PLS_INTEGER;
BEGIN
 MERGE INTO RLACL t
    USING (SELECT id, LISTAGG(CASE WHEN a."GRANT" = 0 THEN '-' ELSE '' END || a."USER", '|') WITHIN GROUP (ORDER BY pos) AS l
				FROM acls a, ACLR_PERMISSION p WHERE a.permission = p.permission 
        and a.id in (SELECT DISTINCT(m.hierarchy_id) id FROM aclr_modified m)
        group by id) s
    ON (t.hierarchy_id = s.id)
	WHEN MATCHED THEN UPDATE SET acl = s.l
    WHEN NOT MATCHED THEN INSERT (hierarchy_id, acl) VALUES (s.id, s.l);
  --
  -- 1/ New documents, no new ACL
  MERGE INTO hierarchy_read_acl t
    USING (SELECT DISTINCT(m.hierarchy_id) id
            FROM aclr_modified m
            JOIN hierarchy h ON m.hierarchy_id = h.id
            WHERE m.is_new = 1) s
    ON (t.id = s.id)
    WHEN NOT MATCHED THEN
      INSERT (id, acl_id) VALUES (s.id, nx_get_read_acl_id(s.id));
  DELETE FROM aclr_modified WHERE is_new = 1;
  --
  -- 2/ Compute the new read ACLS for updated documents
  MERGE INTO HIERARCHY_READ_ACL t
    USING (
      WITH recursiveRA
     (hid, hacl) AS
       (SELECT parent.id, nx_get_read_acl(id) as hacl
        FROM hierarchy parent
        WHERE parent.id in (SELECT distinct(HIERARCHY_ID) FROM ACLR_MODIFIED)
        
        UNION ALL
        
        SELECT child.id, case when a.acl IS null THEN parent.hacl ELSE a.ACL || '|' || parent.HACL END
        FROM recursiveRA parent, hierarchy child
        LEFT OUTER JOIN RLACL a ON a.HIERARCHY_ID = child.id
        WHERE child.parentid = parent.hid
        )
      SELECT hid, hacl
      FROM recursiveRA
    ) s
    ON (t.id = s.hid)
    WHEN MATCHED THEN 
      UPDATE SET acl_id = nx_hash(s.hacl);
    
  DELETE FROM aclr_modified;
END;
/



  CREATE OR REPLACE PROCEDURE "NX_VACUUM_READ_ACLS"
-- Remove unused read acls entries
IS
BEGIN
  -- nx_vacuum_read_acls vacuuming
  DELETE FROM aclr WHERE acl_id IN (SELECT r.acl_id FROM aclr r
    JOIN hierarchy_read_acl h ON r.acl_id=h.acl_id
    WHERE h.acl_id IS NULL);
  EXECUTE IMMEDIATE 'TRUNCATE TABLE aclr_user';
  EXECUTE IMMEDIATE 'TRUNCATE TABLE aclr_user_map';
  EXECUTE IMMEDIATE 'TRUNCATE TABLE aclr_modified';
END;
/
---------------------------------------------------------
--					 soft delete						-
---------------------------------------------------------
CREATE OR REPLACE PROCEDURE NX_DELETE(ids NX_STRING_TABLE, nowTimeIn TIMESTAMP)
-- Marks the given ids as deleted at the given time (null means now)
-- Simulates foreign keys except for the parent-child one which is done in Java
IS
  nowTime TIMESTAMP := nowTimeIn;
BEGIN
  IF nowTime IS NULL THEN
    nowTime := CURRENT_TIMESTAMP;
  END IF;
  UPDATE hierarchy
    SET isdeleted = 1, deletedtime = nowTime
    -- don't use MEMBER OF, it causes a full table scan
    WHERE id IN (SELECT COLUMN_VALUE FROM TABLE(ids));
  -- do hard delete for foreign key proxies.targetid
  DELETE FROM proxies
    -- don't use MEMBER OF, it causes a full table scan
    WHERE proxies.targetid IN (SELECT COLUMN_VALUE FROM TABLE(ids));
END;
/


CREATE OR REPLACE PROCEDURE NX_DELETE_PURGE(maximumIn INTEGER, beforeTimeIn TIMESTAMP, total OUT INTEGER)
-- Does hard delete on soft-deleted rows earlier than beforeTime (null means all).
-- A maximum number of rows to delete can be provided (null means no limit).
-- Returns the number of rows actually deleted.
-- Rows are deleted leaves first.
IS
  beforeTime TIMESTAMP := beforeTimeIn;
  maximum INTEGER := maximumIn;
  ndel INTEGER;
BEGIN
  IF beforeTime IS NULL THEN
    beforeTime := CURRENT_TIMESTAMP + INTERVAL '1' DAY;
  END IF;
  IF maximum = 0 THEN
    maximum := NULL;
  END IF;
  total := 0;
  LOOP
    -- delete some leaves in the tree of soft-deleted documents
    IF maximum IS NULL THEN
      DELETE FROM hierarchy
        WHERE isdeleted = 1 AND deletedtime < beforeTime
        AND id NOT IN (
          -- not leaves: deleted nodes that have deleted children
          SELECT DISTINCT hpar.id FROM hierarchy hpar
            JOIN hierarchy h ON h.parentid = hpar.id
            WHERE hpar.isdeleted = 1 AND h.isdeleted = 1);
    ELSE
      DELETE FROM hierarchy WHERE id IN (
        SELECT id FROM hierarchy
        WHERE isdeleted = 1 AND deletedtime < beforeTime
        AND id NOT IN (
          -- not leaves: deleted nodes that have deleted children
          SELECT DISTINCT hpar.id FROM hierarchy hpar
            JOIN hierarchy h ON h.parentid = hpar.id
            WHERE hpar.isdeleted = 1 AND h.isdeleted = 1)
        AND ROWNUM <= maximum);
    END IF;
    ndel := SQL%ROWCOUNT;
    EXIT WHEN ndel = 0;
    total := total + ndel;
    EXIT WHEN total >= maximum;     -- no exit when maximum = NULL
  END LOOP;
END; 
/

---------------------------------------------------------
--			procédure sequenceur oracle					-
---------------------------------------------------------

CREATE OR REPLACE PROCEDURE CREATE_NEW_SEQUENCE(seqName VARCHAR2)
IS
  request VARCHAR2(2000);
BEGIN
    request := 'CREATE SEQUENCE '||seqName||' START WITH 0 MINVALUE 0 MAXVALUE 99999';
    EXECUTE IMMEDIATE request;
END;
/

---------------------------------------------------------TRIGGERS-----------------------------------

  CREATE OR REPLACE TRIGGER "NX_TRIG_ACLR_MODIFIED"
  AFTER INSERT ON aclr
  FOR EACH ROW
   WHEN (NEW.acl_id IS NOT NULL) DECLARE
  negusers NX_STRING_TABLE;
  acl NX_STRING_ARRAY;
  ace VARCHAR(4000);
  sep VARCHAR2(1) := '|';
BEGIN
  FOR r IN (SELECT * FROM ACLR_USER) LOOP
    -- Build a black list with negative users
    negusers := NX_STRING_TABLE();
    FOR i IN r.users.FIRST .. r.users.LAST LOOP
      negusers.EXTEND;
      negusers(i) := '-' || r.users(i);
    END LOOP;
    acl := split(:NEW.acl, sep);
    FOR i IN acl.FIRST .. acl.LAST LOOP
      ace := acl(i);
      IF ace MEMBER OF r.users THEN
         -- GRANTED
         INSERT INTO ACLR_USER_MAP SELECT r.user_id, :NEW.acl_id FROM DUAL
         WHERE NOT EXISTS (SELECT 1 FROM ACLR_USER_MAP WHERE user_id=r.user_id AND acl_id = :NEW.acl_id);
         GOTO next_user;
      END IF;
      IF ace MEMBER OF negusers THEN
         -- DENIED
         GOTO next_user;
      END IF;
    END LOOP;
    <<next_user>> NULL;
  END LOOP;
END;
/
ALTER TRIGGER "NX_TRIG_ACLR_MODIFIED" ENABLE;



  CREATE OR REPLACE TRIGGER "NX_TRIG_ACLS_MODIFIED"
  AFTER INSERT OR UPDATE OR DELETE ON acls
  FOR EACH ROW
-- Trigger to log change in the acls table
DECLARE
  doc_id acls.id%TYPE := CASE WHEN DELETING THEN :OLD.id ELSE :NEW.id END;
BEGIN
  INSERT INTO aclr_modified (hierarchy_id, is_new) VALUES (doc_id, 0);
END;
/
ALTER TRIGGER "NX_TRIG_ACLS_MODIFIED" ENABLE;



  CREATE OR REPLACE TRIGGER "NX_TRIG_ANCESTORS_INSERT"
  AFTER INSERT ON hierarchy
  FOR EACH ROW
   WHEN (NEW.isproperty = 0 AND NEW.parentid IS NOT NULL) BEGIN
  INSERT INTO ANCESTORS VALUES(:NEW.id, NULL);
END;
/
ALTER TRIGGER "NX_TRIG_ANCESTORS_INSERT" DISABLE;



  CREATE OR REPLACE TRIGGER "NX_TRIG_ANCESTORS_PROCESS"
  AFTER INSERT OR UPDATE ON hierarchy
  -- statement level is required to be able to read hierarchy table with updated values
BEGIN
  UPDATE ancestors SET ancestors=nx_get_ancestors(hierarchy_id)
    WHERE ancestors IS NULL;
END;
/
ALTER TRIGGER "NX_TRIG_ANCESTORS_PROCESS" DISABLE;



  CREATE OR REPLACE TRIGGER "NX_TRIG_ANCESTORS_UPDATE"
  AFTER UPDATE ON hierarchy
  FOR EACH ROW   WHEN (NEW.isproperty = 0 AND NEW.parentid <> OLD.parentid) BEGIN
  UPDATE ancestors SET ancestors = NULL
    WHERE hierarchy_id IN (SELECT hierarchy_id FROM ancestors a
                           WHERE :NEW.id MEMBER OF a.ancestors OR  hierarchy_id = :NEW.id);
END;
/
ALTER TRIGGER "NX_TRIG_ANCESTORS_UPDATE" DISABLE;



  CREATE OR REPLACE TRIGGER "NX_TRIG_FT_UPDATE"
  BEFORE INSERT OR UPDATE ON "FULLTEXT"
  FOR EACH ROW
BEGIN
    :NEW."FULLTEXT" := :NEW."SIMPLETEXT" || :NEW."BINARYTEXT";
  :NEW."FULLTEXT_NOR" := :NEW."SIMPLETEXT_NOR" || :NEW."BINARYTEXT_NOR";
END;
/
ALTER TRIGGER "NX_TRIG_FT_UPDATE" ENABLE;



  CREATE OR REPLACE TRIGGER "NX_TRIG_HIERARCHY_INSERT"
  AFTER INSERT ON hierarchy
  FOR EACH ROW
   WHEN (NEW.isproperty = 0) BEGIN
  INSERT INTO aclr_modified (hierarchy_id, is_new) VALUES (:NEW.id, 1);
END;
/
ALTER TRIGGER "NX_TRIG_HIERARCHY_INSERT" ENABLE;



  CREATE OR REPLACE TRIGGER "NX_TRIG_HIERARCHY_UPDATE"
  AFTER UPDATE ON hierarchy
  FOR EACH ROW
   WHEN (NEW.isproperty = 0 AND NEW.parentid <> OLD.parentid) BEGIN
  INSERT INTO aclr_modified (hierarchy_id, is_new) VALUES (:NEW.id, 0);
END;
/
ALTER TRIGGER "NX_TRIG_HIERARCHY_UPDATE" ENABLE;



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
ALTER TRIGGER "NX_TRIG_HIER_READ_ACL_MOD" ENABLE;

/

spool off

