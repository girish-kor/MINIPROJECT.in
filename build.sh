#!/bin/bash
set -e

# Print Java version
echo "Checking Java version..."
java -version

# Ensure Maven wrapper is executable
echo "Making Maven wrapper executable..."
chmod +x ./mvnw

# Build project for Vercel
echo "Building project with Maven..."
./mvnw clean package -DskipTests -Dspring.profiles.active=vercel

# Print build completion message
echo "Java build completed successfully"

# Create necessary directories
echo "Preparing deployment structure..."
mkdir -p api/lib

# Copy the JAR to the expected location
echo "Copying JAR file to deployment location..."
cp target/in-0.0.1-SNAPSHOT.jar api/lib/

# Validate JAR file
echo "Validating JAR file..."
if [ -f "api/lib/in-0.0.1-SNAPSHOT.jar" ]; then
  echo "JAR file successfully copied"
  ls -la api/lib/
else
  echo "ERROR: JAR file not found in target location"
  exit 1
fi

echo "Build process completed successfully!"
