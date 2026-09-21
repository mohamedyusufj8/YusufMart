@echo off
setlocal enabledelayedexpansion

:: Navigate to project directory
cd /d "%~dp0"

echo ================================================================
echo    STARTING YUSUF MART + CLOUDFLARE PUBLIC TUNNEL LAUNCHER
echo    Anna University R2025 Semester 3 Deliverable
echo ================================================================
echo.

:: 1. Launch YusufMart Java Server in a dedicated window
echo [1/2] Starting YusufMart local server on port 8080...
start "YusufMart Server (DO NOT CLOSE)" cmd /c "run-app.bat"

:: 2. Wait for server to initialize
echo [2/2] Waiting 5 seconds for server to initialize...
timeout /t 5 /nobreak >nul

echo.
echo ================================================================
echo    CONNECTING TO CLOUDFLARE PUBLIC NETWORK...
echo ================================================================
echo.
echo Look for the link ending in .trycloudflare.com below:
echo.

if exist "cloudflared.exe" (
    .\cloudflared.exe tunnel --url http://localhost:8080
) else (
    echo [ERROR] cloudflared.exe not found in this folder!
    echo Please make sure cloudflared.exe is present in this directory.
    pause
)
