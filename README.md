# MP Config in EAR experiments

This is a fork of https://github.com/openliberty/guide-maven-multimodules, from 
https://openliberty.io/guides/maven-multimodules.html

The project lives in the finish/ directory.

Run `mvn clean package` to build it.

## Trying Open Liberty

To deploy in OpenLiberty, run `mvn liberty:dev`.

The REST endpoints are at:
* http://localhost:9080/sample
* http://localhost:9080/sampleB

Each REST endpoint call will give output, and also log the values of properties from various sources.


## Trying WildFly

To deploy in WildFly, deploy as usual. I have been starting WildFly with `standalone-microprofile.xml`.

The REST endpoints are at:
* http://localhost:8080/guide-maven-multimodules-war
* http://localhost:8080/guide-maven-multimodules-warB

Each REST endpoint call will give output, and also log the values of properties from various sources.

