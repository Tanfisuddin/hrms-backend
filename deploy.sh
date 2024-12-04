#!/bin/bash

# Example: Build the application using Gradle
cd admin
./gradlew clean build
#./gradlew build -p common-module

cd build/libs

existing_pid=$(pgrep -f "java.*admin-0.0.1-SNAPSHOT.jar")

if [ -n "$existing_pid" ]; then
    echo "Stopping existing application with PID $existing_pid..."
    sudo kill $existing_pid
    sleep 5  # Optional: Give some time for the process to stop gracefully
else
    echo "No existing application found running."
fi

nohup java -jar -Dspring.profiles.active=dev admin-0.0.1-SNAPSHOT.jar > ~/hrmsAdmin.log  2>&1 &

# Example: Restart application service
echo "Deployment completed."

