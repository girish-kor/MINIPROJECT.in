#!/bin/bash
echo "Building and preparing for Vercel deployment..."

# Ensure script runs in its directory
cd "$(dirname "$0")"

# Ensure Maven wrapper is executable
chmod +x ./mvnw

# Build the project with Vercel configuration
./mvnw clean package -DskipTests -Dspring.profiles.active=vercel

# Copy the JAR to the expected location
cp target/in-0.0.1-SNAPSHOT.jar target/in-0.0.1-SNAPSHOT.jar

echo ""
echo "Project built successfully for Vercel."
