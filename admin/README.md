# starter-admin(web)

## Build

``` sh 
./gradlew admin:build -Pprofile=local
```

## Run(after Build)

``` sh
# from root
cd admin/build/libs
# Heap size is 1G. It is changeable
java -server -Xms1G -Xmx1G -XX:+UseG1GC -verbose:gc -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -jar -Dspring.profiles.active=local -Dlog4j2.formatMsgNoLookups=true admin-1.0.0-SNAPSHOT.jar
```

## Local Development
### 1. Run AdminApplication
 - IDE 환경에 따라 다를수 있음.
 
 <img width="1030" alt="스크린샷 2023-04-09 오후 6 24 12" src="https://user-images.githubusercontent.com/6982740/230765156-5c22e712-85e9-4f41-ad79-750000145201.png">


### 2. Run Webpack Dev Server
``` sh
# start webpack dev server
cd admin
yarn watch
```

<img width="923" alt="스크린샷 2023-04-09 오후 6 24 46" src="https://user-images.githubusercontent.com/6982740/230765175-ecd126ff-b954-43a9-aff7-1d3395041f30.png">

### 3. Web Page 접근
> local 환경에서는 static resoruces 서빙을 webpack-dev-server을 통해 처리함.
> spring boot에서 랜더링하는 페이지 Template을 webpack-dev-server가 proxy해서 제공

 - `http://localhost:3002/` 로 접근하여 개발
 
<img width="1411" alt="스크린샷 2023-04-09 오후 6 25 22" src="https://user-images.githubusercontent.com/6982740/230765195-bd65e76f-5513-4609-9e0e-819fd935292d.png">
