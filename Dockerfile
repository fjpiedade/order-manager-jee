FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

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

# Expose WildFly port (default is 8081)
EXPOSE 8881

# Set the working directory to WildFly home
WORKDIR ${WILDFLY_HOME}

# Set WildFly as the default command to run
CMD ["bin/standalone.sh", "-b", "0.0.0.0"]
