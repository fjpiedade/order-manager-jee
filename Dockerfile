# Use the official WildFly image from Docker Hub
FROM jboss/wildfly:latest

# Set environment variables
ENV JAVA_OPTS="-Xms512m -Xmx1024m"

# Add your WAR file to the deployments directory
COPY target/apiorder.war /opt/jboss/wildfly/standalone/deployments/

# Expose port 8080 (WildFly default port)
EXPOSE 8080

# Start WildFly server in standalone mode
CMD ["./wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
