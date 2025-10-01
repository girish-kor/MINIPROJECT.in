#!/bin/bash

# Print Java version
java -version

# Ensure Maven wrapper is executable
chmod +x ./mvnw

# Build project for Vercel
./mvnw clean package -DskipTests -Dspring.profiles.active=vercel

# Print build completion message
echo "Java build completed"