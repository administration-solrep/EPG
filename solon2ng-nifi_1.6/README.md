# SOLON2NG NIFI

## Prérequis

-   Java 8 installé
-   Maven 3 installé

## Build

```bash
$ mvn clean install
```

## Contenu

### Services

#### fr.dila.nifi.solon.services.ElasticSearchClientDilaServiceImpl

Service qui permet de communiquer avec un cluster Elasticsearch en version 7.9.

Ce service gère le loadbalancing entre différents noeuds d'un cluster.

Les noeuds sont définis dans la propriété "HTTP Hosts".

Un noeud est défini par l'url d'accès au noeud avec le format "{scheme}://{hostname}:{port}".

Les noeuds sont séparés par des ",".

Exemple :

`http://localhost:9200,http://localhost:9201,http://localhost:9202`

En cas d'utilisation d'un cluster sécurisé, il est possible de fournir le nom d'utilisateur, le mot de passe et les certificats autosignés du cluster à reconnaître comme certificats valides.

Le service retourne le temps nécessaire à l'exécution de la requête en cas de succès ou des exceptions :

- ElasticsearchClientException : si la demande n'est pas valide (json incorrect par exemple), dans ce type de cas, il est inutile de rejouer le flow file tant qu'il n'est pas corrigé.
- ElasticsearchRetryableClientException : si le cluster a retourné une erreur 5xx par exemple suite à une coupure temporaire. Le flow file peut-être réessayé en l'état plus tard.

### Processeurs

#### fr.dila.nifi.solon.processors.PutElasticsearch

Ce processeur permet d'indexer un document dans Elasticsearch en mode unitaire.

L'index est passé dans la propriété "Index".

L'identifiant du document est fournit dans un attribut du flow file. Le nom de cet attribut est fournit par la propriété "Identifier Attribute".

Le corps du document à indexer (le JSON) est fournit dans le contenu du flow file.

Le processeur transfère le flow file en sortie vers une des trois files :

- success : si le document est acquitté par au moins un noeud du cluster, il est correctement indexé. Le temps pris par l'indexation en millisecondes est disponible dans l'attribut "elasticsearch.update.took".
- error : si le document ne peut pas être indexé car il est invalide, il est inutile de réessayer sans avoir corrigé le flow file. L'erreur est disponible dans l'attribut "elasticsearch.update.error".
- retry : si le document ne peut pas être indexé suite à un problème de communication avec le cluster. Dans ce cas, il est possible de rejouer le flow file plus tard lorsque le cluster est à nouveau opérationnel. Le flow file dans cette file est pénalisé pour avoir une temporisation minimale avant la prochaine tentative. L'erreur est disponible dans l'attribut "elasticsearch.update.error". Cette relation peut pointer par exemple sur un "RetryFlowFile" et/ou un "ControlRate" pour maîtriser le nombre de tentatives et le taux de tentatives.

## Déploiement

Chaque module maven avec un nom "nifi-solon*-nar" génère un fichier nar de nifi.

Le module "nifi-solon-distribution" lui génère un fichier tar.gz qui contient à sa racine l'ensemble des fichiers nar générés par les autres modules.

Ce fichier tar.gz peut-être extraits directement dans le dossier "lib" de nifi ou dans un dossier dédié personnalisé par le paramètre `nifi.nar.library.directory.<nom de library quelconque>=/path/vers/ma/librairie` ([Lien vers la documentation](https://nifi.apache.org/docs/nifi-docs/html/administration-guide.html#installing-custom-processors)).
