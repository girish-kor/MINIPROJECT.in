# Deploy script for Render
# Usage: .\deploy-to-render.ps1 -RenderApiKey <YOUR_RENDER_API_KEY>

param(
    [Parameter(Mandatory=$true)]
    [string]$RenderApiKey
)

# Set API key
$env:RENDER_API_KEY = $RenderApiKey
Write-Host "API key set successfully." -ForegroundColor Green

# Check if render CLI is installed
$renderInstalled = $null
try {
    $renderInstalled = Get-Command render -ErrorAction SilentlyContinue
} catch {
    $renderInstalled = $null
}

if (-not $renderInstalled) {
    Write-Host "Render CLI not found. Installing..." -ForegroundColor Yellow
    npm install -g @render/cli
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Failed to install Render CLI. Please install manually: npm install -g @render/cli" -ForegroundColor Red
        exit 1
    }
    Write-Host "Render CLI installed successfully." -ForegroundColor Green
}

# Deploy using render.yaml
Write-Host "Deploying to Render..." -ForegroundColor Cyan
render blueprint apply

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error during deployment. Check the output above for details." -ForegroundColor Red
    exit 1
}

# Check deployment status
Write-Host "Checking deployment status..." -ForegroundColor Cyan
render services list

Write-Host "Deployment process initiated. Check the Render dashboard for progress." -ForegroundColor Green
Write-Host "Once deployed, your service will be available at: https://miniproject-backend.onrender.com" -ForegroundColor Green