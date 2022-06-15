# Birt

## Git flow main commands

git flow init : pour initialiser Git et Gitflow dans un projet.
git flow feature start <nom> : pour démarrer le développement d’une nouvelle fonctionnalité.
git flow feature finish <nom> : pour terminer le développement d’une nouvelle fonctionnalité.
git flow release start <version> : pour démarrer le développement d’une nouvelle release.
git flow release finish <nom> : pour terminer le développement d’une nouvelle release.
git flow hotfix start <version> : pour démarrer le développement d’un nouveau hotfix.
git flow hotfix finish <nom> : pour terminer le développement d’un nouveau hotfix.

## Release commands

```sh
releaseVersion=X.X.X
nextVersion=Y.Y.Y
git flow release start ${releaseVersion}
mvn versions:set -DnewVersion=${releaseVersion}
mvn versions:commit
git add pom.xml "*/pom.xml"
git commit -m "New release ${releaseVersion}"
git flow release finish ${releaseVersion} -m "Git flow release ${releaseVersion}"
mvn versions:set -DnewVersion=${nextVersion}-SNAPSHOT
mvn versions:commit
git add pom.xml "*/pom.xml"
git commit -m "Prepare next snapshot ${nextVersion}-SNAPSHOT"
git push origin develop
git push origin master
```
