#!/bin/bash

#
# Outil de géneration de faux dossiers à partir d'un dossier de nor donnée (dont le répertoire est donné)
# 
#

TEMPLATE_DIR=./data/injection-validation-template
TEMPLATE_NOR=AGRU0806191A
DEST_DIR=/tmp/injection-validation
NORS_TXT=./conf/nors.txt
NORS_PASWD=./conf/norspwd.txt

NOR_START=${1:? "$1 est vide, fin de script."}
NOR_END=${2:? "$2 est vide, fin de script."}

if [ -d $DEST_DIR ]; then
    rm -fr $DEST_DIR
fi

mkdir $DEST_DIR

## Création du fichier nors.txt
NORS_CONTENT="nors_a_valider:"
cat /dev/null > $NORS_PASWD
for i in `seq ${NOR_START} ${NOR_END}`
do 
	nor=`printf "AGRU08%05dA\n" "$i"`
	NORS_CONTENT=${NORS_CONTENT}${nor},
	echo $nor:$nor >> $NORS_PASWD 
done
echo $NORS_CONTENT | sed 's/.\{1\}$//' > $NORS_TXT


## Création du fichier nors.txt
NORS_CONTENT="nors_a_valider:"
for i in `seq ${NOR_START} ${NOR_END}`
do 
	nor=`printf "AGRU08%05dA\n" "$i"`
	NORS_CONTENT=${NORS_CONTENT}${nor},
done
echo $NORS_CONTENT | sed 's/.\{1\}$//' > $NORS_TXT


## Creation des dossiers clonés
for i in `seq ${NOR_START} ${NOR_END}`
do
	new_nor=`printf "AGRU08%05dA\n" "$i"`

	# creation d'un nouveau dossier
	mkdir -p $DEST_DIR/$new_nor

	ATT_FILE_ORIG=${TEMPLATE_NOR}_att.xml
	ATT_FILE_DEST=${new_nor}_att.xml
	cat $TEMPLATE_DIR/$TEMPLATE_NOR/$ATT_FILE_ORIG | sed "s/$TEMPLATE_NOR/$new_nor/g" > $DEST_DIR/$new_nor/$ATT_FILE_DEST

	FDR_FILE_ORIG=${TEMPLATE_NOR}_FDR.xml
	FDR_FILE_DEST=${new_nor}_FDR.xml
	cat $TEMPLATE_DIR/$TEMPLATE_NOR/$FDR_FILE_ORIG | sed "s/$TEMPLATE_NOR/$new_nor/g" > $DEST_DIR/$new_nor/$FDR_FILE_DEST

	JOURNAL_FILE_ORIG=${TEMPLATE_NOR}_journal.xml
	JOURNAL_FILE_DEST=${new_nor}_journal.xml
	cat $TEMPLATE_DIR/$TEMPLATE_NOR/$JOURNAL_FILE_ORIG | sed "s/$TEMPLATE_NOR/$new_nor/g" > $DEST_DIR/$new_nor/$JOURNAL_FILE_DEST
	
	mkdir -p $DEST_DIR/$new_nor/CONTENU
	cp $TEMPLATE_DIR/$TEMPLATE_NOR/CONTENU/"${TEMPLATE_NOR}_EXTRAIT - ${TEMPLATE_NOR} - Bombon.doc_extrait_V1.doc" $DEST_DIR/$new_nor/CONTENU/"${new_nor}_EXTRAIT - ${new_nor} - Bombon.doc_extrait_V1.doc"
done 
