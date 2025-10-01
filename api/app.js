const { createServer } = require('http');
const url = require('url');

// Handle API requests directly with Node.js
function handleRequest(req, res) {
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
  
  // Parse the URL
  const parsedUrl = url.parse(req.url, true);
  const path = parsedUrl.pathname;
  
  console.log(`Request: ${req.method} ${path}`);
  
  // Simple API router
  if (path === '/' || path === '/api' || path === '/health') {
    // Health check or root endpoint
    res.statusCode = 200;
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify({
      success: true,
      message: 'MINIPROJECT.in API is operational',
      timestamp: new Date().toISOString(),
      environment: process.env.VERCEL ? 'Vercel' : 'Development'
    }));
  } else if (path.startsWith('/auth')) {
    // Auth endpoints
    const authPath = path.replace('/auth', '');
    if (authPath === '/request-otp' && req.method === 'POST') {
      // Mock OTP request
      res.statusCode = 200;
      res.setHeader('Content-Type', 'application/json');
      res.end(JSON.stringify({
        success: true,
        message: 'OTP sent to your email (mock response)'
      }));
    } else if (authPath === '/verify-otp' && req.method === 'POST') {
      // Mock OTP verify
      res.statusCode = 200;
      res.setHeader('Content-Type', 'application/json');
      res.end(JSON.stringify({
        success: true,
        message: 'OTP verified successfully (mock response)',
        data: {
          token: 'mock-token-12345',
          email: 'user@example.com',
          message: 'Login successful'
        }
      }));
    } else {
      res.statusCode = 404;
      res.setHeader('Content-Type', 'application/json');
      res.end(JSON.stringify({
        success: false,
        message: 'Auth endpoint not found'
      }));
    }
  } else if (path.startsWith('/products')) {
    // Products endpoints
    if (path === '/products' && req.method === 'GET') {
      // Mock product list
      res.statusCode = 200;
      res.setHeader('Content-Type', 'application/json');
      res.end(JSON.stringify({
        success: true,
        message: 'Products retrieved successfully (mock response)',
        data: [
          { id: 'prod1', name: 'Product 1', price: 19.99, description: 'Mock product 1', active: true },
          { id: 'prod2', name: 'Product 2', price: 29.99, description: 'Mock product 2', active: true }
        ]
      }));
    } else {
      res.statusCode = 404;
      res.setHeader('Content-Type', 'application/json');
      res.end(JSON.stringify({
        success: false,
        message: 'Products endpoint not found'
      }));
    }
  } else {
    // Default not found response
    res.statusCode = 404;
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify({
      success: false,
      message: 'Endpoint not found',
      path: path
    }));
  }
}
}

// Create the server
const server = createServer(handleRequest);

// Export a serverless function handler for Vercel
module.exports = (req, res) => {
  console.log('Request received in serverless function');
  handleRequest(req, res);
};