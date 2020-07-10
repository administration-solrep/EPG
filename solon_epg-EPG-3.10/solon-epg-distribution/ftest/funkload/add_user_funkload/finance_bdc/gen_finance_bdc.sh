#!/bin/sh

USER=finance_bdc
POSTE=50001764

# param <idx>
add_user() {
	local idx=$1
	cat <<EOF
dn: uid=${USER}${idx},ou=people,ou=SolonEpg,dc=dila,dc=fr
objectClass: top
objectClass: person
objectClass: organizationalPerson
objectClass: inetOrgPerson
objectClass: personne
cn:: IA==
dateDebut: 20101215230000Z
deleted: FALSE
occasional: FALSE
sn:: TWluaXN0w6hyZSBhZ3JpY3VsdHVyZQ==
temporary: FALSE
dateLastConnection: 20110921073641Z
mail: ${USER}@dila.fr
uid: ${USER}${idx}
userPassword:: e1NTSEF9Rkd0eXIxaHNyMzdEUk5iZXB3QmpEa0NBWG55bURJaUZuTEozaFE9P
 Q==

EOF

}

# param <idx>
mod_user(){
	local idx=$1
	cat <<EOF

dn: cn=$POSTE,ou=poste,ou=SolonEpg,dc=dila,dc=fr
changetype: modify
add: uniquemember
uniqueMember: uid=${USER}${idx},ou=people,ou=SolonEpg,dc=dila,dc=fr

dn: cn=Administrateur ministériel,ou=groups,ou=SolonEpg,dc=dila,dc=fr
changetype: modify
add: uniquemember
uniqueMember: uid=${USER}${idx},ou=people,ou=SolonEpg,dc=dila,dc=fr

EOF

}


OUTPUT_ADD=./${USER}-add.ldif
OUTPUT_MOD=./${USER}-mod.ldif
OUTPUT_PASSWD=./${USER}-passwd.txt
OUTPUT_SCRIPT=./add-${USER}.sh
OUTPUT_GROUP=./${USER}-groupe.txt

rm -f $OUTPUT_ADD $OUTPUT_PASSWD $OUTPUT_SCRIPT $OUTPUT_GROUP $OUTPUT_MOD

i=0
while [ $i -lt 200 ]; do
	idx=$(printf "%03d" $i)
	add_user $idx >> $OUTPUT_ADD
	mod_user $idx >> $OUTPUT_MOD
	echo "${USER}${idx}:agriculture_bdc" >> $OUTPUT_PASSWD
	echo -n "${USER}${idx}," >> $OUTPUT_GROUP	
	let i++ 
done

cat > $OUTPUT_SCRIPT <<EOF
#!/bin/sh

ADMIN_DN="cn=ldapadmin,dc=dila,dc=fr"
ADMIN_PW=changeme
#HOSTNAME=localhost
HOSTNAME=idlv-solon-intel.lyon-dev2.local

ldapadd -h \${HOSTNAME} -D "\${ADMIN_DN}" -w \${ADMIN_PW} -x -f $OUTPUT_ADD

ldapmodify -h \${HOSTNAME} -D "\${ADMIN_DN}" -w \${ADMIN_PW} -x -f $OUTPUT_MOD
 
EOF

