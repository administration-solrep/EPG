#!/bin/bash
# Ce script permet de générer un fichier ldif dans lequel les groupes d'appartenance des utilisateurs leur sont rajoutés.
# Cas d'usage : transfert d'utilisateurs d'une base ldap à une autre. La génération du ldif des utilisateurs ne contient pas leurs groupes. Ce script permet de générer un fichier ldif les rattachant aux groupes qui conviennent.
# Exemple d'utilisation : ./ldapExportGroupsLdif.sh uid=identifiant1,ou=people,ou=SolonEpg,dc=dila,dc=fr uid=identifiant11,ou=people,ou=SolonEpg,dc=dila,dc=fr uid=identifiant12,ou=people,ou=SolonEpg,dc=dila,dc=fr > ldapscript.ldif

host="idlv-solon-intel.lyon-dev2.local"
bindDN="cn=nuxeo5,ou=SolonEpg,dc=dila,dc=fr"
password="changeme"
baseDN="ou=SolonEpg,dc=dila,dc=fr"

for arg in "$@"; do
	postes=($(ldapsearch -h "${host}" -D "${bindDN}" -w "${password}" -b "${baseDN}" -LLL "(uniqueMember=${arg})" dn | perl -p00e 's/\r?\n //g'))
	for ((i=0; i < ${#postes[@]}; i++))
	do
		dnstr=${postes[$i]}		
		i=$((i+1))
		poste=${postes[$i]}		
		echo  ${dnstr} " " ${poste}
		echo "changetype: modify"
		echo "add: uniqueMember"
		echo "uniqueMember: " ${arg}
		echo ""
	done
done
