# Kotlin-spring-multi-module-starter

## Tech Stack
### Back-end
- spring boot 3.0.5
- kotlin 1.8.10
- Spring-Data-Jpa
- MySQL( docker-compose )
- Caffeine Local Cache
- Mapstruct
- CoroutinVersion
- Webflux/Jetty(for Spring6 HTPP Interface)
- Kotest

### Front-end
 - webpack
 - vue.js

## Build

### 1. admin
``` sh
./gradlew admin:build -Pprofile=local
```

### 2. user 
``` sh
./gradlew api:build -Pprofile=local
```

## Intellij Plugins
### Back-end
- kotest
- ktlint

### Front-end
- vue.js 
