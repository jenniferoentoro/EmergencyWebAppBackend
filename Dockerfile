FROM gradle:7.5.0-jdk17
WORKDIR /opt/app

COPY ./build/libs/EmergencyWebApps-0.0.1-SNAPSHOT.jar ./

VOLUME /app/public/newsPhotos
VOLUME /app/public/incidentReportPhotos
VOLUME /app/public/videoTutorials

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar EmergencyWebApps-0.0.1-SNAPSHOT.jar"]
