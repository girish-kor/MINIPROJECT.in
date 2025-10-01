# MINIPROJECT.in

![MINIPROJECT.in Logo](https://via.placeholder.com/150)

**Digital Product Marketplace**
_A secure platform for buying and selling digital products_

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Version](https://img.shields.io/badge/version-1.0.0-blue)
![License](https://img.shields.io/badge/license-MIT-green)

## Overview

MINIPROJECT.in is a modern digital marketplace built with Spring Boot and MongoDB. It provides a platform for securely purchasing and delivering digital products through integrated payment processing.

## Features

- **Secure Authentication** - Email-based OTP authentication
- **Digital Product Catalog** - Browse and purchase digital products
- **Stripe Integration** - Secure payment processing
- **Automated Delivery** - Email delivery of digital products
- **Rate Limiting** - Protection against abuse
- **API Documentation** - OpenAPI/Swagger integration

## Architecture

```text
├── Authentication Service
├── Product Service
├── Payment Processing (Stripe)
└── Delivery Service
```

## Tech Stack

- Java 21
- Spring Boot
- MongoDB
- JWT Authentication
- Stripe API
- Email Service

## Getting Started

1. Clone the repository
2. Copy `.env.example` to `.env` and configure your environment
3. Run `./mvnw spring-boot:run`

## API Documentation

Access the API documentation at `/swagger-ui.html` after starting the application.

## Deployment on Render

### Prerequisites

- A [Render](https://render.com) account
- A MongoDB database (you can use MongoDB Atlas)

### Deployment Steps

1. Fork this repository or push your code to your GitHub account
2. Log in to Render and select "New Web Service"
3. Connect your GitHub repository
4. Use the following settings:
   - Name: miniproject-backend (or your preferred name)
   - Region: Select closest to your users
   - Branch: main (or your default branch)
   - Root Directory: ./
   - Runtime: Docker
   - Plan: Free (or choose another plan for production)
5. Add the following environment variables in the Render dashboard:
   - MONGODB_URI: mongodb+srv://your-connection-string
   - JWT_SECRET: your-secret-key
   - STRIPE_SECRET_KEY: your-stripe-secret-key
   - STRIPE_PUBLISHABLE_KEY: your-stripe-publishable-key
   - STRIPE_WEBHOOK_SECRET: your-stripe-webhook-secret
   - MAIL_USERNAME: your-email
   - MAIL_PASSWORD: your-email-password
6. Click "Create Web Service"

## License

This project is licensed under the MIT License.
