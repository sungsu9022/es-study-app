# starter-user-api

## Build

``` sh 
./gradlew clean build -p user -Pprofile=local
```

## Run(after Build)

``` sh
# from root
cd user/build/libs
# Heap size is 1G. It is changeable
java -server -Xms1G -Xmx1G -XX:+UseG1GC -verbose:gc -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -jar -Dspring.profiles.active=local -Dlog4j2.formatMsgNoLookups=true elasticsearch-1.0.0-SNAPSHOT.jar

```
