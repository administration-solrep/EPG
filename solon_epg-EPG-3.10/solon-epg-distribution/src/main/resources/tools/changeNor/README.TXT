Exemple d'utilisation :

Voici les 2 NOR qu'il faudrait modifier dans SOLON :
- ECEX1013122L à transformer en EFIX1013122L 
- ECEX0927631L à transformer en EFIX1013122L 

Le contenu du fichier mapOldMinToNewMin.txt contient : 
ECE:50005032:EFI:50000507
Le contenu du fichier nors.txt contient : 
ECEX1013122L
ECEX0927631L

Le contenu du dossier changeNor est à mettre dans le répertoire /tmp.
La ligne de commande nuxeo-shell est script /tmp/changeNors.groovy


