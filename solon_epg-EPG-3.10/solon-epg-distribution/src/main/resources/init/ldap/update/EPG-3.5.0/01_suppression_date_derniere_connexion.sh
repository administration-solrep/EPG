#!/bin/bash

echo "Suppression des attributs dateLastConnection"

#for i in `ldapsearch -x -H localhost:389 -D ou=people,ou=SolonEpg,dc=dila,dc=fr -w changeme | grep uid=| cut -d : -f 2 | sed 's/^\ //g'`
for i in `ldapsearch -x -h localhost:389 -D "cn=ldapadmin,dc=dila,dc=fr" -w "changeme" -b "ou=people,ou=SolonEpg,dc=dila,dc=fr" -s sub "(objectclass=personne)" | grep uid=| cut -d : -f 2 | sed 's/^\ //g'`
do

ldapmodify -x -h localhost:389 -D "cn=ldapadmin,dc=dila,dc=fr" -w "changeme" << EOF
dn: $i
changetype: modify
delete: dateLastConnection


EOF

done
