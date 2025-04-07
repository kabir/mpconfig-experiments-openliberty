# MP Config in EAR experiments

This is a fork of https://github.com/openliberty/guide-maven-multimodules, from 
https://openliberty.io/guides/maven-multimodules.html

The project lives in the finish/ directory.

Run `mvn clean package` to build it.

## Trying Open Liberty

While OpenLiberty has a dev mode, it does not seem to work well with ears (this is normally run with `mvn liberty:dev`).

So instead we need to download the server and deploy the application manually. These are the steps:

* Download the MicroProfile 7 zip from https://openliberty.io/start/, and unzip it somewhere (referred to as `$OL_HOME` below)
* Create a 'defaultServer' server instance by running `$OL_HOME/bin/server create`
* Update the server.xml to be the one from this project `cp ear/src/main/liberty/config/server.xml $OL_HOME/usr/servers/defaultServer/`
* Copy the .ear into the server `cp ear/target/guide-maven-multimodules-ear.ear ~/Downloads/wlp/usr/servers/defaultServer/apps`
* Follow the logs by running `tail -F wlp/usr/servers/defaultServer/logs/messages.log` (There is also a `console.log` is a bit less verbose)
* Start the server with `$OL_HOME/bin/server start defaultServer` (Stop it later with `./wlp/bin/server stop defaultServer`)


The REST endpoints are at:
* http://localhost:9080/sample
* http://localhost:9080/sampleB

Each REST endpoint call will give output, and also log the values of properties from various sources.


## Trying WildFly

To deploy in WildFly, deploy as usual. I have been starting WildFly with the default configuration `standalone.xml` 
which contains support for the Jakarta EE parts.

For non-wildfly users:

* [Download WildFly](https://www.wildfly.org/downloads/) and unzip it somewhere (referred to as `$WILDFLY_HOME` below) 
* Run server in a terminal `$WILDFLY_HOME/bin/standalone.sh`
* In another terminal run the CLI, and execute the following commands to deploy the .ear we built initially:
```
% ./build/target/wildfly-36.0.0.Final-SNAPSHOT/bin/jboss-cli.sh
You are disconnected at the moment. Type 'connect' to connect to the server or 'help' for the list of supported commands.
[disconnected /] connect
[standalone@localhost:9990 /] deploy --force path/to/guide-maven-multimodules-ear.ear 
```

The REST endpoints are at:
* http://localhost:8080/guide-maven-multimodules-war
* http://localhost:8080/guide-maven-multimodules-warB

Each REST endpoint call will give output, and also log the values of properties from various sources.

