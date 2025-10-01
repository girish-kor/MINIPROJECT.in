# Deploy script for Render
# Usage: ./deploy-to-render.sh <YOUR_RENDER_API_KEY>

if [ -z "$1" ]; then
  echo "Error: Please provide your Render API key as a parameter."
  echo "Usage: ./deploy-to-render.sh <YOUR_RENDER_API_KEY>"
  exit 1
fi

# Set API key
export RENDER_API_KEY=$1
echo "API key set successfully."

# Check if render CLI is installed
if ! command -v render &> /dev/null; then
  echo "Render CLI not found. Installing..."
  npm install -g @render/cli
  echo "Render CLI installed successfully."
fi

# Deploy using render.yaml
echo "Deploying to Render..."
render blueprint apply

# Check deployment status
echo "Checking deployment status..."
render services list

echo "Deployment process initiated. Check the Render dashboard for progress."
echo "Once deployed, your service will be available at: https://miniproject-backend.onrender.com"
