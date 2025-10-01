# Render Deployment Guide for MINIPROJECT.in

This guide provides step-by-step instructions for deploying the MINIPROJECT.in application to Render.

## Prerequisites

1. A Render account
2. A MongoDB Atlas account with a configured cluster
3. The MongoDB connection URI with database name

## Important Configuration Notes

### MongoDB Connection URI

Ensure that your MongoDB URI includes the database name. The format should be:

```bash
mongodb+srv://username:password@hostname/databasename?options
```

**Example:**

```bash
mongodb+srv://miniproject:password123@miniproject.nsaiyl1.mongodb.net/miniproject?retryWrites=true&w=majority
```

Note the `/miniproject` after the hostname, which specifies the database name. This is crucial for the application to function correctly.

## Deployment Steps

1. **Fork or Clone the Repository**
   - Clone this repository to your local machine or fork it on GitHub.

2. **Create a New Web Service on Render**
   - Log in to your Render dashboard.
   - Click on "New" and select "Web Service".
   - Connect your GitHub repository.

3. **Configure the Web Service**
   - Name: `miniproject-backend` (or your preferred name)
   - Environment: `Docker`
   - Region: Choose the closest region to your users
   - Branch: `main` (or your deployment branch)
   - Build Command: `./mvnw clean package -DskipTests`
   - Start Command: `java $JAVA_OPTS -jar target/*.jar`

4. **Set Environment Variables**
   - In the "Environment" section, add the following variables:
     - `MONGODB_URI`: Your MongoDB URI with database name
     - `JWT_SECRET`: Your JWT secret key
     - `MAIL_USERNAME`: Your email address
     - `MAIL_PASSWORD`: Your email password or app password
     - `STRIPE_SECRET_KEY`: Your Stripe secret key
     - `STRIPE_PUBLISHABLE_KEY`: Your Stripe publishable key
     - `STRIPE_WEBHOOK_SECRET`: Your Stripe webhook secret
     - Add other necessary environment variables as needed

5. **Deploy**
   - Click "Create Web Service".
   - Wait for the deployment to complete.

## Troubleshooting

If you encounter the error: `Database name must not be empty`, check your MongoDB URI format and ensure it includes the database name after the hostname and before the query parameters:

```bash
mongodb+srv://username:password@hostname/databasename?options
```

## Monitoring

- Monitor the deployment logs on Render for any errors.
- Use the application's health endpoints to verify the connection to MongoDB.

## Resources

- [Render Docs](https://render.com/docs)
- [MongoDB Atlas Connection Guide](https://docs.atlas.mongodb.com/connect-to-cluster/)