#!/bin/sh

ADMIN_DN="cn=ldapadmin,dc=dila,dc=fr"
ADMIN_PW=changeme
#HOSTNAME=localhost
HOSTNAME=idlv-solon-intel.lyon-dev2.local

ldapadd -h ${HOSTNAME} -D "${ADMIN_DN}" -w ${ADMIN_PW} -x -f ./finance_bdc-add.ldif

ldapmodify -h ${HOSTNAME} -D "${ADMIN_DN}" -w ${ADMIN_PW} -x -f ./finance_bdc-mod.ldif
 
