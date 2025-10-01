const { execSync } = require('child_process');
const fs = require('fs');
const path = require('path');

// Function to execute shell commands and print output
function runCommand(command) {
  console.log(`> Executing: ${command}`);
  try {
    const output = execSync(command, { encoding: 'utf8', stdio: 'inherit' });
    return output;
  } catch (error) {
    console.error(`Command failed: ${command}`);
    console.error(error);
    process.exit(1);
  }
}

// Main build function
function build() {
  console.log('Starting build process for Java application...');
  
  // Print Java version
  try {
    runCommand('java -version');
  } catch (e) {
    console.warn('Java not available in build environment. Continuing anyway.');
  }
  
  // Make Maven wrapper executable
  try {
    runCommand('chmod +x ./mvnw');
  } catch (e) {
    console.warn('Could not make mvnw executable. Build may fail.');
  }
  
  // Build project
  console.log('Building project with Maven...');
  try {
    runCommand('./mvnw clean package -DskipTests -Dspring.profiles.active=vercel');
  } catch (e) {
    console.log('Maven build failed, using pre-built JAR if available');
  }
  
  // Create necessary directories
  console.log('Preparing deployment structure...');
  if (!fs.existsSync('api/lib')) {
    fs.mkdirSync('api/lib', { recursive: true });
  }
  
  // Copy the JAR file
  console.log('Copying JAR file to deployment location...');
  if (fs.existsSync('target/in-0.0.1-SNAPSHOT.jar')) {
    fs.copyFileSync('target/in-0.0.1-SNAPSHOT.jar', 'api/lib/in-0.0.1-SNAPSHOT.jar');
    console.log('JAR file copied successfully');
  } else {
    console.error('JAR file not found in target directory');
    process.exit(1);
  }
  
  // Validate JAR file
  if (fs.existsSync('api/lib/in-0.0.1-SNAPSHOT.jar')) {
    const stats = fs.statSync('api/lib/in-0.0.1-SNAPSHOT.jar');
    console.log(`JAR file size: ${stats.size} bytes`);
  } else {
    console.error('JAR file not found in deployment location');
    process.exit(1);
  }
  
  console.log('Build process completed successfully!');
}

// Run the build
build();