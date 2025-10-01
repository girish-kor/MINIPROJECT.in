@echo off
echo Building and preparing for Vercel deployment...

:: Build the project
call mvnw.cmd clean package -DskipTests

echo.
echo Project built successfully.
echo You can now deploy with the Vercel CLI using: vercel
