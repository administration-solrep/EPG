#!/bin/sh

ADMIN_DN="cn=ldapadmin,dc=dila,dc=fr"
ADMIN_PW=dila1234
HOSTNAME=p-solrep-ldap

ldapadd -h ${HOSTNAME} -D "${ADMIN_DN}" -w ${ADMIN_PW} -x -f ./agri-add.ldif

ldapmodify -h ${HOSTNAME} -D "${ADMIN_DN}" -w ${ADMIN_PW} -x -f ./agri-mod.ldif
 
