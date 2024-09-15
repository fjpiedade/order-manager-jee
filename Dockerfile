## Use the official WildFly image from Docker Hub
#FROM jboss/wildfly:33.0.1.Final
#
## Set environment variables
#ENV JAVA_OPTS="-Xms512m -Xmx1024m"
#
## Add your WAR file to the deployments directory
#COPY target/apiorder.war /opt/jboss/wildfly/standalone/deployments/
#
## Copy api folder with jakarta.api dependences to support postgresql hibernates
#COPY jboss_wildfly/jakarta/api /opt/jboss/wildfly/modules/system/layers/base/jakarta/
#
## copy postgresql drive and Drive as module supported by JBoss/WildFly
#COPY jboss_wildfly/postgresql /opt/jboss/wildfly/modules/system/layers/base/
#
## Add standalone.xml configured with Postgres Datasource
#COPY jboss_wildfly/standalone.xml /opt/jboss/wildfly/standalone/configuration/
#
## Expose port 8080 (WildFly default port)
#EXPOSE 8080
#
## Start WildFly server in standalone mode
#CMD ["./wildfly/bin/standalone.sh", "-b", "0.0.0.0"]

# Use OpenJDK as the base image
FROM openjdk:17-jdk-slim

# Set environment variables for WildFly installation
ENV WILDFLY_VERSION=33.0.1.Final
ENV WILDFLY_HOME=/opt/wildfly

# Download and install WildFly
RUN apt-get update && apt-get install -y wget unzip \
    && wget https://github.com/wildfly/wildfly/releases/download/${WILDFLY_VERSION}/wildfly-${WILDFLY_VERSION}.tar.gz \
    && tar -xzf wildfly-${WILDFLY_VERSION}.tar.gz \
    && mv wildfly-${WILDFLY_VERSION} $WILDFLY_HOME \
    && rm wildfly-${WILDFLY_VERSION}.tar.gz \
    && apt-get remove -y wget unzip \
    && apt-get autoremove -y

# Copy api folder with Jakarta API dependencies to support PostgreSQL Hibernate
COPY jboss_wildfly/jakarta/ ${WILDFLY_HOME}/modules/system/layers/base/jakarta/

# Copy PostgreSQL driver and module configuration supported by JBoss/WildFly
COPY jboss_wildfly/drives/ ${WILDFLY_HOME}/modules/system/layers/base/org/

# Add standalone.xml configured with Postgres Datasource
COPY jboss_wildfly/standalone.xml ${WILDFLY_HOME}/standalone/configuration/

# Copy WAR file or other deployments
COPY target/apiorder.war ${WILDFLY_HOME}/standalone/deployments/

# Expose WildFly port (default is 8080)
EXPOSE 8080

# Set the working directory to WildFly home
WORKDIR ${WILDFLY_HOME}

# Set WildFly as the default command to run
CMD ["bin/standalone.sh", "-b", "0.0.0.0"]
