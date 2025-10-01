# Quick Reference: Deploy to Render

## Option 1: Using PowerShell Script (Windows)

```powershell
# Deploy with our convenience script
.\deploy-to-render.ps1 -RenderApiKey "your_render_api_key"
```

## Option 2: Using Bash Script (Linux/macOS)

```bash
# Make script executable
chmod +x deploy-to-render.sh

# Deploy with our convenience script
./deploy-to-render.sh your_render_api_key
```

## Option 3: Manual Deployment with CLI

```bash
# Set API key
export RENDER_API_KEY="your_render_api_key"  # Linux/macOS
$env:RENDER_API_KEY="your_render_api_key"    # Windows PowerShell

# Deploy using render.yaml
render blueprint apply
```

## Option 4: Quick One-line Deployment (Windows PowerShell)

```powershell
$env:RENDER_API_KEY="your_render_api_key"; npm install -g @render/cli; render blueprint apply
```

## Option 5: Quick One-line Deployment (Linux/macOS)

```bash
export RENDER_API_KEY="your_render_api_key" && npm install -g @render/cli && render blueprint apply
```

See [RENDER_CLI_DEPLOYMENT.md](./RENDER_CLI_DEPLOYMENT.md) for detailed instructions and more CLI options.
