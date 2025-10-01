# Deploying to Render Using CLI Commands

This guide will walk you through deploying your Spring Boot application to Render using the command line interface.

## Prerequisites

1. Node.js and npm installed on your system
2. A [Render](https://render.com) account with API key
3. Your application code committed to a Git repository

## Step 1: Install Render CLI

First, install the Render CLI using npm:

```bash
npm install -g @render/cli
```

## Step 2: Authenticate with Render

Set up your Render API key (you can find this in your Render account settings):

```bash
# On Windows PowerShell
$env:RENDER_API_KEY="your_render_api_key"

# On Linux/macOS
export RENDER_API_KEY="your_render_api_key"
```

## Step 3: Deploy Your Service

### Option 1: Using render.yaml (Recommended)

Since we already have a `render.yaml` file configured, you can deploy directly:

```bash
# Navigate to your project directory
cd D:\test3\in

# Deploy using render.yaml configuration
render blueprint apply
```

### Option 2: Manual Service Creation

If you prefer to create the service without using render.yaml:

```bash
# Create a new web service
render create service \
  --type web \
  --name miniproject-backend \
  --repo https://github.com/girish-kor/MINIPROJECT.in \
  --branch render-deploy \
  --runtime docker \
  --plan free \
  --region oregon \
  --env MONGODB_URI=mongodb+srv://your-mongodb-uri \
  --env JWT_SECRET=your-jwt-secret \
  --env JWT_EXPIRATION=86400000 \
  --env MAIL_HOST=smtp.gmail.com \
  --env MAIL_PORT=587 \
  --env MAIL_USERNAME=your-email@example.com \
  --env MAIL_PASSWORD=your-app-password \
  --env STRIPE_SECRET_KEY=your-stripe-secret-key \
  --env STRIPE_PUBLISHABLE_KEY=your-stripe-publishable-key \
  --env STRIPE_WEBHOOK_SECRET=your-stripe-webhook-secret \
  --env STRIPE_SUCCESS_URL=https://your-frontend-url/payment/success \
  --env STRIPE_CANCEL_URL=https://your-frontend-url/payment/cancel \
  --env OTP_EXPIRATION=300000 \
  --env JAVA_OPTS="-XX:+UseContainerSupport -Xmx512m -Djava.security.egd=file:/dev/./urandom"
```

Note: For PowerShell, use backticks (`) for line continuation and different environment variable syntax:

```powershell
render create service `
  --type web `
  --name miniproject-backend `
  --repo https://github.com/girish-kor/MINIPROJECT.in `
  --branch render-deploy `
  --runtime docker `
  --plan free `
  --region oregon `
  --env MONGODB_URI=mongodb+srv://your-mongodb-uri `
  --env JWT_SECRET=your-jwt-secret `
  # ... additional environment variables ...
```

## Step 4: Monitor Deployment

Check the status of your deployment:

```bash
render services list
```

Get details for your specific service:

```bash
render services info --name miniproject-backend
```

View deployment logs:

```bash
render services logs --name miniproject-backend
```

## Step 5: Update Environment Variables (if needed)

To update environment variables after deployment:

```bash
render services env set --name miniproject-backend --key MONGODB_URI --value "your-new-mongodb-uri"
```

## Step 6: Trigger Manual Redeployment (if needed)

If you need to manually redeploy your service after updating code:

```bash
render services deploy --name miniproject-backend
```

## Troubleshooting

If you encounter issues, check the deployment logs:

```bash
render services logs --name miniproject-backend
```

For more detailed logs:

```bash
render services logs --name miniproject-backend --follow --all
```

## Additional Commands

- **Suspend Service**: `render services suspend --name miniproject-backend`
- **Resume Service**: `render services resume --name miniproject-backend`
- **Delete Service**: `render services delete --name miniproject-backend`

## References

For more information about Render CLI commands, visit: [Render CLI Documentation](https://render.com/docs/cli)

For API reference, visit: [Render API Documentation](https://api-docs.render.com)

