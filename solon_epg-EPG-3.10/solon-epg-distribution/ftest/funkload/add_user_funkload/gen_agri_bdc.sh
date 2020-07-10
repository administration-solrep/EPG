#!/bin/sh


# param <idx>
add_user() {
	local idx=$1
	cat <<EOF
dn: uid=bdc_${idx},ou=people,ou=SolonEpg,dc=dila,dc=fr
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
mail: bdc@dila.fr
uid: bdc${idx}
userPassword:: e1NTSEF9dUw3Vncvb0VhSXJtMnRJYmxmbjQ5dW1xbS9ldVdnNU9zLzhqQVE9P
 Q==

EOF

}

# param <idx>
mod_user(){
	local idx=$1
	cat <<EOF

dn: cn=Administrateur fonctionnel,ou=groups,ou=SolonEpg,dc=dila,dc=fr
changetype: modify
add: uniquemember
uniqueMember: uid=bdc_${idx},ou=people,ou=SolonEpg,dc=dila,dc=fr

EOF

}



mod_poste(){
         local idx=$1
         cat <<EOF

dn: cn=50001202,ou=poste,ou=SolonEpg,dc=dila,dc=fr
changetype: modify
add: uniqueMember
uniqueMember: uid=bdc_${idx},ou=people,ou=SolonEpg,dc=dila,dc=fr
 
EOF
  
}



# param <idx>
mod_pass_user(){
	local idx=$1
	cat <<EOF

dn: cn=bdc_${idx},ou=groups,ou=SolonEpg,dc=dila,dc=fr
changetype: modify
replace: password 
userPassword:: e1NTSEF9dUw3Vncvb0VhSXJtMnRJYmxmbjQ5dW1xbS9ldVdnNU9zLzhqQVE9P
 Q==

EOF

}


OUTPUT_ADD=./all-add.ldiff
OUTPUT_PASS=./all-pass.ldiff
OUTPUT_MOD=./all-mod.ldiff
OUTPUT_PASSWD=./agri-passwd.txt
OUTPUT_SCRIPT=./add-agri.sh
OUTPUT_GROUP=./agri-groupe.txt

rm -f $OUTPUT_ADD $OUTPUT_PASSWD $OUTPUT_SCRIPT $OUTPUT_GROUP $OUTPUT_MOD

i=0
while [ $i -lt 200 ]; do
	idx=$(printf "%03d" $i)
#	add_user $idx >> $OUTPUT_ADD
	mod_poste $idx  >> $OUTPUT_MOD
#	echo "agriculture_bdc${idx}:agriculture_bdc" >> $OUTPUT_PASSWD
#	echo -n "agriculture_bdc${idx}," >> $OUTPUT_GROUP	
	let i++ 
done

cat > $OUTPUT_SCRIPT <<EOF
#!/bin/sh

ADMIN_DN="cn=ldapadmin,dc=dila,dc=fr"
ADMIN_PW=changeme
#HOSTNAME=localhost
#HOSTNAME=idlv-solon-intel.lyon-dev2.local

#ldapadd -h \${HOSTNAME} -D "\${ADMIN_DN}" -w \${ADMIN_PW} -x -f $OUTPUT_ADD

#ldapmodify -h \${HOSTNAME} -D "\${ADMIN_DN}" -w \${ADMIN_PW} -x -f $OUTPUT_MOD
 
EOF

