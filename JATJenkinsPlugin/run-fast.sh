#! /bin/sh
mvn clean
mvn hpi:run -Dmaven.test.skip=true
