#!/bin/bash
java -Dserver.address=127.0.0.1 -Dserver.port=8081 -cp "solon2ng-birt-webapp-1.0.8-SNAPSHOT.jar:lib/*" fr.dila.solon.birt.rest.RestServiceBirtApplication --properties.location=config/config.properties
