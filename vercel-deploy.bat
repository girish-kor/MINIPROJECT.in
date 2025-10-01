@echo off
echo Building and preparing for Vercel deployment...

:: Build the project with Vercel configuration
call mvnw.cmd clean package -DskipTests -Dspring.profiles.active=vercel

:: Copy the JAR to the expected location
copy target\in-0.0.1-SNAPSHOT.jar target\in-0.0.1-SNAPSHOT.jar

echo.
echo Project built successfully for Vercel.
