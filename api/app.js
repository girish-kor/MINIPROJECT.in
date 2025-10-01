const { spawn } = require('child_process');
const { createServer } = require('http');
const path = require('path');

// Path to the JAR file
const jarPath = path.join(__dirname, '../target/in-0.0.1-SNAPSHOT.jar');

// Function to start the Java application
async function startJavaApp() {
  console.log('Starting Java application...');
  
  // Start Java process
  const java = spawn('java', [
    '-Dserver.port=${PORT}',
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
  
  // Return proxy server that forwards requests to the Java app
  return createServer((req, res) => {
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
    
    req.pipe(proxyReq, { end: true });
    
    proxyReq.on('error', (e) => {
      console.error(`Problem with request: ${e.message}`);
      res.statusCode = 500;
      res.end('Internal Server Error');
    });
  });
}

module.exports = (req, res) => {
  console.log('Request received, starting Java application');
  startJavaApp().then(server => {
    server.emit('request', req, res);
  }).catch(err => {
    console.error('Failed to start Java application:', err);
    res.statusCode = 500;
    res.end('Failed to start Java application');
  });
};