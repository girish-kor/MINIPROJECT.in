# Deploying to Render

This guide provides step-by-step instructions for deploying the MINIPROJECT.in application to Render without using Docker.

## Prerequisites

1. A [Render](https://render.com) account
2. A MongoDB database (you can use [MongoDB Atlas](https://www.mongodb.com/cloud/atlas) for a free tier)
3. Your code pushed to a Git repository (GitHub, GitLab, etc.)

## Deployment Steps

### Step 1: Prepare Your Repository

Ensure your repository contains the following files:

- `Dockerfile` - For building the application
- `system.properties` - To specify Java version
- `render.yaml` - For Render configuration
- `Procfile` - As a fallback for some deployment scenarios

These files have already been added to your project.

### Step 2: Set Up MongoDB

1. Create a MongoDB Atlas account if you don't have one
2. Create a new cluster (free tier is sufficient for development)
3. Set up a database user with read/write permissions
4. Configure network access (preferably allowing access from anywhere for now)
5. Get your MongoDB connection string, it will look like:

   ```shell
   mongodb+srv://username:password@cluster0.mongodb.net/miniproject
   ```

### Step 3: Deploy to Render

1. Log in to your Render account
2. Click on "New" and select "Web Service"
3. Connect your repository by selecting the Git provider where your code is hosted
4. Find and select your repository
5. Configure the deployment:

   - **Name**: `miniproject-backend` (or your preferred name)
   - **Region**: Choose a region closest to your users
   - **Branch**: `main` (or your default branch)
   - **Root Directory**: Leave blank to use the repository root
   - **Runtime**: Select "Docker"
   - **Instance Type**: Free (or select paid options for production)

6. Click "Advanced" and add the following environment variables:

   | Key                    | Value                                                                     | Description                            |
   | ---------------------- | ------------------------------------------------------------------------- | -------------------------------------- |
   | MONGODB_URI            | mongodb+srv://...                                                         | Your MongoDB connection string         |
   | JWT_SECRET             | (secure random string)                                                    | Secret for JWT token signing           |
   | JWT_EXPIRATION         | 86400000                                                                  | Token expiration in milliseconds (24h) |
   | MAIL_HOST              | smtp.gmail.com                                                            | SMTP server for email                  |
   | MAIL_PORT              | 587                                                                       | SMTP port                              |
   | MAIL_USERNAME          | `your.email@example.com`                                                  | Email username                         |
   | MAIL_PASSWORD          | your-app-password                                                         | Email password/app password            |
   | STRIPE_SECRET_KEY      | sk\_...                                                                   | Stripe secret key                      |
   | STRIPE_PUBLISHABLE_KEY | pk\_...                                                                   | Stripe publishable key                 |
   | STRIPE_WEBHOOK_SECRET  | whsec\_...                                                                | Stripe webhook secret                  |
   | STRIPE_SUCCESS_URL     | `https://your-frontend-url/payment/success`                               | Payment success URL                    |
   | STRIPE_CANCEL_URL      | `https://your-frontend-url/payment/cancel`                                | Payment cancel URL                     |
   | OTP_EXPIRATION         | 300000                                                                    | OTP expiration in milliseconds (5m)    |
   | JAVA_OPTS              | -XX:+UseContainerSupport -Xmx512m -Djava.security.egd=file:/dev/./urandom | JVM options                            |

7. Click "Create Web Service"

Render will now:

1. Clone your repository
2. Build your application using the Dockerfile
3. Start your application
4. Assign a unique URL to your application

### Step 4: Verify Deployment

1. Wait for the deployment to complete (may take several minutes)
2. Once deployed, click on the URL provided by Render
3. Add `/swagger-ui.html` to the URL to access the API documentation

## Troubleshooting

If you encounter issues during deployment:

1. Check the Render logs for error messages
2. Verify your MongoDB connection string is correct and accessible
3. Ensure all environment variables are set correctly
4. Check if your application is configured to listen on the port provided by Render ($PORT)

## Important Notes

- The free tier of Render may hibernate your service after inactivity
- For production use, consider upgrading to a paid plan
- Set up automatic deployments by connecting Render to your repository for continuous deployment
