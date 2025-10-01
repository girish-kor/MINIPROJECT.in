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

- Java 17
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

## License

This project is licensed under the MIT License.
# MINIPROJECT.in
