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

# Findings

We are testing four locations in the ear file:

* webinf - the config injected into the war's SampleResource
* webinflibjar - config injected into the lib included in each war's SampleResource
* lib - config injected into the jar contained in the ear's lib/ folder
* ejb - config injected into the ejb which is in an ejb jar module

The properties follow the formats:

* `sanity.test.only.<location>.property` - these are for properties which are only defined once. e.g. 
`sanity.test.only,webinf.property` is a value only used in the config injected into the war's SampleResource
* `sanity.test.shared.<location>[_<location>]*.property` - these are for properties, which are defined in several configs. 
e.g `sanity.test.shared.ejb-lib-webinf-webinflib` is a value used in all the four above locations, while
`sanity.test.shared.ejb-lib` is a value used in the 'lib' and 'ejb' locations.

The below shows the retrieved values for the configs.

In short:
* If a property is defined in only one place, it has its expected value in both OpenLiberty and WildFly. It should be 
noted that both servers appear to get config from classloaders they have access to apart from themselves. i.e.
in the war we have access to the value of the `sanity.test.only.ejb.property` property (which comes from the ejb jar). 
* If a property is defined in several places, the resulting value is not reliable, and varies depending on the server.
This is likely due to the two servers having different ordering of how the classpath is resolved. Also, the ordinals of 
the used microprofile-config.properties are the same. For this reason we should discourage the use of shared properties,
and a future version of the specification could specify the behaviour.

**WAR:**

Property | OpenLiberty | WildFly
---|-------------|---
sanity.test.only,webinf.property | war | war
sanity.test.only.ejb.property | ejb | ejb
sanity.test.only.lib.property | lib | lib
sanity.test.only.webinflibjar.property | webinflibjar | webinflibjar
sanity.test.shared.ejb-lib | ejb | lib
sanity.test.shared.ejb-lib-webinf | ejb | war
sanity.test.shared.ejb-lib-webinf-webinflib | ejb | war
sanity.test.shared.ejb-lib-webinflib | ejb | webinflibjar
sanity.test.shared.ejb-webinf | ejb | war
sanity.test.shared.ejb-webinf-webinflib | ejb | war
sanity.test.shared.ejb-webinflib | ejb | webinflibjar
sanity.test.shared.lib-webinf | lib | war
sanity.test.shared.lib-webinf-webinflib | lib | war
sanity.test.shared.lib-webinflib | lib | webinflibjar
sanity.test.shared.webinf-webinflib | war | war

**WEB-INF/lib/*.jar:**

Property | OpenLiberty | WildFly
---|-------------|---
sanity.test.only,webinf.property | war | war
sanity.test.only.ejb.property | ejb | ejb
sanity.test.only.lib.property | lib | lib
sanity.test.only.webinflibjar.property | webinflibjar | webinflibjar
sanity.test.shared.ejb-lib | ejb | lib
sanity.test.shared.ejb-lib-webinf | ejb | war
sanity.test.shared.ejb-lib-webinf-webinflib | ejb | war
sanity.test.shared.ejb-lib-webinflib | ejb | webinflibjar
sanity.test.shared.ejb-webinf | ejb | war
sanity.test.shared.ejb-webinf-webinflib | ejb | war
sanity.test.shared.ejb-webinflib | ejb | webinflibjar
sanity.test.shared.lib-webinf | lib | war
sanity.test.shared.lib-webinf-webinflib | lib | war
sanity.test.shared.lib-webinflib | lib | webinflibjar
sanity.test.shared.webinf-webinflib | war | war


**EAR lib:**

Property | OpenLiberty | WildFly
---|-------------|---
sanity.test.only,webinf.property | war | war
sanity.test.only.ejb.property | ejb | ejb
sanity.test.only.lib.property | lib | lib
sanity.test.only.webinflibjar.property | webinflibjar | webinflibjar
sanity.test.shared.ejb-lib | ejb | lib
sanity.test.shared.ejb-lib-webinf | ejb | war
sanity.test.shared.ejb-lib-webinf-webinflib | ejb | war
sanity.test.shared.ejb-lib-webinflib | ejb | webinflibjar
sanity.test.shared.ejb-webinf | ejb | war
sanity.test.shared.ejb-webinf-webinflib | ejb | war
sanity.test.shared.ejb-webinflib | ejb | webinflibjar
sanity.test.shared.lib-webinf | lib | war
sanity.test.shared.lib-webinf-webinflib | lib | war
sanity.test.shared.lib-webinflib | lib | webinflibjar
sanity.test.shared.webinf-webinflib | war | war

**EJB:**

Property | OpenLiberty | WildFly
---|-------------|---
sanity.test.only.ejb.property | ejb | ejb
sanity.test.only.lib.property | lib | lib
sanity.test.shared.ejb-lib | ejb | ejb
sanity.test.shared.ejb-lib-webinf | ejb | ejb
sanity.test.shared.ejb-lib-webinf-webinflib | ejb | ejb
sanity.test.shared.ejb-lib-webinflib | ejb | ejb
sanity.test.shared.ejb-webinf | ejb | ejb
sanity.test.shared.ejb-webinf-webinflib | ejb | ejb
sanity.test.shared.ejb-webinflib | ejb | ejb
sanity.test.shared.lib-webinf | lib | lib
sanity.test.shared.lib-webinf-webinflib | lib | lib
sanity.test.shared.lib-webinflib | lib | lib