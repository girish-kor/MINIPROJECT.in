#!/bin/bash
echo "Setting up the Vercel project..."

# Check if Vercel CLI is installed
if ! command -v vercel &> /dev/null; then
  echo "Vercel CLI not found. Installing..."
  npm install -g vercel
fi

echo ""
echo "Running initial Vercel setup. Follow the prompts:"
vercel login
vercel

echo ""
echo "Adding environment variables..."
vercel env add MONGODB_URI
vercel env add JWT_SECRET
vercel env add STRIPE_SECRET_KEY
vercel env add STRIPE_WEBHOOK_SECRET
vercel env add MAIL_HOST
vercel env add MAIL_PORT
vercel env add MAIL_USERNAME
vercel env add MAIL_PASSWORD

echo ""
echo "Setup complete."
echo "To deploy your application, run: vercel --prod"
