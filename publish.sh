#!/bin/sh

mvn deploy:deploy-file -X -DpomFile=pom.xml \
  -Dfile=target/clarin.webservices.pidservices2-3.1.jar \
  -DrepositoryId=Clarin \
  -Durl=https://nexus.clarin.eu/repository/Clarin

