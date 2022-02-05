FROM gradle:7.3.3-jdk17 as base
WORKDIR /server
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY library/build.gradle.kts library/settings.gradle.kts ./library/
COPY server/build.gradle.kts server/settings.gradle.kts ./server/
RUN gradle dependencies --refresh-dependencies
COPY . .

FROM base as test
CMD ["gradle", "test"]

FROM base as development
CMD ["gradle", "--watch-fs", "bootRun"]

FROM base as build
RUN gradle assemble

FROM openjdk:17-jdk-alpine as production
WORKDIR /server
COPY --from=build /server/server/build/libs/server-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "server-0.0.1-SNAPSHOT.jar"]
