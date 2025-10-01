const { spawn } = require('child_process');
const { createServer } = require('http');
const path = require('path');

// Path to the JAR file - check both possible locations
let jarPath;
try {
  // First check in lib directory (for production)
  const libJarPath = path.join(__dirname, 'lib/in-0.0.1-SNAPSHOT.jar');
  if (require('fs').existsSync(libJarPath)) {
    jarPath = libJarPath;
    console.log('Using JAR from lib directory:', jarPath);
  } else {
    // Fallback to target directory (for local development)
    const targetJarPath = path.join(__dirname, '../target/in-0.0.1-SNAPSHOT.jar');
    if (require('fs').existsSync(targetJarPath)) {
      jarPath = targetJarPath;
      console.log('Using JAR from target directory:', jarPath);
    } else {
      throw new Error('JAR file not found in either lib or target directory');
    }
  }
} catch (error) {
  console.error('Error locating JAR file:', error);
  throw error;
}

// Function to start the Java application
async function startJavaApp() {
  console.log('Starting Java application...');
  
  // Start Java process
  const java = spawn('java', [
    `-Dserver.port=${process.env.PORT || 8080}`,
    '-Dspring.profiles.active=vercel',
    '-jar', 
    jarPath
  ]);
  
  // Log output
  java.stdout.on('data', (data) => {
    console.log(`stdout: ${data}`);
  });
  
  java.stderr.on('data', (data) => {
    console.error(`stderr: ${data}`);
  });
  
  java.on('close', (code) => {
    console.log(`Java process exited with code ${code}`);
  });
  
  // Allow time for the Java app to start
  console.log('Waiting for Java application to initialize...');
  await new Promise(resolve => setTimeout(resolve, 2000));
  
  // Create a health check function
  const checkHealth = () => {
    return new Promise((resolve, reject) => {
      const http = require('http');
      const req = http.request({
        hostname: 'localhost',
        port: process.env.PORT || 8080,
        path: '/health',
        method: 'GET',
        timeout: 3000
      }, res => {
        if (res.statusCode === 200) {
          resolve(true);
        } else {
          reject(new Error(`Health check failed with status: ${res.statusCode}`));
        }
      });
      
      req.on('error', reject);
      req.end();
    });
  };
  
  // Try health check a few times
  let healthy = false;
  for (let i = 0; i < 5; i++) {
    try {
      await checkHealth();
      healthy = true;
      console.log('Java application is healthy and ready');
      break;
    } catch (err) {
      console.log(`Health check attempt ${i+1} failed:`, err.message);
      await new Promise(resolve => setTimeout(resolve, 1000));
    }
  }
  
  if (!healthy) {
    console.warn('Could not confirm Java application health, proceeding anyway');
  }
  
  // Return proxy server that forwards requests to the Java app
  return createServer((req, res) => {
    console.log(`Proxying request to ${req.method} ${req.url}`);
    
    // Add CORS headers
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');
    res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type,Authorization');
    res.setHeader('Access-Control-Allow-Credentials', true);
    
    // Handle preflight OPTIONS requests
    if (req.method === 'OPTIONS') {
      res.statusCode = 204;
      res.end();
      return;
    }
    
    // Simple proxy to forward requests to the Java application
    const options = {
      hostname: 'localhost',
      port: process.env.PORT || 8080,
      path: req.url,
      method: req.method,
      headers: req.headers
    };
    
    const proxyReq = require('http').request(options, (proxyRes) => {
      res.writeHead(proxyRes.statusCode, proxyRes.headers);
      proxyRes.pipe(res, { end: true });
    });
    
    proxyReq.on('error', (e) => {
      console.error(`Problem with request: ${e.message}`);
      res.statusCode = 500;
      res.end(JSON.stringify({
        success: false,
        message: 'Internal Server Error',
        error: e.message
      }));
    });
    
    if (['POST', 'PUT', 'PATCH'].includes(req.method)) {
      req.pipe(proxyReq);
    } else {
      proxyReq.end();
    }
  });
}

// Initialize the Java app on module load
let serverPromise = null;

module.exports = (req, res) => {
  console.log('Request received, handling with Java application');
  
  // Start Java app if not already started
  if (!serverPromise) {
    console.log('Initializing Java application for the first time');
    serverPromise = startJavaApp();
  }
  
  serverPromise.then(server => {
    server.emit('request', req, res);
  }).catch(err => {
    console.error('Failed to process request with Java application:', err);
    res.statusCode = 500;
    res.end('Internal Server Error: Failed to process request with Java application');
  });
};