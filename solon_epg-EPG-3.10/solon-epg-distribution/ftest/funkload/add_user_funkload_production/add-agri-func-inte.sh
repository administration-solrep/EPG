#!/bin/sh

ADMIN_DN="cn=ldapadmin,dc=dila,dc=fr"
ADMIN_PW=changeme
HOSTNAME=idlv-solon-intel.lyon-dev2.local


ldapmodify -h ${HOSTNAME} -D "${ADMIN_DN}" -w ${ADMIN_PW} -x -f ./agri-mod-func.ldif
 
